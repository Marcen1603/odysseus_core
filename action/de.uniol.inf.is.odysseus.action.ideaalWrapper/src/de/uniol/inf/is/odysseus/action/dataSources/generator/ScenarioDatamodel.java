package de.uniol.inf.is.odysseus.action.dataSources.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

public class ScenarioDatamodel {
	private GeneratorConfig config;
	
	private int nextToolID;
	
	private int currentFactory;
	private int machinesAssociatedToFactory;	
	private int machinesLeft;
	private List<Integer> unassociatedFactories;
	
	private List<Tool> tools;
	private SortedMap<Integer, Integer> toolsInUse;
	
	private List<Integer> freeMachines;
	
	private Map<Long, Integer> machineReleaseTimes;
	private Map<Long, Integer> machineDownTimes;
	
	private Random randomGen;
	
	private static ScenarioDatamodel instance = null;

	
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
	
	private ScenarioDatamodel(GeneratorConfig config) {
		this.nextToolID = 0;
		this.config = config;
			
		this.unassociatedFactories = new ArrayList<Integer>(config.getNumberOfBuildings()+1);
		this.machinesLeft = config.getNumberOfMachines();
		this.machinesAssociatedToFactory = 0;
		
		this.freeMachines = new Vector<Integer>(config.getNumberOfMachines()+1);
		this.machineReleaseTimes = Collections.synchronizedMap(
				new HashMap<Long, Integer>(config.getNumberOfMachines()+1));
		this.machineDownTimes = Collections.synchronizedMap(
				new HashMap<Long, Integer>(config.getNumberOfMachines()+1));
		
		this.tools = new Vector<Tool>(config.getNumberOfTools()+1);
		this.toolsInUse = Collections.synchronizedSortedMap(
				new TreeMap<Integer, Integer>());
		
		//fill tools	
		for (int i=0; i<this.tools.size(); i++){
			this.tools.add(this.generateTool());
		}
		
		this.randomGen = new Random();
	}
	
	/**
	 * Adds a new factory left to be associated with machines
	 * @param machineID
	 */
	public void addFactory(int factoryID){
		this.unassociatedFactories.add(factoryID);
	}
	
	/**
	 * Adds a new machine, free for installation
	 * @param machineID
	 */
	public void addMachine(int machineID){
		this.freeMachines.add(machineID);
	}
	
	/**
	 * Returns a factory which should be associated with a number of machines
	 * @param minMachines minimum number of machines per factory
	 * @return factoryID
	 */
	public int associateMachineToFactory(int minMachines){
		if (this.machinesAssociatedToFactory < 1){
			//remove factory from unassociated list
			this.currentFactory = this.unassociatedFactories.remove(this.unassociatedFactories.size()-1);
			
			if (this.unassociatedFactories.size() > 1){
				//determine a random number of machines to associate
				this.machinesAssociatedToFactory = this.randomGen.nextInt(
						this.machinesLeft - (minMachines * this.unassociatedFactories.size()) );
			}else {
				//last factory distribute rest of machines
				this.machinesAssociatedToFactory = this.machinesLeft;
			}
		}
		
		return this.currentFactory;
		
	}
	
	/**
	 * Generates a new tool
	 * @param limit1
	 * @param limit2
	 * @return
	 */
	private Tool generateTool(){
		if(this.nextToolID >= this.config.getNumberOfTools()){
			return null;
		}
		
		int limit1 = this.randomGen.nextInt((this.config.getMaxLimit1() - this.config.getMinLimit1()))
			+this.config.getMinLimit1();
		int limit2 = this.randomGen.nextInt((this.config.getMaxLimit2() - this.config.getMinLimit2()))
			+this.config.getMinLimit2();
		
		Tool tool = new Tool(limit1, limit2, this.nextToolID);
		this.tools.add(tool);
			
		this.nextToolID++;
		return tool;
	}
	
	public Integer getFreeMachine(){
		if(this.freeMachines.size() < 1){
			return null;
		}
		
		return this.freeMachines.remove(
				this.randomGen.nextInt(this.freeMachines.size()));
	}
	
	public Integer getOccupiedMachine(){
		if(this.toolsInUse.size() > 1){
			return this.toolsInUse.get(
					randomGen.nextInt(this.toolsInUse.size()));
		}else {
			return null;
		}
	}
	
	/**
	 * Returns a random tool which is not yet used by a machine.
	 * In addition this tool is removed from free tools list
	 * @return
	 * @throws GeneratorException 
	 */
	public Tool installFreeTool(int machineNo) throws GeneratorException{
		int toolAmount = this.tools.size();
		
		if (toolAmount < 1){
			throw new GeneratorException("No more tools avaialable");
		}
		
		if (toolAmount <= this.toolsInUse.size()){
			//all tools in use
			return null;
		}
		
		int toolID = this.randomGen.nextInt(toolAmount);
		
		//if tool is in use iterate until an unused is found
		while (this.toolsInUse.containsKey(toolID)){
			toolID += 1;
			toolID %= toolAmount;
		}
		
		this.toolsInUse.put(machineNo, toolID);
		this.setTimeForRelease(machineNo);
		
		return this.tools.get(toolID);
	}
	
	private void setTimeForRelease(int machineNo) {
		int minTime = this.config.getMinTimeOfUsageInOneMachine();
		int maxTime = this.config.getMaxTimeOfUsageInOneMachine();
		
		int time = this.randomGen.nextInt(maxTime-minTime)+minTime;
		this.machineReleaseTimes.put(System.currentTimeMillis()+time, machineNo);
	}

	/**
	 * Initialize instance for singleton interface
	 * @param config
	 */
	public static void initiDataModel(GeneratorConfig config){
		if (instance == null){
			instance = new ScenarioDatamodel(config);
		}
	}
	
	
	/**
	 * Uninstall tools and free ressources when usageTime is exceeded
	 */
	public void releaseResources(){
		//check for tools that must be installed
		Set<Long> times = this.machineReleaseTimes.keySet();
		synchronized (times) {
			Iterator<Long> iterator = times.iterator();
			while(iterator.hasNext()){
				long time = iterator.next();
				if (time <= System.currentTimeMillis()){
					//remove entry
					iterator.remove();
					
					//uninstall tool
					int machineID =	this.machineReleaseTimes.get(time);
					this.uninstallTool(machineID, time);
				}
			}
		}
		
		//check for machines that can be used again (downTime over)
		times = this.machineDownTimes.keySet();
		synchronized (times) {
			Iterator<Long> iterator = times.iterator();
			while(iterator.hasNext()){
				long time = iterator.next();
				if (time <= System.currentTimeMillis()){
					//remove entry
					iterator.remove();
					
					//machine is ready again
					this.freeMachines.add(this.machineDownTimes.get(time));
				}
			}
		}
	}
	
	/**
	 * Remove a tool which has reached its limit 
	 * @param toolID
	 */
	public void removeTool(int toolID){
		this.tools.remove(toolID);
	}
	
	/**
	 * Uninstalls a tool from a machine
	 * @param machineID
	 */
	public Integer uninstallTool(Integer machineID, long uninstallTimeStamp){
		//set machine to down status
		int minTime = this.config.getMinMachineDowntime();
		int maxTime = this.config.getMaxMachineDowntime();
		
		int time = this.randomGen.nextInt(maxTime-minTime)+minTime;
		this.machineDownTimes.put(uninstallTimeStamp+time, machineID);
		
		return this.toolsInUse.remove(machineID);
	}
	
	/**
	 * Use a tool and return if it can be used again
	 * @param toolID
	 * @param usageRate
	 * @return
	 */
	public boolean useTool(int machineNo, double usageRate){
		Tool tool = this.tools.get(this.toolsInUse.get(machineNo));
		tool.increaseUsageRate(usageRate);
		return !tool.isLimit2Hit();
		
	}

}
