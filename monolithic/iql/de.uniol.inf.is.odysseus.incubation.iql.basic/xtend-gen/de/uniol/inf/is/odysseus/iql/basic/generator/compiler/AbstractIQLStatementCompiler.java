package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBreakStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLCasePart;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLContinueStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLDoWhileStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpressionStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLReturnStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.IIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.TypeResult;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import java.util.ArrayList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmTypeReference;

@SuppressWarnings("all")
public abstract class AbstractIQLStatementCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, E extends IIQLExpressionCompiler<G>, U extends IIQLTypeUtils, P extends IIQLExpressionEvaluator, L extends IIQLLookUp> implements IIQLStatementCompiler<G> {
  protected H helper;
  
  protected E exprCompiler;
  
  protected T typeCompiler;
  
  protected P exprEvaluator;
  
  protected L lookUp;
  
  protected U typeUtils;
  
  public AbstractIQLStatementCompiler(final H helper, final E exprCompiler, final T typeCompiler, final U typeUtils, final P exprEvaluator, final L lookUp) {
    this.helper = helper;
    this.exprCompiler = exprCompiler;
    this.typeCompiler = typeCompiler;
    this.typeUtils = typeUtils;
    this.exprEvaluator = exprEvaluator;
    this.lookUp = lookUp;
  }
  
  @Override
  public String compile(final IQLStatement s, final G c) {
    if ((s instanceof IQLStatementBlock)) {
      return this.compile(((IQLStatementBlock) s), c);
    } else {
      if ((s instanceof IQLExpressionStatement)) {
        return this.compile(((IQLExpressionStatement) s), c);
      } else {
        if ((s instanceof IQLIfStatement)) {
          return this.compile(((IQLIfStatement) s), c);
        } else {
          if ((s instanceof IQLWhileStatement)) {
            return this.compile(((IQLWhileStatement) s), c);
          } else {
            if ((s instanceof IQLDoWhileStatement)) {
              return this.compile(((IQLDoWhileStatement) s), c);
            } else {
              if ((s instanceof IQLForStatement)) {
                return this.compile(((IQLForStatement) s), c);
              } else {
                if ((s instanceof IQLForEachStatement)) {
                  return this.compile(((IQLForEachStatement) s), c);
                } else {
                  if ((s instanceof IQLSwitchStatement)) {
                    return this.compile(((IQLSwitchStatement) s), c);
                  } else {
                    if ((s instanceof IQLVariableStatement)) {
                      return this.compile(((IQLVariableStatement) s), c);
                    } else {
                      if ((s instanceof IQLBreakStatement)) {
                        return this.compile(((IQLBreakStatement) s), c);
                      } else {
                        if ((s instanceof IQLContinueStatement)) {
                          return this.compile(((IQLContinueStatement) s), c);
                        } else {
                          if ((s instanceof IQLReturnStatement)) {
                            return this.compile(((IQLReturnStatement) s), c);
                          } else {
                            if ((s instanceof IQLConstructorCallStatement)) {
                              return this.compile(((IQLConstructorCallStatement) s), c);
                            } else {
                              if ((s instanceof IQLJavaStatement)) {
                                return this.compile(((IQLJavaStatement) s), c);
                              } else {
                                return "";
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
  
  public String compile(final IQLJavaStatement s, final G c) {
    String _xblockexpression = null;
    {
      String text = s.getJava().getText();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(text);
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLStatementBlock s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    {
      EList<IQLStatement> _statements = s.getStatements();
      for(final IQLStatement e : _statements) {
        _builder.append("\t");
        String _compile = this.compile(e, c);
        _builder.append(_compile, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String createTryCatchBlock(final String content, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("try {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append(content, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("} catch (Exception e) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("e.printStackTrace();");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    String result = _builder.toString();
    c.clearExceptions();
    return result;
  }
  
  public String compile(final IQLExpressionStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    String _compile = this.exprCompiler.compile(s.getExpression(), c);
    _builder.append(_compile);
    _builder.append(";");
    String content = _builder.toString();
    boolean _hasException = c.hasException();
    if (_hasException) {
      return this.createTryCatchBlock(content, c);
    } else {
      return content;
    }
  }
  
  public String compile(final IQLIfStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if(");
    String _compile = this.exprCompiler.compile(s.getPredicate(), c);
    _builder.append(_compile);
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _compile_1 = this.compile(s.getThenBody(), c);
    _builder.append(_compile_1, "\t");
    _builder.newLineIfNotEmpty();
    {
      IQLStatement _elseBody = s.getElseBody();
      boolean _tripleNotEquals = (_elseBody != null);
      if (_tripleNotEquals) {
        _builder.append("else");
        _builder.newLine();
        _builder.append("\t");
        String _compile_2 = this.compile(s.getElseBody(), c);
        _builder.append(_compile_2, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    String content = _builder.toString();
    boolean _hasException = c.hasException();
    if (_hasException) {
      return this.createTryCatchBlock(content, c);
    } else {
      return content;
    }
  }
  
  public String compile(final IQLWhileStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("while(");
    String _compile = this.exprCompiler.compile(s.getPredicate(), c);
    _builder.append(_compile);
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _compile_1 = this.compile(s.getBody(), c);
    _builder.append(_compile_1, "\t");
    _builder.newLineIfNotEmpty();
    String content = _builder.toString();
    boolean _hasException = c.hasException();
    if (_hasException) {
      return this.createTryCatchBlock(content, c);
    } else {
      return content;
    }
  }
  
  public String compile(final IQLDoWhileStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("do");
    _builder.newLine();
    _builder.append("\t");
    String _compile = this.compile(s.getBody(), c);
    _builder.append(_compile, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("while(");
    String _compile_1 = this.exprCompiler.compile(s.getPredicate(), c);
    _builder.append(_compile_1);
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    String content = _builder.toString();
    boolean _hasException = c.hasException();
    if (_hasException) {
      return this.createTryCatchBlock(content, c);
    } else {
      return content;
    }
  }
  
  public String compile(final IQLForStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("for (");
    JvmIdentifiableElement _var = s.getVar();
    String _compile = this.compile(((IQLVariableDeclaration) _var), c);
    _builder.append(_compile);
    _builder.append(" = ");
    String _compile_1 = this.exprCompiler.compile(s.getValue(), c);
    _builder.append(_compile_1);
    _builder.append("; ");
    String _compile_2 = this.exprCompiler.compile(s.getPredicate(), c);
    _builder.append(_compile_2);
    _builder.append("; ");
    String _compile_3 = this.exprCompiler.compile(s.getUpdateExpr(), c);
    _builder.append(_compile_3);
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _compile_4 = this.compile(s.getBody(), c);
    _builder.append(_compile_4, "\t");
    _builder.newLineIfNotEmpty();
    String content = _builder.toString();
    boolean _hasException = c.hasException();
    if (_hasException) {
      return this.createTryCatchBlock(content, c);
    } else {
      return content;
    }
  }
  
  public String compile(final IQLForEachStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("for (");
    JvmIdentifiableElement _var = s.getVar();
    String _compile = this.compile(((IQLVariableDeclaration) _var), c);
    _builder.append(_compile);
    _builder.append(" : ");
    String _compile_1 = this.exprCompiler.compile(s.getForExpression(), c);
    _builder.append(_compile_1);
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _compile_2 = this.compile(s.getBody(), c);
    _builder.append(_compile_2, "\t");
    _builder.newLineIfNotEmpty();
    String content = _builder.toString();
    boolean _hasException = c.hasException();
    if (_hasException) {
      return this.createTryCatchBlock(content, c);
    } else {
      return content;
    }
  }
  
  public String compile(final IQLSwitchStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("switch (");
    String _compile = this.exprCompiler.compile(s.getExpr(), c);
    _builder.append(_compile);
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    {
      EList<IQLCasePart> _cases = s.getCases();
      for(final IQLCasePart ca : _cases) {
        _builder.append("\t");
        String _compile_1 = this.compile(ca, c);
        _builder.append(_compile_1, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if (((s.getStatements() != null) && (s.getStatements().size() > 0))) {
        _builder.append("\t");
        _builder.append("default :");
        _builder.newLine();
        {
          EList<IQLStatement> _statements = s.getStatements();
          for(final IQLStatement stmt : _statements) {
            _builder.append("\t");
            _builder.append("\t");
            String _compile_2 = this.compile(stmt, c);
            _builder.append(_compile_2, "\t\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    String content = _builder.toString();
    boolean _hasException = c.hasException();
    if (_hasException) {
      return this.createTryCatchBlock(content, c);
    } else {
      return content;
    }
  }
  
  public String compile(final IQLCasePart cp, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("case ");
    String _compile = this.exprCompiler.compile(cp.getExpr(), c);
    _builder.append(_compile);
    _builder.append(" :");
    _builder.newLineIfNotEmpty();
    {
      EList<IQLStatement> _statements = cp.getStatements();
      for(final IQLStatement stmt : _statements) {
        _builder.append("\t");
        String _compile_1 = this.compile(stmt, c);
        _builder.append(_compile_1, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  public String compile(final IQLBreakStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("break;");
    return _builder.toString();
  }
  
  public String compile(final IQLContinueStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("continue;");
    return _builder.toString();
  }
  
  public String compile(final IQLReturnStatement s, final G c) {
    String _xblockexpression = null;
    {
      TypeResult typeResult = this.exprEvaluator.eval(s.getExpression(), c.getExpectedTypeRef());
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("return ");
      {
        IQLExpression _expression = s.getExpression();
        boolean _tripleNotEquals = (_expression != null);
        if (_tripleNotEquals) {
          String _compile = this.exprCompiler.compile(s.getExpression(), c);
          _builder.append(_compile);
        }
      }
      _builder.append(";");
      String content = _builder.toString();
      String _xifexpression = null;
      boolean _hasException = c.hasException();
      if (_hasException) {
        StringConcatenation _builder_1 = new StringConcatenation();
        String _createTryCatchBlock = this.createTryCatchBlock(content, c);
        _builder_1.append(_createTryCatchBlock);
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("return ");
        String _defaultLiteral = this.getDefaultLiteral(typeResult.getRef());
        _builder_1.append(_defaultLiteral);
        _builder_1.append(";");
        _builder_1.newLineIfNotEmpty();
        _xifexpression = _builder_1.toString();
      } else {
        return content;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String getDefaultLiteral(final JvmTypeReference typeRef) {
    if ((typeRef == null)) {
      return "null";
    } else {
      boolean _isPrimitive = this.typeUtils.isPrimitive(typeRef);
      if (_isPrimitive) {
        boolean _isByte = this.typeUtils.isByte(typeRef);
        if (_isByte) {
          return "0";
        } else {
          boolean _isShort = this.typeUtils.isShort(typeRef);
          if (_isShort) {
            return "0";
          } else {
            boolean _isInt = this.typeUtils.isInt(typeRef);
            if (_isInt) {
              return "0";
            } else {
              boolean _isLong = this.typeUtils.isLong(typeRef);
              if (_isLong) {
                return "0";
              } else {
                boolean _isFloat = this.typeUtils.isFloat(typeRef);
                if (_isFloat) {
                  return "0.0f";
                } else {
                  boolean _isDouble = this.typeUtils.isDouble(typeRef);
                  if (_isDouble) {
                    return "0.0";
                  } else {
                    boolean _isBoolean = this.typeUtils.isBoolean(typeRef);
                    if (_isBoolean) {
                      return "false";
                    } else {
                      boolean _isCharacter = this.typeUtils.isCharacter(typeRef);
                      if (_isCharacter) {
                        return "0";
                      }
                    }
                  }
                }
              }
            }
          }
        }
      } else {
        return "null";
      }
    }
    return null;
  }
  
  public String compile(final IQLConstructorCallStatement s, final G c) {
    IQLClass type = this.helper.getClass(s);
    String content = "";
    if ((type != null)) {
      JvmTypeReference typeRef = this.typeUtils.createTypeRef(type);
      JvmExecutable constructor = null;
      boolean _isSuper = s.isSuper();
      if (_isSuper) {
        constructor = this.lookUp.findSuperConstructor(typeRef, s.getArgs().getElements());
      } else {
        constructor = this.lookUp.findDeclaredConstructor(typeRef, s.getArgs().getElements());
      }
      if (((constructor != null) && s.isSuper())) {
        c.addExceptions(constructor.getExceptions());
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("super(");
        {
          IQLArgumentsList _args = s.getArgs();
          boolean _tripleNotEquals = (_args != null);
          if (_tripleNotEquals) {
            String _compile = this.exprCompiler.compile(s.getArgs(), constructor.getParameters(), c);
            _builder.append(_compile);
          }
        }
        _builder.append(");");
        content = _builder.toString();
      } else {
        if ((constructor != null)) {
          c.addExceptions(constructor.getExceptions());
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("this(");
          {
            IQLArgumentsList _args_1 = s.getArgs();
            boolean _tripleNotEquals_1 = (_args_1 != null);
            if (_tripleNotEquals_1) {
              String _compile_1 = this.exprCompiler.compile(s.getArgs(), constructor.getParameters(), c);
              _builder_1.append(_compile_1);
            }
          }
          _builder_1.append(");");
          content = _builder_1.toString();
        }
      }
    } else {
      boolean _isSuper_1 = s.isSuper();
      if (_isSuper_1) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("super(");
        {
          IQLArgumentsList _args_2 = s.getArgs();
          boolean _tripleNotEquals_2 = (_args_2 != null);
          if (_tripleNotEquals_2) {
            String _compile_2 = this.exprCompiler.compile(s.getArgs(), c);
            _builder_2.append(_compile_2);
          }
        }
        _builder_2.append(");");
        content = _builder_2.toString();
      } else {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("this(");
        {
          IQLArgumentsList _args_3 = s.getArgs();
          boolean _tripleNotEquals_3 = (_args_3 != null);
          if (_tripleNotEquals_3) {
            String _compile_3 = this.exprCompiler.compile(s.getArgs(), c);
            _builder_3.append(_compile_3);
          }
        }
        _builder_3.append(");");
        content = _builder_3.toString();
      }
    }
    boolean _hasException = c.hasException();
    if (_hasException) {
      return this.createTryCatchBlock(content, c);
    } else {
      return content;
    }
  }
  
  public String compile(final IQLVariableStatement s, final G c) {
    String _xblockexpression = null;
    {
      JvmIdentifiableElement _var = s.getVar();
      IQLVariableDeclaration leftVar = ((IQLVariableDeclaration) _var);
      JvmTypeReference leftType = leftVar.getRef();
      String content = "";
      if ((((s.getInit() != null) && (s.getInit().getArgsList() == null)) && (s.getInit().getArgsMap() == null))) {
        TypeResult right = this.exprEvaluator.eval(s.getInit().getValue(), leftType);
        if ((right.isNull() || this.lookUp.isAssignable(leftType, right.getRef()))) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append(" ");
          _builder.append("= ");
          String _compile = this.compile(s.getInit(), leftType, c);
          _builder.append(_compile, " ");
          _builder.append(";");
          content = _builder.toString();
        } else {
          if ((right.isNull() || this.lookUp.isCastable(leftType, right.getRef()))) {
            String target = this.typeCompiler.compile(leftType, c, false);
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append(" ");
            _builder_1.append("= ((");
            _builder_1.append(target, " ");
            _builder_1.append(")");
            String _compile_1 = this.compile(s.getInit(), leftType, c);
            _builder_1.append(_compile_1, " ");
            _builder_1.append(");");
            content = _builder_1.toString();
          } else {
            StringConcatenation _builder_2 = new StringConcatenation();
            _builder_2.append(" ");
            _builder_2.append("= ");
            String _compile_2 = this.compile(s.getInit(), leftType, c);
            _builder_2.append(_compile_2, " ");
            _builder_2.append(";");
            content = _builder_2.toString();
          }
        }
      } else {
        IQLVariableInitialization _init = s.getInit();
        boolean _tripleNotEquals = (_init != null);
        if (_tripleNotEquals) {
          StringConcatenation _builder_3 = new StringConcatenation();
          _builder_3.append(" ");
          _builder_3.append("= ");
          String _compile_3 = this.compile(s.getInit(), leftType, c);
          _builder_3.append(_compile_3, " ");
          _builder_3.append(";");
          content = _builder_3.toString();
        }
      }
      String _xifexpression = null;
      boolean _hasException = c.hasException();
      if (_hasException) {
        StringConcatenation _builder_4 = new StringConcatenation();
        String _compile_4 = this.compile(leftVar, c);
        _builder_4.append(_compile_4);
        _builder_4.append(" = ");
        String _defaultLiteral = this.getDefaultLiteral(leftType);
        _builder_4.append(_defaultLiteral);
        _builder_4.append(";");
        _builder_4.newLineIfNotEmpty();
        String _name = leftVar.getName();
        String _plus = (_name + content);
        String _createTryCatchBlock = this.createTryCatchBlock(_plus, c);
        _builder_4.append(_createTryCatchBlock);
        _builder_4.newLineIfNotEmpty();
        _xifexpression = _builder_4.toString();
      } else {
        String _compile_5 = this.compile(leftVar, c);
        return (_compile_5 + content);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLVariableDeclaration decl, final G context) {
    String _xblockexpression = null;
    {
      JvmTypeReference type = decl.getRef();
      StringConcatenation _builder = new StringConcatenation();
      String _compile = this.typeCompiler.compile(type, context, false);
      _builder.append(_compile);
      _builder.append(" ");
      String _name = decl.getName();
      _builder.append(_name);
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  @Override
  public String compile(final IQLVariableInitialization init, final JvmTypeReference typeRef, final G context) {
    String result = "";
    context.setExpectedTypeRef(typeRef);
    if ((((init.getArgsList() != null) && (init.getArgsMap() != null)) && (init.getArgsMap().getElements().size() > 0))) {
      JvmExecutable constructor = this.lookUp.findPublicConstructor(typeRef, init.getArgsList().getElements());
      if ((constructor != null)) {
        context.addExceptions(constructor.getExceptions());
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("get");
        String _shortName = this.typeUtils.getShortName(typeRef, false);
        _builder.append(_shortName);
        int _hashCode = typeRef.hashCode();
        _builder.append(_hashCode);
        _builder.append("(new ");
        String _compile = this.typeCompiler.compile(typeRef, context, true);
        _builder.append(_compile);
        _builder.append("(");
        String _compile_1 = this.exprCompiler.compile(init.getArgsList(), constructor.getParameters(), context);
        _builder.append(_compile_1);
        _builder.append("), ");
        String _compile_2 = this.exprCompiler.compile(init.getArgsMap(), typeRef, context);
        _builder.append(_compile_2);
        _builder.append(")");
        result = _builder.toString();
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("get");
        String _shortName_1 = this.typeUtils.getShortName(typeRef, false);
        _builder_1.append(_shortName_1);
        int _hashCode_1 = typeRef.hashCode();
        _builder_1.append(_hashCode_1);
        _builder_1.append("(new ");
        String _compile_3 = this.typeCompiler.compile(typeRef, context, true);
        _builder_1.append(_compile_3);
        _builder_1.append("(");
        String _compile_4 = this.exprCompiler.compile(init.getArgsList(), context);
        _builder_1.append(_compile_4);
        _builder_1.append("), ");
        String _compile_5 = this.exprCompiler.compile(init.getArgsMap(), typeRef, context);
        _builder_1.append(_compile_5);
        _builder_1.append(")");
        result = _builder_1.toString();
      }
    } else {
      if (((init.getArgsMap() != null) && (init.getArgsMap().getElements().size() > 0))) {
        ArrayList<IQLExpression> _arrayList = new ArrayList<IQLExpression>();
        JvmExecutable constructor_1 = this.lookUp.findPublicConstructor(typeRef, _arrayList);
        if ((constructor_1 != null)) {
          context.addExceptions(constructor_1.getExceptions());
        }
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("get");
        String _shortName_2 = this.typeUtils.getShortName(typeRef, false);
        _builder_2.append(_shortName_2);
        int _hashCode_2 = typeRef.hashCode();
        _builder_2.append(_hashCode_2);
        _builder_2.append("(new ");
        String _compile_6 = this.typeCompiler.compile(typeRef, context, true);
        _builder_2.append(_compile_6);
        _builder_2.append("(), ");
        String _compile_7 = this.exprCompiler.compile(init.getArgsMap(), typeRef, context);
        _builder_2.append(_compile_7);
        _builder_2.append(")");
        result = _builder_2.toString();
      } else {
        IQLArgumentsList _argsList = init.getArgsList();
        boolean _tripleNotEquals = (_argsList != null);
        if (_tripleNotEquals) {
          JvmExecutable constructor_2 = this.lookUp.findPublicConstructor(typeRef, init.getArgsList().getElements());
          if ((constructor_2 != null)) {
            context.addExceptions(constructor_2.getExceptions());
            StringConcatenation _builder_3 = new StringConcatenation();
            _builder_3.append("new ");
            String _compile_8 = this.typeCompiler.compile(typeRef, context, true);
            _builder_3.append(_compile_8);
            _builder_3.append("(");
            String _compile_9 = this.exprCompiler.compile(init.getArgsList(), constructor_2.getParameters(), context);
            _builder_3.append(_compile_9);
            _builder_3.append(")");
            result = _builder_3.toString();
          } else {
            boolean _isArray = this.typeUtils.isArray(typeRef);
            if (_isArray) {
              context.addImport(ArrayList.class.getCanonicalName());
              StringConcatenation _builder_4 = new StringConcatenation();
              _builder_4.append("new ");
              String _simpleName = ArrayList.class.getSimpleName();
              _builder_4.append(_simpleName);
              _builder_4.append("<>()");
              result = _builder_4.toString();
            } else {
              StringConcatenation _builder_5 = new StringConcatenation();
              _builder_5.append("new ");
              String _compile_10 = this.typeCompiler.compile(typeRef, context, true);
              _builder_5.append(_compile_10);
              _builder_5.append("(");
              String _compile_11 = this.exprCompiler.compile(init.getArgsList(), context);
              _builder_5.append(_compile_11);
              _builder_5.append(")");
              result = _builder_5.toString();
            }
          }
        } else {
          StringConcatenation _builder_6 = new StringConcatenation();
          String _compile_12 = this.exprCompiler.compile(init.getValue(), context);
          _builder_6.append(_compile_12);
          result = _builder_6.toString();
        }
      }
    }
    context.setExpectedTypeRef(null);
    return result;
  }
}
