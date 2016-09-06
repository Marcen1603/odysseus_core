package de.uniol.inf.is.odysseus.iql.basic.scoping;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScopeProvider;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;

public interface IIQLScopeProvider extends IScopeProvider {

	Collection<IEObjectDescription> getTypes(EObject node);
	
	Collection<IEObjectDescription> getScopeIQLJvmElementCallExpression(EObject node);

	Collection<IEObjectDescription> getScopeIQLMemberSelection(IQLMemberSelectionExpression node);
			
	Collection<IEObjectDescription> getScopeIQLArgumentsMapKey(EObject node);
		
}
