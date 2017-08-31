package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.odl.exprevaluator.IODLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;
import javax.inject.Inject;

@SuppressWarnings("all")
public class ODLStatementCompiler extends AbstractIQLStatementCompiler<IODLCompilerHelper, IODLGeneratorContext, IODLTypeCompiler, IODLExpressionCompiler, IODLTypeUtils, IODLExpressionEvaluator, IODLLookUp> implements IODLStatementCompiler {
  @Inject
  public ODLStatementCompiler(final IODLCompilerHelper helper, final IODLExpressionCompiler exprCompiler, final IODLTypeCompiler typeCompiler, final IODLTypeUtils typeUtils, final IODLExpressionEvaluator exprEvaluator, final IODLLookUp lookUp) {
    super(helper, exprCompiler, typeCompiler, typeUtils, exprEvaluator, lookUp);
  }
}
