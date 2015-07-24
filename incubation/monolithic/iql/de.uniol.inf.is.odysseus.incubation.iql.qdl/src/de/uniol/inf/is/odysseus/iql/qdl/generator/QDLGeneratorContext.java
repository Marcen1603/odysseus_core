package de.uniol.inf.is.odysseus.iql.qdl.generator;

import de.uniol.inf.is.odysseus.iql.basic.generator.context.AbstractIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;

public class QDLGeneratorContext extends AbstractIQLGeneratorContext{

	@Override
	public IIQLGeneratorContext cleanCopy() {
		return new QDLGeneratorContext();
	}

}
