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
public class OtherId implements IIvefElement { 

    private String m_id; // default value is uninitialized
    private boolean m_idPresent;
    private String m_value; // default value is uninitialized
    private boolean m_valuePresent;

    public OtherId() {

        m_idPresent = false;
        m_valuePresent = false;
    }

    public OtherId(OtherId val) {

        m_id = val.getId();
        if (val != null) {
            m_idPresent = true;
        }
        m_value = val.getValue();
        if (val != null) {
            m_valuePresent = true;
        }
    }

    public boolean setId(String val) {

        if (val.length() < 1)
          return false;
        if (val.length() > 42)
          return false;
        m_idPresent = true;
        m_id = val;
        return true;
    }

    public String getId() {

        return m_id;
    }

    public boolean setValue(String val) {

        if (val.length() < 1)
          return false;
        if (val.length() > 42)
          return false;
        m_valuePresent = true;
        m_value = val;
        return true;
    }

    public String getValue() {

        return m_value;
    }

    public String toXML() {

        String xml = "<OtherId";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( m_idPresent ) {
            xml += " Id=\"" + encode( m_id) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( m_valuePresent ) {
            xml += " Value=\"" + encode( m_value) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        xml += "/>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "OtherId\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

            str +=  lead + "    Id = " + m_id + "\n";
            str +=  lead + "    Value = " + m_value + "\n";
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
		if(m_idPresent)
			map.addAttributeValue("otherId_id", m_id);
		if(m_valuePresent)
			map.addAttributeValue("otherId_value", m_value);
	}
}
