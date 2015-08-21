package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.typing.factory.IODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;
import javax.inject.Inject;

@SuppressWarnings("all")
public class ODLTypeCompiler extends AbstractIQLTypeCompiler<IODLCompilerHelper, IODLGeneratorContext, IODLExpressionCompiler, IODLTypeFactory, IODLTypeUtils> implements IODLTypeCompiler {
  @Inject
  public ODLTypeCompiler(final IODLCompilerHelper helper, final IODLTypeFactory typeFactory, final IODLTypeUtils typeUtils) {
    super(helper, typeFactory, typeUtils);
  }
}
