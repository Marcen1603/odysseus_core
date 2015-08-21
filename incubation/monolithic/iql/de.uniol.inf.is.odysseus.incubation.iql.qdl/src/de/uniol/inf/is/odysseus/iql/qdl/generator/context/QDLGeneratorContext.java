package de.uniol.inf.is.odysseus.iql.qdl.generator.context;

import de.uniol.inf.is.odysseus.iql.basic.generator.context.AbstractIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;

public class QDLGeneratorContext extends AbstractIQLGeneratorContext implements IQDLGeneratorContext{

	@Override
	public IIQLGeneratorContext cleanCopy() {
		return new QDLGeneratorContext();
	}

}
