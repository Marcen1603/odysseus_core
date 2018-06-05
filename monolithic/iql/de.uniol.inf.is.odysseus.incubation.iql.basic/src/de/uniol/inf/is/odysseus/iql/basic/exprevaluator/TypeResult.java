package de.uniol.inf.is.odysseus.iql.basic.exprevaluator;

import org.eclipse.xtext.common.types.JvmTypeReference;



public class TypeResult {
	private final JvmTypeReference ref;
	private String diagnostic;


	public TypeResult(JvmTypeReference ref) {
		this.ref = ref;
	}
	


	public TypeResult(String diagnostic) {
		this.ref = null;
		this.diagnostic = diagnostic;
	}
	
	public TypeResult() {
		this.ref = null;
	}
	

	public boolean isNull() {
		return this.ref == null;
	}

	public JvmTypeReference getRef() {
		return ref;
	}


	public String getDiagnostic() {
		return diagnostic;
	}
	
	public boolean hasError() {
		return this.diagnostic!=null;
	}

	public void setDiagnostic(String diagnostic) {
		this.diagnostic = diagnostic;
	}

	public void addDiagnostic(String diagnostic) {
		if (this.diagnostic == null) {
			this.diagnostic = diagnostic;
		} else {
			this.diagnostic.concat("\n" + diagnostic);
		}
	}
}
