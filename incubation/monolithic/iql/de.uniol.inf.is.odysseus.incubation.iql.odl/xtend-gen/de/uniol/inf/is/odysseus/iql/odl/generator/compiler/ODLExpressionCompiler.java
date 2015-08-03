package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLExpressionParser;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeOperatorsFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;
import javax.inject.Inject;

@SuppressWarnings("all")
public class ODLExpressionCompiler extends AbstractIQLExpressionCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLTypeCompiler, ODLExpressionParser, ODLTypeUtils, ODLLookUp, ODLTypeOperatorsFactory> {
  @Inject
  public ODLExpressionCompiler(final ODLCompilerHelper helper, final ODLTypeCompiler typeCompiler, final ODLExpressionParser parser, final ODLTypeUtils typeUtils, final ODLLookUp lookUp, final ODLTypeOperatorsFactory typeOperatorsFactory) {
    super(helper, typeCompiler, parser, typeUtils, lookUp, typeOperatorsFactory);
  }
}
