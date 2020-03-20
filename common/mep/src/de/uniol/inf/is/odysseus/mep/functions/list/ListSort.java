package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
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
		super("sort", 1, ACC_TYPES, SDFDatatype.LIST, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getValue() {
		List inputList = (List)getInputValue(0);
		if (inputList.isEmpty()) {
			return new ArrayList((List<?>) getInputValue(0));
		}
		// Copy list, else input will be sorted!
		List l = new ArrayList( inputList);

		// Default behaviour
		if (inputList.get(0) instanceof Comparable) {
			Collections.sort(l);
		}else if (inputList.get(0) instanceof ArrayList) {
			// Hopefully, the ArrayList contains comparable objects
			// else there will be an exception
			Collections.sort(l, new Comparator<ArrayList<Comparable>>() {
				public int compare(ArrayList<Comparable> o1, ArrayList<Comparable>  o2) {
					int min = Math.min(o1.size(), o2.size());
					int compare = 0;
					for (int i=0;i<min;i++) {
						Comparable left = o1.get(i);
						Comparable right = o2.get(i);
						compare = left.compareTo(right);
					}
					return compare;
				};
			});
		}
		
		
		return l;
	}

	@Override
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		return args[0].getReturnType();
	}
	
	@Override
	public boolean determineTypeFromInput() {
		return true;
	}
}
