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
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Header;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Body;

public class MSG_LoginResponse implements IIvefElement { 

    private Header m_header; // default value is uninitialized
    private Body m_body; // default value is uninitialized

    public MSG_LoginResponse() {

    }

    public MSG_LoginResponse(MSG_LoginResponse val) {

        m_header = val.getHeader();
        m_body = val.getBody();
    }

    public void setHeader(Header val) {

        m_header = val;
    }

    public Header getHeader() {

        return m_header;
    }

    public void setBody(Body val) {

        m_body = val;
    }

    public Body getBody() {

        return m_body;
    }

    public String toXML() {

        String xml = "<MSG_LoginResponse";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += ">\n";
        if(m_header != null) // instead of using a flag!
        	xml +=  m_header.toXML();
        if(m_body != null)	 // instead of using a flag!
        	xml +=  m_body.toXML();
        xml += "</MSG_LoginResponse>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "MSG_LoginResponse\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        str +=  m_header.toString(lead + "    ");
        str +=  m_body.toString(lead + "    ");
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
		if (m_header != null)
			m_header.fillMap(map);
		if(m_body != null)
			m_body.fillMap(map);
	}

	public KeyValueObject<? extends IMetaAttribute> toMap() {
		KeyValueObject<? extends IMetaAttribute> map = new KeyValueObject<>();
    	fillMap(map);
    	return map;
	}


}
