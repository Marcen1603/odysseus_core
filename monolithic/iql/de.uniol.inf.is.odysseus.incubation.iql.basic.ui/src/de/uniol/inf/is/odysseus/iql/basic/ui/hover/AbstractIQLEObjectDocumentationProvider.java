package de.uniol.inf.is.odysseus.iql.basic.ui.hover;


import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.util.jdt.IJavaElementFinder;
import org.eclipse.xtext.documentation.impl.MultiLineCommentDocumentationProvider;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

import org.eclipse.jdt.internal.ui.text.javadoc.JavadocContentAccess2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public abstract class AbstractIQLEObjectDocumentationProvider<F extends IIQLTypeDictionary, U extends IIQLTypeUtils> extends MultiLineCommentDocumentationProvider {

	protected U typeUtils;
	protected F typeDictionary;
	@Inject
	protected IJavaElementFinder javaElementFinder;
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractIQLEObjectDocumentationProvider.class);

	public AbstractIQLEObjectDocumentationProvider(F typeDictionary, U typeUtils) {
		this.typeDictionary = typeDictionary;
		this.typeUtils = typeUtils;
	}
	
	@Override
	public String getDocumentation(EObject object) {
		if (object instanceof IQLArgumentsMapKeyValue) {
			return getDocumentationIQLArgumentsMapKeyValue((IQLArgumentsMapKeyValue) object);
		} else if (object instanceof JvmField) {
			return getDocumentationJvmField((JvmField) object);
		} else if (object instanceof JvmOperation) {
			return getDocumentationJvmOperation((JvmOperation) object);
		} else if (object instanceof JvmDeclaredType) {
			return getDocumentationJvmDeclaredType((JvmDeclaredType) object);
		} else if (object instanceof IQLNewExpression) {
			return getDocumentationJvmDeclaredType((IQLNewExpression) object);
		}else {
			return super.getDocumentation(object);
		}
	}
	
	protected String getDocumentationJvmDeclaredType(IQLNewExpression expr) {		
		return "";
	}
	
	protected String getDocumentationJvmDeclaredType(JvmDeclaredType type) {		
		return getJavaDoc(type);
	}
	
	
	protected String getDocumentationJvmField(JvmField attr) {		
		return getJavaDoc(attr);

	}
	
	protected String getDocumentationJvmOperation(JvmOperation method) {
		return getJavaDoc(method);
	}
	
	protected String getDocumentationIQLArgumentsMapKeyValue(IQLArgumentsMapKeyValue keyValue) {		
		return "";
	}
	
	private String getJavaDoc(JvmIdentifiableElement object) {
		IJavaElement element = javaElementFinder.findElementFor(object);
		if (element instanceof IMember && element.exists()) {
			try {
				return JavadocContentAccess2.getHTMLContent((IMember) element,true);
			} catch (CoreException e) {
				LOG.warn("Could not get java doc", e);
			}
		}
		return "";
	}
}
