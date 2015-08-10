package de.uniol.inf.is.odysseus.iql.basic.scoping;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScopeProvider;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;

public interface IIQLScopeProvider extends IScopeProvider {

	Collection<IEObjectDescription> getIQLJvmElementCallExpression(EObject expr);

	Collection<IEObjectDescription> getScopeIQLMemberSelection(IQLMemberSelectionExpression expr);
	
	Collection<IEObjectDescription> getScopeIQLMethodSelection(JvmTypeReference typeRef, boolean isThis, boolean isSuper);

	Collection<IEObjectDescription> getScopeIQLAttributeSelection(JvmTypeReference typeRef, boolean isThis, boolean isSuper);
	
	Collection<String> getUsedNamespaces(EObject obj);

	Collection<IEObjectDescription> getTypes(EObject node);
	
	Collection<JvmType> getAllTypes(EObject node);


}
