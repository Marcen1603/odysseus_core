package de.uniol.inf.is.odysseus.salsa.playground.udf;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

@UserDefinedFunction(name = "Scanner")
public class SickUDFunction<R> implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	String name = null;

	@Override
	public void init(String initString) {
		name = initString;
	}

	@Override
	public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> in, int port) {

		
		//System.out.println(name);
		in.append(name);
		//System.out.println("Attributes:" +intuple.size());

		return in;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT ;
	}


}
