package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmPrimitiveType;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;

import com.google.inject.Inject;

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
			String name = field.getSimpleName();
			return converter.toQualifiedName(name);
		} else if (obj instanceof JvmOperation) {
			JvmOperation op = (JvmOperation)obj;
			String simpleName = op.getSimpleName();
			return converter.toQualifiedName(simpleName);
//		} else if (obj instanceof IQLClass) {
//			return converter.toQualifiedName(((IQLClass) obj).getQualifiedName());
//		} else if (obj instanceof IQLInterface) {
//			return converter.toQualifiedName(((IQLInterface) obj).getQualifiedName());
		} else if (obj instanceof JvmGenericType) {
			JvmGenericType t = (JvmGenericType)obj;
			String name = factory.getLongName(t, false);
			return converter.toQualifiedName(name);
		} else if (obj instanceof JvmPrimitiveType) {
			JvmPrimitiveType t = (JvmPrimitiveType)obj;
			String name = t.getSimpleName();
			return converter.toQualifiedName(name);
		} else {
			return super.getFullyQualifiedName(obj);
		}
	}


}
