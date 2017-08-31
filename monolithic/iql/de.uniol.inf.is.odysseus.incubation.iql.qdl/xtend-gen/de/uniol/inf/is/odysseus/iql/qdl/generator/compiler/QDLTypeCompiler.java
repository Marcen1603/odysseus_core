package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.IQDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;
import javax.inject.Inject;

@SuppressWarnings("all")
public class QDLTypeCompiler extends AbstractIQLTypeCompiler<IQDLCompilerHelper, IQDLGeneratorContext, IQDLExpressionCompiler, IQDLTypeDictionary, IQDLTypeUtils> implements IQDLTypeCompiler {
  @Inject
  public QDLTypeCompiler(final IQDLCompilerHelper helper, final IQDLTypeDictionary typeDictionary, final IQDLTypeUtils typeUtils) {
    super(helper, typeDictionary, typeUtils);
  }
}
