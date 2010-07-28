package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class CreateSDFAttributeParameter extends
		AbstractParameter<SDFAttribute> {

	public CreateSDFAttributeParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalAssignment() {
		List<String> list = (List<String>) inputValue;
		if (list.size() != 2) {
			throw new IllegalArgumentException(
					"Wrong number of inputs for SDFAttribute. Expecting id and datatype.");
		}
		SDFAttribute attribute = new SDFAttribute(list.get(0));
		attribute.setDatatype(SDFDatatypeFactory.getDatatype(list.get(1)));

		setValue(attribute);
	}
}
