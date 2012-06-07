package de.uniol.inf.is.odysseus.core.server.udf;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;

@UserDefinedFunction(name="ELEMENT_SAMPLING")
public class ElementSampling<R> implements IUserDefinedFunction<R, R> {

	int everyNthObject;
	int objectsRead;
	
	/*
	 * 
	 * initString: Send each nth Element
	 */
	@Override
	public void init(String initString) {
		everyNthObject = Integer.parseInt(initString);
		objectsRead = 0;
	}

	@Override
	public R process(R in, int port) {
		objectsRead++;
		if (objectsRead % everyNthObject  == 0){
			return in;
		}
		return null;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

}
