package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Body;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.Header;

/**
 * @author msalous
 *
 */
public class MSG_IVEF implements IIvefElement { 

    private Header m_header; // default value is uninitialized
    private boolean m_headerPresent;
    private Body m_body; // default value is uninitialized
    private boolean m_bodyPresent;

    public MSG_IVEF() {

        m_headerPresent = false;
        m_bodyPresent = false;
    }

    public MSG_IVEF(MSG_IVEF val) {

        m_header = val.getHeader();
        if (val != null) {
            m_headerPresent = true;
        }
        m_body = val.getBody();
        if (val != null) {
            m_bodyPresent = true;
        }
    }

    public boolean setHeader(Header val) {

        m_headerPresent = true;
        m_header = val;
        return true;
    }

    public Header getHeader() {

        return m_header;
    }

    public boolean setBody(Body val) {

        m_bodyPresent = true;
        m_body = val;
        return true;
    }

    public Body getBody() {

        return m_body;
    }
    @Override
    public String toXML() {

        String xml = "<MSG_IVEF";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        String dataMember;
        xml += ">\n";
        if ( m_headerPresent ) {
            dataMember =  m_header.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } else { 
            return null; // not all required data members have been set 
        } 
        if ( m_bodyPresent ) {
            dataMember =  m_body.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } else { 
            return null; // not all required data members have been set 
        } 
        xml += "</MSG_IVEF>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "MSG_IVEF\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

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
    
    public KeyValueObject<? extends IMetaAttribute> toMap() {
    	KeyValueObject<? extends IMetaAttribute> map = new KeyValueObject<>();
    	fillMap(map);
    	return map;
    }

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map) {
		if (m_headerPresent)
			m_header.fillMap(map);
		if(m_bodyPresent)
			m_body.fillMap(map);
	}


}
