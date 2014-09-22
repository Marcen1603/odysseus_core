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
public class Filter implements IIvefElement { 

    private String m_predicate; // default value is uninitialized
    private boolean m_predicatePresent;

    public Filter() {

        m_predicatePresent = false;
    }

    public Filter(Filter val) {

        m_predicate = val.getPredicate();
        if (val != null) {
            m_predicatePresent = true;
        }
    }

    public boolean setPredicate(String val) {

        if (val.length() < 1)
          return false;
        if (val.length() > 1024)
          return false;
        m_predicatePresent = true;
        m_predicate = val;
        return true;
    }

    public String getPredicate() {

        return m_predicate;
    }
    @Override
    public String toXML() {

        String xml = "<Filter";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( m_predicatePresent ) {
            xml += " Predicate=\"" + encode( m_predicate) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        xml += "/>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Filter\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

            str +=  lead + "    Predicate = " + m_predicate + "\n";
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
		if(m_predicatePresent)
			map.addAttributeValue("filter_predicate", m_predicate);
	}
}