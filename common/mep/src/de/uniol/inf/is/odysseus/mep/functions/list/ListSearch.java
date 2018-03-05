package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

@SuppressWarnings("rawtypes")
public class ListSearch extends AbstractFunction<Object> {

	private static final long serialVersionUID = -4546365628234499380L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.getLists(), SDFDatatype.SIMPLE_TYPES,
			{ SDFDatatype.BOOLEAN } };

	public ListSearch() {
		super("search", 3, ACC_TYPES, SDFDatatype.OBJECT, false);
	}

	@Override
	public Object getValue() {
		List<?> l = (List<?>) getInputValue(0);

		Comparable toFind = (Comparable) getInputValue(1);
		Boolean useNext = (Boolean) getInputValue(2);
		// Let java throw exception?
		//		if (! (o instanceof Comparable)){
//			throw new RuntimeException("Value is not comparable!");
//		}
		Comparable<?> last = null;
		Iterator iter = l.iterator();
		Comparable elem = null;
		while(iter.hasNext()){
			elem = (Comparable)iter.next();
			@SuppressWarnings("unchecked")
			int compare = elem.compareTo(toFind);
			// current element is smaller
			if (compare < 0){
				last = (Comparable)elem;
		    // element found
			}else if (compare == 0){
				return elem;
			// element is higher
			}else{
				return send(useNext, last, elem);
			}
		}

		// case: element not found, and useNext = true --> return null
		if (useNext == true){
			return null;
		}

		return send(useNext, last, elem);
	}

	private Object send(Boolean useNext, Comparable<?> last, Comparable elem) {
		if (useNext){
			return elem;
		}else{
			if (last != null){
				return last;
			}else{
				return elem;
			}
		}
	}

}
