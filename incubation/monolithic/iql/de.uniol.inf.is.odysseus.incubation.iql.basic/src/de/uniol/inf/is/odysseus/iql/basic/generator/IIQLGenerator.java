package de.uniol.inf.is.odysseus.iql.basic.generator;

import org.eclipse.xtext.generator.IFileSystemAccess;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;

public interface IIQLGenerator {
	public void doGenerate(IQLModelElement element, IFileSystemAccess fsa);

}
