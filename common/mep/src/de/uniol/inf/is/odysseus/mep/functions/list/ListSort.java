package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.Collections;
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
public class ListSort extends AbstractFunction<List> {

	private static final long serialVersionUID = 8024490462089827538L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.getLists() };

	public ListSort() {
		super("sort", 1, ACC_TYPES, SDFDatatype.OBJECT, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getValue() {
		// Copy list, else input will be sorted!
		List l = new ArrayList( (List<?>) getInputValue(0));

		Collections.sort(l);

		return l;
	}

}
