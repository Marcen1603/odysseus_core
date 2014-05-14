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

public class Area implements IIvefElement { 

    private Vector<Pos> m_poss = new Vector<Pos>();
    private String m_name; // default value is uninitialized
    private boolean m_namePresent;

    public Area() {

        m_namePresent = false;
    }

    public Area(Area val) {

        for(int i=0; i < val.countOfPoss(); i++ ) {
            m_poss.add( val.getPosAt(i) );
        }
        m_name = val.getName();
        if (val != null) {
            m_namePresent = true;
        }
    }

    public boolean removePos(Pos val) {
        if (m_poss.size() <= 3) {
            return false; // scalar already at minOccurs
        }

        m_poss.remove(val);
        return true;
    }

    public boolean addPos(Pos val) {

        m_poss.add(val);
        return true;
    }

    public Pos getPosAt(int i) {

        return m_poss.get(i);
    }

    public int countOfPoss() {

        return m_poss.size();
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

    public boolean hasName() {

        return m_namePresent;
    }

    public String toXML() {

        String xml = "<Area";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasName() ) {
            xml += " Name=\"" + encode( m_name) + "\"";
        }
        String dataMember;
        xml += ">\n";
        if (m_poss.size() < 3) {
            return null; // not enough values
        }
        for(int i=0; i < m_poss.size(); i++ ) {
           Pos attribute = m_poss.get(i);
            dataMember = attribute.toXML();
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        xml += "</Area>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Area\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasName() ) {
            str +=  lead + "    Name = " + m_name + "\n";
        }
        for(int i=0; i < m_poss.size(); i++ ) {
           Pos attribute = m_poss.get(i);
           str += attribute.toString(lead + "    ");
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
		for (Pos pos : m_poss)
			pos.fillMap(map);
		if(m_namePresent)
			map.addAttributeValue("area_name", m_name);
	}
}
