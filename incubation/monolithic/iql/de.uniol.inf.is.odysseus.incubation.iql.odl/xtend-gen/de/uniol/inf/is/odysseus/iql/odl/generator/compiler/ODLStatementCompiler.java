package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLExpressionParser;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import javax.inject.Inject;

@SuppressWarnings("all")
public class ODLStatementCompiler extends AbstractIQLStatementCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLTypeCompiler, ODLExpressionCompiler, ODLTypeFactory, ODLExpressionParser, ODLLookUp> {
  @Inject
  public ODLStatementCompiler(final ODLCompilerHelper helper, final ODLExpressionCompiler exprCompiler, final ODLTypeCompiler typeCompiler, final ODLTypeFactory factory, final ODLExpressionParser exprParser, final ODLLookUp lookUp) {
    super(helper, exprCompiler, typeCompiler, factory, exprParser, lookUp);
  }
}
