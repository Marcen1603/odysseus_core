package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.access.jdt.IJdtTypeProvider;
import org.eclipse.xtext.common.types.xtext.ui.JdtBasedSimpleTypeScope;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;

import com.google.common.base.Predicate;

@SuppressWarnings("restriction")
public class IQLJdtBasedTypeScope extends JdtBasedSimpleTypeScope {


	
	public IQLJdtBasedTypeScope(IJdtTypeProvider typeProvider,
			IQualifiedNameConverter qualifiedNameConverter,
			Predicate<IEObjectDescription> filter) {
		super(typeProvider, qualifiedNameConverter, filter);
	}
	
	@Override
	public IEObjectDescription getSingleElement(QualifiedName name, boolean binary) {
		try {
			JvmType type = getTypeProvider().findTypeByName(name.toString(), binary);
			if (type == null)
				return null;
			IEObjectDescription result = EObjectDescription.create(name, type);
			return result;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
