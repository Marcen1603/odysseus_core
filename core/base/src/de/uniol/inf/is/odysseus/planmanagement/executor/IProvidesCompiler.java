package de.uniol.inf.is.odysseus.planmanagement.executor;

import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;

import de.uniol.inf.is.odysseus.planmanagement.ICompiler;

public interface IProvidesCompiler {
	ICompiler getCompiler() throws CompilerException;
}
