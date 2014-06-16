package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.Waypoint;

/**
 * @author msalous
 *
 */
public class VoyageData implements IIvefElement { 

    private Vector<Waypoint> m_waypoints = new Vector<Waypoint>();
    private double m_airDraught; // default value is uninitialized
    private boolean m_airDraughtPresent;
    private int m_id; // default value is uninitialized
    private boolean m_idPresent;
    private int m_cargoTypeIMO; // default value is uninitialized
    private boolean m_cargoTypeIMOPresent;
    private String m_contactIdentity; // default value is uninitialized
    private boolean m_contactIdentityPresent;
    private String m_destCode; // default value is uninitialized
    private boolean m_destCodePresent;
    private String m_destName; // default value is uninitialized
    private boolean m_destNamePresent;
    private String m_departCode; // default value is uninitialized
    private boolean m_departCodePresent;
    private String m_departName; // default value is uninitialized
    private boolean m_departNamePresent;
    private double m_draught; // default value is uninitialized
    private boolean m_draughtPresent;
    private Date m_ETA; // default value is uninitialized
    private boolean m_ETAPresent;
    private Date m_ATD; // default value is uninitialized
    private boolean m_ATDPresent;
    private double m_ISPSLevel; // default value is uninitialized
    private boolean m_ISPSLevelPresent;
    private double m_overSizedLength; // default value is uninitialized
    private boolean m_overSizedLengthPresent;
    private double m_overSizedWidth; // default value is uninitialized
    private boolean m_overSizedWidthPresent;
    private int m_personsOnBoard; // default value is uninitialized
    private boolean m_personsOnBoardPresent;
    private double m_pilots; // default value is uninitialized
    private boolean m_pilotsPresent;
    private boolean m_routeBound; // default value is uninitialized
    private boolean m_routeBoundPresent;
    private String m_sourceId; // default value is uninitialized
    private boolean m_sourceIdPresent;
    private String m_sourceName; // default value is uninitialized
    private boolean m_sourceNamePresent;
    private int m_sourceType; // default value is uninitialized
    private boolean m_sourceTypePresent;
    private int m_tankerStatus; // default value is uninitialized
    private boolean m_tankerStatusPresent;
    private boolean m_tugs; // default value is uninitialized
    private boolean m_tugsPresent;
    private Date m_updateTime; // default value is uninitialized
    private boolean m_updateTimePresent;

    public VoyageData() {

        m_airDraughtPresent = false;
        m_idPresent = false;
        m_cargoTypeIMOPresent = false;
        m_contactIdentityPresent = false;
        m_destCodePresent = false;
        m_destNamePresent = false;
        m_departCodePresent = false;
        m_departNamePresent = false;
        m_draughtPresent = false;
        m_ETAPresent = false;
        m_ATDPresent = false;
        m_ISPSLevelPresent = false;
        m_overSizedLengthPresent = false;
        m_overSizedWidthPresent = false;
        m_personsOnBoardPresent = false;
        m_pilotsPresent = false;
        m_routeBoundPresent = false;
        m_sourceIdPresent = false;
        m_sourceNamePresent = false;
        m_sourceTypePresent = false;
        m_tankerStatusPresent = false;
        m_tugsPresent = false;
        m_updateTimePresent = false;
    }

    public VoyageData(VoyageData val) {

        for(int i=0; i < val.countOfWaypoints(); i++ ) {
            m_waypoints.add( val.getWaypointAt(i) );
        }
        m_airDraught = val.getAirDraught();
        if (val != null) {
            m_airDraughtPresent = true;
        }
        m_id = val.getId();
        if (val != null) {
            m_idPresent = true;
        }
        m_cargoTypeIMO = val.getCargoTypeIMO();
        if (val != null) {
            m_cargoTypeIMOPresent = true;
        }
        m_contactIdentity = val.getContactIdentity();
        if (val != null) {
            m_contactIdentityPresent = true;
        }
        m_destCode = val.getDestCode();
        if (val != null) {
            m_destCodePresent = true;
        }
        m_destName = val.getDestName();
        if (val != null) {
            m_destNamePresent = true;
        }
        m_departCode = val.getDepartCode();
        if (val != null) {
            m_departCodePresent = true;
        }
        m_departName = val.getDepartName();
        if (val != null) {
            m_departNamePresent = true;
        }
        m_draught = val.getDraught();
        if (val != null) {
            m_draughtPresent = true;
        }
        m_ETA = val.getETA();
        if (val != null) {
            m_ETAPresent = true;
        }
        m_ATD = val.getATD();
        if (val != null) {
            m_ATDPresent = true;
        }
        m_ISPSLevel = val.getISPSLevel();
        if (val != null) {
            m_ISPSLevelPresent = true;
        }
        m_overSizedLength = val.getOverSizedLength();
        if (val != null) {
            m_overSizedLengthPresent = true;
        }
        m_overSizedWidth = val.getOverSizedWidth();
        if (val != null) {
            m_overSizedWidthPresent = true;
        }
        m_personsOnBoard = val.getPersonsOnBoard();
        if (val != null) {
            m_personsOnBoardPresent = true;
        }
        m_pilots = val.getPilots();
        if (val != null) {
            m_pilotsPresent = true;
        }
        m_routeBound = val.getRouteBound();
        if (val != null) {
            m_routeBoundPresent = true;
        }
        m_sourceId = val.getSourceId();
        if (val != null) {
            m_sourceIdPresent = true;
        }
        m_sourceName = val.getSourceName();
        if (val != null) {
            m_sourceNamePresent = true;
        }
        m_sourceType = val.getSourceType();
        if (val != null) {
            m_sourceTypePresent = true;
        }
        m_tankerStatus = val.getTankerStatus();
        if (val != null) {
            m_tankerStatusPresent = true;
        }
        m_tugs = val.getTugs();
        if (val != null) {
            m_tugsPresent = true;
        }
        m_updateTime = val.getUpdateTime();
        if (val != null) {
            m_updateTimePresent = true;
        }
    }

    public boolean removeWaypoint(Waypoint val) {
        if (m_waypoints.size() <= 0) {
            return false; // scalar already at minOccurs
        }

        m_waypoints.remove(val);
        return true;
    }

    public boolean addWaypoint(Waypoint val) {

        m_waypoints.add(val);
        return true;
    }

    public Waypoint getWaypointAt(int i) {

        return m_waypoints.get(i);
    }

    public int countOfWaypoints() {

        return m_waypoints.size();
    }

    public boolean setAirDraught(double val) {

        if (val <= 0)
          return false;
        m_airDraughtPresent = true;
        m_airDraught = val;
        return true;
    }

    public double getAirDraught() {

        return m_airDraught;
    }

    public boolean hasAirDraught() {

        return m_airDraughtPresent;
    }

    public boolean setId(int val) {

        m_idPresent = true;
        m_id = val;
        return true;
    }

    public int getId() {

        return m_id;
    }

    public boolean setCargoTypeIMO(int val) {

        if ( ( val != 0 ) &&
             ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) &&
             ( val != 4 ) &&
             ( val != 5 ) )
            return false;
        m_cargoTypeIMOPresent = true;
        m_cargoTypeIMO = val;
        return true;
    }

    public int getCargoTypeIMO() {

        return m_cargoTypeIMO;
    }

    public boolean hasCargoTypeIMO() {

        return m_cargoTypeIMOPresent;
    }

    public boolean setContactIdentity(String val) {

        if (val.length() < 1)
          return false;
        if (val.length() > 254)
          return false;
        m_contactIdentityPresent = true;
        m_contactIdentity = val;
        return true;
    }

    public String getContactIdentity() {

        return m_contactIdentity;
    }

    public boolean hasContactIdentity() {

        return m_contactIdentityPresent;
    }

    public boolean setDestCode(String val) {

        if (val.length() < 5)
          return false;
        if (val.length() > 15)
          return false;
        m_destCodePresent = true;
        m_destCode = val;
        return true;
    }

    public String getDestCode() {

        return m_destCode;
    }

    public boolean hasDestCode() {

        return m_destCodePresent;
    }

    public boolean setDestName(String val) {

        if (val.length() < 1)
          return false;
        if (val.length() > 42)
          return false;
        m_destNamePresent = true;
        m_destName = val;
        return true;
    }

    public String getDestName() {

        return m_destName;
    }

    public boolean hasDestName() {

        return m_destNamePresent;
    }

    public boolean setDepartCode(String val) {

        if (val.length() < 5)
          return false;
        if (val.length() > 15)
          return false;
        m_departCodePresent = true;
        m_departCode = val;
        return true;
    }

    public String getDepartCode() {

        return m_departCode;
    }

    public boolean hasDepartCode() {

        return m_departCodePresent;
    }

    public boolean setDepartName(String val) {

        if (val.length() < 1)
          return false;
        if (val.length() > 42)
          return false;
        m_departNamePresent = true;
        m_departName = val;
        return true;
    }

    public String getDepartName() {

        return m_departName;
    }

    public boolean hasDepartName() {

        return m_departNamePresent;
    }

    public boolean setDraught(double val) {

        if (val <= 0)
          return false;
        m_draughtPresent = true;
        m_draught = val;
        return true;
    }

    public double getDraught() {

        return m_draught;
    }

    public boolean hasDraught() {

        return m_draughtPresent;
    }

    public boolean setETA(Date val) {

        m_ETAPresent = true;
        m_ETA = val;
        return true;
    }

    public Date getETA() {

        return m_ETA;
    }

    public boolean hasETA() {

        return m_ETAPresent;
    }

    public boolean setATD(Date val) {

        m_ATDPresent = true;
        m_ATD = val;
        return true;
    }

    public Date getATD() {

        return m_ATD;
    }

    public boolean hasATD() {

        return m_ATDPresent;
    }

    public boolean setISPSLevel(double val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) )
            return false;
        m_ISPSLevelPresent = true;
        m_ISPSLevel = val;
        return true;
    }

    public double getISPSLevel() {

        return m_ISPSLevel;
    }

    public boolean hasISPSLevel() {

        return m_ISPSLevelPresent;
    }

    public boolean setOverSizedLength(double val) {

        if (val <= 0)
          return false;
        m_overSizedLengthPresent = true;
        m_overSizedLength = val;
        return true;
    }

    public double getOverSizedLength() {

        return m_overSizedLength;
    }

    public boolean hasOverSizedLength() {

        return m_overSizedLengthPresent;
    }

    public boolean setOverSizedWidth(double val) {

        if (val <= 0)
          return false;
        m_overSizedWidthPresent = true;
        m_overSizedWidth = val;
        return true;
    }

    public double getOverSizedWidth() {

        return m_overSizedWidth;
    }

    public boolean hasOverSizedWidth() {

        return m_overSizedWidthPresent;
    }

    public boolean setPersonsOnBoard(int val) {

        if (val <= 0)
          return false;
        m_personsOnBoardPresent = true;
        m_personsOnBoard = val;
        return true;
    }

    public int getPersonsOnBoard() {

        return m_personsOnBoard;
    }

    public boolean hasPersonsOnBoard() {

        return m_personsOnBoardPresent;
    }

    public boolean setPilots(double val) {

        if (val <= 0)
          return false;
        m_pilotsPresent = true;
        m_pilots = val;
        return true;
    }

    public double getPilots() {

        return m_pilots;
    }

    public boolean hasPilots() {

        return m_pilotsPresent;
    }

    public boolean setRouteBound(boolean val) {

        m_routeBoundPresent = true;
        m_routeBound = val;
        return true;
    }

    public boolean getRouteBound() {

        return m_routeBound;
    }

    public boolean hasRouteBound() {

        return m_routeBoundPresent;
    }

    public boolean setSourceId(String val) {

        if (val.length() < 5)
          return false;
        if (val.length() > 15)
          return false;
        m_sourceIdPresent = true;
        m_sourceId = val;
        return true;
    }

    public String getSourceId() {

        return m_sourceId;
    }

    public boolean hasSourceId() {

        return m_sourceIdPresent;
    }

    public boolean setSourceName(String val) {

        if (val.length() < 1)
          return false;
        if (val.length() > 42)
          return false;
        m_sourceNamePresent = true;
        m_sourceName = val;
        return true;
    }

    public String getSourceName() {

        return m_sourceName;
    }

    public boolean setSourceType(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) &&
             ( val != 4 ) &&
             ( val != 5 ) )
            return false;
        m_sourceTypePresent = true;
        m_sourceType = val;
        return true;
    }

    public int getSourceType() {

        return m_sourceType;
    }

    public boolean setTankerStatus(int val) {

        if ( ( val != 0 ) &&
             ( val != 1 ) &&
             ( val != 2 ) )
            return false;
        m_tankerStatusPresent = true;
        m_tankerStatus = val;
        return true;
    }

    public int getTankerStatus() {

        return m_tankerStatus;
    }

    public boolean hasTankerStatus() {

        return m_tankerStatusPresent;
    }

    public boolean setTugs(boolean val) {

        m_tugsPresent = true;
        m_tugs = val;
        return true;
    }

    public boolean getTugs() {

        return m_tugs;
    }

    public boolean hasTugs() {

        return m_tugsPresent;
    }

    public boolean setUpdateTime(Date val) {

        m_updateTimePresent = true;
        m_updateTime = val;
        return true;
    }

    public Date getUpdateTime() {

        return m_updateTime;
    }
    @Override
    public String toXML() {

        String xml = "<VoyageData";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasAirDraught() ) {
            DecimalFormat nfm_airDraught = new DecimalFormat("0.00");
            xml += " AirDraught=\"" + nfm_airDraught.format(m_airDraught) + "\"";
        }
        if ( m_idPresent ) {
            xml += " Id=\"" + m_id + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( hasCargoTypeIMO() ) {
            xml += " CargoTypeIMO=\"" + m_cargoTypeIMO + "\"";
        }
        if ( hasContactIdentity() ) {
            xml += " ContactIdentity=\"" + encode( m_contactIdentity) + "\"";
        }
        if ( hasDestCode() ) {
            xml += " DestCode=\"" + encode( m_destCode) + "\"";
        }
        if ( hasDestName() ) {
            xml += " DestName=\"" + encode( m_destName) + "\"";
        }
        if ( hasDepartCode() ) {
            xml += " DepartCode=\"" + encode( m_departCode) + "\"";
        }
        if ( hasDepartName() ) {
            xml += " DepartName=\"" + encode( m_departName) + "\"";
        }
        if ( hasDraught() ) {
            DecimalFormat nfm_draught = new DecimalFormat("0.00");
            xml += " Draught=\"" + nfm_draught.format(m_draught) + "\"";
        }
        if ( hasETA() ) {
            xml += " ETA=\"" + df.format(m_ETA) + "\"";
        }
        if ( hasATD() ) {
            xml += " ATD=\"" + df.format(m_ATD) + "\"";
        }
        if ( hasISPSLevel() ) {
            xml += " ISPSLevel=\"" + nf.format(m_ISPSLevel) + "\"";
        }
        if ( hasOverSizedLength() ) {
            DecimalFormat nfm_overSizedLength = new DecimalFormat("0.0");
            xml += " OverSizedLength=\"" + nfm_overSizedLength.format(m_overSizedLength) + "\"";
        }
        if ( hasOverSizedWidth() ) {
            DecimalFormat nfm_overSizedWidth = new DecimalFormat("0.0");
            xml += " OverSizedWidth=\"" + nfm_overSizedWidth.format(m_overSizedWidth) + "\"";
        }
        if ( hasPersonsOnBoard() ) {
            xml += " PersonsOnBoard=\"" + m_personsOnBoard + "\"";
        }
        if ( hasPilots() ) {
            xml += " Pilots=\"" + nf.format(m_pilots) + "\"";
        }
        if ( hasRouteBound() ) {
            xml += " RouteBound=\"" + m_routeBound + "\"";
        }
        if ( hasSourceId() ) {
            xml += " SourceId=\"" + encode( m_sourceId) + "\"";
        }
        if ( m_sourceNamePresent ) {
            xml += " SourceName=\"" + encode( m_sourceName) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( m_sourceTypePresent ) {
            xml += " SourceType=\"" + m_sourceType + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( hasTankerStatus() ) {
            xml += " TankerStatus=\"" + m_tankerStatus + "\"";
        }
        if ( hasTugs() ) {
            xml += " Tugs=\"" + m_tugs + "\"";
        }
        if ( m_updateTimePresent ) {
            xml += " UpdateTime=\"" + df.format(m_updateTime) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        String dataMember;
        xml += ">\n";
        if (m_waypoints.size() < 0) {
            return null; // not enough values
        }
        for(int i=0; i < m_waypoints.size(); i++ ) {
           Waypoint attribute = m_waypoints.get(i);
            dataMember = attribute.toXML();
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        xml += "</VoyageData>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "VoyageData\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasAirDraught() ) {
            DecimalFormat nfm_airDraught = new DecimalFormat("0.00");
            str +=  lead + "    AirDraught = " + nfm_airDraught.format(m_airDraught) + "\n";
        }
            str +=  lead + "    Id = " + m_id + "\n";
        if ( hasCargoTypeIMO() ) {
            str +=  lead + "    CargoTypeIMO = " + m_cargoTypeIMO + "\n";
        }
        if ( hasContactIdentity() ) {
            str +=  lead + "    ContactIdentity = " + m_contactIdentity + "\n";
        }
        if ( hasDestCode() ) {
            str +=  lead + "    DestCode = " + m_destCode + "\n";
        }
        if ( hasDestName() ) {
            str +=  lead + "    DestName = " + m_destName + "\n";
        }
        if ( hasDepartCode() ) {
            str +=  lead + "    DepartCode = " + m_departCode + "\n";
        }
        if ( hasDepartName() ) {
            str +=  lead + "    DepartName = " + m_departName + "\n";
        }
        if ( hasDraught() ) {
            DecimalFormat nfm_draught = new DecimalFormat("0.00");
            str +=  lead + "    Draught = " + nfm_draught.format(m_draught) + "\n";
        }
        if ( hasETA() ) {
            str +=  lead + "    ETA = " + df.format(m_ETA) + "\n";
        }
        if ( hasATD() ) {
            str +=  lead + "    ATD = " + df.format(m_ATD) + "\n";
        }
        if ( hasISPSLevel() ) {
            str +=  lead + "    ISPSLevel = " + nf.format(m_ISPSLevel) + "\n";
        }
        if ( hasOverSizedLength() ) {
            DecimalFormat nfm_overSizedLength = new DecimalFormat("0.0");
            str +=  lead + "    OverSizedLength = " + nfm_overSizedLength.format(m_overSizedLength) + "\n";
        }
        if ( hasOverSizedWidth() ) {
            DecimalFormat nfm_overSizedWidth = new DecimalFormat("0.0");
            str +=  lead + "    OverSizedWidth = " + nfm_overSizedWidth.format(m_overSizedWidth) + "\n";
        }
        if ( hasPersonsOnBoard() ) {
            str +=  lead + "    PersonsOnBoard = " + m_personsOnBoard + "\n";
        }
        if ( hasPilots() ) {
            str +=  lead + "    Pilots = " + nf.format(m_pilots) + "\n";
        }
        if ( hasRouteBound() ) {
            str +=  lead + "    RouteBound = " + m_routeBound + "\n";
        }
        if ( hasSourceId() ) {
            str +=  lead + "    SourceId = " + m_sourceId + "\n";
        }
            str +=  lead + "    SourceName = " + m_sourceName + "\n";
            str +=  lead + "    SourceType = " + m_sourceType + "\n";
        if ( hasTankerStatus() ) {
            str +=  lead + "    TankerStatus = " + m_tankerStatus + "\n";
        }
        if ( hasTugs() ) {
            str +=  lead + "    Tugs = " + m_tugs + "\n";
        }
            str +=  lead + "    UpdateTime = " + df.format(m_updateTime) + "\n";
        for(int i=0; i < m_waypoints.size(); i++ ) {
           Waypoint attribute = m_waypoints.get(i);
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
		for(Waypoint waypoint : m_waypoints)
			waypoint.fillMap(map);	
		if(m_airDraughtPresent)
			map.addAttributeValue("voyageData_airDraught", m_airDraught);
		if(m_idPresent)
			map.addAttributeValue("voyageData_id", m_id);
		if(m_cargoTypeIMOPresent)
			map.addAttributeValue("voyageData_cargoTypeIMO", m_cargoTypeIMO);
		if(m_contactIdentityPresent)
			map.addAttributeValue("voyageData_contactIdentity", m_contactIdentity);
		if(m_destCodePresent)
			map.addAttributeValue("voyageData_destCode", m_destCode);
		if(m_destNamePresent)
			map.addAttributeValue("voyageData_destName", m_destName);
		if(m_departCodePresent)
			map.addAttributeValue("voyageData_departCode", m_departCode);
		if(m_departNamePresent)
			map.addAttributeValue("voyageData_departName", m_departName);
		if(m_draughtPresent)
			map.addAttributeValue("voyageData_draught", m_draught);
		if(m_ETAPresent)
			map.addAttributeValue("voyageData_ETA", m_ETA);
		if(m_ATDPresent)
			map.addAttributeValue("voyageData_ATD", m_ATD);
		if(m_ISPSLevelPresent)
			map.addAttributeValue("voyageData_ISPSLevel", m_ISPSLevel);
		if(m_overSizedLengthPresent)
			map.addAttributeValue("voyageData_overSizedLength", m_overSizedLength);
		if(m_overSizedWidthPresent)
			map.addAttributeValue("voyageData_overSizedWidth", m_overSizedWidth);
		if(m_personsOnBoardPresent)
			map.addAttributeValue("voyageData_personsOnBoard", m_personsOnBoard);
		if(m_pilotsPresent)
			map.addAttributeValue("voyageData_pilots", m_pilots);
		if(m_routeBoundPresent)
			map.addAttributeValue("voyageData_routeBound", m_routeBound);
		if(m_sourceIdPresent)
			map.addAttributeValue("voyageData_sourceId", m_sourceId);
		if(m_sourceNamePresent)
			map.addAttributeValue("voyageData_sourceName", m_sourceName);
		if(m_sourceTypePresent)
			map.addAttributeValue("voyageData_sourceType", m_sourceType);
		if(m_tankerStatusPresent)
			map.addAttributeValue("voyageData_tankerStatus", m_tankerStatus);
		if(m_tugsPresent)
			map.addAttributeValue("voyageData_tugs", m_tugs); 
		if(m_updateTimePresent)
			map.addAttributeValue("voyageData_updateTime", m_updateTime);
	}
}
