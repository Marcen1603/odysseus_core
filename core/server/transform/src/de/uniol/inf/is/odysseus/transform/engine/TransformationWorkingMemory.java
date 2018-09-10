package de.uniol.inf.is.odysseus.transform.engine;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.ruleengine.system.IWorkingEnvironment;
import de.uniol.inf.is.odysseus.ruleengine.system.WorkingMemory;

public class TransformationWorkingMemory extends WorkingMemory {

	final private ISession caller;
	final private IDataDictionary dd;
	
	public TransformationWorkingMemory(IWorkingEnvironment<?> env, ISession caller, IDataDictionary dd) {
		super(env);
		this.caller = caller;
		this.dd = dd;
	}
	
	public ISession getCaller() {
		return caller;
	}
	
	public IDataDictionary getDataDictionary() {
		return dd;
	}

}
