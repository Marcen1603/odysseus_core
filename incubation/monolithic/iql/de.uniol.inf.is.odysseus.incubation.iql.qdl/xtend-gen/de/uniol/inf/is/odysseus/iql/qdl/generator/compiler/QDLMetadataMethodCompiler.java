package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.basic.types.ID;
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLMetadataValueSingleID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class QDLMetadataMethodCompiler extends AbstractIQLMetadataMethodCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler> {
  @Inject
  public QDLMetadataMethodCompiler(final QDLCompilerHelper helper, final QDLTypeCompiler typeCompiler) {
    super(helper, typeCompiler);
  }
  
  public String compile(final IQLMetadataValue o, final String varName, final AtomicInteger counter, final QDLGeneratorContext context) {
    if ((o instanceof QDLMetadataValueSingleID)) {
      return this.compile(((QDLMetadataValueSingleID) o), varName, context);
    } else {
      return super.compile(o, varName, counter, context);
    }
  }
  
  public String compile(final QDLMetadataValueSingleID o, final String varName, final QDLGeneratorContext context) {
    String _xblockexpression = null;
    {
      String _canonicalName = ID.class.getCanonicalName();
      context.addImport(_canonicalName);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("ID ");
      _builder.append(varName, "");
      _builder.append(" = new ID(\"");
      String _value = o.getValue();
      _builder.append(_value, "");
      _builder.append("\");");
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
}
