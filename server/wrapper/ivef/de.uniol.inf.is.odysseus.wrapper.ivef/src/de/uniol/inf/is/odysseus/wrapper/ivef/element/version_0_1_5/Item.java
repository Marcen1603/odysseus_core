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


public class Item implements IIvefElement { 

    private int m_element; // default value is uninitialized
    private String m_field = "";//Initialized instead of using flag! // default value is uninitialized

    public Item() {

    }

    public Item(Item val) {

        m_element = val.getElement();
        m_field = val.getField();
    }

    public void setElement(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) )
            return;
        m_element = val;
    }

    public int getElement() {

        return m_element;
    }

    public void setField(String val) {

        m_field = val;
    }

    public String getField() {

        return m_field;
    }

    public String toXML() {

        String xml = "<Item";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += " Element=\"" + m_element + "\"";
        xml += " Field=\"" + encode( m_field) + "\"";
        xml += ">\n";
        xml += "</Item>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Item\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        str +=  lead + "    Element = " + m_element + "\n";
        str +=  lead + "    Field = " + m_field + "\n";
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
		map.addAttributeValue("item_element", m_element);
		map.addAttributeValue("item_field", m_field);
	}


}
