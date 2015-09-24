/*
 * generated by Xtext
 */
package de.uniol.inf.is.odysseus.iql.basic;

import org.eclipse.xtext.common.types.xtext.ClasspathBasedTypeScopeProvider;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.linking.ILinkingService;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceFactory;

import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.BasicIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.IIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.context.BasicIQLExpressionEvaluatorContext;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.context.IIQLExpressionEvaluatorContext;
import de.uniol.inf.is.odysseus.iql.basic.generator.BasicIQLGenerator;
import de.uniol.inf.is.odysseus.iql.basic.generator.IIQLGenerator;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.linking.IIQLMethodFinder;
import de.uniol.inf.is.odysseus.iql.basic.linking.IQLLinkingResource;
import de.uniol.inf.is.odysseus.iql.basic.linking.IQLLinkingService;
import de.uniol.inf.is.odysseus.iql.basic.linking.IQLResourceFactory;
import de.uniol.inf.is.odysseus.iql.basic.linking.SimpleIQLMethodFinder;
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.scoping.BasicIQLQualifiedNameProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.BasicIQLResourceDescriptionStrategy;
import de.uniol.inf.is.odysseus.iql.basic.scoping.BasicIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLCrossReferenceValidator;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLClasspathTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLCrossReferenceValidator;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLNullJdtTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.service.BasicIQLServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.service.IIQLServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.BasicIQLTypeBuilder;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.IIQLTypeBuilder;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.entrypoint.BasicIQLTypingEntryPoint;
import de.uniol.inf.is.odysseus.iql.basic.typing.entrypoint.IIQLTypingEntryPoint;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.BasicIQLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
@SuppressWarnings({"restriction", "rawtypes"})
public class BasicIQLRuntimeModule extends de.uniol.inf.is.odysseus.iql.basic.AbstractBasicIQLRuntimeModule {

	public Class<? extends IIQLGenerator> bindIQLGenerator() {
		return BasicIQLGenerator.class;
	}
	
	public Class<? extends IDefaultResourceDescriptionStrategy> bindResourceDescriptionStrategy() {
		return BasicIQLResourceDescriptionStrategy.class;
	}
	
	public Class<? extends IIQLCrossReferenceValidator> bindCrossReferenceValidator() {
		return IQLCrossReferenceValidator.class;
	}
	
	
	
	public Class<? extends IIQLJdtTypeProviderFactory> bindJdtTypeProviderFactory() {
		return IQLNullJdtTypeProviderFactory.class;
	}
	
	public Class<? extends IIQLScopeProvider> bindIQLScopeProvider() {
		return BasicIQLScopeProvider.class;
	}
	
	@Override
	public Class<? extends org.eclipse.xtext.common.types.access.IJvmTypeProvider.Factory> bindIJvmTypeProvider$Factory() {
		return IQLClasspathTypeProviderFactory.class;
	}
	
	@Override
	public Class<? extends org.eclipse.xtext.common.types.xtext.AbstractTypeScopeProvider> bindAbstractTypeScopeProvider() {
		return ClasspathBasedTypeScopeProvider.class;
	}	
	

	public Class<? extends IIQLMethodFinder> bindMethodFinder() {
		return SimpleIQLMethodFinder.class;
	}
	
	public Class<? extends IIQLServiceObserver> bindIQLServiceObserver() {
		return BasicIQLServiceObserver.class;
	}	
	
	public Class<? extends IIQLTypeUtils> bindTypeUtils() {
		return BasicIQLTypeUtils.class;
	}
	
	public Class<? extends IIQLTypeExtensionsDictionary> bindTypeExtensionsDictionary() {
		return BasicIQLTypeExtensionsDictionary.class;
	}
	
	@Override
	public Class<? extends ILinkingService> bindILinkingService() {
		return IQLLinkingService.class;
	}	
	
	public Class<? extends IIQLExpressionEvaluatorContext> bindExpressionEvaluatorContext() {
		return BasicIQLExpressionEvaluatorContext.class;
	}
	
	public Class<? extends IGenerator> bindGenerator() {
		return BasicIQLGenerator.class;
	}
	
	public Class<? extends IIQLTypeCompiler> bindIQLTypeCompiler() {
		return BasicIQLTypeCompiler.class;
	}
	
	public Class<? extends IIQLLookUp> bindIQLLookUp() {
		return BasicIQLLookUp.class;
	}
	
	
	public Class<? extends IIQLCompilerHelper> bindIQLCompilerHelper() {
		return BasicIQLCompilerHelper.class;
	}
	
	public Class<? extends IIQLCompiler> bindIQLCompiler() {
		return BasicIQLCompiler.class;
	}
	
	public Class<? extends IIQLExpressionCompiler> bindIQLExpressionCompiler() {
		return BasicIQLExpressionCompiler.class;
	}
	
	public Class<? extends IIQLStatementCompiler> bindIQLStatementCompiler() {
		return BasicIQLStatementCompiler.class;
	}
		
	public Class<? extends IIQLMetadataAnnotationCompiler> bindIQLMetadataAnnotationCompiler() {
		return BasicIQLMetadataAnnotationCompiler.class;
	}
	
	public Class<? extends IIQLMetadataMethodCompiler> bindIQLMetadataMethodCompiler() {
		return BasicIQLMetadataMethodCompiler.class;
	}

	public Class<? extends IIQLExpressionEvaluator> bindIQLExpressionEvaluator() {
		return BasicIQLExpressionEvaluator.class;
	}
	
	
	public Class<? extends IIQLTypeDictionary> bindIQLTypeDictionary() {
		return BasicIQLTypeDictionary.class;
	}
		
	public Class<? extends IIQLTypeBuilder> bindIQLTypeBuilder() {
		return BasicIQLTypeBuilder.class;
	}
	
	public Class<? extends IIQLTypingEntryPoint> bindIQLTypingEntryPoint() {
		return BasicIQLTypingEntryPoint.class;
	}
	
	public Class<? extends org.eclipse.xtext.naming.IQualifiedNameProvider> bindIQualifiedNameProvider() {
		return BasicIQLQualifiedNameProvider.class;
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
	
	public Class<? extends IIQLGeneratorContext> bindGeneratorContext() {
		  return BasicIQLGeneratorContext.class;
	}
	
}

