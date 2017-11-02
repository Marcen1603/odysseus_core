package de.uniol.inf.is.odysseus.wrapper.iec62056.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * Parser for parsing JSON-formated COSEM objects in the following manner:
 * 
 * <pre>
 * { "logical_name": "smartmetergateway_0", 
 *   "objects": 
 *  [
 * 	 {"value": 0.972, "unit": 30, "scaler": -3, "status": "000", "capture_time": 0, "logical_name": "sm_011"},
 * 	 {"value": 0.345, "unit": 30, "scaler": -3, "status": "000", "capture_time": 1, "logical_name": "sm_012"},
 * 	 {"value": 0.465, "unit": 30, "scaler": -3, "status": "000", "capture_time": 2, "logical_name": "sm_013"},
 * 	 {"value": 0.986, "unit": 30, "scaler": -3, "status": "000", "capture_time": 3, "logical_name": "sm_014"},
 * 	 ...
 *  ]
 * }
 * </pre>
 * 
 * @author Jens Plümer
 *
 */
public class JSONCOSEMParser extends AbstractCOSEMParser {

	// private static final Logger logger = LoggerFactory.getLogger(JSONCOSEMParser.class.getSimpleName());

	public JSONCOSEMParser(InputStreamReader reader, StringBuilder builder, String[] tokens) {
		super(reader, builder, tokens);
	}

	private String rootElement;
	private JsonNode rootNode;
	private JsonNode attributesNode;
	private String serverDeviceName;
	private int currentNodeIndex;
	private int maxNodeIndex;
	
	@Override
	protected String next() {
		if(currentNodeIndex < maxNodeIndex) {
			setValue(tokens[tokens.length - 1], serverDeviceName);
			JsonNode attributes = attributesNode.path(currentNodeIndex);
			Iterator<String> fieldNames = attributes.fieldNames();
			for (JsonNode attribute : attributes) {
				setValue(fieldNames.next(), attribute.asText());
			}
			currentNodeIndex++;
			return getStringRepresentation();
		} else {
			close();
		}
		return null;
	}
	
	@Override
	protected void init(InputStreamReader reader) {
		super.reader = reader;
		try {
			rootNode = new ObjectMapper().readTree(new JsonFactory().createParser(reader));
			rootElement = rootNode.fields().next().getKey();
			serverDeviceName = rootNode.get(rootElement).path(tokens[0]).asText();
			attributesNode = rootNode.get(rootElement).path(tokens[1]);
			maxNodeIndex = attributesNode.size();
			currentNodeIndex = 0;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
