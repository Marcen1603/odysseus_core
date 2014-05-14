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


public class ServerStatus implements IIvefElement { 

    private String m_status = "";//Initialized  instead of using a flag! // default value is uninitialized
    private String m_details = "";//Initialized  instead of using a flag! // default value is uninitialized
    private boolean m_detailsPresent;

    public ServerStatus() {

        m_detailsPresent = false;
    }

    public ServerStatus(ServerStatus val) {

        m_status = val.getStatus();
        m_detailsPresent = val.hasDetails();
        m_details = val.getDetails();
    }

    public void setStatus(String val) {

        if ( ( val != "queuefull" ) &&
             ( val != "ok" ) )
            return;
        m_status = val;
    }

    public String getStatus() {

        return m_status;
    }

    public void setDetails(String val) {

        m_detailsPresent = true;
        m_details = val;
    }

    public String getDetails() {

        return m_details;
    }

    public boolean hasDetails() {

        return m_detailsPresent;
    }

    public String toXML() {

        String xml = "<ServerStatus";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += " Status=\"" + encode( m_status) + "\"";
        if ( hasDetails() ) {
            xml += " Details=\"" + encode( m_details) + "\"";
        }
        xml += ">\n";
        xml += "</ServerStatus>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "ServerStatus\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        str +=  lead + "    Status = " + m_status + "\n";
        if ( hasDetails() ) {
            str +=  lead + "    Details = " + m_details + "\n";
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
		map.addAttributeValue("serverStatus_status", m_status);
		if(m_detailsPresent)
			map.addAttributeValue("serverStatus_details", m_details);
	}


}
