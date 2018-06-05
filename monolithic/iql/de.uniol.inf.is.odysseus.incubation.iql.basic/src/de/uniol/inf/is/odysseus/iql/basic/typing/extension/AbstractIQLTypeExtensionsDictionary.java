package de.uniol.inf.is.odysseus.iql.basic.typing.extension;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmAnnotationReference;
import org.eclipse.xtext.common.types.JvmAnnotationValue;
import org.eclipse.xtext.common.types.JvmBooleanAnnotationValue;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.linking.IIQLMethodFinder;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.service.IIQLService;
import de.uniol.inf.is.odysseus.iql.basic.service.IQLServiceBinding;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLDefaultTypes;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public abstract class AbstractIQLTypeExtensionsDictionary<F extends IIQLTypeDictionary, U extends IIQLTypeUtils> implements IIQLTypeExtensionsDictionary, IQLServiceBinding.Listener {
	private Map<String, Collection<IIQLTypeExtensions>> typeExtensions = new HashMap<>();
	private Map<IIQLTypeExtensions, JvmTypeReference> typeExtensionsRefs = new HashMap<>();

	protected F typeDictionary;
	protected U typeUtils;

	@Inject
	protected IIQLMethodFinder methodFinder;
	
	@Inject
	protected IIQLLookUp lookUp;
	
	@Inject
	protected IQLQualifiedNameConverter converter;
	

	public AbstractIQLTypeExtensionsDictionary(F typeDictionary, U typeUtils) {
		this.typeDictionary = typeDictionary;
		this.typeUtils = typeUtils;
		this.init();
	}
	

	private void init() {
		for (IIQLTypeExtensions extensions : IQLDefaultTypes.getTypeExtensions()) {
			this.addTypeExtensions(extensions);
		}
		IQLServiceBinding.getInstance().addListener(this);
		for (IIQLService service : IQLServiceBinding.getInstance().getServices()) {
			onServiceAdded(service);
		}
	}

	@Override
	public void addTypeExtensions(IIQLTypeExtensions extensions) {
		Collection<IIQLTypeExtensions> col = this.typeExtensions.get(extensions.getType().getCanonicalName());
		if (col == null) {
			col = new HashSet<>();
			this.typeExtensions.put(extensions.getType().getCanonicalName(), col);
		}
		col.add(extensions);
		JvmTypeReference typeRef = typeUtils.createTypeRef(extensions.getClass(), typeDictionary.getSystemResourceSet());
		JvmType innerType = typeUtils.getInnerType(typeRef, false);
		for (JvmOperation method : EcoreUtil2.getAllContentsOfType(innerType, JvmOperation.class)) {
			if (method.isStatic() && ignoreFirstParameter(method) && (method.getVisibility().getValue() == JvmVisibility.PUBLIC_VALUE || (method.getVisibility().getValue() == JvmVisibility.PROTECTED_VALUE))) {
				method.getParameters().remove(0);
			}
		}
		this.typeExtensionsRefs.put(extensions, typeRef);
	}
	
	@Override
	public boolean ignoreFirstParameter(JvmOperation method) {
		if (method.getAnnotations().size() > 0) {
			for (JvmAnnotationReference anno : method.getAnnotations()) {
				if (anno.getAnnotation().getSimpleName().equals(ExtensionMethod.class.getSimpleName())) {
					for (JvmAnnotationValue value : anno.getValues()) {
						JvmBooleanAnnotationValue v = (JvmBooleanAnnotationValue) value;
						return v.getValues().get(0);
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public void removeTypeExtensions(IIQLTypeExtensions extensions) {
		Collection<IIQLTypeExtensions> col = this.typeExtensions.get(extensions.getType().getCanonicalName());
		if (col != null) {
			col.remove(extensions);
			if (col.isEmpty()) {
				this.typeExtensions.remove(extensions.getType().getCanonicalName());
			}
		}
	}
	
	
	@Override
	public Collection<JvmField> getAllExtensionAttributes(JvmTypeReference typeRef, int[] visibilities) {
		String name = converter.toJavaString(typeUtils.getLongName(typeRef, true));
		Collection<IIQLTypeExtensions> col = typeExtensions.get(name);
		Collection<JvmField> result = new HashSet<>();
		for (IIQLTypeExtensions extensions : col) {
			JvmTypeReference extensionsTypeRef = typeExtensionsRefs.get(extensions);
			JvmType innerType = typeUtils.getInnerType(extensionsTypeRef, false);
			if (extensionsTypeRef != null) {
				for (JvmField field : EcoreUtil2.getAllContentsOfType(innerType, JvmField.class)) {
					if (field.isStatic()) {
						int visibility = field.getVisibility().getValue();
						for (int v : visibilities) {
							if (visibility == v) {
								result.add(field);
								break;
							}
						}
					}
				}
			}
		}		
		return result;
	}

	@Override
	public Collection<JvmOperation> getAllExtensionMethods(JvmTypeReference typeRef, int[] visibilities) {
		String name = converter.toJavaString(typeUtils.getLongName(typeRef, true));
		Collection<IIQLTypeExtensions> col = typeExtensions.get(name);
		Collection<JvmOperation> result = new HashSet<>();		
		for (IIQLTypeExtensions extensions : col) {
			JvmTypeReference extensionsTypeRef = typeExtensionsRefs.get(extensions);
			JvmType innerType = typeUtils.getInnerType(extensionsTypeRef, false);
			if (extensionsTypeRef != null) {
				for (JvmOperation op : EcoreUtil2.getAllContentsOfType(innerType, JvmOperation.class)) {
					if (op.isStatic()) {
						int visibility = op.getVisibility().getValue();
						for (int v : visibilities) {
							if (visibility == v) {
								result.add(op);
								break;
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	@Override
	public boolean hasTypeExtensions(JvmTypeReference typeRef) {
		return getTypeExtensions(typeRef)!= null;
	}
	
	
	@Override
	public IIQLTypeExtensions getTypeExtensions(JvmTypeReference typeRef) {
		String name = converter.toJavaString(typeUtils.getLongName(typeRef, true));
		Collection<IIQLTypeExtensions> col = typeExtensions.get(name);
		if (col != null) {
			for (IIQLTypeExtensions extensions : col) {
				if (extensions.getType().getCanonicalName().equalsIgnoreCase(name)) {
					return extensions;
				}
			}
		}
		return null;
	}

	@Override
	public IIQLTypeExtensions getTypeExtensions(JvmTypeReference typeRef, String attribute) {
		return findTypeExtensions(typeRef, attribute);
	}

	@Override
	public boolean hasTypeExtensions(JvmTypeReference typeRef, String attribute) {
		return getTypeExtensions(typeRef, attribute) != null;
	}

	
	protected JvmTypeReference getExtendedType(JvmDeclaredType declaredType) {
		return declaredType.getExtendedClass();
	}
	
	private IIQLTypeExtensions findTypeExtensions(JvmTypeReference typeRef, String attribute) {
		if (typeUtils.isArray(typeRef)) {
			typeRef = typeUtils.createTypeRef(List.class, typeDictionary.getSystemResourceSet());
		}
		
		JvmType innerType = typeUtils.getInnerType(typeRef, true);

		Collection<IIQLTypeExtensions> col = typeExtensions.get(converter.toJavaString(typeUtils.getLongName(innerType, true)));
		if (col != null) {
			for (IIQLTypeExtensions extensions : col) {
				try {
					Field field = extensions.getClass().getDeclaredField(attribute);
					if (field.getName().equalsIgnoreCase(attribute)) {
						return extensions;
					}
				} catch (NoSuchFieldException | SecurityException e) {
				}
			}
		}

		if (innerType instanceof JvmDeclaredType) {
			JvmDeclaredType declaredType = (JvmDeclaredType) innerType;
			for (JvmTypeReference interf : declaredType.getExtendedInterfaces()) {
				IIQLTypeExtensions extensions = findTypeExtensions(interf, attribute);
				if (extensions != null) {
					return extensions;
				}
			}

			JvmTypeReference extendedType = getExtendedType(declaredType);
			if (extendedType!= null) {
				return findTypeExtensions(extendedType, attribute);
			}
			
			boolean isObject = converter.toJavaString(typeUtils.getLongName(declaredType, true)).equals(Object.class.getCanonicalName());
			if (!isObject && declaredType.getExtendedClass() == null && !declaredType.getExtendedInterfaces().iterator().hasNext()) {
				return findTypeExtensions(typeUtils.createTypeRef(Object.class, typeDictionary.getSystemResourceSet()), attribute);
			}
		}
		return null;
	}
		
	private IIQLTypeExtensions findTypeExtensions(JvmTypeReference typeRef, String methodName, List<IQLExpression> arguments) {
		if (typeUtils.isArray(typeRef)) {
			typeRef = typeUtils.createTypeRef(List.class, typeDictionary.getSystemResourceSet());
		}
		
		JvmType innerType = typeUtils.getInnerType(typeRef, false);
		
		Collection<IIQLTypeExtensions> col = typeExtensions.get(converter.toJavaString(typeUtils.getLongName(innerType, true)));

		if (col != null) {
			for (IIQLTypeExtensions extensions : col) {
				JvmTypeReference extRef = typeExtensionsRefs.get(extensions);
				Collection<JvmOperation> methods = lookUp.getDeclaredPublicMethods(extRef, true);
				JvmOperation op = methodFinder.findMethod(methods, methodName, arguments);
				if (op != null) {
					return extensions;
				}				
			}
		}

		if (innerType instanceof JvmDeclaredType) {
			JvmDeclaredType declaredType = (JvmDeclaredType) innerType;
			for (JvmTypeReference interf : declaredType.getExtendedInterfaces()) {
				IIQLTypeExtensions extensions = findTypeExtensions(interf, methodName, arguments);
				if (extensions != null) {
					return extensions;
				}
			}
			JvmTypeReference extendedType = getExtendedType(declaredType);
			if (extendedType!= null) {
				return findTypeExtensions(extendedType, methodName, arguments);
			}
			
			boolean isObject = converter.toJavaString(typeUtils.getLongName(declaredType, true)).equals(Object.class.getCanonicalName());
			if (!isObject && declaredType.getExtendedClass() == null && !declaredType.getExtendedInterfaces().iterator().hasNext()) {
				return findTypeExtensions(typeUtils.createTypeRef(Object.class, typeDictionary.getSystemResourceSet()), methodName, arguments);
			}
		}
		return null;
	}


	
	@Override
	public IIQLTypeExtensions getTypeExtensions(JvmTypeReference typeRef,String method, List<IQLExpression> arguments) {
		return findTypeExtensions(typeRef, method, arguments);
	}

	@Override
	public boolean hasTypeExtensions(JvmTypeReference typeRef, String method,IQLExpression argument) {
		return getTypeExtensions(typeRef, method, argument)!= null;
	}


	@Override
	public boolean hasTypeExtensions(JvmTypeReference typeRef, String method,List<IQLExpression> arguments) {
		return getTypeExtensions(typeRef, method, arguments)!= null;
	}


	@Override
	public IIQLTypeExtensions getTypeExtensions(JvmTypeReference typeRef,	String method, IQLExpression argument) {
		return getTypeExtensions(typeRef, method, Collections.singletonList(argument));
	}



	@Override
	public JvmOperation getMethod(JvmTypeReference typeRef, String method,	IQLExpression argument) {
		return getMethod(typeRef, method, Collections.singletonList(argument));
	}


	@Override
	public JvmOperation getMethod(JvmTypeReference typeRef, String method,	List<IQLExpression> arguments) {
		JvmTypeReference ext = typeExtensionsRefs.get(findTypeExtensions(typeRef, method, arguments));
		JvmDeclaredType type = (JvmDeclaredType)  typeUtils.getInnerType(ext, false);
		Collection<JvmOperation> methods = lookUp.getDeclaredPublicMethods(typeUtils.createTypeRef(type), true);
		return methodFinder.findMethod(methods, method, arguments);
	}


	@Override
	public void onServiceAdded(IIQLService service) {
		if (service.getTypeExtensions() != null) {
			for (IIQLTypeExtensions typeExt : service.getTypeExtensions()) {
				this.addTypeExtensions(typeExt);
			}
		}
	}


	@Override
	public void onServiceRemoved(IIQLService service) {
		if (service.getTypeExtensions() != null) {
			for (IIQLTypeExtensions typeExt : service.getTypeExtensions()) {
				this.removeTypeExtensions(typeExt);
			}
		}
	}

}
