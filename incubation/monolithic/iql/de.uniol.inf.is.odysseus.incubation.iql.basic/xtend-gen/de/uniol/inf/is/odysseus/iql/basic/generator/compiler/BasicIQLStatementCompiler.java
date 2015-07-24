package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.BasicIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import javax.inject.Inject;

@SuppressWarnings("all")
public class BasicIQLStatementCompiler extends AbstractIQLStatementCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLTypeCompiler, BasicIQLExpressionCompiler, BasicIQLTypeFactory, BasicIQLExpressionParser, BasicIQLLookUp> {
  @Inject
  public BasicIQLStatementCompiler(final BasicIQLCompilerHelper helper, final BasicIQLExpressionCompiler exprCompiler, final BasicIQLTypeCompiler typeCompiler, final BasicIQLTypeFactory factory, final BasicIQLExpressionParser exprParser, final BasicIQLLookUp lookUp) {
    super(helper, exprCompiler, typeCompiler, factory, exprParser, lookUp);
  }
}
