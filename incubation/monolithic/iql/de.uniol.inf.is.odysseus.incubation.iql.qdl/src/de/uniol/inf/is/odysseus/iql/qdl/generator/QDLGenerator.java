package de.uniol.inf.is.odysseus.iql.qdl.generator;

import javax.inject.Inject;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.generator.IFileSystemAccess;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.generator.AbstractIQLGenerator;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;

public class QDLGenerator extends AbstractIQLGenerator<QDLGeneratorContext, QDLCompiler>{

	@Inject
	public QDLGenerator(QDLGeneratorContext generatorContext, QDLCompiler compiler) {
		super(generatorContext, compiler);
	}
	
	@Override
	protected void doGenerate(IQLFile file, IFileSystemAccess fsa, String outputFolder) {
		for (QDLQuery q : EcoreUtil2.getAllContentsOfType(file, QDLQuery.class)) {
			fsa.generateFile(outputFolder+q.getSimpleName() + ".java", compiler.compile(q,(QDLGeneratorContext) context.cleanCopy()));
		}
		
		super.doGenerate(file, fsa, outputFolder);
	}


}
