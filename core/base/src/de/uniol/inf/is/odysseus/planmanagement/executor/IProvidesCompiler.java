package de.uniol.inf.is.odysseus.planmanagement.executor;

import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoCompilerLoadedException;

public interface IProvidesCompiler {
	ICompiler getCompiler() throws NoCompilerLoadedException;
}
