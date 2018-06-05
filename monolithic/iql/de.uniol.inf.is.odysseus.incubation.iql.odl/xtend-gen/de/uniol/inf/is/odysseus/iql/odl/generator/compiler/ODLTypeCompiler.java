package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;
import javax.inject.Inject;

@SuppressWarnings("all")
public class ODLTypeCompiler extends AbstractIQLTypeCompiler<IODLCompilerHelper, IODLGeneratorContext, IODLExpressionCompiler, IODLTypeDictionary, IODLTypeUtils> implements IODLTypeCompiler {
  @Inject
  public ODLTypeCompiler(final IODLCompilerHelper helper, final IODLTypeDictionary typeDictionary, final IODLTypeUtils typeUtils) {
    super(helper, typeDictionary, typeUtils);
  }
}
