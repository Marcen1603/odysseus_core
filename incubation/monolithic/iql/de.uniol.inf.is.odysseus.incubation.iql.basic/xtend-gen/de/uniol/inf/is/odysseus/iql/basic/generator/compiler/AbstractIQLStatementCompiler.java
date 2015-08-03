package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
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
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLReturnStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.TypeResult;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.IIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

@SuppressWarnings("all")
public abstract class AbstractIQLStatementCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, E extends IIQLExpressionCompiler<G>, U extends IIQLTypeUtils, P extends IIQLExpressionParser, L extends IIQLLookUp> implements IIQLStatementCompiler<G> {
  protected H helper;
  
  protected E exprCompiler;
  
  protected T typeCompiler;
  
  protected P exprParser;
  
  protected L lookUp;
  
  protected U typeUtils;
  
  public AbstractIQLStatementCompiler(final H helper, final E exprCompiler, final T typeCompiler, final U typeUtils, final P exprParser, final L lookUp) {
    this.helper = helper;
    this.exprCompiler = exprCompiler;
    this.typeCompiler = typeCompiler;
    this.typeUtils = typeUtils;
    this.exprParser = exprParser;
    this.lookUp = lookUp;
  }
  
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
      IQLJava _text = s.getText();
      ICompositeNode _node = NodeModelUtils.getNode(_text);
      String text = NodeModelUtils.getTokenText(_node);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(text, "");
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
  
  public String compile(final IQLExpressionStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    IQLExpression _expression = s.getExpression();
    String _compile = this.exprCompiler.compile(_expression, c);
    _builder.append(_compile, "");
    _builder.append(";");
    return _builder.toString();
  }
  
  public String compile(final IQLIfStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if(");
    IQLExpression _predicate = s.getPredicate();
    String _compile = this.exprCompiler.compile(_predicate, c);
    _builder.append(_compile, "");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    IQLStatement _thenBody = s.getThenBody();
    String _compile_1 = this.compile(_thenBody, c);
    _builder.append(_compile_1, "\t");
    _builder.newLineIfNotEmpty();
    {
      IQLStatement _elseBody = s.getElseBody();
      boolean _notEquals = (!Objects.equal(_elseBody, null));
      if (_notEquals) {
        _builder.append("else");
        _builder.newLine();
        _builder.append("\t");
        IQLStatement _elseBody_1 = s.getElseBody();
        String _compile_2 = this.compile(_elseBody_1, c);
        _builder.append(_compile_2, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  public String compile(final IQLWhileStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("while(");
    IQLExpression _predicate = s.getPredicate();
    String _compile = this.exprCompiler.compile(_predicate, c);
    _builder.append(_compile, "");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    IQLStatement _body = s.getBody();
    String _compile_1 = this.compile(_body, c);
    _builder.append(_compile_1, "\t");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String compile(final IQLDoWhileStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("do");
    _builder.newLine();
    _builder.append("\t");
    IQLStatement _body = s.getBody();
    String _compile = this.compile(_body, c);
    _builder.append(_compile, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("while(");
    IQLExpression _predicate = s.getPredicate();
    String _compile_1 = this.exprCompiler.compile(_predicate, c);
    _builder.append(_compile_1, "");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String compile(final IQLForStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("for (");
    IQLStatement _var = s.getVar();
    String _compile = this.compile(_var, c);
    _builder.append(_compile, "");
    _builder.append(" ");
    IQLStatement _predicate = s.getPredicate();
    String _compile_1 = this.compile(_predicate, c);
    _builder.append(_compile_1, "");
    _builder.append(" ");
    IQLExpression _updateExpr = s.getUpdateExpr();
    String _compile_2 = this.exprCompiler.compile(_updateExpr, c);
    _builder.append(_compile_2, "");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    IQLStatement _body = s.getBody();
    String _compile_3 = this.compile(_body, c);
    _builder.append(_compile_3, "\t");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String compile(final IQLForEachStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("for (");
    JvmIdentifiableElement _var = s.getVar();
    String _compile = this.compile(((IQLVariableDeclaration) _var), c);
    _builder.append(_compile, "");
    _builder.append(" : ");
    IQLExpression _forExpression = s.getForExpression();
    String _compile_1 = this.exprCompiler.compile(_forExpression, c);
    _builder.append(_compile_1, "");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    IQLStatement _body = s.getBody();
    String _compile_2 = this.compile(_body, c);
    _builder.append(_compile_2, "\t");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String compile(final IQLSwitchStatement s, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("switch (");
    IQLExpression _expr = s.getExpr();
    String _compile = this.exprCompiler.compile(_expr, c);
    _builder.append(_compile, "");
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
      IQLStatement _default = s.getDefault();
      boolean _notEquals = (!Objects.equal(_default, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("default :");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        IQLStatement _default_1 = s.getDefault();
        String _compile_2 = this.compile(_default_1, c);
        _builder.append(_compile_2, "\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String compile(final IQLCasePart cp, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("case ");
    IQLExpression _expr = cp.getExpr();
    String _compile = this.exprCompiler.compile(_expr, c);
    _builder.append(_compile, "");
    _builder.append(" :");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    IQLStatement _body = cp.getBody();
    String _compile_1 = this.compile(_body, c);
    _builder.append(_compile_1, "\t");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String compile(final IQLVariableStatement s, final G c) {
    String _xblockexpression = null;
    {
      JvmIdentifiableElement _var = s.getVar();
      IQLVariableDeclaration leftVar = ((IQLVariableDeclaration) _var);
      JvmTypeReference leftType = leftVar.getRef();
      String _xifexpression = null;
      boolean _and = false;
      boolean _and_1 = false;
      IQLVariableInitialization _init = s.getInit();
      boolean _notEquals = (!Objects.equal(_init, null));
      if (!_notEquals) {
        _and_1 = false;
      } else {
        IQLVariableInitialization _init_1 = s.getInit();
        IQLArgumentsList _argsList = _init_1.getArgsList();
        boolean _equals = Objects.equal(_argsList, null);
        _and_1 = _equals;
      }
      if (!_and_1) {
        _and = false;
      } else {
        IQLVariableInitialization _init_2 = s.getInit();
        IQLArgumentsMap _argsMap = _init_2.getArgsMap();
        boolean _equals_1 = Objects.equal(_argsMap, null);
        _and = _equals_1;
      }
      if (_and) {
        String _xblockexpression_1 = null;
        {
          IQLVariableInitialization _init_3 = s.getInit();
          IQLExpression _value = _init_3.getValue();
          TypeResult right = this.exprParser.getType(_value, leftType);
          String _xifexpression_1 = null;
          boolean _or = false;
          boolean _isNull = right.isNull();
          if (_isNull) {
            _or = true;
          } else {
            JvmTypeReference _ref = right.getRef();
            boolean _isAssignable = this.lookUp.isAssignable(leftType, _ref);
            _or = _isAssignable;
          }
          if (_or) {
            StringConcatenation _builder = new StringConcatenation();
            String _compile = this.compile(leftVar, c);
            _builder.append(_compile, "");
            {
              IQLVariableInitialization _init_4 = s.getInit();
              boolean _notEquals_1 = (!Objects.equal(_init_4, null));
              if (_notEquals_1) {
                _builder.append(" = ");
                IQLVariableInitialization _init_5 = s.getInit();
                String _compile_1 = this.compile(_init_5, leftType, c);
                _builder.append(_compile_1, "");
              }
            }
            _builder.append(";");
            _xifexpression_1 = _builder.toString();
          } else {
            String _xblockexpression_2 = null;
            {
              String target = this.typeCompiler.compile(leftType, c, false);
              StringConcatenation _builder_1 = new StringConcatenation();
              String _compile_2 = this.compile(leftVar, c);
              _builder_1.append(_compile_2, "");
              {
                IQLVariableInitialization _init_6 = s.getInit();
                boolean _notEquals_2 = (!Objects.equal(_init_6, null));
                if (_notEquals_2) {
                  _builder_1.append(" = ((");
                  _builder_1.append(target, "");
                  _builder_1.append(")");
                  IQLVariableInitialization _init_7 = s.getInit();
                  String _compile_3 = this.compile(_init_7, leftType, c);
                  _builder_1.append(_compile_3, "");
                }
              }
              _builder_1.append(");");
              _xblockexpression_2 = _builder_1.toString();
            }
            _xifexpression_1 = _xblockexpression_2;
          }
          _xblockexpression_1 = _xifexpression_1;
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        IQLVariableInitialization _init_3 = s.getInit();
        boolean _notEquals_1 = (!Objects.equal(_init_3, null));
        if (_notEquals_1) {
          StringConcatenation _builder = new StringConcatenation();
          String _compile = this.compile(leftVar, c);
          _builder.append(_compile, "");
          _builder.append(" = ");
          IQLVariableInitialization _init_4 = s.getInit();
          String _compile_1 = this.compile(_init_4, leftType, c);
          _builder.append(_compile_1, "");
          _builder.append(";");
          _xifexpression_1 = _builder.toString();
        } else {
          StringConcatenation _builder_1 = new StringConcatenation();
          String _compile_2 = this.compile(leftVar, c);
          _builder_1.append(_compile_2, "");
          _builder_1.append(";");
          _xifexpression_1 = _builder_1.toString();
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
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
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("return ");
    IQLExpression _expression = s.getExpression();
    String _compile = this.exprCompiler.compile(_expression, c);
    _builder.append(_compile, "");
    _builder.append(";");
    return _builder.toString();
  }
  
  public String compile(final IQLConstructorCallStatement s, final G c) {
    String _xblockexpression = null;
    {
      IQLClass type = this.helper.getClass(s);
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(type, null));
      if (_notEquals) {
        String _xblockexpression_1 = null;
        {
          JvmTypeReference typeRef = this.typeUtils.createTypeRef(type);
          String _keyword = s.getKeyword();
          boolean _equalsIgnoreCase = _keyword.equalsIgnoreCase("super");
          if (_equalsIgnoreCase) {
            JvmTypeReference _extendedClass = type.getExtendedClass();
            typeRef = _extendedClass;
          }
          IQLArgumentsList _args = s.getArgs();
          EList<IQLExpression> _elements = _args.getElements();
          int _size = _elements.size();
          JvmExecutable constructor = this.lookUp.findConstructor(typeRef, _size);
          String _xifexpression_1 = null;
          boolean _notEquals_1 = (!Objects.equal(constructor, null));
          if (_notEquals_1) {
            StringConcatenation _builder = new StringConcatenation();
            String _keyword_1 = s.getKeyword();
            _builder.append(_keyword_1, "");
            _builder.append("(");
            {
              IQLArgumentsList _args_1 = s.getArgs();
              boolean _notEquals_2 = (!Objects.equal(_args_1, null));
              if (_notEquals_2) {
                IQLArgumentsList _args_2 = s.getArgs();
                EList<JvmFormalParameter> _parameters = constructor.getParameters();
                String _compile = this.exprCompiler.compile(_args_2, _parameters, c);
                _builder.append(_compile, "");
              }
            }
            _builder.append(");");
            _xifexpression_1 = _builder.toString();
          }
          _xblockexpression_1 = _xifexpression_1;
        }
        _xifexpression = _xblockexpression_1;
      } else {
        StringConcatenation _builder = new StringConcatenation();
        String _keyword = s.getKeyword();
        _builder.append(_keyword, "");
        _builder.append("(");
        {
          IQLArgumentsList _args = s.getArgs();
          boolean _notEquals_1 = (!Objects.equal(_args, null));
          if (_notEquals_1) {
            IQLArgumentsList _args_1 = s.getArgs();
            String _compile = this.exprCompiler.compile(_args_1, c);
            _builder.append(_compile, "");
          }
        }
        _builder.append(");");
        _xifexpression = _builder.toString();
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
      _builder.append(_compile, "");
      _builder.append(" ");
      String _name = decl.getName();
      _builder.append(_name, "");
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLVariableInitialization init, final JvmTypeReference typeRef, final G context) {
    String result = "";
    context.setExpectedTypeRef(typeRef);
    boolean _and = false;
    IQLArgumentsMap _argsMap = init.getArgsMap();
    boolean _notEquals = (!Objects.equal(_argsMap, null));
    if (!_notEquals) {
      _and = false;
    } else {
      IQLArgumentsMap _argsMap_1 = init.getArgsMap();
      EList<IQLArgumentsMapKeyValue> _elements = _argsMap_1.getElements();
      int _size = _elements.size();
      boolean _greaterThan = (_size > 0);
      _and = _greaterThan;
    }
    if (_and) {
      IQLArgumentsList _argsList = init.getArgsList();
      EList<IQLExpression> _elements_1 = _argsList.getElements();
      int _size_1 = _elements_1.size();
      JvmExecutable constructor = this.lookUp.findConstructor(typeRef, _size_1);
      boolean _notEquals_1 = (!Objects.equal(constructor, null));
      if (_notEquals_1) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("get");
        String _shortName = this.typeUtils.getShortName(typeRef, false);
        _builder.append(_shortName, "");
        int _hashCode = typeRef.hashCode();
        _builder.append(_hashCode, "");
        _builder.append("(new ");
        String _compile = this.typeCompiler.compile(typeRef, context, true);
        _builder.append(_compile, "");
        _builder.append("(");
        IQLArgumentsList _argsList_1 = init.getArgsList();
        EList<JvmFormalParameter> _parameters = constructor.getParameters();
        String _compile_1 = this.exprCompiler.compile(_argsList_1, _parameters, context);
        _builder.append(_compile_1, "");
        _builder.append("), ");
        IQLArgumentsMap _argsMap_2 = init.getArgsMap();
        String _compile_2 = this.exprCompiler.compile(_argsMap_2, typeRef, context);
        _builder.append(_compile_2, "");
        _builder.append(")");
        result = _builder.toString();
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("get");
        String _shortName_1 = this.typeUtils.getShortName(typeRef, false);
        _builder_1.append(_shortName_1, "");
        int _hashCode_1 = typeRef.hashCode();
        _builder_1.append(_hashCode_1, "");
        _builder_1.append("(new ");
        String _compile_3 = this.typeCompiler.compile(typeRef, context, true);
        _builder_1.append(_compile_3, "");
        _builder_1.append("(");
        IQLArgumentsList _argsList_2 = init.getArgsList();
        String _compile_4 = this.exprCompiler.compile(_argsList_2, context);
        _builder_1.append(_compile_4, "");
        _builder_1.append("), ");
        IQLArgumentsMap _argsMap_3 = init.getArgsMap();
        String _compile_5 = this.exprCompiler.compile(_argsMap_3, typeRef, context);
        _builder_1.append(_compile_5, "");
        _builder_1.append(")");
        result = _builder_1.toString();
      }
    } else {
      IQLArgumentsList _argsList_3 = init.getArgsList();
      boolean _notEquals_2 = (!Objects.equal(_argsList_3, null));
      if (_notEquals_2) {
        IQLArgumentsList _argsList_4 = init.getArgsList();
        EList<IQLExpression> _elements_2 = _argsList_4.getElements();
        int _size_2 = _elements_2.size();
        JvmExecutable constructor_1 = this.lookUp.findConstructor(typeRef, _size_2);
        boolean _notEquals_3 = (!Objects.equal(constructor_1, null));
        if (_notEquals_3) {
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append("new ");
          String _compile_6 = this.typeCompiler.compile(typeRef, context, true);
          _builder_2.append(_compile_6, "");
          _builder_2.append("(");
          IQLArgumentsList _argsList_5 = init.getArgsList();
          EList<JvmFormalParameter> _parameters_1 = constructor_1.getParameters();
          String _compile_7 = this.exprCompiler.compile(_argsList_5, _parameters_1, context);
          _builder_2.append(_compile_7, "");
          _builder_2.append(")");
          result = _builder_2.toString();
        } else {
          StringConcatenation _builder_3 = new StringConcatenation();
          _builder_3.append("new ");
          String _compile_8 = this.typeCompiler.compile(typeRef, context, true);
          _builder_3.append(_compile_8, "");
          _builder_3.append("(");
          IQLArgumentsList _argsList_6 = init.getArgsList();
          String _compile_9 = this.exprCompiler.compile(_argsList_6, context);
          _builder_3.append(_compile_9, "");
          _builder_3.append(")");
          result = _builder_3.toString();
        }
      } else {
        StringConcatenation _builder_4 = new StringConcatenation();
        IQLExpression _value = init.getValue();
        String _compile_10 = this.exprCompiler.compile(_value, context);
        _builder_4.append(_compile_10, "");
        result = _builder_4.toString();
      }
    }
    context.setExpectedTypeRef(null);
    return result;
  }
}
