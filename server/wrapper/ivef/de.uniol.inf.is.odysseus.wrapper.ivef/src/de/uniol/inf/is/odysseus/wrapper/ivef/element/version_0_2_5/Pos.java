package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_2_5;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;

/**
 * @author msalous
 *
 */
public class Pos implements IIvefElement { 

    private double m_altitude; // default value is uninitialized
    private boolean m_altitudePresent;
    private double m_estAccAlt; // default value is uninitialized
    private boolean m_estAccAltPresent;
    private double m_estAccLat; // default value is uninitialized
    private boolean m_estAccLatPresent;
    private double m_estAccLong; // default value is uninitialized
    private boolean m_estAccLongPresent;
    private double m_lat; // default value is uninitialized
    private boolean m_latPresent;
    private double m_long; // default value is uninitialized
    private boolean m_longPresent;

    public Pos() {

        m_altitudePresent = false;
        m_estAccAltPresent = false;
        m_estAccLatPresent = false;
        m_estAccLongPresent = false;
        m_latPresent = false;
        m_longPresent = false;
    }

    public Pos(Pos val) {

        m_altitude = val.getAltitude();
        if (val != null) {
            m_altitudePresent = true;
        }
        m_estAccAlt = val.getEstAccAlt();
        if (val != null) {
            m_estAccAltPresent = true;
        }
        m_estAccLat = val.getEstAccLat();
        if (val != null) {
            m_estAccLatPresent = true;
        }
        m_estAccLong = val.getEstAccLong();
        if (val != null) {
            m_estAccLongPresent = true;
        }
        m_lat = val.getLat();
        if (val != null) {
            m_latPresent = true;
        }
        m_long = val.getLong();
        if (val != null) {
            m_longPresent = true;
        }
    }

    public boolean setAltitude(double val) {

        m_altitudePresent = true;
        m_altitude = val;
        return true;
    }

    public double getAltitude() {

        return m_altitude;
    }

    public boolean hasAltitude() {

        return m_altitudePresent;
    }

    public boolean setEstAccAlt(double val) {

        m_estAccAltPresent = true;
        m_estAccAlt = val;
        return true;
    }

    public double getEstAccAlt() {

        return m_estAccAlt;
    }

    public boolean hasEstAccAlt() {

        return m_estAccAltPresent;
    }

    public boolean setEstAccLat(double val) {

        m_estAccLatPresent = true;
        m_estAccLat = val;
        return true;
    }

    public double getEstAccLat() {

        return m_estAccLat;
    }

    public boolean hasEstAccLat() {

        return m_estAccLatPresent;
    }

    public boolean setEstAccLong(double val) {

        m_estAccLongPresent = true;
        m_estAccLong = val;
        return true;
    }

    public double getEstAccLong() {

        return m_estAccLong;
    }

    public boolean hasEstAccLong() {

        return m_estAccLongPresent;
    }

    public boolean setLat(double val) {

        if (val < -90)
          return false;
        if (val > 90)
          return false;
        m_latPresent = true;
        m_lat = val;
        return true;
    }

    public double getLat() {

        return m_lat;
    }

    public boolean setLong(double val) {

        if (val <= -180)
          return false;
        if (val > 180)
          return false;
        m_longPresent = true;
        m_long = val;
        return true;
    }

    public double getLong() {

        return m_long;
    }
    @Override
    public String toXML() {

        String xml = "<Pos";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasAltitude() ) {
            xml += " Altitude=\"" + nf.format(m_altitude) + "\"";
        }
        if ( hasEstAccAlt() ) {
            xml += " EstAccAlt=\"" + nf.format(m_estAccAlt) + "\"";
        }
        if ( hasEstAccLat() ) {
            xml += " EstAccLat=\"" + nf.format(m_estAccLat) + "\"";
        }
        if ( hasEstAccLong() ) {
            xml += " EstAccLong=\"" + nf.format(m_estAccLong) + "\"";
        }
        if ( m_latPresent ) {
            DecimalFormat nfm_lat = new DecimalFormat("0.00000");
            xml += " Lat=\"" + nfm_lat.format(m_lat) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( m_longPresent ) {
            DecimalFormat nfm_long = new DecimalFormat("0.00000");
            xml += " Long=\"" + nfm_long.format(m_long) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        xml += "/>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Pos\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasAltitude() ) {
            str +=  lead + "    Altitude = " + nf.format(m_altitude) + "\n";
        }
        if ( hasEstAccAlt() ) {
            str +=  lead + "    EstAccAlt = " + nf.format(m_estAccAlt) + "\n";
        }
        if ( hasEstAccLat() ) {
            str +=  lead + "    EstAccLat = " + nf.format(m_estAccLat) + "\n";
        }
        if ( hasEstAccLong() ) {
            str +=  lead + "    EstAccLong = " + nf.format(m_estAccLong) + "\n";
        }
        DecimalFormat nfm_lat = new DecimalFormat("0.00000");
            str +=  lead + "    Lat = " + nfm_lat.format(m_lat) + "\n";
        DecimalFormat nfm_long = new DecimalFormat("0.00000");
            str +=  lead + "    Long = " + nfm_long.format(m_long) + "\n";
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
		if(m_altitudePresent)
			map.addAttributeValue("pos_altitude", m_altitude);
		if(m_estAccAltPresent)
			map.addAttributeValue("pos_estAccAlt", m_estAccAlt);
		if(m_estAccLatPresent)
			map.addAttributeValue("pos_estAccLat", m_estAccLat); 
		if(m_estAccLongPresent)
			map.addAttributeValue("pos_estAccLong", m_estAccLong);
		if(m_latPresent)
			map.addAttributeValue("pos_lat", m_lat);
		if(m_longPresent)
			map.addAttributeValue("pos_long", m_long); 
	}
}
