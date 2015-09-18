package de.uniol.inf.is.odysseus.iql.basic.typing.builder;

import java.util.List;

import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;

public interface IIQLSystemTypeCompiler {

	public String compileFieldSelection(JvmField field);
	public boolean compileFieldSelectionManually();
	
	public String compileMethodSelection(JvmOperation method, List<String> compiledArguments);
	public boolean compileMethodSelectionManually();


}
