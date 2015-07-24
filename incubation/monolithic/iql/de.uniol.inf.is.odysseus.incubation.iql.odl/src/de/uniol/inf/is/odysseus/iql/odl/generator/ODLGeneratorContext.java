package de.uniol.inf.is.odysseus.iql.odl.generator;

import de.uniol.inf.is.odysseus.iql.basic.generator.context.AbstractIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;

public class ODLGeneratorContext extends AbstractIQLGeneratorContext{
	
	private boolean ao;

	@Override
	public IIQLGeneratorContext cleanCopy() {
		return new ODLGeneratorContext();
	}

	public boolean isAo() {
		return ao;
	}

	public void setAo(boolean ao) {
		this.ao = ao;
	}

	
}
