package de.uniol.inf.is.odysseus.action.dataSources.generator;

import java.util.Properties;

public class GeneratorConfig {

	private long frequencyOfUpdates;
	private double accelerationFactor;
	private int numberOfBuildings;
	private int numberOfMachines;
	private int numberOfTools;
	private int minNumberOfMachinesPerBuilding;
	private int minTimeOfUsageInOneMachine;
	private int maxTimeOfUsageInOneMachine;
	private double minUsageRate;
	private double maxUsageRate;
	private int minLimit1;
	private int maxLimit1;
	private int minLimit2;
	private int maxLimit2;
	private int minMachineDowntime;
	private int maxMachineDowntime;
	private boolean simulateDB;

	public GeneratorConfig(Properties props){
		this.frequencyOfUpdates = Long.valueOf(props.getProperty("frequencyOfUpdates"));
		this.accelerationFactor = Double.valueOf(props.getProperty("accelerationFactor"));
		
		this.numberOfBuildings = Integer.valueOf(props.getProperty("numberOfBuildings"));
		this.numberOfMachines = Integer.valueOf(props.getProperty("numberOfMachines"));
		this.numberOfTools = Integer.valueOf(props.getProperty("numberOfTools"));
		this.minNumberOfMachinesPerBuilding = Integer.valueOf(props.getProperty("minNumberOfMachinesPerBuilding"));
		
		this.minTimeOfUsageInOneMachine = Integer.valueOf(props.getProperty("minTimeOfUsageInOneMachine"));
		this.maxTimeOfUsageInOneMachine = Integer.valueOf(props.getProperty("maxTimeOfUsageInOneMachine"));
		
		this.minUsageRate = Double.valueOf(props.getProperty("minUsageRate"));
		this.maxUsageRate = Double.valueOf(props.getProperty("maxUsageRate"));
		this.minLimit1 = Integer.valueOf(props.getProperty("minLimit1"));
		this.maxLimit1 = Integer.valueOf(props.getProperty("maxLimit1"));
		this.minLimit2 = Integer.valueOf(props.getProperty("minLimit2"));
		this.maxLimit2 = Integer.valueOf(props.getProperty("maxLimit2"));
		
		this.minMachineDowntime = Integer.valueOf(props.getProperty("minMachineDowntime"));
		this.maxMachineDowntime = Integer.valueOf(props.getProperty("maxMachineDowntime"));
		
		this.simulateDB = Boolean.valueOf(props.getProperty("simulateDB"));
	}

	public long getFrequencyOfUpdates() {
		return frequencyOfUpdates;
	}

	public void setFrequencyOfUpdates(long frequencyOfUpdates) {
		this.frequencyOfUpdates = frequencyOfUpdates;
	}

	public double getAccelerationFactor() {
		return accelerationFactor;
	}

	public void setAccelerationFactor(double accelerationFactor) {
		this.accelerationFactor = accelerationFactor;
	}

	public int getNumberOfBuildings() {
		return numberOfBuildings;
	}

	public void setNumberOfBuildings(int numberOfBuildings) {
		this.numberOfBuildings = numberOfBuildings;
	}

	public int getNumberOfMachines() {
		return numberOfMachines;
	}

	public void setNumberOfMachines(int numberOfMachines) {
		this.numberOfMachines = numberOfMachines;
	}

	public int getNumberOfTools() {
		return numberOfTools;
	}

	public void setNumberOfTools(int numberOfTools) {
		this.numberOfTools = numberOfTools;
	}

	public int getMinNumberOfMachinesPerBuilding() {
		return minNumberOfMachinesPerBuilding;
	}

	public void setMinNumberOfMachinesPerBuilding(int minNumberOfMachinesPerBuilding) {
		this.minNumberOfMachinesPerBuilding = minNumberOfMachinesPerBuilding;
	}

	public int getMinTimeOfUsageInOneMachine() {
		return minTimeOfUsageInOneMachine;
	}

	public void setMinTimeOfUsageInOneMachine(int minTimeOfUsageInOneMachine) {
		this.minTimeOfUsageInOneMachine = minTimeOfUsageInOneMachine;
	}

	public int getMaxTimeOfUsageInOneMachine() {
		return maxTimeOfUsageInOneMachine;
	}

	public void setMaxTimeOfUsageInOneMachine(int maxTimeOfUsageInOneMachine) {
		this.maxTimeOfUsageInOneMachine = maxTimeOfUsageInOneMachine;
	}

	public double getMinUsageRate() {
		return minUsageRate;
	}

	public void setMinUsageRate(double minUsageRate) {
		this.minUsageRate = minUsageRate;
	}

	public double getMaxUsageRate() {
		return maxUsageRate;
	}

	public void setMaxUsageRate(double maxUsageRate) {
		this.maxUsageRate = maxUsageRate;
	}

	public int getMinLimit1() {
		return minLimit1;
	}

	public void setMinLimit1(int minLimit1) {
		this.minLimit1 = minLimit1;
	}

	public int getMaxLimit1() {
		return maxLimit1;
	}

	public void setMaxLimit1(int maxLimit1) {
		this.maxLimit1 = maxLimit1;
	}

	public int getMinLimit2() {
		return minLimit2;
	}

	public void setMinLimit2(int minLimit2) {
		this.minLimit2 = minLimit2;
	}

	public int getMaxLimit2() {
		return maxLimit2;
	}

	public void setMaxLimit2(int maxLimit2) {
		this.maxLimit2 = maxLimit2;
	}

	public int getMinMachineDowntime() {
		return minMachineDowntime;
	}

	public void setMinMachineDowntime(int minMachineDowntime) {
		this.minMachineDowntime = minMachineDowntime;
	}

	public int getMaxMachineDowntime() {
		return maxMachineDowntime;
	}

	public void setMaxMachineDowntime(int maxMachineDowntime) {
		this.maxMachineDowntime = maxMachineDowntime;
	}

	public boolean isSimulateDB() {
		return simulateDB;
	}

	public void setSimulateDB(boolean simulateDB) {
		this.simulateDB = simulateDB;
	}
}
