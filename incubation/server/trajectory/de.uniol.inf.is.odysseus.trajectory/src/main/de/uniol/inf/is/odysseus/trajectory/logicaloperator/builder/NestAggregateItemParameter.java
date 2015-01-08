package de.uniol.inf.is.odysseus.trajectory.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;

public class NestAggregateItemParameter extends AbstractParameter<NestAggregateItem> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3197742185184362004L;

	public NestAggregateItemParameter() {
		super();
	}

	public NestAggregateItemParameter(String name, REQUIREMENT requirement, USAGE usage,
			String doc) {
		super(name, requirement, usage, doc);
	}

	public NestAggregateItemParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement, usage);
	}

	public NestAggregateItemParameter(String name, REQUIREMENT requirement) {
		super(name, requirement, USAGE.RECENT);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalAssignment() {
		final List<String> value = (List<String>) this.inputValue;
		if(value.size() != 2) {
			throw new IllegalParameterException("illegal value for aggregation");
		}
		this.setValue(
				new NestAggregateItem(
						getAttributeResolver().getAttribute(value.get(0)), 
						new SDFAttribute(null, value.get(1), getDataDictionary().getDatatype("double"), null, null, null)
				)
		);
	}

	@Override
	protected String getPQLStringInternal() {
		// TODO Auto-generated method stub
		return null;
	}

}
