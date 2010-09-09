package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

public class ListParameter<T> extends AbstractParameter<List<T>> {

	private IParameter<T> singleParameter;

	public ListParameter(String name, REQUIREMENT requirement,
			IParameter<T> singleParameter) {
		super(name, requirement);
		this.singleParameter = singleParameter;
	}

	@Override
	protected void internalAssignment() {
		// TODO allgemein input parametertyp ueberpruefen
		try {
			ArrayList<T> list = new ArrayList<T>();
			for (Object o : (List<?>) inputValue) {
				singleParameter.setInputValue(o);
				if (!singleParameter.validate()) {
					throw new RuntimeException(singleParameter.getErrors().get(0));
				}
				list.add(singleParameter.getValue());
				setValue(list);
			}
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("wrong input for parameter "
					+ getName() + ", List expected, got "
					+ inputValue.getClass().getSimpleName());
		}

	}

	@Override
	public void setAttributeResolver(IAttributeResolver resolver) {
		super.setAttributeResolver(resolver);
		singleParameter.setAttributeResolver(resolver);
	}

}
