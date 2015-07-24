package de.uniol.inf.is.odysseus.iql.odl.generator;

import javax.inject.Inject;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.generator.IFileSystemAccess;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.generator.AbstractIQLGenerator;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;

public class ODLGenerator extends AbstractIQLGenerator<ODLGeneratorContext, ODLCompiler>{

	@Inject
	public ODLGenerator(ODLGeneratorContext generatorContext, ODLCompiler compiler) {
		super(generatorContext, compiler);
	}
	
	@Override
	protected void doGenerate(IQLFile file, IFileSystemAccess fsa, String outputFolder) {
		for (ODLOperator o : EcoreUtil2.getAllContentsOfType(file, ODLOperator.class)) {
			fsa.generateFile(outputFolder+o.getSimpleName() + ODLCompilerHelper.AO_OPERATOR + ".java", compiler.compileAO(o,(ODLGeneratorContext) context.cleanCopy()));
			fsa.generateFile(outputFolder+o.getSimpleName() + ODLCompilerHelper.PO_OPERATOR+ ".java", compiler.compilePO(o,(ODLGeneratorContext) context.cleanCopy()));
			fsa.generateFile(outputFolder+o.getSimpleName() + ODLCompilerHelper.AO_RULE_OPERATOR+".java", compiler.compileAORule(o,(ODLGeneratorContext) context.cleanCopy()));
		}
		super.doGenerate(file, fsa, outputFolder);
	}


}
