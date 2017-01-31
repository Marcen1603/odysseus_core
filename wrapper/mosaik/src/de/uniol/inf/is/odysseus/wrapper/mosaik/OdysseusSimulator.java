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

import de.offis.mosaik.api.Simulator;

public class OdysseusSimulator extends Simulator {

	static Logger LOG = LoggerFactory.getLogger(OdysseusSimulator.class);
	
	private MosaikProtocolHandler<?> pHandler;
	private long time;
	@SuppressWarnings("unused")
	private String sid;
	private int stepSize = 1;
	
	// Alternatively, put all the JSON into a .json file and read meta data from
    // that.
    private static final JSONObject meta = (JSONObject) JSONValue.parse(("{"
            + "    'api_version': '" + Simulator.API_VERSION + "',"
            + "    'models': {"
            + "        'Odysseus': {" 
            + "            'public': true,"
            + "			   'any_inputs': true,"
            + "            'params': ['init_val'],"
            + "            'attrs': []" 
            + "        }"
            + "    }" + "}").replace("'", "\""));

	public OdysseusSimulator(MosaikProtocolHandler<?> pHandler, String name) {
		super(name);
		this.pHandler = pHandler;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> create(int num, String model, Map<String, Object> modelParams) throws Exception {
		if(num > 1) {
			throw new Exception("You can create only one odysseus model per simulator.");
		}
        JSONArray entities = new JSONArray();
        String eid = "odysseus_0";

        JSONObject entity = new JSONObject();
        entity.put("eid", eid);
        entity.put("type", model);
        entity.put("rel", new JSONArray());
        entities.add(entity);
        
        return entities;
	}

	@Override
	public Map<String, Object> getData(Map<String, List<String>> arg0)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> init(String sid, Map<String, Object> simParams) throws Exception {
		this.sid = sid;
        if (simParams.containsKey("step_size")) {
            this.stepSize = ((Number) simParams.get("step_size")).intValue();
        }
		time = 0;
        return OdysseusSimulator.meta;
	}

	@Override
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
	
	@Override
    public void setupDone() throws Exception {
//		Map<String, Object> relatedEntities = this.getMosaik().getRelatedEntities();
//		System.out.println("SetupDone(): show related Entities:");
//		for(Entry<String, Object> entry:relatedEntities.entrySet()) {
//			System.out.println("	" + entry.getKey() + " - " + entry.getValue());
//		}
	}
}
