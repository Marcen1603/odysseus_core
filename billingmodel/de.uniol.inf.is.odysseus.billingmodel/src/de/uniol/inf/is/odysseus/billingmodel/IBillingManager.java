package de.uniol.inf.is.odysseus.billingmodel;

import java.util.Map;

public interface IBillingManager {
	public void addPayment(String userID, int queryID, double amount);

	public void addPaymentSanction(String userID, int queryID, double amount);
	
	public void addRevenue(String userID, int queryID, double amount);

	public void addRevenueSanction(String userID, int queryID, double amount);
	
	public Map<String, Map<Integer, Double>> getUnsavedPayments();
	
	public Map<String, Map<Integer, Double>> getUnsavedPaymentSanctions();

	public Map<String, Map<Integer, Double>> getUnsavedRevenues();

	public Map<String, Map<Integer, Double>> getUnsavedRevenueSanctions();

	public int getNumberOfUnsavedPayments();
	
	public int getNumberOfUnsavedPaymentSanctions();
	
	public int getNumberOfUnsavedRevenues();
	
	public int getNumberOfUnsavedRevenueSanctions();
	
	public long getLastTimestampOfPersistence();
	
	public void persistBillingInformations();
}
