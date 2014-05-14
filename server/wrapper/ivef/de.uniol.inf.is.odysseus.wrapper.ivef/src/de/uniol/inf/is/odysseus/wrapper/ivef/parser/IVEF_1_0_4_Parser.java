package de.uniol.inf.is.odysseus.wrapper.ivef.parser;

import java.util.*;
import java.util.regex.*;
import java.text.DateFormat;
import java.io.*;
import java.text.SimpleDateFormat;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.*;

/**
 * @author msalous
 *
 */
public class IVEF_1_0_4_Parser extends DefaultHandler { 

    private String m_dataBuffer = new String();
    @SuppressWarnings("unused")
	private String m_characterBuffer = new String();
    //private ParserListener m_handler =  null;
    private MSG_IVEF m_ivef = null;
    private boolean m_ivefPresent = false;
    private Stack<Object> m_objStack = new Stack<Object>();
    private SAXParser m_parser; // init in constructor
    private Pattern m_closeTagsPattern; 

    public IVEF_1_0_4_Parser() {

        //m_handler = handler;

        // set the parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        m_closeTagsPattern = Pattern.compile( "</MSG_IVEF([A-Za-z0-9]*)>");
        try { 
            m_parser = factory.newSAXParser();
        } catch(Exception e) {
            e.printStackTrace();
        }
}
     // Character buffer routine
     public void characters(char[] ch, int start, int length) throws SAXException {

         m_characterBuffer += ch;
     };

    public void startElement(String namespaceUri,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {

         m_characterBuffer = ""; // reset buffer
    // check all possible options
    if (qName == "MSG_IVEF") {
        MSG_IVEF obj = new MSG_IVEF();
        m_objStack.push( obj );
    }
    else if (qName == "Body") {
        Body obj = new Body();
        m_objStack.push( obj );
    }
    else if (qName == "ObjectDatas") {
        ObjectDatas obj = new ObjectDatas();
        m_objStack.push( obj );
    }
    else if (qName == "Area") {
        Area obj = new Area();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "Name") {
                String val = value;
                if (! obj.setName(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "OtherId") {
        OtherId obj = new OtherId();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "Id") {
                String val = value;
                if (! obj.setId(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Value") {
                String val = value;
                if (! obj.setValue(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "OtherName") {
        OtherName obj = new OtherName();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "Lang") {
                String val = value;
                if (! obj.setLang(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Name") {
                String val = value;
                if (! obj.setName(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "Header") {
        Header obj = new Header();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "MsgRefId") {
                String val = value;
                if (! obj.setMsgRefId(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Version") {
                String val = value;
                if (! obj.setVersion(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "LoginRequest") {
        LoginRequest obj = new LoginRequest();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "Encryption") {
                int val = Integer.parseInt(value);
                if (! obj.setEncryption(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Name") {
                String val = value;
                if (! obj.setName(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Password") {
                String val = value;
                if (! obj.setPassword(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "LoginResponse") {
        LoginResponse obj = new LoginResponse();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "Reason") {
                String val = value;
                if (! obj.setReason(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "ResponseOn") {
                String val = value;
                if (! obj.setResponseOn(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Result") {
                int val = Integer.parseInt(value);
                if (! obj.setResult(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "Logout") {
        Logout obj = new Logout();
        m_objStack.push( obj );
    }
    else if (qName == "ObjectData") {
        ObjectData obj = new ObjectData();
        m_objStack.push( obj );
    }
    else if (qName == "Ping") {
        Ping obj = new Ping();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "TimeStamp") {
                // if the time ends on a Z it is UTC, else localtime 
                Date val = new Date(); // starts since the epoch
                try { 
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    if ( value.charAt(value.length()-1) == 'Z' )                        df.setTimeZone(TimeZone.getTimeZone("UTC"));                    val = df.parse( value );
                } catch(Exception e) {
                    try { // if there are no miliseconds they will not be sent
                       DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                       if ( value.charAt(value.length()-1) == 'Z' )                          df.setTimeZone(TimeZone.getTimeZone("UTC"));                       val = df.parse( value );
                    } catch(Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (! obj.setTimeStamp(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "Pong") {
        Pong obj = new Pong();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "ResponseOn") {
                String val = value;
                if (! obj.setResponseOn(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "SourceId") {
                int val = Integer.parseInt(value);
                if (! obj.setSourceId(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "TimeStamp") {
                // if the time ends on a Z it is UTC, else localtime 
                Date val = new Date(); // starts since the epoch
                try { 
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    if ( value.charAt(value.length()-1) == 'Z' )                        df.setTimeZone(TimeZone.getTimeZone("UTC"));                    val = df.parse( value );
                } catch(Exception e) {
                    try { // if there are no miliseconds they will not be sent
                       DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                       if ( value.charAt(value.length()-1) == 'Z' )                          df.setTimeZone(TimeZone.getTimeZone("UTC"));                       val = df.parse( value );
                    } catch(Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (! obj.setTimeStamp(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "Pos") {
        Pos obj = new Pos();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "Altitude") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setAltitude(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "EstAccAlt") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setEstAccAlt(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "EstAccLat") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setEstAccLat(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "EstAccLong") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setEstAccLong(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Lat") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setLat(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Long") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setLong(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "ServerStatus") {
        ServerStatus obj = new ServerStatus();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "ContactIdentity") {
                String val = value;
                if (! obj.setContactIdentity(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Details") {
                String val = value;
                if (! obj.setDetails(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Status") {
                boolean val = (value.toUpperCase().equals("YES") ||
                               value.toUpperCase().equals("TRUE") ||
                               value.toUpperCase().equals("1"));
                if (! obj.setStatus(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "ServiceRequest") {
        ServiceRequest obj = new ServiceRequest();
        m_objStack.push( obj );
    }
    else if (qName == "Transmission") {
        Transmission obj = new Transmission();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "Type") {
                int val = Integer.parseInt(value);
                if (! obj.setType(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Period") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setPeriod(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "Item") {
        Item obj = new Item();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "DataSelector") {
                int val = Integer.parseInt(value);
                if (! obj.setDataSelector(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "FieldSelector") {
                String val = value;
                if (! obj.setFieldSelector(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "Filter") {
        Filter obj = new Filter();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "Predicate") {
                String val = value;
                if (! obj.setPredicate(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "ServiceRequestResponse") {
        ServiceRequestResponse obj = new ServiceRequestResponse();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "Reason") {
                String val = value;
                if (! obj.setReason(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "ResponseOn") {
                String val = value;
                if (! obj.setResponseOn(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Result") {
                int val = Integer.parseInt(value);
                if (! obj.setResult(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "TaggedItem") {
        TaggedItem obj = new TaggedItem();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "Key") {
                String val = value;
                if (! obj.setKey(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Value") {
                String val = value;
                if (! obj.setValue(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "TrackData") {
        TrackData obj = new TrackData();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "COG") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setCOG(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "EstAccSOG") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setEstAccSOG(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "EstAccCOG") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setEstAccCOG(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Id") {
                int val = Integer.parseInt(value);
                if (! obj.setId(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Length") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setLength(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Heading") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setHeading(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "ROT") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setROT(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "SOG") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setSOG(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "SourceId") {
                String val = value;
                if (! obj.setSourceId(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "SourceName") {
                String val = value;
                if (! obj.setSourceName(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "UpdateTime") {
                // if the time ends on a Z it is UTC, else localtime 
                Date val = new Date(); // starts since the epoch
                try { 
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    if ( value.charAt(value.length()-1) == 'Z' )                        df.setTimeZone(TimeZone.getTimeZone("UTC"));                    val = df.parse( value );
                } catch(Exception e) {
                    try { // if there are no miliseconds they will not be sent
                       DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                       if ( value.charAt(value.length()-1) == 'Z' )                          df.setTimeZone(TimeZone.getTimeZone("UTC"));                       val = df.parse( value );
                    } catch(Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (! obj.setUpdateTime(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "TrackStatus") {
                int val = Integer.parseInt(value);
                if (! obj.setTrackStatus(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Width") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setWidth(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "NavStatus") {
        NavStatus obj = new NavStatus();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "Value") {
                int val = Integer.parseInt(value);
                if (! obj.setValue(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "VesselData") {
        VesselData obj = new VesselData();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "Class") {
                int val = Integer.parseInt(value);
                if (! obj.setIVEFClass(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "BlackListed") {
                boolean val = (value.toUpperCase().equals("YES") ||
                               value.toUpperCase().equals("TRUE") ||
                               value.toUpperCase().equals("1"));
                if (! obj.setBlackListed(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Id") {
                int val = Integer.parseInt(value);
                if (! obj.setId(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "SpecialAttention") {
                String val = value;
                if (! obj.setSpecialAttention(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "SourceId") {
                String val = value;
                if (! obj.setSourceId(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "SourceName") {
                String val = value;
                if (! obj.setSourceName(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "SourceType") {
                int val = Integer.parseInt(value);
                if (! obj.setSourceType(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "UpdateTime") {
                // if the time ends on a Z it is UTC, else localtime 
                Date val = new Date(); // starts since the epoch
                try { 
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    if ( value.charAt(value.length()-1) == 'Z' )                        df.setTimeZone(TimeZone.getTimeZone("UTC"));                    val = df.parse( value );
                } catch(Exception e) {
                    try { // if there are no miliseconds they will not be sent
                       DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                       if ( value.charAt(value.length()-1) == 'Z' )                          df.setTimeZone(TimeZone.getTimeZone("UTC"));                       val = df.parse( value );
                    } catch(Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (! obj.setUpdateTime(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "Construction") {
        Construction obj = new Construction();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "HullColor") {
                String val = value;
                if (! obj.setHullColor(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "HullType") {
                int val = Integer.parseInt(value);
                if (! obj.setHullType(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "DeadWeight") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setDeadWeight(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "GrossWeight") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setGrossWeight(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Length") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setLength(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "LloydsShipType") {
                int val = Integer.parseInt(value);
                if (! obj.setLloydsShipType(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "YearOfBuild") {
                int val = Integer.parseInt(value);
                if (! obj.setYearOfBuild(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "MaxAirDraught") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setMaxAirDraught(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "MaxDraught") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setMaxDraught(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "MaxPersonsOnBoard") {
                int val = Integer.parseInt(value);
                if (! obj.setMaxPersonsOnBoard(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "MaxSpeed") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setMaxSpeed(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Width") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setWidth(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "UnType") {
        UnType obj = new UnType();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "CodeA") {
                String val = value;
                if (! obj.setCodeA(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "CodeB") {
                String val = value;
                if (! obj.setCodeB(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Mode") {
                int val = Integer.parseInt(value);
                if (! obj.setMode(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "Identifier") {
        Identifier obj = new Identifier();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "Callsign") {
                String val = value;
                if (! obj.setCallsign(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "IMO") {
                int val = Integer.parseInt(value);
                if (! obj.setIMO(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Name") {
                String val = value;
                if (! obj.setName(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "FormerName") {
                String val = value;
                if (! obj.setFormerName(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Flag") {
                String val = value;
                if (! obj.setFlag(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Owner") {
                String val = value;
                if (! obj.setOwner(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "MMSI") {
                int val = Integer.parseInt(value);
                if (! obj.setMMSI(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "LRIT") {
                String val = value;
                if (! obj.setLRIT(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "VoyageData") {
        VoyageData obj = new VoyageData();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "AirDraught") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setAirDraught(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Id") {
                int val = Integer.parseInt(value);
                if (! obj.setId(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "CargoTypeIMO") {
                int val = Integer.parseInt(value);
                if (! obj.setCargoTypeIMO(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "ContactIdentity") {
                String val = value;
                if (! obj.setContactIdentity(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "DestCode") {
                String val = value;
                if (! obj.setDestCode(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "DestName") {
                String val = value;
                if (! obj.setDestName(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "DepartCode") {
                String val = value;
                if (! obj.setDepartCode(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "DepartName") {
                String val = value;
                if (! obj.setDepartName(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Draught") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setDraught(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "ETA") {
                // if the time ends on a Z it is UTC, else localtime 
                Date val = new Date(); // starts since the epoch
                try { 
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    if ( value.charAt(value.length()-1) == 'Z' )                        df.setTimeZone(TimeZone.getTimeZone("UTC"));                    val = df.parse( value );
                } catch(Exception e) {
                    try { // if there are no miliseconds they will not be sent
                       DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                       if ( value.charAt(value.length()-1) == 'Z' )                          df.setTimeZone(TimeZone.getTimeZone("UTC"));                       val = df.parse( value );
                    } catch(Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (! obj.setETA(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "ATD") {
                // if the time ends on a Z it is UTC, else localtime 
                Date val = new Date(); // starts since the epoch
                try { 
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    if ( value.charAt(value.length()-1) == 'Z' )                        df.setTimeZone(TimeZone.getTimeZone("UTC"));                    val = df.parse( value );
                } catch(Exception e) {
                    try { // if there are no miliseconds they will not be sent
                       DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                       if ( value.charAt(value.length()-1) == 'Z' )                          df.setTimeZone(TimeZone.getTimeZone("UTC"));                       val = df.parse( value );
                    } catch(Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (! obj.setATD(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "ISPSLevel") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setISPSLevel(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "OverSizedLength") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setOverSizedLength(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "OverSizedWidth") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setOverSizedWidth(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "PersonsOnBoard") {
                int val = Integer.parseInt(value);
                if (! obj.setPersonsOnBoard(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Pilots") {
                double val = Double.parseDouble(value.replace(",", "."));
                if (! obj.setPilots(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "RouteBound") {
                boolean val = (value.toUpperCase().equals("YES") ||
                               value.toUpperCase().equals("TRUE") ||
                               value.toUpperCase().equals("1"));
                if (! obj.setRouteBound(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "SourceId") {
                String val = value;
                if (! obj.setSourceId(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "SourceName") {
                String val = value;
                if (! obj.setSourceName(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "SourceType") {
                int val = Integer.parseInt(value);
                if (! obj.setSourceType(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "TankerStatus") {
                int val = Integer.parseInt(value);
                if (! obj.setTankerStatus(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Tugs") {
                boolean val = (value.toUpperCase().equals("YES") ||
                               value.toUpperCase().equals("TRUE") ||
                               value.toUpperCase().equals("1"));
                if (! obj.setTugs(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "UpdateTime") {
                // if the time ends on a Z it is UTC, else localtime 
                Date val = new Date(); // starts since the epoch
                try { 
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    if ( value.charAt(value.length()-1) == 'Z' )                        df.setTimeZone(TimeZone.getTimeZone("UTC"));                    val = df.parse( value );
                } catch(Exception e) {
                    try { // if there are no miliseconds they will not be sent
                       DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                       if ( value.charAt(value.length()-1) == 'Z' )                          df.setTimeZone(TimeZone.getTimeZone("UTC"));                       val = df.parse( value );
                    } catch(Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (! obj.setUpdateTime(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
    else if (qName == "Waypoint") {
        Waypoint obj = new Waypoint();
        for (int i=0; i < atts.getLength(); i++) {
            String key = atts.getLocalName(i);
            String value = atts.getValue(i);

            if (key == "ATA") {
                // if the time ends on a Z it is UTC, else localtime 
                Date val = new Date(); // starts since the epoch
                try { 
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    if ( value.charAt(value.length()-1) == 'Z' )                        df.setTimeZone(TimeZone.getTimeZone("UTC"));                    val = df.parse( value );
                } catch(Exception e) {
                    try { // if there are no miliseconds they will not be sent
                       DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                       if ( value.charAt(value.length()-1) == 'Z' )                          df.setTimeZone(TimeZone.getTimeZone("UTC"));                       val = df.parse( value );
                    } catch(Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (! obj.setATA(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "ETA") {
                // if the time ends on a Z it is UTC, else localtime 
                Date val = new Date(); // starts since the epoch
                try { 
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    if ( value.charAt(value.length()-1) == 'Z' )                        df.setTimeZone(TimeZone.getTimeZone("UTC"));                    val = df.parse( value );
                } catch(Exception e) {
                    try { // if there are no miliseconds they will not be sent
                       DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                       if ( value.charAt(value.length()-1) == 'Z' )                          df.setTimeZone(TimeZone.getTimeZone("UTC"));                       val = df.parse( value );
                    } catch(Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (! obj.setETA(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "RTA") {
                // if the time ends on a Z it is UTC, else localtime 
                Date val = new Date(); // starts since the epoch
                try { 
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    if ( value.charAt(value.length()-1) == 'Z' )                        df.setTimeZone(TimeZone.getTimeZone("UTC"));                    val = df.parse( value );
                } catch(Exception e) {
                    try { // if there are no miliseconds they will not be sent
                       DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                       if ( value.charAt(value.length()-1) == 'Z' )                          df.setTimeZone(TimeZone.getTimeZone("UTC"));                       val = df.parse( value );
                    } catch(Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (! obj.setRTA(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "LoCode") {
                String val = value;
                if (! obj.setLoCode(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
            else if (key == "Name") {
                String val = value;
                if (! obj.setName(val) ) {
                   throw new SAXException("Validation Exception: " + key + " = " + value );
                }
            }
        }
        m_objStack.push( obj );
    }
}

    public void endElement(String namespaceUri,
                           String localName,
                           String qName) throws SAXException {

    // check all possible options
    if (qName == "MSG_IVEF") {
    	m_ivef = (MSG_IVEF) ( m_objStack.pop() );
    	if (m_ivef != null)
    		m_ivefPresent = true;
        //MSG_IVEF obj = (MSG_IVEF) ( m_objStack.pop() );
        //if (m_handler != null) 
        //    m_handler.handleMSG_IVEF( obj ); 
    }
    else if (qName == "Body") {

        Body obj = (Body) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new MSG_IVEF().getClass() ) {
                if (! ((MSG_IVEF) ( m_objStack.peek() ) ).setBody( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "ObjectDatas") {

        ObjectDatas obj = (ObjectDatas) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new Body().getClass() ) {
                if (! ((Body) ( m_objStack.peek() ) ).setObjectDatas( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "Area") {

        Area obj = (Area) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new ServiceRequest().getClass() ) {
                if (! ((ServiceRequest) ( m_objStack.peek() ) ).addArea( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "OtherId") {

        OtherId obj = (OtherId) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new Identifier().getClass() ) {
                if (! ((Identifier) ( m_objStack.peek() ) ).addOtherId( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "OtherName") {

        OtherName obj = (OtherName) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new Identifier().getClass() ) {
                if (! ((Identifier) ( m_objStack.peek() ) ).addOtherName( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "Header") {

        Header obj = (Header) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new MSG_IVEF().getClass() ) {
                if (! ((MSG_IVEF) ( m_objStack.peek() ) ).setHeader( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "LoginRequest") {

        LoginRequest obj = (LoginRequest) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new Body().getClass() ) {
                if (! ((Body) ( m_objStack.peek() ) ).setLoginRequest( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "LoginResponse") {

        LoginResponse obj = (LoginResponse) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new Body().getClass() ) {
                if (! ((Body) ( m_objStack.peek() ) ).setLoginResponse( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "Logout") {

        Logout obj = (Logout) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new Body().getClass() ) {
                if (! ((Body) ( m_objStack.peek() ) ).setLogout( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "ObjectData") {

        ObjectData obj = (ObjectData) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new ObjectDatas().getClass() ) {
                if (! ((ObjectDatas) ( m_objStack.peek() ) ).addObjectData( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "Ping") {

        Ping obj = (Ping) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new Body().getClass() ) {
                if (! ((Body) ( m_objStack.peek() ) ).setPing( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "Pong") {

        Pong obj = (Pong) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new Body().getClass() ) {
                if (! ((Body) ( m_objStack.peek() ) ).setPong( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "Pos") {

        Pos obj = (Pos) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new Area().getClass() ) {
                if (! ((Area) ( m_objStack.peek() ) ).addPos( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
        if ( m_objStack.peek().getClass() == new TrackData().getClass() ) {
                if (! ((TrackData) ( m_objStack.peek() ) ).addPos( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
        if ( m_objStack.peek().getClass() == new Waypoint().getClass() ) {
        	if (! ((Waypoint) ( m_objStack.peek() ) ).setPos( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "ServerStatus") {

        ServerStatus obj = (ServerStatus) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new Body().getClass() ) {
                if (! ((Body) ( m_objStack.peek() ) ).setServerStatus( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "ServiceRequest") {

        ServiceRequest obj = (ServiceRequest) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new Body().getClass() ) {
                if (! ((Body) ( m_objStack.peek() ) ).setServiceRequest( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "Transmission") {

        Transmission obj = (Transmission) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new ServiceRequest().getClass() ) {
                if (! ((ServiceRequest) ( m_objStack.peek() ) ).setTransmission( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "Item") {

        Item obj = (Item) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new ServiceRequest().getClass() ) {
                if (! ((ServiceRequest) ( m_objStack.peek() ) ).addItem( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "Filter") {

        Filter obj = (Filter) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new ServiceRequest().getClass() ) {
                if (! ((ServiceRequest) ( m_objStack.peek() ) ).setFilter( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "ServiceRequestResponse") {

        ServiceRequestResponse obj = (ServiceRequestResponse) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new Body().getClass() ) {
                if (! ((Body) ( m_objStack.peek() ) ).setServiceRequestResponse( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "TaggedItem") {

        TaggedItem obj = (TaggedItem) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new ObjectData().getClass() ) {
                if (! ((ObjectData) ( m_objStack.peek() ) ).addTaggedItem( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "TrackData") {

        TrackData obj = (TrackData) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new ObjectData().getClass() ) {
                if (! ((ObjectData) ( m_objStack.peek() ) ).setTrackData( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "NavStatus") {

        NavStatus obj = (NavStatus) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new TrackData().getClass() ) {
                if (! ((TrackData) ( m_objStack.peek() ) ).addNavStatus( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "VesselData") {

        VesselData obj = (VesselData) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new ObjectData().getClass() ) {
                if (! ((ObjectData) ( m_objStack.peek() ) ).addVesselData( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "Construction") {

        Construction obj = (Construction) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new VesselData().getClass() ) {
                if (! ((VesselData) ( m_objStack.peek() ) ).setConstruction( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "UnType") {

        UnType obj = (UnType) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new Construction().getClass() ) {
                if (! ((Construction) ( m_objStack.peek() ) ).setUnType( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "Identifier") {

        Identifier obj = (Identifier) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new VesselData().getClass() ) {
                if (! ((VesselData) ( m_objStack.peek() ) ).setIdentifier( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "VoyageData") {

        VoyageData obj = (VoyageData) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new ObjectData().getClass() ) {
                if (! ((ObjectData) ( m_objStack.peek() ) ).addVoyageData( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
    else if (qName == "Waypoint") {

        Waypoint obj = (Waypoint) ( m_objStack.pop() );
        if ( m_objStack.peek().getClass() == new VoyageData().getClass() ) {
                if (! ((VoyageData) ( m_objStack.peek() ) ).addWaypoint( obj ) ) {
                   throw new SAXException("Validation Exception: " + qName);
                }
        }
    }
}

    public boolean parseXMLString(String data, boolean cont) { 
     
    	m_dataBuffer += data;

     // search the buffer for the nearest closetag
     int indexStart = 0, indexEnd = -1;

     // look for the pattern that defines a root element
     Matcher matcher = m_closeTagsPattern.matcher( m_dataBuffer );

     // parse the messages in the buffer one by one
     while ( matcher.find() ) {
         indexEnd = matcher.end();
         // isolate the messages
         String messages = m_dataBuffer.substring(indexStart, indexEnd);
         indexStart = indexEnd;
         // and parse
         try { 
             m_parser.parse(new InputSource(new StringReader(messages)), this);
         } catch(Exception e) {
             String errorMessage =
             "Error parsing " + messages + ": " + e;
             System.err.println(errorMessage);
             e.printStackTrace();
             //Mazen: Important to remove the illegal data from our buffer in somehow!
             m_dataBuffer = "";
             return false;
         }
     }

     // check if we parsed messages
     if (indexEnd > -1) {
         // remove from buffer
         m_dataBuffer = m_dataBuffer.substring(indexEnd);
     }

     // check if we should flush the buffer
     if (!cont) {
         m_dataBuffer = "";
     }
     return true;
}
    //Parsing result: MSG_IVEF
    public MSG_IVEF getIVEF(){
    	return m_ivef;
    }
    public boolean IVEFPresent(){
    	return m_ivefPresent;
    }
    public void resetIVEFPresent(){
    	m_ivefPresent = false;
    }

}
