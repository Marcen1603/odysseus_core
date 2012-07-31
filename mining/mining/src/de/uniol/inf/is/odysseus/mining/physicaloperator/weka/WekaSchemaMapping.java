package de.uniol.inf.is.odysseus.mining.physicaloperator.weka;

import java.util.ArrayList;
import java.util.List;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;

public class WekaSchemaMapping<M extends ITimeInterval> {

	private List<AttributeMapping> attributeMappings = new ArrayList<AttributeMapping>();
	private ArrayList<Attribute> wekaSchema = new ArrayList<Attribute>();
	private SDFSchema schema;

	
	public WekaSchemaMapping(SDFSchema schema){
		this.schema = schema;
		createWekaSchema(schema);
	}

	private void createWekaSchema(SDFSchema schema) {
		this.wekaSchema = new ArrayList<Attribute>();
		for(int i=0;i<schema.size();i++){
			SDFAttribute attribute = schema.get(i);
			// currently everything is a string			
			Attribute a = new Attribute(attribute.getAttributeName(), (List<String>) null, i);				
			addMapping(a, attribute);			
			wekaSchema.add(a);
		}		
	}
	
	public ArrayList<Attribute> getWekaSchema(){
		return this.wekaSchema;
	}

	
	public SDFAttribute getSDFAttribute(Attribute weka){
		for(AttributeMapping am : this.attributeMappings){
			if(am.getWekaAttribute().equals(weka)){
				return am.getSDFAttribute();
			}				
		}
		return null;
	}
	
	
	public Instance convertTupleToInstance(Tuple<M> tuple){		
		Instance ins = new DenseInstance(this.attributeMappings.size());
		for(AttributeMapping am : this.attributeMappings){
			SDFAttribute sdf = am.getSDFAttribute();
			Attribute weka = am.getWekaAttribute();
			Object value = tuple.getAttribute(schema.indexOf(sdf));
			if(sdf.getDatatype().isNumeric()){
				Number num = (Number) value;
				ins.setValue(weka, num.doubleValue());
			}else{				
				ins.setValue(weka, value.toString());
			}			
		}		
		return ins;
	}
	
	public Attribute getWekaAttribute(SDFAttribute sdf){
		for(AttributeMapping am : this.attributeMappings){
			if(am.getSDFAttribute().equals(sdf)){
				return am.getWekaAttribute();
			}
		}
		return null;
	}
	
	public void addMapping(Attribute weka, SDFAttribute sdf){
		AttributeMapping am = new AttributeMapping(weka, sdf);
		this.attributeMappings.add(am);
	}
	
	private class AttributeMapping {
		private Attribute wekaAttribute;
		private SDFAttribute sdfAttribute;

		public AttributeMapping(Attribute weka, SDFAttribute sdf) {
			this.wekaAttribute = weka;
			this.sdfAttribute = sdf;
		}

		public SDFAttribute getSDFAttribute() {
			return this.sdfAttribute;
		}

		public Attribute getWekaAttribute() {
			return this.wekaAttribute;
		}
	}

}
