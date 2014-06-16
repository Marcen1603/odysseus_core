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
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Pos;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5.Sensor;

public class PosReport implements IIvefElement { 

    private Pos m_pos; // default value is uninitialized
    private Vector<Sensor> m_sensors = new Vector<Sensor>();
    private int m_id; //// default value is uninitialized
    private int m_sourceId; //// default value is uninitialized
    private Date m_updateTime; //// default value is uninitialized
    private Date m_updateTimeRadar; // default value is uninitialized
    private boolean m_updateTimeRadarPresent;
    private Date m_updateTimeAIS; // default value is uninitialized
    private boolean m_updateTimeAISPresent;
    private Date m_updateTimeDR; // default value is uninitialized
    private boolean m_updateTimeDRPresent;
    private Float m_SOG = (float)0; // default value is uninitialized
    private Float m_COG = (float)0; // default value is uninitialized
    private String m_lost="no"; //Initialized  instead of using a flag! // default value is uninitialized
    private double m_rateOfTurn; // default value is uninitialized
    private boolean m_rateOfTurnPresent;
    private Float m_orientation; // default value is uninitialized
    private boolean m_orientationPresent;
    private double m_length; // default value is uninitialized
    private boolean m_lengthPresent;
    private double m_breadth; // default value is uninitialized
    private boolean m_breadthPresent;
    private double m_altitude; // default value is uninitialized
    private boolean m_altitudePresent;
    private int m_navStatus; // default value is uninitialized
    private boolean m_navStatusPresent;
    private int m_updSensorType; // default value is uninitialized
    private boolean m_updSensorTypePresent;
    private boolean m_ATONOffPos; // default value is uninitialized
    private boolean m_ATONOffPosPresent;

    public PosReport() {

        m_updateTimeRadarPresent = false;
        m_updateTimeAISPresent = false;
        m_updateTimeDRPresent = false;
        m_rateOfTurnPresent = false;
        m_orientationPresent = false;
        m_lengthPresent = false;
        m_breadthPresent = false;
        m_altitudePresent = false;
        m_navStatusPresent = false;
        m_updSensorTypePresent = false;
        m_ATONOffPosPresent = false;
    }

    public PosReport(PosReport val) {

        m_pos = val.getPos();
        for(int i=0; i < val.countOfSensors(); i++ ) {
            m_sensors.add( val.getSensorAt(i) );
        }
        m_id = val.getId();
        m_sourceId = val.getSourceId();
        m_updateTime = val.getUpdateTime();
        m_updateTimeRadarPresent = val.hasUpdateTimeRadar();
        m_updateTimeRadar = val.getUpdateTimeRadar();
        m_updateTimeAISPresent = val.hasUpdateTimeAIS();
        m_updateTimeAIS = val.getUpdateTimeAIS();
        m_updateTimeDRPresent = val.hasUpdateTimeDR();
        m_updateTimeDR = val.getUpdateTimeDR();
        m_SOG = val.getSOG();
        m_COG = val.getCOG();
        m_lost = val.getLost();
        m_rateOfTurnPresent = val.hasRateOfTurn();
        m_rateOfTurn = val.getRateOfTurn();
        m_orientationPresent = val.hasOrientation();
        m_orientation = val.getOrientation();
        m_lengthPresent = val.hasLength();
        m_length = val.getLength();
        m_breadthPresent = val.hasBreadth();
        m_breadth = val.getBreadth();
        m_altitudePresent = val.hasAltitude();
        m_altitude = val.getAltitude();
        m_navStatusPresent = val.hasNavStatus();
        m_navStatus = val.getNavStatus();
        m_updSensorTypePresent = val.hasUpdSensorType();
        m_updSensorType = val.getUpdSensorType();
        m_ATONOffPosPresent = val.hasATONOffPos();
        m_ATONOffPos = val.getATONOffPos();
    }

    public void setPos(Pos val) {

        m_pos = val;
    }

    public Pos getPos() {

        return m_pos;
    }

    public void addSensor(Sensor val) {

        m_sensors.add(val);
    }

    public Sensor getSensorAt(int i) {

        return m_sensors.get(i);
    }

    public int countOfSensors() {

        return m_sensors.size();
    }

    public void setId(int val) {

        m_id = val;
    }

    public int getId() {

        return m_id;
    }

    public void setSourceId(int val) {

        m_sourceId = val;
    }

    public int getSourceId() {

        return m_sourceId;
    }

    public void setUpdateTime(Date val) {

        m_updateTime = val;
    }

    public Date getUpdateTime() {

        return m_updateTime;
    }

    public void setUpdateTimeRadar(Date val) {

        m_updateTimeRadarPresent = true;
        m_updateTimeRadar = val;
    }

    public Date getUpdateTimeRadar() {

        return m_updateTimeRadar;
    }

    public boolean hasUpdateTimeRadar() {

        return m_updateTimeRadarPresent;
    }

    public void setUpdateTimeAIS(Date val) {

        m_updateTimeAISPresent = true;
        m_updateTimeAIS = val;
    }

    public Date getUpdateTimeAIS() {

        return m_updateTimeAIS;
    }

    public boolean hasUpdateTimeAIS() {

        return m_updateTimeAISPresent;
    }

    public void setUpdateTimeDR(Date val) {

        m_updateTimeDRPresent = true;
        m_updateTimeDR = val;
    }

    public Date getUpdateTimeDR() {

        return m_updateTimeDR;
    }

    public boolean hasUpdateTimeDR() {

        return m_updateTimeDRPresent;
    }

    public void setSOG(Float val) {

        if (val < 0)
          return;
        m_SOG = val;
    }

    public Float getSOG() {

        return m_SOG;
    }

    public void setCOG(Float val) {

        if (val < 0)
          return;
        if (val > 360)
          return;
        m_COG = val;
    }

    public Float getCOG() {

        return m_COG;
    }

    public void setLost(String val) {
//        if ( ( val != "no" ) &&
//             ( val != "yes" ) )
//            return;
      if ( !(val.equals("no") ) &&
    	   !(val.equals("yes") ) )
    	  return;
    	m_lost = val;
    }

    public String getLost() {

        return m_lost;
    }

    public void setRateOfTurn(double val) {

        m_rateOfTurnPresent = true;
        m_rateOfTurn = val;
    }

    public double getRateOfTurn() {

        return m_rateOfTurn;
    }

    public boolean hasRateOfTurn() {

        return m_rateOfTurnPresent;
    }

    public void setOrientation(Float val) {

        if (val < 0)
          return;
        if (val > 360)
          return;
        m_orientationPresent = true;
        m_orientation = val;
    }

    public Float getOrientation() {

        return m_orientation;
    }

    public boolean hasOrientation() {

        return m_orientationPresent;
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

    public void setAltitude(double val) {

        m_altitudePresent = true;
        m_altitude = val;
    }

    public double getAltitude() {

        return m_altitude;
    }

    public boolean hasAltitude() {

        return m_altitudePresent;
    }

    public void setNavStatus(int val) {

        if ( ( val != 0 ) &&
             ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) &&
             ( val != 4 ) &&
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
             ( val != 15 ) )
            return;
        m_navStatusPresent = true;
        m_navStatus = val;
    }

    public int getNavStatus() {

        return m_navStatus;
    }

    public boolean hasNavStatus() {

        return m_navStatusPresent;
    }

    public void setUpdSensorType(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) &&
             ( val != 4 ) &&
             ( val != 5 ) )
            return;
        m_updSensorTypePresent = true;
        m_updSensorType = val;
    }

    public int getUpdSensorType() {

        return m_updSensorType;
    }

    public boolean hasUpdSensorType() {

        return m_updSensorTypePresent;
    }

    public void setATONOffPos(boolean val) {

        m_ATONOffPosPresent = true;
        m_ATONOffPos = val;
    }

    public boolean getATONOffPos() {

        return m_ATONOffPos;
    }

    public boolean hasATONOffPos() {

        return m_ATONOffPosPresent;
    }
    @Override
    public String toXML() {

        String xml = "<PosReport";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");

        xml += " Id=\"" + m_id + "\"";
        xml += " SourceId=\"" + m_sourceId + "\"";
        xml += " UpdateTime=\"" + df.format(m_updateTime) + "\"";
        //xml += " UpdateTime=\"" + "2014-05-05T06:07:06.481Z" + "\"";
        if ( hasUpdateTimeRadar() ) {
            xml += " UpdateTimeRadar=\"" + df.format(m_updateTimeRadar) + "\"";
        }
        if ( hasUpdateTimeAIS() ) {
            xml += " UpdateTimeAIS=\"" + df.format(m_updateTimeAIS) + "\"";
        }
        if ( hasUpdateTimeDR() ) {
            xml += " UpdateTimeDR=\"" + df.format(m_updateTimeDR) + "\"";
        }
        xml += " SOG=\"" + m_SOG + "\"";
        xml += " COG=\"" + m_COG + "\"";
        xml += " Lost=\"" + encode( m_lost) + "\"";
        if ( hasRateOfTurn() ) {
            xml += " RateOfTurn=\"" + m_rateOfTurn + "\"";
        }
        if ( hasOrientation() ) {
            xml += " Orientation=\"" + m_orientation + "\"";
        }
        if ( hasLength() ) {
            xml += " Length=\"" + m_length + "\"";
        }
        if ( hasBreadth() ) {
            xml += " Breadth=\"" + m_breadth + "\"";
        }
        if ( hasAltitude() ) {
            xml += " Altitude=\"" + m_altitude + "\"";
        }
        if ( hasNavStatus() ) {
            xml += " NavStatus=\"" + m_navStatus + "\"";
        }
        if ( hasUpdSensorType() ) {
            xml += " UpdSensorType=\"" + m_updSensorType + "\"";
        }
        if ( hasATONOffPos() ) {
            xml += " ATONOffPos=\"" + m_ATONOffPos + "\"";
        }
        xml += ">\n";
        if (m_pos != null)	// instead of using a flag!
        	xml +=  m_pos.toXML();
        for(int i=0; i < m_sensors.size(); i++ ) {
           Sensor attribute = m_sensors.get(i);
            xml += attribute.toXML();
        }
        xml += "</PosReport>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "PosReport\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");

        str +=  lead + "    Id = " + m_id + "\n";
        str +=  lead + "    SourceId = " + m_sourceId + "\n";
        str +=  lead + "    UpdateTime = " + df.format(m_updateTime) + "\n";
        if ( hasUpdateTimeRadar() ) {
            str +=  lead + "    UpdateTimeRadar = " + df.format(m_updateTimeRadar) + "\n";
        }
        if ( hasUpdateTimeAIS() ) {
            str +=  lead + "    UpdateTimeAIS = " + df.format(m_updateTimeAIS) + "\n";
        }
        if ( hasUpdateTimeDR() ) {
            str +=  lead + "    UpdateTimeDR = " + df.format(m_updateTimeDR) + "\n";
        }
        str +=  lead + "    SOG = " + m_SOG + "\n";
        str +=  lead + "    COG = " + m_COG + "\n";
        str +=  lead + "    Lost = " + m_lost + "\n";
        if ( hasRateOfTurn() ) {
            str +=  lead + "    RateOfTurn = " + m_rateOfTurn + "\n";
        }
        if ( hasOrientation() ) {
            str +=  lead + "    Orientation = " + m_orientation + "\n";
        }
        if ( hasLength() ) {
            str +=  lead + "    Length = " + m_length + "\n";
        }
        if ( hasBreadth() ) {
            str +=  lead + "    Breadth = " + m_breadth + "\n";
        }
        if ( hasAltitude() ) {
            str +=  lead + "    Altitude = " + m_altitude + "\n";
        }
        if ( hasNavStatus() ) {
            str +=  lead + "    NavStatus = " + m_navStatus + "\n";
        }
        if ( hasUpdSensorType() ) {
            str +=  lead + "    UpdSensorType = " + m_updSensorType + "\n";
        }
        if ( hasATONOffPos() ) {
            str +=  lead + "    ATONOffPos = " + m_ATONOffPos + "\n";
        }
        if(m_pos != null)	// instead of using a flag!
        	str +=  m_pos.toString(lead + "    ");
        for(int i=0; i < m_sensors.size(); i++ ) {
           Sensor attribute = m_sensors.get(i);
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
		if(m_pos != null)
			m_pos.fillMap(map);
		for(Sensor sensor : m_sensors)
			sensor.fillMap(map);
	    map.addAttributeValue("posReport_id", m_id);
	    map.addAttributeValue("posReport_sourceId", m_sourceId);
	    map.addAttributeValue("posReport_updateTime", m_updateTime);
	    if(m_updateTimeRadarPresent)
	    	map.addAttributeValue("posReport_updateTimeRadar", m_updateTimeRadar);
	    if(m_updateTimeAISPresent)
	    	map.addAttributeValue("posReport_updateTimeAIS", m_updateTimeAIS);
	    if(m_updateTimeDRPresent)
	    	map.addAttributeValue("posReport_updateTimeDR", m_updateTimeDR);
	    map.addAttributeValue("posReport_SOG", m_SOG);
	    map.addAttributeValue("posReport_COG", m_COG);
	    map.addAttributeValue("posReport_lost", m_lost);
	    if(m_rateOfTurnPresent)
	    	map.addAttributeValue("posReport_rateOfTurn", m_rateOfTurn);
	    if(m_orientationPresent)
	    	map.addAttributeValue("posReport_orientation", m_orientation);
	    if(m_lengthPresent)
	    	map.addAttributeValue("posReport_length", m_length);
	    if(m_breadthPresent)
	    	map.addAttributeValue("posReport_breadth", m_breadth);
	    if(m_altitudePresent)
	    	map.addAttributeValue("posReport_altitude", m_altitude);
	    if(m_navStatusPresent)
	    	map.addAttributeValue("posReport_navStatus", m_navStatus);
	    if(m_updSensorTypePresent)
	    	map.addAttributeValue("posReport_updSensorType", m_updSensorType);
	    if(m_ATONOffPosPresent)
	    	map.addAttributeValue("posReport_ATONOffPos", m_ATONOffPos);
	}
}
