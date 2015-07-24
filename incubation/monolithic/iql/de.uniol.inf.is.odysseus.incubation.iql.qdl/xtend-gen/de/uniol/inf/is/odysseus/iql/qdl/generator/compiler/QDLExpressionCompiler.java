package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttributeSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew;
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
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

@SuppressWarnings("all")
public class QDLExpressionCompiler extends AbstractIQLExpressionCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler, QDLExpressionParser, QDLTypeFactory, QDLLookUp> {
  @Inject
  public QDLExpressionCompiler(final QDLCompilerHelper helper, final QDLTypeCompiler typeCompiler, final QDLExpressionParser exprParser, final QDLTypeFactory factory, final QDLLookUp lookUp) {
    super(helper, typeCompiler, exprParser, factory, lookUp);
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
  
  public String compile(final IQLTerminalExpressionNew e, final QDLGeneratorContext context) {
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
          JvmExecutable constructor = this.lookUp.findConstructor(_ref_1, _argsList);
          String _xifexpression_2 = null;
          boolean _notEquals_1 = (!Objects.equal(constructor, null));
          if (_notEquals_1) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("getOperator");
            JvmTypeReference _ref_2 = e.getRef();
            String _shortName = this.factory.getShortName(_ref_2, false);
            _builder.append(_shortName, "");
            JvmTypeReference _ref_3 = e.getRef();
            int _hashCode = _ref_3.hashCode();
            _builder.append(_hashCode, "");
            _builder.append("(new ");
            JvmTypeReference _ref_4 = e.getRef();
            String _compile = this.typeCompiler.compile(_ref_4, context, false);
            _builder.append(_compile, "");
            _builder.append("(");
            IQLArgumentsList _argsList_1 = e.getArgsList();
            EList<JvmFormalParameter> _parameters = constructor.getParameters();
            String _compile_1 = this.compile(_argsList_1, _parameters, context);
            _builder.append(_compile_1, "");
            _builder.append("), operators,  ");
            IQLArgumentsMap _argsMap_2 = e.getArgsMap();
            JvmTypeReference _ref_5 = e.getRef();
            String _compile_2 = this.compile(_argsMap_2, _ref_5, context);
            _builder.append(_compile_2, "");
            _builder.append(")");
            _xifexpression_2 = _builder.toString();
          } else {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append("getOperator");
            JvmTypeReference _ref_6 = e.getRef();
            String _shortName_1 = this.factory.getShortName(_ref_6, false);
            _builder_1.append(_shortName_1, "");
            JvmTypeReference _ref_7 = e.getRef();
            int _hashCode_1 = _ref_7.hashCode();
            _builder_1.append(_hashCode_1, "");
            _builder_1.append("(new ");
            JvmTypeReference _ref_8 = e.getRef();
            String _compile_3 = this.typeCompiler.compile(_ref_8, context, false);
            _builder_1.append(_compile_3, "");
            _builder_1.append("(");
            IQLArgumentsList _argsList_2 = e.getArgsList();
            String _compile_4 = this.compile(_argsList_2, context);
            _builder_1.append(_compile_4, "");
            _builder_1.append("), operators,  ");
            IQLArgumentsMap _argsMap_3 = e.getArgsMap();
            JvmTypeReference _ref_9 = e.getRef();
            String _compile_5 = this.compile(_argsMap_3, _ref_9, context);
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
            JvmExecutable constructor = this.lookUp.findConstructor(_ref_1, _argsList_1);
            String _xifexpression_3 = null;
            boolean _notEquals_2 = (!Objects.equal(constructor, null));
            if (_notEquals_2) {
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("getOperator");
              JvmTypeReference _ref_2 = e.getRef();
              String _shortName = this.factory.getShortName(_ref_2, false);
              _builder.append(_shortName, "");
              JvmTypeReference _ref_3 = e.getRef();
              int _hashCode = _ref_3.hashCode();
              _builder.append(_hashCode, "");
              _builder.append("(new ");
              JvmTypeReference _ref_4 = e.getRef();
              String _compile = this.typeCompiler.compile(_ref_4, context, false);
              _builder.append(_compile, "");
              _builder.append("(");
              IQLArgumentsList _argsList_2 = e.getArgsList();
              EList<JvmFormalParameter> _parameters = constructor.getParameters();
              String _compile_1 = this.compile(_argsList_2, _parameters, context);
              _builder.append(_compile_1, "");
              _builder.append("), operators)");
              _xifexpression_3 = _builder.toString();
            } else {
              StringConcatenation _builder_1 = new StringConcatenation();
              _builder_1.append("getOperator");
              JvmTypeReference _ref_5 = e.getRef();
              String _shortName_1 = this.factory.getShortName(_ref_5, false);
              _builder_1.append(_shortName_1, "");
              JvmTypeReference _ref_6 = e.getRef();
              int _hashCode_1 = _ref_6.hashCode();
              _builder_1.append(_hashCode_1, "");
              _builder_1.append("(new ");
              JvmTypeReference _ref_7 = e.getRef();
              String _compile_2 = this.typeCompiler.compile(_ref_7, context, false);
              _builder_1.append(_compile_2, "");
              _builder_1.append("(");
              IQLArgumentsList _argsList_3 = e.getArgsList();
              String _compile_3 = this.compile(_argsList_3, context);
              _builder_1.append(_compile_3, "");
              _builder_1.append("), operators)");
              _xifexpression_3 = _builder_1.toString();
            }
            _xblockexpression_1 = _xifexpression_3;
          }
          _xifexpression_2 = _xblockexpression_1;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("getOperator");
          JvmTypeReference _ref_1 = e.getRef();
          String _shortName = this.factory.getShortName(_ref_1, false);
          _builder.append(_shortName, "");
          JvmTypeReference _ref_2 = e.getRef();
          int _hashCode = _ref_2.hashCode();
          _builder.append(_hashCode, "");
          _builder.append("(new ");
          JvmTypeReference _ref_3 = e.getRef();
          String _compile = this.typeCompiler.compile(_ref_3, context, false);
          _builder.append(_compile, "");
          _builder.append("())");
          _xifexpression_2 = _builder.toString();
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    } else {
      String _xifexpression_3 = null;
      JvmTypeReference _ref_4 = e.getRef();
      boolean _isSource = this.helper.isSource(_ref_4);
      if (_isSource) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("getSource(\"");
        JvmTypeReference _ref_5 = e.getRef();
        String _shortName_1 = this.factory.getShortName(_ref_5, false);
        _builder_1.append(_shortName_1, "");
        _builder_1.append("\")");
        _xifexpression_3 = _builder_1.toString();
      } else {
        _xifexpression_3 = super.compile(e, context);
      }
      _xifexpression = _xifexpression_3;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLAttributeSelection a, final JvmTypeReference left, final IQLMemberSelectionExpression expr, final QDLGeneratorContext context) {
    String _xifexpression = null;
    boolean _isSource = this.helper.isSource(left);
    if (_isSource) {
      String _xblockexpression = null;
      {
        JvmField _var = a.getVar();
        String name = _var.getSimpleName();
        String _xifexpression_1 = null;
        boolean _isSourceAttribute = this.helper.isSourceAttribute(left, name);
        if (_isSourceAttribute) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("\"");
          _builder.append(name, "");
          _builder.append("\"");
          _xifexpression_1 = _builder.toString();
        } else {
          _xifexpression_1 = super.compile(expr, context);
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
          JvmField _var = a.getVar();
          String pName = _var.getSimpleName();
          String _xifexpression_2 = null;
          boolean _isParameter = this.helper.isParameter(pName, left);
          if (_isParameter) {
            String _xifexpression_3 = null;
            EObject _eContainer = expr.eContainer();
            if ((_eContainer instanceof IQLAssignmentExpression)) {
              String _xblockexpression_2 = null;
              {
                String setter = this.helper.getParameterSetter(pName, left);
                StringConcatenation _builder = new StringConcatenation();
                IQLExpression _leftOperand = expr.getLeftOperand();
                String _compile = this.compile(_leftOperand, context);
                _builder.append(_compile, "");
                _builder.append(".");
                _builder.append(setter, "");
                _xblockexpression_2 = _builder.toString();
              }
              _xifexpression_3 = _xblockexpression_2;
            } else {
              String _xblockexpression_3 = null;
              {
                String getter = this.helper.getParameterGetter(pName, left);
                StringConcatenation _builder = new StringConcatenation();
                IQLExpression _leftOperand = expr.getLeftOperand();
                String _compile = this.compile(_leftOperand, context);
                _builder.append(_compile, "");
                _builder.append(".");
                _builder.append(getter, "");
                _builder.append("()");
                _xblockexpression_3 = _builder.toString();
              }
              _xifexpression_3 = _xblockexpression_3;
            }
            _xifexpression_2 = _xifexpression_3;
          } else {
            _xifexpression_2 = super.compile(expr, context);
          }
          _xblockexpression_1 = _xifexpression_2;
        }
        _xifexpression_1 = _xblockexpression_1;
      } else {
        _xifexpression_1 = super.compile(expr, context);
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLMethodSelection m, final JvmTypeReference left, final IQLMemberSelectionExpression expr, final QDLGeneratorContext context) {
    String _xifexpression = null;
    boolean _isOperator = this.helper.isOperator(left);
    if (_isOperator) {
      String _xifexpression_1 = null;
      boolean _isParameterGetter = this.helper.isParameterGetter(m, left);
      if (_isParameterGetter) {
        String _xblockexpression = null;
        {
          String getter = this.helper.getParameterGetter(m, left);
          StringConcatenation _builder = new StringConcatenation();
          IQLExpression _leftOperand = expr.getLeftOperand();
          String _compile = this.compile(_leftOperand, context);
          _builder.append(_compile, "");
          _builder.append(".");
          _builder.append(getter, "");
          _builder.append("()");
          _xblockexpression = _builder.toString();
        }
        _xifexpression_1 = _xblockexpression;
      } else {
        String _xifexpression_2 = null;
        boolean _isParameterSetter = this.helper.isParameterSetter(m, left);
        if (_isParameterSetter) {
          String _xblockexpression_1 = null;
          {
            String setter = this.helper.getParameterSetter(m, left);
            String _method = m.getMethod();
            IQLArgumentsList _args = m.getArgs();
            JvmOperation method = this.lookUp.findMethod(left, _method, _args);
            String _xifexpression_3 = null;
            boolean _notEquals = (!Objects.equal(method, null));
            if (_notEquals) {
              StringConcatenation _builder = new StringConcatenation();
              IQLExpression _leftOperand = expr.getLeftOperand();
              String _compile = this.compile(_leftOperand, context);
              _builder.append(_compile, "");
              _builder.append(".");
              _builder.append(setter, "");
              _builder.append("(");
              IQLArgumentsList _args_1 = m.getArgs();
              EList<JvmFormalParameter> _parameters = method.getParameters();
              String _compile_1 = this.compile(_args_1, _parameters, context);
              _builder.append(_compile_1, "");
              _builder.append(")");
              _xifexpression_3 = _builder.toString();
            } else {
              StringConcatenation _builder_1 = new StringConcatenation();
              IQLExpression _leftOperand_1 = expr.getLeftOperand();
              String _compile_2 = this.compile(_leftOperand_1, context);
              _builder_1.append(_compile_2, "");
              _builder_1.append(".");
              _builder_1.append(setter, "");
              _builder_1.append("(");
              IQLArgumentsList _args_2 = m.getArgs();
              String _compile_3 = this.compile(_args_2, context);
              _builder_1.append(_compile_3, "");
              _builder_1.append(")");
              _xifexpression_3 = _builder_1.toString();
            }
            _xblockexpression_1 = _xifexpression_3;
          }
          _xifexpression_2 = _xblockexpression_1;
        } else {
          _xifexpression_2 = super.compile(m, left, expr, context);
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    } else {
      _xifexpression = super.compile(m, left, expr, context);
    }
    return _xifexpression;
  }
  
  public String compile(final IQLAssignmentExpression expr, final QDLGeneratorContext context) {
    String _xifexpression = null;
    IQLExpression _leftOperand = expr.getLeftOperand();
    if ((_leftOperand instanceof IQLMemberSelectionExpression)) {
      String _xblockexpression = null;
      {
        IQLExpression _leftOperand_1 = expr.getLeftOperand();
        IQLMemberSelectionExpression selExpr = ((IQLMemberSelectionExpression) _leftOperand_1);
        String _xifexpression_1 = null;
        IQLMemberSelection _rightOperand = selExpr.getRightOperand();
        if ((_rightOperand instanceof IQLAttributeSelection)) {
          String _xblockexpression_1 = null;
          {
            IQLMemberSelection _rightOperand_1 = selExpr.getRightOperand();
            IQLAttributeSelection attr = ((IQLAttributeSelection) _rightOperand_1);
            IQLExpression _leftOperand_2 = selExpr.getLeftOperand();
            TypeResult left = this.exprParser.getType(_leftOperand_2);
            String _xifexpression_2 = null;
            boolean _and = false;
            boolean _and_1 = false;
            boolean _isNull = left.isNull();
            boolean _not = (!_isNull);
            if (!_not) {
              _and_1 = false;
            } else {
              JvmTypeReference _ref = left.getRef();
              boolean _isOperator = this.helper.isOperator(_ref);
              _and_1 = _isOperator;
            }
            if (!_and_1) {
              _and = false;
            } else {
              JvmField _var = attr.getVar();
              String _simpleName = _var.getSimpleName();
              JvmTypeReference _ref_1 = left.getRef();
              boolean _isParameter = this.helper.isParameter(_simpleName, _ref_1);
              _and = _isParameter;
            }
            if (_and) {
              StringConcatenation _builder = new StringConcatenation();
              IQLExpression _leftOperand_3 = expr.getLeftOperand();
              String _compile = this.compile(_leftOperand_3, context);
              _builder.append(_compile, "");
              _builder.append("(");
              IQLExpression _rightOperand_2 = expr.getRightOperand();
              String _compile_1 = this.compile(_rightOperand_2, context);
              _builder.append(_compile_1, "");
              _builder.append(")");
              _xifexpression_2 = _builder.toString();
            } else {
              _xifexpression_2 = super.compile(expr, context);
            }
            _xblockexpression_1 = _xifexpression_2;
          }
          _xifexpression_1 = _xblockexpression_1;
        } else {
          _xifexpression_1 = super.compile(expr, context);
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = super.compile(expr, context);
    }
    return _xifexpression;
  }
}
