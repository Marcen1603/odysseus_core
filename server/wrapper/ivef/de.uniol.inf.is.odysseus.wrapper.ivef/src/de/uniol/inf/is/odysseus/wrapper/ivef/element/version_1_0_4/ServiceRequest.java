package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.Area;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.Transmission;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.Item;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.Filter;

/**
 * @author msalous
 *
 */
public class ServiceRequest implements IIvefElement { 

    private Vector<Area> m_areas = new Vector<Area>();
    private Transmission m_transmission; // default value is uninitialized
    private boolean m_transmissionPresent;
    private Vector<Item> m_items = new Vector<Item>();
    private Filter m_filter; // default value is uninitialized
    private boolean m_filterPresent;

    public ServiceRequest() {

        m_transmissionPresent = false;
        m_filterPresent = false;
    }

    public ServiceRequest(ServiceRequest val) {

        for(int i=0; i < val.countOfAreas(); i++ ) {
            m_areas.add( val.getAreaAt(i) );
        }
        m_transmission = val.getTransmission();
        if (val != null) {
            m_transmissionPresent = true;
        }
        for(int i=0; i < val.countOfItems(); i++ ) {
            m_items.add( val.getItemAt(i) );
        }
        m_filter = val.getFilter();
        if (val != null) {
            m_filterPresent = true;
        }
    }

    public boolean removeArea(Area val) {
        if (m_areas.size() <= 0) {
            return false; // scalar already at minOccurs
        }

        m_areas.remove(val);
        return true;
    }

    public boolean addArea(Area val) {

        m_areas.add(val);
        return true;
    }

    public Area getAreaAt(int i) {

        return m_areas.get(i);
    }

    public int countOfAreas() {

        return m_areas.size();
    }

    public boolean setTransmission(Transmission val) {

        m_transmissionPresent = true;
        m_transmission = val;
        return true;
    }

    public Transmission getTransmission() {

        return m_transmission;
    }

    public boolean removeItem(Item val) {
        if (m_items.size() <= 0) {
            return false; // scalar already at minOccurs
        }

        m_items.remove(val);
        return true;
    }

    public boolean addItem(Item val) {

        m_items.add(val);
        return true;
    }

    public Item getItemAt(int i) {

        return m_items.get(i);
    }

    public int countOfItems() {

        return m_items.size();
    }

    public boolean setFilter(Filter val) {

        m_filterPresent = true;
        m_filter = val;
        return true;
    }

    public Filter getFilter() {

        return m_filter;
    }

    public boolean hasFilter() {

        return m_filterPresent;
    }

    public String toXML() {

        String xml = "<ServiceRequest";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        String dataMember;
        xml += ">\n";
        if (m_areas.size() < 0) {
            return null; // not enough values
        }
        for(int i=0; i < m_areas.size(); i++ ) {
           Area attribute = m_areas.get(i);
            dataMember = attribute.toXML();
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if ( m_transmissionPresent ) {
            dataMember =  m_transmission.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } else { 
            return null; // not all required data members have been set 
        } 
        if (m_items.size() < 0) {
            return null; // not enough values
        }
        for(int i=0; i < m_items.size(); i++ ) {
           Item attribute = m_items.get(i);
            dataMember = attribute.toXML();
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if ( hasFilter() ) {
            dataMember =  m_filter.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        xml += "</ServiceRequest>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "ServiceRequest\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        for(int i=0; i < m_areas.size(); i++ ) {
           Area attribute = m_areas.get(i);
           str += attribute.toString(lead + "    ");
        }
        str +=  m_transmission.toString(lead + "    ");
        for(int i=0; i < m_items.size(); i++ ) {
           Item attribute = m_items.get(i);
           str += attribute.toString(lead + "    ");
        }
        if ( hasFilter() ) {
            str +=  m_filter.toString(lead + "    ") ;
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
		for (Area area : m_areas)
			area.fillMap(map);
		if(m_transmissionPresent)
			m_transmission.fillMap(map);
		for (Item item : m_items)
			item.fillMap(map);
	    if(m_filterPresent)
	    	m_filter.fillMap(map);
	}
}
