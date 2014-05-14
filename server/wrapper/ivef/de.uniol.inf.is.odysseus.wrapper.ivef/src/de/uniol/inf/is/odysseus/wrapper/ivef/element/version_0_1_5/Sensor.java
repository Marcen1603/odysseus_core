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


public class Sensor implements IIvefElement { 

    private double m_senId; // default value is uninitialized
    private double m_trkId; // default value is uninitialized

    public Sensor() {

    }

    public Sensor(Sensor val) {

        m_senId = val.getSenId();
        m_trkId = val.getTrkId();
    }

    public void setSenId(double val) {

        if (val < 0)
          return;
        if (val > 65536)
          return;
        m_senId = val;
    }

    public double getSenId() {

        return m_senId;
    }

    public void setTrkId(double val) {

        if (val < 0)
          return;
        if (val > 65536)
          return;
        m_trkId = val;
    }

    public double getTrkId() {

        return m_trkId;
    }

    public String toXML() {

        String xml = "<Sensor";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += " SenId=\"" + m_senId + "\"";
        xml += " TrkId=\"" + m_trkId + "\"";
        xml += ">\n";
        xml += "</Sensor>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Sensor\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));	
        str +=  lead + "    SenId = " + m_senId + "\n";
        str +=  lead + "    TrkId = " + m_trkId + "\n";
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
		map.addAttributeValue("sensor_senId", m_senId);
		map.addAttributeValue("sensor_trkId", m_trkId);
	}


}
