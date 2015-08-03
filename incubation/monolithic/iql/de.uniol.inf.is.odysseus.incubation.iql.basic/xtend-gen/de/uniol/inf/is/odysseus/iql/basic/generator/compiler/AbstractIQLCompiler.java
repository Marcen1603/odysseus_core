package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclarationMember;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public abstract class AbstractIQLCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, S extends IIQLStatementCompiler<G>, F extends IIQLTypeFactory, U extends IIQLTypeUtils> implements IIQLCompiler<G> {
  protected H helper;
  
  protected T typeCompiler;
  
  protected F typeFactory;
  
  protected S stmtCompiler;
  
  protected U typeUtils;
  
  public AbstractIQLCompiler(final H helper, final T typeCompiler, final S stmtCompiler, final F typeFactory, final U typeUtils) {
    this.helper = helper;
    this.typeCompiler = typeCompiler;
    this.stmtCompiler = stmtCompiler;
    this.typeUtils = typeUtils;
    this.typeFactory = typeFactory;
  }
  
  public String compile(final IQLTypeDefinition typeDef, final IQLClass c, final G context) {
    String _xblockexpression = null;
    {
      StringBuilder builder = new StringBuilder();
      String _compileClass = this.compileClass(typeDef, c, context);
      builder.append(_compileClass);
      Collection<String> _imports = context.getImports();
      for (final String i : _imports) {
        String _lineSeparator = System.lineSeparator();
        String _plus = ((("import " + i) + ";") + _lineSeparator);
        builder.insert(0, _plus);
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compileClass(final IQLTypeDefinition typeDef, final IQLClass c, final G context) {
    String _xblockexpression = null;
    {
      String name = c.getSimpleName();
      JvmTypeReference superClass = c.getExtendedClass();
      EList<JvmTypeReference> interfaces = c.getExtendedInterfaces();
      Collection<IQLTerminalExpressionNew> newExpressions = this.helper.getNewExpressions(c);
      Collection<IQLAttribute> attributes = this.helper.getAttributes(c);
      Collection<IQLVariableStatement> varStmts = this.helper.getVarStatements(c);
      StringConcatenation _builder = new StringConcatenation();
      {
        EList<IQLJavaMetadata> _javametadata = typeDef.getJavametadata();
        for(final IQLJavaMetadata j : _javametadata) {
          IQLJava _text = j.getText();
          ICompositeNode _node = NodeModelUtils.getNode(_text);
          String text = NodeModelUtils.getTokenText(_node);
          _builder.newLineIfNotEmpty();
          _builder.append(text, "");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("@SuppressWarnings(\"all\")");
      _builder.newLine();
      _builder.append("public class ");
      _builder.append(name, "");
      {
        boolean _notEquals = (!Objects.equal(superClass, null));
        if (_notEquals) {
          _builder.append(" extends ");
          String _compile = this.typeCompiler.compile(superClass, context, true);
          _builder.append(_compile, "");
        }
      }
      {
        int _size = interfaces.size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          _builder.append(" implements ");
          final Function1<JvmTypeReference, String> _function = new Function1<JvmTypeReference, String>() {
            public String apply(final JvmTypeReference el) {
              return AbstractIQLCompiler.this.typeCompiler.compile(el, context, true);
            }
          };
          List<String> _map = ListExtensions.<JvmTypeReference, String>map(interfaces, _function);
          _builder.append(_map, "");
        }
      }
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      {
        EList<JvmMember> _members = c.getMembers();
        for(final JvmMember m : _members) {
          _builder.append("\t");
          String _compile_1 = this.compile(m, context);
          _builder.append(_compile_1, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      {
        for(final IQLTerminalExpressionNew e : newExpressions) {
          {
            boolean _and = false;
            IQLArgumentsMap _argsMap = e.getArgsMap();
            boolean _notEquals_1 = (!Objects.equal(_argsMap, null));
            if (!_notEquals_1) {
              _and = false;
            } else {
              IQLArgumentsMap _argsMap_1 = e.getArgsMap();
              EList<IQLArgumentsMapKeyValue> _elements = _argsMap_1.getElements();
              int _size_1 = _elements.size();
              boolean _greaterThan_1 = (_size_1 > 0);
              _and = _greaterThan_1;
            }
            if (_and) {
              _builder.append("\t");
              JvmTypeReference _ref = e.getRef();
              IQLArgumentsMap _argsMap_2 = e.getArgsMap();
              CharSequence _createGetterMethod = this.createGetterMethod(_ref, _argsMap_2, context);
              _builder.append(_createGetterMethod, "\t");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.append("\t");
      _builder.newLine();
      {
        for(final IQLAttribute a : attributes) {
          {
            boolean _and_1 = false;
            boolean _and_2 = false;
            IQLVariableInitialization _init = a.getInit();
            boolean _notEquals_2 = (!Objects.equal(_init, null));
            if (!_notEquals_2) {
              _and_2 = false;
            } else {
              IQLVariableInitialization _init_1 = a.getInit();
              IQLArgumentsMap _argsMap_3 = _init_1.getArgsMap();
              boolean _notEquals_3 = (!Objects.equal(_argsMap_3, null));
              _and_2 = _notEquals_3;
            }
            if (!_and_2) {
              _and_1 = false;
            } else {
              IQLVariableInitialization _init_2 = a.getInit();
              IQLArgumentsMap _argsMap_4 = _init_2.getArgsMap();
              EList<IQLArgumentsMapKeyValue> _elements_1 = _argsMap_4.getElements();
              int _size_2 = _elements_1.size();
              boolean _greaterThan_2 = (_size_2 > 0);
              _and_1 = _greaterThan_2;
            }
            if (_and_1) {
              _builder.append("\t");
              JvmTypeReference _type = a.getType();
              IQLVariableInitialization _init_3 = a.getInit();
              IQLArgumentsMap _argsMap_5 = _init_3.getArgsMap();
              CharSequence _createGetterMethod_1 = this.createGetterMethod(_type, _argsMap_5, context);
              _builder.append(_createGetterMethod_1, "\t");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.append("\t");
      _builder.newLine();
      {
        for(final IQLVariableStatement a_1 : varStmts) {
          {
            boolean _and_3 = false;
            boolean _and_4 = false;
            IQLVariableInitialization _init_4 = a_1.getInit();
            boolean _notEquals_4 = (!Objects.equal(_init_4, null));
            if (!_notEquals_4) {
              _and_4 = false;
            } else {
              IQLVariableInitialization _init_5 = a_1.getInit();
              IQLArgumentsMap _argsMap_6 = _init_5.getArgsMap();
              boolean _notEquals_5 = (!Objects.equal(_argsMap_6, null));
              _and_4 = _notEquals_5;
            }
            if (!_and_4) {
              _and_3 = false;
            } else {
              IQLVariableInitialization _init_6 = a_1.getInit();
              IQLArgumentsMap _argsMap_7 = _init_6.getArgsMap();
              EList<IQLArgumentsMapKeyValue> _elements_2 = _argsMap_7.getElements();
              int _size_3 = _elements_2.size();
              boolean _greaterThan_3 = (_size_3 > 0);
              _and_3 = _greaterThan_3;
            }
            if (_and_3) {
              _builder.append("\t");
              JvmIdentifiableElement _var = a_1.getVar();
              IQLVariableDeclaration decl = ((IQLVariableDeclaration) _var);
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              JvmTypeReference type = decl.getRef();
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              IQLVariableInitialization _init_7 = a_1.getInit();
              IQLArgumentsMap _argsMap_8 = _init_7.getArgsMap();
              CharSequence _createGetterMethod_2 = this.createGetterMethod(type, _argsMap_8, context);
              _builder.append(_createGetterMethod_2, "\t");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.append("\t");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLTypeDefinition typeDef, final IQLInterface interf, final G context) {
    String _xblockexpression = null;
    {
      StringBuilder builder = new StringBuilder();
      String _compileInterface = this.compileInterface(typeDef, interf, context);
      builder.append(_compileInterface);
      Collection<String> _imports = context.getImports();
      for (final String i : _imports) {
        builder.insert(0, (("import " + i) + ";"));
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compileInterface(final IQLTypeDefinition typeDef, final IQLInterface i, final G context) {
    String _xblockexpression = null;
    {
      String name = i.getSimpleName();
      EList<JvmTypeReference> interfaces = i.getExtendedInterfaces();
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      {
        EList<IQLJavaMetadata> _javametadata = typeDef.getJavametadata();
        for(final IQLJavaMetadata j : _javametadata) {
          IQLJava _text = j.getText();
          ICompositeNode _node = NodeModelUtils.getNode(_text);
          String text = NodeModelUtils.getTokenText(_node);
          _builder.newLineIfNotEmpty();
          _builder.append(text, "");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("@SuppressWarnings(\"all\")");
      _builder.newLine();
      _builder.append("public interface ");
      _builder.append(name, "");
      {
        int _size = interfaces.size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          _builder.append(" extends ");
          final Function1<JvmTypeReference, String> _function = new Function1<JvmTypeReference, String>() {
            public String apply(final JvmTypeReference el) {
              return AbstractIQLCompiler.this.typeCompiler.compile(el, context, true);
            }
          };
          List<String> _map = ListExtensions.<JvmTypeReference, String>map(interfaces, _function);
          _builder.append(_map, "");
        }
      }
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      {
        EList<JvmMember> _members = i.getMembers();
        for(final JvmMember m : _members) {
          _builder.append("\t");
          String _compile = this.compile(m, context);
          _builder.append(_compile, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compile(final JvmMember m, final G context) {
    String _xifexpression = null;
    if ((m instanceof IQLAttribute)) {
      _xifexpression = this.compile(((IQLAttribute) m), context);
    } else {
      String _xifexpression_1 = null;
      if ((m instanceof IQLMethod)) {
        _xifexpression_1 = this.compile(((IQLMethod) m), context);
      } else {
        String _xifexpression_2 = null;
        if ((m instanceof IQLMethodDeclarationMember)) {
          _xifexpression_2 = this.compile(((IQLMethodDeclarationMember) m), context);
        } else {
          String _xifexpression_3 = null;
          if ((m instanceof IQLJavaMember)) {
            _xifexpression_3 = this.compile(((IQLJavaMember) m), context);
          } else {
            StringConcatenation _builder = new StringConcatenation();
            _xifexpression_3 = _builder.toString();
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String compile(final IQLJavaMember m, final G context) {
    String _xblockexpression = null;
    {
      IQLJava _text = m.getText();
      ICompositeNode _node = NodeModelUtils.getNode(_text);
      String text = NodeModelUtils.getTokenText(_node);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(text, "");
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLAttribute a, final G context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public ");
    JvmTypeReference _type = a.getType();
    String _compile = this.typeCompiler.compile(_type, context, false);
    _builder.append(_compile, "");
    _builder.append(" ");
    String _simpleName = a.getSimpleName();
    _builder.append(_simpleName, "");
    {
      IQLVariableInitialization _init = a.getInit();
      boolean _notEquals = (!Objects.equal(_init, null));
      if (_notEquals) {
        _builder.append(" = ");
        IQLVariableInitialization _init_1 = a.getInit();
        JvmTypeReference _type_1 = a.getType();
        String _compile_1 = this.stmtCompiler.compile(_init_1, _type_1, context);
        _builder.append(_compile_1, "");
      }
    }
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    return _builder.toString();
  }
  
  public String compile(final IQLMethod m, final G context) {
    String _xblockexpression = null;
    {
      String className = this.helper.getClassName(m);
      String returnT = "";
      boolean _and = false;
      JvmTypeReference _returnType = m.getReturnType();
      boolean _equals = Objects.equal(_returnType, null);
      if (!_equals) {
        _and = false;
      } else {
        String _simpleName = m.getSimpleName();
        boolean _equalsIgnoreCase = _simpleName.equalsIgnoreCase(className);
        boolean _not = (!_equalsIgnoreCase);
        _and = _not;
      }
      if (_and) {
        JvmTypeReference _returnType_1 = m.getReturnType();
        String _compile = this.typeCompiler.compile(_returnType_1, context, false);
        returnT = _compile;
      }
      StringConcatenation _builder = new StringConcatenation();
      {
        boolean _isOverride = m.isOverride();
        if (_isOverride) {
          _builder.append("@Override");
        }
      }
      _builder.newLineIfNotEmpty();
      _builder.append("public ");
      _builder.append(returnT, "");
      _builder.append(" ");
      String _simpleName_1 = m.getSimpleName();
      _builder.append(_simpleName_1, "");
      _builder.append("(");
      {
        EList<JvmFormalParameter> _parameters = m.getParameters();
        boolean _notEquals = (!Objects.equal(_parameters, null));
        if (_notEquals) {
          EList<JvmFormalParameter> _parameters_1 = m.getParameters();
          final Function1<JvmFormalParameter, String> _function = new Function1<JvmFormalParameter, String>() {
            public String apply(final JvmFormalParameter p) {
              return AbstractIQLCompiler.this.compile(p, context);
            }
          };
          List<String> _map = ListExtensions.<JvmFormalParameter, String>map(_parameters_1, _function);
          String _join = IterableExtensions.join(_map, ", ");
          _builder.append(_join, "");
        }
      }
      _builder.append(")");
      _builder.newLineIfNotEmpty();
      IQLStatement _body = m.getBody();
      String _compile_1 = this.stmtCompiler.compile(_body, context);
      _builder.append(_compile_1, "");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compile(final JvmFormalParameter p, final G context) {
    StringConcatenation _builder = new StringConcatenation();
    JvmTypeReference _parameterType = p.getParameterType();
    String _compile = this.typeCompiler.compile(_parameterType, context, false);
    _builder.append(_compile, "");
    _builder.append(" ");
    String _name = p.getName();
    _builder.append(_name, "");
    return _builder.toString();
  }
  
  public String compile(final IQLMethodDeclarationMember m, final G context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public ");
    JvmTypeReference _returnType = m.getReturnType();
    String _compile = this.typeCompiler.compile(_returnType, context, false);
    _builder.append(_compile, "");
    _builder.append(" ");
    String _simpleName = m.getSimpleName();
    _builder.append(_simpleName, "");
    _builder.append("(");
    EList<JvmFormalParameter> _parameters = m.getParameters();
    final Function1<JvmFormalParameter, String> _function = new Function1<JvmFormalParameter, String>() {
      public String apply(final JvmFormalParameter p) {
        return AbstractIQLCompiler.this.compile(p, context);
      }
    };
    List<String> _map = ListExtensions.<JvmFormalParameter, String>map(_parameters, _function);
    String _join = IterableExtensions.join(_map, ", ");
    _builder.append(_join, "");
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    return _builder.toString();
  }
  
  public CharSequence createGetterMethod(final JvmTypeReference typeRef, final IQLArgumentsMap map, final G context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("private ");
    String _compile = this.typeCompiler.compile(typeRef, context, false);
    _builder.append(_compile, "");
    _builder.append(" get");
    String _shortName = this.typeUtils.getShortName(typeRef, false);
    _builder.append(_shortName, "");
    int _hashCode = typeRef.hashCode();
    _builder.append(_hashCode, "");
    _builder.append("(");
    String _compile_1 = this.typeCompiler.compile(typeRef, context, false);
    _builder.append(_compile_1, "");
    _builder.append(" type, ");
    EList<IQLArgumentsMapKeyValue> _elements = map.getElements();
    final Function1<IQLArgumentsMapKeyValue, String> _function = new Function1<IQLArgumentsMapKeyValue, String>() {
      public String apply(final IQLArgumentsMapKeyValue el) {
        return AbstractIQLCompiler.this.compile(el, typeRef, context);
      }
    };
    List<String> _map = ListExtensions.<IQLArgumentsMapKeyValue, String>map(_elements, _function);
    String _join = IterableExtensions.join(_map, ", ");
    _builder.append(_join, "");
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    {
      EList<IQLArgumentsMapKeyValue> _elements_1 = map.getElements();
      for(final IQLArgumentsMapKeyValue el : _elements_1) {
        _builder.append("\t");
        String _key = el.getKey();
        JvmTypeReference type = this.helper.getPropertyType(_key, typeRef);
        _builder.newLineIfNotEmpty();
        {
          boolean _and = false;
          boolean _notEquals = (!Objects.equal(type, null));
          if (!_notEquals) {
            _and = false;
          } else {
            String _key_1 = el.getKey();
            boolean _isSetter = this.helper.isSetter(_key_1, typeRef, type);
            _and = _isSetter;
          }
          if (_and) {
            _builder.append("\t");
            String _key_2 = el.getKey();
            String _plus = ("set" + _key_2);
            String methodName = this.helper.getMethodName(_plus, typeRef);
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("type.");
            _builder.append(methodName, "\t");
            _builder.append("(");
            String _key_3 = el.getKey();
            _builder.append(_key_3, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("type.");
            String _key_4 = el.getKey();
            _builder.append(_key_4, "\t");
            _builder.append(" = ");
            String _key_5 = el.getKey();
            _builder.append(_key_5, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return type;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public String compile(final IQLArgumentsMapKeyValue e, final JvmTypeReference typeRef, final G context) {
    String _xblockexpression = null;
    {
      String _key = e.getKey();
      JvmTypeReference type = this.helper.getPropertyType(_key, typeRef);
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(type, null));
      if (_notEquals) {
        StringConcatenation _builder = new StringConcatenation();
        String _compile = this.typeCompiler.compile(type, context, false);
        _builder.append(_compile, "");
        _builder.append(" ");
        String _key_1 = e.getKey();
        _builder.append(_key_1, "");
        _xifexpression = _builder.toString();
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _xifexpression = _builder_1.toString();
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
}
