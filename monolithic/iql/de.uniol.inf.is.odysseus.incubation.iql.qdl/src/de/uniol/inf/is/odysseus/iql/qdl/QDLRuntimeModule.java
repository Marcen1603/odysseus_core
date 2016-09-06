/*
 * generated by Xtext
 */
package de.uniol.inf.is.odysseus.iql.qdl;

import org.eclipse.xtext.common.types.xtext.ClasspathBasedTypeScopeProvider;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.linking.ILinkingService;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceFactory;

import de.uniol.inf.is.odysseus.iql.basic.executor.IIQLExecutor;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.IIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.context.IIQLExpressionEvaluatorContext;
import de.uniol.inf.is.odysseus.iql.basic.generator.IIQLGenerator;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.linking.IIQLMethodFinder;
import de.uniol.inf.is.odysseus.iql.basic.linking.IQLLinkingResource;
import de.uniol.inf.is.odysseus.iql.basic.linking.IQLLinkingService;
import de.uniol.inf.is.odysseus.iql.basic.linking.IQLResourceFactory;
import de.uniol.inf.is.odysseus.iql.basic.linking.SimpleIQLMethodFinder;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLCrossReferenceValidator;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLCrossReferenceValidator;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLNullJdtTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.service.IIQLServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.IIQLTypeBuilder;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.entrypoint.IIQLTypingEntryPoint;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.qdl.executor.QDLExecutor;
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.IQDLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.IQDLExpressionEvaluatorContext;
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.QDLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.QDLExpressionEvaluatorContext;
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGenerator;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.IQDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.QDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.IQDLScopeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLClasspathTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLQualifiedNameProvider;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLResourceDescriptionStrategy;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLScopeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceObserver;
import de.uniol.inf.is.odysseus.iql.qdl.typing.builder.IQDLTypeBuilder;
import de.uniol.inf.is.odysseus.iql.qdl.typing.builder.QDLTypeBuilder;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.QDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.entrypoint.QDLTypingEntryPoint;
import de.uniol.inf.is.odysseus.iql.qdl.typing.typeextension.IQDLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.typeextension.QDLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.QDLTypeUtils;


/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
@SuppressWarnings({"restriction", "rawtypes"})
public class QDLRuntimeModule extends de.uniol.inf.is.odysseus.iql.qdl.AbstractQDLRuntimeModule {

	public Class<? extends IIQLGenerator> bindIQLGenerator() {
		return QDLGenerator.class;
	}
	
	public Class<? extends IIQLCrossReferenceValidator> bindCrossReferenceValidator() {
		return IQLCrossReferenceValidator.class;
	}
	
	public Class<? extends IDefaultResourceDescriptionStrategy> bindResourceDescriptionStrategy() {
		return QDLResourceDescriptionStrategy.class;
	}
	
	public Class<? extends IIQLTypeDictionary> bindIQLTypeDictionary() {
		return QDLTypeDictionary.class;
	}
	public Class<? extends IQDLTypeDictionary> bindQDLTypeDictionary() {
		return QDLTypeDictionary.class;
	}
	
	
	
	public Class<? extends IIQLLookUp> bindIQLLookUp() {
		return QDLLookUp.class;
	}	
	public Class<? extends IQDLLookUp> bindQDLLookUp() {
		return QDLLookUp.class;
	}

	
	
	
	public Class<? extends IIQLScopeProvider> bindIQLScopeProvider() {
		return QDLScopeProvider.class;
	}
	public Class<? extends IQDLScopeProvider> bindQDLScopeProvider() {
		return QDLScopeProvider.class;
	}
	
	
	
	
	public Class<? extends IIQLTypeUtils> bindTypeUtils() {
		return QDLTypeUtils.class;
	}
	public Class<? extends IQDLTypeUtils> bindQDLTypeUtils() {
		return QDLTypeUtils.class;
	}
	
	
	
	
	
	public Class<? extends IIQLTypeExtensionsDictionary> bindTypeExtensionsDictionary() {
		return QDLTypeExtensionsDictionary.class;
	}
	public Class<? extends IQDLTypeExtensionsDictionary> bindQDLTypeExtensionsDictionary() {
		return QDLTypeExtensionsDictionary.class;
	}
	
	
	
	public Class<? extends IIQLTypeBuilder> bindIQLTypeBuilder() {
		return QDLTypeBuilder.class;
	}	
	public Class<? extends IQDLTypeBuilder> bindQDLTypeBuilder() {
		return QDLTypeBuilder.class;
	}
	
	
	
	
	public Class<? extends IIQLExpressionEvaluator> bindIQLExpressionEvaluator() {
		return QDLExpressionEvaluator.class;
	}
	public Class<? extends IQDLExpressionEvaluator> bindQDLExpressionEvaluator() {
		return QDLExpressionEvaluator.class;
	}
	
	
	
	
	
	public Class<? extends IIQLExpressionEvaluatorContext> bindExpressionEvaluatorContext() {
		return QDLExpressionEvaluatorContext.class;
	}
	public Class<? extends IQDLExpressionEvaluatorContext> bindQDLExpressionEvaluatorContext() {
		return QDLExpressionEvaluatorContext.class;
	}
	
	
	
	
	public Class<? extends IIQLCompilerHelper> bindIQLCompilerHelper() {
		return QDLCompilerHelper.class;
	}
	public Class<? extends IQDLCompilerHelper> bindQDLCompilerHelper() {
		return QDLCompilerHelper.class;
	}
	
	
	
	
	public Class<? extends IIQLMetadataAnnotationCompiler> bindIQLMetadataAnnotationCompiler() {
		return QDLMetadataAnnotationCompiler.class;
	}
	public Class<? extends IQDLMetadataAnnotationCompiler> bindQDLMetadataAnnotationCompiler() {
		return QDLMetadataAnnotationCompiler.class;
	}
	
	
	
	
	public Class<? extends IIQLTypeCompiler> bindIQLTypeCompiler() {
		return QDLTypeCompiler.class;
	}
	public Class<? extends IQDLTypeCompiler> bindQDLTypeCompiler() {
		return QDLTypeCompiler.class;
	}
	
	
	

	public Class<? extends IIQLStatementCompiler> bindIQLStatementCompiler() {
		return QDLStatementCompiler.class;
	}
	public Class<? extends IQDLStatementCompiler> bindQDLStatementCompiler() {
		return QDLStatementCompiler.class;
	}
	
	
	
	
	
	public Class<? extends IIQLMetadataMethodCompiler> bindIQLMetadataMethodCompiler() {
		return QDLMetadataMethodCompiler.class;
	}
	public Class<? extends IQDLMetadataMethodCompiler> bindQDLMetadataMethodCompiler() {
		return QDLMetadataMethodCompiler.class;
	}
	
	
	
	
	public Class<? extends IIQLExpressionCompiler> bindIQLExpressionCompiler() {
		return QDLExpressionCompiler.class;
	}
	public Class<? extends IQDLExpressionCompiler> bindQDLExpressionCompiler() {
		return QDLExpressionCompiler.class;
	}
	
	
	
	
	public Class<? extends IIQLCompiler> bindIQLCompiler() {
		return QDLCompiler.class;
	}
	public Class<? extends IQDLCompiler> bindQDLCompiler() {
		return QDLCompiler.class;
	}
	
	
	
	
	public Class<? extends IIQLGeneratorContext> bindGeneratorContext() {
		  return QDLGeneratorContext.class;
	}
	public Class<? extends IQDLGeneratorContext> bindQDLGeneratorContext() {
		  return QDLGeneratorContext.class;
	}
	
	
	
	public Class<? extends IIQLJdtTypeProviderFactory> bindJdtTypeProviderFactory() {
		return IQLNullJdtTypeProviderFactory.class;
	}
	
	@Override
	public Class<? extends org.eclipse.xtext.common.types.access.IJvmTypeProvider.Factory> bindIJvmTypeProvider$Factory() {
		return QDLClasspathTypeProviderFactory.class;
	}
		
	@Override
	public Class<? extends org.eclipse.xtext.common.types.xtext.AbstractTypeScopeProvider> bindAbstractTypeScopeProvider() {
		return ClasspathBasedTypeScopeProvider.class;
	}
	
	public Class<? extends IIQLMethodFinder> bindMethodFinder() {
		return SimpleIQLMethodFinder.class;
	}	
	
	public Class<? extends IIQLServiceObserver> bindIQLServiceObserver() {
		return QDLServiceObserver.class;
	}
	
	public Class<? extends IIQLTypingEntryPoint> bindIQLTypingEntryPoint() {
		return QDLTypingEntryPoint.class;
	}
	
	
	@Override
	public Class<? extends ILinkingService> bindILinkingService() {
		return IQLLinkingService.class;
	}
		
	
	public Class<? extends IIQLExecutor> bindExecutor() {
		return QDLExecutor.class;
	}
	
	public Class<? extends IGenerator> bindGenerator() {
		return QDLGenerator.class;
	}
		
	
	public Class<? extends org.eclipse.xtext.naming.IQualifiedNameProvider> bindIQualifiedNameProvider() {
		return QDLQualifiedNameProvider.class;
	}
	
	public Class<? extends IQualifiedNameConverter> bindQualifiedNameConverter() {
		return IQLQualifiedNameConverter.class;
	}
	

	@Override
	public Class<? extends XtextResource> bindXtextResource() {
		return IQLLinkingResource.class;
	}
	
	public Class<? extends XtextResourceFactory> bindXtextResourceFactory() {
		  return IQLResourceFactory.class;
	}


	
}
