package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;
import javax.inject.Inject;

@SuppressWarnings("all")
public class QDLTypeCompiler extends AbstractIQLTypeCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLExpressionCompiler, QDLTypeFactory, QDLTypeUtils> {
  @Inject
  public QDLTypeCompiler(final QDLCompilerHelper helper, final QDLTypeFactory typeFactory, final QDLTypeUtils typeUtils) {
    super(helper, typeFactory, typeUtils);
  }
}
