package de.uniol.inf.is.odysseus.iql.qdl.types.compiler;

import java.util.List;

import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.IIQLSystemTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;

public class SourceSystemTypeCompiler implements IIQLSystemTypeCompiler{
	

	private IQDLTypeUtils typeUtils;
	private IQLQualifiedNameConverter converter;

	public SourceSystemTypeCompiler(IQDLTypeUtils typeUtils, IQLQualifiedNameConverter converter) {
		this.typeUtils = typeUtils;
		this.converter = converter;
	}
	
	@Override
	public String compileFieldSelection(JvmField field) {
		if (converter.toJavaString(typeUtils.getLongName(field.getType(), true)).equals(String.class.getCanonicalName())) {
			StringBuilder builder = new StringBuilder();
			builder.append("getAttribute");
			builder.append("(");
			builder.append("\""+field.getSimpleName()+"\"");
			builder.append(")");			
			return builder.toString();
		} else {
			return field.getSimpleName();
		}
	}

	@Override
	public boolean compileFieldSelectionManually() {
		return true;
	}

	@Override
	public String compileMethodSelection(JvmOperation method, List<String> compiledArguments) {
		return "";
	}

	@Override
	public boolean compileMethodSelectionManually() {
		return false;
	}
	

}
