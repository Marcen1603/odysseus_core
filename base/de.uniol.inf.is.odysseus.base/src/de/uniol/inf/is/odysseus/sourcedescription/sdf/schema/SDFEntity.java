package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.ArrayList;

/**
 * @author  Marco Grawunder
 */
public class SDFEntity extends SDFSchemaElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5891495267668181672L;
	
    /**
	 * @uml.property  name="idAttributes"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFAttributeList  idAttributes = new SDFAttributeList();
    /**
	 * @uml.property  name="attributes"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFAttributeList attributes = new SDFAttributeList();
    /**
	 * @uml.property  name="constants"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFConstantList constants = new SDFConstantList();
    
    private ArrayList<SDFEntity> subClassOf = new ArrayList<SDFEntity>();

	// Evtl. noch weitere Daten??

	public SDFEntity(String URI) {
		super(URI);
	}

	public void addIdAttribute(SDFAttribute idAttribute) {
		idAttributes.add(idAttribute);
	}

	public SDFAttribute getIdAttribute(int index) {
		return (SDFAttribute) idAttributes.get(index);
	}

	public int getNoOfIdAttributes() {
		return idAttributes.size();
	}

	public void addAttribute(SDFAttribute attribute) {
		attributes.add(attribute);
	}

	public SDFAttribute getAttribute(int index) {
		return (SDFAttribute) attributes.get(index);
	}
	
	public SDFAttributeList getAttributes() {
		return this.attributes;
	}

	public int getNoOfAttributes() {
		return attributes.size();
	}

	public void addConstant(SDFConstant constant) {
		constants.add(constant);
	}

	public SDFConstant getConstant(int index) {
		return (SDFConstant) constants.get(index);
	}

	public int getNoOfConstants() {
		return constants.size();
	}

	public void addSuperclass(SDFEntity superclass) {
		subClassOf.add(superclass);
	}

	public SDFEntity getSuperclass(int index) {
		return (SDFEntity) subClassOf.get(index);
	}

	public int getNoOfSuperclasses() {
		return subClassOf.size();
	}
	
	public void setAttributes(SDFAttributeList attrs) {
		this.attributes = attrs;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + "\n");
		for (int i = 0; i < getNoOfSuperclasses(); i++) {
			// Hier nicht die To-String der Oberklasse aufrufen, da dann
			// alle Attribute, Konstanten etc. mit ausgedruckt werden
			ret.append(" isA " + getSuperclass(i).getURI(true) + "\n");
		}
		for (int i = 0; i < getNoOfIdAttributes(); i++) {
			ret.append("\t\t hasIdAttribute "
					+ this.getIdAttribute(i).toString() + "\n");
		}
		for (int i = 0; i < this.getNoOfAttributes(); i++) {
			ret.append("\t\t hasAttribute " + this.getAttribute(i).toString()
					+ "\n");
		}
		for (int i = 0; i < this.getNoOfConstants(); i++) {
			ret.append("\t\t hasConstant " + this.getConstant(i).toString()
					+ "\n");
		}
		return ret.toString();
	}

}