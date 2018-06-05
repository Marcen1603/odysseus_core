package de.uniol.inf.is.odysseus.billingmodel;

public class BillingHelper {
	private static boolean useBillingModel;
	private static IBillingManager billingManager;
	private static long persistenceInterval;

	/**
	 * 
	 * @param use the useBillingModel to set
	 */
	public static void setUseBillingModel(boolean use) {
		useBillingModel = use;
	}

	/**
	 * 
	 * @return the useBillingModel
	 */
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

	/**
	 * @return the persistenceInterval
	 */
	public static long getPersistenceInterval() {
		return persistenceInterval;
	}

	/**
	 * @param persistenceInterval the persistenceInterval to set
	 */
	public static void setPersistenceInterval(long persistenceInterval) {
		BillingHelper.persistenceInterval = persistenceInterval;
	}
}
