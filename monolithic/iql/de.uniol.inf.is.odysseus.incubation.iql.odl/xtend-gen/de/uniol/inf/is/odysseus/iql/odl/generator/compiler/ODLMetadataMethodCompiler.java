package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext;
import javax.inject.Inject;

@SuppressWarnings("all")
public class ODLMetadataMethodCompiler extends AbstractIQLMetadataMethodCompiler<IODLCompilerHelper, IODLGeneratorContext, IODLTypeCompiler> implements IODLMetadataMethodCompiler {
  @Inject
  public ODLMetadataMethodCompiler(final IODLCompilerHelper helper, final IODLTypeCompiler typeCompiler) {
    super(helper, typeCompiler);
  }
}
