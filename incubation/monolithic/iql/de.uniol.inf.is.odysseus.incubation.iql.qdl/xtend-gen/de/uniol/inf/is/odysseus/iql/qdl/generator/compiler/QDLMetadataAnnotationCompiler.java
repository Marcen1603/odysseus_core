package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper;
import javax.inject.Inject;

@SuppressWarnings("all")
public class QDLMetadataAnnotationCompiler extends AbstractIQLMetadataAnnotationCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler> {
  @Inject
  public QDLMetadataAnnotationCompiler(final QDLCompilerHelper helper, final QDLTypeCompiler typeCompiler) {
    super(helper, typeCompiler);
  }
}
