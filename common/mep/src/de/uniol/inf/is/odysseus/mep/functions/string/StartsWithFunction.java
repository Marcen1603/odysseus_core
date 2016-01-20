package de.uniol.inf.is.odysseus.mep.functions.string;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class StartsWithFunction extends AbstractBinaryStringFunction<Boolean> {
	
	private static final long serialVersionUID = 1074176952187932775L;

	public StartsWithFunction(){
		super("startsWith",SDFDatatype.BOOLEAN);
	}
	
		
	@Override
	public Boolean getValue() {
		String a = getInputValue(0);
		String b = getInputValue(1);
		if ((a == null) || (b == null)) {
			return null;
		}
		return Boolean.valueOf(a.startsWith(b));
	}

}
