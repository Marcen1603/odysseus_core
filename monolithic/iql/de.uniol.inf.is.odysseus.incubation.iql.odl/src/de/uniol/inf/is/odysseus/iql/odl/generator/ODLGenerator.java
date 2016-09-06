package de.uniol.inf.is.odysseus.iql.odl.generator;

import java.net.URI;

import javax.inject.Inject;

import org.eclipse.xtext.generator.IFileSystemAccess;









import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
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

	
	protected void doGenerate(IQLModelElement element, IFileSystemAccess fsa, URI outputFolder) {
		if (element.getInner() instanceof ODLOperator) {
			ODLOperator op = (ODLOperator) element.getInner();			
			generateJavaFile(element, fsa, outputFolder, op.getSimpleName() + IODLCompilerHelper.AO_OPERATOR, compiler.compileAO(op,(IODLGeneratorContext) context.cleanCopy()));
			generateJavaFile(element, fsa, outputFolder,op.getSimpleName() + IODLCompilerHelper.AO_RULE_OPERATOR, compiler.compileAORule(op,(IODLGeneratorContext) context.cleanCopy()));
			generateJavaFile(element, fsa, outputFolder,op.getSimpleName() + IODLCompilerHelper.PO_OPERATOR, compiler.compilePO(op,(IODLGeneratorContext) context.cleanCopy()));
		} else {
			super.doGenerate(element, fsa, outputFolder);			
		}
	}


}
