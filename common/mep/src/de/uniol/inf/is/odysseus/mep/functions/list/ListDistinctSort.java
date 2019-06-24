package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 *  A map function used to remove all duplicates (using equals) and to sort all 
 *  remaining content by it default (java) sort order
 * 
 * @author MarcoGrawunder
 *
 */

@SuppressWarnings("rawtypes")
public class ListDistinctSort extends AbstractFunction<List> {

	private static final long serialVersionUID = 8024490462089827538L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.getLists() };

	public ListDistinctSort() {
		super("distinctsort", 1, ACC_TYPES, SDFDatatype.OBJECT, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getValue() {
		SortedSet l = new TreeSet( (List<?>) getInputValue(0));
		return new ArrayList(l);
	}

}
