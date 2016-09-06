package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;


import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;

public interface IIQLStatementCompiler<G extends IIQLGeneratorContext> {

	String compile(IQLStatement stmt, G context);
	
	String compile(IQLVariableInitialization init, JvmTypeReference typeRef, G context);


}
