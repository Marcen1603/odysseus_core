package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.io.File;

public class ValidatedFileNameParameter extends FileNameParameter {

	private static final long serialVersionUID = 1649870037309605096L;

	@Override
	public boolean internalValidation() {	
		File f = new File(inputValue.toString());
		return f.exists();
	}
}
