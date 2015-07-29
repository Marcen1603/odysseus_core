package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLExpressionParser;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeOperatorsFactory;
import javax.inject.Inject;

@SuppressWarnings("all")
public class ODLExpressionCompiler extends AbstractIQLExpressionCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLTypeCompiler, ODLExpressionParser, ODLTypeFactory, ODLLookUp, ODLTypeOperatorsFactory> {
  @Inject
  public ODLExpressionCompiler(final ODLCompilerHelper helper, final ODLTypeCompiler typeCompiler, final ODLExpressionParser parser, final ODLTypeFactory factory, final ODLLookUp lookUp, final ODLTypeOperatorsFactory typeOperatorsFactory) {
    super(helper, typeCompiler, parser, factory, lookUp, typeOperatorsFactory);
  }
}
