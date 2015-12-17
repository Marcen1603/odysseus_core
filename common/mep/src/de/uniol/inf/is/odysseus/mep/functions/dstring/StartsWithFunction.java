package de.uniol.inf.is.odysseus.mep.functions.dstring;

import de.uniol.inf.is.odysseus.core.datatype.DString;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class StartsWithFunction extends AbstractBinaryDStringFunction<Boolean> {
	
	private static final long serialVersionUID = 1074176952187932775L;

	public StartsWithFunction(){
		super("startsWith",SDFDatatype.BOOLEAN);
	}
	
		
	@Override
	public Boolean getValue() {
		
		DString left = getInputValue(0);
		Object right = getInputValue(1);
		
		if (left != null){
			return left.startsWith(right.toString());
		}
		
		return false;
	}

}
