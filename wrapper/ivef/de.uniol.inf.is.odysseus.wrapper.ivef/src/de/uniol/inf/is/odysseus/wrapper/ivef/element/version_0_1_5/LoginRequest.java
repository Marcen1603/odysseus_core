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


public class LoginRequest implements IIvefElement { 

    private String m_name = "";//Initialized instead of using flag! // default value is uninitialized
    private String m_password = "";//Initialized instead of using flag! // default value is uninitialized
    private int m_encryption; // default value is uninitialized

    public LoginRequest() {

    }

    public LoginRequest(LoginRequest val) {

        m_name = val.getName();
        m_password = val.getPassword();
        m_encryption = val.getEncryption();
    }

    public void setName(String val) {

        m_name = val;
    }

    public String getName() {

        return m_name;
    }

    public void setPassword(String val) {

        m_password = val;
    }

    public String getPassword() {

        return m_password;
    }

    public void setEncryption(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) )
            return;
        m_encryption = val;
    }

    public int getEncryption() {

        return m_encryption;
    }
    @Override
    public String toXML() {

        String xml = "<LoginRequest";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += " Name=\"" + encode( m_name) + "\"";
        xml += " Password=\"" + encode( m_password) + "\"";
        xml += " Encryption=\"" + m_encryption + "\"";
        xml += ">\n";
        xml += "</LoginRequest>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "LoginRequest\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        str +=  lead + "    Name = " + m_name + "\n";
        str +=  lead + "    Password = " + m_password + "\n";
        str +=  lead + "    Encryption = " + m_encryption + "\n";
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
		map.addAttributeValue("loginRequest_name", m_name);
		map.addAttributeValue("loginRequest_password", m_password);
		map.addAttributeValue("loginRequest_encryption", m_encryption);
	}


}
