package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;

public interface IIQLCompiler<G extends IIQLGeneratorContext> {

	String compile(IQLModelElement element, IQLClass c, G context);
	
	String compile(IQLModelElement element, IQLInterface i, G context);
}
