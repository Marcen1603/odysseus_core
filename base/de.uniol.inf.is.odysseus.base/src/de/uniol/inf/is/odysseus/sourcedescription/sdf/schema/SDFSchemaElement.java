package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.unit.SDFUnit;

public abstract class SDFSchemaElement extends SDFElement {

	private static final long serialVersionUID = -6044873167369148445L;
	private SDFDatatype datatype = null;
    private Map<String, SDFDatatypeConstraint> dtConstraints = new HashMap<String, SDFDatatypeConstraint>();
    private SDFUnit unit = null;

	public SDFSchemaElement(String URI) {
		super(URI);
	}
	
	public SDFSchemaElement(SDFSchemaElement copy) {
		super(copy);
		this.datatype = copy.datatype;
		this.dtConstraints = copy.dtConstraints;
		this.unit = copy.unit;
	}

    public void setDatatype(SDFDatatype datatype) {
        this.datatype = datatype;
    }
    public SDFDatatype getDatatype() {
        return datatype;
    }
    public void addDtConstraint(String uri, SDFDatatypeConstraint dtConstraint) {
        this.dtConstraints.put(uri, dtConstraint);
    }
    public SDFDatatypeConstraint getDtConstraint(String uri) {
        return dtConstraints.get(uri);
    }
    public Collection<SDFDatatypeConstraint> getDtConstraints() {
        return dtConstraints.values();
    }
    public void setUnit(SDFUnit unit) {
        this.unit = unit;
    }
    public SDFUnit getUnit() {
        return unit;
    }

	@Override
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