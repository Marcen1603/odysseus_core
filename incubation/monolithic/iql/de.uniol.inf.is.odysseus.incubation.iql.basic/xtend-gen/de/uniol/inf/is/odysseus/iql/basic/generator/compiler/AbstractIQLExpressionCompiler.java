package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean;
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
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLParenthesisExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSuperExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLThisExpression;
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
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public abstract class AbstractIQLExpressionCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, E extends IIQLExpressionParser, U extends IIQLTypeUtils, L extends IIQLLookUp, O extends IIQLTypeExtensionsFactory> implements IIQLExpressionCompiler<G> {
  protected H helper;
  
  protected T typeCompiler;
  
  protected E exprParser;
  
  protected U typeUtils;
  
  protected L lookUp;
  
  protected O typeExtensionsFactory;
  
  public AbstractIQLExpressionCompiler(final H helper, final T typeCompiler, final E exprParser, final U typeUtils, final L lookUp, final O typeExtensionsFactory) {
    this.helper = helper;
    this.typeCompiler = typeCompiler;
    this.exprParser = exprParser;
    this.typeUtils = typeUtils;
    this.lookUp = lookUp;
    this.typeExtensionsFactory = typeExtensionsFactory;
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
                                  if ((expr instanceof IQLJvmElementCallExpression)) {
                                    return this.compile(((IQLJvmElementCallExpression) expr), context);
                                  } else {
                                    if ((expr instanceof IQLThisExpression)) {
                                      return this.compile(((IQLThisExpression) expr), context);
                                    } else {
                                      if ((expr instanceof IQLSuperExpression)) {
                                        return this.compile(((IQLSuperExpression) expr), context);
                                      } else {
                                        if ((expr instanceof IQLParenthesisExpression)) {
                                          return this.compile(((IQLParenthesisExpression) expr), context);
                                        } else {
                                          if ((expr instanceof IQLNewExpression)) {
                                            return this.compile(((IQLNewExpression) expr), context);
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
    return null;
  }
  
  public String compile(final IQLAssignmentExpression e, final G c) {
    String _xifexpression = null;
    IQLExpression _leftOperand = e.getLeftOperand();
    if ((_leftOperand instanceof IQLMemberSelectionExpression)) {
      IQLExpression _leftOperand_1 = e.getLeftOperand();
      _xifexpression = this.compileAssignmentExpr(e, ((IQLMemberSelectionExpression) _leftOperand_1), c);
    } else {
      String _xifexpression_1 = null;
      IQLExpression _leftOperand_2 = e.getLeftOperand();
      if ((_leftOperand_2 instanceof IQLJvmElementCallExpression)) {
        IQLExpression _leftOperand_3 = e.getLeftOperand();
        _xifexpression_1 = this.compileAssignmentExpr(e, ((IQLJvmElementCallExpression) _leftOperand_3), c);
      } else {
        String _xifexpression_2 = null;
        IQLExpression _leftOperand_4 = e.getLeftOperand();
        if ((_leftOperand_4 instanceof IQLArrayExpression)) {
          IQLExpression _leftOperand_5 = e.getLeftOperand();
          _xifexpression_2 = this.compileAssignmentExpr(e, ((IQLArrayExpression) _leftOperand_5), c);
        } else {
          _xifexpression_2 = this.compileAssignmentExpr(e, c);
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String compileAssignmentExpr(final IQLAssignmentExpression e, final IQLJvmElementCallExpression elementCallExpr, final G c) {
    String _xifexpression = null;
    boolean _and = false;
    String _op = e.getOp();
    boolean _equals = _op.equals("=");
    if (!_equals) {
      _and = false;
    } else {
      JvmIdentifiableElement _element = elementCallExpr.getElement();
      _and = (_element instanceof JvmOperation);
    }
    if (_and) {
      JvmIdentifiableElement _element_1 = elementCallExpr.getElement();
      EList<JvmFormalParameter> _parameters = ((JvmOperation) _element_1).getParameters();
      JvmFormalParameter _get = _parameters.get(0);
      JvmTypeReference leftType = _get.getParameterType();
      IQLExpression _rightOperand = e.getRightOperand();
      TypeResult rightType = this.exprParser.getType(_rightOperand);
      c.setExpectedTypeRef(leftType);
      String result = "";
      JvmIdentifiableElement _element_2 = elementCallExpr.getElement();
      JvmOperation op = ((JvmOperation) _element_2);
      boolean _or = false;
      boolean _isNull = rightType.isNull();
      if (_isNull) {
        _or = true;
      } else {
        JvmTypeReference _ref = rightType.getRef();
        boolean _isAssignable = this.lookUp.isAssignable(leftType, _ref);
        _or = _isAssignable;
      }
      if (_or) {
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = op.getSimpleName();
        _builder.append(_simpleName, "");
        _builder.append("(");
        IQLExpression _rightOperand_1 = e.getRightOperand();
        String _compile = this.compile(_rightOperand_1, c);
        _builder.append(_compile, "");
        _builder.append(")");
        result = _builder.toString();
      } else {
        boolean _or_1 = false;
        boolean _isNull_1 = rightType.isNull();
        if (_isNull_1) {
          _or_1 = true;
        } else {
          JvmTypeReference _ref_1 = rightType.getRef();
          boolean _isCastable = this.lookUp.isCastable(leftType, _ref_1);
          _or_1 = _isCastable;
        }
        if (_or_1) {
          String target = this.typeCompiler.compile(leftType, c, false);
          StringConcatenation _builder_1 = new StringConcatenation();
          String _simpleName_1 = op.getSimpleName();
          _builder_1.append(_simpleName_1, "");
          _builder_1.append("((");
          _builder_1.append(target, "");
          _builder_1.append(")");
          IQLExpression _rightOperand_2 = e.getRightOperand();
          String _compile_1 = this.compile(_rightOperand_2, c);
          _builder_1.append(_compile_1, "");
          _builder_1.append(")");
          result = _builder_1.toString();
        } else {
          StringConcatenation _builder_2 = new StringConcatenation();
          String _simpleName_2 = op.getSimpleName();
          _builder_2.append(_simpleName_2, "");
          _builder_2.append("(");
          IQLExpression _rightOperand_3 = e.getRightOperand();
          String _compile_2 = this.compile(_rightOperand_3, c);
          _builder_2.append(_compile_2, "");
          _builder_2.append(")");
          result = _builder_2.toString();
        }
      }
      c.setExpectedTypeRef(null);
      return result;
    } else {
      _xifexpression = this.compileAssignmentExpr(e, c);
    }
    return _xifexpression;
  }
  
  public String compileAssignmentExpr(final IQLAssignmentExpression e, final IQLMemberSelectionExpression selExpr, final G c) {
    String _xifexpression = null;
    boolean _and = false;
    String _op = e.getOp();
    boolean _equals = _op.equals("=");
    if (!_equals) {
      _and = false;
    } else {
      IQLMemberSelection _sel = selExpr.getSel();
      JvmMember _member = _sel.getMember();
      _and = (_member instanceof JvmOperation);
    }
    if (_and) {
      IQLMemberSelection _sel_1 = selExpr.getSel();
      JvmMember _member_1 = _sel_1.getMember();
      EList<JvmFormalParameter> _parameters = ((JvmOperation) _member_1).getParameters();
      JvmFormalParameter _get = _parameters.get(0);
      JvmTypeReference leftType = _get.getParameterType();
      IQLExpression _rightOperand = e.getRightOperand();
      TypeResult rightType = this.exprParser.getType(_rightOperand);
      c.setExpectedTypeRef(leftType);
      String result = "";
      IQLMemberSelection _sel_2 = selExpr.getSel();
      JvmMember _member_2 = _sel_2.getMember();
      JvmOperation op = ((JvmOperation) _member_2);
      boolean _or = false;
      boolean _isNull = rightType.isNull();
      if (_isNull) {
        _or = true;
      } else {
        JvmTypeReference _ref = rightType.getRef();
        boolean _isAssignable = this.lookUp.isAssignable(leftType, _ref);
        _or = _isAssignable;
      }
      if (_or) {
        StringConcatenation _builder = new StringConcatenation();
        IQLExpression _leftOperand = selExpr.getLeftOperand();
        String _compile = this.compile(_leftOperand, c);
        _builder.append(_compile, "");
        _builder.append(".");
        String _simpleName = op.getSimpleName();
        _builder.append(_simpleName, "");
        _builder.append("(");
        IQLExpression _rightOperand_1 = e.getRightOperand();
        String _compile_1 = this.compile(_rightOperand_1, c);
        _builder.append(_compile_1, "");
        _builder.append(")");
        result = _builder.toString();
      } else {
        boolean _or_1 = false;
        boolean _isNull_1 = rightType.isNull();
        if (_isNull_1) {
          _or_1 = true;
        } else {
          JvmTypeReference _ref_1 = rightType.getRef();
          boolean _isCastable = this.lookUp.isCastable(leftType, _ref_1);
          _or_1 = _isCastable;
        }
        if (_or_1) {
          String target = this.typeCompiler.compile(leftType, c, false);
          StringConcatenation _builder_1 = new StringConcatenation();
          IQLExpression _leftOperand_1 = selExpr.getLeftOperand();
          String _compile_2 = this.compile(_leftOperand_1, c);
          _builder_1.append(_compile_2, "");
          _builder_1.append(".");
          String _simpleName_1 = op.getSimpleName();
          _builder_1.append(_simpleName_1, "");
          _builder_1.append("((");
          _builder_1.append(target, "");
          _builder_1.append(")");
          IQLExpression _rightOperand_2 = e.getRightOperand();
          String _compile_3 = this.compile(_rightOperand_2, c);
          _builder_1.append(_compile_3, "");
          _builder_1.append(")");
          result = _builder_1.toString();
        } else {
          StringConcatenation _builder_2 = new StringConcatenation();
          IQLExpression _leftOperand_2 = selExpr.getLeftOperand();
          String _compile_4 = this.compile(_leftOperand_2, c);
          _builder_2.append(_compile_4, "");
          _builder_2.append(".");
          String _simpleName_2 = op.getSimpleName();
          _builder_2.append(_simpleName_2, "");
          _builder_2.append("(");
          IQLExpression _rightOperand_3 = e.getRightOperand();
          String _compile_5 = this.compile(_rightOperand_3, c);
          _builder_2.append(_compile_5, "");
          _builder_2.append(")");
          result = _builder_2.toString();
        }
      }
      c.setExpectedTypeRef(null);
      return result;
    } else {
      _xifexpression = this.compileAssignmentExpr(e, c);
    }
    return _xifexpression;
  }
  
  public String compileAssignmentExpr(final IQLAssignmentExpression e, final IQLArrayExpression arrayExpr, final G c) {
    String _xblockexpression = null;
    {
      IQLExpression _leftOperand = arrayExpr.getLeftOperand();
      TypeResult arrayType = this.exprParser.getType(_leftOperand);
      String _xifexpression = null;
      boolean _and = false;
      boolean _and_1 = false;
      String _op = e.getOp();
      boolean _equals = _op.equals("=");
      if (!_equals) {
        _and_1 = false;
      } else {
        boolean _isNull = arrayType.isNull();
        boolean _not = (!_isNull);
        _and_1 = _not;
      }
      if (!_and_1) {
        _and = false;
      } else {
        JvmTypeReference _ref = arrayType.getRef();
        boolean _hasTypeExtensions = this.typeExtensionsFactory.hasTypeExtensions(_ref, "set", 3);
        _and = _hasTypeExtensions;
      }
      if (_and) {
        TypeResult leftType = this.exprParser.getType(arrayExpr);
        IQLExpression _rightOperand = e.getRightOperand();
        TypeResult rightType = this.exprParser.getType(_rightOperand);
        boolean _isNull_1 = leftType.isNull();
        boolean _not_1 = (!_isNull_1);
        if (_not_1) {
          JvmTypeReference _ref_1 = leftType.getRef();
          c.setExpectedTypeRef(_ref_1);
        }
        String result = "";
        JvmTypeReference _ref_2 = arrayType.getRef();
        IIQLTypeExtensions typeOps = this.typeExtensionsFactory.getTypeExtensions(_ref_2, "set", 3);
        Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
        String _canonicalName = _class.getCanonicalName();
        c.addImport(_canonicalName);
        boolean _or = false;
        boolean _or_1 = false;
        boolean _isNull_2 = leftType.isNull();
        if (_isNull_2) {
          _or_1 = true;
        } else {
          boolean _isNull_3 = rightType.isNull();
          _or_1 = _isNull_3;
        }
        if (_or_1) {
          _or = true;
        } else {
          JvmTypeReference _ref_3 = leftType.getRef();
          JvmTypeReference _ref_4 = rightType.getRef();
          boolean _isAssignable = this.lookUp.isAssignable(_ref_3, _ref_4);
          _or = _isAssignable;
        }
        if (_or) {
          StringConcatenation _builder = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
          String _simpleName = _class_1.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append(".set(");
          IQLExpression _leftOperand_1 = arrayExpr.getLeftOperand();
          String _compile = this.compile(_leftOperand_1, c);
          _builder.append(_compile, "");
          _builder.append(", ");
          IQLExpression _rightOperand_1 = e.getRightOperand();
          String _compile_1 = this.compile(_rightOperand_1, c);
          _builder.append(_compile_1, "");
          _builder.append(", ");
          EList<IQLExpression> _expressions = arrayExpr.getExpressions();
          final Function1<IQLExpression, String> _function = new Function1<IQLExpression, String>() {
            public String apply(final IQLExpression el) {
              return AbstractIQLExpressionCompiler.this.compile(el, c);
            }
          };
          List<String> _map = ListExtensions.<IQLExpression, String>map(_expressions, _function);
          String _join = IterableExtensions.join(_map, ", ");
          _builder.append(_join, "");
          _builder.append(")");
          result = _builder.toString();
        } else {
          boolean _or_2 = false;
          boolean _or_3 = false;
          boolean _isNull_4 = leftType.isNull();
          if (_isNull_4) {
            _or_3 = true;
          } else {
            boolean _isNull_5 = rightType.isNull();
            _or_3 = _isNull_5;
          }
          if (_or_3) {
            _or_2 = true;
          } else {
            JvmTypeReference _ref_5 = leftType.getRef();
            JvmTypeReference _ref_6 = rightType.getRef();
            boolean _isCastable = this.lookUp.isCastable(_ref_5, _ref_6);
            _or_2 = _isCastable;
          }
          if (_or_2) {
            JvmTypeReference _ref_7 = leftType.getRef();
            String target = this.typeCompiler.compile(_ref_7, c, false);
            StringConcatenation _builder_1 = new StringConcatenation();
            Class<? extends IIQLTypeExtensions> _class_2 = typeOps.getClass();
            String _simpleName_1 = _class_2.getSimpleName();
            _builder_1.append(_simpleName_1, "");
            _builder_1.append(".set(");
            IQLExpression _leftOperand_2 = arrayExpr.getLeftOperand();
            String _compile_2 = this.compile(_leftOperand_2, c);
            _builder_1.append(_compile_2, "");
            _builder_1.append(", ((");
            _builder_1.append(target, "");
            _builder_1.append(")");
            IQLExpression _rightOperand_2 = e.getRightOperand();
            String _compile_3 = this.compile(_rightOperand_2, c);
            _builder_1.append(_compile_3, "");
            _builder_1.append("), ");
            EList<IQLExpression> _expressions_1 = arrayExpr.getExpressions();
            final Function1<IQLExpression, String> _function_1 = new Function1<IQLExpression, String>() {
              public String apply(final IQLExpression el) {
                return AbstractIQLExpressionCompiler.this.compile(el, c);
              }
            };
            List<String> _map_1 = ListExtensions.<IQLExpression, String>map(_expressions_1, _function_1);
            String _join_1 = IterableExtensions.join(_map_1, ", ");
            _builder_1.append(_join_1, "");
            _builder_1.append(")");
            result = _builder_1.toString();
          } else {
            StringConcatenation _builder_2 = new StringConcatenation();
            Class<? extends IIQLTypeExtensions> _class_3 = typeOps.getClass();
            String _simpleName_2 = _class_3.getSimpleName();
            _builder_2.append(_simpleName_2, "");
            _builder_2.append(".set(");
            IQLExpression _leftOperand_3 = arrayExpr.getLeftOperand();
            String _compile_4 = this.compile(_leftOperand_3, c);
            _builder_2.append(_compile_4, "");
            _builder_2.append(", ");
            IQLExpression _rightOperand_3 = e.getRightOperand();
            String _compile_5 = this.compile(_rightOperand_3, c);
            _builder_2.append(_compile_5, "");
            _builder_2.append(", ");
            EList<IQLExpression> _expressions_2 = arrayExpr.getExpressions();
            final Function1<IQLExpression, String> _function_2 = new Function1<IQLExpression, String>() {
              public String apply(final IQLExpression el) {
                return AbstractIQLExpressionCompiler.this.compile(el, c);
              }
            };
            List<String> _map_2 = ListExtensions.<IQLExpression, String>map(_expressions_2, _function_2);
            String _join_2 = IterableExtensions.join(_map_2, ", ");
            _builder_2.append(_join_2, "");
            _builder_2.append(")");
            result = _builder_2.toString();
          }
        }
        c.setExpectedTypeRef(null);
        return result;
      } else {
        String _xifexpression_1 = null;
        boolean _and_2 = false;
        boolean _and_3 = false;
        String _op_1 = e.getOp();
        boolean _equals_1 = _op_1.equals("=");
        if (!_equals_1) {
          _and_3 = false;
        } else {
          boolean _isNull_6 = arrayType.isNull();
          boolean _not_2 = (!_isNull_6);
          _and_3 = _not_2;
        }
        if (!_and_3) {
          _and_2 = false;
        } else {
          JvmTypeReference _ref_8 = arrayType.getRef();
          boolean _isArray = this.typeUtils.isArray(_ref_8);
          _and_2 = _isArray;
        }
        if (_and_2) {
          TypeResult leftType_1 = this.exprParser.getType(arrayExpr);
          IQLExpression _rightOperand_4 = e.getRightOperand();
          TypeResult rightType_1 = this.exprParser.getType(_rightOperand_4);
          boolean _isNull_7 = leftType_1.isNull();
          boolean _not_3 = (!_isNull_7);
          if (_not_3) {
            JvmTypeReference _ref_9 = leftType_1.getRef();
            c.setExpectedTypeRef(_ref_9);
          }
          String result_1 = "";
          boolean _or_4 = false;
          boolean _or_5 = false;
          boolean _isNull_8 = leftType_1.isNull();
          if (_isNull_8) {
            _or_5 = true;
          } else {
            boolean _isNull_9 = rightType_1.isNull();
            _or_5 = _isNull_9;
          }
          if (_or_5) {
            _or_4 = true;
          } else {
            JvmTypeReference _ref_10 = leftType_1.getRef();
            JvmTypeReference _ref_11 = rightType_1.getRef();
            boolean _isAssignable_1 = this.lookUp.isAssignable(_ref_10, _ref_11);
            _or_4 = _isAssignable_1;
          }
          if (_or_4) {
            String _canonicalName_1 = de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl.ListExtensions.class.getCanonicalName();
            c.addImport(_canonicalName_1);
            StringConcatenation _builder_3 = new StringConcatenation();
            String _simpleName_3 = de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl.ListExtensions.class.getSimpleName();
            _builder_3.append(_simpleName_3, "");
            _builder_3.append(".set(");
            IQLExpression _leftOperand_4 = arrayExpr.getLeftOperand();
            String _compile_6 = this.compile(_leftOperand_4, c);
            _builder_3.append(_compile_6, "");
            _builder_3.append(", ");
            IQLExpression _rightOperand_5 = e.getRightOperand();
            String _compile_7 = this.compile(_rightOperand_5, c);
            _builder_3.append(_compile_7, "");
            _builder_3.append(", ");
            EList<IQLExpression> _expressions_3 = arrayExpr.getExpressions();
            final Function1<IQLExpression, String> _function_3 = new Function1<IQLExpression, String>() {
              public String apply(final IQLExpression el) {
                return AbstractIQLExpressionCompiler.this.compile(el, c);
              }
            };
            List<String> _map_3 = ListExtensions.<IQLExpression, String>map(_expressions_3, _function_3);
            String _join_3 = IterableExtensions.join(_map_3, ", ");
            _builder_3.append(_join_3, "");
            _builder_3.append(")");
            result_1 = _builder_3.toString();
          } else {
            boolean _or_6 = false;
            boolean _or_7 = false;
            boolean _isNull_10 = leftType_1.isNull();
            if (_isNull_10) {
              _or_7 = true;
            } else {
              boolean _isNull_11 = rightType_1.isNull();
              _or_7 = _isNull_11;
            }
            if (_or_7) {
              _or_6 = true;
            } else {
              JvmTypeReference _ref_12 = leftType_1.getRef();
              JvmTypeReference _ref_13 = rightType_1.getRef();
              boolean _isCastable_1 = this.lookUp.isCastable(_ref_12, _ref_13);
              _or_6 = _isCastable_1;
            }
            if (_or_6) {
              String _canonicalName_2 = de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl.ListExtensions.class.getCanonicalName();
              c.addImport(_canonicalName_2);
              JvmTypeReference _ref_14 = leftType_1.getRef();
              String target_1 = this.typeCompiler.compile(_ref_14, c, false);
              StringConcatenation _builder_4 = new StringConcatenation();
              String _simpleName_4 = de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl.ListExtensions.class.getSimpleName();
              _builder_4.append(_simpleName_4, "");
              _builder_4.append(".set(");
              IQLExpression _leftOperand_5 = arrayExpr.getLeftOperand();
              String _compile_8 = this.compile(_leftOperand_5, c);
              _builder_4.append(_compile_8, "");
              _builder_4.append(", ((");
              _builder_4.append(target_1, "");
              _builder_4.append(")");
              IQLExpression _rightOperand_6 = e.getRightOperand();
              String _compile_9 = this.compile(_rightOperand_6, c);
              _builder_4.append(_compile_9, "");
              _builder_4.append("), ");
              EList<IQLExpression> _expressions_4 = arrayExpr.getExpressions();
              final Function1<IQLExpression, String> _function_4 = new Function1<IQLExpression, String>() {
                public String apply(final IQLExpression el) {
                  return AbstractIQLExpressionCompiler.this.compile(el, c);
                }
              };
              List<String> _map_4 = ListExtensions.<IQLExpression, String>map(_expressions_4, _function_4);
              String _join_4 = IterableExtensions.join(_map_4, ", ");
              _builder_4.append(_join_4, "");
              _builder_4.append(")");
              result_1 = _builder_4.toString();
            } else {
              String _canonicalName_3 = de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl.ListExtensions.class.getCanonicalName();
              c.addImport(_canonicalName_3);
              StringConcatenation _builder_5 = new StringConcatenation();
              String _simpleName_5 = de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl.ListExtensions.class.getSimpleName();
              _builder_5.append(_simpleName_5, "");
              _builder_5.append(".set(");
              IQLExpression _leftOperand_6 = arrayExpr.getLeftOperand();
              String _compile_10 = this.compile(_leftOperand_6, c);
              _builder_5.append(_compile_10, "");
              _builder_5.append(", ");
              IQLExpression _rightOperand_7 = e.getRightOperand();
              String _compile_11 = this.compile(_rightOperand_7, c);
              _builder_5.append(_compile_11, "");
              _builder_5.append(", ");
              EList<IQLExpression> _expressions_5 = arrayExpr.getExpressions();
              final Function1<IQLExpression, String> _function_5 = new Function1<IQLExpression, String>() {
                public String apply(final IQLExpression el) {
                  return AbstractIQLExpressionCompiler.this.compile(el, c);
                }
              };
              List<String> _map_5 = ListExtensions.<IQLExpression, String>map(_expressions_5, _function_5);
              String _join_5 = IterableExtensions.join(_map_5, ", ");
              _builder_5.append(_join_5, "");
              _builder_5.append(")");
              result_1 = _builder_5.toString();
            }
          }
          c.setExpectedTypeRef(null);
          return result_1;
        } else {
          _xifexpression_1 = this.compileAssignmentExpr(e, c);
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compileAssignmentExpr(final IQLAssignmentExpression e, final G c) {
    IQLExpression _leftOperand = e.getLeftOperand();
    TypeResult leftType = this.exprParser.getType(_leftOperand);
    String _op = e.getOp();
    boolean _equals = _op.equals("=");
    if (_equals) {
      IQLExpression _rightOperand = e.getRightOperand();
      TypeResult rightType = this.exprParser.getType(_rightOperand);
      boolean _isNull = leftType.isNull();
      boolean _not = (!_isNull);
      if (_not) {
        JvmTypeReference _ref = leftType.getRef();
        c.setExpectedTypeRef(_ref);
      }
      String result = "";
      boolean _or = false;
      boolean _or_1 = false;
      boolean _isNull_1 = leftType.isNull();
      if (_isNull_1) {
        _or_1 = true;
      } else {
        boolean _isNull_2 = rightType.isNull();
        _or_1 = _isNull_2;
      }
      if (_or_1) {
        _or = true;
      } else {
        JvmTypeReference _ref_1 = leftType.getRef();
        JvmTypeReference _ref_2 = rightType.getRef();
        boolean _isAssignable = this.lookUp.isAssignable(_ref_1, _ref_2);
        _or = _isAssignable;
      }
      if (_or) {
        StringConcatenation _builder = new StringConcatenation();
        IQLExpression _leftOperand_1 = e.getLeftOperand();
        String _compile = this.compile(_leftOperand_1, c);
        _builder.append(_compile, "");
        _builder.append(" ");
        String _op_1 = e.getOp();
        _builder.append(_op_1, "");
        _builder.append(" ");
        IQLExpression _rightOperand_1 = e.getRightOperand();
        String _compile_1 = this.compile(_rightOperand_1, c);
        _builder.append(_compile_1, "");
        result = _builder.toString();
      } else {
        boolean _or_2 = false;
        boolean _or_3 = false;
        boolean _isNull_3 = leftType.isNull();
        if (_isNull_3) {
          _or_3 = true;
        } else {
          boolean _isNull_4 = rightType.isNull();
          _or_3 = _isNull_4;
        }
        if (_or_3) {
          _or_2 = true;
        } else {
          JvmTypeReference _ref_3 = leftType.getRef();
          JvmTypeReference _ref_4 = rightType.getRef();
          boolean _isCastable = this.lookUp.isCastable(_ref_3, _ref_4);
          _or_2 = _isCastable;
        }
        if (_or_2) {
          JvmTypeReference _ref_5 = leftType.getRef();
          String target = this.typeCompiler.compile(_ref_5, c, false);
          StringConcatenation _builder_1 = new StringConcatenation();
          IQLExpression _leftOperand_2 = e.getLeftOperand();
          String _compile_2 = this.compile(_leftOperand_2, c);
          _builder_1.append(_compile_2, "");
          _builder_1.append(" ");
          String _op_2 = e.getOp();
          _builder_1.append(_op_2, "");
          _builder_1.append(" ((");
          _builder_1.append(target, "");
          _builder_1.append(") ");
          IQLExpression _rightOperand_2 = e.getRightOperand();
          String _compile_3 = this.compile(_rightOperand_2, c);
          _builder_1.append(_compile_3, "");
          _builder_1.append(")");
          result = _builder_1.toString();
        } else {
          StringConcatenation _builder_2 = new StringConcatenation();
          IQLExpression _leftOperand_3 = e.getLeftOperand();
          String _compile_4 = this.compile(_leftOperand_3, c);
          _builder_2.append(_compile_4, "");
          _builder_2.append(" ");
          String _op_3 = e.getOp();
          _builder_2.append(_op_3, "");
          _builder_2.append(" ");
          IQLExpression _rightOperand_3 = e.getRightOperand();
          String _compile_5 = this.compile(_rightOperand_3, c);
          _builder_2.append(_compile_5, "");
          result = _builder_2.toString();
        }
      }
      c.setExpectedTypeRef(null);
      return result;
    } else {
      boolean _and = false;
      boolean _isNull_5 = leftType.isNull();
      boolean _not_1 = (!_isNull_5);
      if (!_not_1) {
        _and = false;
      } else {
        String _op_4 = e.getOp();
        boolean _equals_1 = _op_4.equals("+=");
        _and = _equals_1;
      }
      if (_and) {
        JvmTypeReference _ref_6 = leftType.getRef();
        IQLExpression _leftOperand_4 = e.getLeftOperand();
        IQLExpression _rightOperand_4 = e.getRightOperand();
        return this.compileOperatorOverloading("+", "plus", _ref_6, _leftOperand_4, _rightOperand_4, c);
      } else {
        boolean _and_1 = false;
        boolean _isNull_6 = leftType.isNull();
        boolean _not_2 = (!_isNull_6);
        if (!_not_2) {
          _and_1 = false;
        } else {
          String _op_5 = e.getOp();
          boolean _equals_2 = _op_5.equals("-=");
          _and_1 = _equals_2;
        }
        if (_and_1) {
          JvmTypeReference _ref_7 = leftType.getRef();
          IQLExpression _leftOperand_5 = e.getLeftOperand();
          IQLExpression _rightOperand_5 = e.getRightOperand();
          return this.compileOperatorOverloading("-", "minus", _ref_7, _leftOperand_5, _rightOperand_5, c);
        } else {
          boolean _and_2 = false;
          boolean _isNull_7 = leftType.isNull();
          boolean _not_3 = (!_isNull_7);
          if (!_not_3) {
            _and_2 = false;
          } else {
            String _op_6 = e.getOp();
            boolean _equals_3 = _op_6.equals("*=");
            _and_2 = _equals_3;
          }
          if (_and_2) {
            JvmTypeReference _ref_8 = leftType.getRef();
            IQLExpression _leftOperand_6 = e.getLeftOperand();
            IQLExpression _rightOperand_6 = e.getRightOperand();
            return this.compileOperatorOverloading("*", "multiply", _ref_8, _leftOperand_6, _rightOperand_6, c);
          } else {
            boolean _and_3 = false;
            boolean _isNull_8 = leftType.isNull();
            boolean _not_4 = (!_isNull_8);
            if (!_not_4) {
              _and_3 = false;
            } else {
              String _op_7 = e.getOp();
              boolean _equals_4 = _op_7.equals("/=");
              _and_3 = _equals_4;
            }
            if (_and_3) {
              JvmTypeReference _ref_9 = leftType.getRef();
              IQLExpression _leftOperand_7 = e.getLeftOperand();
              IQLExpression _rightOperand_7 = e.getRightOperand();
              return this.compileOperatorOverloading("/", "divide", _ref_9, _leftOperand_7, _rightOperand_7, c);
            } else {
              boolean _and_4 = false;
              boolean _isNull_9 = leftType.isNull();
              boolean _not_5 = (!_isNull_9);
              if (!_not_5) {
                _and_4 = false;
              } else {
                String _op_8 = e.getOp();
                boolean _equals_5 = _op_8.equals("%=");
                _and_4 = _equals_5;
              }
              if (_and_4) {
                JvmTypeReference _ref_10 = leftType.getRef();
                IQLExpression _leftOperand_8 = e.getLeftOperand();
                IQLExpression _rightOperand_8 = e.getRightOperand();
                return this.compileOperatorOverloading("%", "modulo", _ref_10, _leftOperand_8, _rightOperand_8, c);
              } else {
                return "";
              }
            }
          }
        }
      }
    }
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
  
  public String compileOperatorOverloading(final String operator, final String operatorName, final JvmTypeReference left, final IQLExpression leftOperand, final IQLExpression rightOperand, final G c) {
    String _xblockexpression = null;
    {
      TypeResult right = this.exprParser.getType(rightOperand);
      IIQLTypeExtensions typeOps = this.typeExtensionsFactory.getTypeExtensions(left, operatorName, rightOperand);
      Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
      String _canonicalName = _class.getCanonicalName();
      c.addImport(_canonicalName);
      JvmOperation _method = this.typeExtensionsFactory.getMethod(left, operatorName, rightOperand);
      EList<JvmFormalParameter> _parameters = _method.getParameters();
      JvmFormalParameter _get = _parameters.get(0);
      JvmTypeReference targetType = _get.getParameterType();
      String _xifexpression = null;
      boolean _and = false;
      boolean _isNull = right.isNull();
      boolean _not = (!_isNull);
      if (!_not) {
        _and = false;
      } else {
        JvmTypeReference _ref = right.getRef();
        boolean _isAssignable = this.lookUp.isAssignable(targetType, _ref);
        _and = _isAssignable;
      }
      if (_and) {
        StringConcatenation _builder = new StringConcatenation();
        Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
        String _simpleName = _class_1.getSimpleName();
        _builder.append(_simpleName, "");
        _builder.append(".");
        _builder.append(operatorName, "");
        _builder.append("(");
        String _compile = this.compile(leftOperand, c);
        _builder.append(_compile, "");
        _builder.append(", ");
        String _compile_1 = this.compile(rightOperand, c);
        _builder.append(_compile_1, "");
        _builder.append(")");
        _xifexpression = _builder.toString();
      } else {
        String _xblockexpression_1 = null;
        {
          String target = this.typeCompiler.compile(targetType, c, false);
          StringConcatenation _builder_1 = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_2 = typeOps.getClass();
          String _simpleName_1 = _class_2.getSimpleName();
          _builder_1.append(_simpleName_1, "");
          _builder_1.append(".");
          _builder_1.append(operatorName, "");
          _builder_1.append("(");
          String _compile_2 = this.compile(leftOperand, c);
          _builder_1.append(_compile_2, "");
          _builder_1.append(", ((");
          _builder_1.append(target, "");
          _builder_1.append(")");
          String _compile_3 = this.compile(rightOperand, c);
          _builder_1.append(_compile_3, "");
          _builder_1.append("))");
          _xblockexpression_1 = _builder_1.toString();
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLAdditiveExpression e, final G c) {
    IQLExpression _leftOperand = e.getLeftOperand();
    TypeResult left = this.exprParser.getType(_leftOperand);
    String result = "";
    boolean _and = false;
    boolean _and_1 = false;
    boolean _isNull = left.isNull();
    boolean _not = (!_isNull);
    if (!_not) {
      _and_1 = false;
    } else {
      String _op = e.getOp();
      boolean _equals = _op.equals("+");
      _and_1 = _equals;
    }
    if (!_and_1) {
      _and = false;
    } else {
      JvmTypeReference _ref = left.getRef();
      IQLExpression _rightOperand = e.getRightOperand();
      boolean _hasTypeExtensions = this.typeExtensionsFactory.hasTypeExtensions(_ref, "plus", _rightOperand);
      _and = _hasTypeExtensions;
    }
    if (_and) {
      JvmTypeReference _ref_1 = left.getRef();
      IQLExpression _leftOperand_1 = e.getLeftOperand();
      IQLExpression _rightOperand_1 = e.getRightOperand();
      String _compileOperatorOverloading = this.compileOperatorOverloading("+", "plus", _ref_1, _leftOperand_1, _rightOperand_1, c);
      result = _compileOperatorOverloading;
    } else {
      boolean _and_2 = false;
      boolean _and_3 = false;
      boolean _isNull_1 = left.isNull();
      boolean _not_1 = (!_isNull_1);
      if (!_not_1) {
        _and_3 = false;
      } else {
        String _op_1 = e.getOp();
        boolean _equals_1 = _op_1.equals("-");
        _and_3 = _equals_1;
      }
      if (!_and_3) {
        _and_2 = false;
      } else {
        JvmTypeReference _ref_2 = left.getRef();
        IQLExpression _rightOperand_2 = e.getRightOperand();
        boolean _hasTypeExtensions_1 = this.typeExtensionsFactory.hasTypeExtensions(_ref_2, "minus", _rightOperand_2);
        _and_2 = _hasTypeExtensions_1;
      }
      if (_and_2) {
        JvmTypeReference _ref_3 = left.getRef();
        IQLExpression _leftOperand_2 = e.getLeftOperand();
        IQLExpression _rightOperand_3 = e.getRightOperand();
        String _compileOperatorOverloading_1 = this.compileOperatorOverloading("-", "minus", _ref_3, _leftOperand_2, _rightOperand_3, c);
        result = _compileOperatorOverloading_1;
      } else {
        boolean _isNull_2 = left.isNull();
        boolean _not_2 = (!_isNull_2);
        if (_not_2) {
          JvmTypeReference _ref_4 = left.getRef();
          c.setExpectedTypeRef(_ref_4);
        }
        StringConcatenation _builder = new StringConcatenation();
        IQLExpression _leftOperand_3 = e.getLeftOperand();
        String _compile = this.compile(_leftOperand_3, c);
        _builder.append(_compile, "");
        _builder.append(" ");
        String _op_2 = e.getOp();
        _builder.append(_op_2, "");
        _builder.append(" ");
        IQLExpression _rightOperand_4 = e.getRightOperand();
        String _compile_1 = this.compile(_rightOperand_4, c);
        _builder.append(_compile_1, "");
        result = _builder.toString();
      }
    }
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLMultiplicativeExpression e, final G c) {
    IQLExpression _leftOperand = e.getLeftOperand();
    TypeResult left = this.exprParser.getType(_leftOperand);
    String result = "";
    boolean _and = false;
    boolean _and_1 = false;
    boolean _isNull = left.isNull();
    boolean _not = (!_isNull);
    if (!_not) {
      _and_1 = false;
    } else {
      String _op = e.getOp();
      boolean _equals = _op.equals("*");
      _and_1 = _equals;
    }
    if (!_and_1) {
      _and = false;
    } else {
      JvmTypeReference _ref = left.getRef();
      IQLExpression _rightOperand = e.getRightOperand();
      boolean _hasTypeExtensions = this.typeExtensionsFactory.hasTypeExtensions(_ref, "multiply", _rightOperand);
      _and = _hasTypeExtensions;
    }
    if (_and) {
      JvmTypeReference _ref_1 = left.getRef();
      IQLExpression _leftOperand_1 = e.getLeftOperand();
      IQLExpression _rightOperand_1 = e.getRightOperand();
      String _compileOperatorOverloading = this.compileOperatorOverloading("*", "multiply", _ref_1, _leftOperand_1, _rightOperand_1, c);
      result = _compileOperatorOverloading;
    } else {
      boolean _and_2 = false;
      boolean _and_3 = false;
      boolean _isNull_1 = left.isNull();
      boolean _not_1 = (!_isNull_1);
      if (!_not_1) {
        _and_3 = false;
      } else {
        String _op_1 = e.getOp();
        boolean _equals_1 = _op_1.equals("/");
        _and_3 = _equals_1;
      }
      if (!_and_3) {
        _and_2 = false;
      } else {
        JvmTypeReference _ref_2 = left.getRef();
        IQLExpression _rightOperand_2 = e.getRightOperand();
        boolean _hasTypeExtensions_1 = this.typeExtensionsFactory.hasTypeExtensions(_ref_2, "divide", _rightOperand_2);
        _and_2 = _hasTypeExtensions_1;
      }
      if (_and_2) {
        JvmTypeReference _ref_3 = left.getRef();
        IQLExpression _leftOperand_2 = e.getLeftOperand();
        IQLExpression _rightOperand_3 = e.getRightOperand();
        String _compileOperatorOverloading_1 = this.compileOperatorOverloading("/", "multiply", _ref_3, _leftOperand_2, _rightOperand_3, c);
        result = _compileOperatorOverloading_1;
      } else {
        boolean _and_4 = false;
        boolean _and_5 = false;
        boolean _isNull_2 = left.isNull();
        boolean _not_2 = (!_isNull_2);
        if (!_not_2) {
          _and_5 = false;
        } else {
          String _op_2 = e.getOp();
          boolean _equals_2 = _op_2.equals("%");
          _and_5 = _equals_2;
        }
        if (!_and_5) {
          _and_4 = false;
        } else {
          JvmTypeReference _ref_4 = left.getRef();
          IQLExpression _rightOperand_4 = e.getRightOperand();
          boolean _hasTypeExtensions_2 = this.typeExtensionsFactory.hasTypeExtensions(_ref_4, "modulo", _rightOperand_4);
          _and_4 = _hasTypeExtensions_2;
        }
        if (_and_4) {
          JvmTypeReference _ref_5 = left.getRef();
          IQLExpression _leftOperand_3 = e.getLeftOperand();
          IQLExpression _rightOperand_5 = e.getRightOperand();
          String _compileOperatorOverloading_2 = this.compileOperatorOverloading("%", "modulo", _ref_5, _leftOperand_3, _rightOperand_5, c);
          result = _compileOperatorOverloading_2;
        } else {
          boolean _isNull_3 = left.isNull();
          boolean _not_3 = (!_isNull_3);
          if (_not_3) {
            JvmTypeReference _ref_6 = left.getRef();
            c.setExpectedTypeRef(_ref_6);
          }
          StringConcatenation _builder = new StringConcatenation();
          IQLExpression _leftOperand_4 = e.getLeftOperand();
          String _compile = this.compile(_leftOperand_4, c);
          _builder.append(_compile, "");
          _builder.append(" ");
          String _op_3 = e.getOp();
          _builder.append(_op_3, "");
          _builder.append(" ");
          IQLExpression _rightOperand_6 = e.getRightOperand();
          String _compile_1 = this.compile(_rightOperand_6, c);
          _builder.append(_compile_1, "");
          result = _builder.toString();
        }
      }
    }
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
    String _xblockexpression = null;
    {
      IQLExpression _operand = e.getOperand();
      TypeResult left = this.exprParser.getType(_operand);
      String _xifexpression = null;
      boolean _and = false;
      boolean _and_1 = false;
      boolean _isNull = left.isNull();
      boolean _not = (!_isNull);
      if (!_not) {
        _and_1 = false;
      } else {
        String _op = e.getOp();
        boolean _equals = _op.equals("++");
        _and_1 = _equals;
      }
      if (!_and_1) {
        _and = false;
      } else {
        JvmTypeReference _ref = left.getRef();
        boolean _hasTypeExtensions = this.typeExtensionsFactory.hasTypeExtensions(_ref, "plusPrefix", 1);
        _and = _hasTypeExtensions;
      }
      if (_and) {
        String _xblockexpression_1 = null;
        {
          JvmTypeReference _ref_1 = left.getRef();
          IIQLTypeExtensions typeOps = this.typeExtensionsFactory.getTypeExtensions(_ref_1, "plusPrefix", 1);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          StringConcatenation _builder = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
          String _simpleName = _class_1.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append(".plusPrefix(");
          IQLExpression _operand_1 = e.getOperand();
          String _compile = this.compile(_operand_1, c);
          _builder.append(_compile, "");
          _builder.append(")");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        boolean _and_2 = false;
        boolean _and_3 = false;
        boolean _isNull_1 = left.isNull();
        boolean _not_1 = (!_isNull_1);
        if (!_not_1) {
          _and_3 = false;
        } else {
          String _op_1 = e.getOp();
          boolean _equals_1 = _op_1.equals("--");
          _and_3 = _equals_1;
        }
        if (!_and_3) {
          _and_2 = false;
        } else {
          JvmTypeReference _ref_1 = left.getRef();
          boolean _hasTypeExtensions_1 = this.typeExtensionsFactory.hasTypeExtensions(_ref_1, "minusPrefix", 1);
          _and_2 = _hasTypeExtensions_1;
        }
        if (_and_2) {
          String _xblockexpression_2 = null;
          {
            JvmTypeReference _ref_2 = left.getRef();
            IIQLTypeExtensions typeOps = this.typeExtensionsFactory.getTypeExtensions(_ref_2, "minusPrefix", 1);
            Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
            String _canonicalName = _class.getCanonicalName();
            c.addImport(_canonicalName);
            StringConcatenation _builder = new StringConcatenation();
            Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
            String _simpleName = _class_1.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append(".minusPrefix(");
            IQLExpression _operand_1 = e.getOperand();
            String _compile = this.compile(_operand_1, c);
            _builder.append(_compile, "");
            _builder.append(")");
            _xblockexpression_2 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          String _op_2 = e.getOp();
          _builder.append(_op_2, "");
          IQLExpression _operand_1 = e.getOperand();
          String _compile = this.compile(_operand_1, c);
          _builder.append(_compile, "");
          _xifexpression_1 = _builder.toString();
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
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
    String _xblockexpression = null;
    {
      IQLExpression _operand = e.getOperand();
      TypeResult right = this.exprParser.getType(_operand);
      String _xifexpression = null;
      boolean _and = false;
      boolean _and_1 = false;
      boolean _isNull = right.isNull();
      boolean _not = (!_isNull);
      if (!_not) {
        _and_1 = false;
      } else {
        String _op = e.getOp();
        boolean _equals = _op.equals("++");
        _and_1 = _equals;
      }
      if (!_and_1) {
        _and = false;
      } else {
        JvmTypeReference _ref = right.getRef();
        boolean _hasTypeExtensions = this.typeExtensionsFactory.hasTypeExtensions(_ref, "plusPostfix", 1);
        _and = _hasTypeExtensions;
      }
      if (_and) {
        String _xblockexpression_1 = null;
        {
          JvmTypeReference _ref_1 = right.getRef();
          IIQLTypeExtensions typeOps = this.typeExtensionsFactory.getTypeExtensions(_ref_1, "plusPostfix", 1);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          StringConcatenation _builder = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
          String _simpleName = _class_1.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append(".plusPostfix(");
          IQLExpression _operand_1 = e.getOperand();
          String _compile = this.compile(_operand_1, c);
          _builder.append(_compile, "");
          _builder.append(")");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        boolean _and_2 = false;
        boolean _and_3 = false;
        boolean _isNull_1 = right.isNull();
        boolean _not_1 = (!_isNull_1);
        if (!_not_1) {
          _and_3 = false;
        } else {
          String _op_1 = e.getOp();
          boolean _equals_1 = _op_1.equals("--");
          _and_3 = _equals_1;
        }
        if (!_and_3) {
          _and_2 = false;
        } else {
          JvmTypeReference _ref_1 = right.getRef();
          boolean _hasTypeExtensions_1 = this.typeExtensionsFactory.hasTypeExtensions(_ref_1, "minusPostfix", 1);
          _and_2 = _hasTypeExtensions_1;
        }
        if (_and_2) {
          String _xblockexpression_2 = null;
          {
            JvmTypeReference _ref_2 = right.getRef();
            IIQLTypeExtensions typeOps = this.typeExtensionsFactory.getTypeExtensions(_ref_2, "minusPostfix", 1);
            Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
            String _canonicalName = _class.getCanonicalName();
            c.addImport(_canonicalName);
            StringConcatenation _builder = new StringConcatenation();
            Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
            String _simpleName = _class_1.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append(".minusPostfix(");
            IQLExpression _operand_1 = e.getOperand();
            String _compile = this.compile(_operand_1, c);
            _builder.append(_compile, "");
            _builder.append(")");
            _xblockexpression_2 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          IQLExpression _operand_1 = e.getOperand();
          String _compile = this.compile(_operand_1, c);
          _builder.append(_compile, "");
          String _op_2 = e.getOp();
          _builder.append(_op_2, "");
          _xifexpression_1 = _builder.toString();
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLArrayExpression e, final G c) {
    String _xblockexpression = null;
    {
      IQLExpression _leftOperand = e.getLeftOperand();
      TypeResult left = this.exprParser.getType(_leftOperand);
      String _xifexpression = null;
      boolean _and = false;
      boolean _isNull = left.isNull();
      boolean _not = (!_isNull);
      if (!_not) {
        _and = false;
      } else {
        JvmTypeReference _ref = left.getRef();
        boolean _hasTypeExtensions = this.typeExtensionsFactory.hasTypeExtensions(_ref, "get", 2);
        _and = _hasTypeExtensions;
      }
      if (_and) {
        String _xblockexpression_1 = null;
        {
          JvmTypeReference _ref_1 = left.getRef();
          IIQLTypeExtensions typeOps = this.typeExtensionsFactory.getTypeExtensions(_ref_1, "get", 2);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          StringConcatenation _builder = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
          String _simpleName = _class_1.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append(".get(");
          IQLExpression _leftOperand_1 = e.getLeftOperand();
          String _compile = this.compile(_leftOperand_1, c);
          _builder.append(_compile, "");
          _builder.append(", ");
          EList<IQLExpression> _expressions = e.getExpressions();
          final Function1<IQLExpression, String> _function = new Function1<IQLExpression, String>() {
            public String apply(final IQLExpression el) {
              return AbstractIQLExpressionCompiler.this.compile(el, c);
            }
          };
          List<String> _map = ListExtensions.<IQLExpression, String>map(_expressions, _function);
          String _join = IterableExtensions.join(_map, ", ");
          _builder.append(_join, "");
          _builder.append(")");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        boolean _and_1 = false;
        boolean _isNull_1 = left.isNull();
        boolean _not_1 = (!_isNull_1);
        if (!_not_1) {
          _and_1 = false;
        } else {
          JvmTypeReference _ref_1 = left.getRef();
          boolean _isArray = this.typeUtils.isArray(_ref_1);
          _and_1 = _isArray;
        }
        if (_and_1) {
          String _xblockexpression_2 = null;
          {
            String _canonicalName = de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl.ListExtensions.class.getCanonicalName();
            c.addImport(_canonicalName);
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl.ListExtensions.class.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append(".get(");
            IQLExpression _leftOperand_1 = e.getLeftOperand();
            String _compile = this.compile(_leftOperand_1, c);
            _builder.append(_compile, "");
            _builder.append(", ");
            EList<IQLExpression> _expressions = e.getExpressions();
            final Function1<IQLExpression, String> _function = new Function1<IQLExpression, String>() {
              public String apply(final IQLExpression el) {
                return AbstractIQLExpressionCompiler.this.compile(el, c);
              }
            };
            List<String> _map = ListExtensions.<IQLExpression, String>map(_expressions, _function);
            String _join = IterableExtensions.join(_map, ", ");
            _builder.append(_join, "");
            _builder.append(")");
            _xblockexpression_2 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          IQLExpression _leftOperand_1 = e.getLeftOperand();
          String _compile = this.compile(_leftOperand_1, c);
          _builder.append(_compile, "");
          _builder.append("[");
          EList<IQLExpression> _expressions = e.getExpressions();
          IQLExpression _get = _expressions.get(0);
          String _compile_1 = this.compile(_get, c);
          _builder.append(_compile_1, "");
          _builder.append("]");
          _xifexpression_1 = _builder.toString();
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLMemberSelectionExpression e, final G c) {
    String _xblockexpression = null;
    {
      IQLExpression _leftOperand = e.getLeftOperand();
      TypeResult _type = this.exprParser.getType(_leftOperand);
      JvmTypeReference left = _type.getRef();
      String _xifexpression = null;
      IQLMemberSelection _sel = e.getSel();
      JvmMember _member = _sel.getMember();
      if ((_member instanceof JvmField)) {
        StringConcatenation _builder = new StringConcatenation();
        IQLMemberSelection _sel_1 = e.getSel();
        JvmMember _member_1 = _sel_1.getMember();
        String _compile = this.compile(e, left, ((JvmField) _member_1), c);
        _builder.append(_compile, "");
        _xifexpression = _builder.toString();
      } else {
        String _xifexpression_1 = null;
        IQLMemberSelection _sel_2 = e.getSel();
        JvmMember _member_2 = _sel_2.getMember();
        if ((_member_2 instanceof JvmOperation)) {
          StringConcatenation _builder_1 = new StringConcatenation();
          IQLMemberSelection _sel_3 = e.getSel();
          JvmMember _member_3 = _sel_3.getMember();
          String _compile_1 = this.compile(e, left, ((JvmOperation) _member_3), c);
          _builder_1.append(_compile_1, "");
          _xifexpression_1 = _builder_1.toString();
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLMemberSelectionExpression e, final JvmTypeReference left, final JvmField field, final G c) {
    String _xifexpression = null;
    String _simpleName = field.getSimpleName();
    boolean _hasTypeExtensions = this.typeExtensionsFactory.hasTypeExtensions(left, _simpleName);
    if (_hasTypeExtensions) {
      String _xblockexpression = null;
      {
        String _simpleName_1 = field.getSimpleName();
        IIQLTypeExtensions typeOps = this.typeExtensionsFactory.getTypeExtensions(left, _simpleName_1);
        Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
        String _canonicalName = _class.getCanonicalName();
        c.addImport(_canonicalName);
        StringConcatenation _builder = new StringConcatenation();
        Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
        String _simpleName_2 = _class_1.getSimpleName();
        _builder.append(_simpleName_2, "");
        _builder.append(".");
        String _simpleName_3 = field.getSimpleName();
        _builder.append(_simpleName_3, "");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      StringConcatenation _builder = new StringConcatenation();
      IQLExpression _leftOperand = e.getLeftOperand();
      String _compile = this.compile(_leftOperand, c);
      _builder.append(_compile, "");
      _builder.append(".");
      String _simpleName_1 = field.getSimpleName();
      _builder.append(_simpleName_1, "");
      _xifexpression = _builder.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLMemberSelectionExpression e, final JvmTypeReference left, final JvmOperation method, final G c) {
    String _xblockexpression = null;
    {
      List<IQLExpression> list = null;
      IQLMemberSelection _sel = e.getSel();
      IQLArgumentsList _args = _sel.getArgs();
      boolean _notEquals = (!Objects.equal(_args, null));
      if (_notEquals) {
        IQLMemberSelection _sel_1 = e.getSel();
        IQLArgumentsList _args_1 = _sel_1.getArgs();
        EList<IQLExpression> _elements = _args_1.getElements();
        list = _elements;
      } else {
        ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
        list = _arrayList;
      }
      String _xifexpression = null;
      String _simpleName = method.getSimpleName();
      boolean _hasTypeExtensions = this.typeExtensionsFactory.hasTypeExtensions(left, _simpleName, list);
      if (_hasTypeExtensions) {
        String _xblockexpression_1 = null;
        {
          String _simpleName_1 = method.getSimpleName();
          IIQLTypeExtensions typeOps = this.typeExtensionsFactory.getTypeExtensions(left, _simpleName_1, list);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          StringConcatenation _builder = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
          String _simpleName_2 = _class_1.getSimpleName();
          _builder.append(_simpleName_2, "");
          _builder.append(".");
          String _simpleName_3 = method.getSimpleName();
          _builder.append(_simpleName_3, "");
          _builder.append("(");
          IQLExpression _leftOperand = e.getLeftOperand();
          String _compile = this.compile(_leftOperand, c);
          _builder.append(_compile, "");
          {
            EList<JvmFormalParameter> _parameters = method.getParameters();
            int _size = _parameters.size();
            boolean _greaterThan = (_size > 0);
            if (_greaterThan) {
              _builder.append(", ");
            }
          }
          IQLMemberSelection _sel_2 = e.getSel();
          IQLArgumentsList _args_2 = _sel_2.getArgs();
          EList<JvmFormalParameter> _parameters_1 = method.getParameters();
          String _compile_1 = this.compile(_args_2, _parameters_1, c);
          _builder.append(_compile_1, "");
          _builder.append(")");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        StringConcatenation _builder = new StringConcatenation();
        IQLExpression _leftOperand = e.getLeftOperand();
        String _compile = this.compile(_leftOperand, c);
        _builder.append(_compile, "");
        _builder.append(".");
        String _simpleName_1 = method.getSimpleName();
        _builder.append(_simpleName_1, "");
        _builder.append("(");
        IQLMemberSelection _sel_2 = e.getSel();
        IQLArgumentsList _args_2 = _sel_2.getArgs();
        EList<JvmFormalParameter> _parameters = method.getParameters();
        String _compile_1 = this.compile(_args_2, _parameters, c);
        _builder.append(_compile_1, "");
        _builder.append(")");
        _xifexpression = _builder.toString();
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLArgumentsList args, final EList<JvmFormalParameter> parameters, final G c) {
    String result = "";
    boolean _equals = Objects.equal(args, null);
    if (_equals) {
      return "";
    }
    for (int i = 0; (i < parameters.size()); i++) {
      {
        if ((i > 0)) {
          result = (result + ",");
        }
        JvmFormalParameter _get = parameters.get(i);
        JvmTypeReference _parameterType = _get.getParameterType();
        c.setExpectedTypeRef(_parameterType);
        EList<IQLExpression> _elements = args.getElements();
        IQLExpression _get_1 = _elements.get(i);
        TypeResult type = this.exprParser.getType(_get_1);
        boolean _and = false;
        boolean _isNull = type.isNull();
        boolean _not = (!_isNull);
        if (!_not) {
          _and = false;
        } else {
          JvmFormalParameter _get_2 = parameters.get(i);
          JvmTypeReference _parameterType_1 = _get_2.getParameterType();
          JvmTypeReference _ref = type.getRef();
          boolean _isAssignable = this.lookUp.isAssignable(_parameterType_1, _ref);
          _and = _isAssignable;
        }
        if (_and) {
          EList<IQLExpression> _elements_1 = args.getElements();
          IQLExpression _get_3 = _elements_1.get(i);
          String _compile = this.compile(_get_3, c);
          String _plus = (result + _compile);
          result = _plus;
        } else {
          boolean _and_1 = false;
          boolean _isNull_1 = type.isNull();
          boolean _not_1 = (!_isNull_1);
          if (!_not_1) {
            _and_1 = false;
          } else {
            JvmFormalParameter _get_4 = parameters.get(i);
            JvmTypeReference _parameterType_2 = _get_4.getParameterType();
            JvmTypeReference _ref_1 = type.getRef();
            boolean _isCastable = this.lookUp.isCastable(_parameterType_2, _ref_1);
            _and_1 = _isCastable;
          }
          if (_and_1) {
            JvmFormalParameter _get_5 = parameters.get(i);
            JvmTypeReference _parameterType_3 = _get_5.getParameterType();
            String target = this.typeCompiler.compile(_parameterType_3, c, false);
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("((");
            _builder.append(target, "");
            _builder.append(")");
            EList<IQLExpression> _elements_2 = args.getElements();
            IQLExpression _get_6 = _elements_2.get(i);
            String _compile_1 = this.compile(_get_6, c);
            _builder.append(_compile_1, "");
            _builder.append(")");
            String _plus_1 = (result + _builder);
            result = _plus_1;
          } else {
            EList<IQLExpression> _elements_3 = args.getElements();
            IQLExpression _get_7 = _elements_3.get(i);
            String _compile_2 = this.compile(_get_7, c);
            String _plus_2 = (result + _compile_2);
            result = _plus_2;
          }
        }
        c.setExpectedTypeRef(null);
      }
    }
    return result;
  }
  
  public String compile(final IQLArgumentsList list, final G context) {
    String _xblockexpression = null;
    {
      boolean _equals = Objects.equal(list, null);
      if (_equals) {
        return "";
      }
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
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
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
  
  public String compile(final IQLJvmElementCallExpression e, final G c) {
    String _xifexpression = null;
    JvmIdentifiableElement _element = e.getElement();
    if ((_element instanceof JvmOperation)) {
      JvmIdentifiableElement _element_1 = e.getElement();
      _xifexpression = this.compile(e, ((JvmOperation) _element_1), c);
    } else {
      String _xifexpression_1 = null;
      JvmIdentifiableElement _element_2 = e.getElement();
      if ((_element_2 instanceof JvmField)) {
        JvmIdentifiableElement _element_3 = e.getElement();
        _xifexpression_1 = this.compile(e, ((JvmField) _element_3), c);
      } else {
        String _xifexpression_2 = null;
        JvmIdentifiableElement _element_4 = e.getElement();
        if ((_element_4 instanceof IQLVariableDeclaration)) {
          JvmIdentifiableElement _element_5 = e.getElement();
          _xifexpression_2 = this.compile(e, ((IQLVariableDeclaration) _element_5), c);
        } else {
          String _xifexpression_3 = null;
          JvmIdentifiableElement _element_6 = e.getElement();
          if ((_element_6 instanceof JvmFormalParameter)) {
            JvmIdentifiableElement _element_7 = e.getElement();
            _xifexpression_3 = this.compile(e, ((JvmFormalParameter) _element_7), c);
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLJvmElementCallExpression e, final JvmField field, final G c) {
    String _xifexpression = null;
    boolean _isStatic = field.isStatic();
    if (_isStatic) {
      String _xblockexpression = null;
      {
        EObject _eContainer = field.eContainer();
        JvmDeclaredType containerType = ((JvmDeclaredType) _eContainer);
        String _longName = this.typeUtils.getLongName(containerType, false);
        Class<?> javaType = this.typeUtils.getJavaType(_longName);
        String _canonicalName = javaType.getCanonicalName();
        c.addImport(_canonicalName);
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = javaType.getSimpleName();
        _builder.append(_simpleName, "");
        _builder.append(".");
        String _simpleName_1 = field.getSimpleName();
        _builder.append(_simpleName_1, "");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      StringConcatenation _builder = new StringConcatenation();
      String _simpleName = field.getSimpleName();
      _builder.append(_simpleName, "");
      _xifexpression = _builder.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLJvmElementCallExpression e, final IQLVariableDeclaration varDecl, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = varDecl.getName();
    _builder.append(_name, "");
    return _builder.toString();
  }
  
  public String compile(final IQLJvmElementCallExpression e, final JvmFormalParameter parameter, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = parameter.getName();
    _builder.append(_name, "");
    return _builder.toString();
  }
  
  public String compile(final IQLJvmElementCallExpression m, final JvmOperation method, final G c) {
    String _xblockexpression = null;
    {
      List<IQLExpression> list = null;
      IQLArgumentsList _args = m.getArgs();
      boolean _notEquals = (!Objects.equal(_args, null));
      if (_notEquals) {
        IQLArgumentsList _args_1 = m.getArgs();
        EList<IQLExpression> _elements = _args_1.getElements();
        list = _elements;
      } else {
        ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
        list = _arrayList;
      }
      TypeResult typeDef = this.exprParser.getThisType(m);
      String _xifexpression = null;
      boolean _and = false;
      boolean _isNull = typeDef.isNull();
      boolean _not = (!_isNull);
      if (!_not) {
        _and = false;
      } else {
        JvmTypeReference _ref = typeDef.getRef();
        String _simpleName = method.getSimpleName();
        boolean _hasTypeExtensions = this.typeExtensionsFactory.hasTypeExtensions(_ref, _simpleName, list);
        _and = _hasTypeExtensions;
      }
      if (_and) {
        String _xblockexpression_1 = null;
        {
          JvmTypeReference _ref_1 = typeDef.getRef();
          String _simpleName_1 = method.getSimpleName();
          IIQLTypeExtensions typeOps = this.typeExtensionsFactory.getTypeExtensions(_ref_1, _simpleName_1, list);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          StringConcatenation _builder = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
          String _simpleName_2 = _class_1.getSimpleName();
          _builder.append(_simpleName_2, "");
          _builder.append(".");
          String _simpleName_3 = method.getSimpleName();
          _builder.append(_simpleName_3, "");
          _builder.append("(this");
          {
            EList<JvmFormalParameter> _parameters = method.getParameters();
            int _size = _parameters.size();
            boolean _greaterThan = (_size > 0);
            if (_greaterThan) {
              _builder.append(", ");
            }
          }
          IQLArgumentsList _args_2 = m.getArgs();
          EList<JvmFormalParameter> _parameters_1 = method.getParameters();
          String _compile = this.compile(_args_2, _parameters_1, c);
          _builder.append(_compile, "");
          _builder.append(")");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        boolean _isStatic = method.isStatic();
        if (_isStatic) {
          String _xblockexpression_2 = null;
          {
            EObject _eContainer = method.eContainer();
            JvmDeclaredType containerType = ((JvmDeclaredType) _eContainer);
            String _longName = this.typeUtils.getLongName(containerType, false);
            Class<?> javaType = this.typeUtils.getJavaType(_longName);
            String _canonicalName = javaType.getCanonicalName();
            c.addImport(_canonicalName);
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName_1 = javaType.getSimpleName();
            _builder.append(_simpleName_1, "");
            _builder.append(".");
            String _simpleName_2 = method.getSimpleName();
            _builder.append(_simpleName_2, "");
            _builder.append("(");
            IQLArgumentsList _args_2 = m.getArgs();
            EList<JvmFormalParameter> _parameters = method.getParameters();
            String _compile = this.compile(_args_2, _parameters, c);
            _builder.append(_compile, "");
            _builder.append(")");
            _xblockexpression_2 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          String _simpleName_1 = method.getSimpleName();
          _builder.append(_simpleName_1, "");
          _builder.append("(");
          {
            IQLArgumentsList _args_2 = m.getArgs();
            boolean _notEquals_1 = (!Objects.equal(_args_2, null));
            if (_notEquals_1) {
              IQLArgumentsList _args_3 = m.getArgs();
              String _compile = this.compile(_args_3, c);
              _builder.append(_compile, "");
            }
          }
          _builder.append(")");
          _xifexpression_1 = _builder.toString();
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLThisExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("this");
    return _builder.toString();
  }
  
  public String compile(final IQLSuperExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("super");
    return _builder.toString();
  }
  
  public String compile(final IQLParenthesisExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    IQLExpression _expr = e.getExpr();
    String _compile = this.compile(_expr, c);
    _builder.append(_compile, "");
    _builder.append(")");
    return _builder.toString();
  }
  
  public String compile(final IQLNewExpression e, final G c) {
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
        EList<IQLExpression> _elements_1 = _argsList.getElements();
        JvmExecutable constructor = this.lookUp.findPublicConstructor(_ref, _elements_1);
        String _xifexpression_1 = null;
        boolean _notEquals_1 = (!Objects.equal(constructor, null));
        if (_notEquals_1) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("get");
          JvmTypeReference _ref_1 = e.getRef();
          String _shortName = this.typeUtils.getShortName(_ref_1, false);
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
          String _shortName_1 = this.typeUtils.getShortName(_ref_5, false);
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
          EList<IQLExpression> _elements_1 = _argsList_1.getElements();
          JvmExecutable constructor = this.lookUp.findPublicConstructor(_ref, _elements_1);
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
      boolean _isFloat = this.typeUtils.isFloat(_expectedTypeRef_1);
      if (_isFloat) {
        StringConcatenation _builder = new StringConcatenation();
        double _value = e.getValue();
        _builder.append(_value, "");
        _builder.append("F");
        _xifexpression_1 = _builder.toString();
      } else {
        String _xifexpression_2 = null;
        JvmTypeReference _expectedTypeRef_2 = c.getExpectedTypeRef();
        boolean _isDouble = this.typeUtils.isDouble(_expectedTypeRef_2, true);
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
      boolean _isFloat = this.typeUtils.isFloat(_expectedTypeRef_1);
      if (_isFloat) {
        StringConcatenation _builder = new StringConcatenation();
        int _value = e.getValue();
        _builder.append(_value, "");
        _builder.append("F");
        _xifexpression_1 = _builder.toString();
      } else {
        String _xifexpression_2 = null;
        JvmTypeReference _expectedTypeRef_2 = c.getExpectedTypeRef();
        boolean _isDouble = this.typeUtils.isDouble(_expectedTypeRef_2, true);
        if (_isDouble) {
          StringConcatenation _builder_1 = new StringConcatenation();
          int _value_1 = e.getValue();
          _builder_1.append(_value_1, "");
          _builder_1.append("D");
          _xifexpression_2 = _builder_1.toString();
        } else {
          String _xifexpression_3 = null;
          JvmTypeReference _expectedTypeRef_3 = c.getExpectedTypeRef();
          boolean _isLong = this.typeUtils.isLong(_expectedTypeRef_3, true);
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
    String _xifexpression = null;
    JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
    boolean _notEquals = (!Objects.equal(_expectedTypeRef, null));
    if (_notEquals) {
      String _xifexpression_1 = null;
      JvmTypeReference _expectedTypeRef_1 = c.getExpectedTypeRef();
      boolean _isCharacter = this.typeUtils.isCharacter(_expectedTypeRef_1);
      if (_isCharacter) {
        String _value = e.getValue();
        String _plus = ("\'" + _value);
        return (_plus + "\'");
      } else {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("\"");
        String _value_1 = e.getValue();
        _builder.append(_value_1, "");
        _builder.append("\"");
        _xifexpression_1 = _builder.toString();
      }
      _xifexpression = _xifexpression_1;
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("\"");
      String _value_2 = e.getValue();
      _builder_1.append(_value_2, "");
      _builder_1.append("\"");
      _xifexpression = _builder_1.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLLiteralExpressionBoolean e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    boolean _isValue = e.isValue();
    _builder.append(_isValue, "");
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
      _builder.append("(");
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
