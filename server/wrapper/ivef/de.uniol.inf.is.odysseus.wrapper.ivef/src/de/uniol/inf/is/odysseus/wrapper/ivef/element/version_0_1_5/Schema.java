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


public class Schema implements IIvefElement { 


    public Schema() {

    }

    public Schema(Schema val) {

    }

    public String getElementFormDefault() {

        return "qualified";
    }

    public String getTargetNamespace() {

        return "urn:http://www.ivef.org/XMLSchema/IVEF/0.1.5";
    }
    @Override
    public String toXML() {

        String xml = "<Schema";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += ">\n";
        xml += "</Schema>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Schema\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
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
		// TODO Auto-generated method stub
		
	}


}
