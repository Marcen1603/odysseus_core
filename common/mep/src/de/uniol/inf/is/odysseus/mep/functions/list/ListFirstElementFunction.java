package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class ListFirstElementFunction extends AbstractListPosElementFunction {

	private static final long serialVersionUID = -3282877303737235603L;

	public ListFirstElementFunction() {
		super("first");
	}
	
	@Override
	protected int getPos(List<Object> l) {
		return 0;
	}
		

}
