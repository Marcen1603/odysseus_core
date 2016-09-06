package de.uniol.inf.is.odysseus.iql.basic.generator.context;

import java.util.Collection;

import org.eclipse.xtext.common.types.JvmTypeReference;

public interface IIQLGeneratorContext {

	public void addImport(String importLine);
	
	public Collection<String> getImports();
	
	public IIQLGeneratorContext cleanCopy();
	
	public JvmTypeReference getExpectedTypeRef();
	
	public void setExpectedTypeRef(JvmTypeReference typeRef);
	
	public boolean hasException();
	
	public void addExceptions(Collection<JvmTypeReference> exceptions);

	void clearExceptions();


}
