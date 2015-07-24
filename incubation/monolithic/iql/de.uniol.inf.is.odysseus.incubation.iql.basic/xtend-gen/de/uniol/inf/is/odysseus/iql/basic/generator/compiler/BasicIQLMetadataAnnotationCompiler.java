package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext;
import javax.inject.Inject;

@SuppressWarnings("all")
public class BasicIQLMetadataAnnotationCompiler extends AbstractIQLMetadataAnnotationCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLTypeCompiler> {
  @Inject
  public BasicIQLMetadataAnnotationCompiler(final BasicIQLCompilerHelper helper, final BasicIQLTypeCompiler typeCompiler) {
    super(helper, typeCompiler);
  }
}
