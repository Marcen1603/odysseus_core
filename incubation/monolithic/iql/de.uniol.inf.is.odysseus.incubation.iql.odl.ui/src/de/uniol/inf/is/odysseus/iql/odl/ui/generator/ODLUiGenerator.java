package de.uniol.inf.is.odysseus.iql.odl.ui.generator;


import java.net.URI;

import javax.inject.Inject;







import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2;
import org.eclipse.xtext.generator.IFileSystemAccess;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.ui.generator.BasicIQLUiGenerator;
import de.uniol.inf.is.odysseus.iql.basic.ui.typing.BasicIQLUiTypeUtils;
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGenerator;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext;

public class ODLUiGenerator extends ODLGenerator{


	@Inject
	public ODLUiGenerator(IODLGeneratorContext generatorContext,ODLCompiler compiler) {
		super(generatorContext, compiler);
	}
	
	
	@Override
	protected String getPackage(IQLModelElement element) {
		String packageName = BasicIQLUiTypeUtils.getPackage(element.eResource());
		if (packageName != null && packageName.length() > 0) {
			return packageName.replace(IQLQualifiedNameConverter.DELIMITER, ".");
		}
		return packageName;
	}


	@Override
	public void doGenerate(Resource input, IFileSystemAccess fsa) {
		URI outputFolder = null;
		if (fsa instanceof EclipseResourceFileSystemAccess2) {
			outputFolder = BasicIQLUiTypeUtils.getOutputPath(input);
			BasicIQLUiGenerator.createEditFolder(input);
		}
		doGenerate(input, fsa, outputFolder);
	}
}
