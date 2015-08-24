package de.uniol.inf.is.odysseus.iql.basic.generator;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;



public abstract class AbstractIQLGenerator<G extends IIQLGeneratorContext,  C extends IIQLCompiler<G>>  implements IGenerator{
	
	protected G context;
	
	
	protected C compiler;
	
	public AbstractIQLGenerator (G context, C compiler) {
		this.context = context;
		this.compiler = compiler;
	}

	@Override
	public void doGenerate(Resource input, IFileSystemAccess fsa) {
		doGenerate(input, fsa, "");
	}
	
	protected void doGenerate(Resource input, IFileSystemAccess fsa, String outputFolder) {
		try {
			for (EObject obj : input.getContents()) {
				if (obj instanceof IQLModel) {
					doGenerate((IQLModel) obj, fsa, outputFolder);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doGenerate(IQLModel model, IFileSystemAccess fsa, String outputFolder) {
		for (IQLModelElement element : EcoreUtil2.getAllContentsOfType(model, IQLModelElement.class)) {
			doGenerate(element, fsa, outputFolder);
			
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void doGenerate(IQLModelElement element, IFileSystemAccess fsa, String outputFolder) {
		if (element.getInner() instanceof IQLClass) {
			IQLClass clazz = (IQLClass) element.getInner();
			fsa.generateFile(outputFolder+clazz.getSimpleName() + ".java", compiler.compile(element, clazz, (G)context.cleanCopy()));
		} else if (element.getInner() instanceof IQLInterface) {
			IQLInterface interf = (IQLInterface) element.getInner();
			fsa.generateFile(outputFolder+interf.getSimpleName() + ".java", compiler.compile(element, interf, (G)context.cleanCopy()));
		}
	}
}
