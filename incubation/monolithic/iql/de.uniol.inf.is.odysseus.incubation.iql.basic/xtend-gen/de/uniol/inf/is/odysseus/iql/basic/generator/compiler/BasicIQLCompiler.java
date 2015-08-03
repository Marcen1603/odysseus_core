package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;
import javax.inject.Inject;

@SuppressWarnings("all")
public class BasicIQLCompiler extends AbstractIQLCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLTypeCompiler, BasicIQLStatementCompiler, BasicIQLTypeFactory, BasicIQLTypeUtils> {
  @Inject
  public BasicIQLCompiler(final BasicIQLCompilerHelper helper, final BasicIQLTypeCompiler typeCompiler, final BasicIQLStatementCompiler stmtCompiler, final BasicIQLTypeFactory typeFactory, final BasicIQLTypeUtils typeUtils) {
    super(helper, typeCompiler, stmtCompiler, typeFactory, typeUtils);
  }
}
