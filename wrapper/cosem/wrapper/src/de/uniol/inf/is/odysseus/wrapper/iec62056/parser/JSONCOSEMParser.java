package de.uniol.inf.is.odysseus.wrapper.iec62056.parser;

import java.io.IOException;
import java.io.InputStreamReader;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;

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
 *</pre>
 * @author Jens Plümer
 *
 */
public class JSONCOSEMParser extends AbstractCOSEMParser {

	private JsonParser jp;
	private int attrCount = 0;
	private boolean inProgress;
	private String SMGW_TOKEN_VALUE;

	protected final String[] DELIMETER = {
			"[",
			"|",
			"]"
	};
	
	public JSONCOSEMParser(InputStreamReader reader, SDFSchema sdfSchema) {
		super(reader, sdfSchema);
		setTokens(new String[] {
				"logical_name", 
				"objects", 
				"value", 
				"unit", 
				"scaler", 
				"status", 
				"capture_time",
				"logical_name"
				}, 
			2);
	}

	@Override
	protected void init(InputStreamReader reader) {
		super.reader = reader;
		try {
			jp = new JsonFactory().createParser(reader);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected String next() {
		String TOKEN;
		try {
			if (!inProgress) {
				inProgress = true;
				jp.nextToken();
				while (jp.nextToken() == JsonToken.FIELD_NAME) {
					jp.nextToken();
					if (TOKENS[0].equals((TOKEN = jp.getCurrentName()))) {
						SMGW_TOKEN_VALUE = DELIMETER[0] + jp.getText();
						System.out.println("SMGW_TOKEN_VALUE " + SMGW_TOKEN_VALUE);
					} else if (TOKENS[1].equals(TOKEN)) {
						return appendNextValue();
					}
				}
			} else {
				jp.nextToken();
				return appendNextValue();
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

	private String appendNextValue() {
		StringBuilder builder = new StringBuilder(SMGW_TOKEN_VALUE);
		try {
			while (jp.nextToken() == JsonToken.START_OBJECT) {
				while (jp.nextToken() != JsonToken.END_OBJECT) {
					String ATTR_TOKEN = jp.getCurrentName();
					jp.nextToken();
					for (int i = attrOffset; i < TOKENS.length; i++) {
						if (TOKENS[i].equals(ATTR_TOKEN)) {
							attrCount++;
							builder.append(DELIMETER[1] + jp.getText());
						}
						if (ATTRIBUTE_NAMES.length - 1 == attrCount) {
							attrCount = 0;
							return builder.append(DELIMETER[2]).toString();
						}
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
