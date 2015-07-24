package de.uniol.inf.is.odysseus.iql.basic.generator;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext;

public class BasicIQLGenerator extends AbstractIQLGenerator<BasicIQLGeneratorContext, BasicIQLCompiler>{

	@Inject
	public BasicIQLGenerator(BasicIQLGeneratorContext generatorContext,BasicIQLCompiler compiler) {
		super(generatorContext, compiler);
	}



}
