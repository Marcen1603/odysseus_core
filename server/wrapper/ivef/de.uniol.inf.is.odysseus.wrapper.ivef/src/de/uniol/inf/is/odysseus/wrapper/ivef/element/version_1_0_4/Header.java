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
public class Header implements IIvefElement { 

    private String m_msgRefId; // default value is uninitialized
    private boolean m_msgRefIdPresent;
    private String m_version; // default value is uninitialized
    private boolean m_versionPresent;

    public Header() {

        m_msgRefIdPresent = false;
        m_version = "0.2.5";
        m_versionPresent = true;
    }

    public Header(Header val) {

        m_msgRefId = val.getMsgRefId();
        if (val != null) {
            m_msgRefIdPresent = true;
        }
        m_version = val.getVersion();
        if (val != null) {
            m_versionPresent = true;
        }
    }

    public boolean setMsgRefId(String val) {

        if (val.length() < 36)
          return false;
        if (val.length() > 42)
          return false;
        m_msgRefIdPresent = true;
        m_msgRefId = val;
        return true;
    }

    public String getMsgRefId() {

        return m_msgRefId;
    }

    public boolean setVersion(String val) {

        m_versionPresent = true;
        m_version = val;
        return true;
    }

    public String getVersion() {

        return m_version;
    }

    public String toXML() {

        String xml = "<Header";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( m_msgRefIdPresent ) {
            xml += " MsgRefId=\"" + encode( m_msgRefId) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( m_versionPresent ) {
            xml += " Version=\"" + encode( m_version) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        xml += "/>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Header\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

            str +=  lead + "    MsgRefId = " + m_msgRefId + "\n";
            str +=  lead + "    Version = " + m_version + "\n";
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
		if (m_msgRefIdPresent)
			map.addAttributeValue("header_msgRefId", m_msgRefId);
		if (m_versionPresent)
			map.addAttributeValue("header_version", m_version);
	}


}
