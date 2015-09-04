package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class PriceCalculator {
	
	private static final Logger LOG = LoggerFactory.getLogger(PriceCalculator.class);
	
	private static final Sigar sigar = new Sigar();
	
	//We need this to have worse CPUs have higher Prices. Just assuming that CPUs won't get over 10.000 Mhz in the next few years...
	private static final int CPU_TO_SUBTRACT_FROM = 10000;
	
	private static Integer CPU_MAX=null; 
	private static Double LOCAL_COST_EXPONENT = null;
	
	
	private static final Double UPPER_BOUND_PERCENTAGE = 90.0;
	private static final Double LOWER_BOUND_PERCENTAGE = 70.0;
	
	
	
	
	private static IPeerResourceUsageManager usageManager;
	
	public static void bindUsageManager(IPeerResourceUsageManager serv) {
		usageManager = serv;
	}
	

	public static void unbindUsageManager(IPeerResourceUsageManager serv) {
		if(usageManager==serv) {
			usageManager = null;
		}
	}
	
	

	public static double getMaximumCosts() {
		return calculateLocalCosts(100);
	}
	
	public static double getMaximumLoad() {
		return getCpuMhz();
	}
	
	public static double getMinPrice() {
		return getPriceRange().getE1();
	}
	
	public static double getMaxPrice() {
		return getPriceRange().getE2();
	}
	
	
	public static double getCurrentCosts() {
		double absoluteUsage = usageManager.getLocalResourceUsage().getCpuMax()-usageManager.getLocalResourceUsage().getCpuFree();
		double relativeUsageInPercent = (absoluteUsage/usageManager.getLocalResourceUsage().getCpuMax())*100.0;
		return calculateLocalCosts(relativeUsageInPercent);
	}
	
	public static double getCurrentLoad() {
		return usageManager.getLocalResourceUsage().getCpuMax()-usageManager.getLocalResourceUsage().getCpuFree();
	}
	
	public static double calculateLocalCosts(double cpuLoad) {
		if(LOCAL_COST_EXPONENT==null) {
			init();
		}
		return Math.pow(cpuLoad, LOCAL_COST_EXPONENT);
	}
	
	
	
	public static void init() {
		CPU_MAX = 3500;
		LOCAL_COST_EXPONENT = getLocalCostExponent(CPU_MAX);
	}
	

	private static int getCpuMhz() {
		try {
			CpuInfo[] info =  sigar.getCpuInfoList();
			return info[0].getMhz();
		} catch (SigarException e) {
			LOG.error("Error while getting CpuInfo.");
			e.printStackTrace();
			return 0;
		}
	}
	
	private static double getLocalCostExponent(int cpuMax) {
		//2.0 because we are assuming 100% -> log_10(100) = 2.
		return Math.log10(CPU_TO_SUBTRACT_FROM-cpuMax)/2.0;
	}
	
	public static double getCostDifference(double firstLoad, double secondLoad) {
		return calculateLocalCosts(secondLoad)-calculateLocalCosts(firstLoad);
	}
	
	private static Pair<Double,Double> getPriceRange() {
		double maxPrice = getCostDifference(UPPER_BOUND_PERCENTAGE-1.0,UPPER_BOUND_PERCENTAGE);
		double minPrice = getCostDifference(LOWER_BOUND_PERCENTAGE-1.0, LOWER_BOUND_PERCENTAGE);
		
		return new Pair<Double,Double>(minPrice,maxPrice);
	}
	
}
