package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;
import javax.inject.Inject;

@SuppressWarnings("all")
public class ODLTypeCompiler extends AbstractIQLTypeCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLExpressionCompiler, ODLTypeFactory, ODLTypeUtils> {
  @Inject
  public ODLTypeCompiler(final ODLCompilerHelper helper, final ODLTypeFactory typeFactory, final ODLTypeUtils typeUtils) {
    super(helper, typeFactory, typeUtils);
  }
}
