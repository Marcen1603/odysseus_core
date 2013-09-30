package de.uniol.inf.is.odysseus.billingmodel;

public class BillingHelper {
	private static boolean useBillingModel;
	private static IBillingManager billingManager;

	public static void setUseBillingModel(boolean use) {
		useBillingModel = use;
	}

	public static boolean useBillingModel() {
		return useBillingModel;
	}

	/**
	 * @return the billingManager
	 */
	public static IBillingManager getBillingManager() {
		return billingManager;
	}

	/**
	 * @param billingManager
	 *            the billingManager to set
	 */
	public static void setBillingManager(IBillingManager billingManager) {
		BillingHelper.billingManager = billingManager;
	}
}
