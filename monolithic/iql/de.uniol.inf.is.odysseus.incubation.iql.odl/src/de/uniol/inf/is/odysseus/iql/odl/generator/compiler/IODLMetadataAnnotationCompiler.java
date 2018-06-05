package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;

public interface IODLMetadataAnnotationCompiler extends IIQLMetadataAnnotationCompiler<IODLGeneratorContext> {

	String getAOAnnotationElements(ODLOperator operator,
			IODLGeneratorContext context);

	String getParameterAnnotationElements(ODLParameter parameter,
			IODLGeneratorContext context);

}
