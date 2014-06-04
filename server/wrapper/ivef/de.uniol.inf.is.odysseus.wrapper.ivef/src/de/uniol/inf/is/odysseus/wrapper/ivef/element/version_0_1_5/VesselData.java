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
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.PosReport;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.StaticData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Voyage;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.TaggedItem;

public class VesselData implements IIvefElement { 

    private PosReport m_posReport; // default value is uninitialized
    private Vector<StaticData> m_staticDatas = new Vector<StaticData>();
    private Vector<Voyage> m_voyages = new Vector<Voyage>();
    private Vector<TaggedItem> m_taggedItems = new Vector<TaggedItem>();

    public VesselData() {

    }

    public VesselData(VesselData val) {

        m_posReport = val.getPosReport();
        for(int i=0; i < val.countOfStaticDatas(); i++ ) {
            m_staticDatas.add( val.getStaticDataAt(i) );
        }
        for(int i=0; i < val.countOfVoyages(); i++ ) {
            m_voyages.add( val.getVoyageAt(i) );
        }
        for(int i=0; i < val.countOfTaggedItems(); i++ ) {
            m_taggedItems.add( val.getTaggedItemAt(i) );
        }
    }

    public void setPosReport(PosReport val) {

        m_posReport = val;
    }

    public PosReport getPosReport() {

        return m_posReport;
    }

    public void addStaticData(StaticData val) {

        m_staticDatas.add(val);
    }

    public StaticData getStaticDataAt(int i) {

        return m_staticDatas.get(i);
    }

    public int countOfStaticDatas() {

        return m_staticDatas.size();
    }

    public void addVoyage(Voyage val) {

        m_voyages.add(val);
    }

    public Voyage getVoyageAt(int i) {

        return m_voyages.get(i);
    }

    public int countOfVoyages() {

        return m_voyages.size();
    }

    public void addTaggedItem(TaggedItem val) {

        m_taggedItems.add(val);
    }

    public TaggedItem getTaggedItemAt(int i) {

        return m_taggedItems.get(i);
    }

    public int countOfTaggedItems() {

        return m_taggedItems.size();
    }

    public String toXML() {

        String xml = "<VesselData";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += ">\n";
        if(m_posReport != null)  // instead of using a flag!
        	xml +=  m_posReport.toXML();
        for(int i=0; i < m_staticDatas.size(); i++ ) {
           StaticData attribute = m_staticDatas.get(i);
            xml += attribute.toXML();
        }
        for(int i=0; i < m_voyages.size(); i++ ) {
           Voyage attribute = m_voyages.get(i);
            xml += attribute.toXML();
        }
        for(int i=0; i < m_taggedItems.size(); i++ ) {
           TaggedItem attribute = m_taggedItems.get(i);
            xml += attribute.toXML();
        }
        xml += "</VesselData>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "VesselData\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        if(m_posReport != null)  // instead of using a flag!
        	str +=  m_posReport.toString(lead + "    ");
        for(int i=0; i < m_staticDatas.size(); i++ ) {
           StaticData attribute = m_staticDatas.get(i);
           str += attribute.toString(lead + "    ");
        }
        for(int i=0; i < m_voyages.size(); i++ ) {
           Voyage attribute = m_voyages.get(i);
           str += attribute.toString(lead + "    ");
        }
        for(int i=0; i < m_taggedItems.size(); i++ ) {
           TaggedItem attribute = m_taggedItems.get(i);
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
		if(m_posReport != null)
			m_posReport.fillMap(map);
		for(StaticData sdata : m_staticDatas)
			sdata.fillMap(map);
		for(Voyage voyage : m_voyages)
			voyage.fillMap(map);
		for(TaggedItem taggedItem : m_taggedItems)
			taggedItem.fillMap(map);
	}


}
