package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * A map function used to sort all containing content by it default (java) sort order
 * 
 * @author MarcoGrawunder
 *
 */

@SuppressWarnings("rawtypes")
public class ListDistinct extends AbstractFunction<List> {

	private static final long serialVersionUID = 8024490462089827538L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.getLists() };

	public ListDistinct() {
		super("distinct", 1, ACC_TYPES, SDFDatatype.OBJECT, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getValue() {
		HashSet l = new LinkedHashSet( (List<?>) getInputValue(0));
		return new ArrayList(l);
	}

}
