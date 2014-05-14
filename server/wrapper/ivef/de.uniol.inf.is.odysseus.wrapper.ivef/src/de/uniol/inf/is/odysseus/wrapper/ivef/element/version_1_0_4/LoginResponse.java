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
public class LoginResponse implements IIvefElement { 

    private String m_reason; // default value is uninitialized
    private boolean m_reasonPresent;
    private String m_responseOn; // default value is uninitialized
    private boolean m_responseOnPresent;
    private int m_result; // default value is uninitialized
    private boolean m_resultPresent;

    public LoginResponse() {

        m_reasonPresent = false;
        m_responseOnPresent = false;
        m_resultPresent = false;
    }

    public LoginResponse(LoginResponse val) {

        m_reason = val.getReason();
        if (val != null) {
            m_reasonPresent = true;
        }
        m_responseOn = val.getResponseOn();
        if (val != null) {
            m_responseOnPresent = true;
        }
        m_result = val.getResult();
        if (val != null) {
            m_resultPresent = true;
        }
    }

    public boolean setReason(String val) {

        if (val.length() > 256)
          return false;
        m_reasonPresent = true;
        m_reason = val;
        return true;
    }

    public String getReason() {

        return m_reason;
    }

    public boolean hasReason() {

        return m_reasonPresent;
    }

    public boolean setResponseOn(String val) {

        if (val.length() < 36)
          return false;
        if (val.length() > 42)
          return false;
        m_responseOnPresent = true;
        m_responseOn = val;
        return true;
    }

    public String getResponseOn() {

        return m_responseOn;
    }

    public boolean setResult(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) )
            return false;
        m_resultPresent = true;
        m_result = val;
        return true;
    }

    public int getResult() {

        return m_result;
    }

    public String toXML() {

        String xml = "<LoginResponse";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasReason() ) {
            xml += " Reason=\"" + encode( m_reason) + "\"";
        }
        if ( m_responseOnPresent ) {
            xml += " ResponseOn=\"" + encode( m_responseOn) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( m_resultPresent ) {
            xml += " Result=\"" + m_result + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        xml += "/>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "LoginResponse\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasReason() ) {
            str +=  lead + "    Reason = " + m_reason + "\n";
        }
            str +=  lead + "    ResponseOn = " + m_responseOn + "\n";
            str +=  lead + "    Result = " + m_result + "\n";
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
		if (m_reasonPresent)
			map.addAttributeValue("loginResponse_reason", m_reason);
		if (m_responseOnPresent)
			map.addAttributeValue("loginResponse_responseOn", m_responseOn);
		if (m_resultPresent)
			map.addAttributeValue("loginResponse_result", m_result);
	}


}
