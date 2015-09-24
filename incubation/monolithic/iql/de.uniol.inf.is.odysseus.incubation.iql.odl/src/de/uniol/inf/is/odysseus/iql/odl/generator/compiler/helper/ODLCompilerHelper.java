package de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.AbstractIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.EventMethodsFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLCompilerHelper extends AbstractIQLCompilerHelper<IODLLookUp, IODLTypeDictionary, IODLTypeUtils> implements IODLCompilerHelper {

	@Inject
	private IQLQualifiedNameConverter converter;
	
	@Inject
	public ODLCompilerHelper(IODLLookUp lookUp, IODLTypeDictionary typeDictionary, IODLTypeUtils typeUtils) {
		super(lookUp, typeDictionary, typeUtils);
	}
	
	@Override
	public Collection<ODLParameter> getParameters(ODLOperator operator) {
		return EcoreUtil2.getAllContentsOfType(operator, ODLParameter.class);
	}
	
	@Override
	public boolean hasValidateMethod(ODLOperator operator, ODLParameter parameter) {
		for (ODLMethod method : EcoreUtil2.getAllContentsOfType(operator, ODLMethod.class)) {
			if (method.isValidate() && method.getSimpleName() != null && method.getSimpleName().equalsIgnoreCase(parameter.getSimpleName())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Collection<ODLMethod> getODLMethods(ODLOperator operator) {
		return EcoreUtil2.getAllContentsOfType(operator, ODLMethod.class);
	}

	@Override
	public JvmTypeReference determineReadType(ODLOperator operator) {
		for (IQLMethod method : EcoreUtil2.getAllContentsOfType(operator, IQLMethod.class)) {
			if (method.getSimpleName() != null && method.getSimpleName().equals("processNext")) {
				JvmFormalParameter parameter = method.getParameters().get(0);
				return parameter.getParameterType();

			}
		}
		return typeUtils.createTypeRef(Tuple.class, typeDictionary.getSystemResourceSet());
	}

	@Override
	public OutputMode determineOutputMode(ODLOperator operator) {
		if (operator.getMetadataList() != null) {
			for (IQLMetadata metadata : operator.getMetadataList().getElements()) {
				if (metadata.getValue() instanceof IQLMetadataValueSingleString) {
					String value = ((IQLMetadataValueSingleString)metadata.getValue()).getValue();					
					return OutputMode.valueOf(value);
				}				
			}
		}
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public boolean hasPredicate(ODLOperator operator) {
		return !getPredicates(operator).isEmpty() || !getPredicateArrays(operator).isEmpty();
	}

	@Override
	public Collection<String> getPredicates(ODLOperator operator) {
		Collection<String> predicates = new HashSet<>();
		for (ODLParameter parameter : EcoreUtil2.getAllContentsOfType(operator, ODLParameter.class)) {
			if (converter.toJavaString(typeUtils.getLongName(parameter.getType(), true)).equals(IPredicate.class.getCanonicalName())) {
				predicates.add(parameter.getSimpleName());
			}
		}
		return predicates;
	}
	
	@Override
	public Collection<String> getPredicateArrays(ODLOperator operator) {
		Collection<String> predicates = new HashSet<>();
		for (ODLParameter parameter : EcoreUtil2.getAllContentsOfType(operator, ODLParameter.class)) {
			if (typeUtils.isArray(parameter.getType())) {
				if (converter.toJavaString(typeUtils.getLongName(parameter.getType(), false)).equals(IPredicate.class.getCanonicalName())) {
					predicates.add(parameter.getSimpleName());
				}

			}
		}
		return predicates;
	}

	@Override
	public boolean hasPOInitMethod(ODLOperator operator) {
		for (ODLMethod method : EcoreUtil2.getAllContentsOfType(operator, ODLMethod.class)) {
			if (method.isOn() && method.getSimpleName().equals("po_init") && method.getParameters().size() == 0) {
				return true;
			}		
		}
		return false;
	}

	@Override
	public boolean hasOperatorValidate(ODLOperator operator) {
		for (ODLMethod method : EcoreUtil2.getAllContentsOfType(operator, ODLMethod.class)) {
			if (method.isValidate() &&  method.getSimpleName() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasProcessNext(ODLOperator operator) {
		for (IQLMethod method : EcoreUtil2.getAllContentsOfType(operator, IQLMethod.class)) {
			if (method.getSimpleName() != null && method.getSimpleName().equals("process_next") && method.getParameters().size() == 2) {
				return true;
			} else if (method.getSimpleName() != null && method.getSimpleName().equals("processNext") && method.getParameters().size() == 2) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasProcessPunctuation(ODLOperator operator) {
		for (IQLMethod method : EcoreUtil2.getAllContentsOfType(operator, IQLMethod.class)) {
			if (method.getSimpleName() != null && method.getSimpleName().equals("processPunctuation") && method.getParameters().size() == 2) {
				return true;
			}
		}
		return false;
	}

	@Override
	public JvmTypeReference determineMetadataType(ODLOperator operator) {
		return typeUtils.createTypeRef(IMetaAttribute.class, typeDictionary.getSystemResourceSet());
	}
	
	@Override
	public Collection<JvmMember> getAOMembers(ODLOperator operator) {
		Collection<IQLAttribute> attributes = getAOAttributes(operator);
		Collection<IQLMethod> methods = getAOMethods(operator);
		Collection<JvmMember> result = new ArrayList<>();
		for (JvmMember member : operator.getMembers()) {
			if (member instanceof IQLAttribute || member instanceof IQLMethod) {
				if (attributes.contains(member) || methods.contains(member)) {
					result.add(member);
				}
			} 
		}
		return result;
	}
	
	@Override
	public Collection<JvmMember> getPOMembers(ODLOperator operator) {
		Collection<IQLAttribute> attributes = getPOAttributes(operator);
		Collection<IQLMethod> methods = getPOMethods(operator);
		Collection<JvmMember> result = new ArrayList<>();
		for (JvmMember member : operator.getMembers()) {
			if (member instanceof IQLAttribute || member instanceof IQLMethod) {
				if (attributes.contains(member) || methods.contains(member)) {
					result.add(member);
				}
			} else {
				result.add(member);
			}
		}
		return result;
	}

	@Override
	public Collection<IQLAttribute> getAOAttributes(ODLOperator operator) {
		Collection<IQLAttribute> result = new HashSet<>();
		for (JvmMember member : operator.getMembers()) {
			if (member instanceof ODLParameter) {
				result.add((IQLAttribute) member);
			}
		}
		for (IQLMethod method : getAOMethods(operator)) {
			for (IQLJvmElementCallExpression expr : EcoreUtil2.getAllContentsOfType(method, IQLJvmElementCallExpression.class)) {
				if (expr.getElement() instanceof IQLAttribute && expr.getElement().eContainer() == operator) {
					result.add((IQLAttribute) expr.getElement());
				}
			}
			for (IQLMemberSelectionExpression expr : EcoreUtil2.getAllContentsOfType(method, IQLMemberSelectionExpression.class)) {
				if (expr.getSel().getMember() instanceof IQLAttribute && expr.getSel().getMember() .eContainer() == operator) {
					result.add((IQLAttribute) expr.getSel().getMember());
				}
			}
		}
		return result;
	}

	@Override
	public Collection<IQLMethod> getAOMethods(ODLOperator operator) {
		Collection<IQLMethod> result = new ArrayList<>();
		for (JvmMember member : operator.getMembers()) {
			if (member instanceof ODLMethod) {
				ODLMethod method = (ODLMethod) member;
				if (method.isValidate() || method.isAo()) {
					result.add(method);
				} else if (method.isOn() && EventMethodsFactory.getInstance().hasEventMethod(true, method.getSimpleName(), method.getParameters())) {
					result.add(method);
				}
			} else if (member instanceof IQLMethod) {
				result.add((IQLMethod) member);
			}
		}
		return result;
	}

	@Override
	public Collection<IQLAttribute> getPOAttributes(ODLOperator operator) {
		Collection<IQLAttribute> result = new HashSet<>();
		for (JvmMember member : operator.getMembers()) {
			if (member instanceof ODLParameter) {
				result.add((IQLAttribute) member);
			}
		}
		for (IQLMethod method : getPOMethods(operator)) {
			for (IQLJvmElementCallExpression expr : EcoreUtil2.getAllContentsOfType(method, IQLJvmElementCallExpression.class)) {
				if (expr.getElement() instanceof IQLAttribute && expr.getElement().eContainer() == operator) {
					result.add((IQLAttribute) expr.getElement());
				}
			}
			for (IQLMemberSelectionExpression expr : EcoreUtil2.getAllContentsOfType(method, IQLMemberSelectionExpression.class)) {
				if (expr.getSel().getMember() instanceof IQLAttribute && expr.getSel().getMember() .eContainer() == operator) {
					result.add((IQLAttribute) expr.getSel().getMember());
				}
			}
		}
		Collection<IQLAttribute> aoAttributes = getAOAttributes(operator);
		for (JvmMember member : operator.getMembers()) {
			if (member instanceof IQLAttribute) {
				IQLAttribute attr = (IQLAttribute) member;
				if (!aoAttributes.contains(attr)) {
					result.add(attr);
				}
			}			
		}
		return result;
	}
	
	@Override
	public Collection<IQLMethod> getPOMethods(ODLOperator operator) {
		Collection<IQLMethod> result = new HashSet<>();
		for (JvmMember member : operator.getMembers()) {
			if (member instanceof ODLMethod) {
				ODLMethod odlMethod = (ODLMethod) member;
				if (odlMethod.isOn() && EventMethodsFactory.getInstance().hasEventMethod(false, odlMethod.getSimpleName(), odlMethod.getParameters())) {
					result.add(odlMethod);
				} else if (odlMethod.isPo()) {
					result.add(odlMethod);
				}
			} else if (member instanceof IQLMethod) {
				result.add((IQLMethod) member);
			}			
		}
		return result;
	}



	@Override
	public Collection<IQLAttribute> getAOAndPOAttributes(ODLOperator operator) {
		Collection<IQLAttribute> result = new HashSet<>();
		
		Collection<IQLAttribute> aoAttributes = getAOAttributes(operator);
		Collection<IQLAttribute> poAttributes = getPOAttributes(operator);
		for (IQLAttribute aoAttr : aoAttributes) {
			if (poAttributes.contains(aoAttr)) {
				result.add(aoAttr);
			}
		}
		return result;
	}

}
