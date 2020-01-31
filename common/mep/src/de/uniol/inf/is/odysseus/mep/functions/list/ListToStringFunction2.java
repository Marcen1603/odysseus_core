package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ListToStringFunction2 extends AbstractFunction<String> {

	private static final long serialVersionUID = 8703882849717127378L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.getLists(),
			{ SDFDatatype.STRING } };

	public ListToStringFunction2() {
		super("toString", 2, ACC_TYPES, SDFDatatype.STRING, false);
	}

	@Override
	public String getValue() {
		List<?> list = (List<?>) getInputValue(0);
		String sep = getInputValue(1);
		StringBuilder res = new StringBuilder();
		if (!list.isEmpty()) {
			res.append(list.get(0));
			for (int i = 1; i < list.size(); i++) {
				res.append(sep).append(list.get(i));
			}
		}
		return res.toString();
	}

}
