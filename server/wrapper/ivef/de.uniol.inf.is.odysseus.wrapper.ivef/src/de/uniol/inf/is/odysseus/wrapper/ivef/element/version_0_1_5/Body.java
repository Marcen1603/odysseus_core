/* 
 * 
 */

package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.LoginRequest;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.LoginResponse;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Logout;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Ping;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Pong;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.ServerStatus;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.ServiceRequest;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.VesselData;

public class Body implements IIvefElement { 

    private LoginRequest m_loginRequest; // default value is uninitialized
    private boolean m_loginRequestPresent;
    private LoginResponse m_loginResponse; // default value is uninitialized
    private boolean m_loginResponsePresent;
    private Logout m_logout; // default value is uninitialized
    private boolean m_logoutPresent;
    private Ping m_ping; // default value is uninitialized
    private boolean m_pingPresent;
    private Pong m_pong; // default value is uninitialized
    private boolean m_pongPresent;
    private ServerStatus m_serverStatus; // default value is uninitialized
    private boolean m_serverStatusPresent;
    private ServiceRequest m_serviceRequest; // default value is uninitialized
    private boolean m_serviceRequestPresent;
    private Vector<VesselData> m_vesselDatas = new Vector<VesselData>();
//    private boolean m_vesselDataPresent;

    public Body() {

        m_loginRequestPresent = false;
        m_loginResponsePresent = false;
        m_logoutPresent = false;
        m_pingPresent = false;
        m_pongPresent = false;
        m_serverStatusPresent = false;
        m_serviceRequestPresent = false;
//      m_vesselDataPresent = false;
    }

    public Body(Body val) {

        m_loginRequestPresent = val.hasLoginRequest();
        m_loginRequest = val.getLoginRequest();
        m_loginResponsePresent = val.hasLoginResponse();
        m_loginResponse = val.getLoginResponse();
        m_logoutPresent = val.hasLogout();
        m_logout = val.getLogout();
        m_pingPresent = val.hasPing();
        m_ping = val.getPing();
        m_pongPresent = val.hasPong();
        m_pong = val.getPong();
        m_serverStatusPresent = val.hasServerStatus();
        m_serverStatus = val.getServerStatus();
        m_serviceRequestPresent = val.hasServiceRequest();
        m_serviceRequest = val.getServiceRequest();
        for(int i=0; i < val.countOfVesselDatas(); i++ ) {
            m_vesselDatas.add( val.getVesselDataAt(i) );
        }
    }

    public void setLoginRequest(LoginRequest val) {

        m_loginRequestPresent = true;
        m_loginRequest = val;
    }

    public LoginRequest getLoginRequest() {

        return m_loginRequest;
    }

    public boolean hasLoginRequest() {

        return m_loginRequestPresent;
    }

    public void setLoginResponse(LoginResponse val) {

        m_loginResponsePresent = true;
        m_loginResponse = val;
    }

    public LoginResponse getLoginResponse() {

        return m_loginResponse;
    }

    public boolean hasLoginResponse() {

        return m_loginResponsePresent;
    }

    public void setLogout(Logout val) {

        m_logoutPresent = true;
        m_logout = val;
    }

    public Logout getLogout() {

        return m_logout;
    }

    public boolean hasLogout() {

        return m_logoutPresent;
    }

    public void setPing(Ping val) {

        m_pingPresent = true;
        m_ping = val;
    }

    public Ping getPing() {

        return m_ping;
    }

    public boolean hasPing() {

        return m_pingPresent;
    }

    public void setPong(Pong val) {

        m_pongPresent = true;
        m_pong = val;
    }

    public Pong getPong() {

        return m_pong;
    }

    public boolean hasPong() {

        return m_pongPresent;
    }

    public void setServerStatus(ServerStatus val) {

        m_serverStatusPresent = true;
        m_serverStatus = val;
    }

    public ServerStatus getServerStatus() {

        return m_serverStatus;
    }

    public boolean hasServerStatus() {

        return m_serverStatusPresent;
    }

    public void setServiceRequest(ServiceRequest val) {

        m_serviceRequestPresent = true;
        m_serviceRequest = val;
    }

    public ServiceRequest getServiceRequest() {

        return m_serviceRequest;
    }

    public boolean hasServiceRequest() {

        return m_serviceRequestPresent;
    }

    public void addVesselData(VesselData val) {

        m_vesselDatas.add(val);
    }

    public VesselData getVesselDataAt(int i) {

        return m_vesselDatas.get(i);
    }

    public int countOfVesselDatas() {

        return m_vesselDatas.size();
    }
    @Override
    public String toXML() {

        String xml = "<Body";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += ">\n";
        if ( hasLoginRequest() ) {
            xml +=  m_loginRequest.toXML() ;
            }
        if ( hasLoginResponse() ) {
            xml +=  m_loginResponse.toXML() ;
            }
        if ( hasLogout() ) {
            xml +=  m_logout.toXML() ;
            }
        if ( hasPing() ) {
            xml +=  m_ping.toXML() ;
            }
        if ( hasPong() ) {
            xml +=  m_pong.toXML() ;
            }
        if ( hasServerStatus() ) {
            xml +=  m_serverStatus.toXML() ;
            }
        if ( hasServiceRequest() ) {
            xml +=  m_serviceRequest.toXML() ;
            }
        for(int i=0; i < m_vesselDatas.size(); i++ ) {
           VesselData attribute = m_vesselDatas.get(i);
            xml += attribute.toXML();
        }
        xml += "</Body>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Body\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        if ( hasLoginRequest() ) {
            str +=  m_loginRequest.toString(lead + "    ") ;
        }
        if ( hasLoginResponse() ) {
            str +=  m_loginResponse.toString(lead + "    ") ;
        }
        if ( hasLogout() ) {
            str +=  m_logout.toString(lead + "    ") ;
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
        for(int i=0; i < m_vesselDatas.size(); i++ ) {
           VesselData attribute = m_vesselDatas.get(i);
           str += attribute.toString(lead + "    ");
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
		if(m_pingPresent)
			m_ping.fillMap(map);
		if(m_pongPresent)
			m_pong.fillMap(map);
		if(m_serverStatusPresent)
			m_serverStatus.fillMap(map);
		if(m_serviceRequestPresent)
			m_serviceRequest.fillMap(map);
	   for(VesselData vdata : m_vesselDatas)
			vdata.fillMap(map);
	}


}
