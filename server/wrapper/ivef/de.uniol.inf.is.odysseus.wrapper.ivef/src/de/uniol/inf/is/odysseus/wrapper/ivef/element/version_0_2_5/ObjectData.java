package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.aisencoding.SixbitEncoder;
import de.uniol.inf.is.odysseus.wrapper.ivef.aisencoding.SixbitException;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.TaggedItem;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.TrackData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.VesselData;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5.VoyageData;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.SentenceUtils;

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

	/**Message Serial Number: 
     * Used for encoding multiFragments messages
     * Takes values from 0 to 9*/
    private static int m_msgSerial = 0; 
	
	public ObjectData() {

		m_trackDataPresent = false;
	}

	public ObjectData(ObjectData val) {

		m_trackData = val.getTrackData();
		if (val != null) {
			m_trackDataPresent = true;
		}
		for (int i = 0; i < val.countOfVesselDatas(); i++) {
			m_vesselDatas.add(val.getVesselDataAt(i));
		}
		for (int i = 0; i < val.countOfVoyageDatas(); i++) {
			m_voyageDatas.add(val.getVoyageDataAt(i));
		}
		for (int i = 0; i < val.countOfTaggedItems(); i++) {
			m_taggedItems.add(val.getTaggedItemAt(i));
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

	@Override
	public String toXML() {

		String xml = "<ObjectData";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		DecimalFormat nf = new DecimalFormat();
		nf.setMinimumFractionDigits(6);
		nf.setGroupingSize(0);

		String dataMember;
		xml += ">\n";
		if (hasTrackData()) {
			dataMember = m_trackData.toXML();
			if (dataMember != null) {
				xml += dataMember;
			} else {
				return null; // not all required data members have been set
			}
		}
		if (m_vesselDatas.size() < 0) {
			return null; // not enough values
		}
		for (int i = 0; i < m_vesselDatas.size(); i++) {
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
		for (int i = 0; i < m_voyageDatas.size(); i++) {
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
		for (int i = 0; i < m_taggedItems.size(); i++) {
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

		if (hasTrackData()) {
			str += m_trackData.toString(lead + "    ");
		}
		for (int i = 0; i < m_vesselDatas.size(); i++) {
			VesselData attribute = m_vesselDatas.get(i);
			str += attribute.toString(lead + "    ");
		}
		for (int i = 0; i < m_voyageDatas.size(); i++) {
			VoyageData attribute = m_voyageDatas.get(i);
			str += attribute.toString(lead + "    ");
		}
		for (int i = 0; i < m_taggedItems.size(); i++) {
			TaggedItem attribute = m_taggedItems.get(i);
			str += attribute.toString(lead + "    ");
		}
		return str;
	}

	public String encode(String str) {

		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map) {
		if (m_trackDataPresent)
			m_trackData.fillMap(map);
		for (VesselData vdata : m_vesselDatas)
			vdata.fillMap(map);
		for (VoyageData vgdata : m_voyageDatas)
			vgdata.fillMap(map);
		for (TaggedItem item : m_taggedItems)
			item.fillMap(map);
	}

	public String encodeAISPositionPayload() {
		String encodedAIS = "!AIVDM,1,1,,A,";

		int msgtype = 1;// position sentence

		Pos pos = null;
		long encodedLongitude = 0;
		long encodedLatitude = 0;
		if (this.m_trackData.countOfPoss() > 0 && this.countOfVesselDatas() > 0) {
			pos = this.m_trackData.getPosAt(0);
			Double lat = pos.getLat();
			Double lon = pos.getLong();
			encodedLongitude = Math.round(lon * 10000.0 * 60.0);
			encodedLatitude = Math.round(lat * 10000.0 * 60.0);
			if (lat == 0.0 && lon == 0.0)
				msgtype = 5;// static-and-voyage AIS message

		}
		long mmsi = 0;
		if (this.getVesselDataAt(0).getIdentifier() != null) {
			mmsi = this.getVesselDataAt(0).getIdentifier().getMMSI();
		}
		int navStatus = 0;
		if (this.m_trackData.countOfNavStatuss() > 0) {
			navStatus = this.m_trackData.getNavStatusAt(0).getValue();
		}
		int rot = (int) this.m_trackData.getROT();
		int sog = (int) this.m_trackData.getSOG();
		int cog = (int) this.m_trackData.getCOG();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.m_trackData.getUpdateTime());
		int ts = calendar.get(Calendar.SECOND);

		SixbitEncoder encoder = new SixbitEncoder();
		encoder.addVal(msgtype, 6);// Message type
		encoder.addVal(0, 2);// RepeatIndicator = 0
		encoder.addVal(mmsi, 30);// MMSI
		encoder.addVal(navStatus, 4);// NavStatus
		encoder.addVal(rot, 8);// ROT
		encoder.addVal(sog, 10);// SOG
		encoder.addVal(0, 1);// PosAccuracy = 0
		encoder.addVal(encodedLongitude, 28);// Longitude
		encoder.addVal(encodedLatitude, 27);// Latitude
		encoder.addVal(cog, 12);// COG
		encoder.addVal(0, 9);// true heading = 0
		encoder.addVal(ts, 6);// time stamp
		encoder.addVal(0, 2);// Maneuver Indicator = 0
		encoder.addVal(0, 3);// Spare = 0 (Maneuver Indicator, Spare and
								// Regional Reserved are represented in these
								// 3+2=5bits)
		encoder.addVal(0, 1);// RAIM flag = 0
		encoder.addVal(0, 2);// Sync State = 0
		encoder.addVal(0, 3);// Slot Timeout = 0
		encoder.addVal(0, 14);// Sub Message = 0
		try {
			encodedAIS += encoder.encode() + "," + encoder.getPadBits();
		} catch (SixbitException e) {
			e.printStackTrace();
		}
		// checkSum
		encodedAIS += "*" + SentenceUtils.calculateChecksum(encodedAIS);
		return encodedAIS;
	}

	public String[] encodeAISStaticVoyagePayload() {
		if(this.countOfVesselDatas() == 0)
			return null;
		if (m_msgSerial == 10)
			m_msgSerial = 0;
		String[] encodedFragments = new String[2];
		String encodedFragment1 = "!AIVDM,2,1," + m_msgSerial + ",A,";
		String encodedFragment2 = "!AIVDM,2,2," + m_msgSerial++ + ",A,";
		String encodedFragment2Payload;
		int msgtype = 5;//Static&Voyage Sentence 
		
		long mmsi = 0;
		long imo = 0;
		String callSign = "";
		String vesselName = "";
		if (this.getVesselDataAt(0).getIdentifier() != null) {
			Identifier identifier = this.getVesselDataAt(0).getIdentifier();
			mmsi = identifier.getMMSI();
			imo = identifier.getIMO();
			callSign = identifier.getCallsign();
			vesselName = identifier.getName();
		}
		Calendar eta = Calendar.getInstance();
		int draught = 0;
		String destination = "";
		
		if (this.countOfVoyageDatas() > 0){
			Date etaDate = this.getVoyageDataAt(0).getETA();
			eta.setTime(etaDate);
			
			draught = (int) this.getVoyageDataAt(0).getDraught();
			destination = this.getVoyageDataAt(0).getDestName();
		}
		
		// Validate
		if (eta == null){
			return null;
		}
		
		SixbitEncoder encoder = new SixbitEncoder();
		encoder.addVal(msgtype, 6);//Message type
		encoder.addVal(0, 2);//RepeatIndicator = 0
		encoder.addVal(mmsi, 30);//MMSI
		encoder.addVal(0, 2);//AIS Version: Not existed in IVEF
		encoder.addVal(imo, 30);//IMO 
		encoder.addString(callSign, 7/*42/6*/);//CallSign 
		encoder.addString(vesselName, 20/*120/6*/);//VesselName
		encoder.addVal(0, 8);//ShipType: undefined in IVEF.
		encoder.addVal(0, 9);//Dimension to Bow: undefined in IVEF.
		encoder.addVal(0, 9);//Dimension to Stern: undefined in IVEF.
		encoder.addVal(0, 6);//Dimension to Port: undefined in IVEF.
		encoder.addVal(0, 6);//Dimension to Starboard: undefined in IVEF.
		encoder.addVal(0, 4);//Position Fix Type: undefined in IVEF.
		encoder.addVal(eta.get(Calendar.MONTH), 4);//ETA Month
		encoder.addVal(eta.get(Calendar.DAY_OF_MONTH), 5);//ETA Month
		encoder.addVal(eta.get(Calendar.HOUR_OF_DAY), 5);//ETA Month
		encoder.addVal(eta.get(Calendar.MINUTE), 6);//ETA Month
		encoder.addVal(draught, 8);//Draught
		encoder.addString(destination, 20/*120/6*/);//Destination
		encoder.addVal(0, 1);//DTE: 0 Data Terminal Ready
		encoder.addVal(0, 1);//Spare: Not used
		
		try {
			encodedFragment1 += encoder.encode();
		} catch (SixbitException e) {
			e.printStackTrace();
		}
		encodedFragment2Payload = encodedFragment1.substring(71);
		encodedFragment1 = encodedFragment1.substring(0, 71);
		//checkSum
		encodedFragment1 += ",0*" + SentenceUtils.calculateChecksum(encodedFragment1);
		encodedFragment2 += encodedFragment2Payload;
		encodedFragment2 += "," + encoder.getPadBits() + "*" + SentenceUtils.calculateChecksum(encodedFragment2);
		encodedFragments[0] = encodedFragment1;
		encodedFragments[1] = encodedFragment2;
		return encodedFragments;
	}

}
