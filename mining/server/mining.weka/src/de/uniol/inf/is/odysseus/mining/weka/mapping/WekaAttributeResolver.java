package de.uniol.inf.is.odysseus.mining.weka.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import weka.core.Attribute;

/**
 * This class is a 1:1 mapping from weka schema to sdf schema
 * @author DGeesen
 */
public class WekaAttributeResolver {

	private ArrayList<Attribute> wekaSchema;
	private SDFSchema schema;
	private Map<SDFAttribute, List<String>> nominals = new HashMap<SDFAttribute, List<String>>();

	public WekaAttributeResolver(SDFSchema schema) {
		this.schema = schema;
		initWekaSchema();
	}
	
	public WekaAttributeResolver(SDFSchema schema, Map<SDFAttribute, List<String>> nominals) {
		this.schema = schema;
		if(nominals!=null){
			this.nominals = nominals;
		}
		initWekaSchema();
	}

	private void initWekaSchema() {
		ArrayList<Attribute> al = new ArrayList<Attribute>();
		for (SDFAttribute sdfattribute : this.schema) {
			List<String> attributeValues = nominals.get(sdfattribute);
			if (attributeValues != null) {
				Attribute attr = new Attribute(sdfattribute.getAttributeName(), attributeValues);
				al.add(attr);
			} else {
				if(sdfattribute.getDatatype().isString()){
					List<String> dummy = null;
					Attribute attr = new Attribute(sdfattribute.getAttributeName(), dummy);
					al.add(attr);
				}else{
					Attribute attr = new Attribute(sdfattribute.getAttributeName());
					al.add(attr);
				}
			}

		}
		this.wekaSchema = al;
	}
	
	
	public Attribute convertSDFAttribute(SDFAttribute attribute){
		int index = this.schema.indexOf(attribute);
		return this.wekaSchema.get(index);
	}
	
	public SDFAttribute convertAttribute(Attribute attribute){
		int index = this.wekaSchema.indexOf(attribute);
		return this.schema.get(index);
	}

	public ArrayList<Attribute> getWekaSchema() {
		return wekaSchema;
	}

	public void setWekaSchema(ArrayList<Attribute> wekaSchema) {
		this.wekaSchema = wekaSchema;
	}

	public SDFSchema getSchema() {
		return schema;
	}

	public void setSchema(SDFSchema schema) {
		this.schema = schema;
	}

	public Map<SDFAttribute, List<String>> getNominals() {
		return nominals;
	}

	public void setNominals(Map<SDFAttribute, List<String>> nominals) {
		this.nominals = nominals;
	}

}
