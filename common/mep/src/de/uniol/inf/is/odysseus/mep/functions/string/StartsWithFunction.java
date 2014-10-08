package de.uniol.inf.is.odysseus.mep.functions.string;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class StartsWithFunction extends AbstractBinaryStringFunction<Boolean> {
	
	private static final long serialVersionUID = 1074176952187932775L;

	public StartsWithFunction(){
		super("startsWith",SDFDatatype.BOOLEAN);
	}
	
		
	@Override
	public Boolean getValue() {
		
		String left = getInputValue(0);
		String right = getInputValue(1);
		
		if (left != null){
			return left.startsWith(right);
		}
		
		return false;
	}

}
