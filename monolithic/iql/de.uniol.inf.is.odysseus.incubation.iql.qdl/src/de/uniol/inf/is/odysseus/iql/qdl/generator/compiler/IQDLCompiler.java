package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;

public interface IQDLCompiler extends IIQLCompiler<IQDLGeneratorContext> {

	String compile(QDLQuery q, IQDLGeneratorContext context);

}
