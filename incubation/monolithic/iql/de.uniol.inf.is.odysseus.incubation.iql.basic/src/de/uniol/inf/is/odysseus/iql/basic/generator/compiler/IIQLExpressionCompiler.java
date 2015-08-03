package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;


import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;

public interface IIQLExpressionCompiler<G extends IIQLGeneratorContext> {

	String compile(IQLExpression expr, G context);
	
	String compile(IQLArgumentsList list, G context);
	
	String compile(IQLArgumentsList list, EList<JvmFormalParameter> parameters, G context);
	
	String compile(IQLArgumentsMap map, JvmTypeReference type, G context);

}
