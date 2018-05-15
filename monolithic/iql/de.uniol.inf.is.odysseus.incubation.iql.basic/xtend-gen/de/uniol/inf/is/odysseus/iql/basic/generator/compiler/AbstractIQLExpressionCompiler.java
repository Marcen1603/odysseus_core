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
import org.eclipse.xtext.common.types.JvmType;
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
      EList<JvmFormalParameter> _parameters = ((JvmOperation) _element).getParameters();
      JvmFormalParameter _get = _parameters.get(0);
      JvmTypeReference leftType = _get.getParameterType();
      IQLExpression _rightOperand = e.getRightOperand();
      TypeResult rightType = this.exprEvaluator.eval(_rightOperand);
      c.setExpectedTypeRef(leftType);
      String result = "";
      JvmIdentifiableElement _element_1 = elementCallExpr.getElement();
      JvmOperation op = ((JvmOperation) _element_1);
      EList<JvmTypeReference> _exceptions = op.getExceptions();
      c.addExceptions(_exceptions);
      if (((this.helper.isJvmArray(leftType) && (!rightType.isNull())) && (!this.helper.isJvmArray(rightType.getRef())))) {
        String _canonicalName = IQLUtils.class.getCanonicalName();
        c.addImport(_canonicalName);
        boolean _isPrimitiveArray = this.helper.isPrimitiveArray(leftType);
        if (_isPrimitiveArray) {
          StringConcatenation _builder = new StringConcatenation();
          String _simpleName = op.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append("(");
          String _simpleName_1 = IQLUtils.class.getSimpleName();
          _builder.append(_simpleName_1, "");
          _builder.append(".");
          String _arrayMethodName = this.helper.getArrayMethodName(leftType);
          _builder.append(_arrayMethodName, "");
          _builder.append("(");
          IQLExpression _rightOperand_1 = e.getRightOperand();
          String _compile = this.compile(_rightOperand_1, c);
          _builder.append(_compile, "");
          _builder.append("))");
          result = _builder.toString();
        } else {
          JvmType _innerType = this.typeUtils.getInnerType(leftType, false);
          JvmTypeReference _createTypeRef = this.typeUtils.createTypeRef(_innerType);
          String clazz = this.typeCompiler.compile(_createTypeRef, c, true);
          StringConcatenation _builder_1 = new StringConcatenation();
          String _simpleName_2 = op.getSimpleName();
          _builder_1.append(_simpleName_2, "");
          _builder_1.append("(");
          String _simpleName_3 = IQLUtils.class.getSimpleName();
          _builder_1.append(_simpleName_3, "");
          _builder_1.append(".");
          String _arrayMethodName_1 = this.helper.getArrayMethodName(leftType);
          _builder_1.append(_arrayMethodName_1, "");
          _builder_1.append("(");
          _builder_1.append(clazz, "");
          _builder_1.append(".class, ");
          IQLExpression _rightOperand_2 = e.getRightOperand();
          String _compile_1 = this.compile(_rightOperand_2, c);
          _builder_1.append(_compile_1, "");
          _builder_1.append("))");
          result = _builder_1.toString();
        }
      } else {
        if ((rightType.isNull() || this.lookUp.isAssignable(leftType, rightType.getRef()))) {
          StringConcatenation _builder_2 = new StringConcatenation();
          String _simpleName_4 = op.getSimpleName();
          _builder_2.append(_simpleName_4, "");
          _builder_2.append("(");
          IQLExpression _rightOperand_3 = e.getRightOperand();
          String _compile_2 = this.compile(_rightOperand_3, c);
          _builder_2.append(_compile_2, "");
          _builder_2.append(")");
          result = _builder_2.toString();
        } else {
          if ((rightType.isNull() || this.lookUp.isCastable(leftType, rightType.getRef()))) {
            String target = this.typeCompiler.compile(leftType, c, false);
            StringConcatenation _builder_3 = new StringConcatenation();
            String _simpleName_5 = op.getSimpleName();
            _builder_3.append(_simpleName_5, "");
            _builder_3.append("((");
            _builder_3.append(target, "");
            _builder_3.append(")");
            IQLExpression _rightOperand_4 = e.getRightOperand();
            String _compile_3 = this.compile(_rightOperand_4, c);
            _builder_3.append(_compile_3, "");
            _builder_3.append(")");
            result = _builder_3.toString();
          } else {
            StringConcatenation _builder_4 = new StringConcatenation();
            String _simpleName_6 = op.getSimpleName();
            _builder_4.append(_simpleName_6, "");
            _builder_4.append("(");
            IQLExpression _rightOperand_5 = e.getRightOperand();
            String _compile_4 = this.compile(_rightOperand_5, c);
            _builder_4.append(_compile_4, "");
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
      IQLMemberSelection _sel = selExpr.getSel();
      JvmMember _member = _sel.getMember();
      EList<JvmFormalParameter> _parameters = ((JvmOperation) _member).getParameters();
      JvmFormalParameter _get = _parameters.get(0);
      JvmTypeReference leftType = _get.getParameterType();
      IQLExpression _rightOperand = e.getRightOperand();
      TypeResult rightType = this.exprEvaluator.eval(_rightOperand);
      c.setExpectedTypeRef(leftType);
      String result = "";
      IQLMemberSelection _sel_1 = selExpr.getSel();
      JvmMember _member_1 = _sel_1.getMember();
      JvmOperation op = ((JvmOperation) _member_1);
      EList<JvmTypeReference> _exceptions = op.getExceptions();
      c.addExceptions(_exceptions);
      if (((this.helper.isJvmArray(leftType) && (!rightType.isNull())) && (!this.helper.isJvmArray(rightType.getRef())))) {
        String _canonicalName = IQLUtils.class.getCanonicalName();
        c.addImport(_canonicalName);
        if ((this.helper.hasSystemTypeCompiler(op) && this.helper.getSystemTypeCompiler(op).compileMethodSelectionManually())) {
          IIQLSystemTypeCompiler systemTypeCompiler = this.helper.getSystemTypeCompiler(op);
          boolean _isPrimitiveArray = this.helper.isPrimitiveArray(leftType);
          if (_isPrimitiveArray) {
            StringConcatenation _builder = new StringConcatenation();
            IQLExpression _leftOperand = selExpr.getLeftOperand();
            String _compile = this.compile(_leftOperand, c);
            _builder.append(_compile, "");
            _builder.append(".");
            String _simpleName = IQLUtils.class.getSimpleName();
            String _plus = (_simpleName + ".");
            String _arrayMethodName = this.helper.getArrayMethodName(leftType);
            String _plus_1 = (_plus + _arrayMethodName);
            String _plus_2 = (_plus_1 + "(");
            IQLExpression _rightOperand_1 = e.getRightOperand();
            String _compile_1 = this.compile(_rightOperand_1, c);
            String _plus_3 = (_plus_2 + _compile_1);
            String _plus_4 = (_plus_3 + ")");
            List<String> _list = this.helper.toList(_plus_4);
            String _compileMethodSelection = systemTypeCompiler.compileMethodSelection(op, _list);
            _builder.append(_compileMethodSelection, "");
            result = _builder.toString();
          } else {
            JvmType _innerType = this.typeUtils.getInnerType(leftType, false);
            JvmTypeReference _createTypeRef = this.typeUtils.createTypeRef(_innerType);
            String clazz = this.typeCompiler.compile(_createTypeRef, c, true);
            StringConcatenation _builder_1 = new StringConcatenation();
            IQLExpression _leftOperand_1 = selExpr.getLeftOperand();
            String _compile_2 = this.compile(_leftOperand_1, c);
            _builder_1.append(_compile_2, "");
            _builder_1.append(".");
            String _simpleName_1 = IQLUtils.class.getSimpleName();
            String _plus_5 = (_simpleName_1 + ".");
            String _arrayMethodName_1 = this.helper.getArrayMethodName(leftType);
            String _plus_6 = (_plus_5 + _arrayMethodName_1);
            String _plus_7 = (_plus_6 + "(");
            String _plus_8 = (_plus_7 + clazz);
            String _plus_9 = (_plus_8 + ".class, ");
            IQLExpression _rightOperand_2 = e.getRightOperand();
            String _compile_3 = this.compile(_rightOperand_2, c);
            String _plus_10 = (_plus_9 + _compile_3);
            String _plus_11 = (_plus_10 + ")");
            List<String> _list_1 = this.helper.toList(_plus_11);
            String _compileMethodSelection_1 = systemTypeCompiler.compileMethodSelection(op, _list_1);
            _builder_1.append(_compileMethodSelection_1, "");
            result = _builder_1.toString();
          }
        } else {
          boolean _isPrimitiveArray_1 = this.helper.isPrimitiveArray(leftType);
          if (_isPrimitiveArray_1) {
            StringConcatenation _builder_2 = new StringConcatenation();
            IQLExpression _leftOperand_2 = selExpr.getLeftOperand();
            String _compile_4 = this.compile(_leftOperand_2, c);
            _builder_2.append(_compile_4, "");
            _builder_2.append(".");
            String _simpleName_2 = op.getSimpleName();
            _builder_2.append(_simpleName_2, "");
            _builder_2.append("(");
            String _simpleName_3 = IQLUtils.class.getSimpleName();
            _builder_2.append(_simpleName_3, "");
            _builder_2.append(".");
            String _arrayMethodName_2 = this.helper.getArrayMethodName(leftType);
            _builder_2.append(_arrayMethodName_2, "");
            _builder_2.append("(");
            IQLExpression _rightOperand_3 = e.getRightOperand();
            String _compile_5 = this.compile(_rightOperand_3, c);
            _builder_2.append(_compile_5, "");
            _builder_2.append("))");
            result = _builder_2.toString();
          } else {
            JvmType _innerType_1 = this.typeUtils.getInnerType(leftType, false);
            JvmTypeReference _createTypeRef_1 = this.typeUtils.createTypeRef(_innerType_1);
            String clazz_1 = this.typeCompiler.compile(_createTypeRef_1, c, true);
            StringConcatenation _builder_3 = new StringConcatenation();
            IQLExpression _leftOperand_3 = selExpr.getLeftOperand();
            String _compile_6 = this.compile(_leftOperand_3, c);
            _builder_3.append(_compile_6, "");
            _builder_3.append(".");
            String _simpleName_4 = op.getSimpleName();
            _builder_3.append(_simpleName_4, "");
            _builder_3.append("(");
            String _simpleName_5 = IQLUtils.class.getSimpleName();
            _builder_3.append(_simpleName_5, "");
            _builder_3.append(".");
            String _arrayMethodName_3 = this.helper.getArrayMethodName(leftType);
            _builder_3.append(_arrayMethodName_3, "");
            _builder_3.append("(");
            _builder_3.append(clazz_1, "");
            _builder_3.append(".class, ");
            IQLExpression _rightOperand_4 = e.getRightOperand();
            String _compile_7 = this.compile(_rightOperand_4, c);
            _builder_3.append(_compile_7, "");
            _builder_3.append("))");
            result = _builder_3.toString();
          }
        }
      } else {
        if ((rightType.isNull() || this.lookUp.isAssignable(leftType, rightType.getRef()))) {
          if ((this.helper.hasSystemTypeCompiler(op) && this.helper.getSystemTypeCompiler(op).compileMethodSelectionManually())) {
            IIQLSystemTypeCompiler systemTypeCompiler_1 = this.helper.getSystemTypeCompiler(op);
            StringConcatenation _builder_4 = new StringConcatenation();
            IQLExpression _leftOperand_4 = selExpr.getLeftOperand();
            String _compile_8 = this.compile(_leftOperand_4, c);
            _builder_4.append(_compile_8, "");
            _builder_4.append(".");
            IQLExpression _rightOperand_5 = e.getRightOperand();
            String _compile_9 = this.compile(_rightOperand_5, c);
            List<String> _list_2 = this.helper.toList(_compile_9);
            String _compileMethodSelection_2 = systemTypeCompiler_1.compileMethodSelection(op, _list_2);
            _builder_4.append(_compileMethodSelection_2, "");
            result = _builder_4.toString();
          } else {
            StringConcatenation _builder_5 = new StringConcatenation();
            IQLExpression _leftOperand_5 = selExpr.getLeftOperand();
            String _compile_10 = this.compile(_leftOperand_5, c);
            _builder_5.append(_compile_10, "");
            _builder_5.append(".");
            String _simpleName_6 = op.getSimpleName();
            _builder_5.append(_simpleName_6, "");
            _builder_5.append("(");
            IQLExpression _rightOperand_6 = e.getRightOperand();
            String _compile_11 = this.compile(_rightOperand_6, c);
            _builder_5.append(_compile_11, "");
            _builder_5.append(")");
            result = _builder_5.toString();
          }
        } else {
          if ((rightType.isNull() || this.lookUp.isCastable(leftType, rightType.getRef()))) {
            String target = this.typeCompiler.compile(leftType, c, false);
            if ((this.helper.hasSystemTypeCompiler(op) && this.helper.getSystemTypeCompiler(op).compileMethodSelectionManually())) {
              IIQLSystemTypeCompiler systemTypeCompiler_2 = this.helper.getSystemTypeCompiler(op);
              StringConcatenation _builder_6 = new StringConcatenation();
              IQLExpression _leftOperand_6 = selExpr.getLeftOperand();
              String _compile_12 = this.compile(_leftOperand_6, c);
              _builder_6.append(_compile_12, "");
              _builder_6.append(".");
              IQLExpression _rightOperand_7 = e.getRightOperand();
              String _compile_13 = this.compile(_rightOperand_7, c);
              String _plus_12 = ((("(" + target) + ")") + _compile_13);
              List<String> _list_3 = this.helper.toList(_plus_12);
              String _compileMethodSelection_3 = systemTypeCompiler_2.compileMethodSelection(op, _list_3);
              _builder_6.append(_compileMethodSelection_3, "");
              result = _builder_6.toString();
            } else {
              StringConcatenation _builder_7 = new StringConcatenation();
              IQLExpression _leftOperand_7 = selExpr.getLeftOperand();
              String _compile_14 = this.compile(_leftOperand_7, c);
              _builder_7.append(_compile_14, "");
              _builder_7.append(".");
              String _simpleName_7 = op.getSimpleName();
              _builder_7.append(_simpleName_7, "");
              _builder_7.append("((");
              _builder_7.append(target, "");
              _builder_7.append(")");
              IQLExpression _rightOperand_8 = e.getRightOperand();
              String _compile_15 = this.compile(_rightOperand_8, c);
              _builder_7.append(_compile_15, "");
              _builder_7.append(")");
              result = _builder_7.toString();
            }
          } else {
            if ((this.helper.hasSystemTypeCompiler(op) && this.helper.getSystemTypeCompiler(op).compileMethodSelectionManually())) {
              IIQLSystemTypeCompiler systemTypeCompiler_3 = this.helper.getSystemTypeCompiler(op);
              StringConcatenation _builder_8 = new StringConcatenation();
              IQLExpression _leftOperand_8 = selExpr.getLeftOperand();
              String _compile_16 = this.compile(_leftOperand_8, c);
              _builder_8.append(_compile_16, "");
              _builder_8.append(".");
              IQLExpression _rightOperand_9 = e.getRightOperand();
              String _compile_17 = this.compile(_rightOperand_9, c);
              List<String> _list_4 = this.helper.toList(_compile_17);
              String _compileMethodSelection_4 = systemTypeCompiler_3.compileMethodSelection(op, _list_4);
              _builder_8.append(_compileMethodSelection_4, "");
              result = _builder_8.toString();
            } else {
              StringConcatenation _builder_9 = new StringConcatenation();
              IQLExpression _leftOperand_9 = selExpr.getLeftOperand();
              String _compile_18 = this.compile(_leftOperand_9, c);
              _builder_9.append(_compile_18, "");
              _builder_9.append(".");
              String _simpleName_8 = op.getSimpleName();
              _builder_9.append(_simpleName_8, "");
              _builder_9.append("(");
              IQLExpression _rightOperand_10 = e.getRightOperand();
              String _compile_19 = this.compile(_rightOperand_10, c);
              _builder_9.append(_compile_19, "");
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
      IQLExpression _leftOperand = arrayExpr.getLeftOperand();
      TypeResult arrayType = this.exprEvaluator.eval(_leftOperand);
      String methodName = IQLOperatorOverloadingUtils.SET;
      String _xifexpression = null;
      if (((e.getOp().equals("=") && (!arrayType.isNull())) && this.typeExtensionsDictionary.hasTypeExtensions(arrayType.getRef(), methodName, this.helper.createSetterArguments(e.getRightOperand(), arrayExpr.getExpressions())))) {
        TypeResult leftType = this.exprEvaluator.eval(arrayExpr);
        IQLExpression _rightOperand = e.getRightOperand();
        TypeResult rightType = this.exprEvaluator.eval(_rightOperand);
        boolean _isNull = leftType.isNull();
        boolean _not = (!_isNull);
        if (_not) {
          JvmTypeReference _ref = leftType.getRef();
          c.setExpectedTypeRef(_ref);
        }
        String result = "";
        JvmTypeReference _ref_1 = arrayType.getRef();
        IQLExpression _rightOperand_1 = e.getRightOperand();
        EList<IQLExpression> _expressions = arrayExpr.getExpressions();
        List<IQLExpression> _createSetterArguments = this.helper.createSetterArguments(_rightOperand_1, _expressions);
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_ref_1, methodName, _createSetterArguments);
        Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
        String _canonicalName = _class.getCanonicalName();
        c.addImport(_canonicalName);
        if (((((!leftType.isNull()) && this.helper.isJvmArray(leftType.getRef())) && (!rightType.isNull())) && (!this.helper.isJvmArray(rightType.getRef())))) {
          String _canonicalName_1 = IQLUtils.class.getCanonicalName();
          c.addImport(_canonicalName_1);
          JvmTypeReference _ref_2 = leftType.getRef();
          boolean _isPrimitiveArray = this.helper.isPrimitiveArray(_ref_2);
          if (_isPrimitiveArray) {
            StringConcatenation _builder = new StringConcatenation();
            Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
            String _simpleName = _class_1.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append(".");
            _builder.append(methodName, "");
            _builder.append("(");
            IQLExpression _leftOperand_1 = arrayExpr.getLeftOperand();
            String _compile = this.compile(_leftOperand_1, c);
            _builder.append(_compile, "");
            _builder.append(", ");
            String _simpleName_1 = IQLUtils.class.getSimpleName();
            _builder.append(_simpleName_1, "");
            _builder.append(".");
            JvmTypeReference _ref_3 = leftType.getRef();
            String _arrayMethodName = this.helper.getArrayMethodName(_ref_3);
            _builder.append(_arrayMethodName, "");
            _builder.append("(");
            IQLExpression _rightOperand_2 = e.getRightOperand();
            String _compile_1 = this.compile(_rightOperand_2, c);
            _builder.append(_compile_1, "");
            _builder.append("), ");
            EList<IQLExpression> _expressions_1 = arrayExpr.getExpressions();
            final Function1<IQLExpression, String> _function = (IQLExpression el) -> {
              return this.compile(el, c);
            };
            List<String> _map = ListExtensions.<IQLExpression, String>map(_expressions_1, _function);
            String _join = IterableExtensions.join(_map, ", ");
            _builder.append(_join, "");
            _builder.append(")");
            result = _builder.toString();
          } else {
            JvmTypeReference _ref_4 = leftType.getRef();
            JvmType _innerType = this.typeUtils.getInnerType(_ref_4, false);
            JvmTypeReference _createTypeRef = this.typeUtils.createTypeRef(_innerType);
            String clazz = this.typeCompiler.compile(_createTypeRef, c, true);
            StringConcatenation _builder_1 = new StringConcatenation();
            Class<? extends IIQLTypeExtensions> _class_2 = typeOps.getClass();
            String _simpleName_2 = _class_2.getSimpleName();
            _builder_1.append(_simpleName_2, "");
            _builder_1.append(".");
            _builder_1.append(methodName, "");
            _builder_1.append("(");
            IQLExpression _leftOperand_2 = arrayExpr.getLeftOperand();
            String _compile_2 = this.compile(_leftOperand_2, c);
            _builder_1.append(_compile_2, "");
            _builder_1.append(", ");
            String _simpleName_3 = IQLUtils.class.getSimpleName();
            _builder_1.append(_simpleName_3, "");
            _builder_1.append(".");
            JvmTypeReference _ref_5 = leftType.getRef();
            String _arrayMethodName_1 = this.helper.getArrayMethodName(_ref_5);
            _builder_1.append(_arrayMethodName_1, "");
            _builder_1.append("(");
            _builder_1.append(clazz, "");
            _builder_1.append(".class, ");
            IQLExpression _rightOperand_3 = e.getRightOperand();
            String _compile_3 = this.compile(_rightOperand_3, c);
            _builder_1.append(_compile_3, "");
            _builder_1.append("), ");
            EList<IQLExpression> _expressions_2 = arrayExpr.getExpressions();
            final Function1<IQLExpression, String> _function_1 = (IQLExpression el) -> {
              return this.compile(el, c);
            };
            List<String> _map_1 = ListExtensions.<IQLExpression, String>map(_expressions_2, _function_1);
            String _join_1 = IterableExtensions.join(_map_1, ", ");
            _builder_1.append(_join_1, "");
            _builder_1.append(")");
            result = _builder_1.toString();
          }
        } else {
          if (((leftType.isNull() || rightType.isNull()) || this.lookUp.isAssignable(leftType.getRef(), rightType.getRef()))) {
            EList<IQLExpression> _expressions_3 = arrayExpr.getExpressions();
            int _size = _expressions_3.size();
            boolean _equals = (_size == 1);
            if (_equals) {
              StringConcatenation _builder_2 = new StringConcatenation();
              Class<? extends IIQLTypeExtensions> _class_3 = typeOps.getClass();
              String _simpleName_4 = _class_3.getSimpleName();
              _builder_2.append(_simpleName_4, "");
              _builder_2.append(".");
              _builder_2.append(methodName, "");
              _builder_2.append("(");
              IQLExpression _leftOperand_3 = arrayExpr.getLeftOperand();
              String _compile_4 = this.compile(_leftOperand_3, c);
              _builder_2.append(_compile_4, "");
              _builder_2.append(", ");
              IQLExpression _rightOperand_4 = e.getRightOperand();
              String _compile_5 = this.compile(_rightOperand_4, c);
              _builder_2.append(_compile_5, "");
              _builder_2.append(", ");
              EList<IQLExpression> _expressions_4 = arrayExpr.getExpressions();
              IQLExpression _get = _expressions_4.get(0);
              String _compile_6 = this.compile(_get, c);
              _builder_2.append(_compile_6, "");
              _builder_2.append(")");
              result = _builder_2.toString();
            } else {
              StringConcatenation _builder_3 = new StringConcatenation();
              Class<? extends IIQLTypeExtensions> _class_4 = typeOps.getClass();
              String _simpleName_5 = _class_4.getSimpleName();
              _builder_3.append(_simpleName_5, "");
              _builder_3.append(".");
              _builder_3.append(methodName, "");
              _builder_3.append("(");
              IQLExpression _leftOperand_4 = arrayExpr.getLeftOperand();
              String _compile_7 = this.compile(_leftOperand_4, c);
              _builder_3.append(_compile_7, "");
              _builder_3.append(", ");
              IQLExpression _rightOperand_5 = e.getRightOperand();
              String _compile_8 = this.compile(_rightOperand_5, c);
              _builder_3.append(_compile_8, "");
              _builder_3.append(", ");
              String _simpleName_6 = IQLUtils.class.getSimpleName();
              _builder_3.append(_simpleName_6, "");
              _builder_3.append(".createList(");
              EList<IQLExpression> _expressions_5 = arrayExpr.getExpressions();
              final Function1<IQLExpression, String> _function_2 = (IQLExpression el) -> {
                return this.compile(el, c);
              };
              List<String> _map_2 = ListExtensions.<IQLExpression, String>map(_expressions_5, _function_2);
              String _join_2 = IterableExtensions.join(_map_2, ", ");
              _builder_3.append(_join_2, "");
              _builder_3.append("))");
              result = _builder_3.toString();
            }
          } else {
            if (((leftType.isNull() || rightType.isNull()) || this.lookUp.isCastable(leftType.getRef(), rightType.getRef()))) {
              JvmTypeReference _ref_6 = leftType.getRef();
              String target = this.typeCompiler.compile(_ref_6, c, false);
              EList<IQLExpression> _expressions_6 = arrayExpr.getExpressions();
              int _size_1 = _expressions_6.size();
              boolean _equals_1 = (_size_1 == 1);
              if (_equals_1) {
                StringConcatenation _builder_4 = new StringConcatenation();
                Class<? extends IIQLTypeExtensions> _class_5 = typeOps.getClass();
                String _simpleName_7 = _class_5.getSimpleName();
                _builder_4.append(_simpleName_7, "");
                _builder_4.append(".");
                _builder_4.append(methodName, "");
                _builder_4.append("(");
                IQLExpression _leftOperand_5 = arrayExpr.getLeftOperand();
                String _compile_9 = this.compile(_leftOperand_5, c);
                _builder_4.append(_compile_9, "");
                _builder_4.append(", ((");
                _builder_4.append(target, "");
                _builder_4.append(")");
                IQLExpression _rightOperand_6 = e.getRightOperand();
                String _compile_10 = this.compile(_rightOperand_6, c);
                _builder_4.append(_compile_10, "");
                _builder_4.append("), ");
                EList<IQLExpression> _expressions_7 = arrayExpr.getExpressions();
                IQLExpression _get_1 = _expressions_7.get(0);
                String _compile_11 = this.compile(_get_1, c);
                _builder_4.append(_compile_11, "");
                _builder_4.append(")");
                result = _builder_4.toString();
              } else {
                StringConcatenation _builder_5 = new StringConcatenation();
                Class<? extends IIQLTypeExtensions> _class_6 = typeOps.getClass();
                String _simpleName_8 = _class_6.getSimpleName();
                _builder_5.append(_simpleName_8, "");
                _builder_5.append(".");
                _builder_5.append(methodName, "");
                _builder_5.append("(");
                IQLExpression _leftOperand_6 = arrayExpr.getLeftOperand();
                String _compile_12 = this.compile(_leftOperand_6, c);
                _builder_5.append(_compile_12, "");
                _builder_5.append(", ((");
                _builder_5.append(target, "");
                _builder_5.append(")");
                IQLExpression _rightOperand_7 = e.getRightOperand();
                String _compile_13 = this.compile(_rightOperand_7, c);
                _builder_5.append(_compile_13, "");
                _builder_5.append("), ");
                String _simpleName_9 = IQLUtils.class.getSimpleName();
                _builder_5.append(_simpleName_9, "");
                _builder_5.append(".createList(");
                EList<IQLExpression> _expressions_8 = arrayExpr.getExpressions();
                final Function1<IQLExpression, String> _function_3 = (IQLExpression el) -> {
                  return this.compile(el, c);
                };
                List<String> _map_3 = ListExtensions.<IQLExpression, String>map(_expressions_8, _function_3);
                String _join_3 = IterableExtensions.join(_map_3, ", ");
                _builder_5.append(_join_3, "");
                _builder_5.append("))");
                result = _builder_5.toString();
              }
            } else {
              EList<IQLExpression> _expressions_9 = arrayExpr.getExpressions();
              int _size_2 = _expressions_9.size();
              boolean _equals_2 = (_size_2 == 1);
              if (_equals_2) {
                StringConcatenation _builder_6 = new StringConcatenation();
                Class<? extends IIQLTypeExtensions> _class_7 = typeOps.getClass();
                String _simpleName_10 = _class_7.getSimpleName();
                _builder_6.append(_simpleName_10, "");
                _builder_6.append(".");
                _builder_6.append(methodName, "");
                _builder_6.append("(");
                IQLExpression _leftOperand_7 = arrayExpr.getLeftOperand();
                String _compile_14 = this.compile(_leftOperand_7, c);
                _builder_6.append(_compile_14, "");
                _builder_6.append(", ");
                IQLExpression _rightOperand_8 = e.getRightOperand();
                String _compile_15 = this.compile(_rightOperand_8, c);
                _builder_6.append(_compile_15, "");
                _builder_6.append(", ");
                EList<IQLExpression> _expressions_10 = arrayExpr.getExpressions();
                IQLExpression _get_2 = _expressions_10.get(0);
                String _compile_16 = this.compile(_get_2, c);
                _builder_6.append(_compile_16, "");
                _builder_6.append(")");
                result = _builder_6.toString();
              } else {
                StringConcatenation _builder_7 = new StringConcatenation();
                Class<? extends IIQLTypeExtensions> _class_8 = typeOps.getClass();
                String _simpleName_11 = _class_8.getSimpleName();
                _builder_7.append(_simpleName_11, "");
                _builder_7.append(".");
                _builder_7.append(methodName, "");
                _builder_7.append("(");
                IQLExpression _leftOperand_8 = arrayExpr.getLeftOperand();
                String _compile_17 = this.compile(_leftOperand_8, c);
                _builder_7.append(_compile_17, "");
                _builder_7.append(", ");
                IQLExpression _rightOperand_9 = e.getRightOperand();
                String _compile_18 = this.compile(_rightOperand_9, c);
                _builder_7.append(_compile_18, "");
                _builder_7.append(", ");
                String _simpleName_12 = IQLUtils.class.getSimpleName();
                _builder_7.append(_simpleName_12, "");
                _builder_7.append(".createList(");
                EList<IQLExpression> _expressions_11 = arrayExpr.getExpressions();
                final Function1<IQLExpression, String> _function_4 = (IQLExpression el) -> {
                  return this.compile(el, c);
                };
                List<String> _map_4 = ListExtensions.<IQLExpression, String>map(_expressions_11, _function_4);
                String _join_4 = IterableExtensions.join(_map_4, ", ");
                _builder_7.append(_join_4, "");
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
          IQLExpression _rightOperand_10 = e.getRightOperand();
          TypeResult rightType_1 = this.exprEvaluator.eval(_rightOperand_10);
          boolean _isNull_1 = leftType_1.isNull();
          boolean _not_1 = (!_isNull_1);
          if (_not_1) {
            JvmTypeReference _ref_7 = leftType_1.getRef();
            c.setExpectedTypeRef(_ref_7);
          }
          String result_1 = "";
          if (((((!leftType_1.isNull()) && this.helper.isJvmArray(leftType_1.getRef())) && (!rightType_1.isNull())) && (!this.helper.isJvmArray(rightType_1.getRef())))) {
            String _canonicalName_2 = IQLUtils.class.getCanonicalName();
            c.addImport(_canonicalName_2);
            JvmTypeReference _ref_8 = leftType_1.getRef();
            boolean _isPrimitiveArray_1 = this.helper.isPrimitiveArray(_ref_8);
            if (_isPrimitiveArray_1) {
              StringConcatenation _builder_8 = new StringConcatenation();
              String _simpleName_13 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
              _builder_8.append(_simpleName_13, "");
              _builder_8.append(".");
              _builder_8.append(methodName, "");
              _builder_8.append("(");
              IQLExpression _leftOperand_9 = arrayExpr.getLeftOperand();
              String _compile_19 = this.compile(_leftOperand_9, c);
              _builder_8.append(_compile_19, "");
              _builder_8.append(", ");
              String _simpleName_14 = IQLUtils.class.getSimpleName();
              _builder_8.append(_simpleName_14, "");
              _builder_8.append(".");
              JvmTypeReference _ref_9 = leftType_1.getRef();
              String _arrayMethodName_2 = this.helper.getArrayMethodName(_ref_9);
              _builder_8.append(_arrayMethodName_2, "");
              _builder_8.append("(");
              IQLExpression _rightOperand_11 = e.getRightOperand();
              String _compile_20 = this.compile(_rightOperand_11, c);
              _builder_8.append(_compile_20, "");
              _builder_8.append("), ");
              EList<IQLExpression> _expressions_12 = arrayExpr.getExpressions();
              final Function1<IQLExpression, String> _function_5 = (IQLExpression el) -> {
                return this.compile(el, c);
              };
              List<String> _map_5 = ListExtensions.<IQLExpression, String>map(_expressions_12, _function_5);
              String _join_5 = IterableExtensions.join(_map_5, ", ");
              _builder_8.append(_join_5, "");
              _builder_8.append(")");
              result_1 = _builder_8.toString();
            } else {
              JvmTypeReference _ref_10 = leftType_1.getRef();
              JvmType _innerType_1 = this.typeUtils.getInnerType(_ref_10, false);
              JvmTypeReference _createTypeRef_1 = this.typeUtils.createTypeRef(_innerType_1);
              String clazz_1 = this.typeCompiler.compile(_createTypeRef_1, c, true);
              StringConcatenation _builder_9 = new StringConcatenation();
              String _simpleName_15 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
              _builder_9.append(_simpleName_15, "");
              _builder_9.append(".");
              _builder_9.append(methodName, "");
              _builder_9.append("(");
              IQLExpression _leftOperand_10 = arrayExpr.getLeftOperand();
              String _compile_21 = this.compile(_leftOperand_10, c);
              _builder_9.append(_compile_21, "");
              _builder_9.append(", ");
              String _simpleName_16 = IQLUtils.class.getSimpleName();
              _builder_9.append(_simpleName_16, "");
              _builder_9.append(".");
              JvmTypeReference _ref_11 = leftType_1.getRef();
              String _arrayMethodName_3 = this.helper.getArrayMethodName(_ref_11);
              _builder_9.append(_arrayMethodName_3, "");
              _builder_9.append("(");
              _builder_9.append(clazz_1, "");
              _builder_9.append(".class, ");
              IQLExpression _rightOperand_12 = e.getRightOperand();
              String _compile_22 = this.compile(_rightOperand_12, c);
              _builder_9.append(_compile_22, "");
              _builder_9.append("), ");
              EList<IQLExpression> _expressions_13 = arrayExpr.getExpressions();
              final Function1<IQLExpression, String> _function_6 = (IQLExpression el) -> {
                return this.compile(el, c);
              };
              List<String> _map_6 = ListExtensions.<IQLExpression, String>map(_expressions_13, _function_6);
              String _join_6 = IterableExtensions.join(_map_6, ", ");
              _builder_9.append(_join_6, "");
              _builder_9.append(")");
              result_1 = _builder_9.toString();
            }
          } else {
            if (((leftType_1.isNull() || rightType_1.isNull()) || this.lookUp.isAssignable(leftType_1.getRef(), rightType_1.getRef()))) {
              String _canonicalName_3 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getCanonicalName();
              c.addImport(_canonicalName_3);
              StringConcatenation _builder_10 = new StringConcatenation();
              String _simpleName_17 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
              _builder_10.append(_simpleName_17, "");
              _builder_10.append(".");
              _builder_10.append(methodName, "");
              _builder_10.append("(");
              IQLExpression _leftOperand_11 = arrayExpr.getLeftOperand();
              String _compile_23 = this.compile(_leftOperand_11, c);
              _builder_10.append(_compile_23, "");
              _builder_10.append(", ");
              IQLExpression _rightOperand_13 = e.getRightOperand();
              String _compile_24 = this.compile(_rightOperand_13, c);
              _builder_10.append(_compile_24, "");
              _builder_10.append(", ");
              EList<IQLExpression> _expressions_14 = arrayExpr.getExpressions();
              final Function1<IQLExpression, String> _function_7 = (IQLExpression el) -> {
                return this.compile(el, c);
              };
              List<String> _map_7 = ListExtensions.<IQLExpression, String>map(_expressions_14, _function_7);
              String _join_7 = IterableExtensions.join(_map_7, ", ");
              _builder_10.append(_join_7, "");
              _builder_10.append(")");
              result_1 = _builder_10.toString();
            } else {
              if (((leftType_1.isNull() || rightType_1.isNull()) || this.lookUp.isCastable(leftType_1.getRef(), rightType_1.getRef()))) {
                String _canonicalName_4 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getCanonicalName();
                c.addImport(_canonicalName_4);
                JvmTypeReference _ref_12 = leftType_1.getRef();
                String target_1 = this.typeCompiler.compile(_ref_12, c, false);
                StringConcatenation _builder_11 = new StringConcatenation();
                String _simpleName_18 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
                _builder_11.append(_simpleName_18, "");
                _builder_11.append(".");
                _builder_11.append(methodName, "");
                _builder_11.append("(");
                IQLExpression _leftOperand_12 = arrayExpr.getLeftOperand();
                String _compile_25 = this.compile(_leftOperand_12, c);
                _builder_11.append(_compile_25, "");
                _builder_11.append(", ((");
                _builder_11.append(target_1, "");
                _builder_11.append(")");
                IQLExpression _rightOperand_14 = e.getRightOperand();
                String _compile_26 = this.compile(_rightOperand_14, c);
                _builder_11.append(_compile_26, "");
                _builder_11.append("), ");
                EList<IQLExpression> _expressions_15 = arrayExpr.getExpressions();
                final Function1<IQLExpression, String> _function_8 = (IQLExpression el) -> {
                  return this.compile(el, c);
                };
                List<String> _map_8 = ListExtensions.<IQLExpression, String>map(_expressions_15, _function_8);
                String _join_8 = IterableExtensions.join(_map_8, ", ");
                _builder_11.append(_join_8, "");
                _builder_11.append(")");
                result_1 = _builder_11.toString();
              } else {
                String _canonicalName_5 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getCanonicalName();
                c.addImport(_canonicalName_5);
                StringConcatenation _builder_12 = new StringConcatenation();
                String _simpleName_19 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
                _builder_12.append(_simpleName_19, "");
                _builder_12.append(".");
                _builder_12.append(methodName, "");
                _builder_12.append("(");
                IQLExpression _leftOperand_13 = arrayExpr.getLeftOperand();
                String _compile_27 = this.compile(_leftOperand_13, c);
                _builder_12.append(_compile_27, "");
                _builder_12.append(", ");
                IQLExpression _rightOperand_15 = e.getRightOperand();
                String _compile_28 = this.compile(_rightOperand_15, c);
                _builder_12.append(_compile_28, "");
                _builder_12.append(", ");
                EList<IQLExpression> _expressions_16 = arrayExpr.getExpressions();
                final Function1<IQLExpression, String> _function_9 = (IQLExpression el) -> {
                  return this.compile(el, c);
                };
                List<String> _map_9 = ListExtensions.<IQLExpression, String>map(_expressions_16, _function_9);
                String _join_9 = IterableExtensions.join(_map_9, ", ");
                _builder_12.append(_join_9, "");
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
    IQLExpression _leftOperand = e.getLeftOperand();
    TypeResult leftType = this.exprEvaluator.eval(_leftOperand);
    String _op = e.getOp();
    boolean _equals = _op.equals("=");
    if (_equals) {
      IQLExpression _rightOperand = e.getRightOperand();
      TypeResult rightType = this.exprEvaluator.eval(_rightOperand);
      boolean _isNull = leftType.isNull();
      boolean _not = (!_isNull);
      if (_not) {
        JvmTypeReference _ref = leftType.getRef();
        c.setExpectedTypeRef(_ref);
      }
      String result = "";
      if (((((!leftType.isNull()) && this.helper.isJvmArray(leftType.getRef())) && (!rightType.isNull())) && (!this.helper.isJvmArray(rightType.getRef())))) {
        String _canonicalName = IQLUtils.class.getCanonicalName();
        c.addImport(_canonicalName);
        JvmTypeReference _ref_1 = leftType.getRef();
        boolean _isPrimitiveArray = this.helper.isPrimitiveArray(_ref_1);
        if (_isPrimitiveArray) {
          StringConcatenation _builder = new StringConcatenation();
          IQLExpression _leftOperand_1 = e.getLeftOperand();
          String _compile = this.compile(_leftOperand_1, c);
          _builder.append(_compile, "");
          _builder.append(" ");
          String _op_1 = e.getOp();
          _builder.append(_op_1, "");
          _builder.append(" ");
          String _simpleName = IQLUtils.class.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append(".");
          JvmTypeReference _ref_2 = leftType.getRef();
          String _arrayMethodName = this.helper.getArrayMethodName(_ref_2);
          _builder.append(_arrayMethodName, "");
          _builder.append("(");
          IQLExpression _rightOperand_1 = e.getRightOperand();
          String _compile_1 = this.compile(_rightOperand_1, c);
          _builder.append(_compile_1, "");
          _builder.append("))");
          result = _builder.toString();
        } else {
          JvmTypeReference _ref_3 = leftType.getRef();
          JvmType _innerType = this.typeUtils.getInnerType(_ref_3, false);
          JvmTypeReference _createTypeRef = this.typeUtils.createTypeRef(_innerType);
          String clazz = this.typeCompiler.compile(_createTypeRef, c, true);
          StringConcatenation _builder_1 = new StringConcatenation();
          IQLExpression _leftOperand_2 = e.getLeftOperand();
          String _compile_2 = this.compile(_leftOperand_2, c);
          _builder_1.append(_compile_2, "");
          _builder_1.append(" ");
          String _op_2 = e.getOp();
          _builder_1.append(_op_2, "");
          _builder_1.append(" ");
          String _simpleName_1 = IQLUtils.class.getSimpleName();
          _builder_1.append(_simpleName_1, "");
          _builder_1.append(".");
          JvmTypeReference _ref_4 = leftType.getRef();
          String _arrayMethodName_1 = this.helper.getArrayMethodName(_ref_4);
          _builder_1.append(_arrayMethodName_1, "");
          _builder_1.append("(");
          _builder_1.append(clazz, "");
          _builder_1.append(".class, ");
          IQLExpression _rightOperand_2 = e.getRightOperand();
          String _compile_3 = this.compile(_rightOperand_2, c);
          _builder_1.append(_compile_3, "");
          _builder_1.append("))");
          result = _builder_1.toString();
        }
      } else {
        if (((leftType.isNull() || rightType.isNull()) || this.lookUp.isAssignable(leftType.getRef(), rightType.getRef()))) {
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
        } else {
          if (((leftType.isNull() || rightType.isNull()) || this.lookUp.isCastable(leftType.getRef(), rightType.getRef()))) {
            JvmTypeReference _ref_5 = leftType.getRef();
            String target = this.typeCompiler.compile(_ref_5, c, false);
            StringConcatenation _builder_3 = new StringConcatenation();
            IQLExpression _leftOperand_4 = e.getLeftOperand();
            String _compile_6 = this.compile(_leftOperand_4, c);
            _builder_3.append(_compile_6, "");
            _builder_3.append(" ");
            String _op_4 = e.getOp();
            _builder_3.append(_op_4, "");
            _builder_3.append(" ((");
            _builder_3.append(target, "");
            _builder_3.append(") ");
            IQLExpression _rightOperand_4 = e.getRightOperand();
            String _compile_7 = this.compile(_rightOperand_4, c);
            _builder_3.append(_compile_7, "");
            _builder_3.append(")");
            result = _builder_3.toString();
          } else {
            StringConcatenation _builder_4 = new StringConcatenation();
            IQLExpression _leftOperand_5 = e.getLeftOperand();
            String _compile_8 = this.compile(_leftOperand_5, c);
            _builder_4.append(_compile_8, "");
            _builder_4.append(" ");
            String _op_5 = e.getOp();
            _builder_4.append(_op_5, "");
            _builder_4.append(" ");
            IQLExpression _rightOperand_5 = e.getRightOperand();
            String _compile_9 = this.compile(_rightOperand_5, c);
            _builder_4.append(_compile_9, "");
            result = _builder_4.toString();
          }
        }
      }
      c.setExpectedTypeRef(null);
      return result;
    } else {
      if (((!leftType.isNull()) && e.getOp().equals("+="))) {
        JvmTypeReference _ref_6 = leftType.getRef();
        IQLExpression _leftOperand_6 = e.getLeftOperand();
        IQLExpression _rightOperand_6 = e.getRightOperand();
        return this.compileOperatorOverloading("+", IQLOperatorOverloadingUtils.PLUS, _ref_6, _leftOperand_6, _rightOperand_6, c);
      } else {
        if (((!leftType.isNull()) && e.getOp().equals("-="))) {
          JvmTypeReference _ref_7 = leftType.getRef();
          IQLExpression _leftOperand_7 = e.getLeftOperand();
          IQLExpression _rightOperand_7 = e.getRightOperand();
          return this.compileOperatorOverloading("-", IQLOperatorOverloadingUtils.MINUS, _ref_7, _leftOperand_7, _rightOperand_7, c);
        } else {
          if (((!leftType.isNull()) && e.getOp().equals("*="))) {
            JvmTypeReference _ref_8 = leftType.getRef();
            IQLExpression _leftOperand_8 = e.getLeftOperand();
            IQLExpression _rightOperand_8 = e.getRightOperand();
            return this.compileOperatorOverloading("*", IQLOperatorOverloadingUtils.MULTIPLY, _ref_8, _leftOperand_8, _rightOperand_8, c);
          } else {
            if (((!leftType.isNull()) && e.getOp().equals("/="))) {
              JvmTypeReference _ref_9 = leftType.getRef();
              IQLExpression _leftOperand_9 = e.getLeftOperand();
              IQLExpression _rightOperand_9 = e.getRightOperand();
              return this.compileOperatorOverloading("/", IQLOperatorOverloadingUtils.DIVIDE, _ref_9, _leftOperand_9, _rightOperand_9, c);
            } else {
              if (((!leftType.isNull()) && e.getOp().equals("%="))) {
                JvmTypeReference _ref_10 = leftType.getRef();
                IQLExpression _leftOperand_10 = e.getLeftOperand();
                IQLExpression _rightOperand_10 = e.getRightOperand();
                return this.compileOperatorOverloading("%", IQLOperatorOverloadingUtils.MODULO, _ref_10, _leftOperand_10, _rightOperand_10, c);
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
    IQLExpression _leftOperand = e.getLeftOperand();
    TypeResult left = this.exprEvaluator.eval(_leftOperand);
    String result = "";
    if (((!left.isNull()) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.LOGICAL_OR, e.getRightOperand()))) {
      String _op = e.getOp();
      JvmTypeReference _ref = left.getRef();
      IQLExpression _leftOperand_1 = e.getLeftOperand();
      IQLExpression _rightOperand = e.getRightOperand();
      String _compileOperatorOverloading = this.compileOperatorOverloading(_op, IQLOperatorOverloadingUtils.LOGICAL_OR, _ref, _leftOperand_1, _rightOperand, c);
      result = _compileOperatorOverloading;
    } else {
      StringConcatenation _builder = new StringConcatenation();
      IQLExpression _leftOperand_2 = e.getLeftOperand();
      String _compile = this.compile(_leftOperand_2, c);
      _builder.append(_compile, "");
      _builder.append(" ");
      String _op_1 = e.getOp();
      _builder.append(_op_1, "");
      _builder.append(" ");
      IQLExpression _rightOperand_1 = e.getRightOperand();
      String _compile_1 = this.compile(_rightOperand_1, c);
      _builder.append(_compile_1, "");
      result = _builder.toString();
    }
    return result;
  }
  
  public String compile(final IQLLogicalAndExpression e, final G c) {
    IQLExpression _leftOperand = e.getLeftOperand();
    TypeResult left = this.exprEvaluator.eval(_leftOperand);
    String result = "";
    if (((!left.isNull()) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.LOGICAL_AND, e.getRightOperand()))) {
      String _op = e.getOp();
      JvmTypeReference _ref = left.getRef();
      IQLExpression _leftOperand_1 = e.getLeftOperand();
      IQLExpression _rightOperand = e.getRightOperand();
      String _compileOperatorOverloading = this.compileOperatorOverloading(_op, IQLOperatorOverloadingUtils.LOGICAL_AND, _ref, _leftOperand_1, _rightOperand, c);
      result = _compileOperatorOverloading;
    } else {
      StringConcatenation _builder = new StringConcatenation();
      IQLExpression _leftOperand_2 = e.getLeftOperand();
      String _compile = this.compile(_leftOperand_2, c);
      _builder.append(_compile, "");
      _builder.append(" ");
      String _op_1 = e.getOp();
      _builder.append(_op_1, "");
      _builder.append(" ");
      IQLExpression _rightOperand_1 = e.getRightOperand();
      String _compile_1 = this.compile(_rightOperand_1, c);
      _builder.append(_compile_1, "");
      result = _builder.toString();
    }
    return result;
  }
  
  public String compile(final IQLEqualityExpression e, final G c) {
    IQLExpression _leftOperand = e.getLeftOperand();
    TypeResult left = this.exprEvaluator.eval(_leftOperand);
    String result = "";
    if ((((!left.isNull()) && e.getOp().equals("==")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.EQUALS, e.getRightOperand()))) {
      JvmTypeReference _ref = left.getRef();
      IQLExpression _leftOperand_1 = e.getLeftOperand();
      IQLExpression _rightOperand = e.getRightOperand();
      String _compileOperatorOverloading = this.compileOperatorOverloading("==", IQLOperatorOverloadingUtils.EQUALS, _ref, _leftOperand_1, _rightOperand, c);
      result = _compileOperatorOverloading;
    } else {
      if ((((!left.isNull()) && e.getOp().equals("!==")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.EQUALS_NOT, e.getRightOperand()))) {
        JvmTypeReference _ref_1 = left.getRef();
        IQLExpression _leftOperand_2 = e.getLeftOperand();
        IQLExpression _rightOperand_1 = e.getRightOperand();
        String _compileOperatorOverloading_1 = this.compileOperatorOverloading("!==", IQLOperatorOverloadingUtils.EQUALS_NOT, _ref_1, _leftOperand_2, _rightOperand_1, c);
        result = _compileOperatorOverloading_1;
      } else {
        boolean _isNull = left.isNull();
        boolean _not = (!_isNull);
        if (_not) {
          JvmTypeReference _ref_2 = left.getRef();
          c.setExpectedTypeRef(_ref_2);
        }
        StringConcatenation _builder = new StringConcatenation();
        IQLExpression _leftOperand_3 = e.getLeftOperand();
        String _compile = this.compile(_leftOperand_3, c);
        _builder.append(_compile, "");
        _builder.append(" ");
        String _op = e.getOp();
        _builder.append(_op, "");
        _builder.append(" ");
        IQLExpression _rightOperand_2 = e.getRightOperand();
        String _compile_1 = this.compile(_rightOperand_2, c);
        _builder.append(_compile_1, "");
        result = _builder.toString();
      }
    }
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLRelationalExpression e, final G c) {
    IQLExpression _leftOperand = e.getLeftOperand();
    TypeResult left = this.exprEvaluator.eval(_leftOperand);
    String result = "";
    if ((((!left.isNull()) && e.getOp().equals(">")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.GREATER_THAN, e.getRightOperand()))) {
      JvmTypeReference _ref = left.getRef();
      IQLExpression _leftOperand_1 = e.getLeftOperand();
      IQLExpression _rightOperand = e.getRightOperand();
      String _compileOperatorOverloading = this.compileOperatorOverloading(">", IQLOperatorOverloadingUtils.GREATER_THAN, _ref, _leftOperand_1, _rightOperand, c);
      result = _compileOperatorOverloading;
    } else {
      if ((((!left.isNull()) && e.getOp().equals("<")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.LESS_THAN, e.getRightOperand()))) {
        JvmTypeReference _ref_1 = left.getRef();
        IQLExpression _leftOperand_2 = e.getLeftOperand();
        IQLExpression _rightOperand_1 = e.getRightOperand();
        String _compileOperatorOverloading_1 = this.compileOperatorOverloading("<", IQLOperatorOverloadingUtils.LESS_THAN, _ref_1, _leftOperand_2, _rightOperand_1, c);
        result = _compileOperatorOverloading_1;
      } else {
        if ((((!left.isNull()) && e.getOp().equals(">=")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.GREATER_EQUALS_THAN, e.getRightOperand()))) {
          JvmTypeReference _ref_2 = left.getRef();
          IQLExpression _leftOperand_3 = e.getLeftOperand();
          IQLExpression _rightOperand_2 = e.getRightOperand();
          String _compileOperatorOverloading_2 = this.compileOperatorOverloading(">=", IQLOperatorOverloadingUtils.GREATER_EQUALS_THAN, _ref_2, _leftOperand_3, _rightOperand_2, c);
          result = _compileOperatorOverloading_2;
        } else {
          if ((((!left.isNull()) && e.getOp().equals("<=")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.LESS_EQUALS_THAN, e.getRightOperand()))) {
            JvmTypeReference _ref_3 = left.getRef();
            IQLExpression _leftOperand_4 = e.getLeftOperand();
            IQLExpression _rightOperand_3 = e.getRightOperand();
            String _compileOperatorOverloading_3 = this.compileOperatorOverloading("<=", IQLOperatorOverloadingUtils.LESS_EQUALS_THAN, _ref_3, _leftOperand_4, _rightOperand_3, c);
            result = _compileOperatorOverloading_3;
          } else {
            boolean _isNull = left.isNull();
            boolean _not = (!_isNull);
            if (_not) {
              JvmTypeReference _ref_4 = left.getRef();
              c.setExpectedTypeRef(_ref_4);
            }
            StringConcatenation _builder = new StringConcatenation();
            IQLExpression _leftOperand_5 = e.getLeftOperand();
            String _compile = this.compile(_leftOperand_5, c);
            _builder.append(_compile, "");
            _builder.append(" ");
            String _op = e.getOp();
            _builder.append(_op, "");
            _builder.append(" ");
            IQLExpression _rightOperand_4 = e.getRightOperand();
            String _compile_1 = this.compile(_rightOperand_4, c);
            _builder.append(_compile_1, "");
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
    TypeResult rightType = this.exprEvaluator.eval(rightOperand);
    IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(left, operatorName, rightOperand);
    Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
    String _canonicalName = _class.getCanonicalName();
    c.addImport(_canonicalName);
    JvmOperation _method = this.typeExtensionsDictionary.getMethod(left, operatorName, rightOperand);
    EList<JvmFormalParameter> _parameters = _method.getParameters();
    JvmFormalParameter _get = _parameters.get(0);
    JvmTypeReference targetType = _get.getParameterType();
    c.setExpectedTypeRef(targetType);
    String result = "";
    if (((((targetType != null) && this.helper.isJvmArray(targetType)) && (!rightType.isNull())) && (!this.helper.isJvmArray(rightType.getRef())))) {
      String _canonicalName_1 = IQLUtils.class.getCanonicalName();
      c.addImport(_canonicalName_1);
      boolean _isPrimitiveArray = this.helper.isPrimitiveArray(targetType);
      if (_isPrimitiveArray) {
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
        String _simpleName_1 = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName_1, "");
        _builder.append(".");
        String _arrayMethodName = this.helper.getArrayMethodName(targetType);
        _builder.append(_arrayMethodName, "");
        _builder.append("(");
        String _compile_1 = this.compile(rightOperand, c);
        _builder.append(_compile_1, "");
        _builder.append("))");
        result = _builder.toString();
      } else {
        JvmType _innerType = this.typeUtils.getInnerType(targetType, false);
        JvmTypeReference _createTypeRef = this.typeUtils.createTypeRef(_innerType);
        String clazz = this.typeCompiler.compile(_createTypeRef, c, true);
        StringConcatenation _builder_1 = new StringConcatenation();
        Class<? extends IIQLTypeExtensions> _class_2 = typeOps.getClass();
        String _simpleName_2 = _class_2.getSimpleName();
        _builder_1.append(_simpleName_2, "");
        _builder_1.append(".");
        _builder_1.append(operatorName, "");
        _builder_1.append("(");
        String _compile_2 = this.compile(leftOperand, c);
        _builder_1.append(_compile_2, "");
        _builder_1.append(", ");
        String _simpleName_3 = IQLUtils.class.getSimpleName();
        _builder_1.append(_simpleName_3, "");
        _builder_1.append(".");
        String _arrayMethodName_1 = this.helper.getArrayMethodName(targetType);
        _builder_1.append(_arrayMethodName_1, "");
        _builder_1.append("(");
        _builder_1.append(clazz, "");
        _builder_1.append(".class, ");
        String _compile_3 = this.compile(rightOperand, c);
        _builder_1.append(_compile_3, "");
        _builder_1.append("))");
        result = _builder_1.toString();
      }
    } else {
      if (((!rightType.isNull()) && this.lookUp.isAssignable(targetType, rightType.getRef()))) {
        StringConcatenation _builder_2 = new StringConcatenation();
        Class<? extends IIQLTypeExtensions> _class_3 = typeOps.getClass();
        String _simpleName_4 = _class_3.getSimpleName();
        _builder_2.append(_simpleName_4, "");
        _builder_2.append(".");
        _builder_2.append(operatorName, "");
        _builder_2.append("(");
        String _compile_4 = this.compile(leftOperand, c);
        _builder_2.append(_compile_4, "");
        _builder_2.append(", ");
        String _compile_5 = this.compile(rightOperand, c);
        _builder_2.append(_compile_5, "");
        _builder_2.append(")");
        result = _builder_2.toString();
      } else {
        if ((rightType.isNull() || this.lookUp.isCastable(targetType, rightType.getRef()))) {
          String target = this.typeCompiler.compile(targetType, c, false);
          StringConcatenation _builder_3 = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_4 = typeOps.getClass();
          String _simpleName_5 = _class_4.getSimpleName();
          _builder_3.append(_simpleName_5, "");
          _builder_3.append(".");
          _builder_3.append(operatorName, "");
          _builder_3.append("(");
          String _compile_6 = this.compile(leftOperand, c);
          _builder_3.append(_compile_6, "");
          _builder_3.append(", ((");
          _builder_3.append(target, "");
          _builder_3.append(")");
          String _compile_7 = this.compile(rightOperand, c);
          _builder_3.append(_compile_7, "");
          _builder_3.append("))");
          result = _builder_3.toString();
        } else {
          StringConcatenation _builder_4 = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_5 = typeOps.getClass();
          String _simpleName_6 = _class_5.getSimpleName();
          _builder_4.append(_simpleName_6, "");
          _builder_4.append(".");
          _builder_4.append(operatorName, "");
          _builder_4.append("(");
          String _compile_8 = this.compile(leftOperand, c);
          _builder_4.append(_compile_8, "");
          _builder_4.append(", ");
          String _compile_9 = this.compile(rightOperand, c);
          _builder_4.append(_compile_9, "");
          _builder_4.append(")");
          result = _builder_4.toString();
        }
      }
    }
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLAdditiveExpression e, final G c) {
    IQLExpression _leftOperand = e.getLeftOperand();
    TypeResult left = this.exprEvaluator.eval(_leftOperand);
    String result = "";
    if ((((!left.isNull()) && e.getOp().equals("+")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.PLUS, e.getRightOperand()))) {
      JvmTypeReference _ref = left.getRef();
      IQLExpression _leftOperand_1 = e.getLeftOperand();
      IQLExpression _rightOperand = e.getRightOperand();
      String _compileOperatorOverloading = this.compileOperatorOverloading("+", IQLOperatorOverloadingUtils.PLUS, _ref, _leftOperand_1, _rightOperand, c);
      result = _compileOperatorOverloading;
    } else {
      if ((((!left.isNull()) && e.getOp().equals("-")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.MINUS, e.getRightOperand()))) {
        JvmTypeReference _ref_1 = left.getRef();
        IQLExpression _leftOperand_2 = e.getLeftOperand();
        IQLExpression _rightOperand_1 = e.getRightOperand();
        String _compileOperatorOverloading_1 = this.compileOperatorOverloading("-", IQLOperatorOverloadingUtils.MINUS, _ref_1, _leftOperand_2, _rightOperand_1, c);
        result = _compileOperatorOverloading_1;
      } else {
        boolean _isNull = left.isNull();
        boolean _not = (!_isNull);
        if (_not) {
          JvmTypeReference _ref_2 = left.getRef();
          c.setExpectedTypeRef(_ref_2);
        }
        StringConcatenation _builder = new StringConcatenation();
        IQLExpression _leftOperand_3 = e.getLeftOperand();
        String _compile = this.compile(_leftOperand_3, c);
        _builder.append(_compile, "");
        _builder.append(" ");
        String _op = e.getOp();
        _builder.append(_op, "");
        _builder.append(" ");
        IQLExpression _rightOperand_2 = e.getRightOperand();
        String _compile_1 = this.compile(_rightOperand_2, c);
        _builder.append(_compile_1, "");
        result = _builder.toString();
      }
    }
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLMultiplicativeExpression e, final G c) {
    IQLExpression _leftOperand = e.getLeftOperand();
    TypeResult left = this.exprEvaluator.eval(_leftOperand);
    String result = "";
    if ((((!left.isNull()) && e.getOp().equals("*")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.MULTIPLY, e.getRightOperand()))) {
      JvmTypeReference _ref = left.getRef();
      IQLExpression _leftOperand_1 = e.getLeftOperand();
      IQLExpression _rightOperand = e.getRightOperand();
      String _compileOperatorOverloading = this.compileOperatorOverloading("*", IQLOperatorOverloadingUtils.MULTIPLY, _ref, _leftOperand_1, _rightOperand, c);
      result = _compileOperatorOverloading;
    } else {
      if ((((!left.isNull()) && e.getOp().equals("/")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.DIVIDE, e.getRightOperand()))) {
        JvmTypeReference _ref_1 = left.getRef();
        IQLExpression _leftOperand_2 = e.getLeftOperand();
        IQLExpression _rightOperand_1 = e.getRightOperand();
        String _compileOperatorOverloading_1 = this.compileOperatorOverloading("/", IQLOperatorOverloadingUtils.DIVIDE, _ref_1, _leftOperand_2, _rightOperand_1, c);
        result = _compileOperatorOverloading_1;
      } else {
        if ((((!left.isNull()) && e.getOp().equals("%")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.MODULO, e.getRightOperand()))) {
          JvmTypeReference _ref_2 = left.getRef();
          IQLExpression _leftOperand_3 = e.getLeftOperand();
          IQLExpression _rightOperand_2 = e.getRightOperand();
          String _compileOperatorOverloading_2 = this.compileOperatorOverloading("%", IQLOperatorOverloadingUtils.MODULO, _ref_2, _leftOperand_3, _rightOperand_2, c);
          result = _compileOperatorOverloading_2;
        } else {
          boolean _isNull = left.isNull();
          boolean _not = (!_isNull);
          if (_not) {
            JvmTypeReference _ref_3 = left.getRef();
            c.setExpectedTypeRef(_ref_3);
          }
          StringConcatenation _builder = new StringConcatenation();
          IQLExpression _leftOperand_4 = e.getLeftOperand();
          String _compile = this.compile(_leftOperand_4, c);
          _builder.append(_compile, "");
          _builder.append(" ");
          String _op = e.getOp();
          _builder.append(_op, "");
          _builder.append(" ");
          IQLExpression _rightOperand_3 = e.getRightOperand();
          String _compile_1 = this.compile(_rightOperand_3, c);
          _builder.append(_compile_1, "");
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
      IQLExpression _operand = e.getOperand();
      TypeResult left = this.exprEvaluator.eval(_operand);
      String _xifexpression = null;
      if ((((!left.isNull()) && e.getOp().equals("+")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.SIMPLE_PLUS_PREFIX, new ArrayList<IQLExpression>()))) {
        String _xblockexpression_1 = null;
        {
          String methodName = IQLOperatorOverloadingUtils.SIMPLE_PLUS_PREFIX;
          JvmTypeReference _ref = left.getRef();
          ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_ref, methodName, _arrayList);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          StringConcatenation _builder = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
          String _simpleName = _class_1.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append(".");
          _builder.append(methodName, "");
          _builder.append("(");
          IQLExpression _operand_1 = e.getOperand();
          String _compile = this.compile(_operand_1, c);
          _builder.append(_compile, "");
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
            Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
            String _canonicalName = _class.getCanonicalName();
            c.addImport(_canonicalName);
            StringConcatenation _builder = new StringConcatenation();
            Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
            String _simpleName = _class_1.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append(".");
            _builder.append(methodName, "");
            _builder.append("(");
            IQLExpression _operand_1 = e.getOperand();
            String _compile = this.compile(_operand_1, c);
            _builder.append(_compile, "");
            _builder.append(")");
            _xblockexpression_2 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          String _op = e.getOp();
          _builder.append(_op, "");
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
  
  public String compile(final IQLBooleanNotExpression e, final G c) {
    String _xblockexpression = null;
    {
      IQLExpression _operand = e.getOperand();
      TypeResult left = this.exprEvaluator.eval(_operand);
      String methodName = IQLOperatorOverloadingUtils.BOOLEAN_NOT_PREFIX;
      String _xifexpression = null;
      if (((!left.isNull()) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), methodName, new ArrayList<IQLExpression>()))) {
        String _xblockexpression_1 = null;
        {
          JvmTypeReference _ref = left.getRef();
          ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_ref, methodName, _arrayList);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          StringConcatenation _builder = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
          String _simpleName = _class_1.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append(".");
          _builder.append(methodName, "");
          _builder.append("(");
          IQLExpression _operand_1 = e.getOperand();
          String _compile = this.compile(_operand_1, c);
          _builder.append(_compile, "");
          _builder.append(")");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        StringConcatenation _builder = new StringConcatenation();
        String _op = e.getOp();
        _builder.append(_op, "");
        IQLExpression _operand_1 = e.getOperand();
        String _compile = this.compile(_operand_1, c);
        _builder.append(_compile, "");
        _xifexpression = _builder.toString();
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLPrefixExpression e, final G c) {
    String _xblockexpression = null;
    {
      IQLExpression _operand = e.getOperand();
      TypeResult left = this.exprEvaluator.eval(_operand);
      String _xifexpression = null;
      if ((((!left.isNull()) && e.getOp().equals("++")) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), IQLOperatorOverloadingUtils.PLUS_PREFIX, new ArrayList<IQLExpression>()))) {
        String _xblockexpression_1 = null;
        {
          String methodName = IQLOperatorOverloadingUtils.PLUS_PREFIX;
          JvmTypeReference _ref = left.getRef();
          ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_ref, methodName, _arrayList);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          StringConcatenation _builder = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
          String _simpleName = _class_1.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append(".");
          _builder.append(methodName, "");
          _builder.append("(");
          IQLExpression _operand_1 = e.getOperand();
          String _compile = this.compile(_operand_1, c);
          _builder.append(_compile, "");
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
            Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
            String _canonicalName = _class.getCanonicalName();
            c.addImport(_canonicalName);
            StringConcatenation _builder = new StringConcatenation();
            Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
            String _simpleName = _class_1.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append(".");
            _builder.append(methodName, "");
            _builder.append("(");
            IQLExpression _operand_1 = e.getOperand();
            String _compile = this.compile(_operand_1, c);
            _builder.append(_compile, "");
            _builder.append(")");
            _xblockexpression_2 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          String _op = e.getOp();
          _builder.append(_op, "");
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
    JvmTypeReference _targetRef = e.getTargetRef();
    c.setExpectedTypeRef(_targetRef);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    JvmTypeReference _targetRef_1 = e.getTargetRef();
    String _compile = this.typeCompiler.compile(_targetRef_1, c, false);
    _builder.append(_compile, "");
    _builder.append(")(");
    IQLExpression _operand = e.getOperand();
    String _compile_1 = this.compile(_operand, c);
    _builder.append(_compile_1, "");
    _builder.append(")");
    String result = _builder.toString();
    c.setExpectedTypeRef(null);
    return result;
  }
  
  public String compile(final IQLPostfixExpression e, final G c) {
    String _xblockexpression = null;
    {
      IQLExpression _operand = e.getOperand();
      TypeResult right = this.exprEvaluator.eval(_operand);
      String _xifexpression = null;
      if ((((!right.isNull()) && e.getOp().equals("++")) && this.typeExtensionsDictionary.hasTypeExtensions(right.getRef(), IQLOperatorOverloadingUtils.PLUS_POSTFIX, new ArrayList<IQLExpression>()))) {
        String _xblockexpression_1 = null;
        {
          String methodName = IQLOperatorOverloadingUtils.PLUS_POSTFIX;
          JvmTypeReference _ref = right.getRef();
          ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_ref, methodName, _arrayList);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          StringConcatenation _builder = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
          String _simpleName = _class_1.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append(".");
          _builder.append(methodName, "");
          _builder.append("(");
          IQLExpression _operand_1 = e.getOperand();
          String _compile = this.compile(_operand_1, c);
          _builder.append(_compile, "");
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
            Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
            String _canonicalName = _class.getCanonicalName();
            c.addImport(_canonicalName);
            StringConcatenation _builder = new StringConcatenation();
            Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
            String _simpleName = _class_1.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append(".");
            _builder.append(methodName, "");
            _builder.append("(");
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
          String _op = e.getOp();
          _builder.append(_op, "");
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
      TypeResult left = this.exprEvaluator.eval(_leftOperand);
      String methodName = IQLOperatorOverloadingUtils.GET;
      String _xifexpression = null;
      if (((!left.isNull()) && this.typeExtensionsDictionary.hasTypeExtensions(left.getRef(), methodName, this.helper.createGetterArguments(e.getExpressions())))) {
        String _xblockexpression_1 = null;
        {
          JvmTypeReference _ref = left.getRef();
          EList<IQLExpression> _expressions = e.getExpressions();
          List<IQLExpression> _createGetterArguments = this.helper.createGetterArguments(_expressions);
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_ref, methodName, _createGetterArguments);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          String _xifexpression_1 = null;
          EList<IQLExpression> _expressions_1 = e.getExpressions();
          int _size = _expressions_1.size();
          boolean _equals = (_size == 1);
          if (_equals) {
            String _xblockexpression_2 = null;
            {
              c.setExpectedTypeRef(null);
              StringConcatenation _builder = new StringConcatenation();
              Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
              String _simpleName = _class_1.getSimpleName();
              _builder.append(_simpleName, "");
              _builder.append(".");
              _builder.append(methodName, "");
              _builder.append("(");
              IQLExpression _leftOperand_1 = e.getLeftOperand();
              String _compile = this.compile(_leftOperand_1, c);
              _builder.append(_compile, "");
              _builder.append(", ");
              EList<IQLExpression> _expressions_2 = e.getExpressions();
              IQLExpression _get = _expressions_2.get(0);
              String _compile_1 = this.compile(_get, c);
              _builder.append(_compile_1, "");
              _builder.append(")");
              _xblockexpression_2 = _builder.toString();
            }
            _xifexpression_1 = _xblockexpression_2;
          } else {
            String _xblockexpression_3 = null;
            {
              String _canonicalName_1 = IQLUtils.class.getCanonicalName();
              c.addImport(_canonicalName_1);
              c.setExpectedTypeRef(null);
              StringConcatenation _builder = new StringConcatenation();
              Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
              String _simpleName = _class_1.getSimpleName();
              _builder.append(_simpleName, "");
              _builder.append(".");
              _builder.append(methodName, "");
              _builder.append("(");
              IQLExpression _leftOperand_1 = e.getLeftOperand();
              String _compile = this.compile(_leftOperand_1, c);
              _builder.append(_compile, "");
              _builder.append(", ");
              String _simpleName_1 = IQLUtils.class.getSimpleName();
              _builder.append(_simpleName_1, "");
              _builder.append(".createList(");
              EList<IQLExpression> _expressions_2 = e.getExpressions();
              final Function1<IQLExpression, String> _function = (IQLExpression el) -> {
                return this.compile(el, c);
              };
              List<String> _map = ListExtensions.<IQLExpression, String>map(_expressions_2, _function);
              String _join = IterableExtensions.join(_map, ", ");
              _builder.append(_join, "");
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
            String _canonicalName = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getCanonicalName();
            c.addImport(_canonicalName);
            String _xifexpression_2 = null;
            EList<IQLExpression> _expressions = e.getExpressions();
            int _size = _expressions.size();
            boolean _equals = (_size == 1);
            if (_equals) {
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
              _builder.append(_simpleName, "");
              _builder.append(".");
              _builder.append(methodName, "");
              _builder.append("(");
              IQLExpression _leftOperand_1 = e.getLeftOperand();
              String _compile = this.compile(_leftOperand_1, c);
              _builder.append(_compile, "");
              _builder.append(", ");
              EList<IQLExpression> _expressions_1 = e.getExpressions();
              IQLExpression _get = _expressions_1.get(0);
              String _compile_1 = this.compile(_get, c);
              _builder.append(_compile_1, "");
              _builder.append(")");
              _xifexpression_2 = _builder.toString();
            } else {
              String _xblockexpression_3 = null;
              {
                String _canonicalName_1 = IQLUtils.class.getCanonicalName();
                c.addImport(_canonicalName_1);
                StringConcatenation _builder_1 = new StringConcatenation();
                String _simpleName_1 = de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions.class.getSimpleName();
                _builder_1.append(_simpleName_1, "");
                _builder_1.append(".");
                _builder_1.append(methodName, "");
                _builder_1.append("(");
                IQLExpression _leftOperand_2 = e.getLeftOperand();
                String _compile_2 = this.compile(_leftOperand_2, c);
                _builder_1.append(_compile_2, "");
                _builder_1.append(", ");
                String _simpleName_2 = IQLUtils.class.getSimpleName();
                _builder_1.append(_simpleName_2, "");
                _builder_1.append(".createList(");
                EList<IQLExpression> _expressions_2 = e.getExpressions();
                final Function1<IQLExpression, String> _function = (IQLExpression el) -> {
                  return this.compile(el, c);
                };
                List<String> _map = ListExtensions.<IQLExpression, String>map(_expressions_2, _function);
                String _join = IterableExtensions.join(_map, ", ");
                _builder_1.append(_join, "");
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
      TypeResult _eval = this.exprEvaluator.eval(_leftOperand);
      JvmTypeReference left = _eval.getRef();
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
    boolean _hasTypeExtensions = this.typeExtensionsDictionary.hasTypeExtensions(left, _simpleName);
    if (_hasTypeExtensions) {
      String _xblockexpression = null;
      {
        String _simpleName_1 = field.getSimpleName();
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(left, _simpleName_1);
        Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
        String _canonicalName = _class.getCanonicalName();
        c.addImport(_canonicalName);
        String _xifexpression_1 = null;
        if ((this.helper.isJvmArray(field.getType()) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
          String _xblockexpression_1 = null;
          {
            String _canonicalName_1 = IQLUtils.class.getCanonicalName();
            c.addImport(_canonicalName_1);
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName_2 = IQLUtils.class.getSimpleName();
            _builder.append(_simpleName_2, "");
            _builder.append(".toList(");
            Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
            String _simpleName_3 = _class_1.getSimpleName();
            _builder.append(_simpleName_3, "");
            _builder.append(".");
            String _simpleName_4 = field.getSimpleName();
            _builder.append(_simpleName_4, "");
            _builder.append(")");
            _xblockexpression_1 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_1;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
          String _simpleName_2 = _class_1.getSimpleName();
          _builder.append(_simpleName_2, "");
          _builder.append(".");
          String _simpleName_3 = field.getSimpleName();
          _builder.append(_simpleName_3, "");
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
              String _canonicalName = IQLUtils.class.getCanonicalName();
              c.addImport(_canonicalName);
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName_1 = IQLUtils.class.getSimpleName();
              _builder.append(_simpleName_1, "");
              _builder.append(".toList(");
              IQLExpression _leftOperand = e.getLeftOperand();
              String _compile = this.compile(_leftOperand, c);
              _builder.append(_compile, "");
              _builder.append(".");
              String _compileFieldSelection = systemTypeCompiler.compileFieldSelection(field);
              _builder.append(_compileFieldSelection, "");
              _builder.append(")");
              _xblockexpression_2 = _builder.toString();
            }
            _xifexpression_2 = _xblockexpression_2;
          } else {
            StringConcatenation _builder = new StringConcatenation();
            IQLExpression _leftOperand = e.getLeftOperand();
            String _compile = this.compile(_leftOperand, c);
            _builder.append(_compile, "");
            _builder.append(".");
            String _compileFieldSelection = systemTypeCompiler.compileFieldSelection(field);
            _builder.append(_compileFieldSelection, "");
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
            String _canonicalName = IQLUtils.class.getCanonicalName();
            c.addImport(_canonicalName);
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName_1 = IQLUtils.class.getSimpleName();
            _builder.append(_simpleName_1, "");
            _builder.append(".toList(");
            IQLExpression _leftOperand = e.getLeftOperand();
            String _compile = this.compile(_leftOperand, c);
            _builder.append(_compile, "");
            _builder.append(".");
            String _simpleName_2 = field.getSimpleName();
            _builder.append(_simpleName_2, "");
            _builder.append(")");
            _xblockexpression_2 = _builder.toString();
          }
          _xifexpression_2 = _xblockexpression_2;
        } else {
          StringConcatenation _builder = new StringConcatenation();
          IQLExpression _leftOperand = e.getLeftOperand();
          String _compile = this.compile(_leftOperand, c);
          _builder.append(_compile, "");
          _builder.append(".");
          String _simpleName_1 = field.getSimpleName();
          _builder.append(_simpleName_1, "");
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
      EList<JvmTypeReference> _exceptions = method.getExceptions();
      c.addExceptions(_exceptions);
      List<IQLExpression> list = null;
      IQLMemberSelection _sel = e.getSel();
      IQLArgumentsList _args = _sel.getArgs();
      boolean _tripleNotEquals = (_args != null);
      if (_tripleNotEquals) {
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
      boolean _hasTypeExtensions = this.typeExtensionsDictionary.hasTypeExtensions(left, _simpleName, list);
      if (_hasTypeExtensions) {
        String _xblockexpression_1 = null;
        {
          String _simpleName_1 = method.getSimpleName();
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(left, _simpleName_1, list);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          String _xifexpression_1 = null;
          if ((((method.getReturnType() != null) && this.helper.isJvmArray(method.getReturnType())) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
            String _xblockexpression_2 = null;
            {
              String _canonicalName_1 = IQLUtils.class.getCanonicalName();
              c.addImport(_canonicalName_1);
              String _xifexpression_2 = null;
              boolean _ignoreFirstParameter = this.typeExtensionsDictionary.ignoreFirstParameter(method);
              if (_ignoreFirstParameter) {
                StringConcatenation _builder = new StringConcatenation();
                String _simpleName_2 = IQLUtils.class.getSimpleName();
                _builder.append(_simpleName_2, "");
                _builder.append(".toList(");
                Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
                String _simpleName_3 = _class_1.getSimpleName();
                _builder.append(_simpleName_3, "");
                _builder.append(".");
                String _simpleName_4 = method.getSimpleName();
                _builder.append(_simpleName_4, "");
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
                _builder.append("))");
                _xifexpression_2 = _builder.toString();
              } else {
                StringConcatenation _builder_1 = new StringConcatenation();
                String _simpleName_5 = IQLUtils.class.getSimpleName();
                _builder_1.append(_simpleName_5, "");
                _builder_1.append(".toList(");
                Class<? extends IIQLTypeExtensions> _class_2 = typeOps.getClass();
                String _simpleName_6 = _class_2.getSimpleName();
                _builder_1.append(_simpleName_6, "");
                _builder_1.append(".");
                String _simpleName_7 = method.getSimpleName();
                _builder_1.append(_simpleName_7, "");
                _builder_1.append("(");
                IQLMemberSelection _sel_3 = e.getSel();
                IQLArgumentsList _args_3 = _sel_3.getArgs();
                EList<JvmFormalParameter> _parameters_2 = method.getParameters();
                String _compile_2 = this.compile(_args_3, _parameters_2, c);
                _builder_1.append(_compile_2, "");
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
              _xifexpression_2 = _builder.toString();
            } else {
              StringConcatenation _builder_1 = new StringConcatenation();
              Class<? extends IIQLTypeExtensions> _class_2 = typeOps.getClass();
              String _simpleName_4 = _class_2.getSimpleName();
              _builder_1.append(_simpleName_4, "");
              _builder_1.append(".");
              String _simpleName_5 = method.getSimpleName();
              _builder_1.append(_simpleName_5, "");
              _builder_1.append("(");
              IQLMemberSelection _sel_3 = e.getSel();
              IQLArgumentsList _args_3 = _sel_3.getArgs();
              EList<JvmFormalParameter> _parameters_2 = method.getParameters();
              String _compile_2 = this.compile(_args_3, _parameters_2, c);
              _builder_1.append(_compile_2, "");
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
                String _canonicalName = IQLUtils.class.getCanonicalName();
                c.addImport(_canonicalName);
                StringConcatenation _builder = new StringConcatenation();
                String _simpleName_1 = IQLUtils.class.getSimpleName();
                _builder.append(_simpleName_1, "");
                _builder.append(".toList(");
                IQLExpression _leftOperand = e.getLeftOperand();
                String _compile = this.compile(_leftOperand, c);
                _builder.append(_compile, "");
                _builder.append(".");
                IQLMemberSelection _sel_2 = e.getSel();
                IQLArgumentsList _args_2 = _sel_2.getArgs();
                EList<JvmFormalParameter> _parameters = method.getParameters();
                List<String> _compileArguments = this.compileArguments(_args_2, _parameters, c);
                String _compileMethodSelection = systemTypeCompiler.compileMethodSelection(method, _compileArguments);
                _builder.append(_compileMethodSelection, "");
                _builder.append(")");
                _xblockexpression_3 = _builder.toString();
              }
              _xifexpression_2 = _xblockexpression_3;
            } else {
              StringConcatenation _builder = new StringConcatenation();
              IQLExpression _leftOperand = e.getLeftOperand();
              String _compile = this.compile(_leftOperand, c);
              _builder.append(_compile, "");
              _builder.append(".");
              IQLMemberSelection _sel_2 = e.getSel();
              IQLArgumentsList _args_2 = _sel_2.getArgs();
              EList<JvmFormalParameter> _parameters = method.getParameters();
              List<String> _compileArguments = this.compileArguments(_args_2, _parameters, c);
              String _compileMethodSelection = systemTypeCompiler.compileMethodSelection(method, _compileArguments);
              _builder.append(_compileMethodSelection, "");
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
              String _canonicalName = IQLUtils.class.getCanonicalName();
              c.addImport(_canonicalName);
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName_1 = IQLUtils.class.getSimpleName();
              _builder.append(_simpleName_1, "");
              _builder.append(".toList(");
              IQLExpression _leftOperand = e.getLeftOperand();
              String _compile = this.compile(_leftOperand, c);
              _builder.append(_compile, "");
              _builder.append(".");
              String _simpleName_2 = method.getSimpleName();
              _builder.append(_simpleName_2, "");
              _builder.append("(");
              IQLMemberSelection _sel_2 = e.getSel();
              IQLArgumentsList _args_2 = _sel_2.getArgs();
              EList<JvmFormalParameter> _parameters = method.getParameters();
              String _compile_1 = this.compile(_args_2, _parameters, c);
              _builder.append(_compile_1, "");
              _builder.append("))");
              _xblockexpression_3 = _builder.toString();
            }
            _xifexpression_2 = _xblockexpression_3;
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
        JvmFormalParameter _get = parameters.get(i);
        JvmTypeReference expectedTypeRef = _get.getParameterType();
        if ((expectedTypeRef != null)) {
          c.setExpectedTypeRef(expectedTypeRef);
        }
        EList<IQLExpression> _elements = args.getElements();
        IQLExpression _get_1 = _elements.get(i);
        TypeResult type = this.exprEvaluator.eval(_get_1);
        if (((((expectedTypeRef != null) && this.helper.isJvmArray(expectedTypeRef)) && (!type.isNull())) && (!this.helper.isJvmArray(type.getRef())))) {
          String _canonicalName = IQLUtils.class.getCanonicalName();
          c.addImport(_canonicalName);
          boolean _isPrimitiveArray = this.helper.isPrimitiveArray(expectedTypeRef);
          if (_isPrimitiveArray) {
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = IQLUtils.class.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append(".");
            String _arrayMethodName = this.helper.getArrayMethodName(expectedTypeRef);
            _builder.append(_arrayMethodName, "");
            _builder.append("(");
            EList<IQLExpression> _elements_1 = args.getElements();
            IQLExpression _get_2 = _elements_1.get(i);
            String _compile = this.compile(_get_2, c);
            _builder.append(_compile, "");
            _builder.append(")");
            result.add(_builder.toString());
          } else {
            JvmType _innerType = this.typeUtils.getInnerType(expectedTypeRef, false);
            JvmTypeReference _createTypeRef = this.typeUtils.createTypeRef(_innerType);
            String clazz = this.typeCompiler.compile(_createTypeRef, c, true);
            StringConcatenation _builder_1 = new StringConcatenation();
            String _simpleName_1 = IQLUtils.class.getSimpleName();
            _builder_1.append(_simpleName_1, "");
            _builder_1.append(".");
            String _arrayMethodName_1 = this.helper.getArrayMethodName(expectedTypeRef);
            _builder_1.append(_arrayMethodName_1, "");
            _builder_1.append("(");
            _builder_1.append(clazz, "");
            _builder_1.append(".class, ");
            EList<IQLExpression> _elements_2 = args.getElements();
            IQLExpression _get_3 = _elements_2.get(i);
            String _compile_1 = this.compile(_get_3, c);
            _builder_1.append(_compile_1, "");
            _builder_1.append(")");
            result.add(_builder_1.toString());
          }
        } else {
          if (((!type.isNull()) && this.lookUp.isAssignable(parameters.get(i).getParameterType(), type.getRef()))) {
            EList<IQLExpression> _elements_3 = args.getElements();
            IQLExpression _get_4 = _elements_3.get(i);
            String _compile_2 = this.compile(_get_4, c);
            result.add(_compile_2);
          } else {
            if (((!type.isNull()) && this.lookUp.isCastable(expectedTypeRef, type.getRef()))) {
              String target = this.typeCompiler.compile(expectedTypeRef, c, false);
              StringConcatenation _builder_2 = new StringConcatenation();
              _builder_2.append("((");
              _builder_2.append(target, "");
              _builder_2.append(")");
              EList<IQLExpression> _elements_4 = args.getElements();
              IQLExpression _get_5 = _elements_4.get(i);
              String _compile_3 = this.compile(_get_5, c);
              _builder_2.append(_compile_3, "");
              _builder_2.append(")");
              result.add(_builder_2.toString());
            } else {
              EList<IQLExpression> _elements_5 = args.getElements();
              IQLExpression _get_6 = _elements_5.get(i);
              String _compile_4 = this.compile(_get_6, c);
              result.add(_compile_4);
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
      EList<IQLExpression> _elements = list.getElements();
      final Function1<IQLExpression, String> _function = (IQLExpression e) -> {
        return this.compile(e, context);
      };
      List<String> _map = ListExtensions.<IQLExpression, String>map(_elements, _function);
      String _join = IterableExtensions.join(_map, ", ");
      _builder.append(_join, "");
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
        EList<IQLArgumentsMapKeyValue> _elements = map.getElements();
        IQLArgumentsMapKeyValue el = _elements.get(i);
        IQLExpression _value = el.getValue();
        TypeResult type = this.exprEvaluator.eval(_value);
        JvmIdentifiableElement _key = el.getKey();
        JvmTypeReference expectedTypeRef = this.helper.getPropertyType(_key, typeRef);
        if ((expectedTypeRef != null)) {
          c.setExpectedTypeRef(expectedTypeRef);
        }
        if (((((expectedTypeRef != null) && this.helper.isJvmArray(expectedTypeRef)) && (!type.isNull())) && (!this.helper.isJvmArray(type.getRef())))) {
          String _canonicalName = IQLUtils.class.getCanonicalName();
          c.addImport(_canonicalName);
          boolean _isPrimitiveArray = this.helper.isPrimitiveArray(expectedTypeRef);
          if (_isPrimitiveArray) {
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = IQLUtils.class.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append(".");
            String _arrayMethodName = this.helper.getArrayMethodName(expectedTypeRef);
            _builder.append(_arrayMethodName, "");
            _builder.append("(");
            IQLExpression _value_1 = el.getValue();
            String _compile = this.compile(_value_1, c);
            _builder.append(_compile, "");
            _builder.append(")");
            result = _builder.toString();
          } else {
            JvmType _innerType = this.typeUtils.getInnerType(expectedTypeRef, false);
            JvmTypeReference _createTypeRef = this.typeUtils.createTypeRef(_innerType);
            String clazz = this.typeCompiler.compile(_createTypeRef, c, true);
            StringConcatenation _builder_1 = new StringConcatenation();
            String _simpleName_1 = IQLUtils.class.getSimpleName();
            _builder_1.append(_simpleName_1, "");
            _builder_1.append(".");
            String _arrayMethodName_1 = this.helper.getArrayMethodName(expectedTypeRef);
            _builder_1.append(_arrayMethodName_1, "");
            _builder_1.append("(");
            _builder_1.append(clazz, "");
            _builder_1.append(".class, ");
            IQLExpression _value_2 = el.getValue();
            String _compile_1 = this.compile(_value_2, c);
            _builder_1.append(_compile_1, "");
            _builder_1.append(")");
            result = _builder_1.toString();
          }
        } else {
          if (((!type.isNull()) && this.lookUp.isAssignable(expectedTypeRef, type.getRef()))) {
            IQLExpression _value_3 = el.getValue();
            String _compile_2 = this.compile(_value_3, c);
            String _plus = (result + _compile_2);
            result = _plus;
          } else {
            if (((!type.isNull()) && this.lookUp.isCastable(expectedTypeRef, type.getRef()))) {
              String target = this.typeCompiler.compile(expectedTypeRef, c, false);
              StringConcatenation _builder_2 = new StringConcatenation();
              _builder_2.append("((");
              _builder_2.append(target, "");
              _builder_2.append(")");
              IQLExpression _value_4 = el.getValue();
              String _compile_3 = this.compile(_value_4, c);
              _builder_2.append(_compile_3, "");
              _builder_2.append(")");
              String _plus_1 = (result + _builder_2);
              result = _plus_1;
            } else {
              IQLExpression _value_5 = el.getValue();
              String _compile_4 = this.compile(_value_5, c);
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
          String _simpleName = field.getSimpleName();
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(thisType, _simpleName);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          String _xifexpression_1 = null;
          if ((this.helper.isJvmArray(field.getType()) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
            String _xblockexpression_2 = null;
            {
              String _canonicalName_1 = IQLUtils.class.getCanonicalName();
              c.addImport(_canonicalName_1);
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName_1 = IQLUtils.class.getSimpleName();
              _builder.append(_simpleName_1, "");
              _builder.append(".toList(");
              Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
              String _simpleName_2 = _class_1.getSimpleName();
              _builder.append(_simpleName_2, "");
              _builder.append(".");
              String _simpleName_3 = field.getSimpleName();
              _builder.append(_simpleName_3, "");
              _builder.append(")");
              _xblockexpression_2 = _builder.toString();
            }
            _xifexpression_1 = _xblockexpression_2;
          } else {
            StringConcatenation _builder = new StringConcatenation();
            Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
            String _simpleName_1 = _class_1.getSimpleName();
            _builder.append(_simpleName_1, "");
            _builder.append(".");
            String _simpleName_2 = field.getSimpleName();
            _builder.append(_simpleName_2, "");
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
                String _canonicalName = IQLUtils.class.getCanonicalName();
                c.addImport(_canonicalName);
                String _xifexpression_3 = null;
                boolean _isStatic = field.isStatic();
                if (_isStatic) {
                  StringConcatenation _builder = new StringConcatenation();
                  String _simpleName = IQLUtils.class.getSimpleName();
                  _builder.append(_simpleName, "");
                  _builder.append(".toList(");
                  String _compile = this.typeCompiler.compile(typeRef, c, true);
                  _builder.append(_compile, "");
                  _builder.append(".");
                  String _compileFieldSelection = systemTypeCompiler.compileFieldSelection(field);
                  _builder.append(_compileFieldSelection, "");
                  _builder.append(")");
                  _xifexpression_3 = _builder.toString();
                } else {
                  StringConcatenation _builder_1 = new StringConcatenation();
                  String _compile_1 = this.typeCompiler.compile(typeRef, c, true);
                  _builder_1.append(_compile_1, "");
                  _builder_1.append(".");
                  String _compileFieldSelection_1 = systemTypeCompiler.compileFieldSelection(field);
                  _builder_1.append(_compileFieldSelection_1, "");
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
                _builder.append(_simpleName, "");
                _builder.append(".toList(");
                String _compileFieldSelection = systemTypeCompiler.compileFieldSelection(field);
                _builder.append(_compileFieldSelection, "");
                _builder.append(")");
                _xifexpression_3 = _builder.toString();
              } else {
                StringConcatenation _builder_1 = new StringConcatenation();
                String _compileFieldSelection_1 = systemTypeCompiler.compileFieldSelection(field);
                _builder_1.append(_compileFieldSelection_1, "");
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
                String _canonicalName = IQLUtils.class.getCanonicalName();
                c.addImport(_canonicalName);
                StringConcatenation _builder = new StringConcatenation();
                String _simpleName = IQLUtils.class.getSimpleName();
                _builder.append(_simpleName, "");
                _builder.append(".toList(");
                String _compile = this.typeCompiler.compile(typeRef, c, true);
                _builder.append(_compile, "");
                _builder.append(".");
                String _simpleName_1 = field.getSimpleName();
                _builder.append(_simpleName_1, "");
                _builder.append(")");
                _xblockexpression_3 = _builder.toString();
              }
              _xifexpression_3 = _xblockexpression_3;
            } else {
              StringConcatenation _builder = new StringConcatenation();
              String _compile = this.typeCompiler.compile(typeRef, c, true);
              _builder.append(_compile, "");
              _builder.append(".");
              String _simpleName = field.getSimpleName();
              _builder.append(_simpleName, "");
              _xifexpression_3 = _builder.toString();
            }
            _xifexpression_2 = _xifexpression_3;
          } else {
            String _xifexpression_4 = null;
            if ((this.helper.isJvmArray(field.getType()) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
              String _xblockexpression_4 = null;
              {
                String _canonicalName = IQLUtils.class.getCanonicalName();
                c.addImport(_canonicalName);
                StringConcatenation _builder_1 = new StringConcatenation();
                String _simpleName_1 = IQLUtils.class.getSimpleName();
                _builder_1.append(_simpleName_1, "");
                _builder_1.append(".toList(");
                String _simpleName_2 = field.getSimpleName();
                _builder_1.append(_simpleName_2, "");
                _builder_1.append(")");
                _xblockexpression_4 = _builder_1.toString();
              }
              _xifexpression_4 = _xblockexpression_4;
            } else {
              StringConcatenation _builder_1 = new StringConcatenation();
              String _simpleName_1 = field.getSimpleName();
              _builder_1.append(_simpleName_1, "");
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
        String _canonicalName = IQLUtils.class.getCanonicalName();
        c.addImport(_canonicalName);
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName, "");
        _builder.append(".toList(");
        String _name = varDecl.getName();
        _builder.append(_name, "");
        _builder.append(")");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      StringConcatenation _builder = new StringConcatenation();
      String _name = varDecl.getName();
      _builder.append(_name, "");
      _xifexpression = _builder.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLJvmElementCallExpression e, final JvmFormalParameter parameter, final G c) {
    String _xifexpression = null;
    if ((this.helper.isJvmArray(parameter.getParameterType()) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
      String _xblockexpression = null;
      {
        String _canonicalName = IQLUtils.class.getCanonicalName();
        c.addImport(_canonicalName);
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName, "");
        _builder.append(".toList(");
        String _name = parameter.getName();
        _builder.append(_name, "");
        _builder.append(")");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      StringConcatenation _builder = new StringConcatenation();
      String _name = parameter.getName();
      _builder.append(_name, "");
      _xifexpression = _builder.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLJvmElementCallExpression m, final JvmOperation method, final G c) {
    String _xblockexpression = null;
    {
      EList<JvmTypeReference> _exceptions = method.getExceptions();
      c.addExceptions(_exceptions);
      List<IQLExpression> list = null;
      IQLArgumentsList _args = m.getArgs();
      boolean _tripleNotEquals = (_args != null);
      if (_tripleNotEquals) {
        IQLArgumentsList _args_1 = m.getArgs();
        EList<IQLExpression> _elements = _args_1.getElements();
        list = _elements;
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
          String _simpleName = method.getSimpleName();
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(thisType, _simpleName, list);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          String _xifexpression_1 = null;
          if ((((method.getReturnType() != null) && this.helper.isJvmArray(method.getReturnType())) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
            String _xblockexpression_2 = null;
            {
              String _canonicalName_1 = IQLUtils.class.getCanonicalName();
              c.addImport(_canonicalName_1);
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName_1 = IQLUtils.class.getSimpleName();
              _builder.append(_simpleName_1, "");
              _builder.append(".toList(");
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
              _builder.append("))");
              _xblockexpression_2 = _builder.toString();
            }
            _xifexpression_1 = _xblockexpression_2;
          } else {
            String _xifexpression_2 = null;
            boolean _ignoreFirstParameter = this.typeExtensionsDictionary.ignoreFirstParameter(method);
            if (_ignoreFirstParameter) {
              StringConcatenation _builder = new StringConcatenation();
              Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
              String _simpleName_1 = _class_1.getSimpleName();
              _builder.append(_simpleName_1, "");
              _builder.append(".");
              String _simpleName_2 = method.getSimpleName();
              _builder.append(_simpleName_2, "");
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
              _xifexpression_2 = _builder.toString();
            } else {
              StringConcatenation _builder_1 = new StringConcatenation();
              Class<? extends IIQLTypeExtensions> _class_2 = typeOps.getClass();
              String _simpleName_3 = _class_2.getSimpleName();
              _builder_1.append(_simpleName_3, "");
              _builder_1.append(".");
              String _simpleName_4 = method.getSimpleName();
              _builder_1.append(_simpleName_4, "");
              _builder_1.append("(");
              IQLArgumentsList _args_3 = m.getArgs();
              EList<JvmFormalParameter> _parameters_2 = method.getParameters();
              String _compile_1 = this.compile(_args_3, _parameters_2, c);
              _builder_1.append(_compile_1, "");
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
                String _canonicalName = IQLUtils.class.getCanonicalName();
                c.addImport(_canonicalName);
                String _xifexpression_3 = null;
                boolean _isStatic = method.isStatic();
                if (_isStatic) {
                  StringConcatenation _builder = new StringConcatenation();
                  String _simpleName = IQLUtils.class.getSimpleName();
                  _builder.append(_simpleName, "");
                  _builder.append(".toList(");
                  String _compile = this.typeCompiler.compile(typeRef, c, true);
                  _builder.append(_compile, "");
                  _builder.append(".");
                  IQLArgumentsList _args_2 = m.getArgs();
                  EList<JvmFormalParameter> _parameters = method.getParameters();
                  List<String> _compileArguments = this.compileArguments(_args_2, _parameters, c);
                  String _compileMethodSelection = systemTypeCompiler.compileMethodSelection(method, _compileArguments);
                  _builder.append(_compileMethodSelection, "");
                  _builder.append(")");
                  _xifexpression_3 = _builder.toString();
                } else {
                  StringConcatenation _builder_1 = new StringConcatenation();
                  String _simpleName_1 = IQLUtils.class.getSimpleName();
                  _builder_1.append(_simpleName_1, "");
                  _builder_1.append(".toList(");
                  IQLArgumentsList _args_3 = m.getArgs();
                  EList<JvmFormalParameter> _parameters_1 = method.getParameters();
                  List<String> _compileArguments_1 = this.compileArguments(_args_3, _parameters_1, c);
                  String _compileMethodSelection_1 = systemTypeCompiler.compileMethodSelection(method, _compileArguments_1);
                  _builder_1.append(_compileMethodSelection_1, "");
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
                _builder.append(_compile, "");
                _builder.append(".");
                IQLArgumentsList _args_2 = m.getArgs();
                EList<JvmFormalParameter> _parameters = method.getParameters();
                List<String> _compileArguments = this.compileArguments(_args_2, _parameters, c);
                String _compileMethodSelection = systemTypeCompiler.compileMethodSelection(method, _compileArguments);
                _builder.append(_compileMethodSelection, "");
                _xifexpression_3 = _builder.toString();
              } else {
                StringConcatenation _builder_1 = new StringConcatenation();
                IQLArgumentsList _args_3 = m.getArgs();
                EList<JvmFormalParameter> _parameters_1 = method.getParameters();
                List<String> _compileArguments_1 = this.compileArguments(_args_3, _parameters_1, c);
                String _compileMethodSelection_1 = systemTypeCompiler.compileMethodSelection(method, _compileArguments_1);
                _builder_1.append(_compileMethodSelection_1, "");
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
                String _canonicalName = IQLUtils.class.getCanonicalName();
                c.addImport(_canonicalName);
                StringConcatenation _builder = new StringConcatenation();
                String _simpleName = IQLUtils.class.getSimpleName();
                _builder.append(_simpleName, "");
                _builder.append(".toList(");
                String _compile = this.typeCompiler.compile(typeRef, c, true);
                _builder.append(_compile, "");
                _builder.append(".");
                String _simpleName_1 = method.getSimpleName();
                _builder.append(_simpleName_1, "");
                _builder.append("(");
                IQLArgumentsList _args_2 = m.getArgs();
                EList<JvmFormalParameter> _parameters = method.getParameters();
                String _compile_1 = this.compile(_args_2, _parameters, c);
                _builder.append(_compile_1, "");
                _builder.append("))");
                _xblockexpression_3 = _builder.toString();
              }
              _xifexpression_3 = _xblockexpression_3;
            } else {
              StringConcatenation _builder = new StringConcatenation();
              String _compile = this.typeCompiler.compile(typeRef, c, true);
              _builder.append(_compile, "");
              _builder.append(".");
              String _simpleName = method.getSimpleName();
              _builder.append(_simpleName, "");
              _builder.append("(");
              IQLArgumentsList _args_2 = m.getArgs();
              EList<JvmFormalParameter> _parameters = method.getParameters();
              String _compile_1 = this.compile(_args_2, _parameters, c);
              _builder.append(_compile_1, "");
              _builder.append(")");
              _xifexpression_3 = _builder.toString();
            }
            _xifexpression_2 = _xifexpression_3;
          } else {
            String _xifexpression_4 = null;
            if ((((method.getReturnType() != null) && this.helper.isJvmArray(method.getReturnType())) && ((c.getExpectedTypeRef() == null) || (!this.helper.isJvmArray(c.getExpectedTypeRef()))))) {
              String _xblockexpression_4 = null;
              {
                String _canonicalName = IQLUtils.class.getCanonicalName();
                c.addImport(_canonicalName);
                StringConcatenation _builder_1 = new StringConcatenation();
                String _simpleName_1 = IQLUtils.class.getSimpleName();
                _builder_1.append(_simpleName_1, "");
                _builder_1.append(".toList(");
                String _simpleName_2 = method.getSimpleName();
                _builder_1.append(_simpleName_2, "");
                _builder_1.append("(");
                {
                  IQLArgumentsList _args_3 = m.getArgs();
                  boolean _tripleNotEquals_1 = (_args_3 != null);
                  if (_tripleNotEquals_1) {
                    IQLArgumentsList _args_4 = m.getArgs();
                    EList<JvmFormalParameter> _parameters_1 = method.getParameters();
                    String _compile_2 = this.compile(_args_4, _parameters_1, c);
                    _builder_1.append(_compile_2, "");
                  }
                }
                _builder_1.append("))");
                _xblockexpression_4 = _builder_1.toString();
              }
              _xifexpression_4 = _xblockexpression_4;
            } else {
              StringConcatenation _builder_1 = new StringConcatenation();
              String _simpleName_1 = method.getSimpleName();
              _builder_1.append(_simpleName_1, "");
              _builder_1.append("(");
              {
                IQLArgumentsList _args_3 = m.getArgs();
                boolean _tripleNotEquals_1 = (_args_3 != null);
                if (_tripleNotEquals_1) {
                  IQLArgumentsList _args_4 = m.getArgs();
                  EList<JvmFormalParameter> _parameters_1 = method.getParameters();
                  String _compile_2 = this.compile(_args_4, _parameters_1, c);
                  _builder_1.append(_compile_2, "");
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
    IQLExpression _expr = e.getExpr();
    String _compile = this.compile(_expr, c);
    _builder.append(_compile, "");
    _builder.append(")");
    return _builder.toString();
  }
  
  public String compile(final IQLNewExpression e, final G c) {
    String _xifexpression = null;
    if ((((e.getArgsList() != null) && (e.getArgsMap() != null)) && (e.getArgsMap().getElements().size() > 0))) {
      String _xblockexpression = null;
      {
        JvmTypeReference _ref = e.getRef();
        IQLArgumentsList _argsList = e.getArgsList();
        EList<IQLExpression> _elements = _argsList.getElements();
        JvmExecutable constructor = this.lookUp.findPublicConstructor(_ref, _elements);
        EList<JvmTypeReference> _exceptions = constructor.getExceptions();
        c.addExceptions(_exceptions);
        String _xifexpression_1 = null;
        if ((constructor != null)) {
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
          IQLArgumentsMap _argsMap = e.getArgsMap();
          JvmTypeReference _ref_4 = e.getRef();
          String _compile_2 = this.compile(_argsMap, _ref_4, c);
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
          IQLArgumentsMap _argsMap_1 = e.getArgsMap();
          JvmTypeReference _ref_8 = e.getRef();
          String _compile_5 = this.compile(_argsMap_1, _ref_8, c);
          _builder_1.append(_compile_5, "");
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
            EList<JvmTypeReference> _exceptions = constructor.getExceptions();
            c.addExceptions(_exceptions);
          }
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
          _builder.append("(), ");
          IQLArgumentsMap _argsMap = e.getArgsMap();
          JvmTypeReference _ref_4 = e.getRef();
          String _compile_1 = this.compile(_argsMap, _ref_4, c);
          _builder.append(_compile_1, "");
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
            JvmTypeReference _ref = e.getRef();
            IQLArgumentsList _argsList_1 = e.getArgsList();
            EList<IQLExpression> _elements = _argsList_1.getElements();
            JvmExecutable constructor = this.lookUp.findPublicConstructor(_ref, _elements);
            String _xifexpression_3 = null;
            if ((constructor != null)) {
              String _xblockexpression_3 = null;
              {
                EList<JvmTypeReference> _exceptions = constructor.getExceptions();
                c.addExceptions(_exceptions);
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
                _xblockexpression_3 = _builder.toString();
              }
              _xifexpression_3 = _xblockexpression_3;
            } else {
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("new ");
              JvmTypeReference _ref_1 = e.getRef();
              String _compile = this.typeCompiler.compile(_ref_1, c, true);
              _builder.append(_compile, "");
              _builder.append("(");
              IQLArgumentsList _argsList_2 = e.getArgsList();
              String _compile_1 = this.compile(_argsList_2, c);
              _builder.append(_compile_1, "");
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
              String _canonicalName = ArrayList.class.getCanonicalName();
              c.addImport(_canonicalName);
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("new ");
              String _simpleName = ArrayList.class.getSimpleName();
              _builder.append(_simpleName, "");
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
                EList<JvmTypeReference> _exceptions = constructor.getExceptions();
                c.addExceptions(_exceptions);
              }
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("new ");
              JvmTypeReference _ref_2 = e.getRef();
              String _compile = this.typeCompiler.compile(_ref_2, c, true);
              _builder.append(_compile, "");
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
        JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_expectedTypeRef, "doubleToType", e);
        Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
        String _canonicalName = _class.getCanonicalName();
        c.addImport(_canonicalName);
        StringConcatenation _builder = new StringConcatenation();
        Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
        String _simpleName = _class_1.getSimpleName();
        _builder.append(_simpleName, "");
        _builder.append(".doubleToType(");
        double _value = e.getValue();
        _builder.append(_value, "");
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
        JvmTypeReference _expectedTypeRef_1 = c.getExpectedTypeRef();
        boolean _isFloat = this.typeUtils.isFloat(_expectedTypeRef_1);
        if (_isFloat) {
          StringConcatenation _builder = new StringConcatenation();
          double _value = e.getValue();
          _builder.append(_value, "");
          _builder.append("F");
          _xifexpression_2 = _builder.toString();
        } else {
          String _xifexpression_3 = null;
          JvmTypeReference _expectedTypeRef_2 = c.getExpectedTypeRef();
          boolean _isDouble = this.typeUtils.isDouble(_expectedTypeRef_2, true);
          if (_isDouble) {
            StringConcatenation _builder_1 = new StringConcatenation();
            double _value_1 = e.getValue();
            _builder_1.append(_value_1, "");
            _builder_1.append("D");
            _xifexpression_3 = _builder_1.toString();
          } else {
            StringConcatenation _builder_2 = new StringConcatenation();
            double _value_2 = e.getValue();
            _builder_2.append(_value_2, "");
            _xifexpression_3 = _builder_2.toString();
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      } else {
        StringConcatenation _builder_3 = new StringConcatenation();
        double _value_3 = e.getValue();
        _builder_3.append(_value_3, "");
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
        JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_expectedTypeRef, "intToType", e);
        Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
        String _canonicalName = _class.getCanonicalName();
        c.addImport(_canonicalName);
        StringConcatenation _builder = new StringConcatenation();
        Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
        String _simpleName = _class_1.getSimpleName();
        _builder.append(_simpleName, "");
        _builder.append(".intToType(");
        int _value = e.getValue();
        _builder.append(_value, "");
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
        JvmTypeReference _expectedTypeRef_1 = c.getExpectedTypeRef();
        boolean _isFloat = this.typeUtils.isFloat(_expectedTypeRef_1);
        if (_isFloat) {
          StringConcatenation _builder = new StringConcatenation();
          int _value = e.getValue();
          _builder.append(_value, "");
          _builder.append("F");
          _xifexpression_2 = _builder.toString();
        } else {
          String _xifexpression_3 = null;
          JvmTypeReference _expectedTypeRef_2 = c.getExpectedTypeRef();
          boolean _isDouble = this.typeUtils.isDouble(_expectedTypeRef_2, true);
          if (_isDouble) {
            StringConcatenation _builder_1 = new StringConcatenation();
            int _value_1 = e.getValue();
            _builder_1.append(_value_1, "");
            _builder_1.append("D");
            _xifexpression_3 = _builder_1.toString();
          } else {
            String _xifexpression_4 = null;
            JvmTypeReference _expectedTypeRef_3 = c.getExpectedTypeRef();
            boolean _isLong = this.typeUtils.isLong(_expectedTypeRef_3, true);
            if (_isLong) {
              StringConcatenation _builder_2 = new StringConcatenation();
              int _value_2 = e.getValue();
              _builder_2.append(_value_2, "");
              _builder_2.append("L");
              _xifexpression_4 = _builder_2.toString();
            } else {
              StringConcatenation _builder_3 = new StringConcatenation();
              int _value_3 = e.getValue();
              _builder_3.append(_value_3, "");
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
        _builder_4.append(_value_4, "");
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
        JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_expectedTypeRef, "stringToType", e);
        Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
        String _canonicalName = _class.getCanonicalName();
        c.addImport(_canonicalName);
        StringConcatenation _builder = new StringConcatenation();
        Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
        String _simpleName = _class_1.getSimpleName();
        _builder.append(_simpleName, "");
        _builder.append(".stringToType(\"");
        String _value = e.getValue();
        _builder.append(_value, "");
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
          _xifexpression_2 = _builder.toString();
        }
        _xifexpression_1 = _xifexpression_2;
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("\"");
        String _value_2 = e.getValue();
        _builder_1.append(_value_2, "");
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
        JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_expectedTypeRef, "booleanToType", e);
        Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
        String _canonicalName = _class.getCanonicalName();
        c.addImport(_canonicalName);
        StringConcatenation _builder = new StringConcatenation();
        Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
        String _simpleName = _class_1.getSimpleName();
        _builder.append(_simpleName, "");
        _builder.append(".booleanToType(");
        boolean _isValue = e.isValue();
        _builder.append(_isValue, "");
        _builder.append(")");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      StringConcatenation _builder = new StringConcatenation();
      boolean _isValue = e.isValue();
      _builder.append(_isValue, "");
      _xifexpression = _builder.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLLiteralExpressionType e, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("((Class)");
    JvmTypeReference _value = e.getValue();
    String _compile = this.typeCompiler.compile(_value, c, true);
    _builder.append(_compile, "");
    _builder.append(".class)");
    return _builder.toString();
  }
  
  public String compile(final IQLLiteralExpressionRange e, final G c) {
    String _xblockexpression = null;
    {
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
      String _xifexpression = null;
      if (((c.getExpectedTypeRef() != null) && this.typeExtensionsDictionary.hasTypeExtensions(c.getExpectedTypeRef(), "rangeToType", e))) {
        String _xblockexpression_1 = null;
        {
          JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
          IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_expectedTypeRef, "rangeToType", e);
          Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
          String _canonicalName = _class.getCanonicalName();
          c.addImport(_canonicalName);
          String _canonicalName_1 = Range.class.getCanonicalName();
          c.addImport(_canonicalName_1);
          StringConcatenation _builder = new StringConcatenation();
          Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
          String _simpleName = _class_1.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append(".rangeToType(new ");
          String _simpleName_1 = Range.class.getSimpleName();
          _builder.append(_simpleName_1, "");
          _builder.append("(");
          _builder.append(from, "");
          _builder.append(" , ");
          _builder.append(to, "");
          _builder.append("))");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xblockexpression_2 = null;
        {
          String _canonicalName = Range.class.getCanonicalName();
          c.addImport(_canonicalName);
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("new ");
          String _simpleName = Range.class.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append("(");
          _builder.append(from, "");
          _builder.append(" , ");
          _builder.append(to, "");
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
        JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_expectedTypeRef, "listToType", e);
        Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
        String _canonicalName = _class.getCanonicalName();
        c.addImport(_canonicalName);
        c.setExpectedTypeRef(null);
        StringConcatenation _builder = new StringConcatenation();
        Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
        String _simpleName = _class_1.getSimpleName();
        _builder.append(_simpleName, "");
        _builder.append(".listToType(");
        String _simpleName_1 = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName_1, "");
        _builder.append(".createList(");
        EList<IQLExpression> _elements = e.getElements();
        final Function1<IQLExpression, String> _function = (IQLExpression el) -> {
          return this.compile(el, c);
        };
        List<String> _map = ListExtensions.<IQLExpression, String>map(_elements, _function);
        String _join = IterableExtensions.join(_map, ", ");
        _builder.append(_join, "");
        _builder.append("))");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      String _xblockexpression_1 = null;
      {
        String _canonicalName = IQLUtils.class.getCanonicalName();
        c.addImport(_canonicalName);
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName, "");
        _builder.append(".createList(");
        EList<IQLExpression> _elements = e.getElements();
        final Function1<IQLExpression, String> _function = (IQLExpression el) -> {
          return this.compile(el, c);
        };
        List<String> _map = ListExtensions.<IQLExpression, String>map(_elements, _function);
        String _join = IterableExtensions.join(_map, ", ");
        _builder.append(_join, "");
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
        JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
        IIQLTypeExtensions typeOps = this.typeExtensionsDictionary.getTypeExtensions(_expectedTypeRef, "mapToType", e);
        Class<? extends IIQLTypeExtensions> _class = typeOps.getClass();
        String _canonicalName = _class.getCanonicalName();
        c.addImport(_canonicalName);
        c.setExpectedTypeRef(null);
        StringConcatenation _builder = new StringConcatenation();
        Class<? extends IIQLTypeExtensions> _class_1 = typeOps.getClass();
        String _simpleName = _class_1.getSimpleName();
        _builder.append(_simpleName, "");
        _builder.append(".mapToType(");
        String _simpleName_1 = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName_1, "");
        _builder.append(".createMap(");
        EList<IQLLiteralExpressionMapKeyValue> _elements = e.getElements();
        final Function1<IQLLiteralExpressionMapKeyValue, String> _function = (IQLLiteralExpressionMapKeyValue el) -> {
          IQLExpression _key = el.getKey();
          String _compile = this.compile(_key, c);
          String _plus = (_compile + ", ");
          IQLExpression _value = el.getValue();
          String _compile_1 = this.compile(_value, c);
          return (_plus + _compile_1);
        };
        List<String> _map = ListExtensions.<IQLLiteralExpressionMapKeyValue, String>map(_elements, _function);
        String _join = IterableExtensions.join(_map, ", ");
        _builder.append(_join, "");
        _builder.append("))");
        _xblockexpression = _builder.toString();
      }
      _xifexpression = _xblockexpression;
    } else {
      String _xblockexpression_1 = null;
      {
        String _canonicalName = IQLUtils.class.getCanonicalName();
        c.addImport(_canonicalName);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("\t");
        String _simpleName = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName, "\t");
        _builder.append(".createMap(");
        EList<IQLLiteralExpressionMapKeyValue> _elements = e.getElements();
        final Function1<IQLLiteralExpressionMapKeyValue, String> _function = (IQLLiteralExpressionMapKeyValue el) -> {
          IQLExpression _key = el.getKey();
          String _compile = this.compile(_key, c);
          String _plus = (_compile + ", ");
          IQLExpression _value = el.getValue();
          String _compile_1 = this.compile(_value, c);
          return (_plus + _compile_1);
        };
        List<String> _map = ListExtensions.<IQLLiteralExpressionMapKeyValue, String>map(_elements, _function);
        String _join = IterableExtensions.join(_map, ", ");
        _builder.append(_join, "\t");
        _builder.append(")");
        _xblockexpression_1 = _builder.toString();
      }
      _xifexpression = _xblockexpression_1;
    }
    return _xifexpression;
  }
}
