package de.uniol.inf.is.odysseus.iql.basic.generator;


import java.io.File;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
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
		try {
			String outputFolder = "";
			if (fsa instanceof EclipseResourceFileSystemAccess2) {
				outputFolder = getOutputFolder(input);
			}
			for (EObject obj : input.getContents()) {
				if (obj instanceof IQLFile) {
					doGenerate((IQLFile) obj, fsa, outputFolder);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void doGenerate(IQLFile file, IFileSystemAccess fsa, String outputFolder) {
		for (IQLClass c : EcoreUtil2.getAllContentsOfType(file, IQLClass.class)) {
			fsa.generateFile(outputFolder+c.getSimpleName() + ".java", compiler.compile(c, (G)context.cleanCopy()));
		}
		
		for (IQLInterface i : EcoreUtil2.getAllContentsOfType(file, IQLInterface.class)) {
			fsa.generateFile(outputFolder+i.getSimpleName() + ".java", compiler.compile(i, (G)context.cleanCopy()));
		}
	}
	
	private String getOutputFolder(Resource res) {
		StringBuilder b = new StringBuilder();
		for (int i = 2; i < res.getURI().segmentCount()-1; i++) {
			b.append(res.getURI().segment(i));
		}
		return b.toString()+File.separator;
	}
	

}
