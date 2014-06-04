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
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Area;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Transmission;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Item;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Object;

public class ServiceRequest implements IIvefElement { 

    private Vector<Area> m_areas = new Vector<Area>();
    private Transmission m_transmission; // default value is uninitialized
    private Vector<Item> m_items = new Vector<Item>();
    private Vector<Object> m_objects = new Vector<Object>();

    public ServiceRequest() {

    }

    public ServiceRequest(ServiceRequest val) {

        for(int i=0; i < val.countOfAreas(); i++ ) {
            m_areas.add( val.getAreaAt(i) );
        }
        m_transmission = val.getTransmission();
        for(int i=0; i < val.countOfItems(); i++ ) {
            m_items.add( val.getItemAt(i) );
        }
        for(int i=0; i < val.countOfObjects(); i++ ) {
            m_objects.add( val.getObjectAt(i) );
        }
    }

    public void addArea(Area val) {

        m_areas.add(val);
    }

    public Area getAreaAt(int i) {

        return m_areas.get(i);
    }

    public int countOfAreas() {

        return m_areas.size();
    }

    public void setTransmission(Transmission val) {

        m_transmission = val;
    }

    public Transmission getTransmission() {

        return m_transmission;
    }

    public void addItem(Item val) {

        m_items.add(val);
    }

    public Item getItemAt(int i) {

        return m_items.get(i);
    }

    public int countOfItems() {

        return m_items.size();
    }

    public void addObject(Object val) {

        m_objects.add(val);
    }

    public Object getObjectAt(int i) {

        return m_objects.get(i);
    }

    public int countOfObjects() {

        return m_objects.size();
    }

    public String toXML() {

        String xml = "<ServiceRequest";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += ">\n";
        for(int i=0; i < m_areas.size(); i++ ) {
           Area attribute = m_areas.get(i);
            xml += attribute.toXML();
        }
        if(m_transmission != null)	// instead of using a flag!
        	xml +=  m_transmission.toXML();
        for(int i=0; i < m_items.size(); i++ ) {
           Item attribute = m_items.get(i);
            xml += attribute.toXML();
        }
        for(int i=0; i < m_objects.size(); i++ ) {
           Object attribute = m_objects.get(i);
            xml += attribute.toXML();
        }
        xml += "</ServiceRequest>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "ServiceRequest\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        for(int i=0; i < m_areas.size(); i++ ) {
           Area attribute = m_areas.get(i);
           str += attribute.toString(lead + "    ");
        }
        str +=  m_transmission.toString(lead + "    ");
        for(int i=0; i < m_items.size(); i++ ) {
           Item attribute = m_items.get(i);
           str += attribute.toString(lead + "    ");
        }
        for(int i=0; i < m_objects.size(); i++ ) {
           Object attribute = m_objects.get(i);
           str += attribute.toString(lead + "    ");
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
		for(Area area : m_areas)
			area.fillMap(map);
		if(m_transmission != null)
			m_transmission.fillMap(map);
		for(Item item : m_items)
			item.fillMap(map);
		for(Object object : m_objects)
			object.fillMap(map);
	}


}
