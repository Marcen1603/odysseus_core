package de.uniol.inf.is.odysseus.iql.odl.generator;

import javax.inject.Inject;

import org.eclipse.xtext.generator.IFileSystemAccess;






import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition;
import de.uniol.inf.is.odysseus.iql.basic.generator.AbstractIQLGenerator;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;

public class ODLGenerator extends AbstractIQLGenerator<IODLGeneratorContext, IODLCompiler>{

	@Inject
	public ODLGenerator(IODLGeneratorContext generatorContext, IODLCompiler compiler) {
		super(generatorContext, compiler);
	}

	
	protected void doGenerate(IQLTypeDefinition typeDef, IFileSystemAccess fsa, String outputFolder) {
		if (typeDef.getInner() instanceof ODLOperator) {
			fsa.generateFile(outputFolder+typeDef.getInner().getSimpleName() + IODLCompilerHelper.AO_OPERATOR + ".java", compiler.compileAO((ODLOperator) typeDef.getInner(),(IODLGeneratorContext) context.cleanCopy()));
			fsa.generateFile(outputFolder+typeDef.getInner().getSimpleName() + IODLCompilerHelper.PO_OPERATOR+ ".java", compiler.compilePO(typeDef, (ODLOperator)typeDef.getInner(),(IODLGeneratorContext) context.cleanCopy()));
			fsa.generateFile(outputFolder+typeDef.getInner().getSimpleName() + IODLCompilerHelper.AO_RULE_OPERATOR+".java", compiler.compileAORule((ODLOperator)typeDef.getInner(),(IODLGeneratorContext) context.cleanCopy()));
		} else {
			super.doGenerate(typeDef, fsa, outputFolder);			
		}
	}


}
