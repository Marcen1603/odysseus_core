package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;

public interface IIQLTypeCompiler<G extends IIQLGeneratorContext> {

	String compile(JvmTypeReference typeRef, G context, boolean wrapper);

}
