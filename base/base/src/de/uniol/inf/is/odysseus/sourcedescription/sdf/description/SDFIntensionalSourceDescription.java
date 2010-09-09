package de.uniol.inf.is.odysseus.sourcedescription.sdf.description;

import java.util.ArrayList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.mapping.SDFSchemaMapping;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDataschema;

public class SDFIntensionalSourceDescription extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4499281515289430928L;

	/**
	 * @uml.property  name="localSchema"
	 * @uml.associationEnd  
	 */
    SDFDataschema localSchema = null;

    /**
	 * @uml.property  name="schemaMapping"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.uniol.inf.is.odysseus.sourcedescription.sdf.mapping.SDFSchemaMapping"
	 */
    // ArrayList qualities = new ArrayList();
    ArrayList<SDFSchemaMapping> schemaMapping = new ArrayList<SDFSchemaMapping>();

    /**
	 * @uml.property  name="accessPatterns"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFAccessPattern"
	 */
    // Zugriffsmuster
    ArrayList<SDFAccessPattern> accessPatterns = new ArrayList<SDFAccessPattern>();


	public SDFIntensionalSourceDescription(String URI) {
		super(URI);
	}

    /**
     * 
     * @uml.property name="localSchema"
     */
    public void setLocalSchema(SDFDataschema localSchema) {
        this.localSchema = localSchema;
        //System.out.println("Lokales Schema gesetzt:
        // "+localSchema.toString());
    }

	public void addSchemaMapping(SDFSchemaMapping mapping) {
		this.schemaMapping.add(mapping);
		//System.out.println("Neues Schemamapping hinzugefuegt:
		// "+mapping.toString());
	}

	public SDFSchemaMapping getSchemaMapping(int index) {
		return (SDFSchemaMapping) this.schemaMapping.get(index);
	}

	public int getSchemaMappingCount() {
		return this.schemaMapping.size();
	}

	//public void addQualityAttribute(SDFQuality qualAttrib){
	// this.qualities.add(qualAttrib);
	//  System.out.println("Neues Qualitaetsattribut hinzugefuegt:
	// "+qualAttrib.toString());
	// }

	public void addAccessPattern(SDFAccessPattern accessPattern) {
		this.accessPatterns.add(accessPattern);
	}

	public int getAccessPatternCount() {
		return this.accessPatterns.size();
	}

	public SDFAccessPattern getAccessPattern(int index) {
		return (SDFAccessPattern) this.accessPatterns.get(index);
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + "\n");
		ret.append("lokales Schema " + this.localSchema.toString() + "\n");
		for (int i = 0; i < getSchemaMappingCount(); i++) {
			ret.append("Mapping : " + getSchemaMapping(i).toString() + "\n");
		}
		for (int i = 0; i < getAccessPatternCount(); i++) {
			ret.append("Access Pattern: " + getAccessPattern(i).toString());
		}
		return ret.toString();
	}

	public static void main(String[] args) {

//		SDFIntensionalSourceDescription isd = new SDFIntensionalSourceDescription("Egal");

	}

}