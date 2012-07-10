/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.operator.xmlprofiler.profiler;

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

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFSchemaExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFSchemaMetadataTypes;
import de.uniol.inf.is.odysseus.scars.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.scars.metadata.PredictionExpression;
import de.uniol.inf.is.odysseus.scars.metadata.PredictionFunctionContainer;

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

	public synchronized void  profile(String operator, SDFSchema schema, MVTuple<?> scan) {
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
		if(schema != null && schema instanceof SDFSchemaExtended) {
			Object pfc = ((SDFSchemaExtended) schema).getMetadata(SDFSchemaMetadataTypes.PREDICTION_FUNCTIONS);
			if(pfc != null) {
				addPredictionFunctionContainer(scanRootElement, (PredictionFunctionContainer<?>)pfc);
			}
		}
		addMetadata(scanRootElement, scan);
		for(int i=0; i<schema.size(); i++) {
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

	public void addData2(SDFSchema rootschema, Element parent, SDFAttribute attr, Object value) {
		if(value instanceof MVTuple<?>) {
			
			MVTuple<?> tuple = (MVTuple<?>)value;
			Element tupleElement = new Element(attr.getAttributeName());
			addMetadata(tupleElement, tuple);
			parent.addContent(tupleElement);
			
			

			
			if(attr.getDatatype().getQualName().equals("Record")) {
				
				SDFSchema schema = attr.getDatatype().getSchema();
				for(int i=0; i<schema.size(); i++) {
					SDFAttribute childAttr = schema.getAttribute(i);
					try {
						addData2(null, tupleElement, childAttr, tuple.getAttribute(i));
					} catch(ArrayIndexOutOfBoundsException e) {
						tupleElement.addContent(new Element("ERROR").setText("schema/tuple different: " + childAttr.getAttributeName() + ", tuple index: " + i));
					}

				}
			} else if (attr.getDatatype().getQualName().equals("List")) {
				
				if(attr.getDatatype().hasSchema()){
					SDFSchema schema = attr.getDatatype().getSchema();
					SDFAttribute childAttr = schema.getAttribute(0);
					for(int i=0; i<tuple.size(); i++) {
						addData2(null, tupleElement, childAttr, tuple.getAttribute(i));
					}
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
	public void addMetadata(Element parent, MVTuple<?> tuple) {
		Object metadata = tuple.getMetadata();
		Element metaDataElement = new Element("METADATA");
		if(metadata != null) {
			if(metadata instanceof IProbability) {
				this.addProbability(metaDataElement, (IProbability) metadata);
			}
			if(metadata instanceof IPredictionFunctionKey) {
				this.addPredictionFunctionKey(metaDataElement, (IPredictionFunctionKey<IPredicate<MVTuple<?>>>) metadata);
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

//	public void addDataFromList(Element parent, SDFSchema schema, MVTuple<?> tuple) {
//		for(int listIndex=0; listIndex<tuple.size(); listIndex++) {
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

	public void addPredictionFunctionKey(Element parent, IPredictionFunctionKey<IPredicate<MVTuple<?>>> predictionFunctionKey) {
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


//	public static void main(String[] args) {
//		ScarsXMLProfiler p = ScarsXMLProfiler.getInstance("D:/test.xml", 0, 2);
//
//		SDFSchema scan = createScanSchema();
//		MVTuple<IProbability> scanTuple = createScanTuple(scan);
//		p.profile("OPERATOR", scan, scanTuple);
//		p.profile("OPERATOR", scan, scanTuple);
//		Element root = p.getRoot();
//		XMLOutputter op = new XMLOutputter(Format.getPrettyFormat());
//		try {
//			op.output(root, System.out);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//
//
//	private static SDFSchema createScanSchema() {
//		SDFSchema scan = new SDFSchema();
//
//		SDFAttribute list = new SDFAttribute("a.list");
//
//		list.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("List"));
//
//		SDFAttribute obj = new SDFAttribute("obj");
//		obj.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("Record"));
//
//		SDFAttribute pos = new SDFAttribute("pos");
//		pos.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("Record"));
//
//		List<List<?>> cov = createObjectCovarianz();
//
//		SDFAttribute x = new SDFAttribute("x");
//		x.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("MV"));
//		x.setCovariance((ArrayList<?>)cov.get(0));
//
//		SDFAttribute y = new SDFAttribute("y");
//		y.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("MV"));
//		y.setCovariance((ArrayList<?>)cov.get(1));
//
//		SDFAttribute z = new SDFAttribute("z");
//		z.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("MV"));
//		z.setCovariance((ArrayList<?>)cov.get(2));
//
//		SDFAttribute speed = new SDFAttribute("speed");
//		speed.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("MV"));
//		speed.setCovariance((ArrayList<?>)cov.get(3));
//
//		SDFAttribute time = new SDFAttribute("a.scanTime");
//		time.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("Long"));
//
//		scan.add(list);
//		scan.add(time);
//		list.addSubattribute(obj);
//		obj.addSubattribute(pos);
//		pos.addSubattribute(x);
//		pos.addSubattribute(y);
//		pos.addSubattribute(z);
//		obj.addSubattribute(speed);
//
////		System.out.println(obj.getAttributeName());
//		return scan;
//	}


	@SuppressWarnings("unused")
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

}
