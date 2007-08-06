package mg.dynaquest.sourcedescription.sdf.mapping;


import mg.dynaquest.sourcedescription.sdf.SDFElement;
import mg.dynaquest.sourcedescription.sdf.function.SDFFunction;
import mg.dynaquest.sourcedescription.sdf.schema.SDFDataschema;
import mg.dynaquest.sourcedescription.sdf.schema.SDFSchemaElement;
import mg.dynaquest.sourcedescription.sdf.schema.SDFSchemaElementSet;

public abstract class SDFSchemaMapping extends SDFElement {

    /**
	 * @uml.property  name="localSchema"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFDataschema localSchema = null;

    /**
	 * @uml.property  name="globalSchema"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFDataschema globalSchema = null;

    /**
	 * @uml.property  name="inElements"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFSchemaElementSet inElements = new SDFSchemaElementSet();

    /**
	 * @uml.property  name="outElements"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFSchemaElementSet outElements = new SDFSchemaElementSet();

    /**
	 * @uml.property  name="mappingFunction"
	 * @uml.associationEnd  
	 */
    protected SDFFunction mappingFunction = null;


	protected SDFSchemaMapping(String URI, SDFDataschema localSchema,
			SDFDataschema globalSchema) {
		super(URI);
		this.localSchema = localSchema;
		this.globalSchema = globalSchema;
	}

	public void addInSchemaElement(SDFSchemaElement inSchemaElement) {
		this.inElements.add(inSchemaElement);
	}

	public SDFSchemaElement getInAttribute(int index) {
		return this.inElements.get(index);
	}

	public int getInSchemaElementCount() {
		return this.inElements.size();
	}

	public void addOutSchemaElement(SDFSchemaElement outSchemaElement) {
		this.outElements.add(outSchemaElement);
	}

	public SDFSchemaElement getOutSchemaElement(int index) {
		return this.outElements.get(index);
	}

	public int getOutSchemaElementCount() {
		return this.outElements.size();
	}

	public boolean isInputElement(SDFSchemaElement element) {
		if (inElements.contains(element)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isOutputElement(SDFSchemaElement element) {
		if (outElements.contains(element)) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		StringBuffer ret = new StringBuffer("SchemaMapping " + super.toString()
				+ "\n");

		// DAS HIER KANN RAUS, SOBALD ALLE MAPPINGS FUNKTIONIEREN!!
		if (this.localSchema == null) {
			//System.out.println("LOKALES SCHEMA NULL " + super.toString());
		} else {
			ret.append(this.localSchema.getURI(true));
		}
		ret.append(" --> ");
		if (this.globalSchema == null) {
			//System.out.println("GLOBALES SCHEMA NULL " + super.toString());
		} else {
			ret.append(this.globalSchema.getURI(true) + "\n");
		}
		if (this.mappingFunction != null)
			ret.append("MapFunc: " + this.mappingFunction.toString() + "\n");
		ret.append("In:" + this.inElements.toString() + "\n");
		ret.append("Out:" + this.outElements.toString());
		return ret.toString();
	}

    /**
     * 
     * @uml.property name="mappingFunction"
     */
    public void setMappingFunction(SDFFunction mappingFunction) {
        this.mappingFunction = mappingFunction;
    }

    /**
     * 
     * @uml.property name="mappingFunction"
     */
    public SDFFunction getMappingFunction() {
        return mappingFunction;
    }

    /**
     * 
     * @uml.property name="localSchema"
     */
    public SDFDataschema getLocalSchema() {
        return localSchema;
    }

    /**
     * 
     * @uml.property name="globalSchema"
     */
    public SDFDataschema getGlobalSchema() {
        return globalSchema;
    }

    /**
     * 
     * @uml.property name="inElements"
     */
    public SDFSchemaElementSet getInElements() {
        return inElements;
    }

    /**
     * 
     * @uml.property name="outElements"
     */
    public SDFSchemaElementSet getOutElements() {
        return outElements;
    }

}