package de.uniol.inf.is.odysseus.iql.basic.generator;


import java.io.File;
import java.net.URI;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;



public abstract class AbstractIQLGenerator<G extends IIQLGeneratorContext,  C extends IIQLCompiler<G>>  implements IIQLGenerator, IGenerator2 {
	
	protected G context;
	
	
	protected C compiler;
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractIQLGenerator.class);

	
	public AbstractIQLGenerator (G context, C compiler) {
		this.context = context;
		this.compiler = compiler;
	}
	
	@Override
	public void doGenerate(IQLModelElement element, IFileSystemAccess fsa) {
		doGenerate(element, fsa, null);		
	}
	
	protected void doGenerate(Resource input, IFileSystemAccess fsa, URI outputFolder) {
		for (EObject obj : input.getContents()) {
			try {
				if (obj instanceof IQLModel) {
					doGenerate((IQLModel) obj, fsa, outputFolder);
				} else if (obj instanceof IQLModelElement) {
					doGenerate((IQLModelElement) obj, fsa, outputFolder);
				}
			} catch (Exception e) {
				LOG.error("error while generating java files", e);
			}
		}
	}
	
	protected void doGenerate(IQLModel model, IFileSystemAccess fsa, URI outputFolder) {
		for (IQLModelElement element : EcoreUtil2.getAllContentsOfType(model, IQLModelElement.class)) {
			doGenerate(element, fsa, outputFolder);
			
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void doGenerate(IQLModelElement element, IFileSystemAccess fsa, URI outputFolder) {
		if (element.getInner() instanceof IQLClass) {
			IQLClass clazz = (IQLClass) element.getInner();
			generateJavaFile(element, fsa, outputFolder, clazz.getSimpleName(), compiler.compile(clazz, (G)context.cleanCopy()));
		} else if (element.getInner() instanceof IQLInterface) {
			IQLInterface interf = (IQLInterface) element.getInner();
			generateJavaFile(element, fsa, outputFolder, interf.getSimpleName(), compiler.compile(interf, (G)context.cleanCopy()));
		}
	}
	
	protected void generateJavaFile(IQLModelElement element, IFileSystemAccess fsa, URI outputFolder, String fileName, String content) {	
		String packageName = getPackage(element);
		if (packageName != null && packageName.length() > 0) {
			String packageLine = "package "+ packageName+"; "+System.lineSeparator();
			content = packageLine+content;
		}
		if (outputFolder != null && outputFolder.toString().length() > 0) {
			fsa.generateFile(outputFolder.toString()+File.separator+fileName+ ".java", content);
		} else {
			fsa.generateFile(fileName+ ".java", content);
		}
	}
	
	protected String getPackage(IQLModelElement element) {
		return "";
	}


}
