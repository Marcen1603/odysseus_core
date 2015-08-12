package de.uniol.inf.is.odysseus.iql.basic.generator.context;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.xtext.common.types.JvmTypeReference;

public abstract class AbstractIQLGeneratorContext implements IIQLGeneratorContext {

	private Collection<String> imports = new HashSet<>();
	
	private JvmTypeReference expectedTypeRef;
	
	@Override
	public void addImport(String importLine) {
		imports.add(importLine);
	}
	
	@Override
	public Collection<String> getImports() {
		return imports;
	}
	
	
	@Override
	public JvmTypeReference getExpectedTypeRef() {
		return this.expectedTypeRef;
	}

	@Override
	public void setExpectedTypeRef(JvmTypeReference typeRef) {
		this.expectedTypeRef = typeRef;
	}


}