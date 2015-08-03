package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.BasicIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.BasicIQLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;
import javax.inject.Inject;

@SuppressWarnings("all")
public class BasicIQLExpressionCompiler extends AbstractIQLExpressionCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLTypeCompiler, BasicIQLExpressionParser, BasicIQLTypeUtils, BasicIQLLookUp, BasicIQLTypeExtensionsFactory> {
  @Inject
  public BasicIQLExpressionCompiler(final BasicIQLCompilerHelper helper, final BasicIQLTypeCompiler typeCompiler, final BasicIQLExpressionParser parser, final BasicIQLTypeUtils typeUtils, final BasicIQLLookUp lookUp, final BasicIQLTypeExtensionsFactory typeExtensionsFactory) {
    super(helper, typeCompiler, parser, typeUtils, lookUp, typeExtensionsFactory);
  }
}
