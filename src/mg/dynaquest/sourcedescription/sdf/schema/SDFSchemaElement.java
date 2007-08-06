package mg.dynaquest.sourcedescription.sdf.schema;

import java.util.ArrayList;
import java.util.List;

import mg.dynaquest.sourcedescription.sdf.SDFElement;
import mg.dynaquest.sourcedescription.sdf.unit.SDFUnit;

public abstract class SDFSchemaElement extends SDFElement {

    /**
	 * @uml.property  name="datatype"
	 * @uml.associationEnd  
	 */
    private SDFDatatype datatype = null;

    /**
	 * @uml.property  name="dtConstraint"
	 * @uml.associationEnd  
	 */
    private List<SDFDatatypeConstraint> dtConstraints = new ArrayList<SDFDatatypeConstraint>();

    /**
	 * @uml.property  name="unit"
	 * @uml.associationEnd  
	 */
    private SDFUnit unit = null;


	public SDFSchemaElement(String URI) {
		super(URI);
	}

    /**
     * 
     * @uml.property name="datatype"
     */
    public void setDatatype(SDFDatatype datatype) {
        this.datatype = datatype;
    }

    /**
     * 
     * @uml.property name="datatype"
     */
    public SDFDatatype getDatatype() {
        return datatype;
    }

    /**
     * 
     * @uml.property name="dtConstraint"
     */
    public void addDtConstraint(SDFDatatypeConstraint dtConstraint) {
        this.dtConstraints.add(dtConstraint);
    }

    /**
     * 
     * @uml.property name="dtConstraint"
     */
    public List<SDFDatatypeConstraint> getDtConstraints() {
        return dtConstraints;
    }

    /**
     * 
     * @uml.property name="unit"
     */
    public void setUnit(SDFUnit unit) {
        this.unit = unit;
    }

    /**
     * 
     * @uml.property name="unit"
     */
    public SDFUnit getUnit() {
        return unit;
    }

	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString());
//		if (getDatatype() != null)
//			ret.append(" : " + this.getDatatype().toString());
//		if (this.getDtConstraint() != null)
//			ret.append("/" + this.getDtConstraint().toString());
//		if (this.getUnit() != null)
//			ret.append(" --> " + this.getUnit().toString());
		return ret.toString();
	}

}