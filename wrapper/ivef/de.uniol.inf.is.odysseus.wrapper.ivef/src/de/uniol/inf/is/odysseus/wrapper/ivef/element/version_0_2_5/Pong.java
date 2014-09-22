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
public class Pong implements IIvefElement { 

    private String m_responseOn; // default value is uninitialized
    private boolean m_responseOnPresent;
    private int m_sourceId; // default value is uninitialized
    private boolean m_sourceIdPresent;
    private Date m_timeStamp; // default value is uninitialized
    private boolean m_timeStampPresent;

    public Pong() {

        m_responseOnPresent = false;
        m_sourceIdPresent = false;
        m_timeStampPresent = false;
    }

    public Pong(Pong val) {

        m_responseOn = val.getResponseOn();
        if (val != null) {
            m_responseOnPresent = true;
        }
        m_sourceId = val.getSourceId();
        if (val != null) {
            m_sourceIdPresent = true;
        }
        m_timeStamp = val.getTimeStamp();
        if (val != null) {
            m_timeStampPresent = true;
        }
    }

    public boolean setResponseOn(String val) {

        if (val.length() < 36)
          return false;
        if (val.length() > 42)
          return false;
        m_responseOnPresent = true;
        m_responseOn = val;
        return true;
    }

    public String getResponseOn() {

        return m_responseOn;
    }

    public boolean setSourceId(int val) {

        m_sourceIdPresent = true;
        m_sourceId = val;
        return true;
    }

    public int getSourceId() {

        return m_sourceId;
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

        String xml = "<Pong";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( m_responseOnPresent ) {
            xml += " ResponseOn=\"" + encode( m_responseOn) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( m_sourceIdPresent ) {
            xml += " SourceId=\"" + m_sourceId + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( m_timeStampPresent ) {
            xml += " TimeStamp=\"" + df.format(m_timeStamp) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        xml += "/>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Pong\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

            str +=  lead + "    ResponseOn = " + m_responseOn + "\n";
            str +=  lead + "    SourceId = " + m_sourceId + "\n";
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
		if(m_responseOnPresent)
			map.addAttributeValue("pong_responseOn", m_responseOn);
		if(m_sourceIdPresent)
			map.addAttributeValue("pong_sourceId", m_sourceId);
		if(m_timeStampPresent)
			map.addAttributeValue("pong_timeStamp", m_timeStamp);
	}
}