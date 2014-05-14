package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;

/**
 * @author msalous
 *
 */
public class ServerStatus implements IIvefElement { 

    private String m_contactIdentity; // default value is uninitialized
    private boolean m_contactIdentityPresent;
    private String m_details; // default value is uninitialized
    private boolean m_detailsPresent;
    private boolean m_status; // default value is uninitialized
    private boolean m_statusPresent;

    public ServerStatus() {

        m_contactIdentityPresent = false;
        m_detailsPresent = false;
        m_statusPresent = false;
    }

    public ServerStatus(ServerStatus val) {

        m_contactIdentity = val.getContactIdentity();
        if (val != null) {
            m_contactIdentityPresent = true;
        }
        m_details = val.getDetails();
        if (val != null) {
            m_detailsPresent = true;
        }
        m_status = val.getStatus();
        if (val != null) {
            m_statusPresent = true;
        }
    }

    public boolean setContactIdentity(String val) {

        if (val.length() < 1)
          return false;
        if (val.length() > 254)
          return false;
        m_contactIdentityPresent = true;
        m_contactIdentity = val;
        return true;
    }

    public String getContactIdentity() {

        return m_contactIdentity;
    }

    public boolean hasContactIdentity() {

        return m_contactIdentityPresent;
    }

    public boolean setDetails(String val) {

        if (val.length() > 50)
          return false;
        m_detailsPresent = true;
        m_details = val;
        return true;
    }

    public String getDetails() {

        return m_details;
    }

    public boolean hasDetails() {

        return m_detailsPresent;
    }

    public boolean setStatus(boolean val) {

        m_statusPresent = true;
        m_status = val;
        return true;
    }

    public boolean getStatus() {

        return m_status;
    }

    public String toXML() {

        String xml = "<ServerStatus";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasContactIdentity() ) {
            xml += " ContactIdentity=\"" + encode( m_contactIdentity) + "\"";
        }
        if ( hasDetails() ) {
            xml += " Details=\"" + encode( m_details) + "\"";
        }
        if ( m_statusPresent ) {
            xml += " Status=\"" + m_status + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        xml += "/>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "ServerStatus\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasContactIdentity() ) {
            str +=  lead + "    ContactIdentity = " + m_contactIdentity + "\n";
        }
        if ( hasDetails() ) {
            str +=  lead + "    Details = " + m_details + "\n";
        }
            str +=  lead + "    Status = " + m_status + "\n";
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
		if(m_contactIdentityPresent)
			map.addAttributeValue("serverStatus_contactIdentity", m_contactIdentity);
		if(m_detailsPresent)
			map.addAttributeValue("serverStatus_details", m_details);
		if(m_statusPresent)
			map.addAttributeValue("serverStatus_status", m_status);
	}
}