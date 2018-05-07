package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.IQDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLMetadataValueSingleID;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;
import javax.inject.Inject;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class QDLMetadataAnnotationCompiler extends AbstractIQLMetadataAnnotationCompiler<IQDLCompilerHelper, IQDLGeneratorContext, IQDLTypeCompiler, IQDLTypeUtils> implements IQDLMetadataAnnotationCompiler {
  @Inject
  public QDLMetadataAnnotationCompiler(final IQDLCompilerHelper helper, final IQDLTypeCompiler typeCompiler, final IQDLTypeUtils typeUtils) {
    super(helper, typeCompiler, typeUtils);
  }
  
  @Override
  public String compile(final IQLMetadataValue o, final IQDLGeneratorContext c) {
    String _xifexpression = null;
    if ((o instanceof QDLMetadataValueSingleID)) {
      return this.compile(((QDLMetadataValueSingleID) o), c);
    } else {
      _xifexpression = super.compile(o, c);
    }
    return _xifexpression;
  }
  
  public String compile(final QDLMetadataValueSingleID o, final IQDLGeneratorContext c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    String _value = o.getValue();
    _builder.append(_value, "");
    _builder.append("\"");
    return _builder.toString();
  }
}
