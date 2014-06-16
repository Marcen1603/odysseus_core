package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.Pos;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.NavStatus;

/**
 * @author msalous
 *
 */
public class TrackData implements IIvefElement { 

    private Vector<Pos> m_poss = new Vector<Pos>();
    private Vector<NavStatus> m_navStatuss = new Vector<NavStatus>();
    private double m_COG; // default value is uninitialized
    private boolean m_COGPresent;
    private double m_estAccSOG; // default value is uninitialized
    private boolean m_estAccSOGPresent;
    private double m_estAccCOG; // default value is uninitialized
    private boolean m_estAccCOGPresent;
    private int m_id; // default value is uninitialized
    private boolean m_idPresent;
    private double m_length; // default value is uninitialized
    private boolean m_lengthPresent;
    private double m_heading; // default value is uninitialized
    private boolean m_headingPresent;
    private double m_ROT; // default value is uninitialized
    private boolean m_ROTPresent;
    private double m_SOG; // default value is uninitialized
    private boolean m_SOGPresent;
    private String m_sourceId; // default value is uninitialized
    private boolean m_sourceIdPresent;
    private String m_sourceName; // default value is uninitialized
    private boolean m_sourceNamePresent;
    private Date m_updateTime; // default value is uninitialized
    private boolean m_updateTimePresent;
    private int m_trackStatus; // default value is uninitialized
    private boolean m_trackStatusPresent;
    private double m_width; // default value is uninitialized
    private boolean m_widthPresent;

    public TrackData() {

        m_COGPresent = false;
        m_estAccSOGPresent = false;
        m_estAccCOGPresent = false;
        m_idPresent = false;
        m_lengthPresent = false;
        m_headingPresent = false;
        m_ROTPresent = false;
        m_SOGPresent = false;
        m_sourceIdPresent = false;
        m_sourceNamePresent = false;
        m_updateTimePresent = false;
        m_trackStatusPresent = false;
        m_widthPresent = false;
    }

    public TrackData(TrackData val) {

        for(int i=0; i < val.countOfPoss(); i++ ) {
            m_poss.add( val.getPosAt(i) );
        }
        for(int i=0; i < val.countOfNavStatuss(); i++ ) {
            m_navStatuss.add( val.getNavStatusAt(i) );
        }
        m_COG = val.getCOG();
        if (val != null) {
            m_COGPresent = true;
        }
        m_estAccSOG = val.getEstAccSOG();
        if (val != null) {
            m_estAccSOGPresent = true;
        }
        m_estAccCOG = val.getEstAccCOG();
        if (val != null) {
            m_estAccCOGPresent = true;
        }
        m_id = val.getId();
        if (val != null) {
            m_idPresent = true;
        }
        m_length = val.getLength();
        if (val != null) {
            m_lengthPresent = true;
        }
        m_heading = val.getHeading();
        if (val != null) {
            m_headingPresent = true;
        }
        m_ROT = val.getROT();
        if (val != null) {
            m_ROTPresent = true;
        }
        m_SOG = val.getSOG();
        if (val != null) {
            m_SOGPresent = true;
        }
        m_sourceId = val.getSourceId();
        if (val != null) {
            m_sourceIdPresent = true;
        }
        m_sourceName = val.getSourceName();
        if (val != null) {
            m_sourceNamePresent = true;
        }
        m_updateTime = val.getUpdateTime();
        if (val != null) {
            m_updateTimePresent = true;
        }
        m_trackStatus = val.getTrackStatus();
        if (val != null) {
            m_trackStatusPresent = true;
        }
        m_width = val.getWidth();
        if (val != null) {
            m_widthPresent = true;
        }
    }

    public boolean removePos(Pos val) {
        if (m_poss.size() <= 0) {
            return false; // scalar already at minOccurs
        }

        m_poss.remove(val);
        return true;
    }

    public boolean addPos(Pos val) {

        m_poss.add(val);
        return true;
    }

    public Pos getPosAt(int i) {

        return m_poss.get(i);
    }

    public int countOfPoss() {

        return m_poss.size();
    }

    public boolean removeNavStatus(NavStatus val) {
        if (m_navStatuss.size() <= 0) {
            return false; // scalar already at minOccurs
        }

        m_navStatuss.remove(val);
        return true;
    }

    public boolean addNavStatus(NavStatus val) {

        m_navStatuss.add(val);
        return true;
    }

    public NavStatus getNavStatusAt(int i) {

        return m_navStatuss.get(i);
    }

    public int countOfNavStatuss() {

        return m_navStatuss.size();
    }

    public boolean setCOG(double val) {

        if (val < 0)
          return false;
        if (val > 360)
          return false;
        m_COGPresent = true;
        m_COG = val;
        return true;
    }

    public double getCOG() {

        return m_COG;
    }

    public boolean setEstAccSOG(double val) {

        m_estAccSOGPresent = true;
        m_estAccSOG = val;
        return true;
    }

    public double getEstAccSOG() {

        return m_estAccSOG;
    }

    public boolean hasEstAccSOG() {

        return m_estAccSOGPresent;
    }

    public boolean setEstAccCOG(double val) {

        m_estAccCOGPresent = true;
        m_estAccCOG = val;
        return true;
    }

    public double getEstAccCOG() {

        return m_estAccCOG;
    }

    public boolean hasEstAccCOG() {

        return m_estAccCOGPresent;
    }

    public boolean setId(int val) {

        m_idPresent = true;
        m_id = val;
        return true;
    }

    public int getId() {

        return m_id;
    }

    public boolean setLength(double val) {

        if (val <= 0)
          return false;
        m_lengthPresent = true;
        m_length = val;
        return true;
    }

    public double getLength() {

        return m_length;
    }

    public boolean hasLength() {

        return m_lengthPresent;
    }

    public boolean setHeading(double val) {

        if (val < 0)
          return false;
        if (val > 360)
          return false;
        m_headingPresent = true;
        m_heading = val;
        return true;
    }

    public double getHeading() {

        return m_heading;
    }

    public boolean hasHeading() {

        return m_headingPresent;
    }

    public boolean setROT(double val) {

        if (val <= -720)
          return false;
        if (val > 720)
          return false;
        m_ROTPresent = true;
        m_ROT = val;
        return true;
    }

    public double getROT() {

        return m_ROT;
    }

    public boolean hasROT() {

        return m_ROTPresent;
    }

    public boolean setSOG(double val) {

        if (val < 0)
          return false;
        m_SOGPresent = true;
        m_SOG = val;
        return true;
    }

    public double getSOG() {

        return m_SOG;
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

    public boolean setUpdateTime(Date val) {

        m_updateTimePresent = true;
        m_updateTime = val;
        return true;
    }

    public Date getUpdateTime() {

        return m_updateTime;
    }

    public boolean setTrackStatus(int val) {

        if ( ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) )
            return false;
        m_trackStatusPresent = true;
        m_trackStatus = val;
        return true;
    }

    public int getTrackStatus() {

        return m_trackStatus;
    }

    public boolean setWidth(double val) {

        if (val <= 0)
          return false;
        m_widthPresent = true;
        m_width = val;
        return true;
    }

    public double getWidth() {

        return m_width;
    }

    public boolean hasWidth() {

        return m_widthPresent;
    }
    @Override
    public String toXML() {

        String xml = "<TrackData";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( m_COGPresent ) {
            DecimalFormat nfm_COG = new DecimalFormat("0.0");
            xml += " COG=\"" + nfm_COG.format(m_COG) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( hasEstAccSOG() ) {
            xml += " EstAccSOG=\"" + nf.format(m_estAccSOG) + "\"";
        }
        if ( hasEstAccCOG() ) {
            xml += " EstAccCOG=\"" + nf.format(m_estAccCOG) + "\"";
        }
        if ( m_idPresent ) {
            xml += " Id=\"" + m_id + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( hasLength() ) {
            xml += " Length=\"" + nf.format(m_length) + "\"";
        }
        if ( hasHeading() ) {
            xml += " Heading=\"" + nf.format(m_heading) + "\"";
        }
        if ( hasROT() ) {
            DecimalFormat nfm_ROT = new DecimalFormat("0.0");
            xml += " ROT=\"" + nfm_ROT.format(m_ROT) + "\"";
        }
        if ( m_SOGPresent ) {
            DecimalFormat nfm_SOG = new DecimalFormat("0.0");
            xml += " SOG=\"" + nfm_SOG.format(m_SOG) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( hasSourceId() ) {
            xml += " SourceId=\"" + encode( m_sourceId) + "\"";
        }
        if ( m_sourceNamePresent ) {
            xml += " SourceName=\"" + encode( m_sourceName) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( m_updateTimePresent ) {
            xml += " UpdateTime=\"" + df.format(m_updateTime) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( m_trackStatusPresent ) {
            xml += " TrackStatus=\"" + m_trackStatus + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( hasWidth() ) {
            xml += " Width=\"" + nf.format(m_width) + "\"";
        }
        String dataMember;
        xml += ">\n";
        if (m_poss.size() < 0) {
            return null; // not enough values
        }
        for(int i=0; i < m_poss.size(); i++ ) {
           Pos attribute = m_poss.get(i);
            dataMember = attribute.toXML();
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if (m_navStatuss.size() < 0) {
            return null; // not enough values
        }
        for(int i=0; i < m_navStatuss.size(); i++ ) {
           NavStatus attribute = m_navStatuss.get(i);
            dataMember = attribute.toXML();
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        xml += "</TrackData>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "TrackData\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        DecimalFormat nfm_COG = new DecimalFormat("0.0");
            str +=  lead + "    COG = " + nfm_COG.format(m_COG) + "\n";
        if ( hasEstAccSOG() ) {
            str +=  lead + "    EstAccSOG = " + nf.format(m_estAccSOG) + "\n";
        }
        if ( hasEstAccCOG() ) {
            str +=  lead + "    EstAccCOG = " + nf.format(m_estAccCOG) + "\n";
        }
            str +=  lead + "    Id = " + m_id + "\n";
        if ( hasLength() ) {
            str +=  lead + "    Length = " + nf.format(m_length) + "\n";
        }
        if ( hasHeading() ) {
            str +=  lead + "    Heading = " + nf.format(m_heading) + "\n";
        }
        if ( hasROT() ) {
            DecimalFormat nfm_ROT = new DecimalFormat("0.0");
            str +=  lead + "    ROT = " + nfm_ROT.format(m_ROT) + "\n";
        }
        DecimalFormat nfm_SOG = new DecimalFormat("0.0");
            str +=  lead + "    SOG = " + nfm_SOG.format(m_SOG) + "\n";
        if ( hasSourceId() ) {
            str +=  lead + "    SourceId = " + m_sourceId + "\n";
        }
            str +=  lead + "    SourceName = " + m_sourceName + "\n";
            str +=  lead + "    UpdateTime = " + df.format(m_updateTime) + "\n";
            str +=  lead + "    TrackStatus = " + m_trackStatus + "\n";
        if ( hasWidth() ) {
            str +=  lead + "    Width = " + nf.format(m_width) + "\n";
        }
        for(int i=0; i < m_poss.size(); i++ ) {
           Pos attribute = m_poss.get(i);
           str += attribute.toString(lead + "    ");
        }
        for(int i=0; i < m_navStatuss.size(); i++ ) {
           NavStatus attribute = m_navStatuss.get(i);
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
		for(Pos pos : m_poss)
			pos.fillMap(map);
		for(NavStatus navstatus : m_navStatuss)
			navstatus.fillMap(map);
		if(m_COGPresent)
			map.addAttributeValue("trackData_COG", m_COG);
		if(m_estAccSOGPresent)
			map.addAttributeValue("trackData_estAccSOG", m_estAccSOG);
		if(m_estAccCOGPresent)
			map.addAttributeValue("trackData_estAccCOG", m_estAccCOG);
		if(m_idPresent)
			map.addAttributeValue("trackData_id", m_id);  
		if(m_lengthPresent)
			map.addAttributeValue("trackData_length", m_length);
		if(m_headingPresent)
			map.addAttributeValue("trackData_heading", m_heading);
		if(m_ROTPresent)
			map.addAttributeValue("trackData_ROT", m_ROT);   
		if(m_SOGPresent)
			map.addAttributeValue("trackData_SOG", m_SOG);
		if(m_sourceIdPresent)
			map.addAttributeValue("trackData_sourceId", m_sourceId);
		if(m_sourceNamePresent)
			map.addAttributeValue("trackData_sourceName", m_sourceName);
		if(m_updateTimePresent)
			map.addAttributeValue("trackData_updateTime", m_updateTime);
		if(m_trackStatusPresent)
			map.addAttributeValue("trackData_trackStatus", m_trackStatus);
		if(m_widthPresent)
			map.addAttributeValue("trackData_width", m_width);
	}
}
