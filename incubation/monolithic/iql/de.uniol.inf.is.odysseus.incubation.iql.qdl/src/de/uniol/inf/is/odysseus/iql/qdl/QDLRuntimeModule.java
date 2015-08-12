/*
 * generated by Xtext
 */
package de.uniol.inf.is.odysseus.iql.qdl;

import org.eclipse.xtext.common.types.xtext.ClasspathBasedTypeScopeProvider;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.linking.ILinkingService;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceFactory;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.linking.IQLLinkingResource;
import de.uniol.inf.is.odysseus.iql.basic.linking.IQLResourceFactory;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.parser.IIQLParser;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLMethodFinder;
import de.uniol.inf.is.odysseus.iql.basic.scoping.DefaultIQLMethodFinder;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLNullJdtTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.service.IIQLServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.IIQLTypeBuilder;
import de.uniol.inf.is.odysseus.iql.basic.typing.entrypoint.IIQLTypingEntryPoint;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.IIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.context.IIQLExpressionParserContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGenerator;
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.linking.QDLLinkingService;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.parser.QDLParser;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLClasspathTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLQualifiedNameProvider;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLScopeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceObserver;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLExpressionParser;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLExpressionParserContext;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeBuilder;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypingEntryPoint;


/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
@SuppressWarnings({"restriction", "rawtypes"})
public class QDLRuntimeModule extends de.uniol.inf.is.odysseus.iql.qdl.AbstractQDLRuntimeModule {

	public Class<? extends IIQLJdtTypeProviderFactory> bindJdtTypeProviderFactory() {
		return IQLNullJdtTypeProviderFactory.class;
	}
	
	public Class<? extends IIQLScopeProvider> bindIQLScopeProvider() {
		return QDLScopeProvider.class;
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
		return DefaultIQLMethodFinder.class;
	}	
	
	public Class<? extends IIQLServiceObserver> bindIQLServiceObserver() {
		return QDLServiceObserver.class;
	}
	
	public Class<? extends IIQLTypeUtils> bindTypeUtils() {
		return QDLTypeUtils.class;
	}
	
	public Class<? extends IIQLTypeExtensionsFactory> bindTypeOperatorsFactory() {
		return QDLTypeExtensionsFactory.class;
	}
	
	@Override
	public Class<? extends ILinkingService> bindILinkingService() {
		return QDLLinkingService.class;
	}
	
	public Class<? extends IIQLExpressionParserContext> bindExpressionParserContext() {
		return QDLExpressionParserContext.class;
	}
	
	
	public Class<? extends IIQLParser> bindParser() {
		return QDLParser.class;
	}
	
	public Class<? extends IGenerator> bindGenerator() {
		return QDLGenerator.class;
	}
	
	public Class<? extends IIQLCompilerHelper> bindIQLCompilerHelper() {
		return QDLCompilerHelper.class;
	}
	
	public Class<? extends IIQLTypeCompiler> bindIQLTypeCompiler() {
		return QDLTypeCompiler.class;
	}
	
	
	public Class<? extends IIQLCompiler> bindIQLCompiler() {
		return QDLCompiler.class;
	}
	
	public Class<? extends IIQLExpressionCompiler> bindIQLExpressionCompiler() {
		return QDLExpressionCompiler.class;
	}
	
	public Class<? extends IIQLStatementCompiler> bindIQLStatementCompiler() {
		return QDLStatementCompiler.class;
	}
		
	public Class<? extends IIQLMetadataAnnotationCompiler> bindIQLMetadataAnnotationCompiler() {
		return QDLMetadataAnnotationCompiler.class;
	}
	
	public Class<? extends IIQLMetadataMethodCompiler> bindIQLMetadataMethodCompiler() {
		return QDLMetadataMethodCompiler.class;
	}

	public Class<? extends IIQLExpressionParser> bindIQLExpressionParser() {
		return QDLExpressionParser.class;
	}
	
	
	public Class<? extends IIQLTypeFactory> bindIQLTypeFactory() {
		return QDLTypeFactory.class;
	}
	
	
	public Class<? extends IIQLLookUp> bindIQLLookUp() {
		return QDLLookUp.class;
	}
	
	
	public Class<? extends IIQLTypeBuilder> bindIQLTypeBuilder() {
		return QDLTypeBuilder.class;
	}
	
	public Class<? extends IIQLTypingEntryPoint> bindIQLTypingEntryPoint() {
		return QDLTypingEntryPoint.class;
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

	public Class<? extends IIQLGeneratorContext> bindGeneratorContext() {
		  return QDLGeneratorContext.class;
	}
	
}