package de.uniol.inf.is.odysseus.wrapper.iec62056.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

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
public class JSONCOSEMParser<T extends IStreamObject<IMetaAttribute>> extends AbstractCOSEMParser<T> {

	// private static final Logger logger = LoggerFactory.getLogger(JSONCOSEMParser.class.getSimpleName());

	public JSONCOSEMParser(InputStream inputStream, SDFSchema schema, String rootNode) {
		super(inputStream, schema, rootNode);
	}

	private String rootElement;
	private JsonNode root;
	private JsonNode objectsNode;
	private int currentNodeIndex;
	private int maxNodeIndex;
	
	@Override
	protected T next() {
		if(currentNodeIndex < maxNodeIndex) {
			JsonNode attributes = objectsNode.path(currentNodeIndex);
			Iterator<String> fieldNames = attributes.fieldNames();
			for (JsonNode attribute : attributes) {
				setValue(fieldNames.next(), attribute.asText());
			}
			
			currentNodeIndex++;
			return getTuple();
		} else {
			close();
		}
		return null;
	}
	
	@Override
	protected void init(InputStream inputStream) {
		super.inputStream = inputStream;
		try {
			root = new ObjectMapper().readTree(new JsonFactory().createParser(new InputStreamReader(inputStream)));
			rootElement = root.fields().next().getKey();
			objectsNode = root.get(rootElement).path(super.rootNode);
			maxNodeIndex = objectsNode.size();
			currentNodeIndex = 0;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
