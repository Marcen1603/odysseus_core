package mg.dynaquest.sourcedescription.sdf.mapping;

import mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate;
import mg.dynaquest.sourcedescription.sdf.schema.SDFDataschema;
import mg.dynaquest.sourcedescription.sdf.schema.SDFEntity;

public class SDFConditionalMapping extends SDFSchemaMapping {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5188343859024323802L;

	/**
	 * @uml.property  name="inEntity"
	 * @uml.associationEnd  
	 */
    SDFEntity inEntity = null;

    /**
	 * @uml.property  name="outEntity"
	 * @uml.associationEnd  
	 */
    SDFEntity outEntity = null;

    /**
	 * @uml.property  name="mappingPredicate"
	 * @uml.associationEnd  
	 */
    SDFPredicate mappingPredicate = null;


	protected SDFConditionalMapping(String URI, SDFDataschema localSchema,
			SDFDataschema globalSchema) {
		super(URI, localSchema, globalSchema);
	}

    /**
     * @param inEntity
     * 
     * @uml.property name="inEntity"
     */
    public void setInEntity(SDFEntity inEntity) {
        this.inEntity = inEntity;
    }

    /**
     * 
     * @uml.property name="inEntity"
     */
    public SDFEntity getInEntity() {
        return inEntity;
    }

    /**
     * 
     * @uml.property name="outEntity"
     */
    public void setOutEntity(SDFEntity outEntity) {
        this.outEntity = outEntity;
    }

    /**
     * 
     * @uml.property name="outEntity"
     */
    public SDFEntity getOutEntity() {
        return outEntity;
    }

    /**
     * 
     * @uml.property name="mappingPredicate"
     */
    public void setMappingPredicate(SDFPredicate mappingPredicate) {
        this.mappingPredicate = mappingPredicate;
    }

    /**
     * 
     * @uml.property name="mappingPredicate"
     */
    public SDFPredicate getMappingPredicate() {
        return mappingPredicate;
    }

}