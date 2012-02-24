package de.uniol.inf.is.odysseus.salsa.playground.udf;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

@UserDefinedFunction(name = "Scanner")
public class SickUDFunction<R> implements IUserDefinedFunction<R, R> {

	String name = null;

	@Override
	public void init(String initString) {
		name = initString;
	}

	@Override
	public R process(R in, int port) {
		RelationalTuple<IMetaAttribute> intuple = (RelationalTuple<IMetaAttribute>) in;

		
		//System.out.println(name);
		intuple = intuple.append(name);
		//System.out.println("Attributes:" +intuple.size());

		return (R)intuple;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT ;
	}


}
