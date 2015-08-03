package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;

public interface IIQLCompiler<G extends IIQLGeneratorContext> {

	String compile(IQLTypeDefinition typeDef, IQLClass c, G context);
	
	String compile(IQLTypeDefinition typeDef, IQLInterface i, G context);
}
