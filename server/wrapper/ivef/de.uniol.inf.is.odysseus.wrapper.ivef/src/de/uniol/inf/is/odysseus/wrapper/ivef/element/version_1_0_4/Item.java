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
public class Item implements IIvefElement { 

    private int m_dataSelector; // default value is uninitialized
    private boolean m_dataSelectorPresent;
    private String m_fieldSelector; // default value is uninitialized
    private boolean m_fieldSelectorPresent;

    public Item() {

        m_dataSelectorPresent = false;
        m_fieldSelectorPresent = false;
    }

    public Item(Item val) {

        m_dataSelector = val.getDataSelector();
        if (val != null) {
            m_dataSelectorPresent = true;
        }
        m_fieldSelector = val.getFieldSelector();
        if (val != null) {
            m_fieldSelectorPresent = true;
        }
    }

    public boolean setDataSelector(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) )
            return false;
        m_dataSelectorPresent = true;
        m_dataSelector = val;
        return true;
    }

    public int getDataSelector() {

        return m_dataSelector;
    }

    public boolean setFieldSelector(String val) {

        if (val.length() < 1)
          return false;
        if (val.length() > 42)
          return false;
        m_fieldSelectorPresent = true;
        m_fieldSelector = val;
        return true;
    }

    public String getFieldSelector() {

        return m_fieldSelector;
    }

    public String toXML() {

        String xml = "<Item";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( m_dataSelectorPresent ) {
            xml += " DataSelector=\"" + m_dataSelector + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( m_fieldSelectorPresent ) {
            xml += " FieldSelector=\"" + encode( m_fieldSelector) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        xml += "/>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Item\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

            str +=  lead + "    DataSelector = " + m_dataSelector + "\n";
            str +=  lead + "    FieldSelector = " + m_fieldSelector + "\n";
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
		if(m_dataSelectorPresent)
			map.addAttributeValue("item_dataSelector", m_dataSelector);
		if(m_fieldSelectorPresent)
			map.addAttributeValue("item_fieldSelector", m_fieldSelector);
	}
}
