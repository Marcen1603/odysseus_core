package de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.ivef.element.version_1_0_4.ObjectData;

/**
 * @author msalous
 *
 */
public class ObjectDatas implements IIvefElement { 

    private Vector<ObjectData> m_objectDatas = new Vector<ObjectData>();

    public ObjectDatas() {

    }

    public ObjectDatas(ObjectDatas val) {

        for(int i=0; i < val.countOfObjectDatas(); i++ ) {
            m_objectDatas.add( val.getObjectDataAt(i) );
        }
    }

    public boolean removeObjectData(ObjectData val) {

        m_objectDatas.remove(val);
        return true;
    }

    public boolean addObjectData(ObjectData val) {

        m_objectDatas.add(val);
        return true;
    }

    public ObjectData getObjectDataAt(int i) {

        return m_objectDatas.get(i);
    }

    public int countOfObjectDatas() {

        return m_objectDatas.size();
    }
    @Override
    public String toXML() {

        String xml = "<ObjectDatas";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        String dataMember;
        xml += ">\n";
        for(int i=0; i < m_objectDatas.size(); i++ ) {
           ObjectData attribute = m_objectDatas.get(i);
            dataMember = attribute.toXML();
            if (dataMember != null) {
               xml += dataMember;
            } else {
               return null; // not all required data members have been set 
            } 
        } 
        xml += "</ObjectDatas>\n";
        return xml;
    }

    public String toString(String lead) {

        String str = lead + "ObjectDatas\n";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DecimalFormat nf = new DecimalFormat();
        nf.setMinimumFractionDigits(6);
        nf.setGroupingSize(0);

        for(int i=0; i < m_objectDatas.size(); i++ ) {
           ObjectData attribute = m_objectDatas.get(i);
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
		for(ObjectData obj : m_objectDatas)
			obj.fillMap(map);
	}


}
