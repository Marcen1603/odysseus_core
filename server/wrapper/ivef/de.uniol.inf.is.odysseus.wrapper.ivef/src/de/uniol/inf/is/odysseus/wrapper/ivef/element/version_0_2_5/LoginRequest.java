package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;

/**
 * @author msalous
 *
 */
public class LoginRequest implements IIvefElement { 

    private int m_encryption; // default value is uninitialized
    private boolean m_encryptionPresent;
    private String m_name; // default value is uninitialized
    private boolean m_namePresent;
    private String m_password; // default value is uninitialized
    private boolean m_passwordPresent;

    public LoginRequest() {

        m_encryptionPresent = false;
        m_namePresent = false;
        m_passwordPresent = false;
    }

    public LoginRequest(LoginRequest val) {

        m_encryption = val.getEncryption();
        if (val != null) {
            m_encryptionPresent = true;
        }
        m_name = val.getName();
        if (val != null) {
            m_namePresent = true;
        }
        m_password = val.getPassword();
        if (val != null) {
            m_passwordPresent = true;
        }
    }

    public boolean setEncryption(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) )
            return false;
        m_encryptionPresent = true;
        m_encryption = val;
        return true;
    }

    public int getEncryption() {

        return m_encryption;
    }

    public boolean setName(String val) {

        if (val.length() > 256)
          return false;
        m_namePresent = true;
        m_name = val;
        return true;
    }

    public String getName() {

        return m_name;
    }

    public boolean setPassword(String val) {

        if (val.length() > 256)
          return false;
        m_passwordPresent = true;
        m_password = val;
        return true;
    }

    public String getPassword() {

        return m_password;
    }
    @Override
    public String toXML() {

        String xml = "<LoginRequest";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( m_encryptionPresent ) {
            xml += " Encryption=\"" + m_encryption + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( m_namePresent ) {
            xml += " Name=\"" + encode( m_name) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( m_passwordPresent ) {
            xml += " Password=\"" + encode( m_password) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        xml += "/>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "LoginRequest\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

            str +=  lead + "    Encryption = " + m_encryption + "\n";
            str +=  lead + "    Name = " + m_name + "\n";
            str +=  lead + "    Password = " + m_password + "\n";
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
		if (m_encryptionPresent)
			map.addAttributeValue("loginRequest_encryption", m_encryption);
		if (m_namePresent)
			map.addAttributeValue("loginRequest_name", m_name);
		if (m_passwordPresent)
			map.addAttributeValue("loginRequest_password", m_password);
	}
}
