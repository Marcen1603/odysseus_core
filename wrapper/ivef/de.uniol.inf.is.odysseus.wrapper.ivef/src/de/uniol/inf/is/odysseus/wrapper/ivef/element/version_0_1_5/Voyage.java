/* 
 *
 */

package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_0_1_5;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;


public class Voyage implements IIvefElement { 

    private String m_id = "0";//Initialized  instead of using a flag! // default value is uninitialized
    private String m_sourceName = "OFFIS";//Initialized  instead of using a flag! // default value is uninitialized
    private int m_source = 3;//3 expresses that it's manual set if not received // default value is uninitialized
    private int m_cargoType; // default value is uninitialized
    private boolean m_cargoTypePresent;
    private String m_destination = "";//Initialized  instead of using a flag! // default value is uninitialized
    private boolean m_destinationPresent;
    private String m_ETA; // default value is uninitialized
    private boolean m_ETAPresent;
    private String m_ATA; // default value is uninitialized
    private boolean m_ATAPresent;
    private double m_personsOnBoard; // default value is uninitialized
    private boolean m_personsOnBoardPresent;
    private double m_airDraught; // default value is uninitialized
    private boolean m_airDraughtPresent;
    private double m_draught; // default value is uninitialized
    private boolean m_draughtPresent;

    public Voyage() {

        m_cargoTypePresent = false;
        m_destinationPresent = false;
        m_ETAPresent = false;
        m_ATAPresent = false;
        m_personsOnBoardPresent = false;
        m_airDraughtPresent = false;
        m_draughtPresent = false;
    }

    public Voyage(Voyage val) {

        m_id = val.getId();
        m_sourceName = val.getSourceName();
        m_source = val.getSource();
        m_cargoTypePresent = val.hasCargoType();
        m_cargoType = val.getCargoType();
        m_destinationPresent = val.hasDestination();
        m_destination = val.getDestination();
        m_ETAPresent = val.hasETA();
        m_ETA = val.getETA();
        m_ATAPresent = val.hasATA();
        m_ATA = val.getATA();
        m_personsOnBoardPresent = val.hasPersonsOnBoard();
        m_personsOnBoard = val.getPersonsOnBoard();
        m_airDraughtPresent = val.hasAirDraught();
        m_airDraught = val.getAirDraught();
        m_draughtPresent = val.hasDraught();
        m_draught = val.getDraught();
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

    public void setCargoType(int val) {

        if ( ( val != 0 ) &&
             ( val != 1 ) &&
             ( val != 2 ) &&
             ( val != 3 ) &&
             ( val != 4 ) &&
             ( val != 9 ) )
            return;
        m_cargoTypePresent = true;
        m_cargoType = val;
    }

    public int getCargoType() {

        return m_cargoType;
    }

    public boolean hasCargoType() {

        return m_cargoTypePresent;
    }

    public void setDestination(String val) {

        m_destinationPresent = true;
        m_destination = val;
    }

    public String getDestination() {

        return m_destination;
    }

    public boolean hasDestination() {

        return m_destinationPresent;
    }

    public void setETA(String val) {

        m_ETAPresent = true;
        m_ETA = val;
    }

    public String getETA() {

        return m_ETA;
    }

    public boolean hasETA() {

        return m_ETAPresent;
    }

    public void setATA(String val) {

        m_ATAPresent = true;
        m_ATA = val;
    }

    public String getATA() {

        return m_ATA;
    }

    public boolean hasATA() {

        return m_ATAPresent;
    }

    public void setPersonsOnBoard(double val) {

        if (val < 0)
          return;
        m_personsOnBoardPresent = true;
        m_personsOnBoard = val;
    }

    public double getPersonsOnBoard() {

        return m_personsOnBoard;
    }

    public boolean hasPersonsOnBoard() {

        return m_personsOnBoardPresent;
    }

    public void setAirDraught(double val) {

        if (val < 0)
          return;
        m_airDraughtPresent = true;
        m_airDraught = val;
    }

    public double getAirDraught() {

        return m_airDraught;
    }

    public boolean hasAirDraught() {

        return m_airDraughtPresent;
    }

    public void setDraught(double val) {

        if (val < 0)
          return;
        m_draughtPresent = true;
        m_draught = val;
    }

    public double getDraught() {

        return m_draught;
    }

    public boolean hasDraught() {

        return m_draughtPresent;
    }
    @Override
    public String toXML() {

        String xml = "<Voyage";
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        xml += " Id=\"" + encode( m_id) + "\"";
        xml += " SourceName=\"" + encode( m_sourceName) + "\"";
        xml += " Source=\"" + m_source + "\"";
        if ( hasCargoType() ) {
            xml += " CargoType=\"" + m_cargoType + "\"";
        }
        if ( hasDestination() ) {
            xml += " Destination=\"" + encode( m_destination) + "\"";
        }
        if ( hasETA() ) {
        	try {
				Date etaDate = df.parse(m_ETA);
//				xml += " ETA=\"" + df.format(etaDate) + "\"";
				xml += " ETA=\"" + df.format(etaDate) + "Z\"";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }
        if ( hasATA() ) {
            xml += " ATA=\"" + df.format(m_ATA) + "\"";
        }
        if ( hasPersonsOnBoard() ) {
            xml += " PersonsOnBoard=\"" + m_personsOnBoard + "\"";
        }
        if ( hasAirDraught() ) {
            xml += " AirDraught=\"" + m_airDraught + "\"";
        }
        if ( hasDraught() ) {
            xml += " Draught=\"" + m_draught + "\"";
        }
        xml += ">\n";
        xml += "</Voyage>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "Voyage\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");

        str +=  lead + "    Id = " + m_id + "\n";
        str +=  lead + "    SourceName = " + m_sourceName + "\n";
        str +=  lead + "    Source = " + m_source + "\n";
        if ( hasCargoType() ) {
            str +=  lead + "    CargoType = " + m_cargoType + "\n";
        }
        if ( hasDestination() ) {
            str +=  lead + "    Destination = " + m_destination + "\n";
        }
        if ( hasETA() ) {
            str +=  lead + "    ETA = " + df.format(m_ETA) + "\n";
        }
        if ( hasATA() ) {
            str +=  lead + "    ATA = " + df.format(m_ATA) + "\n";
        }
        if ( hasPersonsOnBoard() ) {
            str +=  lead + "    PersonsOnBoard = " + m_personsOnBoard + "\n";
        }
        if ( hasAirDraught() ) {
            str +=  lead + "    AirDraught = " + m_airDraught + "\n";
        }
        if ( hasDraught() ) {
            str +=  lead + "    Draught = " + m_draught + "\n";
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
		map.addAttributeValue("voyage_id", m_id);
		map.addAttributeValue("voyage_sourceName", m_sourceName);
		map.addAttributeValue("voyage_source", m_source);
	    if(m_cargoTypePresent)
	    	map.addAttributeValue("voyage_cargoType", m_cargoType);
	    if(m_destinationPresent)
	    	map.addAttributeValue("voyage_destination", m_destination);
	    if(m_ETAPresent)
	    	map.addAttributeValue("voyage_ETA", m_ETA);
	    if(m_ATAPresent)
	    	map.addAttributeValue("voyage_ATA", m_ATA);
	    if(m_personsOnBoardPresent)
	    	map.addAttributeValue("voyage_personsOnBoard", m_personsOnBoard);
	    if(m_airDraughtPresent)
	    	map.addAttributeValue("voyage_airDraught", m_airDraught);
	    if(m_draughtPresent)
	    	map.addAttributeValue("voyage_draught", m_draught);
	}
}
