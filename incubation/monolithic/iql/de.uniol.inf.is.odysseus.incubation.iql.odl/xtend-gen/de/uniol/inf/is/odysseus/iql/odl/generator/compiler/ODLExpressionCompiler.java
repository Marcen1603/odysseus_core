package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.odl.exprevaluator.ODLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;
import javax.inject.Inject;

@SuppressWarnings("all")
public class ODLExpressionCompiler extends AbstractIQLExpressionCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLTypeCompiler, ODLExpressionEvaluator, ODLTypeUtils, ODLLookUp, ODLTypeExtensionsFactory> {
  @Inject
  public ODLExpressionCompiler(final ODLCompilerHelper helper, final ODLTypeCompiler typeCompiler, final ODLExpressionEvaluator exprEvaluator, final ODLTypeUtils typeUtils, final ODLLookUp lookUp, final ODLTypeExtensionsFactory typeOperatorsFactory) {
    super(helper, typeCompiler, exprEvaluator, typeUtils, lookUp, typeOperatorsFactory);
  }
}
