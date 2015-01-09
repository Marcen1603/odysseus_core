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

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;

public class OdysseusSimulator {

	static Logger LOG = LoggerFactory.getLogger(OdysseusSimulator.class);
	
	private AbstractProtocolHandler<?> pHandler;
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

	public OdysseusSimulator(AbstractProtocolHandler<?> pHandler, String name) {
		this.pHandler = pHandler;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> create(int num, String model, Map<String, Object> modelParams) throws Exception {
		LOG.debug("create");
		
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
        return entities;
	}

	public Map<String, Object> getData(Map<String, List<String>> arg0)
			throws Exception {
		LOG.debug("getData");
		
		Map<String, Object> result = new HashMap<String, Object>();
		return result;
//		throw new Exception("Odysseus has no data");
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> init(String sid, Map<String, Object> simParams) throws Exception {
		LOG.debug("init");
		
		this.sid = sid;
        if (simParams.containsKey("step_size")) {
            this.stepSize = ((Number) simParams.get("step_size")).intValue();
        }
		time = 0;
        return OdysseusSimulator.meta;
	}

	public long step(long arg0, Map<String, Object> input) throws Exception {
		LOG.debug("step");
		
		String[] inputString = new String[1];
		inputString[0] = new ObjectMapper().writeValueAsString(input);
		pHandler.process(inputString);
//		String mapAsJson = new ObjectMapper().writeValueAsString(input);
//		ByteBuffer wrapped = ByteBuffer.wrap(mapAsJson.getBytes());
//		wrapped.position(wrapped.limit());
//		pHandler.process(wrapped);
//		tHandler.fireProcess(wrapped);
		time += stepSize;
		return time;
	}
}
