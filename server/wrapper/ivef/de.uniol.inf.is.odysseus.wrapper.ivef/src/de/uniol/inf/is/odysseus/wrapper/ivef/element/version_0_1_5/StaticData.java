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


public class StaticData implements IIvefElement { 

    private String m_id = "0";//Initialized  instead of using a flag! // default value is uninitialized
    private String m_sourceName = "0";//Initialized  instead of using a flag! // default value is uninitialized
    private int m_source; //// default value is uninitialized
    private double m_length; // default value is uninitialized
    private boolean m_lengthPresent;
    private double m_breadth; // default value is uninitialized
    private boolean m_breadthPresent;
    private String m_callsign = "";//Initialized  instead of using a flag! // default value is uninitialized
    private boolean m_callsignPresent;
    private String m_shipName = "";//Initialized  instead of using a flag! // default value is uninitialized
    private boolean m_shipNamePresent;
    private int m_objectType; // default value is uninitialized
    private boolean m_objectTypePresent;
    private int m_shipType; // default value is uninitialized
    private boolean m_shipTypePresent;
    private Long m_IMO; // default value is uninitialized
    private boolean m_IMOPresent;
    private Long m_MMSI; // default value is uninitialized
    private boolean m_MMSIPresent;
    private int m_ATONType; // default value is uninitialized
    private boolean m_ATONTypePresent;
    private String m_ATONName = "";//Initialized  instead of using a flag! // default value is uninitialized
    private boolean m_ATONNamePresent;
    private double m_antPosDistFromFront; // default value is uninitialized
    private boolean m_antPosDistFromFrontPresent;
    private double m_antPosDistFromLeft; // default value is uninitialized
    private boolean m_antPosDistFromLeftPresent;
    private String m_natLangShipName = "";//Initialized  instead of using a flag! // default value is uninitialized
    private boolean m_natLangShipNamePresent;
    private String m_portOfRegistry = "";//Initialized  instead of using a flag! // default value is uninitialized
    private boolean m_portOfRegistryPresent;
    private String m_countryFlag = "";//Initialized  instead of using a flag! // default value is uninitialized
    private boolean m_countryFlagPresent;
    private double m_maxAirDraught; // default value is uninitialized
    private boolean m_maxAirDraughtPresent;
    private double m_maxDraught; // default value is uninitialized
    private boolean m_maxDraughtPresent;
    private String m_deepWaterVesselind = "";//Initialized  instead of using a flag! // default value is uninitialized
    private boolean m_deepWaterVesselindPresent;

    public StaticData() {

        m_lengthPresent = false;
        m_breadthPresent = false;
        m_callsignPresent = false;
        m_shipNamePresent = false;
        m_objectTypePresent = false;
        m_shipTypePresent = false;
        m_IMOPresent = false;
        m_MMSIPresent = false;
        m_ATONTypePresent = false;
        m_ATONNamePresent = false;
        m_antPosDistFromFrontPresent = false;
        m_antPosDistFromLeftPresent = false;
        m_natLangShipNamePresent = false;
        m_portOfRegistryPresent = false;
        m_countryFlagPresent = false;
        m_maxAirDraughtPresent = false;
        m_maxDraughtPresent = false;
        m_deepWaterVesselindPresent = false;
    }

    public StaticData(StaticData val) {

        m_id = val.getId();
        m_sourceName = val.getSourceName();
        m_source = val.getSource();
        m_lengthPresent = val.hasLength();
        m_length = val.getLength();
        m_breadthPresent = val.hasBreadth();
        m_breadth = val.getBreadth();
        m_callsignPresent = val.hasCallsign();
        m_callsign = val.getCallsign();
        m_shipNamePresent = val.hasShipName();
        m_shipName = val.getShipName();
        m_objectTypePresent = val.hasObjectType();
        m_objectType = val.getObjectType();
        m_shipTypePresent = val.hasShipType();
        m_shipType = val.getShipType();
        m_IMOPresent = val.hasIMO();
        m_IMO = val.getIMO();
        m_MMSIPresent = val.hasMMSI();
        m_MMSI = val.getMMSI();
        m_ATONTypePresent = val.hasATONType();
        m_ATONType = val.getATONType();
        m_ATONNamePresent = val.hasATONName();
        m_ATONName = val.getATONName();
        m_antPosDistFromFrontPresent = val.hasAntPosDistFromFront();
        m_antPosDistFromFront = val.getAntPosDistFromFront();
        m_antPosDistFromLeftPresent = val.hasAntPosDistFromLeft();
        m_antPosDistFromLeft = val.getAntPosDistFromLeft();
        m_natLangShipNamePresent = val.hasNatLangShipName();
        m_natLangShipName = val.getNatLangShipName();
        m_portOfRegistryPresent = val.hasPortOfRegistry();
        m_portOfRegistry = val.getPortOfRegistry();
        m_countryFlagPresent = val.hasCountryFlag();
        m_countryFlag = val.getCountryFlag();
        m_maxAirDraughtPresent = val.hasMaxAirDraught();
        m_maxAirDraught = val.getMaxAirDraught();
        m_maxDraughtPresent = val.hasMaxDraught();
        m_maxDraught = val.getMaxDraught();
        m_deepWaterVesselindPresent = val.hasDeepWaterVesselind();
        m_deepWaterVesselind = val.getDeepWaterVesselind();
    }

    public void setId(String val) {

        m_id = val;
    }

    public String getId() {

        return m_id;
    }

    public void setSourceName(String val) {

        m_sourceName = val;
    }

    public String getSourceName() {

        return m_sourceName;
    }

    public void setSource(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) )
            return;
        m_source = val;
    }

    public int getSource() {

        return m_source;
    }

    public void setLength(double val) {

        if (val < 0)
          return;
        m_lengthPresent = true;
        m_length = val;
    }

    public double getLength() {

        return m_length;
    }

    public boolean hasLength() {

        return m_lengthPresent;
    }

    public void setBreadth(double val) {

        if (val < 0)
          return;
        m_breadthPresent = true;
        m_breadth = val;
    }

    public double getBreadth() {

        return m_breadth;
    }

    public boolean hasBreadth() {

        return m_breadthPresent;
    }

    public void setCallsign(String val) {

        m_callsignPresent = true;
        m_callsign = val;
    }

    public String getCallsign() {

        return m_callsign;
    }

    public boolean hasCallsign() {

        return m_callsignPresent;
    }

    public void setShipName(String val) {

        m_shipNamePresent = true;
        m_shipName = val;
    }

    public String getShipName() {

        return m_shipName;
    }

    public boolean hasShipName() {

        return m_shipNamePresent;
    }

    public void setObjectType(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) &&
             ( val != 4 ) &&
             ( val != 5 ) &&
             ( val != 6 ) &&
             ( val != 7 ) )
            return;
        m_objectTypePresent = true;
        m_objectType = val;
    }

    public int getObjectType() {

        return m_objectType;
    }

    public boolean hasObjectType() {

        return m_objectTypePresent;
    }

    public void setShipType(int val) {

        if ( ( val != 20 ) &&
             ( val != 30 ) &&
             ( val != 31 ) &&
             ( val != 32 ) &&
             ( val != 33 ) &&
             ( val != 34 ) &&
             ( val != 35 ) &&
             ( val != 36 ) &&
             ( val != 37 ) &&
             ( val != 40 ) &&
             ( val != 50 ) &&
             ( val != 51 ) &&
             ( val != 52 ) &&
             ( val != 53 ) &&
             ( val != 54 ) &&
             ( val != 55 ) &&
             ( val != 58 ) &&
             ( val != 59 ) &&
             ( val != 60 ) &&
             ( val != 70 ) &&
             ( val != 80 ) &&
             ( val != 90 ) )
            return;
        m_shipTypePresent = true;
        m_shipType = val;
    }

    public int getShipType() {

        return m_shipType;
    }

    public boolean hasShipType() {

        return m_shipTypePresent;
    }

    public void setIMO(Long val) {

        m_IMOPresent = true;
        m_IMO = val;
    }

    public Long getIMO() {

        return m_IMO;
    }

    public boolean hasIMO() {

        return m_IMOPresent;
    }

    public void setMMSI(Long  val) {

        m_MMSIPresent = true;
        m_MMSI = val;
    }

    public Long  getMMSI() {

        return m_MMSI;
    }

    public boolean hasMMSI() {

        return m_MMSIPresent;
    }

    public void setATONType(int val) {

        if ( ( val != 0 ) &&
             ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) &&
             ( val != 5 ) &&
             ( val != 6 ) &&
             ( val != 7 ) &&
             ( val != 8 ) &&
             ( val != 9 ) &&
             ( val != 10 ) &&
             ( val != 11 ) &&
             ( val != 12 ) &&
             ( val != 13 ) &&
             ( val != 14 ) &&
             ( val != 15 ) &&
             ( val != 16 ) &&
             ( val != 17 ) &&
             ( val != 18 ) &&
             ( val != 19 ) &&
             ( val != 20 ) &&
             ( val != 21 ) &&
             ( val != 22 ) &&
             ( val != 23 ) &&
             ( val != 24 ) &&
             ( val != 25 ) &&
             ( val != 26 ) &&
             ( val != 27 ) &&
             ( val != 28 ) &&
             ( val != 29 ) &&
             ( val != 30 ) &&
             ( val != 31 ) &&
             ( val != 32 ) &&
             ( val != 33 ) )
            return;
        m_ATONTypePresent = true;
        m_ATONType = val;
    }

    public int getATONType() {

        return m_ATONType;
    }

    public boolean hasATONType() {

        return m_ATONTypePresent;
    }

    public void setATONName(String val) {

        m_ATONNamePresent = true;
        m_ATONName = val;
    }

    public String getATONName() {

        return m_ATONName;
    }

    public boolean hasATONName() {

        return m_ATONNamePresent;
    }

    public void setAntPosDistFromFront(double val) {

        m_antPosDistFromFrontPresent = true;
        m_antPosDistFromFront = val;
    }

    public double getAntPosDistFromFront() {

        return m_antPosDistFromFront;
    }

    public boolean hasAntPosDistFromFront() {

        return m_antPosDistFromFrontPresent;
    }

    public void setAntPosDistFromLeft(double val) {

        m_antPosDistFromLeftPresent = true;
        m_antPosDistFromLeft = val;
    }

    public double getAntPosDistFromLeft() {

        return m_antPosDistFromLeft;
    }

    public boolean hasAntPosDistFromLeft() {

        return m_antPosDistFromLeftPresent;
    }

    public void setNatLangShipName(String val) {

        m_natLangShipNamePresent = true;
        m_natLangShipName = val;
    }

    public String getNatLangShipName() {

        return m_natLangShipName;
    }

    public boolean hasNatLangShipName() {

        return m_natLangShipNamePresent;
    }

    public void setPortOfRegistry(String val) {

        m_portOfRegistryPresent = true;
        m_portOfRegistry = val;
    }

    public String getPortOfRegistry() {

        return m_portOfRegistry;
    }

    public boolean hasPortOfRegistry() {

        return m_portOfRegistryPresent;
    }

    public void setCountryFlag(String val) {

        m_countryFlagPresent = true;
        m_countryFlag = val;
    }

    public String getCountryFlag() {

        return m_countryFlag;
    }

    public boolean hasCountryFlag() {

        return m_countryFlagPresent;
    }

    public void setMaxAirDraught(double val) {

        if (val < 0)
          return;
        m_maxAirDraughtPresent = true;
        m_maxAirDraught = val;
    }

    public double getMaxAirDraught() {

        return m_maxAirDraught;
    }

    public boolean hasMaxAirDraught() {

        return m_maxAirDraughtPresent;
    }

    public void setMaxDraught(double val) {

        if (val < 0)
          return;
        m_maxDraughtPresent = true;
        m_maxDraught = val;
    }

    public double getMaxDraught() {

        return m_maxDraught;
    }

    public boolean hasMaxDraught() {

        return m_maxDraughtPresent;
    }

    public void setDeepWaterVesselind(String val) {

//      if ( ( val != "no" ) &&
//           ( val != "yes" ) )
//          return;
    if ( !(val.equals("no") ) &&
  	   !(val.equals("yes") ) )
  	  	return;
        m_deepWaterVesselindPresent = true;
        m_deepWaterVesselind = val;
    }

    public String getDeepWaterVesselind() {

        return m_deepWaterVesselind;
    }

    public boolean hasDeepWaterVesselind() {

        return m_deepWaterVesselindPresent;
    }
    @Override
    public String toXML() {

        String xml = "<StaticData";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml += " Id=\"" + encode( m_id) + "\"";
        xml += " SourceName=\"" + encode( m_sourceName) + "\"";
        xml += " Source=\"" + m_source + "\"";
        if ( hasLength() ) {
            xml += " Length=\"" + m_length + "\"";
        }
        if ( hasBreadth() ) {
            xml += " Breadth=\"" + m_breadth + "\"";
        }
        if ( hasCallsign() ) {
            xml += " Callsign=\"" + encode( m_callsign) + "\"";
        }
        if ( hasShipName() ) {
            xml += " ShipName=\"" + encode( m_shipName) + "\"";
        }
        if ( hasObjectType() ) {
            xml += " ObjectType=\"" + m_objectType + "\"";
        }
        if ( hasShipType() ) {
            xml += " ShipType=\"" + m_shipType + "\"";
        }
        if ( hasIMO() ) {
            xml += " IMO=\"" + m_IMO + "\"";
        }
        if ( hasMMSI() ) {
            xml += " MMSI=\"" + m_MMSI + "\"";
        }
        if ( hasATONType() ) {
            xml += " ATONType=\"" + m_ATONType + "\"";
        }
        if ( hasATONName() ) {
            xml += " ATONName=\"" + encode( m_ATONName) + "\"";
        }
        if ( hasAntPosDistFromFront() ) {
            xml += " AntPosDistFromFront=\"" + m_antPosDistFromFront + "\"";
        }
        if ( hasAntPosDistFromLeft() ) {
            xml += " AntPosDistFromLeft=\"" + m_antPosDistFromLeft + "\"";
        }
        if ( hasNatLangShipName() ) {
            xml += " NatLangShipName=\"" + encode( m_natLangShipName) + "\"";
        }
        if ( hasPortOfRegistry() ) {
            xml += " PortOfRegistry=\"" + encode( m_portOfRegistry) + "\"";
        }
        if ( hasCountryFlag() ) {
            xml += " CountryFlag=\"" + encode( m_countryFlag) + "\"";
        }
        if ( hasMaxAirDraught() ) {
            xml += " MaxAirDraught=\"" + m_maxAirDraught + "\"";
        }
        if ( hasMaxDraught() ) {
            xml += " MaxDraught=\"" + m_maxDraught + "\"";
        }
        if ( hasDeepWaterVesselind() ) {
            xml += " DeepWaterVesselind=\"" + encode( m_deepWaterVesselind) + "\"";
        }
        xml += ">\n";
        xml += "</StaticData>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "StaticData\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        str +=  lead + "    Id = " + m_id + "\n";
        str +=  lead + "    SourceName = " + m_sourceName + "\n";
        str +=  lead + "    Source = " + m_source + "\n";
        if ( hasLength() ) {
            str +=  lead + "    Length = " + m_length + "\n";
        }
        if ( hasBreadth() ) {
            str +=  lead + "    Breadth = " + m_breadth + "\n";
        }
        if ( hasCallsign() ) {
            str +=  lead + "    Callsign = " + m_callsign + "\n";
        }
        if ( hasShipName() ) {
            str +=  lead + "    ShipName = " + m_shipName + "\n";
        }
        if ( hasObjectType() ) {
            str +=  lead + "    ObjectType = " + m_objectType + "\n";
        }
        if ( hasShipType() ) {
            str +=  lead + "    ShipType = " + m_shipType + "\n";
        }
        if ( hasIMO() ) {
            str +=  lead + "    IMO = " + m_IMO + "\n";
        }
        if ( hasMMSI() ) {
            str +=  lead + "    MMSI = " + m_MMSI + "\n";
        }
        if ( hasATONType() ) {
            str +=  lead + "    ATONType = " + m_ATONType + "\n";
        }
        if ( hasATONName() ) {
            str +=  lead + "    ATONName = " + m_ATONName + "\n";
        }
        if ( hasAntPosDistFromFront() ) {
            str +=  lead + "    AntPosDistFromFront = " + m_antPosDistFromFront + "\n";
        }
        if ( hasAntPosDistFromLeft() ) {
            str +=  lead + "    AntPosDistFromLeft = " + m_antPosDistFromLeft + "\n";
        }
        if ( hasNatLangShipName() ) {
            str +=  lead + "    NatLangShipName = " + m_natLangShipName + "\n";
        }
        if ( hasPortOfRegistry() ) {
            str +=  lead + "    PortOfRegistry = " + m_portOfRegistry + "\n";
        }
        if ( hasCountryFlag() ) {
            str +=  lead + "    CountryFlag = " + m_countryFlag + "\n";
        }
        if ( hasMaxAirDraught() ) {
            str +=  lead + "    MaxAirDraught = " + m_maxAirDraught + "\n";
        }
        if ( hasMaxDraught() ) {
            str +=  lead + "    MaxDraught = " + m_maxDraught + "\n";
        }
        if ( hasDeepWaterVesselind() ) {
            str +=  lead + "    DeepWaterVesselind = " + m_deepWaterVesselind + "\n";
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
		map.addAttributeValue("staticData_id", m_id);
		map.addAttributeValue("staticData_sourceName", m_sourceName);
		map.addAttributeValue("staticData_source", m_source);
	    if(m_lengthPresent)
	    	map.addAttributeValue("staticData_length", m_length);
	    if(m_breadthPresent)
	    	map.addAttributeValue("staticData_breadth", m_breadth);
	    if(m_callsignPresent)
	    	map.addAttributeValue("staticData_callsign", m_callsign);
	    if(m_shipNamePresent)
	    	map.addAttributeValue("staticData_shipName", m_shipName);
        if(m_objectTypePresent)	  
        	map.addAttributeValue("staticData_objectType", m_objectType);
	    if(m_shipTypePresent)
	    	map.addAttributeValue("staticData_shipType", m_shipType);
	    if(m_IMOPresent)
	    	map.addAttributeValue("staticData_IMO", m_IMO);
	    if(m_MMSIPresent)
	    	map.addAttributeValue("staticData_MMSI", m_MMSI);
	    if(m_ATONTypePresent)
	    	map.addAttributeValue("staticData_ATONType", m_ATONType);
	    if(m_ATONNamePresent)
	    	map.addAttributeValue("staticData_ATONName", m_ATONName);
	    if(m_antPosDistFromFrontPresent)
	    	map.addAttributeValue("staticData_antPosDistFromFront", m_antPosDistFromFront);
	    if(m_antPosDistFromLeftPresent)
	    	map.addAttributeValue("staticData_antPosDistFromLeft", m_antPosDistFromLeft);
	    if(m_natLangShipNamePresent)
	    	map.addAttributeValue("staticData_natLangShipName", m_natLangShipName);
	    if(m_portOfRegistryPresent)
	    	map.addAttributeValue("staticData_portOfRegistry", m_portOfRegistry);
	    if(m_countryFlagPresent)
	    	map.addAttributeValue("staticData_countryFlag", m_countryFlag);
	    if(m_maxAirDraughtPresent)
	    	map.addAttributeValue("staticData_maxAirDraught", m_maxAirDraught);
	    if(m_maxDraughtPresent)
	    	map.addAttributeValue("staticData_maxDraught", m_maxDraught);
	    if(m_deepWaterVesselindPresent)
	    	map.addAttributeValue("staticData_deepWaterVesselind", m_deepWaterVesselind);
	}


}
