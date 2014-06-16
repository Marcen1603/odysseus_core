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


public class Transmission implements IIvefElement { 

    private int m_type; // default value is uninitialized
    private double m_period; // default value is uninitialized
    private boolean m_periodPresent;

    public Transmission() {

        m_periodPresent = false;
    }

    public Transmission(Transmission val) {

        m_type = val.getType();
        m_periodPresent = val.hasPeriod();
        m_period = val.getPeriod();
    }

    public void setType(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) &&
             ( val != 4 ) )
            return;
        m_type = val;
    }

    public int getType() {

        return m_type;
    }

    public void setPeriod(double val) {

        m_periodPresent = true;
        m_period = val;
    }

    public double getPeriod() {

        return m_period;
    }

    public boolean hasPeriod() {

        return m_periodPresent;
    }
    @Override
    public String toXML() {

        String xml = "<Transmission";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += " Type=\"" + m_type + "\"";
        if ( hasPeriod() ) {
            xml += " Period=\"" + m_period + "\"";
        }
        xml += ">\n";
        xml += "</Transmission>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Transmission\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        str +=  lead + "    Type = " + m_type + "\n";
        if ( hasPeriod() ) {
            str +=  lead + "    Period = " + m_period + "\n";
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
		map.addAttributeValue("transmission_type", m_type);
		if(m_periodPresent)
			map.addAttributeValue("transmission_period", m_period);
	}


}
