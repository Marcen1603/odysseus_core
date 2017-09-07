package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

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
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression;
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
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.IIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.TypeResult;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.types.IQLUtils;
import de.uniol.inf.is.odysseus.iql.basic.types.Range;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.IIQLSystemTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IQLOperatorOverloadingUtils;
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
public abstract class AbstractIQLExpressionCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, E extends IIQLExpressionEvaluator, U extends IIQLTypeUtils, L extends IIQLLookUp, O extends IIQLTypeExtensionsDictionary, D extends IIQLTypeDictionary> implements IIQLExpressionCompiler<G> {
  protected H helper;
  
  protected T typeCompiler;
  
  protected E exprEvaluator;
  
  protected U typeUtils;
  
  protected L lookUp;
  
  protected O typeExtensionsDictionary;
  
  protected D typeDictionary;
  
  public AbstractIQLExpressionCompiler(final H helper, final T typeCompiler, final E exprEvaluator, final U typeUtils, final L lookUp, final O typeExtensionsDictionary, final D typeDictionary) {
    this.helper = helper;
    this.typeCompiler = typeCompiler;
    this.exprEvaluator = exprEvaluator;
    this.typeUtils = typeUtils;
    this.lookUp = lookUp;
    this.typeExtensionsDictionary = typeExtensionsDictionary;
    this.typeDictionary = typeDictionary;
  }
  
  @Override
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
                                                          } else {
                                                            if ((expr instanceof IQLLiteralExpressionType)) {
                                                              return this.compile(((IQLLiteralExpressionType) expr), context);
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
    if ((e.getOp().equals("=") && (elementCallExpr.getElement() instanceof JvmOperation))) {
      JvmIdentifiableElement _element = elementCallExpr.getElement();
      JvmTypeReference leftType = ((JvmOperation) _element).getParameters().get(0).getParameterType();
      TypeResult rightType = this.exprEvaluator.eval(e.getRightOperand());
      c.setExpectedTypeRef(leftType);
      String result = "";
      JvmIdentifiableElement _element_1 = elementCallExpr.getElement();
      JvmOperation op = ((JvmOperation) _element_1);
      c.addExceptions(op.getExceptions());
      if (((this.helper.isJvmArray(leftType) && (!rightType.isNull())) && (!this.helper.isJvmArray(rightType.getRef())))) {
        c.addImport(IQLUtils.class.getCanonicalName());
        boolean _isPrimitiveArray = this.helper.isPrimitiveArray(leftType);
        if (_isPrimitiveArray) {
          StringConcatenation _builder = new StringConcatenation();
          String _simpleName = op.getSimpleName();
          _builder.append(_simpleName);
          _builder.append("(");
          String _simpleName_1 = IQLUtils.class.getSimpleName();
          _builder.append(_simpleName_1);
          _builder.append(".");
          String _arrayMethodName = this.helper.getArrayMethodName(leftType);
          _builder.append(_arrayMethodName);
          _builder.append("(");
          String _compile = this.compile(e.getRightOperand(), c);
          _builder.append(_compile);
          _builder.append("))");
          result = _builder.toString();
        } else {
          String clazz = this.typeCompiler.compile(this.typeUtils.createTypeRef(this.typeUtils.getInnerType(leftType, false)), c, true);
          StringConcatenation _builder_1 = new StringConcatenation();
          String _simpleName_2 = op.getSimpleName();
          _builder_1.append(_simpleName_2);
          _builder_1.append("(");
          String _simpleName_3 = IQLUtils.class.getSimpleName();
          _builder_1.append(_simpleName_3);
          _builder_1.append(".");
          String _arrayMethodName_1 = this.helper.getArrayMethodName(leftType);
          _builder_1.append(_arrayMethodName_1);
          _builder_1.append("(");
          _builder_1.append(clazz);
          _builder_1.append(".class, ");
          String _compile_1 = this.compile(e.getRightOperand(), c);
          _builder_1.append(_compile_1);
          _builder_1.append("))");
          result = _builder_1.toString();
        }
      } else {
        if ((rightType.isNull() || this.lookUp.isAssignable(leftType, rightType.getRef()))) {
          StringConcatenation _builder_2 = new StringConcatenation();
          String _simpleName_4 = op.getSimpleName();
          _builder_2.append(_simpleName_4);
          _builder_2.append("(");
          String _compile_2 = this.compile(e.getRightOperand(), c);
          _builder_2.append(_compile_2);
          _builder_2.append(")");
          result = _builder_2.toString();
        } else {
          if ((rightType.isNull() || this.lookUp.isCastable(leftType, rightType.getRef()))) {
            String target = this.typeCompiler.compile(leftType, c, false);
            StringConcatenation _builder_3 = new StringConcatenation();
            String _simpleName_5 = op.getSimpleName();
            _builder_3.append(_simpleName_5);
            _builder_3.append("((");
            _builder_3.append(target);
            _builder_3.append(")");
            String _compile_3 = this.compile(e.getRightOperand(), c);
            _builder_3.append(_compile_3);
            _builder_3.append(")");
            result = _builder_3.toString();
          } else {
            StringConcatenation _builder_4 = new StringConcatenation();
            String _simpleName_6 = op.getSimpleName();
            _builder_4.append(_simpleName_6);
            _builder_4.append("(");
            String _compile_4 = this.compile(e.getRightOperand(), c);
            _builder_4.append(_compile_4);
            _builder_4.append(")");
            result = _builder_4.toString();
          }
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
    if ((e.getOp().equals("=") && (selExpr.getSel().getMember() instanceof JvmOperation))) {
      JvmMember _member = selExpr.getSel().getMember();
      JvmTypeReference leftType = ((JvmOperation) _member).getParameters().get(0).getParameterType();
      TypeResult rightType = this.exprEvaluator.eval(e.getRightOperand());
      c.setExpectedTypeRef(leftType);
      String result = "";
      JvmMember _member_1 = selExpr.getSel().getMember();
      JvmOperation op = ((JvmOperation) _member_1);
      c.addExceptions(op.getExceptions());
      if (((this.helper.isJvmArray(leftType) && (!rightType.isNull())) && (!this.helper.isJvmArray(rightType.getRef())))) {
        c.addImport(IQLUtils.class.getCanonicalName());
        if ((this.helper.hasSystemTypeCompiler(op) && this.helper.getSystemTypeCompiler(op).compileMethodSelectionManually())) {
          IIQLSystemTypeCompiler systemTypeCompiler = this.helper.getSystemTypeCompiler(op);
          boolean _isPrimitiveArray = this.helper.isPrimitiveArray(leftType);
          if (_isPrimitiveArray) {
            StringConcatenation _builder = new StringConcatenation();
            String _compile = this.compile(selExpr.getLeftOperand(), c);
            _builder.append(_compile);
            _builder.append(".");
            String _simpleName = IQLUtils.class.getSimpleName();
            String _plus = (_simpleName + ".");
            String _arrayMethodName = this.helper.getArrayMethodName(leftType);
            String _plus_1 = (_plus + _arrayMethodName);
            String _plus_2 = (_plus_1 + "(");
            String _compile_1 = this.compile(e.getRightOperand(), c);
            String _plus_3 = (_plus_2 + _compile_1);
            String _plus_4 = (_plus_3 + ")");
            String _compileMethodSelection = systemTypeCompiler.compileMethodSelection(op, this.helper.toList(_plus_4));
            _builder.append(_compileMethodSelection);
            result = _builder.toString();
          } else {
            String clazz = this.typeCompiler.compile(this.typeUtils.createTypeRef(this.typeUtils.getInnerType(leftType, false)), c, true);
            StringConcatenation _builder_1 = new StringConcatenation();
            String _compile_2 = this.compile(selExpr.getLeftOperand(), c);
            _builder_1.append(_compile_2);
            _builder_1.append(".");
            String _simpleName_1 = IQLUtils.class.getSimpleName();
            String _plus_5 = (_simpleName_1 + ".");
            String _arrayMethodName_1 = this.helper.getArrayMethodName(leftType);
            String _plus_6 = (_plus_5 + _arrayMethodName_1);
            String _plus_7 = (_plus_6 + "(");
            String _plus_8 = (_plus_7 + clazz);
            String _plus_9 = (_plus_8 + ".class, ");
            String _compile_3 = this.compile(e.getRightOperand(), c);
            String _plus_10 = (_plus_9 + _compile_3);
            String _plus_11 = (_plus_10 + ")");
            String _compileMethodSelection_1 = systemTypeCompiler.compileMethodSelection(op, this.helper.toList(_plus_11));
            _builder_1.append(_compileMethodSelection_1);
            result = _builder_1.toString();
          }
        } else {
          boolean _isPrimitiveArray_1 = this.helper.isPrimitiveArray(leftType);
          if (_isPrimitiveArray_1) {
            StringConcatenation _builder_2 = new StringConcatenation();
            String _compile_4 = this.compile(selExpr.getLeftOperand(), c);
            _builder_2.append(_compile_4);
            _builder_2.append(".");
            String _simpleName_2 = op.getSimpleName();
            _builder_2.append(_simpleName_2);
            _builder_2.append("(");
            String _simpleName_3 = IQLUtils.class.getSimpleName();
            _builder_2.append(_simpleName_3);
            _builder_2.append(".");
            String _arrayMethodName_2 = this.helper.getArrayMethodName(leftType);
            _builder_2.append(_arrayMethodName_2);
            _builder_2.append("(");
            String _compile_5 = this.compile(e.getRightOperand(), c);
            _builder_2.append(_compile_5);
            _builder_2.append("))");
            result = _builder_2.toString();
          } else {
            String clazz_1 = this.typeCompiler.compile(this.typeUtils.createTypeRef(this.typeUtils.getInnerType(leftType, false)), c, true);
            StringConcatenation _builder_3 = new StringConcatenation();
            String _compile_6 = this.compile(selExpr.getLeftOperand(), c);
            _builder_3.append(_compile_6);
            _builder_3.append(".");
            String _simpleName_4 = op.getSimpleName();
            _builder_3.append(_simpleName_4);
            _builder_3.append("(");
            String _simpleName_5 = IQLUtils.class.getSimpleName();
            _builder_3.append(_simpleName_5);
            _builder_3.append(".");
            String _arrayMethodName_3 = this.helper.getArrayMethodName(leftType);
            _builder_3.append(_arrayMethodName_3);
            _builder_3.append("(");
            _builder_3.append(clazz_1);
            _builder_3.append(".class, ");
            String _compile_7 = this.compile(e.getRightOperand(), c);
            _builder_3.append(_compile_7);
            _builder_3.append("))");
            result = _builder_3.toString();
          }
        }
      } else {
        if ((rightType.isNull() || this.lookUp.isAssignable(leftType, rightType.getRef()))) {
          if ((this.helper.hasSystemTypeCompiler(op) && this.helper.getSystemTypeCompiler(op).compileMethodSelectionManually())) {
            IIQLSystemTypeCompiler systemTypeCompiler_1 = this.helper.getSystemTypeCompiler(op);
            StringConcatenation _builder_4 = new StringConcatenation();
            String _compile_8 = this.compile(selExpr.getLeftOperand(), c);
            _builder_4.append(_compile_8);
            _builder_4.append(".");
            String _compileMethodSelection_2 = systemTypeCompiler_1.compileMethodSelection(op, this.helper.toList(this.compile(e.getRightOperand(), c)));
            _builder_4.append(_compileMethodSelection_2);
            result = _builder_4.toString();
          } else {
            StringConcatenation _builder_5 = new StringConcatenation();
            String _compile_9 = this.compile(selExpr.getLeftOperand(), c);
            _builder_5.append(_compile_9);
            _builder_5.append(".");
            String _simpleName_6 = op.getSimpleName();
            _builder_5.append(_simpleName_6);
            _builder_5.append("(");
            String _compile_10 = this.compile(e.getRightOperand(), c);
            _builder_5.append(_compile_10);
            _builder_5.append(")");
            result = _builder_5.toString();
          }
        } else {
          if ((rightType.isNull() || this.lookUp.isCastable(leftType, rightType.getRef()))) {
            String target = this.typeCompiler.compile(leftType, c, false);
            if ((this.helper.hasSystemTypeCompiler(op) && this.helper.getSystemTypeCompiler(op).compileMethodSelectionManually())) {
              IIQLSystemTypeCompiler systemTypeCompiler_2 = this.helper.getSystemTypeCompiler(op);
              StringConcatenation _builder_6 = new StringConcatenation();
              String _compile_11 = this.compile(selExpr.getLeftOperand(), c);
              _builder_6.append(_compile_11);
              _builder_6.append(".");
              String _compile_12 = this.compile(e.getRightOperand(), c);
              String _plus_12 = ((("(" + target) + ")") + _compile_12);
              String _compileMethodSelection_3 = systemTypeCompiler_2.compileMethodSelection(op, this.helper.toList(_plus_12));
              _builder_6.append(_compileMethodSelection_3);
              result = _builder_6.toString();
            } else {
              StringConcatenation _builder_7 = new StringConcatenation();
              String _compile_13 = this.compile(selExpr.getLeftOperand(), c);
              _builder_7.append(_compile_13);
              _builder_7.append(".");
              String _simpleName_7 = op.getSimpleName();
              _builder_7.append(_simpleName_7);
              _builder_7.append("((");
              _builder_7.append(target);
              _builder_7.append(")");
              String _compile_14 = this.compile(e.getRightOperand(), c);
              _builder_7.append(_compile_14);
              _builder_7.append(")");
              result = _builder_7.toString();
            }
          } else {
            if ((this.helper.hasSystemTypeCompiler(op) && this.helper.getSystemTypeCompiler(op).compileMethodSelectionManually())) {
              IIQLSystemTypeCompiler systemTypeCompiler_3 = this.helper.getSystemTypeCompiler(op);
              StringConcatenation _builder_8 = new StringConcatenation();
              String _compile_15 = this.compile(selExpr.getLeftOperand(), c);
              _builder_8.append(_compile_15);
              _builder_8.append(".");
              String _compileMethodSelection_4 = systemTypeCompiler_3.compileMethodSelection(op, this.helper.toList(this.compile(e.getRightOperand(), c)));
              _builder_8.append(_compileMethodSelection_4);
              result = _builder_8.toString();
            } else {
              StringConcatenation _builder_9 = new StringConcatenation();
              String _compile_16 = this.compile(selExpr.getLeftOperand(), c);
              _builder_9.append(_compile_16);
              _builder_9.append(".");
              String _simpleName_8 = op.getSimpleName();
              _builder_9.append(_simpleName_8);
              _builder_9.append("(");
              String _compile_17 = this.compile(e.getRightOperand(), c);
              _builder_9.append(_compile_17);
              _builder_9.append(")");
              result = _builder_9.toString();
            }
          }
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
      TypeResult arrayType = this.exprEvaluator.eval(arrayExpr.getLeftOperand());
      String methodName = IQLOperatorOverloadingUtils.SET;
      String _xifexpression = null;
      if (((e.getOp().equals("=") && (!arrayType.isNull())) && this.typeExtensionsDictionary.hasTypeExtensions(arrayType.getRef(), methodName, this.helper.createSetterArguments(e.getRightOperand(), arrayExpr.getExpressions())))) {
        TypeResult leftType = this.exprEvaluator.eval(arrayExpr);
        TypeResult rightType = this.exprEvaluator.eval(e.getRightOperand());
        boolean _isNull = leftType.isNull();
        boolean _not = (!_isNull);
        if (_not) {
          c.setExpectedTypeRef(leftType.getRef());
        }
        String result = "";
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(arrayType.getRef(), methodName, this.helper.createSetterArguments(e.getRightOperand(), arrayExpr.getExpressions()));
        c.addImport(typeOps.getClass().getCanonicalName());
        if (((((!leftType.isNull()) && this.helper.isJvmArray(leftType.getRef())) && (!rightType.isNull())) && (!this.helper.isJvmArray(rightType.getRef())))) {
          c.addImport(IQLUtils.class.getCanonicalName());
          boolean _isPrimitiveArray = this.helper.isPrimitiveArray(leftType.getRef());
          if (_isPrimitiveArray) {
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = typeOps.getClass().getSimpleName();
            _builder.append(_simpleName);
            _builder.append(".");
            _builder.append(methodName);
            _builder.append("(");
            String _compile = this.compile(arrayExpr.getLeftOperand(), c);
            _builder.append(_compile);
            _builder.append(", ");
            String _simpleName_1 = IQLUtils.class.getSimpleName();
            _builder.append(_simpleName_1);
            _builder.append(".");
            String _arrayMethodName = this.helper.getArrayMethodName(leftType.getRef());
            _builder.append(_arrayMethodName);
            _builder.append("(");
            String _compile_1 = this.compile(e.getRightOperand(), c);
            _builder.append(_compile_1);
            _builder.append("), ");
            final Function1<IQLExpression, String> _function = (IQLExpression el) -> {
              return this.compile(el, c);
            };
            String _join = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(arrayExpr.getExpressions(), _function), ", ");
            _builder.append(_join);
            _builder.append(")");
            result = _builder.toString();
          } else {
            String clazz = this.typeCompiler.compile(this.typeUtils.createTypeRef(this.typeUtils.getInnerType(leftType.getRef(), false)), c, true);
            StringConcatenation _builder_1 = new StringConcatenation();
            String _simpleName_2 = typeOps.getClass().getSimpleName();
            _builder_1.append(_simpleName_2);
            _builder_1.append(".");
            _builder_1.append(methodName);
            _builder_1.append("(");
            String _compile_2 = this.compile(arrayExpr.getLeftOperand(), c);
            _builder_1.append(_compile_2);
            _builder_1.append(", ");
            String _simpleName_3 = IQLUtils.class.getSimpleName();
            _builder_1.append(_simpleName_3);
            _builder_1.append(".");
            String _arrayMethodName_1 = this.helper.getArrayMethodName(leftType.getRef());
            _builder_1.append(_arrayMethodName_1);
            _builder_1.append("(");
            _builder_1.append(clazz);
            _builder_1.append(".class, ");
            String _compile_3 = this.compile(e.getRightOperand(), c);
            _builder_1.append(_compile_3);
            _builder_1.append("), ");
            final Function1<IQLExpression, String> _function_1 = (IQLExpression el) -> {
              return this.compile(el, c);
            };
            String _join_1 = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(arrayExpr.getExpressions(), _function_1), ", ");
            _builder_1.append(_join_1);
            _builder_1.append(")");
            result = _builder_1.toString();
          }
        } else {
          if (((leftType.isNull() || rightType.isNull()) || this.lookUp.isAssignable(leftType.getRef(), rightType.getRef()))) {
            int _size = arrayExpr.getExpressions().size();
            boolean _equals = (_size == 1);
            if (_equals) {
              StringConcatenation _builder_2 = new StringConcatenation();
              String _simpleName_4 = typeOps.getClass().getSimpleName();
              _builder_2.append(_simpleName_4);
              _builder_2.append(".");
              _builder_2.append(methodName);
              _builder_2.append("(");
              String _compile_4 = this.compile(arrayExpr.getLeftOperand(), c);
              _builder_2.append(_compile_4);
              _builder_2.append(", ");
              String _compile_5 = this.compile(e.getRightOperand(), c);
              _builder_2.append(_compile_5);
              _builder_2.append(", ");
              String _compile_6 = this.compile(arrayExpr.getExpressions().get(0), c);
              _builder_2.append(_compile_6);
              _builder_2.append(")");
              result = _builder_2.toString();
            } else {
              StringConcatenation _builder_3 = new StringConcatenation();
              String _simpleName_5 = typeOps.getClass().getSimpleName();
              _builder_3.append(_simpleName_5);
              _builder_3.append(".");
              _builder_3.append(methodName);
              _builder_3.append("(");
              String _compile_7 = this.compile(arrayExpr.getLeftOperand(), c);
              _builder_3.append(_compile_7);
              _builder_3.append(", ");
              String _compile_8 = this.compile(e.getRightOperand(), c);
              _builder_3.append(_compile_8);
              _builder_3.append(", ");
              String _simpleName_6 = IQLUtils.class.getSimpleName();
              _builder_3.append(_simpleName_6);
              _builder_3.append(".createList(");
              final Function1<IQLExpression, String> _function_2 = (IQLExpression el) -> {
                return this.compile(el, c);
              };
              String _join_2 = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(arrayExpr.getExpressions(), _function_2), ", ");
              _builder_3.append(_join_2);
              _builder_3.append("))");
              result = _builder_3.toString();
            }
          } else {
            if (((leftType.isNull() || rightType.isNull()) || this.lookUp.isCastable(leftType.getRef(), rightType.getRef()))) {
              String target = this.typeCompiler.compile(leftType.getRef(), c, false);
              int _size_1 = arrayExpr.getExpressions().size();
              boolean _equals_1 = (_size_1 == 1);
              if (_equals_1) {
                StringConcatenation _builder_4 = new StringConcatenation();
                String _simpleName_7 = typeOps.getClass().getSimpleName();
                _builder_4.append(_simpleName_7);
                _builder_4.append(".");
                _builder_4.append(methodName);
                _builder_4.append("(");
                String _compile_9 = this.compile(arrayExpr.getLeftOperand(), c);
                _builder_4.append(_compile_9);
                _builder_4.append(", ((");
                _builder_4.append(target);
                _builder_4.append(")");
                String _compile_10 = this.compile(e.getRightOperand(), c);
                _builder_4.append(_compile_10);
                _builder_4.append("), ");
                String _compile_11 = this.compile(arrayExpr.getExpressions().get(0), c);
                _builder_4.append(_compile_11);
                _builder_4.append(")");
                result = _builder_4.toString();
              } else {
                StringConcatenation _builder_5 = new StringConcatenation();
                String _simpleName_8 = typeOps.getClass().getSimpleName();
                _builder_5.append(_simpleName_8);
                _builder_5.append(".");
                _builder_5.append(methodName);
                _builder_5.append("(");
                String _compile_12 = this.compile(arrayExpr.getLeftOperand(), c);
                _builder_5.append(_compile_12);
                _builder_5.append(", ((");
                _builder_5.append(target);
                _builder_5.append(")");
                String _compile_13 = this.compile(e.getRightOperand(), c);
                _builder_5.append(_compile_13);
                _builder_5.append("), ");
                String _simpleName_9 = IQLUtils.class.getSimpleName();
                _builder_5.append(_simpleName_9);
                _builder_5.append(".createList(");
                final Function1<IQLExpression, String> _function_3 = (IQLExpression el) -> {
                  return this.compile(el, c);
                };
                String _join_3 = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(arrayExpr.getExpressions(), _function_3), ", ");
                _builder_5.append(_join_3);
                _builder_5.append("))");
                result = _builder_5.toString();
              }
            } else {
              int _size_2 = arrayExpr.getExpressions().size();
              boolean _equals_2 = (_size_2 == 1);
              if (_equals_2) {
                StringConcatenation _builder_6 = new StringConcatenation();
                String _simpleName_10 = typeOps.getClass().getSimpleName();
                _builder_6.append(_simpleName_10);
                _builder_6.append(".");
                _builder_6.append(methodName);
                _builder_6.append("(");
                String _compile_14 = this.compile(arrayExpr.getLeftOperand(), c);
                _builder_6.append(_compile_14);
                _builder_6.append(", ");
                String _compile_15 = this.compile(e.getRightOperand(), c);
                _builder_6.append(_compile_15);
                _builder_6.append(", ");
                String _compile_16 = this.compile(arrayExpr.getExpressions().get(0), c);
                _builder_6.append(_compile_16);
                _builder_6.append(")");
                result = _builder_6.toString();
              } else {
                StringConcatenation _builder_7 = new StringConcatenation();
                String _simpleName_11 = typeOps.getClass().getSimpleName();
                _builder_7.append(_simpleName_11);
                _builder_7.append(".");
                _builder_7.append(methodName);
                _builder_7.append("(");
                String _compile_17 = this.compile(arrayExpr.getLeftOperand(), c);
                _builder_7.append(_compile_17);
                _builder_7.append(", ");
                String _compile_18 = this.compile(e.getRightOperand(), c);
                _builder_7.append(_compile_18);
                _builder_7.append(", ");
                String _simpleName_12 = IQLUtils.class.getSimpleName();
                _builder_7.append(_simpleName_12);
                _builder_7.append(".createList(");
                final Function1<IQLExpression, String> _function_4 = (IQLExpression el) -> {
                  return this.compile(el, c);
                };
                String _join_4 = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(arrayExpr.getExpressions(), _function_4), ", ");
                _builder_7.append(_join_4);
                _builder_7.append("))");
                result = _builder_7.toString();
              }
            }
          }
        }
        c.setExpectedTypeRef(null);
        return result;
      } else {
        String _xifexpression_1 = null;
        if (((e.getOp().equals("=") && (!arrayType.isNull())) && this.typeUtils.isArray(arrayType.getRef()))) {
          TypeResult leftType_1 = this.exprEvaluator.eval(arrayExpr);
          TypeResult rightType_1 = this.exprEvaluator.eval(e.getRightOperand());
          boolean _isNull_1 = leftType_1.isNull();
          boolean _not_1 = (!_isNull_1);
          if (_not_1) {
            c.setExpectedTypeRef(leftType_1.getRef());
          }
          String result_1 = "";
          if (((((!leftType_1.isNull()) && this.helper.isJvmArray(leftType_1.getRef())) && (!rightType_1.isNull())) && (!this.helper.isJvmArray(rightType_1.getRef())))) {
            c.addImport(IQLUtils.class.getCanonicalName());
            boolean _isPrimitiveArray_1 = this.helper.isPrimitiveArray(leftType_1.getRef());
            if (_isPrimitiveArray_1) {
              StringConcatenation _builder_8 = new StringConcatenation();
              String _simpleName_13 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
              _builder_8.append(_simpleName_13);
              _builder_8.append(".");
              _builder_8.append(methodName);
              _builder_8.append("(");
              String _compile_19 = this.compile(arrayExpr.getLeftOperand(), c);
              _builder_8.append(_compile_19);
              _builder_8.append(", ");
              String _simpleName_14 = IQLUtils.class.getSimpleName();
              _builder_8.append(_simpleName_14);
              _builder_8.append(".");
              String _arrayMethodName_2 = this.helper.getArrayMethodName(leftType_1.getRef());
              _builder_8.append(_arrayMethodName_2);
              _builder_8.append("(");
              String _compile_20 = this.compile(e.getRightOperand(), c);
              _builder_8.append(_compile_20);
              _builder_8.append("), ");
              final Function1<IQLExpression, String> _function_5 = (IQLExpression el) -> {
                return this.compile(el, c);
              };
              String _join_5 = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(arrayExpr.getExpressions(), _function_5), ", ");
              _builder_8.append(_join_5);
              _builder_8.append(")");
              result_1 = _builder_8.toString();
            } else {
              String clazz_1 = this.typeCompiler.compile(this.typeUtils.createTypeRef(this.typeUtils.getInnerType(leftType_1.getRef(), false)), c, true);
              StringConcatenation _builder_9 = new StringConcatenation();
              String _simpleName_15 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
              _builder_9.append(_simpleName_15);
              _builder_9.append(".");
              _builder_9.append(methodName);
              _builder_9.append("(");
              String _compile_21 = this.compile(arrayExpr.getLeftOperand(), c);
              _builder_9.append(_compile_21);
              _builder_9.append(", ");
              String _simpleName_16 = IQLUtils.class.getSimpleName();
              _builder_9.append(_simpleName_16);
              _builder_9.append(".");
              String _arrayMethodName_3 = this.helper.getArrayMethodName(leftType_1.getRef());
              _builder_9.append(_arrayMethodName_3);
              _builder_9.append("(");
              _builder_9.append(clazz_1);
              _builder_9.append(".class, ");
              String _compile_22 = this.compile(e.getRightOperand(), c);
              _builder_9.append(_compile_22);
              _builder_9.append("), ");
              final Function1<IQLExpression, String> _function_6 = (IQLExpression el) -> {
                return this.compile(el, c);
              };
              String _join_6 = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(arrayExpr.getExpressions(), _function_6), ", ");
              _builder_9.append(_join_6);
              _builder_9.append(")");
              result_1 = _builder_9.toString();
            }
          } else {
            if (((leftType_1.isNull() || rightType_1.isNull()) || this.lookUp.isAssignable(leftType_1.getRef(), rightType_1.getRef()))) {
              c.addImport(de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getCanonicalName());
              StringConcatenation _builder_10 = new StringConcatenation();
              String _simpleName_17 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
              _builder_10.append(_simpleName_17);
              _builder_10.append(".");
              _builder_10.append(methodName);
              _builder_10.append("(");
              String _compile_23 = this.compile(arrayExpr.getLeftOperand(), c);
              _builder_10.append(_compile_23);
              _builder_10.append(", ");
              String _compile_24 = this.compile(e.getRightOperand(), c);
              _builder_10.append(_compile_24);
              _builder_10.append(", ");
              final Function1<IQLExpression, String> _function_7 = (IQLExpression el) -> {
                return this.compile(el, c);
              };
              String _join_7 = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(arrayExpr.getExpressions(), _function_7), ", ");
              _builder_10.append(_join_7);
              _builder_10.append(")");
              result_1 = _builder_10.toString();
            } else {
              if (((leftType_1.isNull() || rightType_1.isNull()) || this.lookUp.isCastable(leftType_1.getRef(), rightType_1.getRef()))) {
                c.addImport(de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getCanonicalName());
                String target_1 = this.typeCompiler.compile(leftType_1.getRef(), c, false);
                StringConcatenation _builder_11 = new StringConcatenation();
                String _simpleName_18 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
                _builder_11.append(_simpleName_18);
                _builder_11.append(".");
                _builder_11.append(methodName);
                _builder_11.append("(");
                String _compile_25 = this.compile(arrayExpr.getLeftOperand(), c);
                _builder_11.append(_compile_25);
                _builder_11.append(", ((");
                _builder_11.append(target_1);
                _builder_11.append(")");
                String _compile_26 = this.compile(e.getRightOperand(), c);
                _builder_11.append(_compile_26);
                _builder_11.append("), ");
                final Function1<IQLExpression, String> _function_8 = (IQLExpression el) -> {
                  return this.compile(el, c);
                };
                String _join_8 = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(arrayExpr.getExpressions(), _function_8), ", ");
                _builder_11.append(_join_8);
                _builder_11.append(")");
                result_1 = _builder_11.toString();
              } else {
                c.addImport(de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getCanonicalName());
                StringConcatenation _builder_12 = new StringConcatenation();
                String _simpleName_19 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
                _builder_12.append(_simpleName_19);
                _builder_12.append(".");
                _builder_12.append(methodName);
                _builder_12.append("(");
                String _compile_27 = this.compile(arrayExpr.getLeftOperand(), c);
                _builder_12.append(_compile_27);
                _builder_12.append(", ");
                String _compile_28 = this.compile(e.getRightOperand(), c);
                _builder_12.append(_compile_28);
                _builder_12.append(", ");
                final Function1<IQLExpression, String> _function_9 = (IQLExpression el) -> {
                  return this.compile(el, c);
                };
                String _join_9 = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(arrayExpr.getExpressions(), _function_9), ", ");
                _builder_12.append(_join_9);
                _builder_12.append(")");
                result_1 = _builder_12.toString();
              }
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
    TypeResult leftType = this.exprEvaluator.eval(e.getLeftOperand());
    boolean _equals = e.getOp().equals("=");
    if (_equals) {
      TypeResult rightType = this.exprEvaluator.eval(e.getRightOperand());
      boolean _isNull = leftType.isNull();
      boolean _not = (!_isNull);
      if (_not) {
        c.setExpectedTypeRef(leftType.getRef());
      }
      String result = "";
      if (((((!leftType.isNull()) && this.helper.isJvmArray(leftType.getRef())) && (!rightType.isNull())) && (!this.helper.isJvmArray(rightType.getRef())))) {
        c.addImport(IQLUtils.class.getCanonicalName());
        boolean _isPrimitiveArray = this.helper.isPrimitiveArray(leftType.getRef());
        if (_isPrimitiveArray) {
          StringConcatenation _builder = new StringConcatenation();
          String _compile = this.compile(e.getLeftOperand(), c);
          _builder.append(_compile);
          _builder.append(" ");
          String _op = e.getOp();
          _builder.append(_op);
          _builder.append(" ");
          String _simpleName = IQLUtils.class.getSimpleName();
          _builder.append(_simpleName);
          _builder.append(".");
          String _arrayMethodName = this.helper.getArrayMethodName(leftType.getRef());
          _builder.append(_arrayMethodName);
          _builder.append("(");
          String _compile_1 = this.compile(e.getRightOperand(), c);
          _builder.append(_compile_1);
          _builder.append("))");
          result = _builder.toString();
        } else {
          String clazz = this.typeCompiler.compile(this.typeUtils.createTypeRef(this.typeUtils.getInnerType(leftType.getRef(), false)), c, true);
          StringConcatenation _builder_1 = new StringConcatenation();
          String _compile_2 = this.compile(e.getLeftOperand(), c);
          _builder_1.append(_compile_2);
          _builder_1.append(" ");
          String _op_1 = e.getOp();
          _builder_1.append(_op_1);
          _builder_1.append(" ");
          String _simpleName_1 = IQLUtils.class.getSimpleName();
          _builder_1.append(_simpleName_1);
          _builder_1.append(".");
          String _arrayMethodName_1 = this.helper.getArrayMethodName(leftType.getRef());
          _builder_1.append(_arrayMethodName_1);
          _builder_1.append("(");
          _builder_1.append(clazz);
          _builder_1.append(".class, ");
          String _compile_3 = this.compile(e.getRightOperand(), c);
          _builder_1.append(_compile_3);
          _builder_1.append("))");
          result = _builder_1.toString();
        }
      } else {
        if (((leftType.isNull() || rightType.isNull()) || this.lookUp.isAssignable(leftType.getRef(), rightType.getRef()))) {
          StringConcatenation _builder_2 = new StringConcatenation();
          String _compile_4 = this.compile(e.getLeftOperand(), c);
          _builder_2.append(_compile_4);
          _builder_2.append(" ");
          String _op_2 = e.getOp();
          _builder_2.append(_op_2);
          _builder_2.append(" ");
          String _compile_5 = this.compile(e.getRightOperand(), c);
          _builder_2.append(_compile_5);
          result = _builder_2.toString();
        } else {
          if (((leftType.isNull() || rightType.isNull()) || this.lookUp.isCastable(leftType.getRef(), rightType.getRef()))) {
            String target = this.typeCompiler.compile(leftType.getRef(), c, false);
            StringConcatenation _builder_3 = new StringConcatenation();
            String _compile_6 = this.compile(e.getLeftOperand(), c);
            _builder_3.append(_compile_6);
            _builder_3.append(" ");
            String _op_3 = e.getOp();
            _builder_3.append(_op_3);
            _builder_3.append(" ((");
            _builder_3.append(target);
            _builder_3.append(") ");
            String _compile_7 = this.compile(e.getRightOperand(), c);
            _builder_3.append(_compile_7);
            _builder_3.append(")");
            result = _builder_3.toString();
          } else {
            StringConcatenation _builder_4 = new StringConcatenation();
            String _compile_8 = this.compile(e.getLeftOperand(), c);
            _builder_4.append(_compile_8);
            _builder_4.append(" ");
            String _op_4 = e.getOp();
            _builder_4.append(_op_4);
            _builder_4.append(" ");
            String _compile_9 = this.compile(e.getRightOperand(), c);
            _builder_4.append(_compile_9);
            result = _builder_4.toString();
          }
        }
      }
      c.setExpectedTypeRef(null);
      return result;
    } else {
      if (((!leftType.isNull()) && e.getOp().equals("+="))) {
        return this.compileOperatorOverloading("+", IQLOperatorOverloadingUtils.PLUS, leftType.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
      } else {
        if (((!leftType.isNull()) && e.getOp().equals("-="))) {
          return this.compileOperatorOverloading("-", IQLOperatorOverloadingUtils.MINUS, leftType.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
        } else {
          if (((!leftType.isNull()) && e.getOp().equals("*="))) {
            return this.compileOperatorOverloading("*", IQLOperatorOverloadingUtils.MULTIPLY, leftType.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
          } else {
            if (((!leftType.isNull()) && e.getOp().equals("/="))) {
              return this.compileOperatorOverloading("/", IQLOperatorOverloadingUtils.DIVIDE, leftType.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
            } else {
              if (((!leftType.isNull()) && e.getOp().equals("%="))) {
                return this.compileOperatorOverloading("%", IQLOperatorOverloadingUtils.MODULO, leftType.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
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
    TypeResult left = this.exprEvaluator.eval(e.getLeftOperand());
    String result = "";
    if (((!left.isNull()) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.LOGICAL_OR, e.getRightOperand()))) {
      result = this.compileOperatorOverloading(e.getOp(), IQLOperatorOverloadingUtils.LOGICAL_OR, left.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
    } else {
      StringConcatenation _builder = new StringConcatenation();
      String _compile = this.compile(e.getLeftOperand(), c);
      _builder.append(_compile);
      _builder.append(" ");
      String _op = e.getOp();
      _builder.append(_op);
      _builder.append(" ");
      String _compile_1 = this.compile(e.getRightOperand(), c);
      _builder.append(_compile_1);
      result = _builder.toString();
    }
    return result;
  }
  
  public String compile(final IQLLogicalAndExpression e, final G c) {
    TypeResult left = this.exprEvaluator.eval(e.getLeftOperand());
    String result = "";
    if (((!left.isNull()) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.LOGICAL_AND, e.getRightOperand()))) {
      result = this.compileOperatorOverloading(e.getOp(), IQLOperatorOverloadingUtils.LOGICAL_AND, left.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
    } else {
      StringConcatenation _builder = new StringConcatenation();
      String _compile = this.compile(e.getLeftOperand(), c);
      _builder.append(_compile);
      _builder.append(" ");
      String _op = e.getOp();
      _builder.append(_op);
      _builder.append(" ");
      String _compile_1 = this.compile(e.getRightOperand(), c);
      _builder.append(_compile_1);
      result = _builder.toString();
    }
    return result;
  }
  
  public String compile(final IQLEqualityExpression e, final G c) {
    TypeResult left = this.exprEvaluator.eval(e.getLeftOperand());
    String result = "";
    if ((((!left.isNull()) && e.getOp().equals("==")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.EQUALS, e.getRightOperand()))) {
      result = this.compileOperatorOverloading("==", IQLOperatorOverloadingUtils.EQUALS, left.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
    } else {
      if ((((!left.isNull()) && e.getOp().equals("!==")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.EQUALS_NOT, e.getRightOperand()))) {
        result = this.compileOperatorOverloading("!==", IQLOperatorOverloadingUtils.EQUALS_NOT, left.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
      } else {
        boolean _isNull = left.isNull();
        boolean _not = (!_isNull);
        if (_not) {
          c.setExpectedTypeRef(left.getRef());
        }
        StringConcatenation _builder = new StringConcatenation();
        String _compile = this.compile(e.getLeftOperand(), c);
        _builder.append(_compile);
        _builder.append(" ");
        String _op = e.getOp();
        _builder.append(_op);
        _builder.append(" ");
        String _compile_1 = this.compile(e.getRightOperand(), c);
        _builder.append(_compile_1);
        result = _builder.toString();
      }
    }
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLRelationalExpression e, final G c) {
    TypeResult left = this.exprEvaluator.eval(e.getLeftOperand());
    String result = "";
    if ((((!left.isNull()) && e.getOp().equals(">")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.GREATER_THAN, e.getRightOperand()))) {
      result = this.compileOperatorOverloading(">", IQLOperatorOverloadingUtils.GREATER_THAN, left.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
    } else {
      if ((((!left.isNull()) && e.getOp().equals("<")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.LESS_THAN, e.getRightOperand()))) {
        result = this.compileOperatorOverloading("<", IQLOperatorOverloadingUtils.LESS_THAN, left.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
      } else {
        if ((((!left.isNull()) && e.getOp().equals(">=")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.GREATER_EQUALS_THAN, e.getRightOperand()))) {
          result = this.compileOperatorOverloading(">=", IQLOperatorOverloadingUtils.GREATER_EQUALS_THAN, left.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
        } else {
          if ((((!left.isNull()) && e.getOp().equals("<=")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.LESS_EQUALS_THAN, e.getRightOperand()))) {
            result = this.compileOperatorOverloading("<=", IQLOperatorOverloadingUtils.LESS_EQUALS_THAN, left.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
          } else {
            boolean _isNull = left.isNull();
            boolean _not = (!_isNull);
            if (_not) {
              c.setExpectedTypeRef(left.getRef());
            }
            StringConcatenation _builder = new StringConcatenation();
            String _compile = this.compile(e.getLeftOperand(), c);
            _builder.append(_compile);
            _builder.append(" ");
            String _op = e.getOp();
            _builder.append(_op);
            _builder.append(" ");
            String _compile_1 = this.compile(e.getRightOperand(), c);
            _builder.append(_compile_1);
            result = _builder.toString();
          }
        }
      }
    }
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLInstanceOfExpression e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    String _compile = this.compile(e.getLeftOperand(), c);
    _builder.append(_compile);
    _builder.append(" instanceof ");
    String _compile_1 = this.typeCompiler.compile(e.getTargetRef(), c, true);
    _builder.append(_compile_1);
    return _builder.toString();
  }
  
  public String compileOperatorOverloading(final String operator, final String operatorName, final JvmTypeReference left, final IQLExpression leftOperand, final IQLExpression rightOperand, final G c) {
    TypeResult rightType = this.exprEvaluator.eval(rightOperand);
    IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(left, operatorName, rightOperand);
    c.addImport(typeOps.getClass().getCanonicalName());
    JvmTypeReference targetType = this.typeExtensionsDictionary.getMethod(left, operatorName, rightOperand).getParameters().get(0).getParameterType();
    c.setExpectedTypeRef(targetType);
    String result = "";
    if (((((targetType != null) && this.helper.isJvmArray(targetType)) && (!rightType.isNull())) && (!this.helper.isJvmArray(rightType.getRef())))) {
      c.addImport(IQLUtils.class.getCanonicalName());
      boolean _isPrimitiveArray = this.helper.isPrimitiveArray(targetType);
      if (_isPrimitiveArray) {
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = typeOps.getClass().getSimpleName();
        _builder.append(_simpleName);
        _builder.append(".");
        _builder.append(operatorName);
        _builder.append("(");
        String _compile = this.compile(leftOperand, c);
        _builder.append(_compile);
        _builder.append(", ");
        String _simpleName_1 = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName_1);
        _builder.append(".");
        String _arrayMethodName = this.helper.getArrayMethodName(targetType);
        _builder.append(_arrayMethodName);
        _builder.append("(");
        String _compile_1 = this.compile(rightOperand, c);
        _builder.append(_compile_1);
        _builder.append("))");
        result = _builder.toString();
      } else {
        String clazz = this.typeCompiler.compile(this.typeUtils.createTypeRef(this.typeUtils.getInnerType(targetType, false)), c, true);
        StringConcatenation _builder_1 = new StringConcatenation();
        String _simpleName_2 = typeOps.getClass().getSimpleName();
        _builder_1.append(_simpleName_2);
        _builder_1.append(".");
        _builder_1.append(operatorName);
        _builder_1.append("(");
        String _compile_2 = this.compile(leftOperand, c);
        _builder_1.append(_compile_2);
        _builder_1.append(", ");
        String _simpleName_3 = IQLUtils.class.getSimpleName();
        _builder_1.append(_simpleName_3);
        _builder_1.append(".");
        String _arrayMethodName_1 = this.helper.getArrayMethodName(targetType);
        _builder_1.append(_arrayMethodName_1);
        _builder_1.append("(");
        _builder_1.append(clazz);
        _builder_1.append(".class, ");
        String _compile_3 = this.compile(rightOperand, c);
        _builder_1.append(_compile_3);
        _builder_1.append("))");
        result = _builder_1.toString();
      }
    } else {
      if (((!rightType.isNull()) && this.lookUp.isAssignable(targetType, rightType.getRef()))) {
        StringConcatenation _builder_2 = new StringConcatenation();
        String _simpleName_4 = typeOps.getClass().getSimpleName();
        _builder_2.append(_simpleName_4);
        _builder_2.append(".");
        _builder_2.append(operatorName);
        _builder_2.append("(");
        String _compile_4 = this.compile(leftOperand, c);
        _builder_2.append(_compile_4);
        _builder_2.append(", ");
        String _compile_5 = this.compile(rightOperand, c);
        _builder_2.append(_compile_5);
        _builder_2.append(")");
        result = _builder_2.toString();
      } else {
        if ((rightType.isNull() || this.lookUp.isCastable(targetType, rightType.getRef()))) {
          String target = this.typeCompiler.compile(targetType, c, false);
          StringConcatenation _builder_3 = new StringConcatenation();
          String _simpleName_5 = typeOps.getClass().getSimpleName();
          _builder_3.append(_simpleName_5);
          _builder_3.append(".");
          _builder_3.append(operatorName);
          _builder_3.append("(");
          String _compile_6 = this.compile(leftOperand, c);
          _builder_3.append(_compile_6);
          _builder_3.append(", ((");
          _builder_3.append(target);
          _builder_3.append(")");
          String _compile_7 = this.compile(rightOperand, c);
          _builder_3.append(_compile_7);
          _builder_3.append("))");
          result = _builder_3.toString();
        } else {
          StringConcatenation _builder_4 = new StringConcatenation();
          String _simpleName_6 = typeOps.getClass().getSimpleName();
          _builder_4.append(_simpleName_6);
          _builder_4.append(".");
          _builder_4.append(operatorName);
          _builder_4.append("(");
          String _compile_8 = this.compile(leftOperand, c);
          _builder_4.append(_compile_8);
          _builder_4.append(", ");
          String _compile_9 = this.compile(rightOperand, c);
          _builder_4.append(_compile_9);
          _builder_4.append(")");
          result = _builder_4.toString();
        }
      }
    }
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLAdditiveExpression e, final G c) {
    TypeResult left = this.exprEvaluator.eval(e.getLeftOperand());
    String result = "";
    if ((((!left.isNull()) && e.getOp().equals("+")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.PLUS, e.getRightOperand()))) {
      result = this.compileOperatorOverloading("+", IQLOperatorOverloadingUtils.PLUS, left.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
    } else {
      if ((((!left.isNull()) && e.getOp().equals("-")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.MINUS, e.getRightOperand()))) {
        result = this.compileOperatorOverloading("-", IQLOperatorOverloadingUtils.MINUS, left.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
      } else {
        boolean _isNull = left.isNull();
        boolean _not = (!_isNull);
        if (_not) {
          c.setExpectedTypeRef(left.getRef());
        }
        StringConcatenation _builder = new StringConcatenation();
        String _compile = this.compile(e.getLeftOperand(), c);
        _builder.append(_compile);
        _builder.append(" ");
        String _op = e.getOp();
        _builder.append(_op);
        _builder.append(" ");
        String _compile_1 = this.compile(e.getRightOperand(), c);
        _builder.append(_compile_1);
        result = _builder.toString();
      }
    }
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLMultiplicativeExpression e, final G c) {
    TypeResult left = this.exprEvaluator.eval(e.getLeftOperand());
    String result = "";
    if ((((!left.isNull()) && e.getOp().equals("*")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.MULTIPLY, e.getRightOperand()))) {
      result = this.compileOperatorOverloading("*", IQLOperatorOverloadingUtils.MULTIPLY, left.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
    } else {
      if ((((!left.isNull()) && e.getOp().equals("/")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.DIVIDE, e.getRightOperand()))) {
        result = this.compileOperatorOverloading("/", IQLOperatorOverloadingUtils.DIVIDE, left.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
      } else {
        if ((((!left.isNull()) && e.getOp().equals("%")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.MODULO, e.getRightOperand()))) {
          result = this.compileOperatorOverloading("%", IQLOperatorOverloadingUtils.MODULO, left.getRef(), e.getLeftOperand(), e.getRightOperand(), c);
        } else {
          boolean _isNull = left.isNull();
          boolean _not = (!_isNull);
          if (_not) {
            c.setExpectedTypeRef(left.getRef());
          }
          StringConcatenation _builder = new StringConcatenation();
          String _compile = this.compile(e.getLeftOperand(), c);
          _builder.append(_compile);
          _builder.append(" ");
          String _op = e.getOp();
          _builder.append(_op);
          _builder.append(" ");
          String _compile_1 = this.compile(e.getRightOperand(), c);
          _builder.append(_compile_1);
          result = _builder.toString();
        }
      }
    }
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLPlusMinusExpression e, final G c) {
    String _xblockexpression = null;
    {
      TypeResult left = this.exprEvaluator.eval(e.getOperand());
      String _xifexpression = null;
      if ((((!left.isNull()) && e.getOp().equals("+")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.SIMPLE_PLUS_PREFIX, new ArrayList<IQLExpression>()))) {
        String _xblockexpression_1 = null;
        {
          String methodName = IQLOperatorOverloadingUtils.SIMPLE_PLUS_PREFIX;
          JvmTypeReference _ref = left.getRef();
          ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_ref, methodName, _arrayList);
          c.addImport(typeOps.getClass().getCanonicalName());
          StringConcatenation _builder = new StringConcatenation();
          String _simpleName = typeOps.getClass().getSimpleName();
          _builder.append(_simpleName);
          _builder.append(".");
          _builder.append(methodName);
          _builder.append("(");
          String _compile = this.compile(e.getOperand(), c);
          _builder.append(_compile);
          _builder.append(")");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        if ((((!left.isNull()) && e.getOp().equals("-")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.SIMPLE_MINUS_PREFIX, new ArrayList<IQLExpression>()))) {
          String _xblockexpression_2 = null;
          {
            String methodName = IQLOperatorOverloadingUtils.SIMPLE_MINUS_PREFIX;
            JvmTypeReference _ref = left.getRef();
            ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
            IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_ref, methodName, _arrayList);
            c.addImport(typeOps.getClass().getCanonicalName());
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = typeOps.getClass().getSimpleName();
            _builder.append(_simpleName);
            _builder.append(".");
            _builder.append(methodName);
            _builder.append("(");
            String _compile = this.compile(e.getOperand(), c);
            _builder.append(_compile);
            _builder.append(")");
            _xblockexpression_2 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          String _op = e.getOp();
          _builder.append(_op);
          String _compile = this.compile(e.getOperand(), c);
          _builder.append(_compile);
          _xifexpression_1 = _builder.toString();
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLBooleanNotExpression e, final G c) {
    String _xblockexpression = null;
    {
      TypeResult left = this.exprEvaluator.eval(e.getOperand());
      String methodName = IQLOperatorOverloadingUtils.BOOLEAN_NOT_PREFIX;
      String _xifexpression = null;
      if (((!left.isNull()) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), methodName, new ArrayList<IQLExpression>()))) {
        String _xblockexpression_1 = null;
        {
          JvmTypeReference _ref = left.getRef();
          ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_ref, methodName, _arrayList);
          c.addImport(typeOps.getClass().getCanonicalName());
          StringConcatenation _builder = new StringConcatenation();
          String _simpleName = typeOps.getClass().getSimpleName();
          _builder.append(_simpleName);
          _builder.append(".");
          _builder.append(methodName);
          _builder.append("(");
          String _compile = this.compile(e.getOperand(), c);
          _builder.append(_compile);
          _builder.append(")");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        StringConcatenation _builder = new StringConcatenation();
        String _op = e.getOp();
        _builder.append(_op);
        String _compile = this.compile(e.getOperand(), c);
        _builder.append(_compile);
        _xifexpression = _builder.toString();
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLPrefixExpression e, final G c) {
    String _xblockexpression = null;
    {
      TypeResult left = this.exprEvaluator.eval(e.getOperand());
      String _xifexpression = null;
      if ((((!left.isNull()) && e.getOp().equals("++")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.PLUS_PREFIX, new ArrayList<IQLExpression>()))) {
        String _xblockexpression_1 = null;
        {
          String methodName = IQLOperatorOverloadingUtils.PLUS_PREFIX;
          JvmTypeReference _ref = left.getRef();
          ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_ref, methodName, _arrayList);
          c.addImport(typeOps.getClass().getCanonicalName());
          StringConcatenation _builder = new StringConcatenation();
          String _simpleName = typeOps.getClass().getSimpleName();
          _builder.append(_simpleName);
          _builder.append(".");
          _builder.append(methodName);
          _builder.append("(");
          String _compile = this.compile(e.getOperand(), c);
          _builder.append(_compile);
          _builder.append(")");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        if ((((!left.isNull()) && e.getOp().equals("--")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.MINUS_PREFIX, new ArrayList<IQLExpression>()))) {
          String _xblockexpression_2 = null;
          {
            String methodName = IQLOperatorOverloadingUtils.MINUS_PREFIX;
            JvmTypeReference _ref = left.getRef();
            ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
            IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_ref, methodName, _arrayList);
            c.addImport(typeOps.getClass().getCanonicalName());
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = typeOps.getClass().getSimpleName();
            _builder.append(_simpleName);
            _builder.append(".");
            _builder.append(methodName);
            _builder.append("(");
            String _compile = this.compile(e.getOperand(), c);
            _builder.append(_compile);
            _builder.append(")");
            _xblockexpression_2 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          String _op = e.getOp();
          _builder.append(_op);
          String _compile = this.compile(e.getOperand(), c);
          _builder.append(_compile);
          _xifexpression_1 = _builder.toString();
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLTypeCastExpression e, final G c) {
    c.setExpectedTypeRef(e.getTargetRef());
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    String _compile = this.typeCompiler.compile(e.getTargetRef(), c, false);
    _builder.append(_compile);
    _builder.append(")(");
    String _compile_1 = this.compile(e.getOperand(), c);
    _builder.append(_compile_1);
    _builder.append(")");
    String result = _builder.toString();
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLPostfixExpression e, final G c) {
    String _xblockexpression = null;
    {
      TypeResult right = this.exprEvaluator.eval(e.getOperand());
      String _xifexpression = null;
      if ((((!right.isNull()) && e.getOp().equals("++")) && this.typeExtensionsDictionary.hasTypeExtensions(right.getRef(), IQLOperatorOverloadingUtils.PLUS_POSTFIX, new ArrayList<IQLExpression>()))) {
        String _xblockexpression_1 = null;
        {
          String methodName = IQLOperatorOverloadingUtils.PLUS_POSTFIX;
          JvmTypeReference _ref = right.getRef();
          ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_ref, methodName, _arrayList);
          c.addImport(typeOps.getClass().getCanonicalName());
          StringConcatenation _builder = new StringConcatenation();
          String _simpleName = typeOps.getClass().getSimpleName();
          _builder.append(_simpleName);
          _builder.append(".");
          _builder.append(methodName);
          _builder.append("(");
          String _compile = this.compile(e.getOperand(), c);
          _builder.append(_compile);
          _builder.append(")");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        if ((((!right.isNull()) && e.getOp().equals("--")) && this.typeExtensionsDictionary.hasTypeExtensions(right.getRef(), IQLOperatorOverloadingUtils.MINUS_POSTFIX, new ArrayList<IQLExpression>()))) {
          String _xblockexpression_2 = null;
          {
            String methodName = IQLOperatorOverloadingUtils.MINUS_POSTFIX;
            JvmTypeReference _ref = right.getRef();
            ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
            IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_ref, methodName, _arrayList);
            c.addImport(typeOps.getClass().getCanonicalName());
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = typeOps.getClass().getSimpleName();
            _builder.append(_simpleName);
            _builder.append(".");
            _builder.append(methodName);
            _builder.append("(");
            String _compile = this.compile(e.getOperand(), c);
            _builder.append(_compile);
            _builder.append(")");
            _xblockexpression_2 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          String _compile = this.compile(e.getOperand(), c);
          _builder.append(_compile);
          String _op = e.getOp();
          _builder.append(_op);
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
      TypeResult left = this.exprEvaluator.eval(e.getLeftOperand());
      String methodName = IQLOperatorOverloadingUtils.GET;
      String _xifexpression = null;
      if (((!left.isNull()) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), methodName, this.helper.createGetterArguments(e.getExpressions())))) {
        String _xblockexpression_1 = null;
        {
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(left.getRef(), methodName, this.helper.createGetterArguments(e.getExpressions()));
          c.addImport(typeOps.getClass().getCanonicalName());
          String _xifexpression_1 = null;
          int _size = e.getExpressions().size();
          boolean _equals = (_size == 1);
          if (_equals) {
            String _xblockexpression_2 = null;
            {
              c.setExpectedTypeRef(null);
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName = typeOps.getClass().getSimpleName();
              _builder.append(_simpleName);
              _builder.append(".");
              _builder.append(methodName);
              _builder.append("(");
              String _compile = this.compile(e.getLeftOperand(), c);
              _builder.append(_compile);
              _builder.append(", ");
              String _compile_1 = this.compile(e.getExpressions().get(0), c);
              _builder.append(_compile_1);
              _builder.append(")");
              _xblockexpression_2 = _builder.toString();
            }
            _xifexpression_1 = _xblockexpression_2;
          } else {
            String _xblockexpression_3 = null;
            {
              c.addImport(IQLUtils.class.getCanonicalName());
              c.setExpectedTypeRef(null);
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName = typeOps.getClass().getSimpleName();
              _builder.append(_simpleName);
              _builder.append(".");
              _builder.append(methodName);
              _builder.append("(");
              String _compile = this.compile(e.getLeftOperand(), c);
              _builder.append(_compile);
              _builder.append(", ");
              String _simpleName_1 = IQLUtils.class.getSimpleName();
              _builder.append(_simpleName_1);
              _builder.append(".createList(");
              final Function1<IQLExpression, String> _function = (IQLExpression el) -> {
                return this.compile(el, c);
              };
              String _join = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(e.getExpressions(), _function), ", ");
              _builder.append(_join);
              _builder.append("))");
              _xblockexpression_3 = _builder.toString();
            }
            _xifexpression_1 = _xblockexpression_3;
          }
          _xblockexpression_1 = _xifexpression_1;
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        if (((!left.isNull()) && this.typeUtils.isArray(left.getRef()))) {
          String _xblockexpression_2 = null;
          {
            c.addImport(de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getCanonicalName());
            String _xifexpression_2 = null;
            int _size = e.getExpressions().size();
            boolean _equals = (_size == 1);
            if (_equals) {
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
              _builder.append(_simpleName);
              _builder.append(".");
              _builder.append(methodName);
              _builder.append("(");
              String _compile = this.compile(e.getLeftOperand(), c);
              _builder.append(_compile);
              _builder.append(", ");
              String _compile_1 = this.compile(e.getExpressions().get(0), c);
              _builder.append(_compile_1);
              _builder.append(")");
              _xifexpression_2 = _builder.toString();
            } else {
              String _xblockexpression_3 = null;
              {
                c.addImport(IQLUtils.class.getCanonicalName());
                StringConcatenation _builder_1 = new StringConcatenation();
                String _simpleName_1 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
                _builder_1.append(_simpleName_1);
                _builder_1.append(".");
                _builder_1.append(methodName);
                _builder_1.append("(");
                String _compile_2 = this.compile(e.getLeftOperand(), c);
                _builder_1.append(_compile_2);
                _builder_1.append(", ");
                String _simpleName_2 = IQLUtils.class.getSimpleName();
                _builder_1.append(_simpleName_2);
                _builder_1.append(".createList(");
                final Function1<IQLExpression, String> _function = (IQLExpression el) -> {
                  return this.compile(el, c);
                };
                String _join = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(e.getExpressions(), _function), ", ");
                _builder_1.append(_join);
                _builder_1.append("))");
                _xblockexpression_3 = _builder_1.toString();
              }
              _xifexpression_2 = _xblockexpression_3;
            }
            _xblockexpression_2 = _xifexpression_2;
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          String _compile = this.compile(e.getLeftOperand(), c);
          _builder.append(_compile);
          _builder.append("[");
          String _compile_1 = this.compile(e.getExpressions().get(0), c);
          _builder.append(_compile_1);
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
      JvmTypeReference left = this.exprEvaluator.eval(e.getLeftOperand()).getRef();
      String _xifexpression = null;
      JvmMember _member = e.getSel().getMember();
      if ((_member instanceof JvmField)) {
        StringConcatenation _builder = new StringConcatenation();
        JvmMember _member_1 = e.getSel().getMember();
        String _compile = this.compile(e, left, ((JvmField) _member_1), c);
        _builder.append(_compile);
        _xifexpression = _builder.toString();
      } else {
        String _xifexpression_1 = null;
        JvmMember _member_2 = e.getSel().getMember();
        if ((_member_2 instanceof JvmOperation)) {
          StringConcatenation _builder_1 = new StringConcatenation();
          JvmMember _member_3 = e.getSel().getMember();
          String _compile_1 = this.compile(e, left, ((JvmOperation) _member_3), c);
          _builder_1.append(_compile_1);
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
    boolean _hasTypeExtensions = this.typeExtensionsDictionary.hasTypeExtensions(left, field.getSimpleName());
    if (_hasTypeExtensions) {
      String _xblockexpression = null;
      {
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(left, field.getSimpleName());
        c.addImport(typeOps.getClass().getCanonicalName());
        String _xifexpression_1 = null;
        if ((this.helper.isJvmArray(field.getType()) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
          String _xblockexpression_1 = null;
          {
            c.addImport(IQLUtils.class.getCanonicalName());
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = IQLUtils.class.getSimpleName();
            _builder.append(_simpleName);
            _builder.append(".toList(");
            String _simpleName_1 = typeOps.getClass().getSimpleName();
            _builder.append(_simpleName_1);
            _builder.append(".");
            String _simpleName_2 = field.getSimpleName();
            _builder.append(_simpleName_2);
            _builder.append(")");
            _xblockexpression_1 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_1;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          String _simpleName = typeOps.getClass().getSimpleName();
          _builder.append(_simpleName);
          _builder.append(".");
          String _simpleName_1 = field.getSimpleName();
          _builder.append(_simpleName_1);
          _xifexpression_1 = _builder.toString();
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    } else {
      String _xifexpression_1 = null;
      if ((this.helper.hasSystemTypeCompiler(field) && this.helper.getSystemTypeCompiler(field).compileFieldSelectionManually())) {
        String _xblockexpression_1 = null;
        {
          IIQLSystemTypeCompiler systemTypeCompiler = this.helper.getSystemTypeCompiler(field);
          String _xifexpression_2 = null;
          if ((this.helper.isJvmArray(field.getType()) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
            String _xblockexpression_2 = null;
            {
              c.addImport(IQLUtils.class.getCanonicalName());
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName = IQLUtils.class.getSimpleName();
              _builder.append(_simpleName);
              _builder.append(".toList(");
              String _compile = this.compile(e.getLeftOperand(), c);
              _builder.append(_compile);
              _builder.append(".");
              String _compileFieldSelection = systemTypeCompiler.compileFieldSelection(field);
              _builder.append(_compileFieldSelection);
              _builder.append(")");
              _xblockexpression_2 = _builder.toString();
            }
            _xifexpression_2 = _xblockexpression_2;
          } else {
            StringConcatenation _builder = new StringConcatenation();
            String _compile = this.compile(e.getLeftOperand(), c);
            _builder.append(_compile);
            _builder.append(".");
            String _compileFieldSelection = systemTypeCompiler.compileFieldSelection(field);
            _builder.append(_compileFieldSelection);
            _xifexpression_2 = _builder.toString();
          }
          _xblockexpression_1 = _xifexpression_2;
        }
        _xifexpression_1 = _xblockexpression_1;
      } else {
        String _xifexpression_2 = null;
        if ((this.helper.isJvmArray(field.getType()) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
          String _xblockexpression_2 = null;
          {
            c.addImport(IQLUtils.class.getCanonicalName());
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = IQLUtils.class.getSimpleName();
            _builder.append(_simpleName);
            _builder.append(".toList(");
            String _compile = this.compile(e.getLeftOperand(), c);
            _builder.append(_compile);
            _builder.append(".");
            String _simpleName_1 = field.getSimpleName();
            _builder.append(_simpleName_1);
            _builder.append(")");
            _xblockexpression_2 = _builder.toString();
          }
          _xifexpression_2 = _xblockexpression_2;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          String _compile = this.compile(e.getLeftOperand(), c);
          _builder.append(_compile);
          _builder.append(".");
          String _simpleName = field.getSimpleName();
          _builder.append(_simpleName);
          _xifexpression_2 = _builder.toString();
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLMemberSelectionExpression e, final JvmTypeReference left, final JvmOperation method, final G c) {
    String _xblockexpression = null;
    {
      c.addExceptions(method.getExceptions());
      List<IQLExpression> list = null;
      IQLArgumentsList _args = e.getSel().getArgs();
      boolean _tripleNotEquals = (_args != null);
      if (_tripleNotEquals) {
        list = e.getSel().getArgs().getElements();
      } else {
        ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
        list = _arrayList;
      }
      String _xifexpression = null;
      boolean _hasTypeExtensions = this.typeExtensionsDictionary.hasTypeExtensions(left, method.getSimpleName(), list);
      if (_hasTypeExtensions) {
        String _xblockexpression_1 = null;
        {
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(left, method.getSimpleName(), list);
          c.addImport(typeOps.getClass().getCanonicalName());
          String _xifexpression_1 = null;
          if ((((method.getReturnType() != null) && this.helper.isJvmArray(method.getReturnType())) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
            String _xblockexpression_2 = null;
            {
              c.addImport(IQLUtils.class.getCanonicalName());
              String _xifexpression_2 = null;
              boolean _ignoreFirstParameter = this.typeExtensionsDictionary.ignoreFirstParameter(method);
              if (_ignoreFirstParameter) {
                StringConcatenation _builder = new StringConcatenation();
                String _simpleName = IQLUtils.class.getSimpleName();
                _builder.append(_simpleName);
                _builder.append(".toList(");
                String _simpleName_1 = typeOps.getClass().getSimpleName();
                _builder.append(_simpleName_1);
                _builder.append(".");
                String _simpleName_2 = method.getSimpleName();
                _builder.append(_simpleName_2);
                _builder.append("(");
                String _compile = this.compile(e.getLeftOperand(), c);
                _builder.append(_compile);
                {
                  int _size = method.getParameters().size();
                  boolean _greaterThan = (_size > 0);
                  if (_greaterThan) {
                    _builder.append(", ");
                  }
                }
                String _compile_1 = this.compile(e.getSel().getArgs(), method.getParameters(), c);
                _builder.append(_compile_1);
                _builder.append("))");
                _xifexpression_2 = _builder.toString();
              } else {
                StringConcatenation _builder_1 = new StringConcatenation();
                String _simpleName_3 = IQLUtils.class.getSimpleName();
                _builder_1.append(_simpleName_3);
                _builder_1.append(".toList(");
                String _simpleName_4 = typeOps.getClass().getSimpleName();
                _builder_1.append(_simpleName_4);
                _builder_1.append(".");
                String _simpleName_5 = method.getSimpleName();
                _builder_1.append(_simpleName_5);
                _builder_1.append("(");
                String _compile_2 = this.compile(e.getSel().getArgs(), method.getParameters(), c);
                _builder_1.append(_compile_2);
                _builder_1.append("))");
                _xifexpression_2 = _builder_1.toString();
              }
              _xblockexpression_2 = _xifexpression_2;
            }
            _xifexpression_1 = _xblockexpression_2;
          } else {
            String _xifexpression_2 = null;
            boolean _ignoreFirstParameter = this.typeExtensionsDictionary.ignoreFirstParameter(method);
            if (_ignoreFirstParameter) {
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName = typeOps.getClass().getSimpleName();
              _builder.append(_simpleName);
              _builder.append(".");
              String _simpleName_1 = method.getSimpleName();
              _builder.append(_simpleName_1);
              _builder.append("(");
              String _compile = this.compile(e.getLeftOperand(), c);
              _builder.append(_compile);
              {
                int _size = method.getParameters().size();
                boolean _greaterThan = (_size > 0);
                if (_greaterThan) {
                  _builder.append(", ");
                }
              }
              String _compile_1 = this.compile(e.getSel().getArgs(), method.getParameters(), c);
              _builder.append(_compile_1);
              _builder.append(")");
              _xifexpression_2 = _builder.toString();
            } else {
              StringConcatenation _builder_1 = new StringConcatenation();
              String _simpleName_2 = typeOps.getClass().getSimpleName();
              _builder_1.append(_simpleName_2);
              _builder_1.append(".");
              String _simpleName_3 = method.getSimpleName();
              _builder_1.append(_simpleName_3);
              _builder_1.append("(");
              String _compile_2 = this.compile(e.getSel().getArgs(), method.getParameters(), c);
              _builder_1.append(_compile_2);
              _builder_1.append(")");
              _xifexpression_2 = _builder_1.toString();
            }
            _xifexpression_1 = _xifexpression_2;
          }
          _xblockexpression_1 = _xifexpression_1;
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        if ((this.helper.hasSystemTypeCompiler(method) && this.helper.getSystemTypeCompiler(method).compileMethodSelectionManually())) {
          String _xblockexpression_2 = null;
          {
            IIQLSystemTypeCompiler systemTypeCompiler = this.helper.getSystemTypeCompiler(method);
            String _xifexpression_2 = null;
            if ((((method.getReturnType() != null) && this.helper.isJvmArray(method.getReturnType())) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
              String _xblockexpression_3 = null;
              {
                c.addImport(IQLUtils.class.getCanonicalName());
                StringConcatenation _builder = new StringConcatenation();
                String _simpleName = IQLUtils.class.getSimpleName();
                _builder.append(_simpleName);
                _builder.append(".toList(");
                String _compile = this.compile(e.getLeftOperand(), c);
                _builder.append(_compile);
                _builder.append(".");
                String _compileMethodSelection = systemTypeCompiler.compileMethodSelection(method, this.compileArguments(e.getSel().getArgs(), method.getParameters(), c));
                _builder.append(_compileMethodSelection);
                _builder.append(")");
                _xblockexpression_3 = _builder.toString();
              }
              _xifexpression_2 = _xblockexpression_3;
            } else {
              StringConcatenation _builder = new StringConcatenation();
              String _compile = this.compile(e.getLeftOperand(), c);
              _builder.append(_compile);
              _builder.append(".");
              String _compileMethodSelection = systemTypeCompiler.compileMethodSelection(method, this.compileArguments(e.getSel().getArgs(), method.getParameters(), c));
              _builder.append(_compileMethodSelection);
              _xifexpression_2 = _builder.toString();
            }
            _xblockexpression_2 = _xifexpression_2;
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          String _xifexpression_2 = null;
          if ((((method.getReturnType() != null) && this.helper.isJvmArray(method.getReturnType())) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
            String _xblockexpression_3 = null;
            {
              c.addImport(IQLUtils.class.getCanonicalName());
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName = IQLUtils.class.getSimpleName();
              _builder.append(_simpleName);
              _builder.append(".toList(");
              String _compile = this.compile(e.getLeftOperand(), c);
              _builder.append(_compile);
              _builder.append(".");
              String _simpleName_1 = method.getSimpleName();
              _builder.append(_simpleName_1);
              _builder.append("(");
              String _compile_1 = this.compile(e.getSel().getArgs(), method.getParameters(), c);
              _builder.append(_compile_1);
              _builder.append("))");
              _xblockexpression_3 = _builder.toString();
            }
            _xifexpression_2 = _xblockexpression_3;
          } else {
            StringConcatenation _builder = new StringConcatenation();
            String _compile = this.compile(e.getLeftOperand(), c);
            _builder.append(_compile);
            _builder.append(".");
            String _simpleName = method.getSimpleName();
            _builder.append(_simpleName);
            _builder.append("(");
            String _compile_1 = this.compile(e.getSel().getArgs(), method.getParameters(), c);
            _builder.append(_compile_1);
            _builder.append(")");
            _xifexpression_2 = _builder.toString();
          }
          _xifexpression_1 = _xifexpression_2;
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  @Override
  public String compile(final IQLArgumentsList args, final EList<JvmFormalParameter> parameters, final G c) {
    List<String> arguments = this.compileArguments(args, parameters, c);
    String result = "";
    int i = 0;
    for (final String argument : arguments) {
      {
        if ((i > 0)) {
          result = (result + ", ");
        }
        i++;
        result = (result + argument);
      }
    }
    return result;
  }
  
  public List<String> compileArguments(final IQLArgumentsList args, final EList<JvmFormalParameter> parameters, final G c) {
    ArrayList<String> result = new ArrayList<String>();
    if ((args == null)) {
      return result;
    }
    for (int i = 0; (i < parameters.size()); i++) {
      {
        JvmTypeReference expectedTypeRef = parameters.get(i).getParameterType();
        if ((expectedTypeRef != null)) {
          c.setExpectedTypeRef(expectedTypeRef);
        }
        TypeResult type = this.exprEvaluator.eval(args.getElements().get(i));
        if (((((expectedTypeRef != null) && this.helper.isJvmArray(expectedTypeRef)) && (!type.isNull())) && (!this.helper.isJvmArray(type.getRef())))) {
          c.addImport(IQLUtils.class.getCanonicalName());
          boolean _isPrimitiveArray = this.helper.isPrimitiveArray(expectedTypeRef);
          if (_isPrimitiveArray) {
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = IQLUtils.class.getSimpleName();
            _builder.append(_simpleName);
            _builder.append(".");
            String _arrayMethodName = this.helper.getArrayMethodName(expectedTypeRef);
            _builder.append(_arrayMethodName);
            _builder.append("(");
            String _compile = this.compile(args.getElements().get(i), c);
            _builder.append(_compile);
            _builder.append(")");
            result.add(_builder.toString());
          } else {
            String clazz = this.typeCompiler.compile(this.typeUtils.createTypeRef(this.typeUtils.getInnerType(expectedTypeRef, false)), c, true);
            StringConcatenation _builder_1 = new StringConcatenation();
            String _simpleName_1 = IQLUtils.class.getSimpleName();
            _builder_1.append(_simpleName_1);
            _builder_1.append(".");
            String _arrayMethodName_1 = this.helper.getArrayMethodName(expectedTypeRef);
            _builder_1.append(_arrayMethodName_1);
            _builder_1.append("(");
            _builder_1.append(clazz);
            _builder_1.append(".class, ");
            String _compile_1 = this.compile(args.getElements().get(i), c);
            _builder_1.append(_compile_1);
            _builder_1.append(")");
            result.add(_builder_1.toString());
          }
        } else {
          if (((!type.isNull()) && this.lookUp.isAssignable(parameters.get(i).getParameterType(), type.getRef()))) {
            result.add(this.compile(args.getElements().get(i), c));
          } else {
            if (((!type.isNull()) && this.lookUp.isCastable(expectedTypeRef, type.getRef()))) {
              String target = this.typeCompiler.compile(expectedTypeRef, c, false);
              StringConcatenation _builder_2 = new StringConcatenation();
              _builder_2.append("((");
              _builder_2.append(target);
              _builder_2.append(")");
              String _compile_2 = this.compile(args.getElements().get(i), c);
              _builder_2.append(_compile_2);
              _builder_2.append(")");
              result.add(_builder_2.toString());
            } else {
              result.add(this.compile(args.getElements().get(i), c));
            }
          }
        }
        c.setExpectedTypeRef(null);
      }
    }
    return result;
  }
  
  @Override
  public String compile(final IQLArgumentsList list, final G context) {
    String _xblockexpression = null;
    {
      if ((list == null)) {
        return "";
      }
      StringConcatenation _builder = new StringConcatenation();
      final Function1<IQLExpression, String> _function = (IQLExpression e) -> {
        return this.compile(e, context);
      };
      String _join = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(list.getElements(), _function), ", ");
      _builder.append(_join);
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  @Override
  public String compile(final IQLArgumentsMap map, final JvmTypeReference typeRef, final G c) {
    String result = "";
    for (int i = 0; (i < map.getElements().size()); i++) {
      {
        if ((i > 0)) {
          result = (result + ", ");
        }
        IQLArgumentsMapKeyValue el = map.getElements().get(i);
        TypeResult type = this.exprEvaluator.eval(el.getValue());
        JvmTypeReference expectedTypeRef = this.helper.getPropertyType(el.getKey(), typeRef);
        if ((expectedTypeRef != null)) {
          c.setExpectedTypeRef(expectedTypeRef);
        }
        if (((((expectedTypeRef != null) && this.helper.isJvmArray(expectedTypeRef)) && (!type.isNull())) && (!this.helper.isJvmArray(type.getRef())))) {
          c.addImport(IQLUtils.class.getCanonicalName());
          boolean _isPrimitiveArray = this.helper.isPrimitiveArray(expectedTypeRef);
          if (_isPrimitiveArray) {
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = IQLUtils.class.getSimpleName();
            _builder.append(_simpleName);
            _builder.append(".");
            String _arrayMethodName = this.helper.getArrayMethodName(expectedTypeRef);
            _builder.append(_arrayMethodName);
            _builder.append("(");
            String _compile = this.compile(el.getValue(), c);
            _builder.append(_compile);
            _builder.append(")");
            result = _builder.toString();
          } else {
            String clazz = this.typeCompiler.compile(this.typeUtils.createTypeRef(this.typeUtils.getInnerType(expectedTypeRef, false)), c, true);
            StringConcatenation _builder_1 = new StringConcatenation();
            String _simpleName_1 = IQLUtils.class.getSimpleName();
            _builder_1.append(_simpleName_1);
            _builder_1.append(".");
            String _arrayMethodName_1 = this.helper.getArrayMethodName(expectedTypeRef);
            _builder_1.append(_arrayMethodName_1);
            _builder_1.append("(");
            _builder_1.append(clazz);
            _builder_1.append(".class, ");
            String _compile_1 = this.compile(el.getValue(), c);
            _builder_1.append(_compile_1);
            _builder_1.append(")");
            result = _builder_1.toString();
          }
        } else {
          if (((!type.isNull()) && this.lookUp.isAssignable(expectedTypeRef, type.getRef()))) {
            String _compile_2 = this.compile(el.getValue(), c);
            String _plus = (result + _compile_2);
            result = _plus;
          } else {
            if (((!type.isNull()) && this.lookUp.isCastable(expectedTypeRef, type.getRef()))) {
              String target = this.typeCompiler.compile(expectedTypeRef, c, false);
              StringConcatenation _builder_2 = new StringConcatenation();
              _builder_2.append("((");
              _builder_2.append(target);
              _builder_2.append(")");
              String _compile_3 = this.compile(el.getValue(), c);
              _builder_2.append(_compile_3);
              _builder_2.append(")");
              String _plus_1 = (result + _builder_2);
              result = _plus_1;
            } else {
              String _compile_4 = this.compile(el.getValue(), c);
              String _plus_2 = (result + _compile_4);
              result = _plus_2;
            }
          }
        }
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
    String _xblockexpression = null;
    {
      JvmTypeReference thisType = this.lookUp.getThisType(e);
      EObject _eContainer = field.eContainer();
      JvmDeclaredType containerType = ((JvmDeclaredType) _eContainer);
      JvmTypeReference typeRef = this.typeUtils.createTypeRef(containerType);
      String _xifexpression = null;
      if (((thisType != null) && this.typeExtensionsDictionary.hasTypeExtensions(thisType, field.getSimpleName()))) {
        String _xblockexpression_1 = null;
        {
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(thisType, field.getSimpleName());
          c.addImport(typeOps.getClass().getCanonicalName());
          String _xifexpression_1 = null;
          if ((this.helper.isJvmArray(field.getType()) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
            String _xblockexpression_2 = null;
            {
              c.addImport(IQLUtils.class.getCanonicalName());
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName = IQLUtils.class.getSimpleName();
              _builder.append(_simpleName);
              _builder.append(".toList(");
              String _simpleName_1 = typeOps.getClass().getSimpleName();
              _builder.append(_simpleName_1);
              _builder.append(".");
              String _simpleName_2 = field.getSimpleName();
              _builder.append(_simpleName_2);
              _builder.append(")");
              _xblockexpression_2 = _builder.toString();
            }
            _xifexpression_1 = _xblockexpression_2;
          } else {
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = typeOps.getClass().getSimpleName();
            _builder.append(_simpleName);
            _builder.append(".");
            String _simpleName_1 = field.getSimpleName();
            _builder.append(_simpleName_1);
            _xifexpression_1 = _builder.toString();
          }
          _xblockexpression_1 = _xifexpression_1;
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        if ((this.helper.hasSystemTypeCompiler(field) && this.helper.getSystemTypeCompiler(field).compileFieldSelectionManually())) {
          String _xblockexpression_2 = null;
          {
            IIQLSystemTypeCompiler systemTypeCompiler = this.helper.getSystemTypeCompiler(field);
            String _xifexpression_2 = null;
            if ((this.helper.isJvmArray(field.getType()) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
              String _xblockexpression_3 = null;
              {
                c.addImport(IQLUtils.class.getCanonicalName());
                String _xifexpression_3 = null;
                boolean _isStatic = field.isStatic();
                if (_isStatic) {
                  StringConcatenation _builder = new StringConcatenation();
                  String _simpleName = IQLUtils.class.getSimpleName();
                  _builder.append(_simpleName);
                  _builder.append(".toList(");
                  String _compile = this.typeCompiler.compile(typeRef, c, true);
                  _builder.append(_compile);
                  _builder.append(".");
                  String _compileFieldSelection = systemTypeCompiler.compileFieldSelection(field);
                  _builder.append(_compileFieldSelection);
                  _builder.append(")");
                  _xifexpression_3 = _builder.toString();
                } else {
                  StringConcatenation _builder_1 = new StringConcatenation();
                  String _compile_1 = this.typeCompiler.compile(typeRef, c, true);
                  _builder_1.append(_compile_1);
                  _builder_1.append(".");
                  String _compileFieldSelection_1 = systemTypeCompiler.compileFieldSelection(field);
                  _builder_1.append(_compileFieldSelection_1);
                  _xifexpression_3 = _builder_1.toString();
                }
                _xblockexpression_3 = _xifexpression_3;
              }
              _xifexpression_2 = _xblockexpression_3;
            } else {
              String _xifexpression_3 = null;
              boolean _isStatic = field.isStatic();
              if (_isStatic) {
                StringConcatenation _builder = new StringConcatenation();
                String _simpleName = IQLUtils.class.getSimpleName();
                _builder.append(_simpleName);
                _builder.append(".toList(");
                String _compileFieldSelection = systemTypeCompiler.compileFieldSelection(field);
                _builder.append(_compileFieldSelection);
                _builder.append(")");
                _xifexpression_3 = _builder.toString();
              } else {
                StringConcatenation _builder_1 = new StringConcatenation();
                String _compileFieldSelection_1 = systemTypeCompiler.compileFieldSelection(field);
                _builder_1.append(_compileFieldSelection_1);
                _xifexpression_3 = _builder_1.toString();
              }
              _xifexpression_2 = _xifexpression_3;
            }
            _xblockexpression_2 = _xifexpression_2;
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          String _xifexpression_2 = null;
          boolean _isStatic = field.isStatic();
          if (_isStatic) {
            String _xifexpression_3 = null;
            if ((this.helper.isJvmArray(field.getType()) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
              String _xblockexpression_3 = null;
              {
                c.addImport(IQLUtils.class.getCanonicalName());
                StringConcatenation _builder = new StringConcatenation();
                String _simpleName = IQLUtils.class.getSimpleName();
                _builder.append(_simpleName);
                _builder.append(".toList(");
                String _compile = this.typeCompiler.compile(typeRef, c, true);
                _builder.append(_compile);
                _builder.append(".");
                String _simpleName_1 = field.getSimpleName();
                _builder.append(_simpleName_1);
                _builder.append(")");
                _xblockexpression_3 = _builder.toString();
              }
              _xifexpression_3 = _xblockexpression_3;
            } else {
              StringConcatenation _builder = new StringConcatenation();
              String _compile = this.typeCompiler.compile(typeRef, c, true);
              _builder.append(_compile);
              _builder.append(".");
              String _simpleName = field.getSimpleName();
              _builder.append(_simpleName);
              _xifexpression_3 = _builder.toString();
            }
            _xifexpression_2 = _xifexpression_3;
          } else {
            String _xifexpression_4 = null;
            if ((this.helper.isJvmArray(field.getType()) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
              String _xblockexpression_4 = null;
              {
                c.addImport(IQLUtils.class.getCanonicalName());
                StringConcatenation _builder_1 = new StringConcatenation();
                String _simpleName_1 = IQLUtils.class.getSimpleName();
                _builder_1.append(_simpleName_1);
                _builder_1.append(".toList(");
                String _simpleName_2 = field.getSimpleName();
                _builder_1.append(_simpleName_2);
                _builder_1.append(")");
                _xblockexpression_4 = _builder_1.toString();
              }
              _xifexpression_4 = _xblockexpression_4;
            } else {
              StringConcatenation _builder_1 = new StringConcatenation();
              String _simpleName_1 = field.getSimpleName();
              _builder_1.append(_simpleName_1);
              _xifexpression_4 = _builder_1.toString();
            }
            _xifexpression_2 = _xifexpression_4;
          }
          _xifexpression_1 = _xifexpression_2;
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLJvmElementCallExpression e, final IQLVariableDeclaration varDecl, final G c) {
    String _xifexpression = null;
    if ((this.helper.isJvmArray(varDecl.getRef()) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
      String _xblockexpression = null;
      {
        c.addImport(IQLUtils.class.getCanonicalName());
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName);
        _builder.append(".toList(");
        String _name = varDecl.getName();
        _builder.append(_name);
        _builder.append(")");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      StringConcatenation _builder = new StringConcatenation();
      String _name = varDecl.getName();
      _builder.append(_name);
      _xifexpression = _builder.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLJvmElementCallExpression e, final JvmFormalParameter parameter, final G c) {
    String _xifexpression = null;
    if ((this.helper.isJvmArray(parameter.getParameterType()) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
      String _xblockexpression = null;
      {
        c.addImport(IQLUtils.class.getCanonicalName());
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName);
        _builder.append(".toList(");
        String _name = parameter.getName();
        _builder.append(_name);
        _builder.append(")");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      StringConcatenation _builder = new StringConcatenation();
      String _name = parameter.getName();
      _builder.append(_name);
      _xifexpression = _builder.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLJvmElementCallExpression m, final JvmOperation method, final G c) {
    String _xblockexpression = null;
    {
      c.addExceptions(method.getExceptions());
      List<IQLExpression> list = null;
      IQLArgumentsList _args = m.getArgs();
      boolean _tripleNotEquals = (_args != null);
      if (_tripleNotEquals) {
        list = m.getArgs().getElements();
      } else {
        ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
        list = _arrayList;
      }
      JvmTypeReference thisType = this.lookUp.getThisType(m);
      EObject _eContainer = method.eContainer();
      JvmDeclaredType containerType = ((JvmDeclaredType) _eContainer);
      JvmTypeReference typeRef = this.typeUtils.createTypeRef(containerType);
      String _xifexpression = null;
      if (((thisType != null) && this.typeExtensionsDictionary.hasTypeExtensions(thisType, method.getSimpleName(), list))) {
        String _xblockexpression_1 = null;
        {
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(thisType, method.getSimpleName(), list);
          c.addImport(typeOps.getClass().getCanonicalName());
          String _xifexpression_1 = null;
          if ((((method.getReturnType() != null) && this.helper.isJvmArray(method.getReturnType())) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
            String _xblockexpression_2 = null;
            {
              c.addImport(IQLUtils.class.getCanonicalName());
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName = IQLUtils.class.getSimpleName();
              _builder.append(_simpleName);
              _builder.append(".toList(");
              String _simpleName_1 = typeOps.getClass().getSimpleName();
              _builder.append(_simpleName_1);
              _builder.append(".");
              String _simpleName_2 = method.getSimpleName();
              _builder.append(_simpleName_2);
              _builder.append("(this");
              {
                int _size = method.getParameters().size();
                boolean _greaterThan = (_size > 0);
                if (_greaterThan) {
                  _builder.append(", ");
                }
              }
              String _compile = this.compile(m.getArgs(), method.getParameters(), c);
              _builder.append(_compile);
              _builder.append("))");
              _xblockexpression_2 = _builder.toString();
            }
            _xifexpression_1 = _xblockexpression_2;
          } else {
            String _xifexpression_2 = null;
            boolean _ignoreFirstParameter = this.typeExtensionsDictionary.ignoreFirstParameter(method);
            if (_ignoreFirstParameter) {
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName = typeOps.getClass().getSimpleName();
              _builder.append(_simpleName);
              _builder.append(".");
              String _simpleName_1 = method.getSimpleName();
              _builder.append(_simpleName_1);
              _builder.append("(this");
              {
                int _size = method.getParameters().size();
                boolean _greaterThan = (_size > 0);
                if (_greaterThan) {
                  _builder.append(", ");
                }
              }
              String _compile = this.compile(m.getArgs(), method.getParameters(), c);
              _builder.append(_compile);
              _builder.append(")");
              _xifexpression_2 = _builder.toString();
            } else {
              StringConcatenation _builder_1 = new StringConcatenation();
              String _simpleName_2 = typeOps.getClass().getSimpleName();
              _builder_1.append(_simpleName_2);
              _builder_1.append(".");
              String _simpleName_3 = method.getSimpleName();
              _builder_1.append(_simpleName_3);
              _builder_1.append("(");
              String _compile_1 = this.compile(m.getArgs(), method.getParameters(), c);
              _builder_1.append(_compile_1);
              _builder_1.append(")");
              _xifexpression_2 = _builder_1.toString();
            }
            _xifexpression_1 = _xifexpression_2;
          }
          _xblockexpression_1 = _xifexpression_1;
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        if ((this.helper.hasSystemTypeCompiler(method) && this.helper.getSystemTypeCompiler(method).compileMethodSelectionManually())) {
          String _xblockexpression_2 = null;
          {
            IIQLSystemTypeCompiler systemTypeCompiler = this.helper.getSystemTypeCompiler(method);
            String _xifexpression_2 = null;
            if ((((method.getReturnType() != null) && this.helper.isJvmArray(method.getReturnType())) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
              String _xblockexpression_3 = null;
              {
                c.addImport(IQLUtils.class.getCanonicalName());
                String _xifexpression_3 = null;
                boolean _isStatic = method.isStatic();
                if (_isStatic) {
                  StringConcatenation _builder = new StringConcatenation();
                  String _simpleName = IQLUtils.class.getSimpleName();
                  _builder.append(_simpleName);
                  _builder.append(".toList(");
                  String _compile = this.typeCompiler.compile(typeRef, c, true);
                  _builder.append(_compile);
                  _builder.append(".");
                  String _compileMethodSelection = systemTypeCompiler.compileMethodSelection(method, this.compileArguments(m.getArgs(), method.getParameters(), c));
                  _builder.append(_compileMethodSelection);
                  _builder.append(")");
                  _xifexpression_3 = _builder.toString();
                } else {
                  StringConcatenation _builder_1 = new StringConcatenation();
                  String _simpleName_1 = IQLUtils.class.getSimpleName();
                  _builder_1.append(_simpleName_1);
                  _builder_1.append(".toList(");
                  String _compileMethodSelection_1 = systemTypeCompiler.compileMethodSelection(method, this.compileArguments(m.getArgs(), method.getParameters(), c));
                  _builder_1.append(_compileMethodSelection_1);
                  _builder_1.append(")");
                  _xifexpression_3 = _builder_1.toString();
                }
                _xblockexpression_3 = _xifexpression_3;
              }
              _xifexpression_2 = _xblockexpression_3;
            } else {
              String _xifexpression_3 = null;
              boolean _isStatic = method.isStatic();
              if (_isStatic) {
                StringConcatenation _builder = new StringConcatenation();
                String _compile = this.typeCompiler.compile(typeRef, c, true);
                _builder.append(_compile);
                _builder.append(".");
                String _compileMethodSelection = systemTypeCompiler.compileMethodSelection(method, this.compileArguments(m.getArgs(), method.getParameters(), c));
                _builder.append(_compileMethodSelection);
                _xifexpression_3 = _builder.toString();
              } else {
                StringConcatenation _builder_1 = new StringConcatenation();
                String _compileMethodSelection_1 = systemTypeCompiler.compileMethodSelection(method, this.compileArguments(m.getArgs(), method.getParameters(), c));
                _builder_1.append(_compileMethodSelection_1);
                _xifexpression_3 = _builder_1.toString();
              }
              _xifexpression_2 = _xifexpression_3;
            }
            _xblockexpression_2 = _xifexpression_2;
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          String _xifexpression_2 = null;
          boolean _isStatic = method.isStatic();
          if (_isStatic) {
            String _xifexpression_3 = null;
            if ((((method.getReturnType() != null) && this.helper.isJvmArray(method.getReturnType())) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
              String _xblockexpression_3 = null;
              {
                c.addImport(IQLUtils.class.getCanonicalName());
                StringConcatenation _builder = new StringConcatenation();
                String _simpleName = IQLUtils.class.getSimpleName();
                _builder.append(_simpleName);
                _builder.append(".toList(");
                String _compile = this.typeCompiler.compile(typeRef, c, true);
                _builder.append(_compile);
                _builder.append(".");
                String _simpleName_1 = method.getSimpleName();
                _builder.append(_simpleName_1);
                _builder.append("(");
                String _compile_1 = this.compile(m.getArgs(), method.getParameters(), c);
                _builder.append(_compile_1);
                _builder.append("))");
                _xblockexpression_3 = _builder.toString();
              }
              _xifexpression_3 = _xblockexpression_3;
            } else {
              StringConcatenation _builder = new StringConcatenation();
              String _compile = this.typeCompiler.compile(typeRef, c, true);
              _builder.append(_compile);
              _builder.append(".");
              String _simpleName = method.getSimpleName();
              _builder.append(_simpleName);
              _builder.append("(");
              String _compile_1 = this.compile(m.getArgs(), method.getParameters(), c);
              _builder.append(_compile_1);
              _builder.append(")");
              _xifexpression_3 = _builder.toString();
            }
            _xifexpression_2 = _xifexpression_3;
          } else {
            String _xifexpression_4 = null;
            if ((((method.getReturnType() != null) && this.helper.isJvmArray(method.getReturnType())) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
              String _xblockexpression_4 = null;
              {
                c.addImport(IQLUtils.class.getCanonicalName());
                StringConcatenation _builder_1 = new StringConcatenation();
                String _simpleName_1 = IQLUtils.class.getSimpleName();
                _builder_1.append(_simpleName_1);
                _builder_1.append(".toList(");
                String _simpleName_2 = method.getSimpleName();
                _builder_1.append(_simpleName_2);
                _builder_1.append("(");
                {
                  IQLArgumentsList _args_1 = m.getArgs();
                  boolean _tripleNotEquals_1 = (_args_1 != null);
                  if (_tripleNotEquals_1) {
                    String _compile_2 = this.compile(m.getArgs(), method.getParameters(), c);
                    _builder_1.append(_compile_2);
                  }
                }
                _builder_1.append("))");
                _xblockexpression_4 = _builder_1.toString();
              }
              _xifexpression_4 = _xblockexpression_4;
            } else {
              StringConcatenation _builder_1 = new StringConcatenation();
              String _simpleName_1 = method.getSimpleName();
              _builder_1.append(_simpleName_1);
              _builder_1.append("(");
              {
                IQLArgumentsList _args_1 = m.getArgs();
                boolean _tripleNotEquals_1 = (_args_1 != null);
                if (_tripleNotEquals_1) {
                  String _compile_2 = this.compile(m.getArgs(), method.getParameters(), c);
                  _builder_1.append(_compile_2);
                }
              }
              _builder_1.append(")");
              _xifexpression_4 = _builder_1.toString();
            }
            _xifexpression_2 = _xifexpression_4;
          }
          _xifexpression_1 = _xifexpression_2;
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
    String _compile = this.compile(e.getExpr(), c);
    _builder.append(_compile);
    _builder.append(")");
    return _builder.toString();
  }
  
  public String compile(final IQLNewExpression e, final G c) {
    String _xifexpression = null;
    if ((((e.getArgsList() != null) && (e.getArgsMap() != null)) && (e.getArgsMap().getElements().size() > 0))) {
      String _xblockexpression = null;
      {
        JvmExecutable constructor = this.lookUp.findPublicConstructor(e.getRef(), e.getArgsList().getElements());
        c.addExceptions(constructor.getExceptions());
        String _xifexpression_1 = null;
        if ((constructor != null)) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("get");
          String _shortName = this.typeUtils.getShortName(e.getRef(), false);
          _builder.append(_shortName);
          int _hashCode = e.getRef().hashCode();
          _builder.append(_hashCode);
          _builder.append("(new ");
          String _compile = this.typeCompiler.compile(e.getRef(), c, true);
          _builder.append(_compile);
          _builder.append("(");
          String _compile_1 = this.compile(e.getArgsList(), constructor.getParameters(), c);
          _builder.append(_compile_1);
          _builder.append("), ");
          String _compile_2 = this.compile(e.getArgsMap(), e.getRef(), c);
          _builder.append(_compile_2);
          _builder.append(")");
          _xifexpression_1 = _builder.toString();
        } else {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("get");
          String _shortName_1 = this.typeUtils.getShortName(e.getRef(), false);
          _builder_1.append(_shortName_1);
          int _hashCode_1 = e.getRef().hashCode();
          _builder_1.append(_hashCode_1);
          _builder_1.append("(new ");
          String _compile_3 = this.typeCompiler.compile(e.getRef(), c, true);
          _builder_1.append(_compile_3);
          _builder_1.append("(");
          String _compile_4 = this.compile(e.getArgsList(), c);
          _builder_1.append(_compile_4);
          _builder_1.append("), ");
          String _compile_5 = this.compile(e.getArgsMap(), e.getRef(), c);
          _builder_1.append(_compile_5);
          _builder_1.append(")");
          _xifexpression_1 = _builder_1.toString();
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    } else {
      String _xifexpression_1 = null;
      if (((e.getArgsMap() != null) && (e.getArgsMap().getElements().size() > 0))) {
        String _xblockexpression_1 = null;
        {
          JvmTypeReference _ref = e.getRef();
          ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
          JvmExecutable constructor = this.lookUp.findPublicConstructor(_ref, _arrayList);
          if ((constructor != null)) {
            c.addExceptions(constructor.getExceptions());
          }
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("get");
          String _shortName = this.typeUtils.getShortName(e.getRef(), false);
          _builder.append(_shortName);
          int _hashCode = e.getRef().hashCode();
          _builder.append(_hashCode);
          _builder.append("(new ");
          String _compile = this.typeCompiler.compile(e.getRef(), c, true);
          _builder.append(_compile);
          _builder.append("(), ");
          String _compile_1 = this.compile(e.getArgsMap(), e.getRef(), c);
          _builder.append(_compile_1);
          _builder.append(")");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression_1 = _xblockexpression_1;
      } else {
        String _xifexpression_2 = null;
        IQLArgumentsList _argsList = e.getArgsList();
        boolean _tripleNotEquals = (_argsList != null);
        if (_tripleNotEquals) {
          String _xblockexpression_2 = null;
          {
            JvmExecutable constructor = this.lookUp.findPublicConstructor(e.getRef(), e.getArgsList().getElements());
            String _xifexpression_3 = null;
            if ((constructor != null)) {
              String _xblockexpression_3 = null;
              {
                c.addExceptions(constructor.getExceptions());
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("new ");
                String _compile = this.typeCompiler.compile(e.getRef(), c, true);
                _builder.append(_compile);
                _builder.append("(");
                String _compile_1 = this.compile(e.getArgsList(), constructor.getParameters(), c);
                _builder.append(_compile_1);
                _builder.append(")");
                _xblockexpression_3 = _builder.toString();
              }
              _xifexpression_3 = _xblockexpression_3;
            } else {
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("new ");
              String _compile = this.typeCompiler.compile(e.getRef(), c, true);
              _builder.append(_compile);
              _builder.append("(");
              String _compile_1 = this.compile(e.getArgsList(), c);
              _builder.append(_compile_1);
              _builder.append(")");
              _xifexpression_3 = _builder.toString();
            }
            _xblockexpression_2 = _xifexpression_3;
          }
          _xifexpression_2 = _xblockexpression_2;
        } else {
          String _xifexpression_3 = null;
          JvmTypeReference _ref = e.getRef();
          if ((_ref instanceof IQLArrayTypeRef)) {
            String _xblockexpression_3 = null;
            {
              c.addImport(ArrayList.class.getCanonicalName());
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("new ");
              String _simpleName = ArrayList.class.getSimpleName();
              _builder.append(_simpleName);
              _builder.append("()");
              _xblockexpression_3 = _builder.toString();
            }
            _xifexpression_3 = _xblockexpression_3;
          } else {
            String _xblockexpression_4 = null;
            {
              JvmTypeReference _ref_1 = e.getRef();
              ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
              JvmExecutable constructor = this.lookUp.findPublicConstructor(_ref_1, _arrayList);
              if ((constructor != null)) {
                c.addExceptions(constructor.getExceptions());
              }
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("new ");
              String _compile = this.typeCompiler.compile(e.getRef(), c, true);
              _builder.append(_compile);
              _builder.append("()");
              _xblockexpression_4 = _builder.toString();
            }
            _xifexpression_3 = _xblockexpression_4;
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLLiteralExpressionDouble e, final G c) {
    String _xifexpression = null;
    if (((c.getExpectedTypeRef() != null) && this.typeExtensionsDictionary.hasTypeExtensions(c.getExpectedTypeRef(), "doubleToType", e))) {
      String _xblockexpression = null;
      {
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(c.getExpectedTypeRef(), "doubleToType", e);
        c.addImport(typeOps.getClass().getCanonicalName());
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = typeOps.getClass().getSimpleName();
        _builder.append(_simpleName);
        _builder.append(".doubleToType(");
        double _value = e.getValue();
        _builder.append(_value);
        _builder.append(")");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      String _xifexpression_1 = null;
      JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
      boolean _tripleNotEquals = (_expectedTypeRef != null);
      if (_tripleNotEquals) {
        String _xifexpression_2 = null;
        boolean _isFloat = this.typeUtils.isFloat(c.getExpectedTypeRef());
        if (_isFloat) {
          StringConcatenation _builder = new StringConcatenation();
          double _value = e.getValue();
          _builder.append(_value);
          _builder.append("F");
          _xifexpression_2 = _builder.toString();
        } else {
          String _xifexpression_3 = null;
          boolean _isDouble = this.typeUtils.isDouble(c.getExpectedTypeRef(), true);
          if (_isDouble) {
            StringConcatenation _builder_1 = new StringConcatenation();
            double _value_1 = e.getValue();
            _builder_1.append(_value_1);
            _builder_1.append("D");
            _xifexpression_3 = _builder_1.toString();
          } else {
            StringConcatenation _builder_2 = new StringConcatenation();
            double _value_2 = e.getValue();
            _builder_2.append(_value_2);
            _xifexpression_3 = _builder_2.toString();
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      } else {
        StringConcatenation _builder_3 = new StringConcatenation();
        double _value_3 = e.getValue();
        _builder_3.append(_value_3);
        _xifexpression_1 = _builder_3.toString();
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLLiteralExpressionInt e, final G c) {
    String _xifexpression = null;
    if (((c.getExpectedTypeRef() != null) && this.typeExtensionsDictionary.hasTypeExtensions(c.getExpectedTypeRef(), "intToType", e))) {
      String _xblockexpression = null;
      {
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(c.getExpectedTypeRef(), "intToType", e);
        c.addImport(typeOps.getClass().getCanonicalName());
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = typeOps.getClass().getSimpleName();
        _builder.append(_simpleName);
        _builder.append(".intToType(");
        int _value = e.getValue();
        _builder.append(_value);
        _builder.append(")");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      String _xifexpression_1 = null;
      JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
      boolean _tripleNotEquals = (_expectedTypeRef != null);
      if (_tripleNotEquals) {
        String _xifexpression_2 = null;
        boolean _isFloat = this.typeUtils.isFloat(c.getExpectedTypeRef());
        if (_isFloat) {
          StringConcatenation _builder = new StringConcatenation();
          int _value = e.getValue();
          _builder.append(_value);
          _builder.append("F");
          _xifexpression_2 = _builder.toString();
        } else {
          String _xifexpression_3 = null;
          boolean _isDouble = this.typeUtils.isDouble(c.getExpectedTypeRef(), true);
          if (_isDouble) {
            StringConcatenation _builder_1 = new StringConcatenation();
            int _value_1 = e.getValue();
            _builder_1.append(_value_1);
            _builder_1.append("D");
            _xifexpression_3 = _builder_1.toString();
          } else {
            String _xifexpression_4 = null;
            boolean _isLong = this.typeUtils.isLong(c.getExpectedTypeRef(), true);
            if (_isLong) {
              StringConcatenation _builder_2 = new StringConcatenation();
              int _value_2 = e.getValue();
              _builder_2.append(_value_2);
              _builder_2.append("L");
              _xifexpression_4 = _builder_2.toString();
            } else {
              StringConcatenation _builder_3 = new StringConcatenation();
              int _value_3 = e.getValue();
              _builder_3.append(_value_3);
              _xifexpression_4 = _builder_3.toString();
            }
            _xifexpression_3 = _xifexpression_4;
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      } else {
        StringConcatenation _builder_4 = new StringConcatenation();
        int _value_4 = e.getValue();
        _builder_4.append(_value_4);
        _xifexpression_1 = _builder_4.toString();
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLLiteralExpressionString e, final G c) {
    String _xifexpression = null;
    if (((c.getExpectedTypeRef() != null) && this.typeExtensionsDictionary.hasTypeExtensions(c.getExpectedTypeRef(), "stringToType", e))) {
      String _xblockexpression = null;
      {
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(c.getExpectedTypeRef(), "stringToType", e);
        c.addImport(typeOps.getClass().getCanonicalName());
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = typeOps.getClass().getSimpleName();
        _builder.append(_simpleName);
        _builder.append(".stringToType(\"");
        String _value = e.getValue();
        _builder.append(_value);
        _builder.append("\")");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      String _xifexpression_1 = null;
      JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
      boolean _tripleNotEquals = (_expectedTypeRef != null);
      if (_tripleNotEquals) {
        String _xifexpression_2 = null;
        boolean _isCharacter = this.typeUtils.isCharacter(c.getExpectedTypeRef());
        if (_isCharacter) {
          String _value = e.getValue();
          String _plus = ("\'" + _value);
          return (_plus + "\'");
        } else {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("\"");
          String _value_1 = e.getValue();
          _builder.append(_value_1);
          _builder.append("\"");
          _xifexpression_2 = _builder.toString();
        }
        _xifexpression_1 = _xifexpression_2;
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("\"");
        String _value_2 = e.getValue();
        _builder_1.append(_value_2);
        _builder_1.append("\"");
        _xifexpression_1 = _builder_1.toString();
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLLiteralExpressionBoolean e, final G c) {
    String _xifexpression = null;
    if (((c.getExpectedTypeRef() != null) && this.typeExtensionsDictionary.hasTypeExtensions(c.getExpectedTypeRef(), "booleanToType", e))) {
      String _xblockexpression = null;
      {
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(c.getExpectedTypeRef(), "booleanToType", e);
        c.addImport(typeOps.getClass().getCanonicalName());
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = typeOps.getClass().getSimpleName();
        _builder.append(_simpleName);
        _builder.append(".booleanToType(");
        boolean _isValue = e.isValue();
        _builder.append(_isValue);
        _builder.append(")");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      StringConcatenation _builder = new StringConcatenation();
      boolean _isValue = e.isValue();
      _builder.append(_isValue);
      _xifexpression = _builder.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLLiteralExpressionType e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("((Class)");
    String _compile = this.typeCompiler.compile(e.getValue(), c, true);
    _builder.append(_compile);
    _builder.append(".class)");
    return _builder.toString();
  }
  
  public String compile(final IQLLiteralExpressionRange e, final G c) {
    String _xblockexpression = null;
    {
      int from = Integer.parseInt(e.getValue().substring(0, e.getValue().indexOf(".")));
      String _value = e.getValue();
      int _lastIndexOf = e.getValue().lastIndexOf(".");
      int _plus = (_lastIndexOf + 1);
      int to = Integer.parseInt(_value.substring(_plus, e.getValue().length()));
      String _xifexpression = null;
      if (((c.getExpectedTypeRef() != null) && this.typeExtensionsDictionary.hasTypeExtensions(c.getExpectedTypeRef(), "rangeToType", e))) {
        String _xblockexpression_1 = null;
        {
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(c.getExpectedTypeRef(), "rangeToType", e);
          c.addImport(typeOps.getClass().getCanonicalName());
          c.addImport(Range.class.getCanonicalName());
          StringConcatenation _builder = new StringConcatenation();
          String _simpleName = typeOps.getClass().getSimpleName();
          _builder.append(_simpleName);
          _builder.append(".rangeToType(new ");
          String _simpleName_1 = Range.class.getSimpleName();
          _builder.append(_simpleName_1);
          _builder.append("(");
          _builder.append(from);
          _builder.append(" , ");
          _builder.append(to);
          _builder.append("))");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xblockexpression_2 = null;
        {
          c.addImport(Range.class.getCanonicalName());
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("new ");
          String _simpleName = Range.class.getSimpleName();
          _builder.append(_simpleName);
          _builder.append("(");
          _builder.append(from);
          _builder.append(" , ");
          _builder.append(to);
          _builder.append(")");
          _xblockexpression_2 = _builder.toString();
        }
        _xifexpression = _xblockexpression_2;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLLiteralExpressionNull e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("null");
    return _builder.toString();
  }
  
  public String compile(final IQLLiteralExpressionList e, final G c) {
    String _xifexpression = null;
    if (((c.getExpectedTypeRef() != null) && this.typeExtensionsDictionary.hasTypeExtensions(c.getExpectedTypeRef(), "listToType", e))) {
      String _xblockexpression = null;
      {
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(c.getExpectedTypeRef(), "listToType", e);
        c.addImport(typeOps.getClass().getCanonicalName());
        c.setExpectedTypeRef(null);
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = typeOps.getClass().getSimpleName();
        _builder.append(_simpleName);
        _builder.append(".listToType(");
        String _simpleName_1 = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName_1);
        _builder.append(".createList(");
        final Function1<IQLExpression, String> _function = (IQLExpression el) -> {
          return this.compile(el, c);
        };
        String _join = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(e.getElements(), _function), ", ");
        _builder.append(_join);
        _builder.append("))");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      String _xblockexpression_1 = null;
      {
        c.addImport(IQLUtils.class.getCanonicalName());
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName);
        _builder.append(".createList(");
        final Function1<IQLExpression, String> _function = (IQLExpression el) -> {
          return this.compile(el, c);
        };
        String _join = IterableExtensions.join(ListExtensions.<IQLExpression, String>map(e.getElements(), _function), ", ");
        _builder.append(_join);
        _builder.append(")");
        _xblockexpression_1 = _builder.toString();
      }
      _xifexpression = _xblockexpression_1;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLLiteralExpressionMap e, final G c) {
    String _xifexpression = null;
    if (((c.getExpectedTypeRef() != null) && this.typeExtensionsDictionary.hasTypeExtensions(c.getExpectedTypeRef(), "mapToType", e))) {
      String _xblockexpression = null;
      {
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(c.getExpectedTypeRef(), "mapToType", e);
        c.addImport(typeOps.getClass().getCanonicalName());
        c.setExpectedTypeRef(null);
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = typeOps.getClass().getSimpleName();
        _builder.append(_simpleName);
        _builder.append(".mapToType(");
        String _simpleName_1 = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName_1);
        _builder.append(".createMap(");
        final Function1<IQLLiteralExpressionMapKeyValue, String> _function = (IQLLiteralExpressionMapKeyValue el) -> {
          String _compile = this.compile(el.getKey(), c);
          String _plus = (_compile + ", ");
          String _compile_1 = this.compile(el.getValue(), c);
          return (_plus + _compile_1);
        };
        String _join = IterableExtensions.join(ListExtensions.<IQLLiteralExpressionMapKeyValue, String>map(e.getElements(), _function), ", ");
        _builder.append(_join);
        _builder.append("))");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      String _xblockexpression_1 = null;
      {
        c.addImport(IQLUtils.class.getCanonicalName());
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("\t");
        String _simpleName = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName, "\t");
        _builder.append(".createMap(");
        final Function1<IQLLiteralExpressionMapKeyValue, String> _function = (IQLLiteralExpressionMapKeyValue el) -> {
          String _compile = this.compile(el.getKey(), c);
          String _plus = (_compile + ", ");
          String _compile_1 = this.compile(el.getValue(), c);
          return (_plus + _compile_1);
        };
        String _join = IterableExtensions.join(ListExtensions.<IQLLiteralExpressionMapKeyValue, String>map(e.getElements(), _function), ", ");
        _builder.append(_join, "\t");
        _builder.append(")");
        _xblockexpression_1 = _builder.toString();
      }
      _xifexpression = _xblockexpression_1;
    }
    return _xifexpression;
  }
}
