package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

@SuppressWarnings("rawtypes")
public class ListMinMaxElementFunction2 extends AbstractFunction<List<Comparable>> {

	private static final long serialVersionUID = 758116340731496107L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		{ SDFDatatype.LIST }};

	
	public ListMinMaxElementFunction2(){
		super("minmax_",1, accTypes, SDFDatatype.LIST, false);
	}
	
	@Override
	@SuppressWarnings({ "unchecked"})
	public List<Comparable> getValue() {
		List<Comparable> list = getInputValue(0);
		Comparable min = list.get(0);
		Comparable max = list.get(0);
		
		for (Comparable t:list){
			if (t.compareTo(min) < 0){
				min = t;
			}
			if (t.compareTo(max) > 0) {
				max = t;
			}
		}
		List<Comparable> minMax = new ArrayList<Comparable>();
		minMax.add(min);
		minMax.add(max);
		return minMax;
	}
	
}