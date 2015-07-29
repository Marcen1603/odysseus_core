package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmPrimitiveType;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;


public abstract class AbstractIQLQualifiedNameProvider extends DefaultDeclarativeQualifiedNameProvider implements IQualifiedNameProvider{
	@Inject
	private IQualifiedNameConverter converter;
	
	@Inject
	private IIQLTypeFactory factory;
	
	
	@Override
	public QualifiedName getFullyQualifiedName(EObject obj) {
		if (obj instanceof JvmField) {
			JvmField field = (JvmField)obj;
			return converter.toQualifiedName(field.getSimpleName());
		} else if (obj instanceof JvmOperation) {
			JvmOperation op = (JvmOperation)obj;
			return converter.toQualifiedName(op.getSimpleName());
		} else if (obj instanceof JvmGenericType) {
			JvmGenericType type = (JvmGenericType)obj;
			return converter.toQualifiedName(factory.getLongName(type, false));
		} else if (obj instanceof JvmPrimitiveType) {
			JvmPrimitiveType type = (JvmPrimitiveType)obj;
			return converter.toQualifiedName(type.getSimpleName());
		} else if (obj instanceof IQLVariableDeclaration) {
			IQLVariableDeclaration decl = (IQLVariableDeclaration)obj;
			return converter.toQualifiedName(decl.getName());
		}else if (obj instanceof IQLArgumentsMapKeyValue) {
			IQLArgumentsMapKeyValue keyValue = (IQLArgumentsMapKeyValue)obj;
			return converter.toQualifiedName(keyValue.getKey());
		} else if (obj instanceof JvmFormalParameter) {
			JvmFormalParameter parameter = (JvmFormalParameter)obj;
			return converter.toQualifiedName(parameter.getName());
		}else {
			return super.getFullyQualifiedName(obj);
		}
	}


}
