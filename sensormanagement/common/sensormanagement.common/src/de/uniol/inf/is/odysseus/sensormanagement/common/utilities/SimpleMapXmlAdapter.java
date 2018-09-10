package de.uniol.inf.is.odysseus.sensormanagement.common.utilities;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

class MapElements 
{
    @XmlAttribute
    public String key;
    @XmlAttribute
    public String value;

    public MapElements() 
    {
    }

    public MapElements(String key, String value) 
    {
        this.key = key;
        this.value = value;
    }
}

public class SimpleMapXmlAdapter extends XmlAdapter<MapElements[], Map<String, String>> 
{
    public SimpleMapXmlAdapter() {
    }

    @Override
	public MapElements[] marshal(Map<String, String> map) 
    {
        MapElements[] mapElements = new MapElements[map.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet())
            mapElements[i++] = new MapElements(entry.getKey(), entry.getValue());

        return mapElements;
    }

    @Override
	public Map<String, String> unmarshal(MapElements[] mapElements) 
    {
        Map<String, String> r = new HashMap<String, String>();
        for (MapElements element : mapElements)
            r.put(element.key, element.value);
        return r;
    }
}