package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.typing.TypeResult;
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.QDLSubscribableWithPort;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLExpressionParser;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

@SuppressWarnings("all")
public class QDLExpressionCompiler extends AbstractIQLExpressionCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler, QDLExpressionParser, QDLTypeUtils, QDLLookUp, QDLTypeExtensionsFactory> {
  @Inject
  public QDLExpressionCompiler(final QDLCompilerHelper helper, final QDLTypeCompiler typeCompiler, final QDLExpressionParser exprParser, final QDLTypeUtils typeUtils, final QDLLookUp lookUp, final QDLTypeExtensionsFactory typeOperatorsFactory) {
    super(helper, typeCompiler, exprParser, typeUtils, lookUp, typeOperatorsFactory);
  }
  
  public String compile(final IQLExpression e, final QDLGeneratorContext context) {
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
  
  public String compile(final IQLSubscribeExpression e, final QDLGeneratorContext context) {
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
  
  public String compile(final IQLPortExpression e, final QDLGeneratorContext context) {
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
  
  public String compile(final IQLNewExpression e, final QDLGeneratorContext context) {
    String _xifexpression = null;
    JvmTypeReference _ref = e.getRef();
    boolean _isOperator = this.helper.isOperator(_ref);
    if (_isOperator) {
      String _xifexpression_1 = null;
      boolean _and = false;
      IQLArgumentsMap _argsMap = e.getArgsMap();
      boolean _notEquals = (!Objects.equal(_argsMap, null));
      if (!_notEquals) {
        _and = false;
      } else {
        IQLArgumentsMap _argsMap_1 = e.getArgsMap();
        EList<IQLArgumentsMapKeyValue> _elements = _argsMap_1.getElements();
        int _size = _elements.size();
        boolean _greaterThan = (_size > 0);
        _and = _greaterThan;
      }
      if (_and) {
        String _xblockexpression = null;
        {
          JvmTypeReference _ref_1 = e.getRef();
          IQLArgumentsList _argsList = e.getArgsList();
          EList<IQLExpression> _elements_1 = _argsList.getElements();
          JvmExecutable constructor = this.lookUp.findPublicConstructor(_ref_1, _elements_1);
          IQLArgumentsList _argsList_1 = e.getArgsList();
          EList<IQLExpression> _elements_2 = _argsList_1.getElements();
          int _size_1 = _elements_2.size();
          boolean args = (_size_1 > 0);
          String _xifexpression_2 = null;
          boolean _notEquals_1 = (!Objects.equal(constructor, null));
          if (_notEquals_1) {
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
            IQLArgumentsMap _argsMap_2 = e.getArgsMap();
            JvmTypeReference _ref_6 = e.getRef();
            String _compile_2 = this.compile(_argsMap_2, _ref_6, context);
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
            IQLArgumentsMap _argsMap_3 = e.getArgsMap();
            JvmTypeReference _ref_11 = e.getRef();
            String _compile_5 = this.compile(_argsMap_3, _ref_11, context);
            _builder_1.append(_compile_5, "");
            _builder_1.append(")");
            _xifexpression_2 = _builder_1.toString();
          }
          _xblockexpression = _xifexpression_2;
        }
        _xifexpression_1 = _xblockexpression;
      } else {
        String _xifexpression_2 = null;
        IQLArgumentsList _argsList = e.getArgsList();
        boolean _notEquals_1 = (!Objects.equal(_argsList, null));
        if (_notEquals_1) {
          String _xblockexpression_1 = null;
          {
            JvmTypeReference _ref_1 = e.getRef();
            IQLArgumentsList _argsList_1 = e.getArgsList();
            EList<IQLExpression> _elements_1 = _argsList_1.getElements();
            JvmExecutable constructor = this.lookUp.findPublicConstructor(_ref_1, _elements_1);
            IQLArgumentsList _argsList_2 = e.getArgsList();
            EList<IQLExpression> _elements_2 = _argsList_2.getElements();
            int _size_1 = _elements_2.size();
            boolean args = (_size_1 > 0);
            String _xifexpression_3 = null;
            boolean _notEquals_2 = (!Objects.equal(constructor, null));
            if (_notEquals_2) {
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
              IQLArgumentsList _argsList_3 = e.getArgsList();
              EList<JvmFormalParameter> _parameters = constructor.getParameters();
              String _compile_1 = this.compile(_argsList_3, _parameters, context);
              _builder.append(_compile_1, "");
              _builder.append("), operators)");
              _xifexpression_3 = _builder.toString();
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
              _builder_1.append("\"");
              {
                if (args) {
                  _builder_1.append(", ");
                }
              }
              IQLArgumentsList _argsList_4 = e.getArgsList();
              String _compile_3 = this.compile(_argsList_4, context);
              _builder_1.append(_compile_3, "");
              _builder_1.append(")>, operators)");
              _xifexpression_3 = _builder_1.toString();
            }
            _xblockexpression_1 = _xifexpression_3;
          }
          _xifexpression_2 = _xblockexpression_1;
        } else {
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
          _builder.append("\"), operators)");
          _xifexpression_2 = _builder.toString();
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    } else {
      String _xifexpression_3 = null;
      JvmTypeReference _ref_5 = e.getRef();
      boolean _isSource = this.helper.isSource(_ref_5);
      if (_isSource) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("getSource(\"");
        JvmTypeReference _ref_6 = e.getRef();
        String _shortName_2 = this.typeUtils.getShortName(_ref_6, false);
        _builder_1.append(_shortName_2, "");
        _builder_1.append("\")");
        _xifexpression_3 = _builder_1.toString();
      } else {
        _xifexpression_3 = super.compile(e, context);
      }
      _xifexpression = _xifexpression_3;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLMemberSelectionExpression e, final JvmTypeReference left, final JvmField field, final QDLGeneratorContext c) {
    String _xifexpression = null;
    boolean _isSource = this.helper.isSource(left);
    if (_isSource) {
      String _xblockexpression = null;
      {
        String name = field.getSimpleName();
        String _xifexpression_1 = null;
        boolean _isSourceAttribute = this.helper.isSourceAttribute(left, name);
        if (_isSourceAttribute) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("\"");
          _builder.append(name, "");
          _builder.append("\"");
          _xifexpression_1 = _builder.toString();
        } else {
          _xifexpression_1 = super.compile(e, left, field, c);
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    } else {
      String _xifexpression_1 = null;
      boolean _isOperator = this.helper.isOperator(left);
      if (_isOperator) {
        String _xblockexpression_1 = null;
        {
          String pName = field.getSimpleName();
          String _xifexpression_2 = null;
          boolean _isParameter = this.helper.isParameter(pName, left);
          if (_isParameter) {
            StringConcatenation _builder = new StringConcatenation();
            IQLExpression _leftOperand = e.getLeftOperand();
            String _compile = this.compile(_leftOperand, c);
            _builder.append(_compile, "");
            _builder.append(".getParameter(\"");
            _builder.append(pName, "");
            _builder.append("\")");
            _xifexpression_2 = _builder.toString();
          } else {
            _xifexpression_2 = super.compile(e, left, field, c);
          }
          _xblockexpression_1 = _xifexpression_2;
        }
        _xifexpression_1 = _xblockexpression_1;
      } else {
        _xifexpression_1 = super.compile(e, left, field, c);
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLMemberSelectionExpression e, final JvmTypeReference left, final JvmOperation method, final QDLGeneratorContext c) {
    String _xifexpression = null;
    boolean _isOperator = this.helper.isOperator(left);
    if (_isOperator) {
      String _xifexpression_1 = null;
      boolean _isParameterGetter = this.helper.isParameterGetter(method, left);
      if (_isParameterGetter) {
        String _xblockexpression = null;
        {
          String name = this.helper.getParameterOfGetter(method);
          StringConcatenation _builder = new StringConcatenation();
          IQLExpression _leftOperand = e.getLeftOperand();
          String _compile = this.compile(_leftOperand, c);
          _builder.append(_compile, "");
          _builder.append(".getParameter(\"");
          _builder.append(name, "");
          _builder.append("\")");
          _xblockexpression = _builder.toString();
        }
        _xifexpression_1 = _xblockexpression;
      } else {
        String _xifexpression_2 = null;
        boolean _isParameterSetter = this.helper.isParameterSetter(method, left);
        if (_isParameterSetter) {
          String _xblockexpression_1 = null;
          {
            String name = this.helper.getParameterOfSetter(method);
            StringConcatenation _builder = new StringConcatenation();
            IQLExpression _leftOperand = e.getLeftOperand();
            String _compile = this.compile(_leftOperand, c);
            _builder.append(_compile, "");
            _builder.append(".setParameter(\"");
            _builder.append(name, "");
            _builder.append("\",");
            IQLMemberSelection _sel = e.getSel();
            IQLArgumentsList _args = _sel.getArgs();
            EList<JvmFormalParameter> _parameters = method.getParameters();
            String _compile_1 = this.compile(_args, _parameters, c);
            _builder.append(_compile_1, "");
            _builder.append(")");
            _xblockexpression_1 = _builder.toString();
          }
          _xifexpression_2 = _xblockexpression_1;
        } else {
          _xifexpression_2 = super.compile(e, left, method, c);
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    } else {
      _xifexpression = super.compile(e, left, method, c);
    }
    return _xifexpression;
  }
  
  public String compileAssignmentExpr(final IQLAssignmentExpression e, final IQLMemberSelectionExpression selExpr, final QDLGeneratorContext c) {
    String _xifexpression = null;
    boolean _and = false;
    String _op = e.getOp();
    boolean _equals = _op.equals("=");
    if (!_equals) {
      _and = false;
    } else {
      IQLMemberSelection _sel = selExpr.getSel();
      JvmMember _member = _sel.getMember();
      _and = (_member instanceof JvmField);
    }
    if (_and) {
      String _xblockexpression = null;
      {
        IQLMemberSelection _sel_1 = selExpr.getSel();
        JvmMember _member_1 = _sel_1.getMember();
        JvmField field = ((JvmField) _member_1);
        IQLExpression _leftOperand = selExpr.getLeftOperand();
        TypeResult left = this.exprParser.getType(_leftOperand);
        String _xifexpression_1 = null;
        boolean _and_1 = false;
        boolean _and_2 = false;
        boolean _isNull = left.isNull();
        boolean _not = (!_isNull);
        if (!_not) {
          _and_2 = false;
        } else {
          JvmTypeReference _ref = left.getRef();
          boolean _isOperator = this.helper.isOperator(_ref);
          _and_2 = _isOperator;
        }
        if (!_and_2) {
          _and_1 = false;
        } else {
          String _simpleName = field.getSimpleName();
          JvmTypeReference _ref_1 = left.getRef();
          boolean _isParameter = this.helper.isParameter(_simpleName, _ref_1);
          _and_1 = _isParameter;
        }
        if (_and_1) {
          StringConcatenation _builder = new StringConcatenation();
          IQLExpression _leftOperand_1 = selExpr.getLeftOperand();
          String _compile = this.compile(_leftOperand_1, c);
          _builder.append(_compile, "");
          _builder.append(".setParameter(\"");
          String _simpleName_1 = field.getSimpleName();
          _builder.append(_simpleName_1, "");
          _builder.append("\",");
          IQLExpression _rightOperand = e.getRightOperand();
          String _compile_1 = this.compile(_rightOperand, c);
          _builder.append(_compile_1, "");
          _builder.append(")");
          _xifexpression_1 = _builder.toString();
        } else {
          _xifexpression_1 = super.compileAssignmentExpr(e, selExpr, c);
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = super.compileAssignmentExpr(e, selExpr, c);
    }
    return _xifexpression;
  }
}
