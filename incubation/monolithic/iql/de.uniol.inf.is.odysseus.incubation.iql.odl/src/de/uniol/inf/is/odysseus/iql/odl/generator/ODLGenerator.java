package de.uniol.inf.is.odysseus.iql.odl.generator;

import javax.inject.Inject;

import org.eclipse.xtext.generator.IFileSystemAccess;


import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition;
import de.uniol.inf.is.odysseus.iql.basic.generator.AbstractIQLGenerator;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;

public class ODLGenerator extends AbstractIQLGenerator<ODLGeneratorContext, ODLCompiler>{

	@Inject
	public ODLGenerator(ODLGeneratorContext generatorContext, ODLCompiler compiler) {
		super(generatorContext, compiler);
	}

	
	protected void doGenerate(IQLTypeDefinition typeDef, IFileSystemAccess fsa, String outputFolder) {
		if (typeDef.getInner() instanceof ODLOperator) {
			fsa.generateFile(outputFolder+typeDef.getInner().getSimpleName() + ODLCompilerHelper.AO_OPERATOR + ".java", compiler.compileAO((ODLOperator) typeDef.getInner(),(ODLGeneratorContext) context.cleanCopy()));
			fsa.generateFile(outputFolder+typeDef.getInner().getSimpleName() + ODLCompilerHelper.PO_OPERATOR+ ".java", compiler.compilePO(typeDef, (ODLOperator)typeDef.getInner(),(ODLGeneratorContext) context.cleanCopy()));
			fsa.generateFile(outputFolder+typeDef.getInner().getSimpleName() + ODLCompilerHelper.AO_RULE_OPERATOR+".java", compiler.compileAORule((ODLOperator)typeDef.getInner(),(ODLGeneratorContext) context.cleanCopy()));
		} else {
			super.doGenerate(typeDef, fsa, outputFolder);			
		}
	}


}
