package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicates;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition;
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
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;
import de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator.AbstractODLAO;
import de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator.AbstractODLAORule;
import de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator.AbstractODLPO;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class ODLCompiler extends AbstractIQLCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLTypeCompiler, ODLStatementCompiler, ODLTypeFactory, ODLTypeUtils> {
  @Inject
  private ODLMetadataAnnotationCompiler metadataAnnotationCompiler;
  
  @Inject
  private ODLLookUp lookUp;
  
  @Inject
  public ODLCompiler(final ODLCompilerHelper helper, final ODLTypeCompiler typeCompiler, final ODLStatementCompiler stmtCompiler, final ODLTypeFactory factory, final ODLTypeUtils typeUtils) {
    super(helper, typeCompiler, stmtCompiler, factory, typeUtils);
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
  
  public String compilePO(final IQLTypeDefinition typeDef, final ODLOperator o, final ODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      StringBuilder builder = new StringBuilder();
      String _compilePOIntern = this.compilePOIntern(typeDef, o, context);
      builder.append(_compilePOIntern);
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
      Collection<IQLNewExpression> newExpressions = this.helper.getNewExpressions(o);
      Collection<IQLVariableStatement> varStmts = this.helper.getVarStatements(o);
      Collection<ODLMethod> odlmethods = this.helper.getODLMethods(o);
      boolean hasPredicate = this.helper.hasPredicate(o);
      Collection<String> predicates = this.helper.getPredicates(o);
      Collection<String> predicateArrays = this.helper.getPredicateArrays(o);
      ArrayList<ODLParameter> parametersToValidate = new ArrayList<ODLParameter>();
      for (final ODLParameter p : parameters) {
        boolean _hasValidateMethod = this.helper.hasValidateMethod(o, p);
        if (_hasValidateMethod) {
          parametersToValidate.add(p);
        }
      }
      if (hasPredicate) {
        String _canonicalName = IHasPredicates.class.getCanonicalName();
        context.addImport(_canonicalName);
        String _canonicalName_1 = List.class.getCanonicalName();
        context.addImport(_canonicalName_1);
        String _canonicalName_2 = ArrayList.class.getCanonicalName();
        context.addImport(_canonicalName_2);
      }
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
      {
        if (hasPredicate) {
          _builder.append(" implements ");
          String _simpleName_2 = IHasPredicates.class.getSimpleName();
          _builder.append(_simpleName_2, "");
        }
      }
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      {
        for(final ODLParameter p_1 : parameters) {
          _builder.append("\t");
          String _compile = this.compile(p_1, context);
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
        for(final ODLParameter p_2 : parameters) {
          _builder.append("\t\t");
          String _createCloneStatements = this.createCloneStatements(p_2, "ao", context);
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
        for(final ODLParameter p_3 : parameters) {
          _builder.append("\t");
          String _simpleName_3 = p_3.getSimpleName();
          String pName = this.helper.firstCharUpperCase(_simpleName_3);
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          JvmTypeReference _type = p_3.getType();
          String type = this.typeCompiler.compile(_type, context, false);
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("@");
          String _simpleName_4 = Parameter.class.getSimpleName();
          _builder.append(_simpleName_4, "\t");
          _builder.append("(");
          String _parameterAnnotationElements = this.metadataAnnotationCompiler.getParameterAnnotationElements(p_3, context);
          _builder.append(_parameterAnnotationElements, "\t");
          _builder.append(")");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("public void set");
          _builder.append(pName, "\t");
          _builder.append("(");
          _builder.append(type, "\t");
          _builder.append(" ");
          String _simpleName_5 = p_3.getSimpleName();
          _builder.append(_simpleName_5, "\t");
          _builder.append(") {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("this.");
          String _simpleName_6 = p_3.getSimpleName();
          _builder.append(_simpleName_6, "\t\t");
          _builder.append(" = ");
          String _simpleName_7 = p_3.getSimpleName();
          _builder.append(_simpleName_7, "\t\t");
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
        for(final ODLParameter p_4 : parameters) {
          _builder.append("\t");
          String _simpleName_8 = p_4.getSimpleName();
          String pName_1 = this.helper.firstCharUpperCase(_simpleName_8);
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          JvmTypeReference _type_1 = p_4.getType();
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
          String _simpleName_9 = p_4.getSimpleName();
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
      {
        int _size = parametersToValidate.size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          _builder.append("\t");
          _builder.append("@Override");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("protected boolean validateParameters() {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("return ");
          final Function1<ODLParameter, String> _function = new Function1<ODLParameter, String>() {
            public String apply(final ODLParameter p) {
              String _simpleName = p.getSimpleName();
              String _plus = ("this.validate" + _simpleName);
              return (_plus + "()");
            }
          };
          List<String> _map = ListExtensions.<ODLParameter, String>map(parametersToValidate, _function);
          String _join = IterableExtensions.join(_map, "&&");
          _builder.append(_join, "\t\t");
          _builder.append(";");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("}");
          _builder.newLine();
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
        if (hasPredicate) {
          _builder.append("\t");
          _builder.append("@Override");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("public ");
          String _simpleName_10 = List.class.getSimpleName();
          _builder.append(_simpleName_10, "\t");
          _builder.append("<IPredicate<?>> getPredicates() {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          String _simpleName_11 = List.class.getSimpleName();
          _builder.append(_simpleName_11, "\t\t");
          _builder.append(" result = new ");
          String _simpleName_12 = ArrayList.class.getSimpleName();
          _builder.append(_simpleName_12, "\t\t");
          _builder.append("<>();");
          _builder.newLineIfNotEmpty();
          {
            for(final String pred : predicates) {
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("result.add(this.");
              _builder.append(pred, "\t\t");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
            }
          }
          {
            for(final String pred_1 : predicateArrays) {
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("for (");
              String _simpleName_13 = IPredicate.class.getSimpleName();
              _builder.append(_simpleName_13, "\t\t");
              _builder.append(" p : ");
              _builder.append(pred_1, "\t\t");
              _builder.append(") {");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("result.add(p);\t");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("}");
              _builder.newLine();
            }
          }
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("return result;");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("}");
          _builder.newLine();
        }
      }
      {
        for(final IQLNewExpression e : newExpressions) {
          {
            boolean _and = false;
            IQLArgumentsMap _argsMap = e.getArgsMap();
            boolean _notEquals = (!Objects.equal(_argsMap, null));
            if (!_notEquals) {
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
              int _size_2 = _elements_1.size();
              boolean _greaterThan_2 = (_size_2 > 0);
              _and_1 = _greaterThan_2;
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
  
  public String compilePOIntern(final IQLTypeDefinition typeDef, final ODLOperator o, final ODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      String _simpleName = o.getSimpleName();
      String opName = (_simpleName + ODLCompilerHelper.PO_OPERATOR);
      String _simpleName_1 = o.getSimpleName();
      String aoName = (_simpleName_1 + ODLCompilerHelper.AO_OPERATOR);
      Collection<ODLParameter> parameters = this.helper.getParameters(o);
      Collection<IQLAttribute> attributes = this.helper.getAttributes(o);
      Class<AbstractODLPO> superClass = AbstractODLPO.class;
      Class<?> read = this.helper.determineReadType(o);
      Class<?> write = read;
      Class<IMetaAttribute> meta = IMetaAttribute.class;
      Collection<IQLNewExpression> newExpressions = this.helper.getNewExpressions(o);
      Collection<IQLVariableStatement> varStmts = this.helper.getVarStatements(o);
      AbstractPipe.OutputMode outputmode = this.helper.determineOutputMode(o);
      String _canonicalName = superClass.getCanonicalName();
      context.addImport(_canonicalName);
      String _canonicalName_1 = read.getCanonicalName();
      context.addImport(_canonicalName_1);
      String _canonicalName_2 = write.getCanonicalName();
      context.addImport(_canonicalName_2);
      String _canonicalName_3 = meta.getCanonicalName();
      context.addImport(_canonicalName_3);
      Class<? extends AbstractPipe.OutputMode> _class = outputmode.getClass();
      String _canonicalName_4 = _class.getCanonicalName();
      context.addImport(_canonicalName_4);
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
      _builder.append("public class ");
      _builder.append(opName, "");
      _builder.append(" extends ");
      String _simpleName_2 = superClass.getSimpleName();
      _builder.append(_simpleName_2, "");
      _builder.append("<");
      String _simpleName_3 = read.getSimpleName();
      _builder.append(_simpleName_3, "");
      _builder.append("<");
      String _simpleName_4 = meta.getSimpleName();
      _builder.append(_simpleName_4, "");
      _builder.append(">,");
      String _simpleName_5 = write.getSimpleName();
      _builder.append(_simpleName_5, "");
      _builder.append("<");
      String _simpleName_6 = meta.getSimpleName();
      _builder.append(_simpleName_6, "");
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
      _builder.append("\t");
      _builder.append("@");
      String _simpleName_7 = Override.class.getSimpleName();
      _builder.append(_simpleName_7, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("public ");
      String _simpleName_8 = AbstractPipe.OutputMode.class.getSimpleName();
      _builder.append(_simpleName_8, "\t");
      _builder.append(" getOutputMode() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("return ");
      String _simpleName_9 = AbstractPipe.OutputMode.class.getSimpleName();
      _builder.append(_simpleName_9, "\t\t");
      _builder.append(".");
      String _string = outputmode.toString();
      _builder.append(_string, "\t\t");
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      {
        for(final ODLParameter p_2 : parameters) {
          _builder.append("\t");
          String _simpleName_10 = p_2.getSimpleName();
          String pName = this.helper.firstCharUpperCase(_simpleName_10);
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
          String _simpleName_11 = p_2.getSimpleName();
          _builder.append(_simpleName_11, "\t\t");
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
        for(final IQLNewExpression e : newExpressions) {
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
    boolean _and_1 = false;
    boolean _isValidate = m.isValidate();
    if (!_isValidate) {
      _and_1 = false;
    } else {
      String _simpleName = m.getSimpleName();
      boolean _notEquals = (!Objects.equal(_simpleName, null));
      _and_1 = _notEquals;
    }
    if (!_and_1) {
      _and = false;
    } else {
      boolean _isAo = context.isAo();
      _and = _isAo;
    }
    if (_and) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("private boolean validate");
      String _simpleName_1 = m.getSimpleName();
      _builder.append(_simpleName_1, "");
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
      boolean _and_2 = false;
      boolean _and_3 = false;
      boolean _isValidate_1 = m.isValidate();
      if (!_isValidate_1) {
        _and_3 = false;
      } else {
        String _simpleName_2 = m.getSimpleName();
        boolean _equals = Objects.equal(_simpleName_2, null);
        _and_3 = _equals;
      }
      if (!_and_3) {
        _and_2 = false;
      } else {
        boolean _isAo_1 = context.isAo();
        _and_2 = _isAo_1;
      }
      if (_and_2) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("@Override");
        _builder_1.newLine();
        _builder_1.append("protected boolean validate()");
        _builder_1.newLine();
        _builder_1.append("\t");
        IQLStatement _body_1 = m.getBody();
        String _compile_1 = this.stmtCompiler.compile(_body_1, context);
        _builder_1.append(_compile_1, "\t");
        _builder_1.append("\t");
        _builder_1.newLineIfNotEmpty();
        _xifexpression_1 = _builder_1.toString();
      } else {
        String _xifexpression_2 = null;
        boolean _and_4 = false;
        boolean _and_5 = false;
        boolean _isValidate_2 = m.isValidate();
        boolean _not = (!_isValidate_2);
        if (!_not) {
          _and_5 = false;
        } else {
          boolean _isOn = m.isOn();
          _and_5 = _isOn;
        }
        if (!_and_5) {
          _and_4 = false;
        } else {
          boolean _or = false;
          boolean _and_6 = false;
          boolean _isAo_2 = context.isAo();
          boolean _not_1 = (!_isAo_2);
          if (!_not_1) {
            _and_6 = false;
          } else {
            String _simpleName_3 = m.getSimpleName();
            boolean _hasOnMethod = this.lookUp.hasOnMethod(_simpleName_3, false);
            _and_6 = _hasOnMethod;
          }
          if (_and_6) {
            _or = true;
          } else {
            boolean _and_7 = false;
            boolean _isAo_3 = context.isAo();
            if (!_isAo_3) {
              _and_7 = false;
            } else {
              String _simpleName_4 = m.getSimpleName();
              boolean _hasOnMethod_1 = this.lookUp.hasOnMethod(_simpleName_4, true);
              _and_7 = _hasOnMethod_1;
            }
            _or = _and_7;
          }
          _and_4 = _or;
        }
        if (_and_4) {
          String _xblockexpression = null;
          {
            String className = this.helper.getClassName(m);
            String returnT = "";
            boolean _and_8 = false;
            JvmTypeReference _returnType = m.getReturnType();
            boolean _notEquals_1 = (!Objects.equal(_returnType, null));
            if (!_notEquals_1) {
              _and_8 = false;
            } else {
              String _simpleName_5 = m.getSimpleName();
              boolean _equalsIgnoreCase = _simpleName_5.equalsIgnoreCase(className);
              boolean _not_2 = (!_equalsIgnoreCase);
              _and_8 = _not_2;
            }
            if (_and_8) {
              JvmTypeReference _returnType_1 = m.getReturnType();
              String _compile_2 = this.typeCompiler.compile(_returnType_1, context, false);
              returnT = _compile_2;
            } else {
              boolean _and_9 = false;
              JvmTypeReference _returnType_2 = m.getReturnType();
              boolean _equals_1 = Objects.equal(_returnType_2, null);
              if (!_equals_1) {
                _and_9 = false;
              } else {
                String _simpleName_6 = m.getSimpleName();
                boolean _equalsIgnoreCase_1 = _simpleName_6.equalsIgnoreCase(className);
                boolean _not_3 = (!_equalsIgnoreCase_1);
                _and_9 = _not_3;
              }
              if (_and_9) {
                returnT = "void";
              }
            }
            StringConcatenation _builder_2 = new StringConcatenation();
            _builder_2.append("@Override");
            _builder_2.newLine();
            _builder_2.append("public ");
            _builder_2.append(returnT, "");
            _builder_2.append(" on");
            String _simpleName_7 = m.getSimpleName();
            String _firstCharUpperCase = this.helper.firstCharUpperCase(_simpleName_7);
            _builder_2.append(_firstCharUpperCase, "");
            _builder_2.append("(");
            {
              EList<JvmFormalParameter> _parameters = m.getParameters();
              boolean _notEquals_2 = (!Objects.equal(_parameters, null));
              if (_notEquals_2) {
                EList<JvmFormalParameter> _parameters_1 = m.getParameters();
                final Function1<JvmFormalParameter, String> _function = new Function1<JvmFormalParameter, String>() {
                  public String apply(final JvmFormalParameter p) {
                    return ODLCompiler.this.compile(p, context);
                  }
                };
                List<String> _map = ListExtensions.<JvmFormalParameter, String>map(_parameters_1, _function);
                String _join = IterableExtensions.join(_map, ", ");
                _builder_2.append(_join, "");
              }
            }
            _builder_2.append(")");
            _builder_2.newLineIfNotEmpty();
            _builder_2.append("\t");
            IQLStatement _body_2 = m.getBody();
            String _compile_3 = this.stmtCompiler.compile(_body_2, context);
            _builder_2.append(_compile_3, "\t");
            _builder_2.append("\t");
            _builder_2.newLineIfNotEmpty();
            _xblockexpression = _builder_2.toString();
          }
          _xifexpression_2 = _xblockexpression;
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
      boolean _isList = this.typeUtils.isList(type);
      if (_isList) {
        String _xblockexpression_1 = null;
        {
          String _canonicalName = IQLUtils.class.getCanonicalName();
          context.addImport(_canonicalName);
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("this.");
          _builder.append(name, "");
          _builder.append(" = ");
          String _simpleName = IQLUtils.class.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append(".createList(");
          _builder.append(varName, "");
          _builder.append(".get");
          _builder.append(pName, "");
          _builder.append("());");
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        String _xifexpression_1 = null;
        boolean _isMap = this.typeUtils.isMap(type);
        if (_isMap) {
          String _xblockexpression_2 = null;
          {
            String _canonicalName = IQLUtils.class.getCanonicalName();
            context.addImport(_canonicalName);
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
            _xblockexpression_2 = _builder.toString();
          }
          _xifexpression_1 = _xblockexpression_2;
        } else {
          String _xifexpression_2 = null;
          boolean _isClonable = this.typeUtils.isClonable(type);
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
