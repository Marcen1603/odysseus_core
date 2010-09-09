package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class AggregateItemParameter extends AbstractParameter<AggregateItem> {

	public AggregateItemParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalAssignment() {
		List<String> value = (List<String>) inputValue;
		if (value.size() != 3) {
			throw new IllegalParameterException("illegal value for aggregation");
		}

		String funcStr = value.get(0);
		String attributeStr = value.get(1);
		SDFAttribute attribute = getAttributeResolver().getAttribute(
				attributeStr);
		String outputName = value.get(2);
		SDFAttribute outAttr = new SDFAttribute(outputName);
		outAttr.setDatatype(SDFDatatypeFactory.getDatatype("Double"));

		setValue(new AggregateItem(funcStr, attribute, outAttr));
	}

}
