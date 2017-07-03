package de.uniol.inf.is.odysseus.wrapper.iec62056.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class JSONCOSEMParser extends AbstractCOSEMParser<String>{

	private static final String SMWG         = "logical_name";
	private static final String DEVICES      = "ldevs";
	private static final String DEVICE_NAME  = "logical_name";
	private static final String ATTRIBUTES   = "objects";
	private static final String ATTRIBUTE_1  = "value";
	private static final String ATTRIBUTE_2  = "status";
	private static final String ATTRIBUTE_3  = "scalar";
	private static final String ATTRIBUTE_4  = "unit";
	private static final String ATTRIBUTE_5  = "capture_time";
	private static final String ATTRIBUTE_6  = "logical_name";
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
//		ObjectMapper m = new ObjectMapper();
//		try {
//			CosemData cosem = m.readValue(new File("output_example.json"), CosemData.class);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println("objectMapper: " + (System.currentTimeMillis() - startTime) + " ms");
		startTime = System.currentTimeMillis();
		try {
			JSONCOSEMParser p = new JSONCOSEMParser(new InputStreamReader(new FileInputStream(new File("output_example2.json"))), 6);
			Iterator<String> iter = p.process();
			////////
			while(iter.hasNext()) {
				System.out.println("--> " + iter.next());
			}
			iter = p.process();
			while(iter.hasNext()) {
				System.out.println("--> " + iter.next());
			}
			iter = p.process();
			while(iter.hasNext()) {
				System.out.println("--> " + iter.next());
			}
			iter = p.process();
			while(iter.hasNext()) {
				System.out.println("--> " + iter.next());
			}
			iter = p.process();
			while(iter.hasNext()) {
				System.out.println("--> " + iter.next());
			}
			////////
			iter = p.process();
//			while(iter.hasNext()) {
//				System.out.println("--> " + iter.next());
			System.out.println("inProgress= " + JSONCOSEMParser.inProgress);
//			}
			iter = p.process();
			System.out.println(iter);
//			while(iter.hasNext()) {
//				System.out.println("--> " + iter.next());
//			}
			System.out.println("inProgress= " + JSONCOSEMParser.inProgress);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("streamParsing: " + (System.currentTimeMillis() - startTime) + " ms");
	}
	
	private JsonParser jp;
	
	public JSONCOSEMParser(InputStreamReader reader, SDFSchema sdfSchema) {
		super(reader, sdfSchema);
		try {
			jp = new JsonFactory().createParser(reader);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public JSONCOSEMParser(InputStreamReader reader, int i) {
		super(reader, i);
		try {
			jp = new JsonFactory().createParser(reader);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String logical_name_SMGW = null;
	static boolean inProgress = false;
	
	public Iterator<String> process() {
		try {
			if(!inProgress) {
				inProgress = true;
				jp.nextToken();
				while (jp.nextToken() == JsonToken.FIELD_NAME) {
					String fName = jp.getCurrentName();
					jp.nextToken();
					if (SMWG.equals(fName)) {
						logical_name_SMGW = jp.getText();
					} else if (ATTRIBUTES.equals(fName)) {
						return iterateAttributes();
					}
				}
			} else {
				jp.nextToken();
				return iterateAttributes();
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Iterator<String> iterateAttributes() {
		List<String> list = new LinkedList<>();
		try {
			while (jp.nextToken() == JsonToken.START_OBJECT) {
				list.add(logical_name_SMGW);
				while (jp.nextToken() != JsonToken.END_OBJECT) {
					String attribute_value = jp.getCurrentName();
					jp.nextToken();
					switch (attribute_value) {
					case (ATTRIBUTE_1):
					case (ATTRIBUTE_2):
					case (ATTRIBUTE_3):
					case (ATTRIBUTE_4):
					case (ATTRIBUTE_5):
					case (ATTRIBUTE_6):
						list.add(jp.getText());
						break;
					default:
						break;
					}
					if(list.size() == ATTRIBUTE_SIZE) {
						Iterator<String> iter = list.iterator();
						list = new LinkedList<>();
						return iter;
					}
				}
			}
			inProgress = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
