package de.uniol.inf.is.odysseus.fastflowerdelivery.store;

import de.uniol.inf.is.odysseus.fastflowerdelivery.configuration.AbstractJsonConfiguration;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.MysqlConfiguration;

/**
 * A configuration class to configure the fixed data needed to start the application
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class StoreConfiguration extends AbstractJsonConfiguration<StoreConfiguration> {

	private int websitePort = 8080;
	private String odysseusAddress = "localhost";
	private int outgoingPortDeliveryRequest = 4711;
	private int outgoingPortManualAssignment = 4714;
	private int outgoingPortPickupConfirmation = 4716;
	private int outgoingPortDeliveryRequestCancellation = 4777;
	private int incomingPortDeliveryBid = 4719;
	private int incomingPortDeliveryConfirmation = 4720;
	private int incomingPortManualAssignmentTimeoutAlert = 4721;
	private int incomingPortPickupAlert = 4722;
	private int incomingPortDeliveryAlert = 4723;
	private boolean createUserOnLogin = true;
	private MysqlConfiguration mysql = new MysqlConfiguration("localhost", 3307, "root", "cogni", "fastflower");
	
	public int getWebsitePort() {
		return websitePort;
	}
	
	public void setWebsitePort(int websitePort) {
		this.websitePort = websitePort;
	}
	
	public String getOdysseusAddress() {
		return odysseusAddress;
	}
	
	public void setOdysseusAddress(String odysseusAddress) {
		this.odysseusAddress = odysseusAddress;
	}
	
	public int getOutgoingPortDeliveryRequest() {
		return outgoingPortDeliveryRequest;
	}
	
	public void setOutgoingPortDeliveryRequest(int outgoingPortDeliveryRequest) {
		this.outgoingPortDeliveryRequest = outgoingPortDeliveryRequest;
	}
	
	public int getOutgoingPortManualAssignment() {
		return outgoingPortManualAssignment;
	}
	
	public void setOutgoingPortManualAssignment(int outgoingPortManualAssignment) {
		this.outgoingPortManualAssignment = outgoingPortManualAssignment;
	}
	
	public int getOutgoingPortPickupConfirmation() {
		return outgoingPortPickupConfirmation;
	}
	
	public void setOutgoingPortPickupConfirmation(int outgoingPortPickupConfirmation) {
		this.outgoingPortPickupConfirmation = outgoingPortPickupConfirmation;
	}
	
	public int getIncomingPortDeliveryBid() {
		return incomingPortDeliveryBid;
	}
	
	public void setIncomingPortDeliveryBid(int incomingPortDeliveryBid) {
		this.incomingPortDeliveryBid = incomingPortDeliveryBid;
	}
	
	public int getIncomingPortDeliveryConfirmation() {
		return incomingPortDeliveryConfirmation;
	}
	
	public void setIncomingPortDeliveryConfirmation(
			int incomingPortDeliveryConfirmation) {
		this.incomingPortDeliveryConfirmation = incomingPortDeliveryConfirmation;
	}

	public MysqlConfiguration getMysql() {
		return mysql;
	}

	public void setMysql(MysqlConfiguration mysql) {
		this.mysql = mysql;
	}

	public boolean isCreateUserOnLogin() {
		return createUserOnLogin;
	}

	public void setCreateUserOnLogin(boolean createUserOnLogin) {
		this.createUserOnLogin = createUserOnLogin;
	}

	/**
	 * @return the incomingPortManualAssignmentTimeoutAlert
	 */
	public int getIncomingPortManualAssignmentTimeoutAlert() {
		return incomingPortManualAssignmentTimeoutAlert;
	}

	/**
	 * @param incomingPortManualAssignmentTimeoutAlert the incomingPortManualAssignmentTimeoutAlert to set
	 */
	public void setIncomingPortManualAssignmentTimeoutAlert(
			int incomingPortManualAssignmentTimeoutAlert) {
		this.incomingPortManualAssignmentTimeoutAlert = incomingPortManualAssignmentTimeoutAlert;
	}

	/**
	 * @return the incomingPortPickupAlert
	 */
	public int getIncomingPortPickupAlert() {
		return incomingPortPickupAlert;
	}

	/**
	 * @param incomingPortPickupAlert the incomingPortPickupAlert to set
	 */
	public void setIncomingPortPickupAlert(int incomingPortPickupAlert) {
		this.incomingPortPickupAlert = incomingPortPickupAlert;
	}

	/**
	 * @return the incomingPortDeliveryAlert
	 */
	public int getIncomingPortDeliveryAlert() {
		return incomingPortDeliveryAlert;
	}

	/**
	 * @param incomingPortDeliveryAlert the incomingPortDeliveryAlert to set
	 */
	public void setIncomingPortDeliveryAlert(int incomingPortDeliveryAlert) {
		this.incomingPortDeliveryAlert = incomingPortDeliveryAlert;
	}

	/**
	 * @return the outgoingPortDeliveryRequestCancellation
	 */
	public int getOutgoingPortDeliveryRequestCancellation() {
		return outgoingPortDeliveryRequestCancellation;
	}

	/**
	 * @param outgoingPortDeliveryRequestCancellation the outgoingPortDeliveryRequestCancellation to set
	 */
	public void setOutgoingPortDeliveryRequestCancellation(
			int outgoingPortDeliveryRequestCancellation) {
		this.outgoingPortDeliveryRequestCancellation = outgoingPortDeliveryRequestCancellation;
	}
}
