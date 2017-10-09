package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;
import javax.inject.Inject;

@SuppressWarnings("all")
public class BasicIQLTypeCompiler extends AbstractIQLTypeCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLExpressionCompiler, BasicIQLTypeDictionary, BasicIQLTypeUtils> {
  @Inject
  public BasicIQLTypeCompiler(final BasicIQLCompilerHelper helper, final BasicIQLTypeDictionary typeDictionary, final BasicIQLTypeUtils typeUtils) {
    super(helper, typeDictionary, typeUtils);
  }
}
