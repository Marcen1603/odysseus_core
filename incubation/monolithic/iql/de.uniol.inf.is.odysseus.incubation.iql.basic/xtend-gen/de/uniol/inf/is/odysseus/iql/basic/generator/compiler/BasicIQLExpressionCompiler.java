package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.BasicIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators.BasicIQLTypeOperatorsFactory;
import javax.inject.Inject;

@SuppressWarnings("all")
public class BasicIQLExpressionCompiler extends AbstractIQLExpressionCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLTypeCompiler, BasicIQLExpressionParser, BasicIQLTypeFactory, BasicIQLLookUp, BasicIQLTypeOperatorsFactory> {
  @Inject
  public BasicIQLExpressionCompiler(final BasicIQLCompilerHelper helper, final BasicIQLTypeCompiler typeCompiler, final BasicIQLExpressionParser parser, final BasicIQLTypeFactory factory, final BasicIQLLookUp lookUp, final BasicIQLTypeOperatorsFactory typeOperatorsFactory) {
    super(helper, typeCompiler, parser, factory, lookUp, typeOperatorsFactory);
  }
}
