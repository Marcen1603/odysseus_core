package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.LoginRequest;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.LoginResponse;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.Logout;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.ObjectDatas;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.Ping;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.Pong;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.ServerStatus;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.ServiceRequest;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.ServiceRequestResponse;

/**
 * @author msalous
 *
 */
public class Body implements IIvefElement { 

    private LoginRequest m_loginRequest; // default value is uninitialized
    private boolean m_loginRequestPresent;
    private LoginResponse m_loginResponse; // default value is uninitialized
    private boolean m_loginResponsePresent;
    private Logout m_logout; // default value is uninitialized
    private boolean m_logoutPresent;
    private ObjectDatas m_objectDatas; // default value is uninitialized
    private boolean m_objectDatasPresent;
    private Ping m_ping; // default value is uninitialized
    private boolean m_pingPresent;
    private Pong m_pong; // default value is uninitialized
    private boolean m_pongPresent;
    private ServerStatus m_serverStatus; // default value is uninitialized
    private boolean m_serverStatusPresent;
    private ServiceRequest m_serviceRequest; // default value is uninitialized
    private boolean m_serviceRequestPresent;
    private ServiceRequestResponse m_serviceRequestResponse; // default value is uninitialized
    private boolean m_serviceRequestResponsePresent;

    public Body() {

        m_loginRequestPresent = false;
        m_loginResponsePresent = false;
        m_logoutPresent = false;
        m_objectDatasPresent = false;
        m_pingPresent = false;
        m_pongPresent = false;
        m_serverStatusPresent = false;
        m_serviceRequestPresent = false;
        m_serviceRequestResponsePresent = false;
    }

    public Body(Body val) {

        m_loginRequest = val.getLoginRequest();
        if (val != null) {
            m_loginRequestPresent = true;
        }
        m_loginResponse = val.getLoginResponse();
        if (val != null) {
            m_loginResponsePresent = true;
        }
        m_logout = val.getLogout();
        if (val != null) {
            m_logoutPresent = true;
        }
        m_objectDatas = val.getObjectDatas();
        if (val != null) {
            m_objectDatasPresent = true;
        }
        m_ping = val.getPing();
        if (val != null) {
            m_pingPresent = true;
        }
        m_pong = val.getPong();
        if (val != null) {
            m_pongPresent = true;
        }
        m_serverStatus = val.getServerStatus();
        if (val != null) {
            m_serverStatusPresent = true;
        }
        m_serviceRequest = val.getServiceRequest();
        if (val != null) {
            m_serviceRequestPresent = true;
        }
        m_serviceRequestResponse = val.getServiceRequestResponse();
        if (val != null) {
            m_serviceRequestResponsePresent = true;
        }
    }

    public boolean setLoginRequest(LoginRequest val) {

        m_loginRequestPresent = true;
        m_loginRequest = val;
        return true;
    }

    public LoginRequest getLoginRequest() {

        return m_loginRequest;
    }

    public boolean hasLoginRequest() {

        return m_loginRequestPresent;
    }

    public boolean setLoginResponse(LoginResponse val) {

        m_loginResponsePresent = true;
        m_loginResponse = val;
        return true;
    }

    public LoginResponse getLoginResponse() {

        return m_loginResponse;
    }

    public boolean hasLoginResponse() {

        return m_loginResponsePresent;
    }

    public boolean setLogout(Logout val) {

        m_logoutPresent = true;
        m_logout = val;
        return true;
    }

    public Logout getLogout() {

        return m_logout;
    }

    public boolean hasLogout() {

        return m_logoutPresent;
    }

    public boolean setObjectDatas(ObjectDatas val) {

        m_objectDatasPresent = true;
        m_objectDatas = val;
        return true;
    }

    public ObjectDatas getObjectDatas() {

        return m_objectDatas;
    }

    public boolean hasObjectDatas() {

        return m_objectDatasPresent;
    }

    public boolean setPing(Ping val) {

        m_pingPresent = true;
        m_ping = val;
        return true;
    }

    public Ping getPing() {

        return m_ping;
    }

    public boolean hasPing() {

        return m_pingPresent;
    }

    public boolean setPong(Pong val) {

        m_pongPresent = true;
        m_pong = val;
        return true;
    }

    public Pong getPong() {

        return m_pong;
    }

    public boolean hasPong() {

        return m_pongPresent;
    }

    public boolean setServerStatus(ServerStatus val) {

        m_serverStatusPresent = true;
        m_serverStatus = val;
        return true;
    }

    public ServerStatus getServerStatus() {

        return m_serverStatus;
    }

    public boolean hasServerStatus() {

        return m_serverStatusPresent;
    }

    public boolean setServiceRequest(ServiceRequest val) {

        m_serviceRequestPresent = true;
        m_serviceRequest = val;
        return true;
    }

    public ServiceRequest getServiceRequest() {

        return m_serviceRequest;
    }

    public boolean hasServiceRequest() {

        return m_serviceRequestPresent;
    }

    public boolean setServiceRequestResponse(ServiceRequestResponse val) {

        m_serviceRequestResponsePresent = true;
        m_serviceRequestResponse = val;
        return true;
    }

    public ServiceRequestResponse getServiceRequestResponse() {

        return m_serviceRequestResponse;
    }

    public boolean hasServiceRequestResponse() {

        return m_serviceRequestResponsePresent;
    }

    public String toXML() {

        String xml = "<Body";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        String dataMember;
        xml += ">\n";
        if ( hasLoginRequest() ) {
            dataMember =  m_loginRequest.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if ( hasLoginResponse() ) {
            dataMember =  m_loginResponse.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if ( hasLogout() ) {
            dataMember =  m_logout.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if ( hasObjectDatas() ) {
            dataMember =  m_objectDatas.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if ( hasPing() ) {
            dataMember =  m_ping.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if ( hasPong() ) {
            dataMember =  m_pong.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if ( hasServerStatus() ) {
            dataMember =  m_serverStatus.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if ( hasServiceRequest() ) {
            dataMember =  m_serviceRequest.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if ( hasServiceRequestResponse() ) {
            dataMember =  m_serviceRequestResponse.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        xml += "</Body>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Body\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasLoginRequest() ) {
            str +=  m_loginRequest.toString(lead + "    ") ;
        }
        if ( hasLoginResponse() ) {
            str +=  m_loginResponse.toString(lead + "    ") ;
        }
        if ( hasLogout() ) {
            str +=  m_logout.toString(lead + "    ") ;
        }
        if ( hasObjectDatas() ) {
            str +=  m_objectDatas.toString(lead + "    ") ;
        }
        if ( hasPing() ) {
            str +=  m_ping.toString(lead + "    ") ;
        }
        if ( hasPong() ) {
            str +=  m_pong.toString(lead + "    ") ;
        }
        if ( hasServerStatus() ) {
            str +=  m_serverStatus.toString(lead + "    ") ;
        }
        if ( hasServiceRequest() ) {
            str +=  m_serviceRequest.toString(lead + "    ") ;
        }
        if ( hasServiceRequestResponse() ) {
            str +=  m_serviceRequestResponse.toString(lead + "    ") ;
        }
        return str;
    }
    public String encode( String str) {

        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\"", "&quot;");
        return str;
    }

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map) {
		if(m_loginRequestPresent)
			m_loginRequest.fillMap(map);
		if(m_loginResponsePresent)
			m_loginResponse.fillMap(map);
		if(m_logoutPresent)
			m_logout.fillMap(map);
		if(m_objectDatasPresent)
			m_objectDatas.fillMap(map);
		if(m_pingPresent)
			m_ping.fillMap(map);
		if(m_pongPresent)
			m_pong.fillMap(map);
		if(m_serverStatusPresent)
			m_serverStatus.fillMap(map);
		if(m_serviceRequestPresent)
			m_serviceRequest.fillMap(map);
		if(m_serviceRequestResponsePresent)
			m_serviceRequestResponse.fillMap(map);
	}
}