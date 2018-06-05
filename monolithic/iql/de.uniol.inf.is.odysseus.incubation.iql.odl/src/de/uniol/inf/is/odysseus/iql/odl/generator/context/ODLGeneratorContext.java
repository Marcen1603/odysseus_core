package de.uniol.inf.is.odysseus.iql.odl.generator.context;

import de.uniol.inf.is.odysseus.iql.basic.generator.context.AbstractIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;

public class ODLGeneratorContext extends AbstractIQLGeneratorContext implements IODLGeneratorContext{
	
	private boolean ao;

	@Override
	public IIQLGeneratorContext cleanCopy() {
		return new ODLGeneratorContext();
	}
	
	@Override
	public boolean isAo() {
		return ao;
	}

	@Override
	public void setAo(boolean ao) {
		this.ao = ao;
	}

	
}
