package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLMetadataValueSingleID;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;
import javax.inject.Inject;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class QDLMetadataAnnotationCompiler extends AbstractIQLMetadataAnnotationCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler, QDLTypeUtils> {
  @Inject
  public QDLMetadataAnnotationCompiler(final QDLCompilerHelper helper, final QDLTypeCompiler typeCompiler, final QDLTypeUtils typeUtils) {
    super(helper, typeCompiler, typeUtils);
  }
  
  public String compile(final IQLMetadataValue o, final QDLGeneratorContext c) {
    String _xifexpression = null;
    if ((o instanceof QDLMetadataValueSingleID)) {
      return this.compile(((QDLMetadataValueSingleID) o), c);
    } else {
      _xifexpression = super.compile(o, c);
    }
    return _xifexpression;
  }
  
  public String compile(final QDLMetadataValueSingleID o, final QDLGeneratorContext c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    String _value = o.getValue();
    _builder.append(_value, "");
    _builder.append("\"");
    return _builder.toString();
  }
}
