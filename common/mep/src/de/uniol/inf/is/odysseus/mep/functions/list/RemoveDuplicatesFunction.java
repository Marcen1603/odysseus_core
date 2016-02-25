package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class RemoveDuplicatesFunction extends AbstractFunction<List<Object>> {
	
	private static final long serialVersionUID = 1737236108042025309L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.LIST }};

	public RemoveDuplicatesFunction(){
		super("removeDuplicates", 1, accTypes, SDFDatatype.LIST, false);
	}
	
	@Override
	public List<Object> getValue() {
		// TODO: Find a faster solution?
		// LinkedHashSet to preserve order
		Set<Object> ob = new LinkedHashSet<>(getInputValue(0));
		return new ArrayList<>(ob);
	}
	
	
}
