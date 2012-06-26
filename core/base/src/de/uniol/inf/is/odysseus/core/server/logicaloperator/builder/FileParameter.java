package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.io.File;

public class FileParameter extends AbstractParameter<File> {

	
	private static final long serialVersionUID = -4970934499527167572L;

	@Override
	protected void internalAssignment() {
		String path = inputValue.toString();
		File file = new File(path);
		setValue(file);		
	}
	
	@Override
	public boolean internalValidation() {	
		File f = new File(inputValue.toString());
		return f.exists();
	}

}
