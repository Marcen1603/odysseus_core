/**
 * generated by Xtext
 */
package de.uniol.inf.is.odysseus.iql.basic.scoping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;
import org.eclipse.xtext.scoping.impl.SimpleScope;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSuperExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLThisExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.IIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.TypeResult;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;


/**
 * This class contains custom scoping description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation.html#scoping
 * on how and when to use it
 */
@SuppressWarnings("restriction")
public abstract class AbstractIQLScopeProvider<T extends IIQLTypeDictionary, L extends IIQLLookUp, P extends IIQLExpressionEvaluator, U extends IIQLTypeUtils> extends AbstractDeclarativeScopeProvider implements IIQLScopeProvider {

	protected P exprEvaluator;
	
	protected U typeUtils;
	
	@Inject
	protected IQualifiedNameProvider qualifiedNameProvider;
	
	protected L lookUp;

	@Inject
	protected IQualifiedNameConverter converter;
	
	@Inject
	protected IQLClasspathTypeProviderFactory factory;
	
	@Inject
	protected IIQLCrossReferenceValidator validator;
		
	protected T typeDictionary;
	
	@Inject
	private ResourceDescriptionsProvider resourceDescriptionsProvider;
	
	public AbstractIQLScopeProvider(T typeDictionary, L lookUp, P exprEvaluator, U typeUtils) {
		this.typeDictionary = typeDictionary;
		this.lookUp = lookUp;
		this.exprEvaluator = exprEvaluator;
		this.typeUtils = typeUtils;

	}
	
	protected abstract IScope getJdtScope(ResourceSet set, IIQLJdtTypeProvider typeProvider);
	
	
	public IScope scope_IQLSimpleTypeRef_type(IQLModel model, EReference type) {	
		Collection<JvmType> types = getAllTypes(model);
				
		IQLClasspathTypeProvider provider = (IQLClasspathTypeProvider) factory.findOrCreateTypeProvider(EcoreUtil2.getResourceSet(model));
		IScope jdtTypeScope = getJdtScope(EcoreUtil2.getResourceSet(model), provider.getJdtTypeProvider());
	
		IScope parentScope =  Scopes.scopeFor(types, qualifiedNameProvider, IScope.NULLSCOPE);
		IScope scope = new IQLClasspathBasedTypeScope(provider,parentScope,jdtTypeScope, converter,resourceDescriptionsProvider, validator, model.eResource(), null);
		return new IQLImportScope(createImportNormalizers(model), scope, null, type.getEReferenceType(), true);
	}
	
	public IScope scope_IQLArrayType_componentType(IQLModel model, EReference type) {	
		Collection<JvmType> types = getAllTypes(model);

		IQLClasspathTypeProvider provider = (IQLClasspathTypeProvider) factory.findOrCreateTypeProvider(EcoreUtil2.getResourceSet(model));

		IScope jdtTypeScope = getJdtScope(EcoreUtil2.getResourceSet(model), provider.getJdtTypeProvider());
		IScope parentScope =  Scopes.scopeFor(types, qualifiedNameProvider, IScope.NULLSCOPE);
		
		IScope scope = new IQLClasspathBasedTypeScope(provider, parentScope,jdtTypeScope, converter,resourceDescriptionsProvider, validator, model.eResource(), null);
		return new IQLImportScope(createImportNormalizers(model), scope, null, type.getEReferenceType(), true);
	}
	
	private Collection<JvmType> getAllTypes(EObject node) {
		IQLModel model =  EcoreUtil2.getContainerOfType(node, IQLModel.class);		
		Collection<JvmType> types = new HashSet<>();		
		types.addAll(typeDictionary.getVisibleTypes(getUsedNamespaces(model), model.eResource()));
		return types;
	}
	


	@Override
	public Collection<IEObjectDescription> getTypes(EObject node) {
		IQLClasspathTypeProvider provider = (IQLClasspathTypeProvider) factory.findOrCreateTypeProvider(EcoreUtil2.getResourceSet(node));
		IScope jdtTypeScope = getJdtScope(EcoreUtil2.getResourceSet(node), provider.getJdtTypeProvider());
				
		IScope scope = new IQLClasspathBasedTypeScope(provider, IScope.NULLSCOPE, jdtTypeScope, converter,resourceDescriptionsProvider, validator, node.eResource(),null);
		Map<QualifiedName, IEObjectDescription> result = new HashMap<>();
		for (IEObjectDescription e : scope.getAllElements()) {
			result.put(e.getQualifiedName(), e);
		}		
		
		for (JvmType type : getAllTypes(node)) {
			QualifiedName qName = converter.toQualifiedName(typeUtils.getLongName(type, false));
			if (!result.containsKey(qName)) {
				result.put(qName, EObjectDescription.create(qName, type));
			}
		}
		return result.values();
	}
	
	public IScope scope_IQLJvmElementCallExpression_element(IQLJvmElementCallExpression expr, EReference type) {
		Collection<IEObjectDescription> elements = getScopeIQLJvmElementCallExpression(expr);
		return new SimpleScope(elements);
	}
	
	protected Collection<JvmIdentifiableElement> getElementsIQLJvmElementCallExpression(EObject expr) {
		Collection<JvmIdentifiableElement> elements = new HashSet<>();
		Set<String> vars = new HashSet<>();
		EObject container = expr;
		EObject lastContainer = null;
		while (container != null && !(container instanceof JvmDeclaredType)) {
			for (EObject obj : container.eContents()) {
				if (container instanceof IQLStatementBlock && obj == lastContainer) {
					break;
				}
				if (obj instanceof IQLVariableDeclaration) {
					IQLVariableDeclaration var = (IQLVariableDeclaration) obj;
					if (!vars.contains(var.getName())) {
						vars.add(var.getName());
						elements.add(var);
					}
				} else if (obj instanceof JvmFormalParameter) {
					JvmFormalParameter parameter = (JvmFormalParameter) obj;
					if (!vars.contains(parameter.getName())) {
						vars.add(parameter.getName());
						elements.add(parameter);
					}
				} else if (obj instanceof IQLVariableStatement) {
					for (IQLVariableDeclaration var : EcoreUtil2.getAllContentsOfType(obj, IQLVariableDeclaration.class)) {
						if (!vars.contains(var.getName())) {
							vars.add(var.getName());
							elements.add(var);
						}
					}
					for (JvmFormalParameter parameter : EcoreUtil2.getAllContentsOfType(obj, JvmFormalParameter.class)) {
						if (!vars.contains(parameter.getName())) {
							vars.add(parameter.getName());
							elements.add(parameter);
						}
					}
				}					
			}
			
			lastContainer = container;
			container = container.eContainer();
		}
		
		Collection<JvmTypeReference> importedTypes = typeDictionary.getStaticImports(expr);
		
		JvmTypeReference superType = lookUp.getSuperType(expr);

		elements.addAll(lookUp.getPublicAttributes(superType, importedTypes, true));
		elements.addAll(lookUp.getProtectedAttributes(superType, importedTypes, true));
		
		elements.addAll(lookUp.getPublicMethods(superType, importedTypes,true));
		elements.addAll(lookUp.getProtectedMethods(superType, importedTypes,true));

		return elements;
	}
	
	@Override
	public Collection<IEObjectDescription> getScopeIQLJvmElementCallExpression(EObject expr) {
		Collection<JvmIdentifiableElement> elements = getElementsIQLJvmElementCallExpression(expr);
		
		Collection<IEObjectDescription> result = new HashSet<>();
		for (JvmIdentifiableElement element : elements) {
			if (element instanceof JvmOperation) {
				JvmOperation method = (JvmOperation) element;
				if (method.getSimpleName().startsWith("set")) {
					result.add(EObjectDescription.create(firstCharLowerCase(method.getSimpleName().substring(3)), method));
				} else if (method.getSimpleName().startsWith("get")) {
					result.add(EObjectDescription.create(firstCharLowerCase(method.getSimpleName().substring(3)), method));
				} else if (method.getSimpleName().startsWith("is")) {
					result.add(EObjectDescription.create(firstCharLowerCase(method.getSimpleName().substring(2)), method));
				}
				if (method.isStatic()) {
					JvmDeclaredType declaredType = (JvmDeclaredType) method.eContainer();
					result.add(EObjectDescription.create(declaredType.getSimpleName()+IQLQualifiedNameConverter.DELIMITER+qualifiedNameProvider.getFullyQualifiedName(method), method));
				}
			} else if (element instanceof JvmField) {
				JvmField field = (JvmField) element;
				if (field.isStatic()) {
					JvmDeclaredType declaredType = (JvmDeclaredType) element.eContainer();
					result.add(EObjectDescription.create(declaredType.getSimpleName()+IQLQualifiedNameConverter.DELIMITER+qualifiedNameProvider.getFullyQualifiedName(field), field));
				}
			} 
			result.add(EObjectDescription.create(qualifiedNameProvider.getFullyQualifiedName(element), element));
		}
		return result;		
	}	
	
	
	public IScope scope_IQLMemberCallExpression_member(IQLMemberSelectionExpression expr, EReference type) {		
		Collection<IEObjectDescription> elements = new HashSet<>();
		elements.addAll(getScopeIQLMemberSelection(expr));		
		return new SimpleScope(elements);
	}
	
	public IScope scope_IQLArgumentsMapKeyValue_key(IQLArgumentsMap argumentsMap, EReference type) {	
		Collection<IEObjectDescription> elements = new HashSet<>();
		elements.addAll(getScopeIQLArgumentsMapKey(argumentsMap));		
		return new SimpleScope(elements);
	}	

	
	@Override
	public Collection<IEObjectDescription> getScopeIQLMemberSelection(IQLMemberSelectionExpression expr) {
		Collection<IEObjectDescription> result = new HashSet<>();
		TypeResult typeResult = exprEvaluator.eval(expr.getLeftOperand());
		
		if (typeUtils.isArray(typeResult.getRef())) {
			result.addAll(getScopeIQLAttributeSelection(typeUtils.createTypeRef(List.class, typeDictionary.getSystemResourceSet()), false, false));
			result.addAll(getScopeIQLMethodSelection(typeUtils.createTypeRef(List.class, typeDictionary.getSystemResourceSet()), false, false));

		} else {
			boolean isThis = expr.getLeftOperand() instanceof IQLThisExpression;
			boolean isSuper = expr.getLeftOperand() instanceof IQLSuperExpression;
			
			result.addAll(getScopeIQLAttributeSelection(typeResult.getRef(),isThis, isSuper));
			result.addAll(getScopeIQLMethodSelection(typeResult.getRef(),isThis, isSuper));

		}
		return result;
	}

	protected Collection<IEObjectDescription> getScopeIQLAttributeSelection(JvmTypeReference typeRef, boolean isThis, boolean isSuper) {
		Collection<JvmField> attributes = null;
		attributes = lookUp.getPublicAttributes(typeRef, true);
		if (isThis || isSuper) {
			attributes.addAll(lookUp.getProtectedAttributes(typeRef, true));
		}
		Collection<IEObjectDescription> result = new HashSet<>();
		for (JvmField attribute : attributes) {
			result.add(EObjectDescription.create(qualifiedNameProvider.getFullyQualifiedName(attribute), attribute));
		}
		return result;
	}
	
	
	protected Collection<IEObjectDescription> getScopeIQLMethodSelection(JvmTypeReference typeRef, boolean isThis, boolean isSuper) {
		Collection<JvmOperation> methods = null;
		methods = lookUp.getPublicMethods(typeRef, true);
		if (isThis || isSuper) {
			methods.addAll(lookUp.getProtectedMethods(typeRef, true));
		}
		Map<String, Pair<JvmOperation, JvmOperation>> properties = new HashMap<>();
		Collection<IEObjectDescription> result = new HashSet<>();
		for (JvmOperation method : methods) {
			if (typeUtils.isSetter(method)) {
				String name = typeUtils.getNameWithoutSetterPrefix(method);
				Pair<JvmOperation, JvmOperation> pair = properties.get(name);
				if (pair == null) {
					pair = new Pair<JvmOperation, JvmOperation>();
					properties.put(name, pair);
				}
				pair.setE1(method);
			} else if (typeUtils.isGetter(method)) {
				String name = typeUtils.getNameWithoutGetterPrefix(method);
				Pair<JvmOperation, JvmOperation> pair = properties.get(name);
				if (pair == null) {
					pair = new Pair<JvmOperation, JvmOperation>();
					properties.put(name, pair);
				}
				pair.setE2(method);
			} 
			result.add(EObjectDescription.create(qualifiedNameProvider.getFullyQualifiedName(method), method));
		}
		for (Entry<String, Pair<JvmOperation, JvmOperation>> entry : properties.entrySet()) {
			if (entry.getValue().getE1() != null && entry.getValue().getE2() != null) {
				String name = firstCharLowerCase(entry.getKey());
				result.add(EObjectDescription.create(name, entry.getValue().getE1()));
				result.add(EObjectDescription.create(name, entry.getValue().getE2()));
			}
		}
		return result;
	}
	

	
	
	@Override
	public Collection<IEObjectDescription> getScopeIQLArgumentsMapKey(EObject node) {
		JvmTypeReference typeRef = null;
		if (EcoreUtil2.getContainerOfType(node, IQLNewExpression.class) != null) {
			IQLNewExpression expr = EcoreUtil2.getContainerOfType(node, IQLNewExpression.class);
			typeRef = expr.getRef();
		} else if (EcoreUtil2.getContainerOfType(node, IQLAttribute.class) != null) {
			IQLAttribute attr = EcoreUtil2.getContainerOfType(node, IQLAttribute.class);
			typeRef = attr.getType();
		} else if (EcoreUtil2.getContainerOfType(node, IQLVariableStatement.class) != null) {
			IQLVariableStatement stmt = EcoreUtil2.getContainerOfType(node, IQLVariableStatement.class);
			typeRef = ((IQLVariableDeclaration)stmt.getVar()).getRef();
		}
				
		Collection<IEObjectDescription> result = new ArrayList<>();
		if (typeRef != null) {
			Collection<JvmField> attributes = lookUp.getPublicAttributes(typeRef, false);
			for (JvmField attr : attributes) {
				result.add(EObjectDescription.create(attr.getSimpleName(), attr));
			}
			Collection<JvmOperation> setters = lookUp.getPublicSetters(typeRef);
			for (JvmOperation op : setters) {
				String name = op.getSimpleName().substring(3);
				result.add(EObjectDescription.create(firstCharLowerCase(name), op));
			}	
		}
		return result;
	}
	

	
	protected String firstCharLowerCase(String s) {
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}
	
	private Collection<String> getUsedNamespaces(EObject obj) {
		Collection<String> namespaces = new HashSet<>();
		IQLModel model = EcoreUtil2.getContainerOfType(obj, IQLModel.class);
		namespaces.addAll(typeDictionary.getImplicitImports());
		for (IQLNamespace namespace : model.getNamespaces()) {
			String name = namespace.getImportedNamespace();
			namespaces.add(name);
		}
		return namespaces;
	}
	
	protected List<ImportNormalizer> createImportNormalizers(EObject obj) {
		IQLModel model = EcoreUtil2.getContainerOfType(obj, IQLModel.class);
		List<ImportNormalizer> result = new ArrayList<>();	
		Set<QualifiedName> imports = new HashSet<>();	
		Set<QualifiedName> wildCards = new TreeSet<>();	
		for (IQLNamespace namespace : model.getNamespaces()) {
			String name = namespace.getImportedNamespace();
			boolean wildcard = name.endsWith("*");
			if (wildcard) {
				name = name.substring(0, name.lastIndexOf(IQLQualifiedNameConverter.DELIMITER+"*"));
				wildCards.add(converter.toQualifiedName(name));
			} else {
				imports.add(converter.toQualifiedName(name));
			}
		}
		
		Collection<String> implicitImports = getImplicitImports(obj);
		for (String name : implicitImports) {
			boolean wildcard = name.endsWith("*");
			if (wildcard) {
				name = name.substring(0, name.lastIndexOf(IQLQualifiedNameConverter.DELIMITER+"*"));
				wildCards.add(converter.toQualifiedName(name));
			} else {
				imports.add(converter.toQualifiedName(name));
			}
		}
		
		for (QualifiedName name : imports) {
			if (!wildCards.contains(name.skipLast(1))) {
				result.add(new IQLImportNormalizer(name, false, true));
			} 
		}
		
		for (QualifiedName name : wildCards) {
			result.add(new IQLImportNormalizer(name, true, true));
		}
		
		return result;
	} 
	
	protected Collection<String> getImplicitImports(EObject obj) {
		Collection<String> result = new HashSet<>();
		result.addAll(typeDictionary.getImplicitImports());
		return result;
	}
}
