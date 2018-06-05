package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.expression;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.style.PersistentCondition;

public class IntegerCondition extends Condition<Integer>{

	public IntegerCondition(PersistentCondition condition) {
		super(condition);
		// TODO Auto-generated constructor stub
	}

	public IntegerCondition(Integer defaultValue) {
		super(defaultValue);
	}
	@Override
	protected Integer getValue(Object o) {
		if (o instanceof Number)
			return ((Number) o).intValue();
		return 0;
	}

	@Override
	protected Object getPersistentDefaultValue() {
		// TODO Auto-generated method stub
		return getDefault();
	}

}
