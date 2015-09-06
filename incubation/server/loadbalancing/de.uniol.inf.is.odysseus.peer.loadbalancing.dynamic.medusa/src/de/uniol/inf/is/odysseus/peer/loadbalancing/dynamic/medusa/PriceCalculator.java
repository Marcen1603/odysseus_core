package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa;

import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class PriceCalculator {
	
	
	private static final double LOWER_BOUND = 70;
	private static final double UPPER_BOUND = 90;
	
	//private static final Logger LOG = LoggerFactory.getLogger(PriceCalculator.class);
	
	
	private static IPeerResourceUsageManager usageManager;
	
	
	public static void bindUsageManager(IPeerResourceUsageManager serv) {
		usageManager = serv;
	}
	

	public static void unbindUsageManager(IPeerResourceUsageManager serv) {
		if(usageManager==serv) {
			usageManager = null;
		}
	}
	
	public static double calcMarginCost(double loadInPercent) {
		return 2*loadInPercent;
	}
	
	public static double getPrice() {
		if(getCurrentLoad()<LOWER_BOUND) {
			return calcMarginCost(LOWER_BOUND);
		}
		else {
			if(getCurrentLoad()>UPPER_BOUND) {
				return Double.POSITIVE_INFINITY;
			}
		}
		return calcCurrentMarginCost();
	}

	
	public static double calcCurrentMarginCost() {
		return calcMarginCost(getCurrentLoad());
	}

	public static double getCurrentCosts() {
		double absoluteUsage = usageManager.getLocalResourceUsage().getCpuMax()-usageManager.getLocalResourceUsage().getCpuFree();
		double relativeUsageInPercent = (absoluteUsage/usageManager.getLocalResourceUsage().getCpuMax())*100.0;
		return calculateLocalCosts(relativeUsageInPercent);
	}
	
	public static double getCostDifference(double perc1,double perc2) {
		return calculateLocalCosts(perc2)-calculateLocalCosts(perc1);
	}
	
	
	public static double getCurrentLoad() {
		return (usageManager.getLocalResourceUsage().getCpuMax()-usageManager.getLocalResourceUsage().getCpuFree())*100;
	}
	
	
	public static double calculateLocalCosts(double cpuLoad) {
		return Math.pow(cpuLoad, 2.0);
	}
	
	
	
	
}
