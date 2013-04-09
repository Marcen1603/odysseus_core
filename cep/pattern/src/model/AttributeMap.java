package model;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class AttributeMap {
	private SDFSchema schema;
	private int attrPos;
	
	public AttributeMap(SDFSchema schema, int attrPos) {
		this.setSchema(schema);
		this.setAttrPos(attrPos);				
	}

	public SDFSchema getSchema() {
		return schema;
	}

	public void setSchema(SDFSchema schema) {
		this.schema = schema;
	}

	public int getAttrPos() {
		return attrPos;
	}

	public void setAttrPos(int attrPos) {
		this.attrPos = attrPos;
	}
}
