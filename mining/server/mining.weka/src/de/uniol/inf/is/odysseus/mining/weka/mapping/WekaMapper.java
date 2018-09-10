package de.uniol.inf.is.odysseus.mining.weka.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

public final class WekaMapper<M extends ITimeInterval> {

	private SDFSchema schema;
	private ArrayList<Attribute> wekaSchema;
	private Map<SDFAttribute, List<String>> nominals;
	private int[] attributes;

	public WekaMapper(SDFSchema schema, int[] attributes) {
		this.schema = schema;
		this.wekaSchema = calcAttributeList(attributes, null);
		this.attributes = attributes;
	}

	public WekaMapper(SDFSchema schema, int[] attributes, Map<SDFAttribute, List<String>> nominals) {
		this.schema = schema;
		this.wekaSchema = calcAttributeList(attributes, nominals);
		this.attributes = attributes;
		this.nominals = nominals;
	}

	private ArrayList<Attribute> calcAttributeList(int[] attributes, Map<SDFAttribute, List<String>> nominals) {
		ArrayList<Attribute> al = new ArrayList<Attribute>();
		for (int i = 0; i < attributes.length; i++) {
			SDFAttribute sdfa = this.schema.getAttribute(attributes[i]);
			List<String> attributeValues = nominals.get(sdfa);
			if (attributeValues != null) {
				Attribute attr = new Attribute(sdfa.getAttributeName(), attributeValues);
				al.add(attr);
			} else {
				Attribute attr = new Attribute(sdfa.getAttributeName());
				al.add(attr);
			}

		}
		return al;
	}

	protected void removeFromSchema(SDFAttribute attribute) {
		int index = this.schema.indexOf(attribute);
		List<SDFAttribute> attributes = this.schema.getAttributes();
		if (index != -1) {
			attributes.remove(index);
			this.schema = SDFSchemaFactory.createNewWithAttributes(attributes, schema);
		}
	}

	public Instances convertToNumericInstances(List<Tuple<M>> tuples, int[] attributes) {
		Instances instances = new Instances(this.schema.getURI(), this.wekaSchema, 10);
		// wrap up elements
		for (Tuple<M> tuple : tuples) {
			Instance inst = convertToNumericInstance(tuple, attributes);
			instances.add(inst);
		}
		return instances;

	}

	public Instance convertToNumericInstance(Tuple<M> tuple, int[] attributes) {
		Instance inst = new DenseInstance(attributes.length);
		for (int i = 0; i < attributes.length; i++) {
			double val = ((Number) tuple.getAttribute(attributes[i])).doubleValue();
			inst.setValue(i, val);
		}
		return inst;
	}

	public Instance convertToNominalInstance(Tuple<M> tuple, int[] attributes) {
		Instance inst = new DenseInstance(attributes.length);
		for (int i = 0; i < attributes.length; i++) {
			Attribute a = this.wekaSchema.get(i);
			if (a.isNominal()) {
				String val = tuple.getAttribute(attributes[i]).toString();
				inst.setValue(a, val);
			} else {
				double val = ((Number) tuple.getAttribute(attributes[i])).doubleValue();
				inst.setValue(i, val);

			}
		}
		return inst;
	}

	public Instances convertToInstances(List<Tuple<M>> tuples, int[] attributes) {
		Instances instances = new Instances(this.schema.getURI(), this.wekaSchema, 10);
		// wrap up elements
		for (Tuple<M> tuple : tuples) {

			Instance inst = convertToNominalInstance(tuple, attributes);
			instances.add(inst);
		}
		return instances;

	}

	public SDFSchema getSchema() {
		return schema;
	}

	public void setSchema(SDFSchema schema) {
		this.schema = schema;
	}

	public ArrayList<Attribute> getWekaSchema() {
		return wekaSchema;
	}

	public void setWekaSchema(ArrayList<Attribute> wekaSchema) {
		this.wekaSchema = wekaSchema;
	}

	public Map<SDFAttribute, List<String>> getNominals() {
		return nominals;
	}

	public void setNominals(Map<SDFAttribute, List<String>> nominals) {
		this.nominals = nominals;
	}

	public int[] getAttributes() {
		return attributes;
	}

	public void setAttributes(int[] attributes) {
		this.attributes = attributes;
	}

}
