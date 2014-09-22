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


public class Object implements IIvefElement { 

    private String m_fileName = "";//Initialized instead of using a flag! // default value is uninitialized

    public Object() {

    }

    public Object(Object val) {

        m_fileName = val.getFileName();
    }

    public void setFileName(String val) {

        m_fileName = val;
    }

    public String getFileName() {

        return m_fileName;
    }
    @Override
    public String toXML() {

        String xml = "<Object";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += " FileName=\"" + encode( m_fileName) + "\"";
        xml += ">\n";
        xml += "</Object>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Object\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        str +=  lead + "    FileName = " + m_fileName + "\n";
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
		map.addAttributeValue("object_fileName", m_fileName);
	}
}
