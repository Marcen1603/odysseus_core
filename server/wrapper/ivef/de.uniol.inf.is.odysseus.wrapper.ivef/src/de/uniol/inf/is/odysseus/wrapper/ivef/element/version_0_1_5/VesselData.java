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
import de.uniol.inf.is.odysseus.wrapper.ivef.aisencoding.SixbitEncoder;
import de.uniol.inf.is.odysseus.wrapper.ivef.aisencoding.SixbitException;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.PosReport;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.StaticData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Voyage;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.TaggedItem;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.SentenceUtils;

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
    @Override
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

	public String encodeAISPayload() {
		//TODO: to handle the multiFragment messages
		String encodedAIS = "!AIVDM,1,1,,A,";
		Pos pos = this.m_posReport.getPos();
		Double lat = pos.getLat();
		Double lon = pos.getLong();
		int msgtype = 1;//position sentence
		if(lat == 0.0 && lon == 0.0)
			msgtype = 5;//static-and-voyage AIS message
		long mmsi = 0;
		if(this.countOfStaticDatas() > 0)
			mmsi = this.getStaticDataAt(0).getMMSI();
		int navStatus = this.m_posReport.getNavStatus();
		int rot = (int)this.m_posReport.getRateOfTurn();
		int sog = this.m_posReport.getSOG().intValue();
		long encodedLongitude = Math.round(lon * 10000.0 * 60.0);
		long encodedLatitude = Math.round(lat * 10000.0 * 60.0);
		int cog = this.m_posReport.getCOG().intValue();
		@SuppressWarnings("deprecation")
		int ts = this.m_posReport.getUpdateTime().getSeconds();
		
		SixbitEncoder encoder = new SixbitEncoder();
		encoder.addVal(msgtype, 6);//Message type
		encoder.addVal(0, 2);//RepeatIndicator = 0
		encoder.addVal(mmsi, 30);//MMSI
		encoder.addVal(navStatus, 4);//NavStatus
		encoder.addVal(rot, 8);// ROT 
		encoder.addVal(sog, 10);//SOG 
		encoder.addVal(0, 1);//PosAccuracy = 0
		encoder.addVal(encodedLongitude, 28);//Longitude
		encoder.addVal(encodedLatitude, 27);//Latitude
		encoder.addVal(cog, 12);//COG
		encoder.addVal(0, 9);//true heading = 0
		encoder.addVal(ts, 6);//time stamp
		encoder.addVal(0, 2);//Maneuver Indicator = 0
		encoder.addVal(0, 3);//Spare = 0 (Maneuver Indicator, Spare and Regional Reserved are represented in these 3+2=5bits)
		encoder.addVal(0, 1);//RAIM flag = 0
		encoder.addVal(0, 2);//Sync State = 0
		encoder.addVal(0, 3);//Slot Timeout = 0
		encoder.addVal(0, 14);//Sub Message = 0
		try {
			encodedAIS += encoder.encode() + "," + encoder.getPadBits();
		} catch (SixbitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//checkSum
		encodedAIS += "*" + SentenceUtils.calculateChecksum(encodedAIS);
		return encodedAIS;
	}

}
