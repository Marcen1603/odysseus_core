package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5;

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
public class OtherName implements IIvefElement { 

    private String m_lang; // default value is uninitialized
    private boolean m_langPresent;
    private String m_name; // default value is uninitialized
    private boolean m_namePresent;

    public OtherName() {

        m_langPresent = false;
        m_namePresent = false;
    }

    public OtherName(OtherName val) {

        m_lang = val.getLang();
        if (val != null) {
            m_langPresent = true;
        }
        m_name = val.getName();
        if (val != null) {
            m_namePresent = true;
        }
    }

    public boolean setLang(String val) {

        if (val.length() < 2)
          return false;
        if (val.length() > 2)
          return false;
        m_langPresent = true;
        m_lang = val;
        return true;
    }

    public String getLang() {

        return m_lang;
    }

    public boolean setName(String val) {

        if (val.length() < 1)
          return false;
        if (val.length() > 42)
          return false;
        m_namePresent = true;
        m_name = val;
        return true;
    }

    public String getName() {

        return m_name;
    }
    @Override
    public String toXML() {

        String xml = "<OtherName";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( m_langPresent ) {
            xml += " Lang=\"" + encode( m_lang) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( m_namePresent ) {
            xml += " Name=\"" + encode( m_name) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        xml += "/>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "OtherName\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

            str +=  lead + "    Lang = " + m_lang + "\n";
            str +=  lead + "    Name = " + m_name + "\n";
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
		if(m_langPresent)
			map.addAttributeValue("otherName_lang", m_lang);
		if(m_namePresent)
			map.addAttributeValue("otherName_name", m_name);
	}
}
