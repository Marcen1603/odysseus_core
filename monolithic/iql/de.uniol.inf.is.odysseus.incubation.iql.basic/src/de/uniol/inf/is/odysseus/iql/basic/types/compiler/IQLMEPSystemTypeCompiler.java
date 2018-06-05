package de.uniol.inf.is.odysseus.iql.basic.types.compiler;

import java.util.List;

import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;

import de.uniol.inf.is.odysseus.iql.basic.typing.builder.IIQLSystemTypeCompiler;

public class IQLMEPSystemTypeCompiler implements IIQLSystemTypeCompiler{

	@Override
	public String compileFieldSelection(JvmField field) {
		return null;
	}

	@Override
	public boolean compileFieldSelectionManually() {
		return false;
	}

	@Override
	public String compileMethodSelection(JvmOperation method, List<String> compiledArguments) {
		StringBuilder builder = new StringBuilder();
		builder.append("get");
		builder.append("(");
		builder.append("\""+method.getSimpleName()+"\"");
		for (String arg : compiledArguments) {
			builder.append(",");
			builder.append(arg);
		}
		builder.append(")");
		return builder.toString();
	}

	@Override
	public boolean compileMethodSelectionManually() {
		return true;
	}

}
