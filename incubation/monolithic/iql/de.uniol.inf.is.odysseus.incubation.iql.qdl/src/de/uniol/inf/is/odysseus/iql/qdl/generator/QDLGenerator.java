package de.uniol.inf.is.odysseus.iql.qdl.generator;

import javax.inject.Inject;

import org.eclipse.xtext.generator.IFileSystemAccess;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.generator.AbstractIQLGenerator;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;

public class QDLGenerator extends AbstractIQLGenerator<IQDLGeneratorContext, IQDLCompiler>{

	@Inject
	public QDLGenerator(IQDLGeneratorContext generatorContext, IQDLCompiler compiler) {
		super(generatorContext, compiler);
	}

	protected void doGenerate(IQLModelElement element, IFileSystemAccess fsa, String outputFolder) {
		if (element.getInner() instanceof QDLQuery) {
			QDLQuery query = (QDLQuery) element.getInner();
			fsa.generateFile(outputFolder+query.getSimpleName() + ".java", compiler.compile(query,(IQDLGeneratorContext) context.cleanCopy()));
		} else {
			super.doGenerate(element, fsa, outputFolder);			
		}
	}


}
