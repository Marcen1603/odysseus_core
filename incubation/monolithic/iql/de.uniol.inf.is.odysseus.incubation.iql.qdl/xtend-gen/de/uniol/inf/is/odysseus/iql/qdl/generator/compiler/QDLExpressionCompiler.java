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
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.TypeResult;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.types.IQLUtils;
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.IQDLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.IQDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.QDLSubscribableWithPort;
import de.uniol.inf.is.odysseus.iql.qdl.typing.typeextension.IQDLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;
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
public class QDLExpressionCompiler extends AbstractIQLExpressionCompiler<IQDLCompilerHelper, IQDLGeneratorContext, IQDLTypeCompiler, IQDLExpressionEvaluator, IQDLTypeUtils, IQDLLookUp, IQDLTypeExtensionsDictionary> implements IQDLExpressionCompiler {
  @Inject
  public QDLExpressionCompiler(final IQDLCompilerHelper helper, final IQDLTypeCompiler typeCompiler, final IQDLExpressionEvaluator exprEvaluator, final IQDLTypeUtils typeUtils, final IQDLLookUp lookUp, final IQDLTypeExtensionsDictionary typeExtensionsDictionary) {
    super(helper, typeCompiler, exprEvaluator, typeUtils, lookUp, typeExtensionsDictionary);
  }
  
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
  
  public String compile(final IQLNewExpression e, final IQDLGeneratorContext context) {
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
  
  public String compile(final IQLMemberSelectionExpression e, final JvmTypeReference left, final JvmField field, final IQDLGeneratorContext c) {
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
          boolean _and = false;
          boolean _and_1 = false;
          boolean _isParameter = this.helper.isParameter(pName, left);
          if (!_isParameter) {
            _and_1 = false;
          } else {
            JvmTypeReference _type = field.getType();
            boolean _isJvmArray = this.helper.isJvmArray(_type);
            _and_1 = _isJvmArray;
          }
          if (!_and_1) {
            _and = false;
          } else {
            boolean _or = false;
            JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
            boolean _equals = Objects.equal(_expectedTypeRef, null);
            if (_equals) {
              _or = true;
            } else {
              JvmTypeReference _expectedTypeRef_1 = c.getExpectedTypeRef();
              boolean _isJvmArray_1 = this.helper.isJvmArray(_expectedTypeRef_1);
              boolean _not = (!_isJvmArray_1);
              _or = _not;
            }
            _and = _or;
          }
          if (_and) {
            String _xblockexpression_2 = null;
            {
              String _canonicalName = IQLUtils.class.getCanonicalName();
              c.addImport(_canonicalName);
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName = IQLUtils.class.getSimpleName();
              _builder.append(_simpleName, "");
              _builder.append(".toList(");
              IQLExpression _leftOperand = e.getLeftOperand();
              String _compile = this.compile(_leftOperand, c);
              _builder.append(_compile, "");
              _builder.append(".getParameter(\"");
              _builder.append(pName, "");
              _builder.append("\"))");
              _xblockexpression_2 = _builder.toString();
            }
            _xifexpression_2 = _xblockexpression_2;
          } else {
            String _xifexpression_3 = null;
            boolean _isParameter_1 = this.helper.isParameter(pName, left);
            if (_isParameter_1) {
              StringConcatenation _builder = new StringConcatenation();
              IQLExpression _leftOperand = e.getLeftOperand();
              String _compile = this.compile(_leftOperand, c);
              _builder.append(_compile, "");
              _builder.append(".getParameter(\"");
              _builder.append(pName, "");
              _builder.append("\")");
              _xifexpression_3 = _builder.toString();
            } else {
              _xifexpression_3 = super.compile(e, left, field, c);
            }
            _xifexpression_2 = _xifexpression_3;
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
  
  public String compile(final IQLMemberSelectionExpression e, final JvmTypeReference left, final JvmOperation method, final IQDLGeneratorContext c) {
    String _xifexpression = null;
    boolean _isOperator = this.helper.isOperator(left);
    if (_isOperator) {
      String _xifexpression_1 = null;
      boolean _and = false;
      boolean _and_1 = false;
      boolean _isParameterGetter = this.helper.isParameterGetter(method, left);
      if (!_isParameterGetter) {
        _and_1 = false;
      } else {
        JvmTypeReference _returnType = method.getReturnType();
        boolean _isJvmArray = this.helper.isJvmArray(_returnType);
        _and_1 = _isJvmArray;
      }
      if (!_and_1) {
        _and = false;
      } else {
        boolean _or = false;
        JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
        boolean _equals = Objects.equal(_expectedTypeRef, null);
        if (_equals) {
          _or = true;
        } else {
          JvmTypeReference _expectedTypeRef_1 = c.getExpectedTypeRef();
          boolean _isJvmArray_1 = this.helper.isJvmArray(_expectedTypeRef_1);
          boolean _not = (!_isJvmArray_1);
          _or = _not;
        }
        _and = _or;
      }
      if (_and) {
        String _xblockexpression = null;
        {
          String name = this.helper.getParameterOfGetter(method);
          String _canonicalName = IQLUtils.class.getCanonicalName();
          c.addImport(_canonicalName);
          StringConcatenation _builder = new StringConcatenation();
          String _simpleName = IQLUtils.class.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append(".toList(");
          IQLExpression _leftOperand = e.getLeftOperand();
          String _compile = this.compile(_leftOperand, c);
          _builder.append(_compile, "");
          _builder.append(".getParameter(\"");
          _builder.append(name, "");
          _builder.append("\"))");
          _xblockexpression = _builder.toString();
        }
        _xifexpression_1 = _xblockexpression;
      } else {
        String _xifexpression_2 = null;
        boolean _isParameterGetter_1 = this.helper.isParameterGetter(method, left);
        if (_isParameterGetter_1) {
          String _xblockexpression_1 = null;
          {
            String name = this.helper.getParameterOfGetter(method);
            StringConcatenation _builder = new StringConcatenation();
            IQLExpression _leftOperand = e.getLeftOperand();
            String _compile = this.compile(_leftOperand, c);
            _builder.append(_compile, "");
            _builder.append(".getParameter(\"");
            _builder.append(name, "");
            _builder.append("\")");
            _xblockexpression_1 = _builder.toString();
          }
          _xifexpression_2 = _xblockexpression_1;
        } else {
          String _xifexpression_3 = null;
          boolean _isParameterSetter = this.helper.isParameterSetter(method, left);
          if (_isParameterSetter) {
            String _xblockexpression_2 = null;
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
              _xblockexpression_2 = _builder.toString();
            }
            _xifexpression_3 = _xblockexpression_2;
          } else {
            _xifexpression_3 = super.compile(e, left, method, c);
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    } else {
      _xifexpression = super.compile(e, left, method, c);
    }
    return _xifexpression;
  }
  
  public String compileAssignmentExpr(final IQLAssignmentExpression e, final IQLMemberSelectionExpression selExpr, final IQDLGeneratorContext c) {
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
        TypeResult leftType = this.exprEvaluator.eval(_leftOperand);
        IQLExpression _rightOperand = e.getRightOperand();
        TypeResult rightType = this.exprEvaluator.eval(_rightOperand);
        String _xifexpression_1 = null;
        boolean _and_1 = false;
        boolean _and_2 = false;
        boolean _and_3 = false;
        boolean _and_4 = false;
        boolean _and_5 = false;
        boolean _isNull = leftType.isNull();
        boolean _not = (!_isNull);
        if (!_not) {
          _and_5 = false;
        } else {
          JvmTypeReference _ref = leftType.getRef();
          boolean _isOperator = this.helper.isOperator(_ref);
          _and_5 = _isOperator;
        }
        if (!_and_5) {
          _and_4 = false;
        } else {
          String _simpleName = field.getSimpleName();
          JvmTypeReference _ref_1 = leftType.getRef();
          boolean _isParameter = this.helper.isParameter(_simpleName, _ref_1);
          _and_4 = _isParameter;
        }
        if (!_and_4) {
          _and_3 = false;
        } else {
          JvmTypeReference _type = field.getType();
          boolean _isJvmArray = this.helper.isJvmArray(_type);
          _and_3 = _isJvmArray;
        }
        if (!_and_3) {
          _and_2 = false;
        } else {
          boolean _isNull_1 = rightType.isNull();
          boolean _not_1 = (!_isNull_1);
          _and_2 = _not_1;
        }
        if (!_and_2) {
          _and_1 = false;
        } else {
          JvmTypeReference _ref_2 = rightType.getRef();
          boolean _isJvmArray_1 = this.helper.isJvmArray(_ref_2);
          boolean _not_2 = (!_isJvmArray_1);
          _and_1 = _not_2;
        }
        if (_and_1) {
          String _xblockexpression_1 = null;
          {
            String _canonicalName = IQLUtils.class.getCanonicalName();
            c.addImport(_canonicalName);
            JvmTypeReference _type_1 = field.getType();
            int dim = this.typeUtils.getArrayDim(_type_1);
            StringConcatenation _builder = new StringConcatenation();
            IQLExpression _leftOperand_1 = selExpr.getLeftOperand();
            String _compile = this.compile(_leftOperand_1, c);
            _builder.append(_compile, "");
            _builder.append(".setParameter(\"");
            String _simpleName_1 = field.getSimpleName();
            _builder.append(_simpleName_1, "");
            _builder.append("\",");
            String _simpleName_2 = IQLUtils.class.getSimpleName();
            _builder.append(_simpleName_2, "");
            _builder.append(".toArray");
            _builder.append(dim, "");
            _builder.append("(");
            IQLExpression _rightOperand_1 = e.getRightOperand();
            String _compile_1 = this.compile(_rightOperand_1, c);
            _builder.append(_compile_1, "");
            _builder.append("))");
            _xblockexpression_1 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_1;
        } else {
          String _xifexpression_2 = null;
          boolean _and_6 = false;
          boolean _and_7 = false;
          boolean _isNull_2 = leftType.isNull();
          boolean _not_3 = (!_isNull_2);
          if (!_not_3) {
            _and_7 = false;
          } else {
            JvmTypeReference _ref_3 = leftType.getRef();
            boolean _isOperator_1 = this.helper.isOperator(_ref_3);
            _and_7 = _isOperator_1;
          }
          if (!_and_7) {
            _and_6 = false;
          } else {
            String _simpleName_1 = field.getSimpleName();
            JvmTypeReference _ref_4 = leftType.getRef();
            boolean _isParameter_1 = this.helper.isParameter(_simpleName_1, _ref_4);
            _and_6 = _isParameter_1;
          }
          if (_and_6) {
            StringConcatenation _builder = new StringConcatenation();
            IQLExpression _leftOperand_1 = selExpr.getLeftOperand();
            String _compile = this.compile(_leftOperand_1, c);
            _builder.append(_compile, "");
            _builder.append(".setParameter(\"");
            String _simpleName_2 = field.getSimpleName();
            _builder.append(_simpleName_2, "");
            _builder.append("\",");
            IQLExpression _rightOperand_1 = e.getRightOperand();
            String _compile_1 = this.compile(_rightOperand_1, c);
            _builder.append(_compile_1, "");
            _builder.append(")");
            _xifexpression_2 = _builder.toString();
          } else {
            _xifexpression_2 = super.compileAssignmentExpr(e, selExpr, c);
          }
          _xifexpression_1 = _xifexpression_2;
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
