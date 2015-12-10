package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class LayerTypeRegistry
{
	
	private static Map<String, ILayer> layertypes = new TreeMap<String, ILayer>();
	
	public static void register(ILayer layer) {
		for(String datatype : layer.getSupportedDatatypes()){
			layertypes.put(datatype, layer);
		}
	}
	
	public static void unregister(ILayer layer) {
		for(String datatype : layer.getSupportedDatatypes()){
			layertypes.remove(datatype);
		}
	}

	public static ILayer getLayer(SDFDatatype datatype){
		try {
			if (layertypes.containsKey(datatype.getURI()))
				return layertypes.get(datatype.getURI()).getClass().newInstance();
			return null;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Collection<ILayer> getTypes() {
		return layertypes.values();
	}
}
