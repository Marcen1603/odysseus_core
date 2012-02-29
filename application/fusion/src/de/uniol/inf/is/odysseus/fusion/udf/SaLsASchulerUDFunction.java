package de.uniol.inf.is.odysseus.fusion.udf;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

@UserDefinedFunction(name="SchulerBG")
public class SaLsASchulerUDFunction implements IUserDefinedFunction<RelationalTuple<? extends IMetaAttribute>, RelationalTuple<? extends IMetaAttribute>> {

	String init = null;
	
	@Override
	public void init(String initString) {
		init = initString;
	}

	@Override
	public RelationalTuple<? extends IMetaAttribute> process(RelationalTuple<? extends IMetaAttribute> in, int port) {

		
		
		return in;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

}
