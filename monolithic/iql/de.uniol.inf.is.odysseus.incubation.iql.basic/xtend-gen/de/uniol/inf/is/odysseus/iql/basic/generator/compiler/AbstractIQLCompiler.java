package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import java.util.Collection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public abstract class AbstractIQLCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, S extends IIQLStatementCompiler<G>, F extends IIQLTypeDictionary, U extends IIQLTypeUtils> implements IIQLCompiler<G> {
  protected H helper;
  
  protected T typeCompiler;
  
  protected F typeDictionary;
  
  protected S stmtCompiler;
  
  protected U typeUtils;
  
  public AbstractIQLCompiler(final H helper, final T typeCompiler, final S stmtCompiler, final F typeDictionary, final U typeUtils) {
    this.helper = helper;
    this.typeCompiler = typeCompiler;
    this.stmtCompiler = stmtCompiler;
    this.typeUtils = typeUtils;
    this.typeDictionary = typeDictionary;
  }
  
  @Override
  public String compile(final IQLClass c, final G context) {
    String _xblockexpression = null;
    {
      StringBuilder builder = new StringBuilder();
      EObject _eContainer = c.eContainer();
      IQLModelElement element = ((IQLModelElement) _eContainer);
      builder.append(this.compileClass(element, c, context));
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
  
  public String compileClass(final IQLModelElement element, final IQLClass c, final G context) {
    String _xblockexpression = null;
    {
      String name = c.getSimpleName();
      JvmTypeReference superClass = c.getExtendedClass();
      EList<JvmTypeReference> interfaces = c.getExtendedInterfaces();
      Collection<IQLNewExpression> newExpressions = this.helper.getNewExpressions(c);
      Collection<IQLAttribute> attributes = this.helper.getAttributes(c);
      Collection<IQLVariableStatement> varStmts = this.helper.getVarStatements(c);
      StringConcatenation _builder = new StringConcatenation();
      {
        EList<IQLJavaMetadata> _javametadata = element.getJavametadata();
        for(final IQLJavaMetadata j : _javametadata) {
          String text = j.getJava().getText();
          _builder.newLineIfNotEmpty();
          _builder.append(text);
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("@SuppressWarnings(\"all\")");
      _builder.newLine();
      _builder.append("public class ");
      _builder.append(name);
      {
        boolean _notEquals = (!Objects.equal(superClass, null));
        if (_notEquals) {
          _builder.append(" extends ");
          String _compile = this.typeCompiler.compile(superClass, context, true);
          _builder.append(_compile);
        }
      }
      {
        int _size = interfaces.size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          _builder.append(" implements ");
          final Function1<JvmTypeReference, String> _function = (JvmTypeReference el) -> {
            return this.typeCompiler.compile(el, context, true);
          };
          String _join = IterableExtensions.join(ListExtensions.<JvmTypeReference, String>map(interfaces, _function), ",");
          _builder.append(_join);
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
        for(final IQLNewExpression e : newExpressions) {
          {
            if (((!Objects.equal(e.getArgsMap(), null)) && (e.getArgsMap().getElements().size() > 0))) {
              _builder.append("\t");
              CharSequence _createGetterMethod = this.createGetterMethod(e.getRef(), e.getArgsMap(), context);
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
            if ((((!Objects.equal(a.getInit(), null)) && (!Objects.equal(a.getInit().getArgsMap(), null))) && (a.getInit().getArgsMap().getElements().size() > 0))) {
              _builder.append("\t");
              CharSequence _createGetterMethod_1 = this.createGetterMethod(a.getType(), a.getInit().getArgsMap(), context);
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
            if ((((!Objects.equal(a_1.getInit(), null)) && (!Objects.equal(a_1.getInit().getArgsMap(), null))) && (a_1.getInit().getArgsMap().getElements().size() > 0))) {
              _builder.append("\t");
              JvmIdentifiableElement _var = a_1.getVar();
              IQLVariableDeclaration decl = ((IQLVariableDeclaration) _var);
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              JvmTypeReference type = decl.getRef();
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              CharSequence _createGetterMethod_2 = this.createGetterMethod(type, a_1.getInit().getArgsMap(), context);
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
  
  @Override
  public String compile(final IQLInterface interf, final G context) {
    String _xblockexpression = null;
    {
      StringBuilder builder = new StringBuilder();
      EObject _eContainer = interf.eContainer();
      IQLModelElement element = ((IQLModelElement) _eContainer);
      builder.append(this.compileInterface(element, interf, context));
      Collection<String> _imports = context.getImports();
      for (final String i : _imports) {
        builder.insert(0, (("import " + i) + ";"));
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compileInterface(final IQLModelElement element, final IQLInterface i, final G context) {
    String _xblockexpression = null;
    {
      String name = i.getSimpleName();
      EList<JvmTypeReference> interfaces = i.getExtendedInterfaces();
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      {
        EList<IQLJavaMetadata> _javametadata = element.getJavametadata();
        for(final IQLJavaMetadata j : _javametadata) {
          String text = j.getJava().getText();
          _builder.newLineIfNotEmpty();
          _builder.append(text);
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("@SuppressWarnings(\"all\")");
      _builder.newLine();
      _builder.append("public interface ");
      _builder.append(name);
      {
        int _size = interfaces.size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          _builder.append(" extends ");
          final Function1<JvmTypeReference, String> _function = (JvmTypeReference el) -> {
            return this.typeCompiler.compile(el, context, true);
          };
          String _join = IterableExtensions.join(ListExtensions.<JvmTypeReference, String>map(interfaces, _function), ",");
          _builder.append(_join);
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
        if ((m instanceof IQLMethodDeclaration)) {
          _xifexpression_2 = this.compile(((IQLMethodDeclaration) m), context);
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
      String text = m.getJava().getText();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(text);
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLAttribute a, final G context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public ");
    String _compile = this.typeCompiler.compile(a.getType(), context, false);
    _builder.append(_compile);
    _builder.append(" ");
    String _simpleName = a.getSimpleName();
    _builder.append(_simpleName);
    {
      IQLVariableInitialization _init = a.getInit();
      boolean _notEquals = (!Objects.equal(_init, null));
      if (_notEquals) {
        _builder.append(" = ");
        String _compile_1 = this.stmtCompiler.compile(a.getInit(), a.getType(), context);
        _builder.append(_compile_1);
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
      if (((!Objects.equal(m.getReturnType(), null)) && (!m.getSimpleName().equalsIgnoreCase(className)))) {
        returnT = this.typeCompiler.compile(m.getReturnType(), context, false);
      } else {
        if ((Objects.equal(m.getReturnType(), null) && (!m.getSimpleName().equalsIgnoreCase(className)))) {
          returnT = "void";
        }
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
      _builder.append(returnT);
      _builder.append(" ");
      String _simpleName = m.getSimpleName();
      _builder.append(_simpleName);
      _builder.append("(");
      {
        EList<JvmFormalParameter> _parameters = m.getParameters();
        boolean _notEquals = (!Objects.equal(_parameters, null));
        if (_notEquals) {
          final Function1<JvmFormalParameter, String> _function = (JvmFormalParameter p) -> {
            return this.compile(p, context);
          };
          String _join = IterableExtensions.join(ListExtensions.<JvmFormalParameter, String>map(m.getParameters(), _function), ", ");
          _builder.append(_join);
        }
      }
      _builder.append(")");
      _builder.newLineIfNotEmpty();
      String _compile = this.stmtCompiler.compile(m.getBody(), context);
      _builder.append(_compile);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compile(final JvmFormalParameter p, final G context) {
    StringConcatenation _builder = new StringConcatenation();
    String _compile = this.typeCompiler.compile(p.getParameterType(), context, false);
    _builder.append(_compile);
    _builder.append(" ");
    String _name = p.getName();
    _builder.append(_name);
    return _builder.toString();
  }
  
  public String compile(final IQLMethodDeclaration m, final G context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public ");
    String _compile = this.typeCompiler.compile(m.getReturnType(), context, false);
    _builder.append(_compile);
    _builder.append(" ");
    String _simpleName = m.getSimpleName();
    _builder.append(_simpleName);
    _builder.append("(");
    final Function1<JvmFormalParameter, String> _function = (JvmFormalParameter p) -> {
      return this.compile(p, context);
    };
    String _join = IterableExtensions.join(ListExtensions.<JvmFormalParameter, String>map(m.getParameters(), _function), ", ");
    _builder.append(_join);
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
    _builder.append(_compile);
    _builder.append(" get");
    String _shortName = this.typeUtils.getShortName(typeRef, false);
    _builder.append(_shortName);
    int _hashCode = typeRef.hashCode();
    _builder.append(_hashCode);
    _builder.append("(");
    String _compile_1 = this.typeCompiler.compile(typeRef, context, false);
    _builder.append(_compile_1);
    _builder.append(" type, ");
    final Function1<IQLArgumentsMapKeyValue, String> _function = (IQLArgumentsMapKeyValue el) -> {
      return this.compile(el, typeRef, context);
    };
    String _join = IterableExtensions.join(ListExtensions.<IQLArgumentsMapKeyValue, String>map(map.getElements(), _function), ", ");
    _builder.append(_join);
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    {
      EList<IQLArgumentsMapKeyValue> _elements = map.getElements();
      for(final IQLArgumentsMapKeyValue el : _elements) {
        {
          JvmIdentifiableElement _key = el.getKey();
          if ((_key instanceof JvmOperation)) {
            _builder.append("\t");
            _builder.append("type.");
            String _simpleName = el.getKey().getSimpleName();
            _builder.append(_simpleName, "\t");
            _builder.append("(");
            String _simpleName_1 = el.getKey().getSimpleName();
            _builder.append(_simpleName_1, "\t");
            _builder.append(");\t\t\t\t");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("type.");
            String _simpleName_2 = el.getKey().getSimpleName();
            _builder.append(_simpleName_2, "\t");
            _builder.append(" = ");
            String _simpleName_3 = el.getKey().getSimpleName();
            _builder.append(_simpleName_3, "\t");
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
      JvmTypeReference type = this.helper.getPropertyType(e.getKey(), typeRef);
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(type, null));
      if (_notEquals) {
        StringConcatenation _builder = new StringConcatenation();
        String _compile = this.typeCompiler.compile(type, context, false);
        _builder.append(_compile);
        _builder.append(" ");
        String _simpleName = e.getKey().getSimpleName();
        _builder.append(_simpleName);
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
