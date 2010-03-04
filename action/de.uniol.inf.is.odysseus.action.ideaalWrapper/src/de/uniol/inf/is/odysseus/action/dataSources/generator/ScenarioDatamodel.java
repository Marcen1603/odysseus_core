package de.uniol.inf.is.odysseus.action.dataSources.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ScenarioDatamodel {
	private int highestToolID;
	private int maxToolID;
	
	private Map<Integer, Tool> tools;
	private List<Integer> freeTools;
	
	private Random randomGen;
	
	private static ScenarioDatamodel instance = null;

	
	private ScenarioDatamodel(GeneratorConfig config) {
		this.maxToolID = config.getNumberOfTools()-1;
		this.highestToolID = -1;
		
		//both data structures hold <numberOfMachines> tools at max!
		this.tools = new HashMap<Integer, Tool>(config.getNumberOfMachines()+1, 1.0f);
		this.freeTools = new ArrayList<Integer>(config.getNumberOfMachines()+1);
		
		this.randomGen = new Random();
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
	 * Returns a random tool which is not yet used by a machine.
	 * In addition this tool is removed from free tools list
	 * @return
	 */
	public Tool installFreeTool(){
		int toolID = this.randomGen.nextInt(this.freeTools.size());
		//remove from free list
		this.freeTools.remove(toolID);
		
		return this.tools.get(toolID);
	}
	
	
	/**
	 * Generates a new tool
	 * @param limit1
	 * @param limit2
	 * @return
	 */
	public Tool generateTool(int limit1, int limit2){
		Tool tool = new Tool(limit1, limit2);
		this.highestToolID++;
		
		if(this.highestToolID > this.maxToolID){
			return null;
		}
		
		synchronized (this.tools) {
			this.tools.put(this.highestToolID, tool);
		}
		synchronized(this.freeTools){
			this.freeTools.add(this.highestToolID);
		}
		return tool;
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
	 * Use a tool and return if it can be used again
	 * @param toolID
	 * @param usageRate
	 * @return
	 */
	public boolean useTool(int toolID, double usageRate){
		Tool tool = this.tools.get(toolID);
		tool.increaseUsageRate(usageRate);
		return !tool.isLimit2Hit();
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
