package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttributeSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionChar;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionDouble;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionInt;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionNull;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionRange;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionString;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionParenthesis;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionSuper;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionThis;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionVariable;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.types.IQLUtils;
import de.uniol.inf.is.odysseus.iql.basic.types.Range;
import de.uniol.inf.is.odysseus.iql.basic.typing.TypeResult;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.IIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public abstract class AbstractIQLExpressionCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, E extends IIQLExpressionParser, F extends IIQLTypeFactory, L extends IIQLLookUp> implements IIQLExpressionCompiler<G> {
  protected H helper;
  
  protected T typeCompiler;
  
  protected E exprParser;
  
  protected F factory;
  
  protected L lookUp;
  
  public AbstractIQLExpressionCompiler(final H helper, final T typeCompiler, final E exprParser, final F factory, final L lookUp) {
    this.helper = helper;
    this.typeCompiler = typeCompiler;
    this.exprParser = exprParser;
    this.factory = factory;
    this.lookUp = lookUp;
  }
  
  public String compile(final IQLExpression expr, final G context) {
    if ((expr instanceof IQLAssignmentExpression)) {
      return this.compile(((IQLAssignmentExpression) expr), context);
    } else {
      if ((expr instanceof IQLLogicalOrExpression)) {
        return this.compile(((IQLLogicalOrExpression) expr), context);
      } else {
        if ((expr instanceof IQLLogicalAndExpression)) {
          return this.compile(((IQLLogicalAndExpression) expr), context);
        } else {
          if ((expr instanceof IQLEqualityExpression)) {
            return this.compile(((IQLEqualityExpression) expr), context);
          } else {
            if ((expr instanceof IQLRelationalExpression)) {
              return this.compile(((IQLRelationalExpression) expr), context);
            } else {
              if ((expr instanceof IQLInstanceOfExpression)) {
                return this.compile(((IQLInstanceOfExpression) expr), context);
              } else {
                if ((expr instanceof IQLAdditiveExpression)) {
                  return this.compile(((IQLAdditiveExpression) expr), context);
                } else {
                  if ((expr instanceof IQLMultiplicativeExpression)) {
                    return this.compile(((IQLMultiplicativeExpression) expr), context);
                  } else {
                    if ((expr instanceof IQLPlusMinusExpression)) {
                      return this.compile(((IQLPlusMinusExpression) expr), context);
                    } else {
                      if ((expr instanceof IQLBooleanNotExpression)) {
                        return this.compile(((IQLBooleanNotExpression) expr), context);
                      } else {
                        if ((expr instanceof IQLPrefixExpression)) {
                          return this.compile(((IQLPrefixExpression) expr), context);
                        } else {
                          if ((expr instanceof IQLTypeCastExpression)) {
                            return this.compile(((IQLTypeCastExpression) expr), context);
                          } else {
                            if ((expr instanceof IQLPostfixExpression)) {
                              return this.compile(((IQLPostfixExpression) expr), context);
                            } else {
                              if ((expr instanceof IQLArrayExpression)) {
                                return this.compile(((IQLArrayExpression) expr), context);
                              } else {
                                if ((expr instanceof IQLMemberSelectionExpression)) {
                                  return this.compile(((IQLMemberSelectionExpression) expr), context);
                                } else {
                                  if ((expr instanceof IQLTerminalExpressionVariable)) {
                                    return this.compile(((IQLTerminalExpressionVariable) expr), context);
                                  } else {
                                    if ((expr instanceof IQLTerminalExpressionThis)) {
                                      return this.compile(((IQLTerminalExpressionThis) expr), context);
                                    } else {
                                      if ((expr instanceof IQLTerminalExpressionSuper)) {
                                        return this.compile(((IQLTerminalExpressionSuper) expr), context);
                                      } else {
                                        if ((expr instanceof IQLTerminalExpressionParenthesis)) {
                                          return this.compile(((IQLTerminalExpressionParenthesis) expr), context);
                                        } else {
                                          if ((expr instanceof IQLTerminalExpressionNew)) {
                                            return this.compile(((IQLTerminalExpressionNew) expr), context);
                                          } else {
                                            if ((expr instanceof IQLLiteralExpressionInt)) {
                                              return this.compile(((IQLLiteralExpressionInt) expr), context);
                                            } else {
                                              if ((expr instanceof IQLLiteralExpressionDouble)) {
                                                return this.compile(((IQLLiteralExpressionDouble) expr), context);
                                              } else {
                                                if ((expr instanceof IQLLiteralExpressionString)) {
                                                  return this.compile(((IQLLiteralExpressionString) expr), context);
                                                } else {
                                                  if ((expr instanceof IQLLiteralExpressionBoolean)) {
                                                    return this.compile(((IQLLiteralExpressionBoolean) expr), context);
                                                  } else {
                                                    if ((expr instanceof IQLLiteralExpressionChar)) {
                                                      return this.compile(((IQLLiteralExpressionChar) expr), context);
                                                    } else {
                                                      if ((expr instanceof IQLLiteralExpressionRange)) {
                                                        return this.compile(((IQLLiteralExpressionRange) expr), context);
                                                      } else {
                                                        if ((expr instanceof IQLLiteralExpressionNull)) {
                                                          return this.compile(((IQLLiteralExpressionNull) expr), context);
                                                        } else {
                                                          if ((expr instanceof IQLLiteralExpressionList)) {
                                                            return this.compile(((IQLLiteralExpressionList) expr), context);
                                                          } else {
                                                            if ((expr instanceof IQLLiteralExpressionMap)) {
                                                              return this.compile(((IQLLiteralExpressionMap) expr), context);
                                                            }
                                                          }
                                                        }
                                                      }
                                                    }
                                                  }
                                                }
                                              }
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return null;
  }
  
  public String compile(final IQLAssignmentExpression e, final G c) {
    IQLExpression _leftOperand = e.getLeftOperand();
    TypeResult left = this.exprParser.getType(_leftOperand);
    IQLExpression _rightOperand = e.getRightOperand();
    TypeResult right = this.exprParser.getType(_rightOperand);
    boolean _isNull = left.isNull();
    boolean _not = (!_isNull);
    if (_not) {
      JvmTypeReference _ref = left.getRef();
      c.setExpectedTypeRef(_ref);
      IQLExpression _rightOperand_1 = e.getRightOperand();
      JvmTypeReference _ref_1 = left.getRef();
      this.exprParser.getType(_rightOperand_1, _ref_1);
    }
    String result = "";
    boolean _or = false;
    boolean _or_1 = false;
    boolean _isNull_1 = left.isNull();
    if (_isNull_1) {
      _or_1 = true;
    } else {
      boolean _isNull_2 = right.isNull();
      _or_1 = _isNull_2;
    }
    if (_or_1) {
      _or = true;
    } else {
      JvmTypeReference _ref_2 = left.getRef();
      JvmTypeReference _ref_3 = right.getRef();
      boolean _isAssignable = this.lookUp.isAssignable(_ref_2, _ref_3);
      _or = _isAssignable;
    }
    if (_or) {
      StringConcatenation _builder = new StringConcatenation();
      IQLExpression _leftOperand_1 = e.getLeftOperand();
      String _compile = this.compile(_leftOperand_1, c);
      _builder.append(_compile, "");
      _builder.append(" ");
      String _op = e.getOp();
      _builder.append(_op, "");
      _builder.append(" ");
      IQLExpression _rightOperand_2 = e.getRightOperand();
      String _compile_1 = this.compile(_rightOperand_2, c);
      _builder.append(_compile_1, "");
      result = _builder.toString();
    } else {
      JvmTypeReference _ref_4 = left.getRef();
      String target = this.typeCompiler.compile(_ref_4, c, false);
      StringConcatenation _builder_1 = new StringConcatenation();
      IQLExpression _leftOperand_2 = e.getLeftOperand();
      String _compile_2 = this.compile(_leftOperand_2, c);
      _builder_1.append(_compile_2, "");
      _builder_1.append(" ");
      String _op_1 = e.getOp();
      _builder_1.append(_op_1, "");
      _builder_1.append(" ((");
      _builder_1.append(target, "");
      _builder_1.append(") ");
      IQLExpression _rightOperand_3 = e.getRightOperand();
      String _compile_3 = this.compile(_rightOperand_3, c);
      _builder_1.append(_compile_3, "");
      _builder_1.append(")");
      result = _builder_1.toString();
    }
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLLogicalOrExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    IQLExpression _leftOperand = e.getLeftOperand();
    String _compile = this.compile(_leftOperand, c);
    _builder.append(_compile, "");
    _builder.append(" ");
    String _op = e.getOp();
    _builder.append(_op, "");
    _builder.append(" ");
    IQLExpression _rightOperand = e.getRightOperand();
    String _compile_1 = this.compile(_rightOperand, c);
    _builder.append(_compile_1, "");
    return _builder.toString();
  }
  
  public String compile(final IQLLogicalAndExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    IQLExpression _leftOperand = e.getLeftOperand();
    String _compile = this.compile(_leftOperand, c);
    _builder.append(_compile, "");
    _builder.append(" ");
    String _op = e.getOp();
    _builder.append(_op, "");
    _builder.append(" ");
    IQLExpression _rightOperand = e.getRightOperand();
    String _compile_1 = this.compile(_rightOperand, c);
    _builder.append(_compile_1, "");
    return _builder.toString();
  }
  
  public String compile(final IQLEqualityExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    IQLExpression _leftOperand = e.getLeftOperand();
    String _compile = this.compile(_leftOperand, c);
    _builder.append(_compile, "");
    _builder.append(" ");
    String _op = e.getOp();
    _builder.append(_op, "");
    _builder.append(" ");
    IQLExpression _rightOperand = e.getRightOperand();
    String _compile_1 = this.compile(_rightOperand, c);
    _builder.append(_compile_1, "");
    return _builder.toString();
  }
  
  public String compile(final IQLRelationalExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    IQLExpression _leftOperand = e.getLeftOperand();
    String _compile = this.compile(_leftOperand, c);
    _builder.append(_compile, "");
    _builder.append(" ");
    String _op = e.getOp();
    _builder.append(_op, "");
    _builder.append(" ");
    IQLExpression _rightOperand = e.getRightOperand();
    String _compile_1 = this.compile(_rightOperand, c);
    _builder.append(_compile_1, "");
    return _builder.toString();
  }
  
  public String compile(final IQLInstanceOfExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    IQLExpression _leftOperand = e.getLeftOperand();
    String _compile = this.compile(_leftOperand, c);
    _builder.append(_compile, "");
    _builder.append(" instanceof ");
    JvmTypeReference _targetRef = e.getTargetRef();
    String _compile_1 = this.typeCompiler.compile(_targetRef, c, true);
    _builder.append(_compile_1, "");
    return _builder.toString();
  }
  
  public String compile(final IQLAdditiveExpression e, final G c) {
    IQLExpression _leftOperand = e.getLeftOperand();
    TypeResult left = this.exprParser.getType(_leftOperand);
    boolean _isNull = left.isNull();
    boolean _not = (!_isNull);
    if (_not) {
      JvmTypeReference _ref = left.getRef();
      c.setExpectedTypeRef(_ref);
    }
    StringConcatenation _builder = new StringConcatenation();
    IQLExpression _leftOperand_1 = e.getLeftOperand();
    String _compile = this.compile(_leftOperand_1, c);
    _builder.append(_compile, "");
    _builder.append(" ");
    String _op = e.getOp();
    _builder.append(_op, "");
    _builder.append(" ");
    IQLExpression _rightOperand = e.getRightOperand();
    String _compile_1 = this.compile(_rightOperand, c);
    _builder.append(_compile_1, "");
    String result = _builder.toString();
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLMultiplicativeExpression e, final G c) {
    IQLExpression _leftOperand = e.getLeftOperand();
    TypeResult left = this.exprParser.getType(_leftOperand);
    boolean _isNull = left.isNull();
    boolean _not = (!_isNull);
    if (_not) {
      JvmTypeReference _ref = left.getRef();
      c.setExpectedTypeRef(_ref);
    }
    StringConcatenation _builder = new StringConcatenation();
    IQLExpression _leftOperand_1 = e.getLeftOperand();
    String _compile = this.compile(_leftOperand_1, c);
    _builder.append(_compile, "");
    _builder.append(" ");
    String _op = e.getOp();
    _builder.append(_op, "");
    _builder.append(" ");
    IQLExpression _rightOperand = e.getRightOperand();
    String _compile_1 = this.compile(_rightOperand, c);
    _builder.append(_compile_1, "");
    String result = _builder.toString();
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLPlusMinusExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    String _op = e.getOp();
    _builder.append(_op, "");
    IQLExpression _operand = e.getOperand();
    String _compile = this.compile(_operand, c);
    _builder.append(_compile, "");
    return _builder.toString();
  }
  
  public String compile(final IQLBooleanNotExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    String _op = e.getOp();
    _builder.append(_op, "");
    IQLExpression _operand = e.getOperand();
    String _compile = this.compile(_operand, c);
    _builder.append(_compile, "");
    return _builder.toString();
  }
  
  public String compile(final IQLPrefixExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    String _op = e.getOp();
    _builder.append(_op, "");
    IQLExpression _operand = e.getOperand();
    String _compile = this.compile(_operand, c);
    _builder.append(_compile, "");
    return _builder.toString();
  }
  
  public String compile(final IQLTypeCastExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    JvmTypeReference _targetRef = e.getTargetRef();
    String _compile = this.typeCompiler.compile(_targetRef, c, false);
    _builder.append(_compile, "");
    _builder.append(")(");
    IQLExpression _operand = e.getOperand();
    String _compile_1 = this.compile(_operand, c);
    _builder.append(_compile_1, "");
    _builder.append(")");
    return _builder.toString();
  }
  
  public String compile(final IQLPostfixExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    IQLExpression _operand = e.getOperand();
    String _compile = this.compile(_operand, c);
    _builder.append(_compile, "");
    String _op = e.getOp();
    _builder.append(_op, "");
    return _builder.toString();
  }
  
  public String compile(final IQLArrayExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    IQLExpression _leftOperand = e.getLeftOperand();
    String _compile = this.compile(_leftOperand, c);
    _builder.append(_compile, "");
    _builder.append("[");
    IQLExpression _expr = e.getExpr();
    String _compile_1 = this.compile(_expr, c);
    _builder.append(_compile_1, "");
    _builder.append("]");
    return _builder.toString();
  }
  
  public String compile(final IQLMemberSelectionExpression e, final G c) {
    String _xblockexpression = null;
    {
      IQLExpression _leftOperand = e.getLeftOperand();
      TypeResult _type = this.exprParser.getType(_leftOperand);
      JvmTypeReference left = _type.getRef();
      String _xifexpression = null;
      IQLMemberSelection _rightOperand = e.getRightOperand();
      if ((_rightOperand instanceof IQLAttributeSelection)) {
        String _xblockexpression_1 = null;
        {
          IQLMemberSelection _rightOperand_1 = e.getRightOperand();
          IQLAttributeSelection a = ((IQLAttributeSelection) _rightOperand_1);
          StringConcatenation _builder = new StringConcatenation();
          String _compile = this.compile(a, left, e, c);
          _builder.append(_compile, "");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        IQLMemberSelection _rightOperand_1 = e.getRightOperand();
        if ((_rightOperand_1 instanceof IQLMethodSelection)) {
          String _xblockexpression_2 = null;
          {
            IQLMemberSelection _rightOperand_2 = e.getRightOperand();
            IQLMethodSelection m = ((IQLMethodSelection) _rightOperand_2);
            StringConcatenation _builder = new StringConcatenation();
            String _compile = this.compile(m, left, e, c);
            _builder.append(_compile, "");
            _xblockexpression_2 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_2;
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLAttributeSelection a, final JvmTypeReference left, final IQLMemberSelectionExpression expr, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    IQLExpression _leftOperand = expr.getLeftOperand();
    String _compile = this.compile(_leftOperand, c);
    _builder.append(_compile, "");
    _builder.append(".");
    JvmField _var = a.getVar();
    String _simpleName = _var.getSimpleName();
    _builder.append(_simpleName, "");
    return _builder.toString();
  }
  
  public String compile(final IQLMethodSelection m, final JvmTypeReference left, final IQLMemberSelectionExpression expr, final G c) {
    IQLExpression _leftOperand = expr.getLeftOperand();
    TypeResult leftType = this.exprParser.getType(_leftOperand);
    boolean _and = false;
    boolean _isNull = leftType.isNull();
    boolean _not = (!_isNull);
    if (!_not) {
      _and = false;
    } else {
      IQLArgumentsList _args = m.getArgs();
      EList<IQLExpression> _elements = _args.getElements();
      int _size = _elements.size();
      boolean _greaterThan = (_size > 0);
      _and = _greaterThan;
    }
    if (_and) {
      JvmTypeReference _ref = leftType.getRef();
      String _method = m.getMethod();
      IQLArgumentsList _args_1 = m.getArgs();
      JvmOperation method = this.lookUp.findMethod(_ref, _method, _args_1);
      boolean _notEquals = (!Objects.equal(method, null));
      if (_notEquals) {
        StringConcatenation _builder = new StringConcatenation();
        IQLExpression _leftOperand_1 = expr.getLeftOperand();
        String _compile = this.compile(_leftOperand_1, c);
        _builder.append(_compile, "");
        _builder.append(".");
        String _method_1 = m.getMethod();
        _builder.append(_method_1, "");
        _builder.append("(");
        IQLArgumentsList _args_2 = m.getArgs();
        EList<JvmFormalParameter> _parameters = method.getParameters();
        String _compile_1 = this.compile(_args_2, _parameters, c);
        _builder.append(_compile_1, "");
        _builder.append(")");
        return _builder.toString();
      }
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    IQLExpression _leftOperand_2 = expr.getLeftOperand();
    String _compile_2 = this.compile(_leftOperand_2, c);
    _builder_1.append(_compile_2, "");
    _builder_1.append(".");
    String _method_2 = m.getMethod();
    _builder_1.append(_method_2, "");
    _builder_1.append("(");
    IQLArgumentsList _args_3 = m.getArgs();
    String _compile_3 = this.compile(_args_3, c);
    _builder_1.append(_compile_3, "");
    _builder_1.append(")");
    return _builder_1.toString();
  }
  
  public String compile(final IQLArgumentsList args, final EList<JvmFormalParameter> parameters, final G c) {
    String result = "";
    for (int i = 0; (i < args.getElements().size()); i++) {
      {
        if ((i > 0)) {
          result = (result + ",");
        }
        JvmFormalParameter _get = parameters.get(i);
        JvmTypeReference _parameterType = _get.getParameterType();
        c.setExpectedTypeRef(_parameterType);
        EList<IQLExpression> _elements = args.getElements();
        IQLExpression _get_1 = _elements.get(i);
        String _compile = this.compile(_get_1, c);
        String _plus = (result + _compile);
        result = _plus;
        c.setExpectedTypeRef(null);
      }
    }
    return result;
  }
  
  public String compile(final IQLArgumentsList list, final G context) {
    StringConcatenation _builder = new StringConcatenation();
    EList<IQLExpression> _elements = list.getElements();
    final Function1<IQLExpression, String> _function = new Function1<IQLExpression, String>() {
      public String apply(final IQLExpression e) {
        return AbstractIQLExpressionCompiler.this.compile(e, context);
      }
    };
    List<String> _map = ListExtensions.<IQLExpression, String>map(_elements, _function);
    String _join = IterableExtensions.join(_map, ", ");
    _builder.append(_join, "");
    return _builder.toString();
  }
  
  public String compile(final IQLArgumentsMap map, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    EList<IQLArgumentsMapKeyValue> _elements = map.getElements();
    final Function1<IQLArgumentsMapKeyValue, String> _function = new Function1<IQLArgumentsMapKeyValue, String>() {
      public String apply(final IQLArgumentsMapKeyValue el) {
        IQLExpression _value = el.getValue();
        return AbstractIQLExpressionCompiler.this.compile(_value, c);
      }
    };
    List<String> _map = ListExtensions.<IQLArgumentsMapKeyValue, String>map(_elements, _function);
    String _join = IterableExtensions.join(_map, ", ");
    _builder.append(_join, "");
    return _builder.toString();
  }
  
  public String compile(final IQLArgumentsMap map, final JvmTypeReference typeRef, final G c) {
    String result = "";
    for (int i = 0; (i < map.getElements().size()); i++) {
      {
        if ((i > 0)) {
          result = (result + ", ");
        }
        EList<IQLArgumentsMapKeyValue> _elements = map.getElements();
        IQLArgumentsMapKeyValue el = _elements.get(i);
        String _key = el.getKey();
        JvmTypeReference type = this.helper.getPropertyType(_key, typeRef);
        boolean _notEquals = (!Objects.equal(type, null));
        if (_notEquals) {
          c.setExpectedTypeRef(type);
        }
        IQLExpression _value = el.getValue();
        String _compile = this.compile(_value, c);
        String _plus = (result + _compile);
        result = _plus;
        c.setExpectedTypeRef(null);
      }
    }
    return result;
  }
  
  public String compile(final IQLTerminalExpressionVariable e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    IQLVariableDeclaration _var = e.getVar();
    String _name = _var.getName();
    _builder.append(_name, "");
    return _builder.toString();
  }
  
  public String compile(final IQLTerminalExpressionThis e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("this");
    return _builder.toString();
  }
  
  public String compile(final IQLTerminalExpressionSuper e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("super");
    return _builder.toString();
  }
  
  public String compile(final IQLTerminalExpressionParenthesis e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    IQLExpression _expr = e.getExpr();
    String _compile = this.compile(_expr, c);
    _builder.append(_compile, "");
    _builder.append(")");
    return _builder.toString();
  }
  
  public String compile(final IQLTerminalExpressionNew e, final G c) {
    String _xifexpression = null;
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
        JvmTypeReference _ref = e.getRef();
        IQLArgumentsList _argsList = e.getArgsList();
        JvmExecutable constructor = this.lookUp.findConstructor(_ref, _argsList);
        String _xifexpression_1 = null;
        boolean _notEquals_1 = (!Objects.equal(constructor, null));
        if (_notEquals_1) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("get");
          JvmTypeReference _ref_1 = e.getRef();
          String _shortName = this.factory.getShortName(_ref_1, false);
          _builder.append(_shortName, "");
          JvmTypeReference _ref_2 = e.getRef();
          int _hashCode = _ref_2.hashCode();
          _builder.append(_hashCode, "");
          _builder.append("(new ");
          JvmTypeReference _ref_3 = e.getRef();
          String _compile = this.typeCompiler.compile(_ref_3, c, true);
          _builder.append(_compile, "");
          _builder.append("(");
          IQLArgumentsList _argsList_1 = e.getArgsList();
          EList<JvmFormalParameter> _parameters = constructor.getParameters();
          String _compile_1 = this.compile(_argsList_1, _parameters, c);
          _builder.append(_compile_1, "");
          _builder.append("), ");
          IQLArgumentsMap _argsMap_2 = e.getArgsMap();
          JvmTypeReference _ref_4 = e.getRef();
          String _compile_2 = this.compile(_argsMap_2, _ref_4, c);
          _builder.append(_compile_2, "");
          _builder.append(")");
          _xifexpression_1 = _builder.toString();
        } else {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("get");
          JvmTypeReference _ref_5 = e.getRef();
          String _shortName_1 = this.factory.getShortName(_ref_5, false);
          _builder_1.append(_shortName_1, "");
          JvmTypeReference _ref_6 = e.getRef();
          int _hashCode_1 = _ref_6.hashCode();
          _builder_1.append(_hashCode_1, "");
          _builder_1.append("(new ");
          JvmTypeReference _ref_7 = e.getRef();
          String _compile_3 = this.typeCompiler.compile(_ref_7, c, true);
          _builder_1.append(_compile_3, "");
          _builder_1.append("(");
          IQLArgumentsList _argsList_2 = e.getArgsList();
          String _compile_4 = this.compile(_argsList_2, c);
          _builder_1.append(_compile_4, "");
          _builder_1.append("), ");
          IQLArgumentsMap _argsMap_3 = e.getArgsMap();
          JvmTypeReference _ref_8 = e.getRef();
          String _compile_5 = this.compile(_argsMap_3, _ref_8, c);
          _builder_1.append(_compile_5, "");
          _builder_1.append(")");
          _xifexpression_1 = _builder_1.toString();
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    } else {
      String _xifexpression_1 = null;
      IQLArgumentsList _argsList = e.getArgsList();
      boolean _notEquals_1 = (!Objects.equal(_argsList, null));
      if (_notEquals_1) {
        String _xblockexpression_1 = null;
        {
          JvmTypeReference _ref = e.getRef();
          IQLArgumentsList _argsList_1 = e.getArgsList();
          JvmExecutable constructor = this.lookUp.findConstructor(_ref, _argsList_1);
          String _xifexpression_2 = null;
          boolean _notEquals_2 = (!Objects.equal(constructor, null));
          if (_notEquals_2) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("new ");
            JvmTypeReference _ref_1 = e.getRef();
            String _compile = this.typeCompiler.compile(_ref_1, c, true);
            _builder.append(_compile, "");
            _builder.append("(");
            IQLArgumentsList _argsList_2 = e.getArgsList();
            EList<JvmFormalParameter> _parameters = constructor.getParameters();
            String _compile_1 = this.compile(_argsList_2, _parameters, c);
            _builder.append(_compile_1, "");
            _builder.append(")");
            _xifexpression_2 = _builder.toString();
          } else {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append("new ");
            JvmTypeReference _ref_2 = e.getRef();
            String _compile_2 = this.typeCompiler.compile(_ref_2, c, true);
            _builder_1.append(_compile_2, "");
            _builder_1.append("(");
            IQLArgumentsList _argsList_3 = e.getArgsList();
            String _compile_3 = this.compile(_argsList_3, c);
            _builder_1.append(_compile_3, "");
            _builder_1.append(")");
            _xifexpression_2 = _builder_1.toString();
          }
          _xblockexpression_1 = _xifexpression_2;
        }
        _xifexpression_1 = _xblockexpression_1;
      } else {
        String _xifexpression_2 = null;
        JvmTypeReference _ref = e.getRef();
        if ((_ref instanceof IQLArrayTypeRef)) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("new ");
          JvmTypeReference _ref_1 = e.getRef();
          String _compile = this.typeCompiler.compile(_ref_1, c, false);
          _builder.append(_compile, "");
          _xifexpression_2 = _builder.toString();
        } else {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("new ");
          JvmTypeReference _ref_2 = e.getRef();
          String _compile_1 = this.typeCompiler.compile(_ref_2, c, true);
          _builder_1.append(_compile_1, "");
          _builder_1.append("()");
          _xifexpression_2 = _builder_1.toString();
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLLiteralExpressionDouble e, final G c) {
    String _xifexpression = null;
    JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
    boolean _notEquals = (!Objects.equal(_expectedTypeRef, null));
    if (_notEquals) {
      String _xifexpression_1 = null;
      JvmTypeReference _expectedTypeRef_1 = c.getExpectedTypeRef();
      boolean _isFloat = this.factory.isFloat(_expectedTypeRef_1);
      if (_isFloat) {
        StringConcatenation _builder = new StringConcatenation();
        double _value = e.getValue();
        _builder.append(_value, "");
        _builder.append("F");
        _xifexpression_1 = _builder.toString();
      } else {
        String _xifexpression_2 = null;
        JvmTypeReference _expectedTypeRef_2 = c.getExpectedTypeRef();
        boolean _isDouble = this.factory.isDouble(_expectedTypeRef_2, true);
        if (_isDouble) {
          StringConcatenation _builder_1 = new StringConcatenation();
          double _value_1 = e.getValue();
          _builder_1.append(_value_1, "");
          _builder_1.append("D");
          _xifexpression_2 = _builder_1.toString();
        } else {
          StringConcatenation _builder_2 = new StringConcatenation();
          double _value_2 = e.getValue();
          _builder_2.append(_value_2, "");
          _xifexpression_2 = _builder_2.toString();
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    } else {
      StringConcatenation _builder_3 = new StringConcatenation();
      double _value_3 = e.getValue();
      _builder_3.append(_value_3, "");
      _xifexpression = _builder_3.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLLiteralExpressionInt e, final G c) {
    String _xifexpression = null;
    JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
    boolean _notEquals = (!Objects.equal(_expectedTypeRef, null));
    if (_notEquals) {
      String _xifexpression_1 = null;
      JvmTypeReference _expectedTypeRef_1 = c.getExpectedTypeRef();
      boolean _isFloat = this.factory.isFloat(_expectedTypeRef_1);
      if (_isFloat) {
        StringConcatenation _builder = new StringConcatenation();
        int _value = e.getValue();
        _builder.append(_value, "");
        _builder.append("F");
        _xifexpression_1 = _builder.toString();
      } else {
        String _xifexpression_2 = null;
        JvmTypeReference _expectedTypeRef_2 = c.getExpectedTypeRef();
        boolean _isDouble = this.factory.isDouble(_expectedTypeRef_2, true);
        if (_isDouble) {
          StringConcatenation _builder_1 = new StringConcatenation();
          int _value_1 = e.getValue();
          _builder_1.append(_value_1, "");
          _builder_1.append("D");
          _xifexpression_2 = _builder_1.toString();
        } else {
          String _xifexpression_3 = null;
          JvmTypeReference _expectedTypeRef_3 = c.getExpectedTypeRef();
          boolean _isLong = this.factory.isLong(_expectedTypeRef_3, true);
          if (_isLong) {
            StringConcatenation _builder_2 = new StringConcatenation();
            int _value_2 = e.getValue();
            _builder_2.append(_value_2, "");
            _builder_2.append("L");
            _xifexpression_3 = _builder_2.toString();
          } else {
            StringConcatenation _builder_3 = new StringConcatenation();
            int _value_3 = e.getValue();
            _builder_3.append(_value_3, "");
            _xifexpression_3 = _builder_3.toString();
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    } else {
      StringConcatenation _builder_4 = new StringConcatenation();
      int _value_4 = e.getValue();
      _builder_4.append(_value_4, "");
      _xifexpression = _builder_4.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLLiteralExpressionString e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    String _value = e.getValue();
    _builder.append(_value, "");
    _builder.append("\"");
    return _builder.toString();
  }
  
  public String compile(final IQLLiteralExpressionBoolean e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    boolean _isValue = e.isValue();
    _builder.append(_isValue, "");
    return _builder.toString();
  }
  
  public String compile(final IQLLiteralExpressionChar e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\'");
    char _value = e.getValue();
    _builder.append(_value, "");
    _builder.append("\' ");
    return _builder.toString();
  }
  
  public String compile(final IQLLiteralExpressionRange e, final G c) {
    String _xblockexpression = null;
    {
      String _canonicalName = Range.class.getCanonicalName();
      c.addImport(_canonicalName);
      String _value = e.getValue();
      String _value_1 = e.getValue();
      int _indexOf = _value_1.indexOf(".");
      String _substring = _value.substring(0, _indexOf);
      int from = Integer.parseInt(_substring);
      String _value_2 = e.getValue();
      String _value_3 = e.getValue();
      int _lastIndexOf = _value_3.lastIndexOf(".");
      int _plus = (_lastIndexOf + 1);
      String _value_4 = e.getValue();
      int _length = _value_4.length();
      String _substring_1 = _value_2.substring(_plus, _length);
      int to = Integer.parseInt(_substring_1);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("new ");
      String _simpleName = Range.class.getSimpleName();
      _builder.append(_simpleName, "");
      _builder.append("\'(");
      _builder.append(from, "");
      _builder.append(" , ");
      _builder.append(to, "");
      _builder.append(")");
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLLiteralExpressionNull e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("null");
    return _builder.toString();
  }
  
  public String compile(final IQLLiteralExpressionList e, final G c) {
    String _xblockexpression = null;
    {
      String _canonicalName = IQLUtils.class.getCanonicalName();
      c.addImport(_canonicalName);
      StringConcatenation _builder = new StringConcatenation();
      String _simpleName = IQLUtils.class.getSimpleName();
      _builder.append(_simpleName, "");
      _builder.append(".createList(");
      EList<IQLExpression> _elements = e.getElements();
      final Function1<IQLExpression, String> _function = new Function1<IQLExpression, String>() {
        public String apply(final IQLExpression el) {
          return AbstractIQLExpressionCompiler.this.compile(el, c);
        }
      };
      List<String> _map = ListExtensions.<IQLExpression, String>map(_elements, _function);
      String _join = IterableExtensions.join(_map, ", ");
      _builder.append(_join, "");
      _builder.append(")");
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLLiteralExpressionMap e, final G c) {
    String _xblockexpression = null;
    {
      String _canonicalName = IQLUtils.class.getCanonicalName();
      c.addImport(_canonicalName);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("\t");
      String _simpleName = IQLUtils.class.getSimpleName();
      _builder.append(_simpleName, "\t");
      _builder.append(".createMap(");
      EList<IQLLiteralExpressionMapKeyValue> _elements = e.getElements();
      final Function1<IQLLiteralExpressionMapKeyValue, String> _function = new Function1<IQLLiteralExpressionMapKeyValue, String>() {
        public String apply(final IQLLiteralExpressionMapKeyValue el) {
          IQLExpression _key = el.getKey();
          String _compile = AbstractIQLExpressionCompiler.this.compile(_key, c);
          String _plus = (_compile + ", ");
          IQLExpression _value = el.getValue();
          String _compile_1 = AbstractIQLExpressionCompiler.this.compile(_value, c);
          return (_plus + _compile_1);
        }
      };
      List<String> _map = ListExtensions.<IQLLiteralExpressionMapKeyValue, String>map(_elements, _function);
      String _join = IterableExtensions.join(_map, ", ");
      _builder.append(_join, "\t");
      _builder.append(")");
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
}
