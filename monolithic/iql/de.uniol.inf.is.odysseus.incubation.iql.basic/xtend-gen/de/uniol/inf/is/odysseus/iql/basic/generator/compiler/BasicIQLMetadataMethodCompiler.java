package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext;
import javax.inject.Inject;

@SuppressWarnings("all")
public class BasicIQLMetadataMethodCompiler extends AbstractIQLMetadataMethodCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLTypeCompiler> {
  @Inject
  public BasicIQLMetadataMethodCompiler(final BasicIQLCompilerHelper helper, final BasicIQLTypeCompiler typeCompiler) {
    super(helper, typeCompiler);
  }
}
