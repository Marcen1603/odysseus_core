package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.Construction;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.Identifier;

/**
 * @author msalous
 *
 */
public class VesselData implements IIvefElement { 

    private Construction m_construction; // default value is uninitialized
    private boolean m_constructionPresent;
    private Identifier m_identifier; // default value is uninitialized
    private boolean m_identifierPresent;
    private int m_class; // default value is uninitialized
    private boolean m_classPresent;
    private boolean m_blackListed; // default value is uninitialized
    private boolean m_blackListedPresent;
    private int m_id; // default value is uninitialized
    private boolean m_idPresent;
    private String m_specialAttention; // default value is uninitialized
    private boolean m_specialAttentionPresent;
    private String m_sourceId; // default value is uninitialized
    private boolean m_sourceIdPresent;
    private String m_sourceName; // default value is uninitialized
    private boolean m_sourceNamePresent;
    private int m_sourceType; // default value is uninitialized
    private boolean m_sourceTypePresent;
    private Date m_updateTime; // default value is uninitialized
    private boolean m_updateTimePresent;

    public VesselData() {

        m_constructionPresent = false;
        m_identifierPresent = false;
        m_classPresent = false;
        m_blackListedPresent = false;
        m_idPresent = false;
        m_specialAttentionPresent = false;
        m_sourceIdPresent = false;
        m_sourceNamePresent = false;
        m_sourceTypePresent = false;
        m_updateTimePresent = false;
    }

    public VesselData(VesselData val) {

        m_construction = val.getConstruction();
        if (val != null) {
            m_constructionPresent = true;
        }
        m_identifier = val.getIdentifier();
        if (val != null) {
            m_identifierPresent = true;
        }
        m_class = val.getIVEFClass();
        if (val != null) {
            m_classPresent = true;
        }
        m_blackListed = val.getBlackListed();
        if (val != null) {
            m_blackListedPresent = true;
        }
        m_id = val.getId();
        if (val != null) {
            m_idPresent = true;
        }
        m_specialAttention = val.getSpecialAttention();
        if (val != null) {
            m_specialAttentionPresent = true;
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
        m_updateTime = val.getUpdateTime();
        if (val != null) {
            m_updateTimePresent = true;
        }
    }

    public boolean setConstruction(Construction val) {

        m_constructionPresent = true;
        m_construction = val;
        return true;
    }

    public Construction getConstruction() {

        return m_construction;
    }

    public boolean hasConstruction() {

        return m_constructionPresent;
    }

    public boolean setIdentifier(Identifier val) {

        m_identifierPresent = true;
        m_identifier = val;
        return true;
    }

    public Identifier getIdentifier() {

        return m_identifier;
    }

    public boolean hasIdentifier() {

        return m_identifierPresent;
    }

    public boolean setIVEFClass(int val) {

        if ( ( val != 0 ) &&
             ( val != 1 ) &&
             ( val != 2 ) )
            return false;
        m_classPresent = true;
        m_class = val;
        return true;
    }

    public int getIVEFClass() {

        return m_class;
    }

    public boolean hasIVEFClass() {

        return m_classPresent;
    }

    public boolean setBlackListed(boolean val) {

        m_blackListedPresent = true;
        m_blackListed = val;
        return true;
    }

    public boolean getBlackListed() {

        return m_blackListed;
    }

    public boolean hasBlackListed() {

        return m_blackListedPresent;
    }

    public boolean setId(int val) {

        m_idPresent = true;
        m_id = val;
        return true;
    }

    public int getId() {

        return m_id;
    }

    public boolean setSpecialAttention(String val) {

        if (val.length() > 20)
          return false;
        m_specialAttentionPresent = true;
        m_specialAttention = val;
        return true;
    }

    public String getSpecialAttention() {

        return m_specialAttention;
    }

    public boolean hasSpecialAttention() {

        return m_specialAttentionPresent;
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

    public boolean setUpdateTime(Date val) {

        m_updateTimePresent = true;
        m_updateTime = val;
        return true;
    }

    public Date getUpdateTime() {

        return m_updateTime;
    }

    public String toXML() {

        String xml = "<VesselData";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasIVEFClass() ) {
            xml += " Class=\"" + m_class + "\"";
        }
        if ( hasBlackListed() ) {
            xml += " BlackListed=\"" + m_blackListed + "\"";
        }
        if ( m_idPresent ) {
            xml += " Id=\"" + m_id + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        if ( hasSpecialAttention() ) {
            xml += " SpecialAttention=\"" + encode( m_specialAttention) + "\"";
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
        if ( m_updateTimePresent ) {
            xml += " UpdateTime=\"" + df.format(m_updateTime) + "\"";
        } else { 
            return null; // not all required attributes have been set 
        } 
        String dataMember;
        xml += ">\n";
        if ( hasConstruction() ) {
            dataMember =  m_construction.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        if ( hasIdentifier() ) {
            dataMember =  m_identifier.toXML() ;
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        xml += "</VesselData>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "VesselData\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        if ( hasIVEFClass() ) {
            str +=  lead + "    Class = " + m_class + "\n";
        }
        if ( hasBlackListed() ) {
            str +=  lead + "    BlackListed = " + m_blackListed + "\n";
        }
            str +=  lead + "    Id = " + m_id + "\n";
        if ( hasSpecialAttention() ) {
            str +=  lead + "    SpecialAttention = " + m_specialAttention + "\n";
        }
        if ( hasSourceId() ) {
            str +=  lead + "    SourceId = " + m_sourceId + "\n";
        }
            str +=  lead + "    SourceName = " + m_sourceName + "\n";
            str +=  lead + "    SourceType = " + m_sourceType + "\n";
            str +=  lead + "    UpdateTime = " + df.format(m_updateTime) + "\n";
        if ( hasConstruction() ) {
            str +=  m_construction.toString(lead + "    ") ;
        }
        if ( hasIdentifier() ) {
            str +=  m_identifier.toString(lead + "    ") ;
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
		if(m_constructionPresent)
			m_construction.fillMap(map);
	    if(m_identifierPresent)
	    	m_identifier.fillMap(map);
		if(m_classPresent)
			map.addAttributeValue("vesselData_class", m_class);
		if(m_blackListedPresent)
			map.addAttributeValue("vesselData_blackListed", m_blackListed);
		if(m_idPresent)
			map.addAttributeValue("vesselData_id", m_id);
		if(m_specialAttentionPresent)
			map.addAttributeValue("vesselData_specialAttention", m_specialAttention);
		if(m_sourceIdPresent)
			map.addAttributeValue("vesselData_sourceId", m_sourceId);
		if(m_sourceNamePresent)
			map.addAttributeValue("vesselData_sourceName", m_sourceName);
		if(m_sourceTypePresent)
			map.addAttributeValue("vesselData_sourceType", m_sourceType);
		if(m_updateTimePresent)
			map.addAttributeValue("vesselData_updateTime", m_updateTime);
	}
}
