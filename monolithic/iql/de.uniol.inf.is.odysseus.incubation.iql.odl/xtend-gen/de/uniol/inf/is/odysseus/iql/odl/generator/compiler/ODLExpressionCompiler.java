package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.odl.exprevaluator.IODLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.typeextension.IODLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;
import javax.inject.Inject;

@SuppressWarnings("all")
public class ODLExpressionCompiler extends AbstractIQLExpressionCompiler<IODLCompilerHelper, IODLGeneratorContext, IODLTypeCompiler, IODLExpressionEvaluator, IODLTypeUtils, IODLLookUp, IODLTypeExtensionsDictionary, IODLTypeDictionary> implements IODLExpressionCompiler {
  @Inject
  public ODLExpressionCompiler(final IODLCompilerHelper helper, final IODLTypeCompiler typeCompiler, final IODLExpressionEvaluator exprEvaluator, final IODLTypeUtils typeUtils, final IODLLookUp lookUp, final IODLTypeExtensionsDictionary typeExtensionsDictionary, final IODLTypeDictionary typeDictionary) {
    super(helper, typeCompiler, exprEvaluator, typeUtils, lookUp, typeExtensionsDictionary, typeDictionary);
  }
}
