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
public class Transmission implements IIvefElement { 

    private int m_type; // default value is uninitialized
    private boolean m_typePresent;
    private double m_period; // default value is uninitialized
    private boolean m_periodPresent;

    public Transmission() {

        m_typePresent = false;
        m_periodPresent = false;
    }

    public Transmission(Transmission val) {

        m_type = val.getType();
        if (val != null) {
            m_typePresent = true;
        }
        m_period = val.getPeriod();
        if (val != null) {
            m_periodPresent = true;
        }
    }

    public boolean setType(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) )
            return false;
        m_typePresent = true;
        m_type = val;
        return true;
    }

    public int getType() {

        return m_type;
    }

    public boolean setPeriod(double val) {

        m_periodPresent = true;
        m_period = val;
        return true;
    }

    public double getPeriod() {

        return m_period;
    }

    public boolean hasPeriod() {

        return m_periodPresent;
    }

    public String toXML() {

        String xml = "<Transmission";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( m_typePresent ) {
            xml += " Type=\"" + m_type + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( hasPeriod() ) {
            xml += " Period=\"" + nf.format(m_period) + "\"";
        }
        xml += "/>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Transmission\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

            str +=  lead + "    Type = " + m_type + "\n";
        if ( hasPeriod() ) {
            str +=  lead + "    Period = " + nf.format(m_period) + "\n";
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
		if(m_typePresent)
			map.addAttributeValue("transmission_type", m_type);	
		if(m_periodPresent)
			map.addAttributeValue("transmission_period", m_period);
	}
}
