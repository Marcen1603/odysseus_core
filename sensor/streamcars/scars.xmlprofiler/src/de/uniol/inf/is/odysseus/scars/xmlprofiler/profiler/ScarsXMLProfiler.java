package de.uniol.inf.is.odysseus.scars.xmlprofiler.profiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.metadata.Probability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class ScarsXMLProfiler {

	private static Map<String, ScarsXMLProfiler> instances = new HashMap<String, ScarsXMLProfiler>();

	private Map<String, Integer> operatorCycleCounts;

	private Element root;
	private int numCycle;
	private int numSkips;
	private String file;


	private ScarsXMLProfiler(String file, int numBeginSkips, int numCycle) {
		this.file =  file.replace("..", File.separator);
		this.numCycle = numCycle;
		this.numSkips = numBeginSkips;
		root = new Element("ROOT");
		operatorCycleCounts = new HashMap<String, Integer>();
	}

	public Element getRoot() {
		return root;
	}

	public static synchronized ScarsXMLProfiler getInstance(String file, int numBeginSkips, int numCycle) {
		if(instances.get(file) == null) {
			ScarsXMLProfiler p  = new ScarsXMLProfiler(file, numBeginSkips, numCycle);
			instances.put(file, p);
			return p;
		}
		return instances.get(file);
	}
	
	private boolean finish = false;

	public void profile(String operator, SDFAttributeList schema, MVRelationalTuple<?> scan) {
		if(finish) {
			return;
		}
		Element operatorElement = getOperatorElement(operator);
		Element scanElement = new Element("SCAN_RESULT");
		
		operatorElement.addContent(scanElement);
		addData(scanElement, schema, scan);

		finish = true;
		for(Integer cycle : operatorCycleCounts.values()) {
			if(cycle <= numCycle) {
				finish = false;
			}
		}

		if(finish) {
			XMLOutputter op = new XMLOutputter(Format.getPrettyFormat());
			try {

				op.output(root,  new FileOutputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void addData(Element parent, SDFAttributeList schema,  Object tuple) {
		for(int index=0; index<schema.getAttributeCount(); index++) {
			SDFAttribute attr = schema.get(index);


			if(attr.getDatatype().getQualName().equals("Record")) {
				String name = attr.getAttributeName();
				Element e = new Element(name);
				parent.addContent(e);

				addData(e, attr.getSubattributes(), ((MVRelationalTuple<?>)tuple).getAttribute(index));

			} else if(attr.getDatatype().getQualName().equals("List")) {
				String name = attr.getAttributeName();
				Element e = new Element(name);
				parent.addContent(e);
				Element metaDataElement = new Element("METADATA");
				MVRelationalTuple<?> mvrTuple = (MVRelationalTuple<?>) tuple;

				if(mvrTuple.getMetadata() instanceof IProbability) {
					this.addProbability(metaDataElement, (IProbability) mvrTuple.getMetadata());
				}
				if(mvrTuple.getMetadata() instanceof IPredictionFunctionKey) {
					this.addPredictionFunctionKey(metaDataElement, (IPredictionFunctionKey<IPredicate<MVRelationalTuple<?>>>) mvrTuple.getMetadata());
				}
				if(mvrTuple.getMetadata() instanceof IConnectionContainer) {
					this.addConnectionList(metaDataElement, (IConnectionContainer) mvrTuple.getMetadata());
				}

				e.addContent(metaDataElement);
				addDataFromList(e, attr.getSubattributes(), (MVRelationalTuple<?>)((MVRelationalTuple<?>)tuple).getAttribute(index));
			} else if(tuple instanceof MVRelationalTuple) {
				String attrName = attr.getAttributeName();
				Object value = ((MVRelationalTuple<?>)tuple).getAttribute(index);
				parent.setAttribute(attrName, value.toString());
			} else {
				String attrName = attr.getAttributeName();
				Object value = tuple;
				parent.setAttribute(attrName, value.toString());
			}



		}
	}

	public void addDataFromList(Element parent, SDFAttributeList schema, MVRelationalTuple<?> tuple) {
		for(int listIndex=0; listIndex<tuple.getAttributeCount(); listIndex++) {
			addData(parent, schema, (MVRelationalTuple<?>)tuple.getAttribute(listIndex));
		}


	}

	public void addProbability(Element parent, IProbability tuple) {
		double[][] cov = tuple.getCovariance();
		RealMatrix covMatrix = new RealMatrixImpl(cov);
		String covText = "";
		for(int i=0; i<covMatrix.getRowDimension(); i++) {
			double[] row = covMatrix.getRow(i);
			for(int j=0; j<row.length; j++) {
				covText += row[j];
			}
			if(i != covMatrix.getRowDimension()-1) {
				covText += "\n";
			}
		}
		Element covEle = new Element("COVARIANCEMATRIX");
		covEle.setText(covText);
		parent.addContent(covEle);
	}

	public void addPredictionFunctionKey(Element parent, IPredictionFunctionKey<IPredicate<MVRelationalTuple<?>>> predictionFunctionKey) {
		Element pfkEle = new Element("PREDICTIONFUNCTIONKEY");
		String pfkText = predictionFunctionKey.toString();
		pfkEle.setText(pfkText);
		parent.addContent(pfkEle);
	}

	public void addConnectionList(Element parent, IConnectionContainer connectionContainer) {
		Element conListEle = new Element("CONNECTIONLIST");
		ConnectionList conList = connectionContainer.getConnectionList();
		for(Connection con : conList) {
			Element conEle = new Element("CONNECTION");
			conEle.setAttribute("rating", String.valueOf(con.getRating()));

			Element conEleOldPath = new Element("OLDPATH");
			Element conEleNewPath = new Element("NEWPATH");

			String conEleOldPathText = "";
			String conEleNewPathText = "";

			for(int i=0; i<con.getLeftPath().length; i++) {
				conEleOldPathText += con.getLeftPath()[i];
				if(i != con.getLeftPath().length-1) {
					conEleOldPathText += ", ";
				}
			}

			for(int i=0; i<con.getRightPath().length; i++) {
				conEleNewPathText += con.getRightPath()[i];
				if(i != con.getRightPath().length-1) {
					conEleNewPathText += ", ";
				}
			}

			conEleOldPath.setText(conEleOldPathText);
			conEleNewPath.setText(conEleNewPathText);

			conEle.addContent(conEleOldPath);
			conEle.addContent(conEleNewPath);

			conListEle.addContent(conEle);
		}
	}

	public void setAttribute(String operator, String name, String value) {
		Element e = getOperatorElement(operator);
		e.setAttribute(name, value);
	}

	public Element getOperatorElement(String name) {
		Element e = root.getChild(name);
		if(e == null) {
			e = new Element(name);
			operatorCycleCounts.put(name, 1);
			root.addContent(e);
		}
		int cycle = operatorCycleCounts.get(name);
		operatorCycleCounts.put(name, ++cycle);
		return e;
	}


	public static void main(String[] args) {
		ScarsXMLProfiler p = ScarsXMLProfiler.getInstance("D:/test.xml", 0, 2);

		SDFAttributeList scan = createScanSchema();
		MVRelationalTuple<IProbability> scanTuple = createScanTuple(scan);
		p.profile("OPERATOR", scan, scanTuple);
		p.profile("OPERATOR", scan, scanTuple);
		Element root = p.getRoot();
		XMLOutputter op = new XMLOutputter(Format.getPrettyFormat());
		try {
			op.output(root, System.out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	private static SDFAttributeList createScanSchema() {
		SDFAttributeList scan = new SDFAttributeList();

		SDFAttribute list = new SDFAttribute("a.list");

		list.setDatatype(SDFDatatypeFactory.getDatatype("List"));

		SDFAttribute obj = new SDFAttribute("obj");
		obj.setDatatype(SDFDatatypeFactory.getDatatype("Record"));

		SDFAttribute pos = new SDFAttribute("pos");
		pos.setDatatype(SDFDatatypeFactory.getDatatype("Record"));

		List<List<?>> cov = createObjectCovarianz();

		SDFAttribute x = new SDFAttribute("x");
		x.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		x.setCovariance((ArrayList<?>)cov.get(0));

		SDFAttribute y = new SDFAttribute("y");
		y.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		y.setCovariance((ArrayList<?>)cov.get(1));

		SDFAttribute z = new SDFAttribute("z");
		z.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		z.setCovariance((ArrayList<?>)cov.get(2));

		SDFAttribute speed = new SDFAttribute("speed");
		speed.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		speed.setCovariance((ArrayList<?>)cov.get(3));

		SDFAttribute time = new SDFAttribute("a.scanTime");
		time.setDatatype(SDFDatatypeFactory.getDatatype("Long"));

		scan.add(list);
		scan.add(time);
		list.addSubattribute(obj);
		obj.addSubattribute(pos);
		pos.addSubattribute(x);
		pos.addSubattribute(y);
		pos.addSubattribute(z);
		obj.addSubattribute(speed);

//		System.out.println(obj.getAttributeName());
		return scan;
	}



	private static<M extends IProbability> MVRelationalTuple<M> createScanTuple(SDFAttributeList schema) {
		MVRelationalTuple<M>[] objList = createObjectList();

		MVRelationalTuple<M> list = new MVRelationalTuple<M>(objList.length);
		for(int index=0; index<objList.length; index++) {
			list.setAttribute(index, objList[index]);

		}

		MVRelationalTuple<M> scan = new MVRelationalTuple<M>(2);
		scan.setAttribute(0, list);
		scan.setAttribute(1, 1);

		return scan;
	}

	@SuppressWarnings("unchecked")
	private static<M extends IProbability> MVRelationalTuple<M> createObjectTuple() {
		MVRelationalTuple<M> obj = new MVRelationalTuple<M>(2);

		MVRelationalTuple<M> pos = new MVRelationalTuple<M>(3);
		pos.setAttribute(0, 1.0);
		pos.setAttribute(1, 1.0);
		pos.setAttribute(2, 0.0);

		obj.setAttribute(0, pos);
		obj.setAttribute(1, 45.0);


		double[][] cov = new double[4][4];
		cov[0][0] = 0.5;
		cov[0][1] = 0.5;
		cov[0][2] = 0.5;
		cov[0][3] = 0.5;

		cov[1][0] = 0.5;
		cov[1][1] = 0.5;
		cov[1][2] = 0.5;
		cov[1][3] = 0.5;

		cov[2][0] = 0.5;
		cov[2][1] = 0.5;
		cov[2][2] = 0.5;
		cov[2][3] = 0.5;

		cov[3][0] = 0.5;
		cov[3][1] = 0.5;
		cov[3][2] = 0.5;
		cov[3][3] = 0.5;

		ArrayList<int[]> paths = new ArrayList<int[]>();
		paths.add(new int[] {0, 0, 0, 0});
		paths.add(new int[] {0, 0, 0, 1});
		paths.add(new int[] {0, 0, 0, 2});
		paths.add(new int[] {0, 0, 1});

		Probability p = new Probability();
		p.setCovariance(cov);
		p.setAttributePaths(paths);
		obj.setMetadata((M) p);

		return obj;
	}


	private static List<List<?>> createObjectCovarianz() {
		List<List<?>> cov = new ArrayList<List<?>>(4);
		ArrayList<Double> row0 = new ArrayList<Double>(4);
		row0.add(0.5);
		row0.add(0.5);
		row0.add(0.5);
		row0.add(0.5);
		cov.add(row0);

		ArrayList<Double> row1 = new ArrayList<Double>(4);
		row1.add(0.5);
		row1.add(0.5);
		row1.add(0.5);
		row1.add(0.5);
		cov.add(row1);

		ArrayList<Double> row2 = new ArrayList<Double>(4);
		row2.add(0.5);
		row2.add(0.5);
		row2.add(0.5);
		row2.add(0.5);
		cov.add(row2);

		ArrayList<Double> row3 = new ArrayList<Double>(4);
		row3.add(0.5);
		row3.add(0.5);
		row3.add(0.5);
		row3.add(0.5);
		cov.add(row3);

		return cov;
	}

	private static<M extends IProbability> MVRelationalTuple<M>[] createObjectList() {
		@SuppressWarnings("unchecked")
		MVRelationalTuple<M>[] list = new MVRelationalTuple[4];
		list[0] = createObjectTuple();
		list[1] = createObjectTuple();
		list[2] = createObjectTuple();
		list[3] = createObjectTuple();
		return list;
	}

	private static<M extends IProbability> MVRelationalTuple<M> createTimeTuple() {
		MVRelationalTuple<M> time = new MVRelationalTuple<M>(1);
		time.setAttribute(0, 10);
		return time;
	}

}
