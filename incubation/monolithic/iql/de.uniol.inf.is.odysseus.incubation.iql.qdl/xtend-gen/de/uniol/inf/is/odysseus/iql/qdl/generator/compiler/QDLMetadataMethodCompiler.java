package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper;
import javax.inject.Inject;

@SuppressWarnings("all")
public class QDLMetadataMethodCompiler extends AbstractIQLMetadataMethodCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler> {
  @Inject
  public QDLMetadataMethodCompiler(final QDLCompilerHelper helper, final QDLTypeCompiler typeCompiler) {
    super(helper, typeCompiler);
  }
}
