package de.uniol.inf.is.odysseus.iql.qdl.lookup;


import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.inject.Inject;





















import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.lookup.AbstractIQLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceBinding;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.AbstractQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.Operator;
import de.uniol.inf.is.odysseus.iql.qdl.types.source.Source;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.typeextension.IQDLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class QDLLookUp extends AbstractIQLLookUp<IQDLTypeDictionary, IQDLTypeExtensionsDictionary, IQDLTypeUtils> implements IQDLLookUp{

	private static final Logger LOG = LoggerFactory.getLogger(QDLLookUp.class);

	@Inject
	public QDLLookUp(IQDLTypeDictionary typeDictionary, IQDLTypeExtensionsDictionary typeExtensionsDictionary, IQDLTypeUtils typeUtils) {
		super(typeDictionary, typeExtensionsDictionary, typeUtils);
	}
	
	@Override
	public JvmTypeReference getThisType(EObject node) {
		QDLQuery query = EcoreUtil2.getContainerOfType(node, QDLQuery.class);
		if (node instanceof QDLQuery || query != null) {
			return typeUtils.createTypeRef(AbstractQDLQuery.class, typeDictionary.getSystemResourceSet());
		} else {
			return super.getSuperType(node);
		}
	}
	
	@Override
	public JvmTypeReference getSuperType(EObject node) {
		QDLQuery query = EcoreUtil2.getContainerOfType(node, QDLQuery.class);
		if (node instanceof QDLQuery || query != null) {
			return typeUtils.createTypeRef(AbstractQDLQuery.class, typeDictionary.getSystemResourceSet());
		} else {
			return super.getSuperType(node);
		}
	}

	@Override
	public Collection<String> getMetadataKeys() {
		Collection<String> result = new HashSet<>();
		for (IPreParserKeywordProvider provider : QDLServiceBinding.getPreParserKeywordProviders()) {
			for (Entry<String, Class<? extends IPreParserKeyword>> entry : provider.getKeywords().entrySet()) {
				result.add(entry.getKey());
			}
		}
		result.add(IQDLLookUp.AUTO_CREATE);
		return result;
	}

	@Override
	public Collection<String> getMetadataValues(String key) {
		Collection<String> result = new HashSet<>();
		if (key.equalsIgnoreCase(IQDLLookUp.AUTO_CREATE)) {
			result.add("true");
			result.add("false");
		} else {
			for (IPreParserKeywordProvider provider : QDLServiceBinding.getPreParserKeywordProviders()) {
				for (Entry<String, Class<? extends IPreParserKeyword>> entry : provider.getKeywords().entrySet()) {
					if (key.equalsIgnoreCase(entry.getKey())) {
						try {
							Collection<String> parameters = entry.getValue().newInstance().getAllowedParameters(OdysseusRCPPlugIn.getActiveSession());
							if (parameters != null) {
								result.addAll(parameters);
							}
						} catch (InstantiationException	| IllegalAccessException e) {
							LOG.error("", e);
						}
					}
				}
			}
		}
		return result;
	}
	
	@Override
	public boolean isOperator(JvmTypeReference typeRef) {
		if (typeUtils.getInnerType(typeRef, true) instanceof IQLClass) {
			JvmDeclaredType clazz = (JvmDeclaredType) typeUtils.getInnerType(typeRef, true);
			JvmTypeReference operator = typeUtils.createTypeRef(Operator.class, typeDictionary.getSystemResourceSet());
			return isAssignable(operator, clazz);			
		} 
		return false;	
	}

	@Override
	public boolean isSource(JvmTypeReference typeRef) {
		if (typeUtils.getInnerType(typeRef, true) instanceof IQLClass) {
			JvmDeclaredType clazz = (JvmDeclaredType) typeUtils.getInnerType(typeRef, true);
			JvmTypeReference source = typeUtils.createTypeRef(Source.class, typeDictionary.getSystemResourceSet());
			return isAssignable(source, clazz);	
		} 
		return false;
	}

//	@Override
//	protected Collection<IQLModel> getAllFiles(Resource context) {
//		Collection<IQLModel> files = new HashSet<>();
//		for (IResourceDescription res : resources.getAllResourceDescriptions()) {
//			Resource r = EcoreUtil2.getResource(context, res.getURI().toString());
//			if (r.getContents().size() > 0) {
//				EObject obj = r.getContents().get(0);
//				if (obj instanceof QDLModel) {
//					files.add((IQLModel)obj);
//				}
//			}
//		}
//		return files;
//	}
}
