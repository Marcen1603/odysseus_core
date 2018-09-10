package de.uniol.inf.is.odysseus.temporaltypes.function;

import java.util.List;

public class AtMaxFunction extends AtMinMaxFunction {

	private static final long serialVersionUID = -176454713503390654L;

	public AtMaxFunction() {
		super("atMax");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean isMinMaxValue(List<Comparable> minValues, Comparable currentValue) {
		return minValues.isEmpty() || currentValue.compareTo(minValues.get(0)) > 0;
	}
}
