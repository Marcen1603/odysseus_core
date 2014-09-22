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


public class Pos implements IIvefElement { 

    private Double m_lat = (double)0; // default value is uninitialized
    private Double m_long = (double)0; // default value is uninitialized

    public Pos() {

    }

    public Pos(Pos val) {

        m_lat = val.getLat();
        m_long = val.getLong();
    }

    public void setLat(Double val) {

        if (val < -90)
          return;
        if (val > 90)
          return;
        m_lat = val;
    }

    public Double getLat() {

        return m_lat;
    }

    public void setLong(Double val) {

        if (val < -180)
          return;
        if (val > 180)
          return;
        m_long = val;
    }

    public Double getLong() {

        return m_long;
    }
    @Override
    public String toXML() {

        String xml = "<Pos";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += " Lat=\"" + m_lat + "\"";
        xml += " Long=\"" + m_long + "\"";
        xml += ">\n";
        xml += "</Pos>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Pos\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        str +=  lead + "    Lat = " + m_lat + "\n";
        str +=  lead + "    Long = " + m_long + "\n";
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
		map.addAttributeValue("pos_lat", m_lat);
		map.addAttributeValue("pos_long", m_long);
	}


}
