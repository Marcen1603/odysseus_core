package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;




public interface IIQLMetadataMethodCompiler<G extends IIQLGeneratorContext> {
	public static final String CREATE_METADATA_METHOD_NAME = "createMetadata";

	String compile(IQLMetadataList o, G context);

}
