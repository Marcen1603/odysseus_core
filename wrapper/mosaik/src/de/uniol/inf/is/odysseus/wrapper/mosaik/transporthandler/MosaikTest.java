package de.uniol.inf.is.odysseus.wrapper.mosaik.transporthandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import de.offis.mosaik.api.SimProcess;
import de.offis.mosaik.api.Simulator;

public class MosaikTest extends Simulator {
	
	private int stepSize = 1;
	private int idCounter = 0;
	
	// Alternatively, put all the JSON into a .json file and read meta data from
    // that.
    private static final JSONObject meta = (JSONObject) JSONValue.parse(("{"
            + "    'api_version': " + Simulator.API_VERSION + ","
            + "    'models': {"
            + "        'Odysseus': {" + "            'public': true,"
            + "            'params': ['init_val'],"
            + "            'attrs': ['val_in', 'val_out']" + "        }"
            + "    }" + "}").replace("'", "\""));
    private final HashMap<String, MosaikTest> instances;
	
    public static void main(String[] args) throws Throwable {
    	final ServerSocket serverSocket;
    	serverSocket = new ServerSocket(5554);
    	System.out.println("Server gestartet");
    	Socket cs = serverSocket.accept();
    	System.out.println("Client accepted");
    	
        Simulator sim = new MosaikTest("Odysseus");
        String[] args2 = new String[1];
        args2[0] = "127.0.0.1:5554";
        SimProcess.startSimulation(cs, sim);
    }

	public MosaikTest(String arg0) {
		super(arg0);
		this.instances = new HashMap<String, MosaikTest>();
	}

	@Override
	public List<Map<String, Object>> create(int arg0, String arg1,
			Map<String, Object> arg2) throws Exception {
		return null;
	}

	@Override
	public Map<String, Object> getData(Map<String, List<String>> arg0)
			throws Exception {
		throw new Exception("Odysseus has no data");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> init(String sid, Map<String, Object> simParams) throws Exception {
        if (simParams.containsKey("step_size")) {
            this.stepSize = ((Number) simParams.get("step_size")).intValue();
        }
        return MosaikTest.meta;
	}

	@Override
	public long step(long arg0, Map<String, Object> arg1) throws Exception {
		return 0;
	}

}
