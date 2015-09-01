package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;

public interface IODLCompiler extends IIQLCompiler<IODLGeneratorContext> {

	String compileAO(ODLOperator operator, IODLGeneratorContext context);
	String compilePO(ODLOperator o, IODLGeneratorContext context);	
	String compileAORule(ODLOperator o, IODLGeneratorContext context);

	
}