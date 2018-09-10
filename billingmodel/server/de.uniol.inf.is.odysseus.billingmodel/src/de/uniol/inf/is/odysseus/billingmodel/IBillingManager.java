package de.uniol.inf.is.odysseus.billingmodel;

import java.util.Map;

public interface IBillingManager {
	public void addPayment(String userID, int queryID, long amount);

	public void addPaymentSanction(String userID, int queryID, long amount);
	
	public void addRevenue(String userID, int queryID, long amount);

	public void addRevenueSanction(String userID, int queryID, long amount);
	
	public Map<String, Map<Integer, Long>> getUnsavedPayments();
	
	public Map<String, Map<Integer, Long>> getUnsavedPaymentSanctions();

	public Map<String, Map<Integer, Long>> getUnsavedRevenues();

	public Map<String, Map<Integer, Long>> getUnsavedRevenueSanctions();

	public int getNumberOfUnsavedPayments();
	
	public int getNumberOfUnsavedPaymentSanctions();
	
	public int getNumberOfUnsavedRevenues();
	
	public int getNumberOfUnsavedRevenueSanctions();
	
	public long getLastTimestampOfPersistence();
	
	/**
	 * persists the temporary (within one persistence interval) stored billing informations
	 */
	public void persistBillingInformations();
}
