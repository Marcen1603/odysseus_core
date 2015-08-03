package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

@SuppressWarnings("all")
public class BasicIQLTypeCompiler extends AbstractIQLTypeCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLExpressionCompiler, BasicIQLTypeFactory, BasicIQLTypeUtils> {
  public BasicIQLTypeCompiler(final BasicIQLCompilerHelper helper, final BasicIQLTypeFactory typeFactory, final BasicIQLTypeUtils typeUtils) {
    super(helper, typeFactory, typeUtils);
  }
}
