package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
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
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;

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
    String _op = e.getOp();
    boolean _equals = _op.equals("->");
    if (_equals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("subscribeSink(");
      IQLExpression _leftOperand = e.getLeftOperand();
      String _compile = this.compile(_leftOperand, context);
      _builder.append(_compile, "");
      _builder.append(", ");
      IQLExpression _rightOperand = e.getRightOperand();
      String _compile_1 = this.compile(_rightOperand, context);
      _builder.append(_compile_1, "");
      _builder.append(")");
      _xifexpression = _builder.toString();
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("subscribeToSource(");
      IQLExpression _leftOperand_1 = e.getLeftOperand();
      String _compile_2 = this.compile(_leftOperand_1, context);
      _builder_1.append(_compile_2, "");
      _builder_1.append(", ");
      IQLExpression _rightOperand_1 = e.getRightOperand();
      String _compile_3 = this.compile(_rightOperand_1, context);
      _builder_1.append(_compile_3, "");
      _builder_1.append(")");
      _xifexpression = _builder_1.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLPortExpression e, final IQDLGeneratorContext context) {
    String _xblockexpression = null;
    {
      String _canonicalName = QDLSubscribableWithPort.class.getCanonicalName();
      context.addImport(_canonicalName);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("new ");
      String _simpleName = QDLSubscribableWithPort.class.getSimpleName();
      _builder.append(_simpleName, "");
      _builder.append("(");
      IQLExpression _leftOperand = e.getLeftOperand();
      String _compile = this.compile(_leftOperand, context);
      _builder.append(_compile, "");
      _builder.append(", ");
      IQLExpression _rightOperand = e.getRightOperand();
      String _compile_1 = this.compile(_rightOperand, context);
      _builder.append(_compile_1, "");
      _builder.append(")");
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  @Override
  public String compile(final IQLNewExpression e, final IQDLGeneratorContext context) {
    String _xifexpression = null;
    JvmTypeReference _ref = e.getRef();
    boolean _isOperator = this.helper.isOperator(_ref);
    if (_isOperator) {
      String _xifexpression_1 = null;
      if ((((e.getArgsList() != null) && (e.getArgsMap() != null)) && (e.getArgsMap().getElements().size() > 0))) {
        String _xblockexpression = null;
        {
          JvmTypeReference _ref_1 = e.getRef();
          IQLArgumentsList _argsList = e.getArgsList();
          EList<IQLExpression> _elements = _argsList.getElements();
          JvmExecutable constructor = this.lookUp.findPublicConstructor(_ref_1, _elements);
          IQLArgumentsList _argsList_1 = e.getArgsList();
          EList<IQLExpression> _elements_1 = _argsList_1.getElements();
          int _size = _elements_1.size();
          boolean args = (_size > 0);
          String _xifexpression_2 = null;
          if ((constructor != null)) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("getOperator");
            JvmTypeReference _ref_2 = e.getRef();
            String _shortName = this.typeUtils.getShortName(_ref_2, false);
            _builder.append(_shortName, "");
            JvmTypeReference _ref_3 = e.getRef();
            int _hashCode = _ref_3.hashCode();
            _builder.append(_hashCode, "");
            _builder.append("(new ");
            JvmTypeReference _ref_4 = e.getRef();
            String _compile = this.typeCompiler.compile(_ref_4, context, false);
            _builder.append(_compile, "");
            _builder.append("(\"");
            JvmTypeReference _ref_5 = e.getRef();
            String _shortName_1 = this.typeUtils.getShortName(_ref_5, false);
            _builder.append(_shortName_1, "");
            _builder.append("\"");
            {
              if (args) {
                _builder.append(", ");
              }
            }
            IQLArgumentsList _argsList_2 = e.getArgsList();
            EList<JvmFormalParameter> _parameters = constructor.getParameters();
            String _compile_1 = this.compile(_argsList_2, _parameters, context);
            _builder.append(_compile_1, "");
            _builder.append("), operators,  ");
            IQLArgumentsMap _argsMap = e.getArgsMap();
            JvmTypeReference _ref_6 = e.getRef();
            String _compile_2 = this.compile(_argsMap, _ref_6, context);
            _builder.append(_compile_2, "");
            _builder.append(")");
            _xifexpression_2 = _builder.toString();
          } else {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append("getOperator");
            JvmTypeReference _ref_7 = e.getRef();
            String _shortName_2 = this.typeUtils.getShortName(_ref_7, false);
            _builder_1.append(_shortName_2, "");
            JvmTypeReference _ref_8 = e.getRef();
            int _hashCode_1 = _ref_8.hashCode();
            _builder_1.append(_hashCode_1, "");
            _builder_1.append("(new ");
            JvmTypeReference _ref_9 = e.getRef();
            String _compile_3 = this.typeCompiler.compile(_ref_9, context, false);
            _builder_1.append(_compile_3, "");
            _builder_1.append("(\"");
            JvmTypeReference _ref_10 = e.getRef();
            String _shortName_3 = this.typeUtils.getShortName(_ref_10, false);
            _builder_1.append(_shortName_3, "");
            _builder_1.append("\"");
            {
              if (args) {
                _builder_1.append(", ");
              }
            }
            IQLArgumentsList _argsList_3 = e.getArgsList();
            String _compile_4 = this.compile(_argsList_3, context);
            _builder_1.append(_compile_4, "");
            _builder_1.append("), operators,  ");
            IQLArgumentsMap _argsMap_1 = e.getArgsMap();
            JvmTypeReference _ref_11 = e.getRef();
            String _compile_5 = this.compile(_argsMap_1, _ref_11, context);
            _builder_1.append(_compile_5, "");
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
          JvmTypeReference _ref_1 = e.getRef();
          String _shortName = this.typeUtils.getShortName(_ref_1, false);
          _builder.append(_shortName, "");
          JvmTypeReference _ref_2 = e.getRef();
          int _hashCode = _ref_2.hashCode();
          _builder.append(_hashCode, "");
          _builder.append("(new ");
          JvmTypeReference _ref_3 = e.getRef();
          String _compile = this.typeCompiler.compile(_ref_3, context, false);
          _builder.append(_compile, "");
          _builder.append("(\"");
          JvmTypeReference _ref_4 = e.getRef();
          String _shortName_1 = this.typeUtils.getShortName(_ref_4, false);
          _builder.append(_shortName_1, "");
          _builder.append("\"), operators,  ");
          IQLArgumentsMap _argsMap = e.getArgsMap();
          JvmTypeReference _ref_5 = e.getRef();
          String _compile_1 = this.compile(_argsMap, _ref_5, context);
          _builder.append(_compile_1, "");
          _builder.append(")");
          _xifexpression_2 = _builder.toString();
        } else {
          String _xifexpression_3 = null;
          IQLArgumentsList _argsList = e.getArgsList();
          boolean _tripleNotEquals = (_argsList != null);
          if (_tripleNotEquals) {
            String _xblockexpression_1 = null;
            {
              JvmTypeReference _ref_6 = e.getRef();
              IQLArgumentsList _argsList_1 = e.getArgsList();
              EList<IQLExpression> _elements = _argsList_1.getElements();
              JvmExecutable constructor = this.lookUp.findPublicConstructor(_ref_6, _elements);
              IQLArgumentsList _argsList_2 = e.getArgsList();
              EList<IQLExpression> _elements_1 = _argsList_2.getElements();
              int _size = _elements_1.size();
              boolean args = (_size > 0);
              String _xifexpression_4 = null;
              if ((constructor != null)) {
                StringConcatenation _builder_1 = new StringConcatenation();
                _builder_1.append("getOperator");
                JvmTypeReference _ref_7 = e.getRef();
                String _shortName_2 = this.typeUtils.getShortName(_ref_7, false);
                _builder_1.append(_shortName_2, "");
                JvmTypeReference _ref_8 = e.getRef();
                int _hashCode_1 = _ref_8.hashCode();
                _builder_1.append(_hashCode_1, "");
                _builder_1.append("(new ");
                JvmTypeReference _ref_9 = e.getRef();
                String _compile_2 = this.typeCompiler.compile(_ref_9, context, false);
                _builder_1.append(_compile_2, "");
                _builder_1.append("(\"");
                JvmTypeReference _ref_10 = e.getRef();
                String _shortName_3 = this.typeUtils.getShortName(_ref_10, false);
                _builder_1.append(_shortName_3, "");
                _builder_1.append("\"");
                {
                  if (args) {
                    _builder_1.append(", ");
                  }
                }
                IQLArgumentsList _argsList_3 = e.getArgsList();
                EList<JvmFormalParameter> _parameters = constructor.getParameters();
                String _compile_3 = this.compile(_argsList_3, _parameters, context);
                _builder_1.append(_compile_3, "");
                _builder_1.append("), operators)");
                _xifexpression_4 = _builder_1.toString();
              } else {
                StringConcatenation _builder_2 = new StringConcatenation();
                _builder_2.append("getOperator");
                JvmTypeReference _ref_11 = e.getRef();
                String _shortName_4 = this.typeUtils.getShortName(_ref_11, false);
                _builder_2.append(_shortName_4, "");
                JvmTypeReference _ref_12 = e.getRef();
                int _hashCode_2 = _ref_12.hashCode();
                _builder_2.append(_hashCode_2, "");
                _builder_2.append("(new ");
                JvmTypeReference _ref_13 = e.getRef();
                String _compile_4 = this.typeCompiler.compile(_ref_13, context, false);
                _builder_2.append(_compile_4, "");
                _builder_2.append("(\"");
                JvmTypeReference _ref_14 = e.getRef();
                String _shortName_5 = this.typeUtils.getShortName(_ref_14, false);
                _builder_2.append(_shortName_5, "");
                _builder_2.append("\"");
                {
                  if (args) {
                    _builder_2.append(", ");
                  }
                }
                IQLArgumentsList _argsList_4 = e.getArgsList();
                String _compile_5 = this.compile(_argsList_4, context);
                _builder_2.append(_compile_5, "");
                _builder_2.append(")>, operators)");
                _xifexpression_4 = _builder_2.toString();
              }
              _xblockexpression_1 = _xifexpression_4;
            }
            _xifexpression_3 = _xblockexpression_1;
          } else {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append("getOperator");
            JvmTypeReference _ref_6 = e.getRef();
            String _shortName_2 = this.typeUtils.getShortName(_ref_6, false);
            _builder_1.append(_shortName_2, "");
            JvmTypeReference _ref_7 = e.getRef();
            int _hashCode_1 = _ref_7.hashCode();
            _builder_1.append(_hashCode_1, "");
            _builder_1.append("(new ");
            JvmTypeReference _ref_8 = e.getRef();
            String _compile_2 = this.typeCompiler.compile(_ref_8, context, false);
            _builder_1.append(_compile_2, "");
            _builder_1.append("(\"");
            JvmTypeReference _ref_9 = e.getRef();
            String _shortName_3 = this.typeUtils.getShortName(_ref_9, false);
            _builder_1.append(_shortName_3, "");
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
      JvmTypeReference _ref_10 = e.getRef();
      boolean _isSource = this.helper.isSource(_ref_10);
      if (_isSource) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("new ");
        JvmTypeReference _ref_11 = e.getRef();
        String _compile_3 = this.typeCompiler.compile(_ref_11, context, false);
        _builder_2.append(_compile_3, "");
        _builder_2.append("(\"");
        JvmTypeReference _ref_12 = e.getRef();
        String _shortName_4 = this.typeUtils.getShortName(_ref_12, false);
        _builder_2.append(_shortName_4, "");
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
