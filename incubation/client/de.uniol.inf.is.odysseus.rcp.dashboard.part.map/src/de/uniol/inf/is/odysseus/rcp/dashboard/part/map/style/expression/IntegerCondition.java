package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.expression;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.style.PersistentCondition;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.expression.Condition;

public class IntegerCondition extends Condition<Integer>{

	public IntegerCondition(PersistentCondition condition) {
		super(condition);
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
		return getDefault();
	}

}