package de.uniol.inf.is.odysseus.costmodel.operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.OdysseusDefaults;

public class OperatorCostModelCfg {

	private static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(OperatorCostModelCfg.class);
		}
		return _logger;
	}

	private static OperatorCostModelCfg instance;

	public static OperatorCostModelCfg getInstance() {
		if (instance == null)
			instance = new OperatorCostModelCfg();
		return instance;
	}

	private static final double STD_MEM_COST = 4;
	private static final double STD_CPU_COST = 0.0001;
	private static final double MEM_HEADROOM = 0.4;
	private static final double CPU_HEADROOM = 0.4;
	
	private double stdMemCost;
	private double stdCpuCost;
	private double memHeadroom;
	private double cpuHeadroom;
	
	private OperatorCostModelCfg() {
		stdMemCost = toDouble( OdysseusDefaults.get("ac_standardMemCost"), STD_MEM_COST);
		stdCpuCost = toDouble( OdysseusDefaults.get("ac_standardCpuCost"), STD_CPU_COST);
		memHeadroom = toDouble( OdysseusDefaults.get("ac_memHeadroom"), MEM_HEADROOM);
		cpuHeadroom = toDouble( OdysseusDefaults.get("ac_cpuHeadroom"), CPU_HEADROOM);
		
		// check cfg
		if( stdMemCost <= 0.0 ) {
			getLogger().error("Standard Memory Cost is invalid: " + stdMemCost + ", it must be positive. Setting to default of " + STD_MEM_COST);
			stdMemCost = STD_MEM_COST;
		}
		// check cfg
		if( stdCpuCost <= 0.0 ) {
			getLogger().error("Standard Processor Cost is invalid: " + stdCpuCost + ", it must be positive. Setting to default of " + STD_CPU_COST);
			stdMemCost = STD_CPU_COST;
		}
		if( memHeadroom <= 0.0 || memHeadroom > 1.0 ) {
			getLogger().error("Headroom of Memory Cost is invalid: " + memHeadroom + ", it must be between 0 and 1. Setting to default of " + MEM_HEADROOM);
			stdMemCost = MEM_HEADROOM;
		}
		if( cpuHeadroom <= 0.0 || cpuHeadroom > 1.0 ) {
			getLogger().error("Headroom of Processor Cost is invalid: " + cpuHeadroom + ", it must be between 0 and 1. Setting to default of " + CPU_HEADROOM);
			stdMemCost = CPU_HEADROOM;
		}		
	}
	
	public double getStandardMemCost() {
		return stdMemCost;
	}
	
	public double getStandardCpuCost() {
		return stdCpuCost;
	}
	
	public double getMemHeadroom() {
		return memHeadroom;
	}
	
	public double getCpuHeadroom() {
		return cpuHeadroom;
	}

	private Double toDouble(String value, double std) {
		try {
			if (value == null)
				return std;
			return Double.valueOf(value);
		} catch (Exception ex) {
			return std;
		}
	}
}
