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
public class UnType implements IIvefElement { 

    private String m_codeA; // default value is uninitialized
    private boolean m_codeAPresent;
    private String m_codeB; // default value is uninitialized
    private boolean m_codeBPresent;
    private int m_mode; // default value is uninitialized
    private boolean m_modePresent;

    public UnType() {

        m_codeAPresent = false;
        m_codeBPresent = false;
        m_modePresent = false;
    }

    public UnType(UnType val) {

        m_codeA = val.getCodeA();
        if (val != null) {
            m_codeAPresent = true;
        }
        m_codeB = val.getCodeB();
        if (val != null) {
            m_codeBPresent = true;
        }
        m_mode = val.getMode();
        if (val != null) {
            m_modePresent = true;
        }
    }

    public boolean setCodeA(String val) {

        m_codeAPresent = true;
        m_codeA = val;
        return true;
    }

    public String getCodeA() {

        return m_codeA;
    }

    public boolean setCodeB(String val) {

        m_codeBPresent = true;
        m_codeB = val;
        return true;
    }

    public String getCodeB() {

        return m_codeB;
    }

    public boolean hasCodeB() {

        return m_codeBPresent;
    }

    public boolean setMode(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) &&
             ( val != 4 ) &&
             ( val != 6 ) &&
             ( val != 7 ) &&
             ( val != 8 ) )
            return false;
        m_modePresent = true;
        m_mode = val;
        return true;
    }

    public int getMode() {

        return m_mode;
    }
    @Override
    public String toXML() {

        String xml = "<UnType";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( m_codeAPresent ) {
            xml += " CodeA=\"" + encode( m_codeA) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( hasCodeB() ) {
            xml += " CodeB=\"" + encode( m_codeB) + "\"";
        }
        if ( m_modePresent ) {
            xml += " Mode=\"" + m_mode + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        xml += "/>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "UnType\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

            str +=  lead + "    CodeA = " + m_codeA + "\n";
        if ( hasCodeB() ) {
            str +=  lead + "    CodeB = " + m_codeB + "\n";
        }
            str +=  lead + "    Mode = " + m_mode + "\n";
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
		if(m_codeAPresent) 
			map.addAttributeValue("unType_codeA", m_codeA);
		if(m_codeBPresent)
			map.addAttributeValue("unType_codeB", m_codeB);
		if(m_modePresent)
			map.addAttributeValue("unType_mode", m_mode);
	}
}
