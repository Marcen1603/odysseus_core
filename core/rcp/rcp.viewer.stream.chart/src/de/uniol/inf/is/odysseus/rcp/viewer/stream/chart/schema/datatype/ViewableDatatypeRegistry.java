package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class ViewableDatatypeRegistry {

	private static ViewableDatatypeRegistry instance = null;
	
	private Map<SDFDatatype, IViewableDatatype<?>> types = new HashMap<SDFDatatype, IViewableDatatype<?>>();
	
	private ViewableDatatypeRegistry(){
		
	}
	
	public static synchronized ViewableDatatypeRegistry getInstance(){
		if(instance==null){
			instance = new ViewableDatatypeRegistry();
		}
		return instance;
	}
	
	
	public void register(IViewableDatatype<?>  datatype){
		for(SDFDatatype type : datatype.getProvidedSDFDatatypes()){
			if(this.types.containsKey(type)){
				System.out.println("WARN: double entry for "+type+" in ViewableDatatypeRegistry! Value overwritten!");
			}
			this.types.put(type, datatype);
		}
	}
	
	public void unregister(IViewableDatatype<?> datatype){
		this.types.remove(datatype);
	}
	
	public Collection<IViewableDatatype<?>> getRegisteredTypes(){
		return this.types.values();
	}

	public IViewableDatatype<?> getConverter(SDFDatatype sdfDatatype) {
		return this.types.get(sdfDatatype);		
	}
	
	
	public boolean isAllowedDataType(SDFDatatype datatype) {
		return this.types.containsKey(datatype);				
	} 
}
