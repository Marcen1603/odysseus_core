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


public class TaggedItem implements IIvefElement { 

    private String m_key = "";//Initialized  instead of using a flag! // default value is uninitialized
    private String m_value = "";//Initialized  instead of using a flag! // default value is uninitialized

    public TaggedItem() {

    }

    public TaggedItem(TaggedItem val) {

        m_key = val.getKey();
        m_value = val.getValue();
    }

    public void setKey(String val) {

        m_key = val;
    }

    public String getKey() {

        return m_key;
    }

    public void setValue(String val) {

        m_value = val;
    }

    public String getValue() {

        return m_value;
    }

    public String toXML() {

        String xml = "<TaggedItem";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += " Key=\"" + encode( m_key) + "\"";
        xml += " Value=\"" + encode( m_value) + "\"";
        xml += ">\n";
        xml += "</TaggedItem>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "TaggedItem\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
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
		map.addAttributeValue("taggedItem_key", m_key);
		map.addAttributeValue("taggedItem_value", m_value);
	}


}
