package de.uniol.inf.is.odysseus.iql.basic.types.extension;

import java.util.Collection;

import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CollectionExtensions implements IIQLTypeExtensions {
	
	public static <T extends Collection> T plus(T col, Object operand) {
		if (operand instanceof Collection) {
			col.addAll((Collection)operand);
		} else {
			col.add(operand);
		}
		return col;
	}

	
	public static <T extends Collection> T  minus(T col, Object operand) {
		if (operand instanceof Collection) {
			col.removeAll((Collection)operand);
		} else {
			col.remove(operand);
		}
		return col;
	}
		
	@Override
	public Class<?> getType() {
		return Collection.class;
	}
}
