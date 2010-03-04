package de.uniol.inf.is.odysseus.action.dataSources.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScenarioDatamodel {
	private int highestToolID;
	private int maxToolID;
	
	private Map<Integer, Tool> tools;
	private List<Integer> freeTools;
	
	private static ScenarioDatamodel instance = null;

	
	private ScenarioDatamodel(GeneratorConfig config) {
		this.maxToolID = config.getNumberOfTools()-1;
		this.highestToolID = -1;
		
		//both data structures hold <numberOfMachines> tools at max!
		this.tools = new HashMap<Integer, Tool>(config.getNumberOfMachines()+1, 1.0f);
		this.freeTools = new ArrayList<Integer>(config.getNumberOfMachines()+1);
	}
	
	/**
	 * Initialize instance for singleton interface
	 * @param config
	 */
	public void initiDataModel(GeneratorConfig config){
		if (instance == null){
			instance = new ScenarioDatamodel(config);
		}
	}
	
	/**
	 * Generate a new tool
	 * @param toolID
	 * @param MachineID
	 */
	public int generateTool(int limit1, int limit2){
		Tool tool = new Tool(limit1, limit2);
		this.highestToolID++;
		synchronized (this.tools) {
			this.tools.put(this.highestToolID, tool);
		}
		synchronized(this.freeTools){
			this.freeTools.add(this.highestToolID);
		}
		return this.highestToolID;
	}
	
	/**
	 * Remove a tool which has reached its limit 
	 * @param toolID
	 */
	public void removeTool(int toolID){
		synchronized (this.tools) {
			this.tools.remove(toolID);
		}
	}
	
	/**
	 * Singleton interface. Instance is not automatically initialized!
	 * This must be done before.
	 * @return
	 * @throws GeneratorException
	 */
	public static ScenarioDatamodel getInstance() throws GeneratorException {
		if (instance != null){
			return instance;
		}
		throw new GeneratorException("Datamodel not initialized");
	}

}
