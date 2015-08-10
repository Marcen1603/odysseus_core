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
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLMethodFinder;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLDefaultTypes;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public abstract class AbstractIQLTypeExtensionsFactory<F extends IIQLTypeFactory, U extends IIQLTypeUtils> implements IIQLTypeExtensionsFactory {
	private Map<String, Collection<IIQLTypeExtensions>> typeExtensions = new HashMap<>();
	private Map<IIQLTypeExtensions, JvmTypeReference> typeExtensionsRefs = new HashMap<>();

	protected F typeFactory;
	protected U typeUtils;

	@Inject
	protected IIQLMethodFinder methodFinder;

	public AbstractIQLTypeExtensionsFactory(F typeFactory, U typeUtils) {
		this.typeFactory = typeFactory;
		this.typeUtils = typeUtils;
		this.init();
	}
	

	private void init() {
		for (IIQLTypeExtensions extensions : IQLDefaultTypes.getTypeExtensions()) {
			this.addTypeExtensions(extensions);
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
		JvmTypeReference typeRef = typeUtils.createTypeRef(extensions.getClass(), typeFactory.getSystemResourceSet());
		JvmType innerType = typeUtils.getInnerType(typeRef, false);
		for (JvmOperation method : EcoreUtil2.getAllContentsOfType(innerType, JvmOperation.class)) {
			if (method.isStatic() && (method.getVisibility().getValue() == JvmVisibility.PUBLIC_VALUE || (method.getVisibility().getValue() == JvmVisibility.PROTECTED_VALUE))) {
				method.getParameters().remove(0);
			}
		}
		this.typeExtensionsRefs.put(extensions, typeRef);
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
		String name = typeUtils.getLongName(typeRef, true);
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
		String name = typeUtils.getLongName(typeRef, true);
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
		String name = typeUtils.getLongName(typeRef, true);
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
	public IIQLTypeExtensions getTypeExtensions(JvmTypeReference typeRef,String method, int args) {
		return findTypeExtensions(typeRef, method, args);
	}

	
	@Override
	public boolean hasTypeExtensions(JvmTypeReference typeRef, String attribute) {
		return getTypeExtensions(typeRef, attribute) != null;
	}

	@Override
	public boolean hasTypeExtensions(JvmTypeReference typeRef, String method,int args) {
		return getTypeExtensions(typeRef, method, args) != null;
	}
	
	private IIQLTypeExtensions findTypeExtensions(JvmTypeReference typeRef, String attribute) {
		JvmType innerType = typeUtils.getInnerType(typeRef, true);

		String name = typeUtils.getLongName(innerType, true);
		Collection<IIQLTypeExtensions> col = typeExtensions.get(name);
		if (col != null) {
			for (IIQLTypeExtensions extensions : col) {
				Class<?> type = extensions.getType();
				if (type.getCanonicalName().equalsIgnoreCase(name)) {
					try {
						Field field = extensions.getClass().getDeclaredField(attribute);
						if (field.getName().equalsIgnoreCase(attribute)) {
							return extensions;
						}
					} catch (NoSuchFieldException | SecurityException e) {
					}
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
			if (declaredType.getExtendedClass() != null) {
				return findTypeExtensions(declaredType.getExtendedClass(), attribute);
			}
			
			boolean isObject = typeUtils.getLongName(declaredType, true).equals(Object.class.getCanonicalName());
			if (!isObject && declaredType.getExtendedClass() == null && !declaredType.getExtendedInterfaces().iterator().hasNext()) {
				return findTypeExtensions(typeUtils.createTypeRef(Object.class, typeFactory.getSystemResourceSet()), attribute);
			}
		}
		return null;
	}
	
	private IIQLTypeExtensions findTypeExtensions(JvmTypeReference typeRef, String method, int args) {
		JvmType innerType = typeUtils.getInnerType(typeRef, true);

		String name = typeUtils.getLongName(innerType, true);
		Collection<IIQLTypeExtensions> col = typeExtensions.get(name);

		if (col != null) {
			for (IIQLTypeExtensions extensions : col) {
				JvmTypeReference typeExtRef = typeExtensionsRefs.get(extensions);
				JvmOperation op = methodFinder.findDeclaredMethod(typeExtRef, method, args-1);
				if (op != null) {
					return extensions;
				}
			}
		}

		if (innerType instanceof JvmDeclaredType) {
			JvmDeclaredType declaredType = (JvmDeclaredType) innerType;
			for (JvmTypeReference interf : declaredType.getExtendedInterfaces()) {
				IIQLTypeExtensions extensions = findTypeExtensions(interf, method, args);
				if (extensions != null) {
					return extensions;
				}
			}
			if (declaredType.getExtendedClass() != null) {
				return findTypeExtensions(declaredType.getExtendedClass(), method, args);
			}
			
			boolean isObject = typeUtils.getLongName(declaredType, true).equals(Object.class.getCanonicalName());
			if (!isObject && declaredType.getExtendedClass() == null && !declaredType.getExtendedInterfaces().iterator().hasNext()) {
				return findTypeExtensions(typeUtils.createTypeRef(Object.class, typeFactory.getSystemResourceSet()), method, args);
			}
		}
		return null;
	}
	
	private IIQLTypeExtensions findTypeExtensions(JvmTypeReference typeRef, String methodName, List<IQLExpression> arguments) {
		JvmType innerType = typeUtils.getInnerType(typeRef, true);

		String name = typeUtils.getLongName(innerType, true);
		Collection<IIQLTypeExtensions> col = typeExtensions.get(name);

		if (col != null) {
			for (IIQLTypeExtensions extensions : col) {
				JvmOperation op = methodFinder.findDeclaredMethod(typeExtensionsRefs.get(extensions), methodName, arguments);
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
			if (declaredType.getExtendedClass() != null) {
				return findTypeExtensions(declaredType.getExtendedClass(), methodName, arguments);
			}
			
			boolean isObject = typeUtils.getLongName(declaredType, true).equals(Object.class.getCanonicalName());
			if (!isObject && declaredType.getExtendedClass() == null && !declaredType.getExtendedInterfaces().iterator().hasNext()) {
				return findTypeExtensions(typeUtils.createTypeRef(Object.class, typeFactory.getSystemResourceSet()), methodName, arguments);
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
	public JvmOperation getMethod(JvmTypeReference typeRef, String method, int args) {
		JvmTypeReference ext = typeExtensionsRefs.get(findTypeExtensions(typeRef, method, args));
		JvmDeclaredType type = (JvmDeclaredType)  typeUtils.getInnerType(ext, false);
		for (JvmOperation op : type.getDeclaredOperations()) {
			if (op.getSimpleName().equalsIgnoreCase(method) && op.getParameters().size() == args-1) {
				return op;
			}
		}
		return null;
	}


	@Override
	public JvmOperation getMethod(JvmTypeReference typeRef, String method,	IQLExpression argument) {
		return getMethod(typeRef, method, Collections.singletonList(argument));
	}


	@Override
	public JvmOperation getMethod(JvmTypeReference typeRef, String method,	List<IQLExpression> arguments) {
		JvmTypeReference ext = typeExtensionsRefs.get(findTypeExtensions(typeRef, method, arguments));
		JvmDeclaredType type = (JvmDeclaredType)  typeUtils.getInnerType(ext, false);
		return methodFinder.findDeclaredMethod(type, method, arguments);
	}

}
