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


public class Ping implements IIvefElement { 

    private Date m_timeStamp; // default value is uninitialized

    public Ping() {

    }

    public Ping(Ping val) {

        m_timeStamp = val.getTimeStamp();
    }

    public void setTimeStamp(Date val) {

        m_timeStamp = val;
    }

    public Date getTimeStamp() {

        return m_timeStamp;
    }

    public String toXML() {

        String xml = "<Ping";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");

        xml += " TimeStamp=\"" + df.format(m_timeStamp) + "\"";
        xml += ">\n";
        xml += "</Ping>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Ping\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");

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
		map.addAttributeValue("ping_timeStamp", m_timeStamp);
	}
}
