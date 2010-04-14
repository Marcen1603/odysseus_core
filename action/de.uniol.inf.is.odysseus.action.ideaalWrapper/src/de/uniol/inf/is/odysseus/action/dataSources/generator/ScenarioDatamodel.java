package de.uniol.inf.is.odysseus.action.dataSources.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class ScenarioDatamodel {
	private GeneratorConfig config;
	
	private int currentFactory;
	private int machinesToAssociatToFactory;	
	private int machinesLeft;
	private int factoryCounter;
	private List<Integer> unassociatedFactories;
	
	private Map<Integer, Tool> tools;
	private Map<Integer, Integer> toolsInUse;
	private List<Integer> avaiableToolIDs;
	private Vector<Integer> occupiedMachines;
	
	private List<Integer> freeMachines;
	
	private Map<Integer, Long> machineReleaseTimes;
	private Map<Integer, Long> machineDownTimes;
	
	private Boolean noFactoryProducedYet;
	
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
		this.config = config;
			
		this.unassociatedFactories = new ArrayList<Integer>(config.getNumberOfBuildings()+1);
		this.machinesLeft = config.getNumberOfMachines();
		this.machinesToAssociatToFactory = 0;
		
		this.freeMachines = new Vector<Integer>(config.getNumberOfMachines()+1);
		this.machineReleaseTimes =	new HashMap<Integer, Long>(config.getNumberOfMachines()+1);
		this.machineDownTimes = new HashMap<Integer, Long>(config.getNumberOfMachines()+1);
		
		this.tools = Collections.synchronizedMap(
				new HashMap<Integer, Tool>(config.getNumberOfTools()+1));
		this.toolsInUse = Collections.synchronizedMap(
				new HashMap<Integer, Integer>(config.getNumberOfMachines()+1));
		this.avaiableToolIDs = new Vector<Integer>(this.tools.size());
		this.occupiedMachines = new Vector<Integer>(this.tools.size());
		
		this.randomGen = new Random();
		
		//fill tools	
		for (int i=0; i<config.getNumberOfTools(); i++){
			this.tools.put(i, this.generateTool(i));
			this.avaiableToolIDs.add(i);
		}
		
		this.noFactoryProducedYet = true;
		this.factoryCounter = config.getNumberOfBuildings();
		
	}
	
	/**
	 * Adds a new factory left to be associated with machines
	 * @param machineID
	 */
	public void addFactory(int factoryID){
		synchronized (this.noFactoryProducedYet) {
			this.noFactoryProducedYet = false;
		}
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
	public Integer associateMachineToFactory(int minMachines){
		synchronized (this.noFactoryProducedYet) {
			if (this.noFactoryProducedYet){
				//no factories generated yet
				return null;
			}
		}
		
		if (this.machinesToAssociatToFactory < 1 && this.unassociatedFactories.size() > 0){
			//remove factory from unassociated list
			this.currentFactory = this.unassociatedFactories.remove(this.unassociatedFactories.size()-1);
			
			//determine a random number of machines to associate
			this.machinesToAssociatToFactory = this.randomGen.nextInt(this.machinesLeft - (minMachines * factoryCounter)) 
					+minMachines;
			this.factoryCounter--;
		}
		this.machinesToAssociatToFactory--;
		this.machinesLeft--;
		return this.currentFactory;
		
	}
	
	/**
	 * Generates a new tool
	 * @param limit1
	 * @param limit2
	 * @return
	 */
	private Tool generateTool(int id){
		int limit1 = this.randomGen.nextInt((this.config.getMaxLimit1() - this.config.getMinLimit1()))
			+this.config.getMinLimit1();
		int limit2 = this.randomGen.nextInt((this.config.getMaxLimit2() - this.config.getMinLimit2()))
			+this.config.getMinLimit2();
		
		Tool tool = new Tool(limit1, limit2, id);
		return tool;
	}
	
	public Integer getFreeMachine(){
		if(this.freeMachines.size() < 1){
			return null;
		}
		
		return this.freeMachines.remove(
				this.randomGen.nextInt(this.freeMachines.size()));
	}
	
	public Integer getOccupiedMachine() throws GeneratorException{
		if (this.tools.size() < 1){
			throw new GeneratorException("All tools used.");
		}
		
		if(this.occupiedMachines.size() > 0){
			return this.occupiedMachines.get(randomGen.nextInt(this.occupiedMachines.size()));
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
		
		if (toolAmount <= this.occupiedMachines.size()){
			//all tools in use, free machine again
			this.freeMachines.add(machineNo);
			return null;
		}
			
		int index = this.randomGen.nextInt(this.avaiableToolIDs.size());
		
		Tool tool = this.tools.get(this.avaiableToolIDs.remove(index));
		
		this.occupiedMachines.add(machineNo);
		this.toolsInUse.put(machineNo, tool.getId());
		
		this.setTimeForRelease(machineNo);
		
		return tool;
	}
	
	private void setTimeForRelease(int machineNo) {
		int minTime = this.config.getMinTimeOfUsageInOneMachine();
		int maxTime = this.config.getMaxTimeOfUsageInOneMachine();
		
		int time = this.randomGen.nextInt(maxTime-minTime)+minTime;
		
		synchronized (this.machineReleaseTimes) {
			this.machineReleaseTimes.put(machineNo, System.currentTimeMillis()+time);
		}
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
		synchronized (this.machineReleaseTimes) {
			Iterator<Integer> iterator = this.machineReleaseTimes.keySet().iterator();
			while(iterator.hasNext()){
				int machineID = iterator.next();
				long time = this.machineReleaseTimes.get(machineID);
				if (time <= System.currentTimeMillis()){	
					//uninstall tool
					this.uninstallTool(machineID, time);
					
					//remove entry
					iterator.remove();
				}
			}
			
		}
		
		//check for machines that can be used again (downTime over)
		synchronized (this.machineDownTimes) {
			Iterator<Integer> iterator = this.machineDownTimes.keySet().iterator();
			while(iterator.hasNext()){
				int machineID = iterator.next();
				long time = this.machineDownTimes.get(machineID);
				if (time <= System.currentTimeMillis()){
					//machine is ready again
					
				this.freeMachines.add(machineID);
				//remove entry
				iterator.remove();

	
				}
			}
		}
	}
	
	
	/**
	 * Uninstalls a tool from a machine
	 * @param machineID
	 */
	public void uninstallTool(Integer machineID, long uninstallTimeStamp){
		//set machine to down status
		int minTime = this.config.getMinMachineDowntime();
		int maxTime = this.config.getMaxMachineDowntime();
		
		int time = this.randomGen.nextInt(maxTime-minTime)+minTime;
		synchronized (machineDownTimes) {
			this.machineDownTimes.put(machineID, uninstallTimeStamp+time);
		}

		this.occupiedMachines.remove(machineID);
		Tool tool = this.tools.get(this.toolsInUse.remove(machineID));
	
		//tool could have been removed by other thread
		if (tool != null){
			this.avaiableToolIDs.add(tool.getId());	
		}
		
	}
	
	/**
	 * Use a tool. Uninstalls/Deletes tool if necessary
	 * @param toolID
	 * @return
	 */
	public Double useTool(int machineNo){

			Integer index = this.toolsInUse.get(machineNo);
			
			if (index != null){
				double usageRate = Math.random() * (this.config.getMaxUsageRate() - this.config.getMinUsageRate());
				usageRate += this.config.getMinUsageRate();
				
				Tool tool = this.tools.get(index);
				if (tool != null){
					tool.increaseUsageRate(usageRate);
					
					//check if tool must be uninstalled and removed
					if (tool.isLimit2Hit()){
						synchronized (this.machineReleaseTimes) {
							this.machineReleaseTimes.remove(machineNo);
						}
						this.uninstallTool(machineNo, System.currentTimeMillis());
						
						
						this.avaiableToolIDs.remove(index);
						this.tools.remove(index);
					}
	
					return usageRate;
				}else {
					return null;
				}
			}else {
				//can happen if tool was released during execution 
				return null;
			}

	}
		
	public static void reset() {
		synchronized (instance) {
			instance = new ScenarioDatamodel(instance.config);
		}
	}

}
