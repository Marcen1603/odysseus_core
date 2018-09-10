package de.uniol.inf.is.odysseus.sensormanagement.common.utilities;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

class MapMapElements
{
    @XmlAttribute
    public String key;

    @XmlJavaTypeAdapter(SimpleMapXmlAdapter.class)
    public Map<String, String> values;

    public MapMapElements() 
    {
    }

    public MapMapElements(String key, Map<String, String> value) 
    {
        this.key = key;
        this.values = value;
    }
}

public class StringMapMapXmlAdapter extends XmlAdapter<MapMapElements[], Map<String, Map<String, String>>>
{
	public StringMapMapXmlAdapter() {
	}

	@Override
	public MapMapElements[] marshal(Map<String, Map<String, String>> map) 
	{
		MapMapElements[] mapElements = new MapMapElements[map.size()];
		int i = 0;
		for (Map.Entry<String, Map<String, String>> entry : map.entrySet())
			mapElements[i++] = new MapMapElements(entry.getKey(), entry.getValue());

		return mapElements;
	}

	@Override
	public Map<String, Map<String, String>> unmarshal(MapMapElements[] mapElements) 
	{
		Map<String, Map<String, String>> r = new HashMap<>();
		for (MapMapElements element : mapElements)
			r.put(element.key, element.values);
		return r;
	}
}
