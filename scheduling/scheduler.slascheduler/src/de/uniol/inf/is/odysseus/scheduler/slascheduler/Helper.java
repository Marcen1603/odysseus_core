package de.uniol.inf.is.odysseus.scheduler.slascheduler;

public class Helper {
	private static boolean useBillingModel;
	
	public static void setUseBillingModel(boolean use) {
		useBillingModel = use;
	}
	
	public static boolean useBillingModel() {
		return useBillingModel;
	}
}
