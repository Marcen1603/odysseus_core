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


public class Header implements IIvefElement { 

    private String m_version = "";//Initialized instead of using flag! // default value is uninitialized
    private String m_msgRefId= "";//Initialized instead of using flag! // default value is uninitialized

    public Header() {

        m_version = "0.1.5";
    }

    public Header(Header val) {

        m_version = val.getVersion();
        m_msgRefId = val.getMsgRefId();
    }

    public void setVersion(String val) {

        m_version = val;
    }

    public String getVersion() {

        return m_version;
    }

    public void setMsgRefId(String val) {

        m_msgRefId = val;
    }

    public String getMsgRefId() {

        return m_msgRefId;
    }
    @Override
    public String toXML() {

        String xml = "<Header";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += " Version=\"" + encode( m_version) + "\"";
        xml += " MsgRefId=\"" + encode( m_msgRefId) + "\"";
        xml += ">\n";
        xml += "</Header>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Header\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        str +=  lead + "    Version = " + m_version + "\n";
        str +=  lead + "    MsgRefId = " + m_msgRefId + "\n";
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
		map.addAttributeValue("header_msgRefId", m_msgRefId);
		map.addAttributeValue("header_version", m_version);
	}


}
