package de.uniol.inf.is.odysseus.fastflowerdelivery.driver;

import de.uniol.inf.is.odysseus.fastflowerdelivery.configuration.AbstractJsonConfiguration;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.MysqlConfiguration;

/**
 * A configuration class to configure the fixed data needed to start the application
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class DriverConfiguration extends AbstractJsonConfiguration<DriverConfiguration> {

	private int websitePort = 8081;
	private String odysseusAddress = "localhost";
	private int outgoingPortDeliveryBid = 4713;
	private int outgoingPortDeliveryConfirmation = 4717;
	private int incomingPortBidRequest = 4712;
	private int incomingPortAssignment = 4715;
	private int incomingPortPickupConfirmation = 4718;
	private int incomingPortIdleDriverReport = 4761;
	private int incomingPortImprovingDriverReport = 4762;
	private int incomingPortPermanentWeakDriverReport = 4760;
	private int incomingPortConsistentWeakDriverReport = 4763;
	private int incomingPortConsistentStrongDriverReport = 4764;
	private int incomingPortImprovingNote = 4770;
	private int incomingPortDeliveryRequestCancellation = 4778;
	private boolean createUserOnLogin = true;
	private MysqlConfiguration mysql = new MysqlConfiguration("localhost", 3307, "root", "cogni", "fastflower");

	public int getWebsitePort() {
		return websitePort;
	}
	
	public int getIncomingPortIdleDriverReport() {
		return incomingPortIdleDriverReport;
	}

	public void setIncomingPortIdleDriverReport(int incomingPortIdleDriverReport) {
		this.incomingPortIdleDriverReport = incomingPortIdleDriverReport;
	}

	public int getIncomingPortImprovingDriverReport() {
		return incomingPortImprovingDriverReport;
	}

	public void setIncomingPortImprovingDriverReport(
			int incomingPortImprovingDriverReport) {
		this.incomingPortImprovingDriverReport = incomingPortImprovingDriverReport;
	}

	public int getIncomingPortPermanentWeakDriverReport() {
		return incomingPortPermanentWeakDriverReport;
	}

	public void setIncomingPortPermanentWeakDriverReport(
			int incomingPortPermanentWeakDriverReport) {
		this.incomingPortPermanentWeakDriverReport = incomingPortPermanentWeakDriverReport;
	}

	public int getIncomingPortConsistentWeakDriverReport() {
		return incomingPortConsistentWeakDriverReport;
	}

	public void setIncomingPortConsistentWeakDriverReport(
			int incomingPortConsistentWeakDriverReport) {
		this.incomingPortConsistentWeakDriverReport = incomingPortConsistentWeakDriverReport;
	}

	public int getIncomingPortConsistentStrongDriverReport() {
		return incomingPortConsistentStrongDriverReport;
	}

	public void setIncomingPortConsistentStrongDriverReport(
			int incomingPortConsistentStrongDriverReport) {
		this.incomingPortConsistentStrongDriverReport = incomingPortConsistentStrongDriverReport;
	}

	public int getIncomingPortImprovingNote() {
		return incomingPortImprovingNote;
	}

	public void setIncomingPortImprovingNote(int incomingPortImprovingNote) {
		this.incomingPortImprovingNote = incomingPortImprovingNote;
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

	public int getOutgoingPortDeliveryBid() {
		return outgoingPortDeliveryBid;
	}

	public void setOutgoingPortDeliveryBid(int outgoingPortDeliveryBid) {
		this.outgoingPortDeliveryBid = outgoingPortDeliveryBid;
	}

	public int getOutgoingPortDeliveryConfirmation() {
		return outgoingPortDeliveryConfirmation;
	}

	public void setOutgoingPortDeliveryConfirmation(
			int outgoingPortDeliveryConfirmation) {
		this.outgoingPortDeliveryConfirmation = outgoingPortDeliveryConfirmation;
	}

	public int getIncomingPortBidRequest() {
		return incomingPortBidRequest;
	}

	public void setIncomingPortBidRequest(int incomingPortBidRequest) {
		this.incomingPortBidRequest = incomingPortBidRequest;
	}

	public int getIncomingPortAssignment() {
		return incomingPortAssignment;
	}

	public void setIncomingPortAssignment(int incomingPortAssignment) {
		this.incomingPortAssignment = incomingPortAssignment;
	}

	public int getIncomingPortPickupConfirmation() {
		return incomingPortPickupConfirmation;
	}

	public void setIncomingPortPickupConfirmation(int incomingPortPickupConfirmation) {
		this.incomingPortPickupConfirmation = incomingPortPickupConfirmation;
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
	 * @return the incomingPortDeliveryRequestCancellation
	 */
	public int getIncomingPortDeliveryRequestCancellation() {
		return incomingPortDeliveryRequestCancellation;
	}

	/**
	 * @param incomingPortDeliveryRequestCancellation the incomingPortDeliveryRequestCancellation to set
	 */
	public void setIncomingPortDeliveryRequestCancellation(
			int incomingPortDeliveryRequestCancellation) {
		this.incomingPortDeliveryRequestCancellation = incomingPortDeliveryRequestCancellation;
	}

}
