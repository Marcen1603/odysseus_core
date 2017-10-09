package de.uniol.inf.is.odysseus.iql.qdl.ui.generator;

import java.net.URI;

import javax.inject.Inject;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2;
import org.eclipse.xtext.generator.IFileSystemAccess;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.ui.generator.BasicIQLUiGenerator;
import de.uniol.inf.is.odysseus.iql.basic.ui.typing.BasicIQLUiTypeUtils;
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGenerator;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext;

public class QDLUiGenerator extends QDLGenerator{


	@Inject
	public QDLUiGenerator(IQDLGeneratorContext generatorContext,IQDLCompiler compiler) {
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

	public void doGenerate(Resource input, IFileSystemAccess fsa) {
		URI outputFolder = BasicIQLUiTypeUtils.getOutputPath(input);
		if (fsa instanceof EclipseResourceFileSystemAccess2) {
			BasicIQLUiGenerator.createEditFolder(input);
		}
		doGenerate(input, fsa, outputFolder);
	}
	
	@Override
	public void doGenerate(IQLModelElement element, IFileSystemAccess fsa) {
		URI outputFolder = BasicIQLUiTypeUtils.getOutputPath(element.eResource());
		if (fsa instanceof EclipseResourceFileSystemAccess2) {
			BasicIQLUiGenerator.createEditFolder(element.eResource());
		}
		doGenerate(element, fsa, outputFolder);
	}


}
