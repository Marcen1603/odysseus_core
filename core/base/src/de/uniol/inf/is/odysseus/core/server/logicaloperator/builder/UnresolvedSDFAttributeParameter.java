package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class UnresolvedSDFAttributeParameter extends AbstractParameter<SDFAttribute>{

	private static final long serialVersionUID = -2623322425009781058L;

	@Override
	protected void internalAssignment() {
		try {
			SDFSchema schema = getAttributeResolver().getSchema();
			String uri = schema.getQualName();
			SDFAttribute attribute = new SDFAttribute(uri, (String) this.inputValue, new SDFDatatype("String"));
			setValue(attribute);
		} catch (Exception ex) {
			throw new RuntimeException("cannot assign attribute value", ex);
		}
	}

	@Override
	protected String getPQLStringInternal() {
		if( !Strings.isNullOrEmpty(getValue().getSourceName())) {
			return "'" + getValue().getSourceName() + "." + getValue().getAttributeName() + "'";
		} 
		return "'" + getValue().getAttributeName() + "'";
	}

}
