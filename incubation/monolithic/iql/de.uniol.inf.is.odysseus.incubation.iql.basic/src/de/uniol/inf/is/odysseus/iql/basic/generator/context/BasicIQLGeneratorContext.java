package de.uniol.inf.is.odysseus.iql.basic.generator.context;

public class BasicIQLGeneratorContext extends AbstractIQLGeneratorContext{

	@Override
	public IIQLGeneratorContext cleanCopy() {
		return new BasicIQLGeneratorContext();
	}

}
