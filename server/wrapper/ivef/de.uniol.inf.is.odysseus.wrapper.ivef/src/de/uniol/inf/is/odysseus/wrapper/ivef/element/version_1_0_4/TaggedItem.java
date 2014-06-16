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
public class TaggedItem implements IIvefElement { 

    private String m_key; // default value is uninitialized
    private boolean m_keyPresent;
    private String m_value; // default value is uninitialized
    private boolean m_valuePresent;

    public TaggedItem() {

        m_keyPresent = false;
        m_valuePresent = false;
    }

    public TaggedItem(TaggedItem val) {

        m_key = val.getKey();
        if (val != null) {
            m_keyPresent = true;
        }
        m_value = val.getValue();
        if (val != null) {
            m_valuePresent = true;
        }
    }

    public boolean setKey(String val) {

        if (val.length() < 1)
          return false;
        if (val.length() > 42)
          return false;
        m_keyPresent = true;
        m_key = val;
        return true;
    }

    public String getKey() {

        return m_key;
    }

    public boolean setValue(String val) {

        m_valuePresent = true;
        m_value = val;
        return true;
    }

    public String getValue() {

        return m_value;
    }
    @Override
    public String toXML() {

        String xml = "<TaggedItem";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( m_keyPresent ) {
            xml += " Key=\"" + encode( m_key) + "\"";
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

        String str = lead + "TaggedItem\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

            str +=  lead + "    Key = " + m_key + "\n";
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
		if(m_keyPresent)
			map.addAttributeValue("taggedItem_key", m_key);	
		if(m_valuePresent)
			map.addAttributeValue("taggedItem_value", m_value);
	}
}
