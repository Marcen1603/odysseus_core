package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.TrackData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.VoyageData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.TaggedItem;

/**
 * @author msalous
 *
 */
public class ObjectData implements IIvefElement { 

    private TrackData m_trackData; // default value is uninitialized
    private boolean m_trackDataPresent;
    private Vector<VesselData> m_vesselDatas = new Vector<VesselData>();
    private Vector<VoyageData> m_voyageDatas = new Vector<VoyageData>();
    private Vector<TaggedItem> m_taggedItems = new Vector<TaggedItem>();

    public ObjectData() {

        m_trackDataPresent = false;
    }

    public ObjectData(ObjectData val) {

        m_trackData = val.getTrackData();
        if (val != null) {
            m_trackDataPresent = true;
        }
        for(int i=0; i < val.countOfVesselDatas(); i++ ) {
            m_vesselDatas.add( val.getVesselDataAt(i) );
        }
        for(int i=0; i < val.countOfVoyageDatas(); i++ ) {
            m_voyageDatas.add( val.getVoyageDataAt(i) );
        }
        for(int i=0; i < val.countOfTaggedItems(); i++ ) {
            m_taggedItems.add( val.getTaggedItemAt(i) );
        }
    }

    public boolean setTrackData(TrackData val) {

        m_trackDataPresent = true;
        m_trackData = val;
        return true;
    }

    public TrackData getTrackData() {

        return m_trackData;
    }

    public boolean hasTrackData() {

        return m_trackDataPresent;
    }

    public boolean removeVesselData(VesselData val) {
        if (m_vesselDatas.size() <= 0) {
            return false; // scalar already at minOccurs
        }

        m_vesselDatas.remove(val);
        return true;
    }

    public boolean addVesselData(VesselData val) {

        m_vesselDatas.add(val);
        return true;
    }

    public VesselData getVesselDataAt(int i) {

        return m_vesselDatas.get(i);
    }

    public int countOfVesselDatas() {

        return m_vesselDatas.size();
    }

    public boolean removeVoyageData(VoyageData val) {
        if (m_voyageDatas.size() <= 0) {
            return false; // scalar already at minOccurs
        }

        m_voyageDatas.remove(val);
        return true;
    }

    public boolean addVoyageData(VoyageData val) {

        m_voyageDatas.add(val);
        return true;
    }

    public VoyageData getVoyageDataAt(int i) {

        return m_voyageDatas.get(i);
    }

    public int countOfVoyageDatas() {

        return m_voyageDatas.size();
    }

    public boolean removeTaggedItem(TaggedItem val) {
        if (m_taggedItems.size() <= 0) {
            return false; // scalar already at minOccurs
        }

        m_taggedItems.remove(val);
        return true;
    }

    public boolean addTaggedItem(TaggedItem val) {

        m_taggedItems.add(val);
        return true;
    }

    public TaggedItem getTaggedItemAt(int i) {

        return m_taggedItems.get(i);
    }

    public int countOfTaggedItems() {

        return m_taggedItems.size();
    }

    public String toXML() {

        String xml = "<ObjectData";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        String dataMember;
        xml += ">\n";
        if ( hasTrackData() ) {
            dataMember =  m_trackData.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if (m_vesselDatas.size() < 0) {
            return null; // not enough values
        }
        for(int i=0; i < m_vesselDatas.size(); i++ ) {
           VesselData attribute = m_vesselDatas.get(i);
            dataMember = attribute.toXML();
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if (m_voyageDatas.size() < 0) {
            return null; // not enough values
        }
        for(int i=0; i < m_voyageDatas.size(); i++ ) {
           VoyageData attribute = m_voyageDatas.get(i);
            dataMember = attribute.toXML();
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if (m_taggedItems.size() < 0) {
            return null; // not enough values
        }
        for(int i=0; i < m_taggedItems.size(); i++ ) {
           TaggedItem attribute = m_taggedItems.get(i);
            dataMember = attribute.toXML();
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        xml += "</ObjectData>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "ObjectData\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasTrackData() ) {
            str +=  m_trackData.toString(lead + "    ") ;
        }
        for(int i=0; i < m_vesselDatas.size(); i++ ) {
           VesselData attribute = m_vesselDatas.get(i);
           str += attribute.toString(lead + "    ");
        }
        for(int i=0; i < m_voyageDatas.size(); i++ ) {
           VoyageData attribute = m_voyageDatas.get(i);
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
		if(m_trackDataPresent)
			m_trackData.fillMap(map);
		for(VesselData vdata : m_vesselDatas)
			vdata.fillMap(map);
		for(VoyageData vgdata : m_voyageDatas)
			vgdata.fillMap(map);
		for(TaggedItem item : m_taggedItems)
			item.fillMap(map);
	}


}
