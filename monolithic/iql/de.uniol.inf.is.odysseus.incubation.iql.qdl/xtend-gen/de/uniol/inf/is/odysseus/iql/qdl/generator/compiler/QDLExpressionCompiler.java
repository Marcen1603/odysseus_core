package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.IQDLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.IQDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.QDLSubscribableWithPort;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.typeextension.IQDLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;
import javax.inject.Inject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmExecutable;

@SuppressWarnings("all")
public class QDLExpressionCompiler extends AbstractIQLExpressionCompiler<IQDLCompilerHelper, IQDLGeneratorContext, IQDLTypeCompiler, IQDLExpressionEvaluator, IQDLTypeUtils, IQDLLookUp, IQDLTypeExtensionsDictionary, IQDLTypeDictionary> implements IQDLExpressionCompiler {
  @Inject
  public QDLExpressionCompiler(final IQDLCompilerHelper helper, final IQDLTypeCompiler typeCompiler, final IQDLExpressionEvaluator exprEvaluator, final IQDLTypeUtils typeUtils, final IQDLLookUp lookUp, final IQDLTypeExtensionsDictionary typeExtensionsDictionary, final IQDLTypeDictionary typeDictionary) {
    super(helper, typeCompiler, exprEvaluator, typeUtils, lookUp, typeExtensionsDictionary, typeDictionary);
  }
  
  @Override
  public String compile(final IQLExpression e, final IQDLGeneratorContext context) {
    String _xifexpression = null;
    if ((e instanceof IQLSubscribeExpression)) {
      return this.compile(((IQLSubscribeExpression) e), context);
    } else {
      String _xifexpression_1 = null;
      if ((e instanceof IQLPortExpression)) {
        return this.compile(((IQLPortExpression) e), context);
      } else {
        _xifexpression_1 = super.compile(e, context);
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLSubscribeExpression e, final IQDLGeneratorContext context) {
    String _xifexpression = null;
    boolean _equals = e.getOp().equals("->");
    if (_equals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("subscribeSink(");
      String _compile = this.compile(e.getLeftOperand(), context);
      _builder.append(_compile);
      _builder.append(", ");
      String _compile_1 = this.compile(e.getRightOperand(), context);
      _builder.append(_compile_1);
      _builder.append(")");
      _xifexpression = _builder.toString();
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("subscribeToSource(");
      String _compile_2 = this.compile(e.getLeftOperand(), context);
      _builder_1.append(_compile_2);
      _builder_1.append(", ");
      String _compile_3 = this.compile(e.getRightOperand(), context);
      _builder_1.append(_compile_3);
      _builder_1.append(")");
      _xifexpression = _builder_1.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLPortExpression e, final IQDLGeneratorContext context) {
    String _xblockexpression = null;
    {
      context.addImport(QDLSubscribableWithPort.class.getCanonicalName());
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("new ");
      String _simpleName = QDLSubscribableWithPort.class.getSimpleName();
      _builder.append(_simpleName);
      _builder.append("(");
      String _compile = this.compile(e.getLeftOperand(), context);
      _builder.append(_compile);
      _builder.append(", ");
      String _compile_1 = this.compile(e.getRightOperand(), context);
      _builder.append(_compile_1);
      _builder.append(")");
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  @Override
  public String compile(final IQLNewExpression e, final IQDLGeneratorContext context) {
    String _xifexpression = null;
    boolean _isOperator = this.helper.isOperator(e.getRef());
    if (_isOperator) {
      String _xifexpression_1 = null;
      if ((((e.getArgsList() != null) && (e.getArgsMap() != null)) && (e.getArgsMap().getElements().size() > 0))) {
        String _xblockexpression = null;
        {
          JvmExecutable constructor = this.lookUp.findPublicConstructor(e.getRef(), e.getArgsList().getElements());
          int _size = e.getArgsList().getElements().size();
          boolean args = (_size > 0);
          String _xifexpression_2 = null;
          if ((constructor != null)) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("getOperator");
            String _shortName = this.typeUtils.getShortName(e.getRef(), false);
            _builder.append(_shortName);
            int _hashCode = e.getRef().hashCode();
            _builder.append(_hashCode);
            _builder.append("(new ");
            String _compile = this.typeCompiler.compile(e.getRef(), context, false);
            _builder.append(_compile);
            _builder.append("(\"");
            String _shortName_1 = this.typeUtils.getShortName(e.getRef(), false);
            _builder.append(_shortName_1);
            _builder.append("\"");
            {
              if (args) {
                _builder.append(", ");
              }
            }
            String _compile_1 = this.compile(e.getArgsList(), constructor.getParameters(), context);
            _builder.append(_compile_1);
            _builder.append("), operators,  ");
            String _compile_2 = this.compile(e.getArgsMap(), e.getRef(), context);
            _builder.append(_compile_2);
            _builder.append(")");
            _xifexpression_2 = _builder.toString();
          } else {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append("getOperator");
            String _shortName_2 = this.typeUtils.getShortName(e.getRef(), false);
            _builder_1.append(_shortName_2);
            int _hashCode_1 = e.getRef().hashCode();
            _builder_1.append(_hashCode_1);
            _builder_1.append("(new ");
            String _compile_3 = this.typeCompiler.compile(e.getRef(), context, false);
            _builder_1.append(_compile_3);
            _builder_1.append("(\"");
            String _shortName_3 = this.typeUtils.getShortName(e.getRef(), false);
            _builder_1.append(_shortName_3);
            _builder_1.append("\"");
            {
              if (args) {
                _builder_1.append(", ");
              }
            }
            String _compile_4 = this.compile(e.getArgsList(), context);
            _builder_1.append(_compile_4);
            _builder_1.append("), operators,  ");
            String _compile_5 = this.compile(e.getArgsMap(), e.getRef(), context);
            _builder_1.append(_compile_5);
            _builder_1.append(")");
            _xifexpression_2 = _builder_1.toString();
          }
          _xblockexpression = _xifexpression_2;
        }
        _xifexpression_1 = _xblockexpression;
      } else {
        String _xifexpression_2 = null;
        if (((e.getArgsMap() != null) && (e.getArgsMap().getElements().size() > 0))) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("getOperator");
          String _shortName = this.typeUtils.getShortName(e.getRef(), false);
          _builder.append(_shortName);
          int _hashCode = e.getRef().hashCode();
          _builder.append(_hashCode);
          _builder.append("(new ");
          String _compile = this.typeCompiler.compile(e.getRef(), context, false);
          _builder.append(_compile);
          _builder.append("(\"");
          String _shortName_1 = this.typeUtils.getShortName(e.getRef(), false);
          _builder.append(_shortName_1);
          _builder.append("\"), operators,  ");
          String _compile_1 = this.compile(e.getArgsMap(), e.getRef(), context);
          _builder.append(_compile_1);
          _builder.append(")");
          _xifexpression_2 = _builder.toString();
        } else {
          String _xifexpression_3 = null;
          IQLArgumentsList _argsList = e.getArgsList();
          boolean _tripleNotEquals = (_argsList != null);
          if (_tripleNotEquals) {
            String _xblockexpression_1 = null;
            {
              JvmExecutable constructor = this.lookUp.findPublicConstructor(e.getRef(), e.getArgsList().getElements());
              int _size = e.getArgsList().getElements().size();
              boolean args = (_size > 0);
              String _xifexpression_4 = null;
              if ((constructor != null)) {
                StringConcatenation _builder_1 = new StringConcatenation();
                _builder_1.append("getOperator");
                String _shortName_2 = this.typeUtils.getShortName(e.getRef(), false);
                _builder_1.append(_shortName_2);
                int _hashCode_1 = e.getRef().hashCode();
                _builder_1.append(_hashCode_1);
                _builder_1.append("(new ");
                String _compile_2 = this.typeCompiler.compile(e.getRef(), context, false);
                _builder_1.append(_compile_2);
                _builder_1.append("(\"");
                String _shortName_3 = this.typeUtils.getShortName(e.getRef(), false);
                _builder_1.append(_shortName_3);
                _builder_1.append("\"");
                {
                  if (args) {
                    _builder_1.append(", ");
                  }
                }
                String _compile_3 = this.compile(e.getArgsList(), constructor.getParameters(), context);
                _builder_1.append(_compile_3);
                _builder_1.append("), operators)");
                _xifexpression_4 = _builder_1.toString();
              } else {
                StringConcatenation _builder_2 = new StringConcatenation();
                _builder_2.append("getOperator");
                String _shortName_4 = this.typeUtils.getShortName(e.getRef(), false);
                _builder_2.append(_shortName_4);
                int _hashCode_2 = e.getRef().hashCode();
                _builder_2.append(_hashCode_2);
                _builder_2.append("(new ");
                String _compile_4 = this.typeCompiler.compile(e.getRef(), context, false);
                _builder_2.append(_compile_4);
                _builder_2.append("(\"");
                String _shortName_5 = this.typeUtils.getShortName(e.getRef(), false);
                _builder_2.append(_shortName_5);
                _builder_2.append("\"");
                {
                  if (args) {
                    _builder_2.append(", ");
                  }
                }
                String _compile_5 = this.compile(e.getArgsList(), context);
                _builder_2.append(_compile_5);
                _builder_2.append(")>, operators)");
                _xifexpression_4 = _builder_2.toString();
              }
              _xblockexpression_1 = _xifexpression_4;
            }
            _xifexpression_3 = _xblockexpression_1;
          } else {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append("getOperator");
            String _shortName_2 = this.typeUtils.getShortName(e.getRef(), false);
            _builder_1.append(_shortName_2);
            int _hashCode_1 = e.getRef().hashCode();
            _builder_1.append(_hashCode_1);
            _builder_1.append("(new ");
            String _compile_2 = this.typeCompiler.compile(e.getRef(), context, false);
            _builder_1.append(_compile_2);
            _builder_1.append("(\"");
            String _shortName_3 = this.typeUtils.getShortName(e.getRef(), false);
            _builder_1.append(_shortName_3);
            _builder_1.append("\"), operators)");
            _xifexpression_3 = _builder_1.toString();
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    } else {
      String _xifexpression_4 = null;
      boolean _isSource = this.helper.isSource(e.getRef());
      if (_isSource) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("new ");
        String _compile_3 = this.typeCompiler.compile(e.getRef(), context, false);
        _builder_2.append(_compile_3);
        _builder_2.append("(\"");
        String _shortName_4 = this.typeUtils.getShortName(e.getRef(), false);
        _builder_2.append(_shortName_4);
        _builder_2.append("\")");
        _xifexpression_4 = _builder_2.toString();
      } else {
        _xifexpression_4 = super.compile(e, context);
      }
      _xifexpression = _xifexpression_4;
    }
    return _xifexpression;
  }
}
