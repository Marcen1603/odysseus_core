package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.BasicIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;
import javax.inject.Inject;

@SuppressWarnings("all")
public class BasicIQLStatementCompiler extends AbstractIQLStatementCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLTypeCompiler, BasicIQLExpressionCompiler, BasicIQLTypeUtils, BasicIQLExpressionEvaluator, BasicIQLLookUp> {
  @Inject
  public BasicIQLStatementCompiler(final BasicIQLCompilerHelper helper, final BasicIQLExpressionCompiler exprCompiler, final BasicIQLTypeCompiler typeCompiler, final BasicIQLTypeUtils typeUtils, final BasicIQLExpressionEvaluator exprEvaluator, final BasicIQLLookUp lookUp) {
    super(helper, exprCompiler, typeCompiler, typeUtils, exprEvaluator, lookUp);
  }
}
