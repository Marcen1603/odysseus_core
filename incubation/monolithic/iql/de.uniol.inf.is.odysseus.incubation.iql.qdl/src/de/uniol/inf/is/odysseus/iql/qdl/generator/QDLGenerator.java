package de.uniol.inf.is.odysseus.iql.qdl.generator;

import javax.inject.Inject;

import org.eclipse.xtext.generator.IFileSystemAccess;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition;
import de.uniol.inf.is.odysseus.iql.basic.generator.AbstractIQLGenerator;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;

public class QDLGenerator extends AbstractIQLGenerator<QDLGeneratorContext, QDLCompiler>{

	@Inject
	public QDLGenerator(QDLGeneratorContext generatorContext, QDLCompiler compiler) {
		super(generatorContext, compiler);
	}

	protected void doGenerate(IQLTypeDefinition typeDef, IFileSystemAccess fsa, String outputFolder) {
		if (typeDef.getInner() instanceof QDLQuery) {
			fsa.generateFile(outputFolder+typeDef.getInner().getSimpleName() + ".java", compiler.compile(typeDef, (QDLQuery)typeDef.getInner(),(QDLGeneratorContext) context.cleanCopy()));
		} else {
			super.doGenerate(typeDef, fsa, outputFolder);			
		}
	}


}
