package de.uniol.inf.is.odysseus.scars.xmlprofiler.profiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.metadata.Probability;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListMetadataTypes;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.PredictionExpression;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.PredictionFunctionContainer;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class ScarsXMLProfiler {

	private static Map<String, ScarsXMLProfiler> instances = new HashMap<String, ScarsXMLProfiler>();

	private Map<String, Integer> operatorCycleCounts;
	private Map<String, Integer> currentOperatorSkips;

	private Element root;
	private int numCycle;
	private int numSkips;
//	private int currentSkips;
	private String file;
	private boolean finish = false;


	private ScarsXMLProfiler(String file, int numBeginSkips, int numCycle) {
		this.file =  file.replace("..", File.separator);
		this.numCycle = numCycle;
		this.numSkips = numBeginSkips;
		root = new Element("ROOT");
		operatorCycleCounts = new HashMap<String, Integer>();
		currentOperatorSkips = new HashMap<String, Integer>();
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

	public synchronized void  profile(String operator, SDFAttributeList schema, MVRelationalTuple<?> scan) {
		if(finish) {
			return;
		}
		Element operatorElement = getOperatorElement(operator);
		
		int currentSkips = currentOperatorSkips.get(operator);
		if(currentSkips < numSkips) {
			currentOperatorSkips.put(operator, ++currentSkips);
			return;
		}
		
		
		
		int currentCycleCount = operatorCycleCounts.get(operator);

		Element scanRootElement = new Element("ScanRoot");
		operatorElement.addContent(scanRootElement);
		if(schema != null && schema instanceof SDFAttributeListExtended) {
			Object pfc = ((SDFAttributeListExtended) schema).getMetadata(SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS);
			if(pfc != null) {
				addPredictionFunctionContainer(scanRootElement, (PredictionFunctionContainer<?>)pfc);
			}
		}
		addMetadata(scanRootElement, scan);
		for(int i=0; i<schema.getAttributeCount(); i++) {
			addData2(schema, scanRootElement, schema.getAttribute(i), scan.getAttribute(i));
		}

		operatorCycleCounts.put(operator, ++currentCycleCount);
		updateFinish();

		if(finish) {
			saveFile();
		}
	}
	
	private void updateFinish() {
		finish = true;
		for(Integer cycle : operatorCycleCounts.values()) {
			if(cycle < numCycle) {
				finish = false;
			}
		}


	}
	
	private void saveFile() {
		XMLOutputter op = new XMLOutputter(Format.getPrettyFormat());
		try {
			op.output(root,  new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void  profilePunctuation(String operator, PointInTime timestamp) {
		
		if(finish) {
			return;
		}
		Element operatorElement = getOperatorElement(operator);
		int currentSkips = currentOperatorSkips.get(operator);
		if(currentSkips < numSkips) {
			currentOperatorSkips.put(operator, ++currentSkips);
			return;
		}
		

		Element punctuationElement = new Element("punctuation");
		operatorElement.addContent(punctuationElement);
		punctuationElement.setAttribute("time", timestamp.toString());
		int currentCycleCount = operatorCycleCounts.get(operator);
		operatorCycleCounts.put(operator, ++currentCycleCount);
		
		updateFinish();

		if(finish) {
			saveFile();
		}
	}

	public void addData2(SDFAttributeList rootschema, Element parent, SDFAttribute attr, Object value) {
		if(value instanceof MVRelationalTuple<?>) {
			
			MVRelationalTuple<?> tuple = (MVRelationalTuple<?>)value;
			Element tupleElement = new Element(attr.getAttributeName());
			addMetadata(tupleElement, tuple);
			parent.addContent(tupleElement);
			
			

			
			if(attr.getDatatype().getQualName().equals("Record")) {
				
				SDFAttributeList schema = attr.getSubattributes();
				for(int i=0; i<schema.getAttributeCount(); i++) {
					SDFAttribute childAttr = schema.getAttribute(i);
					try {
						addData2(null, tupleElement, childAttr, tuple.getAttribute(i));
					} catch(ArrayIndexOutOfBoundsException e) {
						tupleElement.addContent(new Element("ERROR").setText("schema/tuple different: " + childAttr.getAttributeName() + ", tuple index: " + i));
					}

				}
			} else if (attr.getDatatype().getQualName().equals("List")) {
				
				SDFAttributeList schema = attr.getSubattributes();
				SDFAttribute childAttr = schema.getAttribute(0);
				for(int i=0; i<tuple.getAttributeCount(); i++) {
					addData2(null, tupleElement, childAttr, tuple.getAttribute(i));
				}
			}
		} else {
			parent.setAttribute(attr.getAttributeName(), value.toString());
		}
	}
	
	public void addPredictionFunctionContainer(Element parent, PredictionFunctionContainer<?> c) {
		Element predFuncContainerElement = new Element("PREDICTION_FUNCTION_CONTAINER");
		for(Entry<?, ?> e : c.getMap().entrySet()) {
			Element predFuncElement = new Element("PREDICTION_FUNCTION");
			predFuncElement.setAttribute("where", e.getKey().toString());
			addPredictionFunction(predFuncElement, (IPredictionFunction<?>)e.getValue());
			predFuncContainerElement.addContent(predFuncElement);
		}
		parent.addContent(predFuncContainerElement);
	}
	
	public void addPredictionFunction(Element parent, IPredictionFunction<?> p) {
		for(PredictionExpression exp : p.getExpressions()) {
			Element expElement = new Element("Expression");
			expElement.setAttribute("target", exp.getTargetAttributeName());
			expElement.setAttribute("exp", exp.getExpression());
			parent.addContent(expElement);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addMetadata(Element parent, MVRelationalTuple<?> tuple) {
		Object metadata = tuple.getMetadata();
		Element metaDataElement = new Element("METADATA");
		if(metadata != null) {
			if(metadata instanceof IProbability) {
				this.addProbability(metaDataElement, (IProbability) metadata);
			}
			if(metadata instanceof IPredictionFunctionKey) {
				this.addPredictionFunctionKey(metaDataElement, (IPredictionFunctionKey<IPredicate<MVRelationalTuple<?>>>) metadata);
			}
			if(metadata instanceof IConnectionContainer) {
				this.addConnectionList(metaDataElement, (IConnectionContainer) metadata);
			}
			if(metadata instanceof IGain) {
				this.addGain(metaDataElement, (IGain) metadata);
			}
			if(metadata instanceof ITimeInterval) {
				this.addTimeInterval(metaDataElement, (ITimeInterval) metadata);
			}
			if(metadata instanceof ILatency) {
				this.addLatency(metaDataElement, (ILatency) metadata);
			}
			if(metadata instanceof IApplicationTime) {
				
			}
		}
		parent.addContent(metaDataElement);
	}

//	public void addDataFromList(Element parent, SDFAttributeList schema, MVRelationalTuple<?> tuple) {
//		for(int listIndex=0; listIndex<tuple.getAttributeCount(); listIndex++) {
//			addData(parent, schema, tuple.getAttribute(listIndex));
//		}
//	}

	public void addProbability(Element parent, IProbability tuple) {
		double[][] cov = tuple.getCovariance();
		
		Element covEle = new Element("COVARIANCEMATRIX");
		parent.addContent(covEle);
		
		if(cov == null) {
			covEle.setText("null");
			return;
		}
		
		String covText = "";


		for(int r = 0; r<cov.length; r++) {
			covText += " \n\t";
			for(int c = 0; c<cov[0].length; c++) {
				covText += cov[r][c] + "\t";
			}
		}
		covEle.setText(covText);
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
		for(IConnection con : conList) {
			Element conEle = new Element("CONNECTION");
			conEle.setAttribute("rating", String.valueOf(con.getRating()));

			Element conEleOldPath = new Element("OLDPATH");
			Element conEleNewPath = new Element("NEWPATH");

			String conEleOldPathText = con.getLeftPath().toString();
			String conEleNewPathText = con.getRightPath().toString();

			conEleOldPath.setText(conEleOldPathText);
			conEleNewPath.setText(conEleNewPathText);

			conEle.addContent(conEleOldPath);
			conEle.addContent(conEleNewPath);

			conListEle.addContent(conEle);
			
		}
		parent.addContent(conListEle);
	}

	public void addGain(Element parent, IGain gain) {
		Element gainElement = new Element("GAIN");
		double[][] g = gain.getGain();
		String gainText = "";
		if(g != null) {
			for(int r=0; r<g.length; r++) {
				gainText +="\n\t";
				for(int c=0; c<g[0].length; c++) {
					gainText += g[r][c] + "\t";
				}
			}
		}
		gainElement.setText(gainText);
		parent.addContent(gainElement);
	}

	public void addTimeInterval(Element parent, ITimeInterval ti) {
		Element tiElement = new Element("TIME_INTERVAL");
		tiElement.setAttribute("start", ti.getStart() == null ? "null" : ti.getStart().toString());
		tiElement.setAttribute("end", ti.getEnd() == null ? "null" : ti.getEnd().toString());
		parent.addContent(tiElement);
	}
	
	public void addLatency(Element parent, ILatency latency) {
		Element latencyElement = new Element("LATENCY");
		latencyElement.setAttribute("latency", String.valueOf(latency.getLatency()));
		latencyElement.setAttribute("start", String.valueOf(latency.getLatencyStart()));
		latencyElement.setAttribute("end", String.valueOf(latency.getLatencyEnd()));
		parent.addContent(latencyElement);
	}
	
	public void addApplicationTime(Element parent, IApplicationTime appTime) {
		Element appTimeElement = new Element("APPLICATION_TIME");
		appTimeElement.setText(appTime.getAllApplicationTimeIntervals().toString());
		parent.addContent(appTimeElement);
	}
	
	public Element getOperatorElement(String name) {
		
		Element e = root.getChild(name);
		if(e == null) {
			e = new Element(name);
			currentOperatorSkips.put(name, 0);
			operatorCycleCounts.put(name, 0);
			root.addContent(e);
		}
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

//	private static<M extends IProbability> MVRelationalTuple<M> createTimeTuple() {
//		MVRelationalTuple<M> time = new MVRelationalTuple<M>(1);
//		time.setAttribute(0, 10);
//		return time;
//	}

}
