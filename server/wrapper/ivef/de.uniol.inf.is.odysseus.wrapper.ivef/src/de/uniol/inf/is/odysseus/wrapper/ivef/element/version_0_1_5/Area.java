/* 
 *
 */

package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Pos;

public class Area implements IIvefElement { 

    private Vector<Pos> m_poss = new Vector<Pos>();

    public Area() { 

    }

    public Area(Area val) {

        for(int i=0; i < val.countOfPoss(); i++ ) {
            m_poss.add( val.getPosAt(i) );
        }
    }

    public void addPos(Pos val) {

        m_poss.add(val);
    }

    public Pos getPosAt(int i) {

        return (Pos) m_poss.get(i);
    }

    public int countOfPoss() {

        return m_poss.size();
    }

    public String toXML() {

        String xml = "<Area";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += ">\n";
        for(int i=0; i < m_poss.size(); i++ ) {
           Pos attribute = (Pos) m_poss.get(i);
            xml += attribute.toXML();
        }
        xml += "</Area>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Area\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        for(int i=0; i < m_poss.size(); i++ ) {
           Pos attribute = (Pos) m_poss.get(i);
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
		for(Pos pos : m_poss)
			pos.fillMap(map);
	}


}
