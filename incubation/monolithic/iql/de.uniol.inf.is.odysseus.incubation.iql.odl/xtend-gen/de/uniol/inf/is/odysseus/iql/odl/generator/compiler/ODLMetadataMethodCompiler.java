package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper;
import javax.inject.Inject;

@SuppressWarnings("all")
public class ODLMetadataMethodCompiler extends AbstractIQLMetadataMethodCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLTypeCompiler> {
  @Inject
  public ODLMetadataMethodCompiler(final ODLCompilerHelper helper, final ODLTypeCompiler typeCompiler) {
    super(helper, typeCompiler);
  }
}
