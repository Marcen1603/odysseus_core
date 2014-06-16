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
public class Ping implements IIvefElement { 

    private Date m_timeStamp; // default value is uninitialized
    private boolean m_timeStampPresent;

    public Ping() {

        m_timeStampPresent = false;
    }

    public Ping(Ping val) {

        m_timeStamp = val.getTimeStamp();
        if (val != null) {
            m_timeStampPresent = true;
        }
    }

    public boolean setTimeStamp(Date val) {

        m_timeStampPresent = true;
        m_timeStamp = val;
        return true;
    }

    public Date getTimeStamp() {

        return m_timeStamp;
    }
    @Override
    public String toXML() {

        String xml = "<Ping";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( m_timeStampPresent ) {
            xml += " TimeStamp=\"" + df.format(m_timeStamp) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        xml += "/>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Ping\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

            str +=  lead + "    TimeStamp = " + df.format(m_timeStamp) + "\n";
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
		if(m_timeStampPresent)
			map.addAttributeValue("ping_timeStamp", m_timeStamp);
	}
}
