package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Adds a value to an existing list
 * 
 * @author Tobias Brandt
 *
 */
public class AddToListFunction extends AbstractFunction<List<?>> {

	private static final long serialVersionUID = -7979878191918739481L;

	public AddToListFunction() {
		super("addTo", 2, null, SDFDatatype.LIST);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<?> getValue() {
		Object value = getInputValue(0);
		List list = (List) getInputValue(1);
		list.add(value);
		return list;
	}
}
