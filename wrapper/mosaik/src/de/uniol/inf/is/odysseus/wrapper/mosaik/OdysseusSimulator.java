package de.uniol.inf.is.odysseus.wrapper.mosaik;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OdysseusSimulator {

	static Logger LOG = LoggerFactory.getLogger(OdysseusSimulator.class);
	
	private MosaikProtocolHandler<?> pHandler;
	private long time;
	@SuppressWarnings("unused")
	private String sid;
	private int stepSize = 1;
	private int idCounter = 0;
	
	// Alternatively, put all the JSON into a .json file and read meta data from
    // that.
    private static final JSONObject meta = (JSONObject) JSONValue.parse(("{"
            + "    'api_version': 2.0,"
            + "    'models': {"
            + "        'Odysseus': {" + "            'public': true,"
            + "			   'any_inputs': true,"
            + "            'params': ['init_val'],"
            + "            'attrs': []" + "        }"
            + "    }" + "}").replace("'", "\""));

	public OdysseusSimulator(MosaikProtocolHandler<?> pHandler, String name) {
		this.pHandler = pHandler;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> create(int num, String model, Map<String, Object> modelParams) throws Exception {
        JSONArray entities = new JSONArray();
        for (int i = 0; i < num; i++) {
            String eid = "odysseus_" + (this.idCounter + i);

            JSONObject entity = new JSONObject();
            entity.put("eid", eid);
            entity.put("type", model);
            entity.put("rel", new JSONArray());
            entities.add(entity);
        }
        this.idCounter += num;
//		this.pHandler.sendRequest = true;
        return entities;
	}

	public Map<String, Object> getData(Map<String, List<String>> arg0)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		return result;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> init(String sid, Map<String, Object> simParams) throws Exception {
		this.sid = sid;
        if (simParams.containsKey("step_size")) {
            this.stepSize = ((Number) simParams.get("step_size")).intValue();
        }
		time = 0;
        return OdysseusSimulator.meta;
	}

	public long step(long arg0, Map<String, Object> input) throws Exception {
		String[] inputString = new String[1];
		input.put("timestamp", time);
		if(this.pHandler.cleanStrings) {
			inputString[0] = new ObjectMapper().writeValueAsString(input).replace("_", "-");
		} else {
			inputString[0] = new ObjectMapper().writeValueAsString(input);
		}
		pHandler.process(inputString);
		time += stepSize;
		return time;
	}
}
