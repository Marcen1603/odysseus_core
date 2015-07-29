package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.types.IQLUtils;
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;
import de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator.AbstractODLAO;
import de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator.AbstractODLAORule;
import de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator.AbstractODLPO;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
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
public class ODLCompiler extends AbstractIQLCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLTypeCompiler, ODLStatementCompiler, ODLTypeFactory> {
  @Inject
  private ODLMetadataAnnotationCompiler metadataAnnotationCompiler;
  
  @Inject
  public ODLCompiler(final ODLCompilerHelper helper, final ODLTypeCompiler typeCompiler, final ODLStatementCompiler stmtCompiler, final ODLTypeFactory factory) {
    super(helper, typeCompiler, stmtCompiler, factory);
  }
  
  public String compileAO(final ODLOperator o, final ODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      context.setAo(true);
      StringBuilder builder = new StringBuilder();
      String _compileAOIntern = this.compileAOIntern(o, context);
      builder.append(_compileAOIntern);
      String _canonicalName = AbstractODLAO.class.getCanonicalName();
      context.addImport(_canonicalName);
      String _canonicalName_1 = LogicalOperatorCategory.class.getCanonicalName();
      context.addImport(_canonicalName_1);
      String _canonicalName_2 = LogicalOperator.class.getCanonicalName();
      context.addImport(_canonicalName_2);
      String _canonicalName_3 = Parameter.class.getCanonicalName();
      context.addImport(_canonicalName_3);
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
  
  public String compilePO(final ODLOperator o, final ODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      StringBuilder builder = new StringBuilder();
      String _compilePOIntern = this.compilePOIntern(o, context);
      builder.append(_compilePOIntern);
      String _canonicalName = AbstractODLPO.class.getCanonicalName();
      context.addImport(_canonicalName);
      String _canonicalName_1 = Tuple.class.getCanonicalName();
      context.addImport(_canonicalName_1);
      String _canonicalName_2 = IMetaAttribute.class.getCanonicalName();
      context.addImport(_canonicalName_2);
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
  
  public String compileAORule(final ODLOperator o, final ODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      StringBuilder builder = new StringBuilder();
      String _compileAORuleIntern = this.compileAORuleIntern(o, context);
      builder.append(_compileAORuleIntern);
      String _canonicalName = AbstractODLAORule.class.getCanonicalName();
      context.addImport(_canonicalName);
      String _canonicalName_1 = TransformationConfiguration.class.getCanonicalName();
      context.addImport(_canonicalName_1);
      String _canonicalName_2 = RuleException.class.getCanonicalName();
      context.addImport(_canonicalName_2);
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
  
  public String compileAOIntern(final ODLOperator o, final ODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      String _simpleName = o.getSimpleName();
      String opName = (_simpleName + ODLCompilerHelper.AO_OPERATOR);
      String superClass = AbstractODLAO.class.getSimpleName();
      Collection<ODLParameter> parameters = this.helper.getParameters(o);
      Collection<IQLTerminalExpressionNew> newExpressions = this.helper.getNewExpressions(o);
      Collection<IQLVariableStatement> varStmts = this.helper.getVarStatements(o);
      Collection<ODLMethod> odlmethods = this.helper.getODLMethods(o);
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("@SuppressWarnings(\"all\")");
      _builder.newLine();
      _builder.append("@");
      String _simpleName_1 = LogicalOperator.class.getSimpleName();
      _builder.append(_simpleName_1, "");
      _builder.append("(");
      String _aOAnnotationElements = this.metadataAnnotationCompiler.getAOAnnotationElements(o, context);
      _builder.append(_aOAnnotationElements, "");
      _builder.append(")");
      _builder.newLineIfNotEmpty();
      _builder.append("public class ");
      _builder.append(opName, "");
      _builder.append(" extends ");
      _builder.append(superClass, "");
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      {
        for(final ODLParameter p : parameters) {
          _builder.append("\t");
          String _compile = this.compile(p, context);
          _builder.append(_compile, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public ");
      _builder.append(opName, "\t");
      _builder.append("() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("super();");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public ");
      _builder.append(opName, "\t");
      _builder.append("(");
      _builder.append(opName, "\t");
      _builder.append(" ao) {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("super(ao);");
      _builder.newLine();
      {
        for(final ODLParameter p_1 : parameters) {
          _builder.append("\t\t");
          String _createCloneStatements = this.createCloneStatements(p_1, "ao", context);
          _builder.append(_createCloneStatements, "\t\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      {
        for(final ODLParameter p_2 : parameters) {
          _builder.append("\t");
          String _simpleName_2 = p_2.getSimpleName();
          String pName = this.helper.firstCharUpperCase(_simpleName_2);
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          JvmTypeReference _type = p_2.getType();
          String type = this.typeCompiler.compile(_type, context, false);
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          boolean validate = this.helper.hasValidateMethod(o, p_2);
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("@");
          String _simpleName_3 = Parameter.class.getSimpleName();
          _builder.append(_simpleName_3, "\t");
          _builder.append("(");
          String _parameterAnnotationElements = this.metadataAnnotationCompiler.getParameterAnnotationElements(p_2, context);
          _builder.append(_parameterAnnotationElements, "\t");
          _builder.append(")");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("public void set");
          _builder.append(pName, "\t");
          _builder.append("(");
          _builder.append(type, "\t");
          _builder.append(" ");
          String _simpleName_4 = p_2.getSimpleName();
          _builder.append(_simpleName_4, "\t");
          _builder.append(") {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("this.");
          String _simpleName_5 = p_2.getSimpleName();
          _builder.append(_simpleName_5, "\t\t");
          _builder.append(" = ");
          String _simpleName_6 = p_2.getSimpleName();
          _builder.append(_simpleName_6, "\t\t");
          _builder.append(";");
          _builder.newLineIfNotEmpty();
          {
            if (validate) {
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("this.validate");
              String _simpleName_7 = p_2.getSimpleName();
              _builder.append(_simpleName_7, "\t\t");
              _builder.append("();");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.append("\t");
          _builder.append("}");
          _builder.newLine();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      {
        for(final ODLParameter p_3 : parameters) {
          _builder.append("\t");
          String _simpleName_8 = p_3.getSimpleName();
          String pName_1 = this.helper.firstCharUpperCase(_simpleName_8);
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          JvmTypeReference _type_1 = p_3.getType();
          String type_1 = this.typeCompiler.compile(_type_1, context, false);
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("public ");
          _builder.append(type_1, "\t");
          _builder.append(" get");
          _builder.append(pName_1, "\t");
          _builder.append("() {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("return this.");
          String _simpleName_9 = p_3.getSimpleName();
          _builder.append(_simpleName_9, "\t\t");
          _builder.append(";");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("}");
          _builder.newLine();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      {
        for(final ODLMethod m : odlmethods) {
          _builder.append("\t");
          String _compile_1 = this.compile(m, context);
          _builder.append(_compile_1, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public ");
      _builder.append(superClass, "\t");
      _builder.append(" clone() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("return new ");
      _builder.append(opName, "\t\t");
      _builder.append("(this);");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      {
        for(final IQLTerminalExpressionNew e : newExpressions) {
          {
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
        for(final ODLParameter a : parameters) {
          {
            boolean _and_1 = false;
            boolean _and_2 = false;
            IQLVariableInitialization _init = a.getInit();
            boolean _notEquals_1 = (!Objects.equal(_init, null));
            if (!_notEquals_1) {
              _and_2 = false;
            } else {
              IQLVariableInitialization _init_1 = a.getInit();
              IQLArgumentsMap _argsMap_3 = _init_1.getArgsMap();
              boolean _notEquals_2 = (!Objects.equal(_argsMap_3, null));
              _and_2 = _notEquals_2;
            }
            if (!_and_2) {
              _and_1 = false;
            } else {
              IQLVariableInitialization _init_2 = a.getInit();
              IQLArgumentsMap _argsMap_4 = _init_2.getArgsMap();
              EList<IQLArgumentsMapKeyValue> _elements_1 = _argsMap_4.getElements();
              int _size_1 = _elements_1.size();
              boolean _greaterThan_1 = (_size_1 > 0);
              _and_1 = _greaterThan_1;
            }
            if (_and_1) {
              _builder.append("\t");
              JvmTypeReference _type_2 = a.getType();
              IQLVariableInitialization _init_3 = a.getInit();
              IQLArgumentsMap _argsMap_5 = _init_3.getArgsMap();
              CharSequence _createGetterMethod_1 = this.createGetterMethod(_type_2, _argsMap_5, context);
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
            boolean _notEquals_3 = (!Objects.equal(_init_4, null));
            if (!_notEquals_3) {
              _and_4 = false;
            } else {
              IQLVariableInitialization _init_5 = a_1.getInit();
              IQLArgumentsMap _argsMap_6 = _init_5.getArgsMap();
              boolean _notEquals_4 = (!Objects.equal(_argsMap_6, null));
              _and_4 = _notEquals_4;
            }
            if (!_and_4) {
              _and_3 = false;
            } else {
              IQLVariableInitialization _init_6 = a_1.getInit();
              IQLArgumentsMap _argsMap_7 = _init_6.getArgsMap();
              EList<IQLArgumentsMapKeyValue> _elements_2 = _argsMap_7.getElements();
              int _size_2 = _elements_2.size();
              boolean _greaterThan_2 = (_size_2 > 0);
              _and_3 = _greaterThan_2;
            }
            if (_and_3) {
              _builder.append("\t");
              JvmIdentifiableElement _var = a_1.getVar();
              IQLVariableDeclaration decl = ((IQLVariableDeclaration) _var);
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              JvmTypeReference type_2 = decl.getRef();
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              IQLVariableInitialization _init_7 = a_1.getInit();
              IQLArgumentsMap _argsMap_8 = _init_7.getArgsMap();
              CharSequence _createGetterMethod_2 = this.createGetterMethod(type_2, _argsMap_8, context);
              _builder.append(_createGetterMethod_2, "\t");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compileAORuleIntern(final ODLOperator o, final ODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      String _simpleName = o.getSimpleName();
      String name = (_simpleName + ODLCompilerHelper.AO_RULE_OPERATOR);
      String superClass = AbstractODLAORule.class.getSimpleName();
      String _simpleName_1 = o.getSimpleName();
      String aoName = (_simpleName_1 + ODLCompilerHelper.AO_OPERATOR);
      String _simpleName_2 = o.getSimpleName();
      String poName = (_simpleName_2 + ODLCompilerHelper.PO_OPERATOR);
      String transform = TransformationConfiguration.class.getSimpleName();
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("@SuppressWarnings(\"all\")");
      _builder.newLine();
      _builder.append("public class ");
      _builder.append(name, "");
      _builder.append(" extends ");
      _builder.append(superClass, "");
      _builder.append("<");
      _builder.append(aoName, "");
      _builder.append("> {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public void execute(");
      _builder.append(aoName, "\t");
      _builder.append(" operator, ");
      _builder.append(transform, "\t");
      _builder.append(" config) throws ");
      String _simpleName_3 = RuleException.class.getSimpleName();
      _builder.append(_simpleName_3, "\t");
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("defaultExecute(operator, new ");
      _builder.append(poName, "\t\t");
      _builder.append("(operator), config, true, true);");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compilePOIntern(final ODLOperator o, final ODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      String _simpleName = o.getSimpleName();
      String opName = (_simpleName + ODLCompilerHelper.PO_OPERATOR);
      String _simpleName_1 = o.getSimpleName();
      String aoName = (_simpleName_1 + ODLCompilerHelper.AO_OPERATOR);
      String superClass = AbstractODLPO.class.getSimpleName();
      Collection<ODLParameter> parameters = this.helper.getParameters(o);
      Collection<IQLAttribute> attributes = this.helper.getAttributes(o);
      String read = Tuple.class.getSimpleName();
      String write = Tuple.class.getSimpleName();
      String meta = IMetaAttribute.class.getSimpleName();
      Collection<IQLTerminalExpressionNew> newExpressions = this.helper.getNewExpressions(o);
      Collection<IQLVariableStatement> varStmts = this.helper.getVarStatements(o);
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      {
        EList<IQLJavaMetadata> _javametadata = o.getJavametadata();
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
      _builder.append(opName, "");
      _builder.append(" extends ");
      _builder.append(superClass, "");
      _builder.append("<");
      _builder.append(read, "");
      _builder.append("<");
      _builder.append(meta, "");
      _builder.append(">,");
      _builder.append(write, "");
      _builder.append("<");
      _builder.append(meta, "");
      _builder.append(">> {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.newLine();
      {
        EList<JvmMember> _members = o.getMembers();
        for(final JvmMember m : _members) {
          _builder.append("\t");
          String _compile = this.compile(m, context);
          _builder.append(_compile, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public ");
      _builder.append(opName, "\t");
      _builder.append("() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("super();");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public ");
      _builder.append(opName, "\t");
      _builder.append("(");
      _builder.append(opName, "\t");
      _builder.append(" po) {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("super(po);");
      _builder.newLine();
      {
        for(final ODLParameter p : parameters) {
          _builder.append("\t\t");
          String _createCloneStatements = this.createCloneStatements(p, "po", context);
          _builder.append(_createCloneStatements, "\t\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public ");
      _builder.append(opName, "\t");
      _builder.append("(");
      _builder.append(aoName, "\t");
      _builder.append(" ao) {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("super(ao);");
      _builder.newLine();
      {
        for(final ODLParameter p_1 : parameters) {
          _builder.append("\t\t");
          String _createCloneStatements_1 = this.createCloneStatements(p_1, "ao", context);
          _builder.append(_createCloneStatements_1, "\t\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      {
        for(final ODLParameter p_2 : parameters) {
          _builder.append("\t");
          String _simpleName_2 = p_2.getSimpleName();
          String pName = this.helper.firstCharUpperCase(_simpleName_2);
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          JvmTypeReference _type = p_2.getType();
          String type = this.typeCompiler.compile(_type, context, false);
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("public ");
          _builder.append(type, "\t");
          _builder.append(" get");
          _builder.append(pName, "\t");
          _builder.append("() {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("return this.");
          String _simpleName_3 = p_2.getSimpleName();
          _builder.append(_simpleName_3, "\t\t");
          _builder.append(";");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("}");
          _builder.newLine();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      {
        for(final IQLTerminalExpressionNew e : newExpressions) {
          {
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
            boolean _notEquals_1 = (!Objects.equal(_init, null));
            if (!_notEquals_1) {
              _and_2 = false;
            } else {
              IQLVariableInitialization _init_1 = a.getInit();
              IQLArgumentsMap _argsMap_3 = _init_1.getArgsMap();
              boolean _notEquals_2 = (!Objects.equal(_argsMap_3, null));
              _and_2 = _notEquals_2;
            }
            if (!_and_2) {
              _and_1 = false;
            } else {
              IQLVariableInitialization _init_2 = a.getInit();
              IQLArgumentsMap _argsMap_4 = _init_2.getArgsMap();
              EList<IQLArgumentsMapKeyValue> _elements_1 = _argsMap_4.getElements();
              int _size_1 = _elements_1.size();
              boolean _greaterThan_1 = (_size_1 > 0);
              _and_1 = _greaterThan_1;
            }
            if (_and_1) {
              _builder.append("\t");
              JvmTypeReference _type_1 = a.getType();
              IQLVariableInitialization _init_3 = a.getInit();
              IQLArgumentsMap _argsMap_5 = _init_3.getArgsMap();
              CharSequence _createGetterMethod_1 = this.createGetterMethod(_type_1, _argsMap_5, context);
              _builder.append(_createGetterMethod_1, "\t");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.newLine();
      {
        for(final IQLVariableStatement a_1 : varStmts) {
          {
            boolean _and_3 = false;
            boolean _and_4 = false;
            IQLVariableInitialization _init_4 = a_1.getInit();
            boolean _notEquals_3 = (!Objects.equal(_init_4, null));
            if (!_notEquals_3) {
              _and_4 = false;
            } else {
              IQLVariableInitialization _init_5 = a_1.getInit();
              IQLArgumentsMap _argsMap_6 = _init_5.getArgsMap();
              boolean _notEquals_4 = (!Objects.equal(_argsMap_6, null));
              _and_4 = _notEquals_4;
            }
            if (!_and_4) {
              _and_3 = false;
            } else {
              IQLVariableInitialization _init_6 = a_1.getInit();
              IQLArgumentsMap _argsMap_7 = _init_6.getArgsMap();
              EList<IQLArgumentsMapKeyValue> _elements_2 = _argsMap_7.getElements();
              int _size_2 = _elements_2.size();
              boolean _greaterThan_2 = (_size_2 > 0);
              _and_3 = _greaterThan_2;
            }
            if (_and_3) {
              _builder.append("\t");
              JvmIdentifiableElement _var = a_1.getVar();
              IQLVariableDeclaration decl = ((IQLVariableDeclaration) _var);
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              JvmTypeReference type_1 = decl.getRef();
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              IQLVariableInitialization _init_7 = a_1.getInit();
              IQLArgumentsMap _argsMap_8 = _init_7.getArgsMap();
              CharSequence _createGetterMethod_2 = this.createGetterMethod(type_1, _argsMap_8, context);
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
  
  public String compile(final JvmMember m, final ODLGeneratorContext context) {
    String _xifexpression = null;
    boolean _and = false;
    boolean _isAo = context.isAo();
    if (!_isAo) {
      _and = false;
    } else {
      _and = (m instanceof ODLParameter);
    }
    if (_and) {
      _xifexpression = this.compile(((ODLParameter) m), context);
    } else {
      String _xifexpression_1 = null;
      if ((m instanceof ODLMethod)) {
        _xifexpression_1 = this.compile(((ODLMethod) m), context);
      } else {
        _xifexpression_1 = super.compile(m, context);
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String compile(final ODLParameter a, final ODLGeneratorContext context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private ");
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
  
  public String compile(final ODLMethod m, final ODLGeneratorContext context) {
    String _xifexpression = null;
    boolean _and = false;
    boolean _isValidate = m.isValidate();
    if (!_isValidate) {
      _and = false;
    } else {
      boolean _isAo = context.isAo();
      _and = _isAo;
    }
    if (_and) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("private void validate");
      String _simpleName = m.getSimpleName();
      _builder.append(_simpleName, "");
      _builder.append("()");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      IQLStatement _body = m.getBody();
      String _compile = this.stmtCompiler.compile(_body, context);
      _builder.append(_compile, "\t");
      _builder.append("\t");
      _builder.newLineIfNotEmpty();
      _xifexpression = _builder.toString();
    } else {
      String _xifexpression_1 = null;
      boolean _and_1 = false;
      boolean _isAo_1 = context.isAo();
      boolean _not = (!_isAo_1);
      if (!_not) {
        _and_1 = false;
      } else {
        boolean _isOn = m.isOn();
        _and_1 = _isOn;
      }
      if (_and_1) {
        String _xblockexpression = null;
        {
          String className = this.helper.getClassName(m);
          String returnT = "";
          boolean _and_2 = false;
          JvmTypeReference _returnType = m.getReturnType();
          boolean _equals = Objects.equal(_returnType, null);
          if (!_equals) {
            _and_2 = false;
          } else {
            String _simpleName_1 = m.getSimpleName();
            boolean _equalsIgnoreCase = _simpleName_1.equalsIgnoreCase(className);
            boolean _not_1 = (!_equalsIgnoreCase);
            _and_2 = _not_1;
          }
          if (_and_2) {
            JvmTypeReference _returnType_1 = m.getReturnType();
            String _compile_1 = this.typeCompiler.compile(_returnType_1, context, false);
            returnT = _compile_1;
          }
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("@Override");
          _builder_1.newLine();
          _builder_1.append("public ");
          _builder_1.append(returnT, "");
          _builder_1.append(" on");
          String _simpleName_2 = m.getSimpleName();
          String _firstCharUpperCase = this.helper.firstCharUpperCase(_simpleName_2);
          _builder_1.append(_firstCharUpperCase, "");
          _builder_1.append("(");
          {
            EList<JvmFormalParameter> _parameters = m.getParameters();
            boolean _notEquals = (!Objects.equal(_parameters, null));
            if (_notEquals) {
              EList<JvmFormalParameter> _parameters_1 = m.getParameters();
              final Function1<JvmFormalParameter, String> _function = new Function1<JvmFormalParameter, String>() {
                public String apply(final JvmFormalParameter p) {
                  return ODLCompiler.this.compile(p, context);
                }
              };
              List<String> _map = ListExtensions.<JvmFormalParameter, String>map(_parameters_1, _function);
              String _join = IterableExtensions.join(_map, ", ");
              _builder_1.append(_join, "");
            }
          }
          _builder_1.append(")");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("\t");
          IQLStatement _body_1 = m.getBody();
          String _compile_2 = this.stmtCompiler.compile(_body_1, context);
          _builder_1.append(_compile_2, "\t");
          _builder_1.append("\t");
          _builder_1.newLineIfNotEmpty();
          _xblockexpression = _builder_1.toString();
        }
        _xifexpression_1 = _xblockexpression;
      } else {
        String _xifexpression_2 = null;
        boolean _isAo_2 = context.isAo();
        boolean _not_1 = (!_isAo_2);
        if (_not_1) {
          _xifexpression_2 = super.compile(m, context);
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String createCloneStatements(final ODLParameter p, final String varName, final ODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      String name = p.getSimpleName();
      String pName = this.helper.firstCharUpperCase(name);
      JvmTypeReference type = p.getType();
      String _xifexpression = null;
      boolean _isList = this.factory.isList(type);
      if (_isList) {
        String _xblockexpression_1 = null;
        {
          JvmTypeReference listElement = this.factory.getListElementType(p);
          String _canonicalName = IQLUtils.class.getCanonicalName();
          context.addImport(_canonicalName);
          String _xifexpression_1 = null;
          boolean _isClonable = this.factory.isClonable(listElement);
          if (_isClonable) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("this.");
            _builder.append(name, "");
            _builder.append(" = ");
            String _simpleName = IQLUtils.class.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append(".createEmptyList();");
            _builder.newLineIfNotEmpty();
            _builder.append("for (");
            String _compile = this.typeCompiler.compile(listElement, context, false);
            _builder.append(_compile, "");
            _builder.append(" e : ");
            _builder.append(varName, "");
            _builder.append(".get");
            _builder.append(pName, "");
            _builder.append("()) {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("this.");
            _builder.append(name, "\t");
            _builder.append(".add(e.clone()); ");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _xifexpression_1 = _builder.toString();
          } else {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append("this.");
            _builder_1.append(name, "");
            _builder_1.append(" = ");
            String _simpleName_1 = IQLUtils.class.getSimpleName();
            _builder_1.append(_simpleName_1, "");
            _builder_1.append(".createList(");
            _builder_1.append(varName, "");
            _builder_1.append(".get");
            _builder_1.append(pName, "");
            _builder_1.append("());");
            _xifexpression_1 = _builder_1.toString();
          }
          _xblockexpression_1 = _xifexpression_1;
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        boolean _isMap = this.factory.isMap(type);
        if (_isMap) {
          String _xblockexpression_2 = null;
          {
            JvmTypeReference key = this.factory.getMapKeyType(p);
            JvmTypeReference value = this.factory.getMapValueType(p);
            String _canonicalName = IQLUtils.class.getCanonicalName();
            context.addImport(_canonicalName);
            String _xifexpression_2 = null;
            boolean _or = false;
            boolean _isClonable = this.factory.isClonable(key);
            if (_isClonable) {
              _or = true;
            } else {
              boolean _isClonable_1 = this.factory.isClonable(value);
              _or = _isClonable_1;
            }
            if (_or) {
              String _xblockexpression_3 = null;
              {
                String _canonicalName_1 = Map.Entry.class.getCanonicalName();
                context.addImport(_canonicalName_1);
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("this.");
                _builder.append(name, "");
                _builder.append(" = ");
                String _simpleName = IQLUtils.class.getSimpleName();
                _builder.append(_simpleName, "");
                _builder.append(".createEmptyMap();");
                _builder.newLineIfNotEmpty();
                _builder.append("for (");
                String _canonicalName_2 = Map.Entry.class.getCanonicalName();
                _builder.append(_canonicalName_2, "");
                _builder.append("<");
                String _compile = this.typeCompiler.compile(key, context, true);
                _builder.append(_compile, "");
                _builder.append(",");
                String _compile_1 = this.typeCompiler.compile(value, context, true);
                _builder.append(_compile_1, "");
                _builder.append("> e : ");
                _builder.append(varName, "");
                _builder.append(".get");
                _builder.append(pName, "");
                _builder.append("().entrySet()) {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("this.");
                _builder.append(name, "\t");
                _builder.append(".put(e.getKey()");
                {
                  boolean _isClonable_2 = this.factory.isClonable(key);
                  if (_isClonable_2) {
                    _builder.append(".clone()");
                  }
                }
                _builder.append(",e.getValue()");
                {
                  boolean _isClonable_3 = this.factory.isClonable(value);
                  if (_isClonable_3) {
                    _builder.append(".clone()");
                  }
                }
                _builder.append("); ");
                _builder.newLineIfNotEmpty();
                _builder.append("}");
                _builder.newLine();
                _xblockexpression_3 = _builder.toString();
              }
              _xifexpression_2 = _xblockexpression_3;
            } else {
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("this.");
              _builder.append(name, "");
              _builder.append(" = ");
              String _simpleName = IQLUtils.class.getSimpleName();
              _builder.append(_simpleName, "");
              _builder.append(".createMap(");
              _builder.append(varName, "");
              _builder.append(".get");
              _builder.append(pName, "");
              _builder.append("());");
              _xifexpression_2 = _builder.toString();
            }
            _xblockexpression_2 = _xifexpression_2;
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          String _xifexpression_2 = null;
          boolean _isClonable = this.factory.isClonable(type);
          if (_isClonable) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("this.");
            _builder.append(name, "");
            _builder.append(" = ");
            _builder.append(varName, "");
            _builder.append(".get");
            _builder.append(pName, "");
            _builder.append("().clone();");
            _xifexpression_2 = _builder.toString();
          } else {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append("this.");
            _builder_1.append(name, "");
            _builder_1.append(" = ");
            _builder_1.append(varName, "");
            _builder_1.append(".get");
            _builder_1.append(pName, "");
            _builder_1.append("();");
            _xifexpression_2 = _builder_1.toString();
          }
          _xifexpression_1 = _xifexpression_2;
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
}
