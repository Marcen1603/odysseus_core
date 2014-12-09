package de.uniol.inf.is.odysseus.generator.process.util;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.generator.DataTuple;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public class XESLogDataImporter {
	
	public final static String TRACE = "trace";
	public final static String STRING = "string";
	public final static String VALUE = "value";
	public final static String EVENT = "event";
	private final static String FILEPATH = "/home/phil/Documents/eventlogs/Chapter_7/Lfull.xes"; // checked
	private final static String FILEPATH2 = "/home/phil/Documents/eventlogs/inductiveminer_log_double_ending.xes"; //checked
	private final static String FILEPATH3 = "/home/phil/Documents/eventlogs/singleTrace.xes";
	private final static String FILE_LOSSY_TEST = "/home/phil/Documents/eventlogs/lossycounter_testlog.xes";
	private final static String FILE_FINANCIAL_LOG = "/home/phil/Documents/eventlogs/financial_log.xes";
	private final static String FILE_PRINTER_LOG = "/home/phil/Documents/eventlogs/drucker.xes";
	private final static String FILE_ETM_LOG = "/home/phil/Documents/eventlogs/ETM_Configuration1.xes";
	private static Map<String,Integer> traceCounter = new HashMap();
	
	
	public static List<DataTuple> getEvents(){
		List<DataTuple> eventTuples = Lists.newArrayList();
		
		File document = new File(FILEPATH);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(document);
			doc.getDocumentElement().normalize();		 
				// test counter
			NodeList nlist = doc.getDocumentElement().getElementsByTagName(TRACE);
	        for (int i = 0; i < nlist.getLength(); i++){
	        	// Trace Elemente
	        	StringBuilder keyBuilder = new StringBuilder();
	        	StringBuilder caseBuilder = new StringBuilder();
	        	Element element = (Element)nlist.item(i);
	        	
	        	Element caseID = (Element)element.getElementsByTagName(STRING).item(0);
	        	String caze =null;
	        	if(caseID != null) {
	        		caze = caseID.getAttribute(VALUE);
	        		
	        	}
	        	
	        	NodeList eventTags = element.getElementsByTagName(EVENT);
	        	keyBuilder.append("[");
	        	caseBuilder.append("<");
	        	for(int j =0; j< eventTags.getLength(); j++){
	        		Element eventTag =((Element)eventTags.item(j));
	        		
	        		NodeList eventData = eventTag.getElementsByTagName(STRING);
	        		for(int k =0; k<eventData.getLength();k++){
	        			Element dataElement = ((Element)eventData.item(k));
	        			if(dataElement.getAttribute("key").equals("concept:name")){
	        				// Aktivitätsname des Events
	        				keyBuilder.append(dataElement.getAttribute(VALUE));
	        				caseBuilder.append(dataElement.getAttribute(VALUE)+",");
	        				eventTuples.add(createTuple(caze, dataElement.getAttribute(VALUE)));
	        			}
	        		}   		
	        	}
	        	keyBuilder.append("]");
	        	caseBuilder.append(">");
	        	incMap(caseBuilder.toString());

	        } 
	        int count=0;
//	        for(Map.Entry e : traceCounter.entrySet()){
//	        	System.out.println(e.getKey()+ ": "+e.getValue());
//	        	count++;
//	        }
	        System.out.println(traceCounter);
			 System.out.println("Gesamt: "+ count);

			 count = 0;
				 
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ImmutableList.copyOf(eventTuples);
	}

	private static void incMap(String key){
		int freq=0;
		if(traceCounter.containsKey(key)){
			freq = traceCounter.get(key).intValue();
			traceCounter.remove(key);
			traceCounter.put(key, freq+1);	
		} else {
			traceCounter.put(key, 1);	
		}		
	}
	
	private static DataTuple createTuple(String caseID,String activityname){
		DataTuple tuple = new DataTuple();
		tuple.addString(caseID);
		tuple.addString(activityname);
		return tuple;
	}

}
