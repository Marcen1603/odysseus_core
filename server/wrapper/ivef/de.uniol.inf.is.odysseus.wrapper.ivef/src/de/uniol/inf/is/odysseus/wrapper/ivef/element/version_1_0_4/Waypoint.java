package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.Pos;

/**
 * @author msalous
 *
 */
public class Waypoint implements IIvefElement { 

    private Pos m_pos; // default value is uninitialized
    private boolean m_posPresent;
    private Date m_ATA; // default value is uninitialized
    private boolean m_ATAPresent;
    private Date m_ETA; // default value is uninitialized
    private boolean m_ETAPresent;
    private Date m_RTA; // default value is uninitialized
    private boolean m_RTAPresent;
    private String m_loCode; // default value is uninitialized
    private boolean m_loCodePresent;
    private String m_name; // default value is uninitialized
    private boolean m_namePresent;

    public Waypoint() {

        m_posPresent = false;
        m_ATAPresent = false;
        m_ETAPresent = false;
        m_RTAPresent = false;
        m_loCodePresent = false;
        m_namePresent = false;
    }

    public Waypoint(Waypoint val) {

        m_pos = val.getPos();
        if (val != null) {
            m_posPresent = true;
        }
        m_ATA = val.getATA();
        if (val != null) {
            m_ATAPresent = true;
        }
        m_ETA = val.getETA();
        if (val != null) {
            m_ETAPresent = true;
        }
        m_RTA = val.getRTA();
        if (val != null) {
            m_RTAPresent = true;
        }
        m_loCode = val.getLoCode();
        if (val != null) {
            m_loCodePresent = true;
        }
        m_name = val.getName();
        if (val != null) {
            m_namePresent = true;
        }
    }

    public boolean setPos(Pos val) {

        m_posPresent = true;
        m_pos = val;
        return true;
    }

    public Pos getPos() {

        return m_pos;
    }

    public boolean hasPos() {

        return m_posPresent;
    }

    public boolean setATA(Date val) {

        m_ATAPresent = true;
        m_ATA = val;
        return true;
    }

    public Date getATA() {

        return m_ATA;
    }

    public boolean hasATA() {

        return m_ATAPresent;
    }

    public boolean setETA(Date val) {

        m_ETAPresent = true;
        m_ETA = val;
        return true;
    }

    public Date getETA() {

        return m_ETA;
    }

    public boolean hasETA() {

        return m_ETAPresent;
    }

    public boolean setRTA(Date val) {

        m_RTAPresent = true;
        m_RTA = val;
        return true;
    }

    public Date getRTA() {

        return m_RTA;
    }

    public boolean hasRTA() {

        return m_RTAPresent;
    }

    public boolean setLoCode(String val) {

        if (val.length() < 5)
          return false;
        if (val.length() > 15)
          return false;
        m_loCodePresent = true;
        m_loCode = val;
        return true;
    }

    public String getLoCode() {

        return m_loCode;
    }

    public boolean hasLoCode() {

        return m_loCodePresent;
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

    public String toXML() {

        String xml = "<Waypoint";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasATA() ) {
            xml += " ATA=\"" + df.format(m_ATA) + "\"";
        }
        if ( hasETA() ) {
            xml += " ETA=\"" + df.format(m_ETA) + "\"";
        }
        if ( hasRTA() ) {
            xml += " RTA=\"" + df.format(m_RTA) + "\"";
        }
        if ( hasLoCode() ) {
            xml += " LoCode=\"" + encode( m_loCode) + "\"";
        }
        if ( m_namePresent ) {
            xml += " Name=\"" + encode( m_name) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        String dataMember;
        xml += ">\n";
        if ( hasPos() ) {
            dataMember =  m_pos.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        xml += "</Waypoint>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Waypoint\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasATA() ) {
            str +=  lead + "    ATA = " + df.format(m_ATA) + "\n";
        }
        if ( hasETA() ) {
            str +=  lead + "    ETA = " + df.format(m_ETA) + "\n";
        }
        if ( hasRTA() ) {
            str +=  lead + "    RTA = " + df.format(m_RTA) + "\n";
        }
        if ( hasLoCode() ) {
            str +=  lead + "    LoCode = " + m_loCode + "\n";
        }
            str +=  lead + "    Name = " + m_name + "\n";
        if ( hasPos() ) {
            str +=  m_pos.toString(lead + "    ") ;
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
		if(m_posPresent)
			m_pos.fillMap(map);
		if(m_ATAPresent)
			map.addAttributeValue("waypoint_ATA", m_ATA);    
		if(m_ETAPresent)
			map.addAttributeValue("waypoint_ETA", m_ETA);    
		if(m_RTAPresent)
			map.addAttributeValue("waypoint_RTA", m_RTA);
		if(m_loCodePresent)
			map.addAttributeValue("waypoint_loCode", m_loCode);
		if(m_namePresent)
			map.addAttributeValue("waypoint_name", m_name);    
	}
}
