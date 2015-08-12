package de.uniol.inf.is.odysseus.iql.basic.generator;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition;
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
		for (IQLTypeDefinition typeDef : EcoreUtil2.getAllContentsOfType(model, IQLTypeDefinition.class)) {
			doGenerate(typeDef, fsa, outputFolder);
			
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void doGenerate(IQLTypeDefinition typeDef, IFileSystemAccess fsa, String outputFolder) {
		if (typeDef.getInner() instanceof IQLClass) {
			fsa.generateFile(outputFolder+typeDef.getInner().getSimpleName() + ".java", compiler.compile(typeDef, (IQLClass)typeDef.getInner(), (G)context.cleanCopy()));
		} else if (typeDef.getInner() instanceof IQLInterface) {
			fsa.generateFile(outputFolder+typeDef.getInner().getSimpleName() + ".java", compiler.compile(typeDef, (IQLInterface)typeDef.getInner(), (G)context.cleanCopy()));
		}
	}
}
