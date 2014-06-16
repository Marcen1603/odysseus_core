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


public class Pong implements IIvefElement { 

    private Date m_timeStamp; // default value is uninitialized
    private String m_msgId = "";//initialized instead of using a flag! // default value is uninitialized
    private int m_sourceId; // default value is uninitialized

    public Pong() {

    }

    public Pong(Pong val) {

        m_timeStamp = val.getTimeStamp();
        m_msgId = val.getMsgId();
        m_sourceId = val.getSourceId();
    }

    public void setTimeStamp(Date val) {

        m_timeStamp = val;
    }

    public Date getTimeStamp() {

        return m_timeStamp;
    }

    public void setMsgId(String val) {

        m_msgId = val;
    }

    public String getMsgId() {

        return m_msgId;
    }

    public void setSourceId(int val) {

        m_sourceId = val;
    }

    public int getSourceId() {

        return m_sourceId;
    }
    @Override
    public String toXML() {

        String xml = "<Pong";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");

        xml += " TimeStamp=\"" + df.format(m_timeStamp) + "\"";
        xml += " MsgId=\"" + encode( m_msgId) + "\"";
        xml += " SourceId=\"" + m_sourceId + "\"";
        xml += ">\n";
        xml += "</Pong>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Pong\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");

        str +=  lead + "    TimeStamp = " + df.format(m_timeStamp) + "\n";
        str +=  lead + "    MsgId = " + m_msgId + "\n";
        str +=  lead + "    SourceId = " + m_sourceId + "\n";
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
		map.addAttributeValue("pong_timeStamp", m_timeStamp);
		map.addAttributeValue("pong_msgId", m_msgId);
		map.addAttributeValue("pong_sourceId", m_sourceId);
	}
}
