/* 
 *
 */

package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5;

//import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;


public class LoginResponse implements IIvefElement { 

    private String m_msgId = "";//Initialized instead of using flag! // default value is uninitialized
    private int m_result; // default value is uninitialized
    private String m_reason = "";//Initialized instead of using flag! // default value is uninitialized
    private boolean m_reasonPresent;

    public LoginResponse() {

        m_reasonPresent = false;
    }

    public LoginResponse(LoginResponse val) {

        m_msgId = val.getMsgId();
        m_result = val.getResult();
        m_reasonPresent = val.hasReason();
        m_reason = val.getReason();
    }

    public void setMsgId(String val) {

        m_msgId = val;
    }

    public String getMsgId() {

        return m_msgId;
    }

    public void setResult(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) )
            return;
        m_result = val;
    }

    public int getResult() {

        return m_result;
    }

    public void setReason(String val) {

        m_reasonPresent = true;
        m_reason = val;
    }

    public String getReason() {

        return m_reason;
    }

    public boolean hasReason() {

        return m_reasonPresent;
    }
    @Override
    public String toXML() {

        String xml = "<LoginResponse";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += " MsgId=\"" + encode( m_msgId) + "\"";
        xml += " Result=\"" + m_result + "\"";
        if ( hasReason() ) {
            xml += " Reason=\"" + encode( m_reason) + "\"";
        }
        xml += ">\n";
        xml += "</LoginResponse>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "LoginResponse\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        str +=  lead + "    MsgId = " + m_msgId + "\n";
        str +=  lead + "    Result = " + m_result + "\n";
        if ( hasReason() ) {
            str +=  lead + "    Reason = " + m_reason + "\n";
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
		map.addAttributeValue("loginResponse_msgId", m_msgId);
		map.addAttributeValue("loginResponse_result", m_result);
		if(m_reasonPresent)
			map.addAttributeValue("loginResponse_reason", m_reason);
	}
}
