/**
 * 
 */
package de.uniol.inf.is.odysseus.wrapper.ivef;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.*;
import de.uniol.inf.is.odysseus.wrapper.ivef.parser.IVEF_0_1_5_Parser;

/**
 * @author msalous
 *
 */
public class IVEF_0_1_5_ProtocolHandler extends
		AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {
	
	/** Logger for this class. */
	private final Logger LOG = LoggerFactory.getLogger(IVEF_0_1_5_ProtocolHandler.class);
	/** Input stream as BufferedReader (Only in GenericPull: for example: file). */
	protected BufferedReader reader;
	/** IVEF Parser*/
	private IVEF_0_1_5_Parser parser;
	/** The parsed messages */
	private MSG_LoginRequest m_loginRequest;	    
    private MSG_LoginResponse m_loginResponse;
    private MSG_Logout m_logout;
    private MSG_Ping m_ping;
    private MSG_Pong m_pong;
    private MSG_ServerStatus m_serverStatus;
    private MSG_ServiceRequest m_serviceRequest;
    private MSG_VesselData m_vesselData;

	
	/** IVEF object as key-value to be returned for GenericPull. */
	private KeyValueObject<? extends IMetaAttribute> next = null;
	/** Delay on GenericPull. */
	private long delay = 0;
	
	//Constructors
	public IVEF_0_1_5_ProtocolHandler() {
		this.parser = new IVEF_0_1_5_Parser(); 
		this.m_loginRequest = new MSG_LoginRequest();
		this.m_loginResponse = new MSG_LoginResponse();
		this.m_logout = new MSG_Logout();
		this.m_ping = new MSG_Ping();
		this.m_pong = new MSG_Pong();
		this.m_serverStatus = new MSG_ServerStatus();
		this.m_serviceRequest = new MSG_ServiceRequest();
		this.m_vesselData = new MSG_VesselData();
	}

	public IVEF_0_1_5_ProtocolHandler(ITransportDirection direction,
			IAccessPattern access,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler);
		this.parser = new IVEF_0_1_5_Parser();
		this.m_loginRequest = new MSG_LoginRequest();
		this.m_loginResponse = new MSG_LoginResponse();
		this.m_logout = new MSG_Logout();
		this.m_ping = new MSG_Ping();
		this.m_pong = new MSG_Pong();
		this.m_serverStatus = new MSG_ServerStatus();
		this.m_serviceRequest = new MSG_ServiceRequest();
		this.m_vesselData = new MSG_VesselData();
	}

	@Override
	public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(
			ITransportDirection direction, IAccessPattern access,
			Map<String, String> options,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		IVEF_0_1_5_ProtocolHandler instance = new IVEF_0_1_5_ProtocolHandler(direction, access, dataHandler);
		instance.setOptionsMap(options);
		if (options.containsKey("delay")) 
			instance.delay = Integer.parseInt(options.get("delay"));
		return instance;
	}

	@Override
	public String getName() {
		return "IVEF_0_1_5";
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return (other instanceof IVEF_0_1_5_ProtocolHandler);
	}
	
	//GenericPull stuff:
	//hasNext, getNext open and close methods are used by genericPull...files
	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if ( (this.getAccess().equals(IAccessPattern.PULL)) || 
			 (this.getAccess().equals(IAccessPattern.ROBUST_PULL) ) )
			 this.reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream() ) );
	}

	@Override
	public void close() throws IOException {
		try {
			if (this.reader != null)
				this.reader.close();
		} 
		catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		getTransportHandler().close();
	}
	
	@Override
	public boolean hasNext() throws IOException {
		if (!reader.ready()) {
			return false;
		}
		String ivefLine = null;
		while((ivefLine=this.reader.readLine()) != null) {
			if (!(this.parser.parseXMLString(ivefLine, true) ) )
				return false;
			//m_loginRequest
			if (this.parser.loginRequestPresent()) {
				this.m_loginRequest = this.parser.getLoginRequest();
				this.next = this.m_loginRequest.toMap(); 
				this.next.setMetadata("object", this.m_loginRequest);
				this.parser.resetLoginRequestPresent();
				return true;
			}
			//m_loginResponse
			if (this.parser.loginResponsePresent()) {
				this.m_loginResponse = this.parser.getLoginResponse();
				this.next = this.m_loginResponse.toMap(); 
				this.next.setMetadata("object", this.m_loginResponse);
				this.parser.resetLoginResponsePresent();
				return true;
			}
			//m_logout
			if (this.parser.logoutPresent()) {
				this.m_logout = this.parser.getLogout();
				this.next = this.m_logout.toMap(); 
				this.next.setMetadata("object", this.m_logout);
				this.parser.resetLogoutPresent();
				return true;
			}
			//m_ping
			if (this.parser.pingPresent()) {
				this.m_ping = this.parser.getPing();
				this.next = this.m_ping.toMap(); 
				this.next.setMetadata("object", this.m_ping);
				this.parser.resetPingPresent();
				return true;
			}
			
			//m_pong
			if (this.parser.pongPresent()) {
				this.m_pong = this.parser.getPong();
				this.next = this.m_pong.toMap(); 
				this.next.setMetadata("object", this.m_pong);
				this.parser.resetPongPresent();
				return true;
			}
			//m_serverStatus
			if (this.parser.serverStatusPresent()) {
				this.m_serverStatus = this.parser.getServerStatus();
				this.next = this.m_serverStatus.toMap(); 
				this.next.setMetadata("object", this.m_serverStatus);
				this.parser.resetServerStatusPresent();;
				return true;
			}
			//m_serviceRequest
			if (this.parser.serviceRequestPresent()) {
				this.m_serviceRequest = this.parser.getServiceRequest();
				this.next = this.m_serviceRequest.toMap(); 
				this.next.setMetadata("object", this.m_serviceRequest);
				this.parser.resetServiceRequestPresent();
				return true;
			}
			//m_vesselData
			if (this.parser.vesselDataPresent()) {
				this.m_vesselData = this.parser.getVesselData();
				this.next = this.m_vesselData.toMap(); 
				this.next.setMetadata("object", this.m_vesselData);
				this.parser.resetVesselDataPresent();
				return true;
			}
		}
		return false;
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getNext()
			throws IOException {
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}
		}
		return next;
	}

	//GenericPush stuff:
	//process is used by genericPush...UDP 
	@Override
	public void process(ByteBuffer message) {
		byte[] m = new byte[message.limit()];
		message.get(m);
		String ivefStr = (new String(m));//.trim();
		KeyValueObject<? extends IMetaAttribute> map = new KeyValueObject<>();
		if (!(this.parser.parseXMLString(ivefStr, true) ) )
			return;
		boolean found = false;
		//m_loginRequest
		if(this.parser.loginRequestPresent()){
			this.parser.resetLoginRequestPresent();
			this.m_loginRequest = this.parser.getLoginRequest();
			map = this.m_loginRequest.toMap();
			map.setMetadata("object", this.m_loginRequest);
			found = true;
//			getTransfer().transfer(map);
		}
		//m_loginResponse
		if (this.parser.loginResponsePresent()) {
			this.parser.resetLoginResponsePresent();
			this.m_loginResponse = this.parser.getLoginResponse();
			map = this.m_loginResponse.toMap(); 
			map.setMetadata("object", this.m_loginResponse);
			found = true;
//			getTransfer().transfer(map);
		}
		//m_logout
		if (this.parser.logoutPresent()) {
			this.parser.resetLogoutPresent();
			this.m_logout = this.parser.getLogout();
			map = this.m_logout.toMap(); 
			map.setMetadata("object", this.m_logout);
			found = true;
//			getTransfer().transfer(map);
		}
		//m_ping
		if (this.parser.pingPresent()) {
			this.parser.resetPingPresent();
			this.m_ping = this.parser.getPing();
			map = this.m_ping.toMap(); 
			map.setMetadata("object", this.m_ping);
			found = true;
//			getTransfer().transfer(map);
		}
		
		//m_pong
		if (this.parser.pongPresent()) {
			this.parser.resetPongPresent();
			this.m_pong = this.parser.getPong();
			map = this.m_pong.toMap(); 
			map.setMetadata("object", this.m_pong);
			found = true;
//			getTransfer().transfer(map);
		}
		//m_serverStatus
		if (this.parser.serverStatusPresent()) {
			this.parser.resetServerStatusPresent();
			this.m_serverStatus = this.parser.getServerStatus();
			map = this.m_serverStatus.toMap(); 
			map.setMetadata("object", this.m_serverStatus);
			found = true;
//			getTransfer().transfer(map);
		}
		//m_serviceRequest
		if (this.parser.serviceRequestPresent()) {
			this.parser.resetServiceRequestPresent();
			this.m_serviceRequest = this.parser.getServiceRequest();
			map = this.m_serviceRequest.toMap(); 
			map.setMetadata("object", this.m_serviceRequest);
			found = true;
//			getTransfer().transfer(map);
		}
		//m_vesselData
		if (this.parser.vesselDataPresent()) {
			this.parser.resetVesselDataPresent();
			this.m_vesselData = this.parser.getVesselData();
			map = this.m_vesselData.toMap(); 
			map.setMetadata("object", this.m_vesselData);
			found = true;
//			getTransfer().transfer(map);
		}
		if(found)
			getTransfer().transfer(map);
	}
	
	@Override
	public void write(KeyValueObject<? extends IMetaAttribute> object)
			throws IOException {
		try {
			java.lang.Object obj = object.getMetadata("object");
			//m_loginRequest
			if (obj instanceof MSG_LoginRequest) {
				this.m_loginRequest = (MSG_LoginRequest) obj;
				getTransportHandler().send(this.m_loginRequest.toXML().getBytes());
			}
			//m_loginResponse
			else if (obj instanceof MSG_LoginResponse) {
				this.m_loginResponse = (MSG_LoginResponse) obj;
				getTransportHandler().send(this.m_loginResponse.toXML().getBytes());
			}
			//m_logout
			else if (obj instanceof MSG_Logout) {
				this.m_logout = (MSG_Logout) obj;
				getTransportHandler().send(this.m_logout.toXML().getBytes());
			}
			//m_ping
			else if (obj instanceof MSG_Ping) {
				this.m_ping = (MSG_Ping) obj;
				getTransportHandler().send(this.m_ping.toXML().getBytes());
			}
			//m_pong
			else if (obj instanceof MSG_Pong) {
				this.m_pong = (MSG_Pong) obj;
				getTransportHandler().send(this.m_pong.toXML().getBytes());
			}
			//m_serverStatus
			else if (obj instanceof MSG_ServerStatus) {
				this.m_serverStatus = (MSG_ServerStatus) obj;
				getTransportHandler().send(this.m_serverStatus.toXML().getBytes());
			}
			//m_serviceRequest
			else if (obj instanceof MSG_ServiceRequest) {
				this.m_serviceRequest = (MSG_ServiceRequest) obj;
				getTransportHandler().send(this.m_serviceRequest.toXML().getBytes());
			}
			//m_vesselData
			else {
				this.m_vesselData = (MSG_VesselData) obj;
				getTransportHandler().send(this.m_vesselData.toXML().getBytes());
			}
		} 
		catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
