package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicates;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.types.IQLUtils;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.IODLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;
import de.uniol.inf.is.odysseus.iql.odl.types.useroperator.IODLAO;
import de.uniol.inf.is.odysseus.iql.odl.types.useroperator.IODLAORule;
import de.uniol.inf.is.odysseus.iql.odl.types.useroperator.IODLPO;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.EventMethodsFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.IEventMethod;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class ODLCompiler extends AbstractIQLCompiler<IODLCompilerHelper, IODLGeneratorContext, IODLTypeCompiler, IODLStatementCompiler, IODLTypeDictionary, IODLTypeUtils> implements IODLCompiler {
  @Inject
  private IODLMetadataAnnotationCompiler metadataAnnotationCompiler;
  
  @Inject
  private IODLLookUp lookUp;
  
  @Inject
  public ODLCompiler(final IODLCompilerHelper helper, final IODLTypeCompiler typeCompiler, final IODLStatementCompiler stmtCompiler, final IODLTypeDictionary typeDictionary, final IODLTypeUtils typeUtils) {
    super(helper, typeCompiler, stmtCompiler, typeDictionary, typeUtils);
  }
  
  @Override
  public String compileAO(final ODLOperator o, final IODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      context.setAo(true);
      StringBuilder builder = new StringBuilder();
      builder.append(this.compileAOIntern(o, context));
      context.addImport(AbstractLogicalOperator.class.getCanonicalName());
      context.addImport(LogicalOperatorCategory.class.getCanonicalName());
      context.addImport(LogicalOperator.class.getCanonicalName());
      context.addImport(Parameter.class.getCanonicalName());
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
  
  @Override
  public String compilePO(final ODLOperator o, final IODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      StringBuilder builder = new StringBuilder();
      EObject _eContainer = o.eContainer();
      IQLModelElement element = ((IQLModelElement) _eContainer);
      builder.append(this.compilePOIntern(element, o, context));
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
  
  @Override
  public String compileAORule(final ODLOperator o, final IODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      StringBuilder builder = new StringBuilder();
      builder.append(this.compileAORuleIntern(o, context));
      context.addImport(AbstractTransformationRule.class.getCanonicalName());
      context.addImport(IRuleFlowGroup.class.getCanonicalName());
      context.addImport(TransformRuleFlowGroup.class.getCanonicalName());
      context.addImport(TransformationConfiguration.class.getCanonicalName());
      context.addImport(IODLAORule.class.getCanonicalName());
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
  
  public String compileAOIntern(final ODLOperator o, final IODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      String _simpleName = o.getSimpleName();
      String opName = (_simpleName + IODLCompilerHelper.AO_OPERATOR);
      String superClass = AbstractLogicalOperator.class.getSimpleName();
      Collection<JvmMember> members = this.helper.getAOMembers(o);
      Collection<IQLAttribute> attributes = this.helper.getAOAttributes(o);
      Collection<ODLParameter> parameters = this.helper.getParameters(o);
      boolean operatorValidate = this.helper.hasOperatorValidate(o);
      ArrayList<ODLParameter> parametersToValidate = new ArrayList<ODLParameter>();
      for (final ODLParameter p : parameters) {
        boolean _hasValidateMethod = this.helper.hasValidateMethod(o, p);
        if (_hasValidateMethod) {
          parametersToValidate.add(p);
        }
      }
      boolean hasPredicate = this.helper.hasPredicate(o);
      Collection<IQLNewExpression> newExpressions = this.helper.getNewExpressions(o);
      Collection<IQLVariableStatement> varStmts = this.helper.getVarStatements(o);
      Collection<String> predicates = this.helper.getPredicates(o);
      Collection<String> predicateArrays = this.helper.getPredicateArrays(o);
      context.addImport(List.class.getCanonicalName());
      context.addImport(ArrayList.class.getCanonicalName());
      context.addImport(Map.class.getCanonicalName());
      context.addImport(HashMap.class.getCanonicalName());
      context.addImport(IODLAO.class.getCanonicalName());
      if (hasPredicate) {
        context.addImport(IHasPredicates.class.getCanonicalName());
      }
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("@SuppressWarnings(\"all\")");
      _builder.newLine();
      _builder.append("@");
      String _simpleName_1 = LogicalOperator.class.getSimpleName();
      _builder.append(_simpleName_1);
      _builder.append("(");
      String _aOAnnotationElements = this.metadataAnnotationCompiler.getAOAnnotationElements(o, context);
      _builder.append(_aOAnnotationElements);
      _builder.append(")");
      _builder.newLineIfNotEmpty();
      _builder.append("public class ");
      _builder.append(opName);
      _builder.append(" extends ");
      _builder.append(superClass);
      _builder.append(" implements ");
      String _simpleName_2 = IODLAO.class.getSimpleName();
      _builder.append(_simpleName_2);
      {
        if (hasPredicate) {
          _builder.append(", ");
          String _simpleName_3 = IHasPredicates.class.getSimpleName();
          _builder.append(_simpleName_3);
        }
      }
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t\t\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("private ");
      String _simpleName_4 = Map.class.getSimpleName();
      _builder.append(_simpleName_4, "\t");
      _builder.append("<");
      String _simpleName_5 = String.class.getSimpleName();
      _builder.append(_simpleName_5, "\t");
      _builder.append(", ");
      String _simpleName_6 = List.class.getSimpleName();
      _builder.append(_simpleName_6, "\t");
      _builder.append("<");
      String _simpleName_7 = Object.class.getSimpleName();
      _builder.append(_simpleName_7, "\t");
      _builder.append(">> metadata = new ");
      String _simpleName_8 = HashMap.class.getSimpleName();
      _builder.append(_simpleName_8, "\t");
      _builder.append("<>();");
      _builder.newLineIfNotEmpty();
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
        for(final IQLAttribute attr : attributes) {
          _builder.append("\t\t");
          String _createCloneStatements = this.createCloneStatements(attr, "ao", context);
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
        for(final JvmMember member : members) {
          _builder.append("\t");
          String _compile = this.compile(((JvmMember) member), context);
          _builder.append(_compile, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      {
        for(final ODLParameter p_1 : parameters) {
          _builder.append("\t");
          String pName = this.helper.firstCharUpperCase(p_1.getSimpleName());
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          String type = this.typeCompiler.compile(p_1.getType(), context, false);
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("@");
          String _simpleName_9 = Parameter.class.getSimpleName();
          _builder.append(_simpleName_9, "\t");
          _builder.append("(");
          String _parameterAnnotationElements = this.metadataAnnotationCompiler.getParameterAnnotationElements(p_1, context);
          _builder.append(_parameterAnnotationElements, "\t");
          _builder.append(")");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("public void set");
          _builder.append(pName, "\t");
          _builder.append("(");
          _builder.append(type, "\t");
          _builder.append(" ");
          String _simpleName_10 = p_1.getSimpleName();
          _builder.append(_simpleName_10, "\t");
          _builder.append(") {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("this.");
          String _simpleName_11 = p_1.getSimpleName();
          _builder.append(_simpleName_11, "\t\t");
          _builder.append(" = ");
          String _simpleName_12 = p_1.getSimpleName();
          _builder.append(_simpleName_12, "\t\t");
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
        for(final IQLAttribute attr_1 : attributes) {
          _builder.append("\t");
          String attrName = this.helper.firstCharUpperCase(attr_1.getSimpleName());
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          String type_1 = this.typeCompiler.compile(attr_1.getType(), context, false);
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("public ");
          _builder.append(type_1, "\t");
          _builder.append(" get");
          _builder.append(attrName, "\t");
          _builder.append("() {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("return this.");
          String _simpleName_13 = attr_1.getSimpleName();
          _builder.append(_simpleName_13, "\t\t");
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
        if ((operatorValidate || (parametersToValidate.size() > 0))) {
          _builder.append("\t");
          _builder.append("@Override");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("public boolean isValid() {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("return super.isValid()");
          {
            if (operatorValidate) {
              _builder.append(" && validate()");
            }
          }
          {
            int _size = parametersToValidate.size();
            boolean _greaterThan = (_size > 0);
            if (_greaterThan) {
              _builder.append(" && validateParameters()");
            }
          }
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
        int _size_1 = parametersToValidate.size();
        boolean _greaterThan_1 = (_size_1 > 0);
        if (_greaterThan_1) {
          _builder.append("\t");
          _builder.append("protected boolean validateParameters() {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("return ");
          final Function1<ODLParameter, String> _function = (ODLParameter p_2) -> {
            String _simpleName_14 = p_2.getSimpleName();
            String _plus = ("this.validate" + _simpleName_14);
            return (_plus + "()");
          };
          String _join = IterableExtensions.join(ListExtensions.<ODLParameter, String>map(parametersToValidate, _function), "&&");
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
          String _simpleName_14 = List.class.getSimpleName();
          _builder.append(_simpleName_14, "\t");
          _builder.append("<IPredicate<?>> getPredicates() {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          String _simpleName_15 = List.class.getSimpleName();
          _builder.append(_simpleName_15, "\t\t");
          _builder.append(" result = new ");
          String _simpleName_16 = ArrayList.class.getSimpleName();
          _builder.append(_simpleName_16, "\t\t");
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
              String _simpleName_17 = IPredicate.class.getSimpleName();
              _builder.append(_simpleName_17, "\t\t");
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
              JvmTypeReference type_2 = decl.getRef();
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              CharSequence _createGetterMethod_2 = this.createGetterMethod(type_2, a_1.getInit().getArgsMap(), context);
              _builder.append(_createGetterMethod_2, "\t");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public ");
      String _simpleName_18 = Map.class.getSimpleName();
      _builder.append(_simpleName_18, "\t");
      _builder.append("<");
      String _simpleName_19 = String.class.getSimpleName();
      _builder.append(_simpleName_19, "\t");
      _builder.append(", ");
      String _simpleName_20 = List.class.getSimpleName();
      _builder.append(_simpleName_20, "\t");
      _builder.append("<");
      String _simpleName_21 = Object.class.getSimpleName();
      _builder.append(_simpleName_21, "\t");
      _builder.append(">> getMetadata() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("return metadata;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public void addMetadata(");
      String _simpleName_22 = String.class.getSimpleName();
      _builder.append(_simpleName_22, "\t");
      _builder.append(" key, ");
      String _simpleName_23 = Object.class.getSimpleName();
      _builder.append(_simpleName_23, "\t");
      _builder.append(" value) {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      String _simpleName_24 = List.class.getSimpleName();
      _builder.append(_simpleName_24, "\t\t");
      _builder.append("<");
      String _simpleName_25 = Object.class.getSimpleName();
      _builder.append(_simpleName_25, "\t\t");
      _builder.append("> valueList = metadata.get(key);");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("if (valueList == null) {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("valueList = new ");
      String _simpleName_26 = ArrayList.class.getSimpleName();
      _builder.append(_simpleName_26, "\t\t\t");
      _builder.append("<>();");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("valueList.add(value);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compileAORuleIntern(final ODLOperator o, final IODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      String _simpleName = o.getSimpleName();
      String name = (_simpleName + IODLCompilerHelper.AO_RULE_OPERATOR);
      String superClass = AbstractTransformationRule.class.getSimpleName();
      String _simpleName_1 = o.getSimpleName();
      String aoName = (_simpleName_1 + IODLCompilerHelper.AO_OPERATOR);
      String _simpleName_2 = o.getSimpleName();
      String poName = (_simpleName_2 + IODLCompilerHelper.PO_OPERATOR);
      String transform = TransformationConfiguration.class.getSimpleName();
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("@SuppressWarnings(\"all\")");
      _builder.newLine();
      _builder.append("public class ");
      _builder.append(name);
      _builder.append(" extends ");
      _builder.append(superClass);
      _builder.append("<");
      _builder.append(aoName);
      _builder.append("> implements ");
      String _simpleName_3 = IODLAORule.class.getSimpleName();
      _builder.append(_simpleName_3);
      _builder.append("<");
      _builder.append(aoName);
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
      _builder.append(" config) {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("defaultExecute(operator, new ");
      _builder.append(poName, "\t\t");
      _builder.append("(operator), config, true, true);");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public boolean isExecutable(");
      _builder.append(aoName, "\t");
      _builder.append(" operator,TransformationConfiguration config) {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("return operator.isAllPhysicalInputSet();");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public IRuleFlowGroup getRuleFlowGroup() {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("return TransformRuleFlowGroup.TRANSFORMATION;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compilePOIntern(final IQLModelElement element, final ODLOperator o, final IODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      String _simpleName = o.getSimpleName();
      String opName = (_simpleName + IODLCompilerHelper.PO_OPERATOR);
      String _simpleName_1 = o.getSimpleName();
      String aoName = (_simpleName_1 + IODLCompilerHelper.AO_OPERATOR);
      Class<AbstractPipe> superClass = AbstractPipe.class;
      JvmTypeReference read = this.helper.determineReadType(o);
      JvmTypeReference write = read;
      JvmTypeReference meta = this.helper.determineMetadataType(o);
      Collection<JvmMember> members = this.helper.getPOMembers(o);
      Collection<IQLAttribute> attributes = this.helper.getPOAttributes(o);
      Collection<IQLAttribute> aoAttributes = this.helper.getAOAndPOAttributes(o);
      Collection<IQLNewExpression> newExpressions = this.helper.getNewExpressions(o);
      Collection<IQLVariableStatement> varStmts = this.helper.getVarStatements(o);
      AbstractPipe.OutputMode outputmode = this.helper.determineOutputMode(o);
      boolean hasProcessNext = this.helper.hasProcessNext(o);
      boolean hasProcessPunctuation = this.helper.hasProcessPunctuation(o);
      boolean hasInit = this.helper.hasPOInitMethod(o);
      context.addImport(superClass.getCanonicalName());
      context.addImport(outputmode.getClass().getCanonicalName());
      context.addImport(IODLPO.class.getCanonicalName());
      context.addImport(IPunctuation.class.getCanonicalName());
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
      _builder.append("public class ");
      _builder.append(opName);
      _builder.append(" extends ");
      String _simpleName_2 = superClass.getSimpleName();
      _builder.append(_simpleName_2);
      _builder.append("<");
      String _compile = this.typeCompiler.compile(read, context, false);
      _builder.append(_compile);
      _builder.append("<");
      String _compile_1 = this.typeCompiler.compile(meta, context, false);
      _builder.append(_compile_1);
      _builder.append(">,");
      String _compile_2 = this.typeCompiler.compile(write, context, false);
      _builder.append(_compile_2);
      _builder.append("<");
      String _compile_3 = this.typeCompiler.compile(meta, context, false);
      _builder.append(_compile_3);
      _builder.append(">> implements ");
      String _simpleName_3 = IODLPO.class.getSimpleName();
      _builder.append(_simpleName_3);
      _builder.append("<");
      String _compile_4 = this.typeCompiler.compile(read, context, false);
      _builder.append(_compile_4);
      _builder.append("<");
      String _compile_5 = this.typeCompiler.compile(meta, context, false);
      _builder.append(_compile_5);
      _builder.append(">,");
      String _compile_6 = this.typeCompiler.compile(write, context, false);
      _builder.append(_compile_6);
      _builder.append("<");
      String _compile_7 = this.typeCompiler.compile(meta, context, false);
      _builder.append(_compile_7);
      _builder.append(">> {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public ");
      _builder.append(opName, "\t");
      _builder.append("() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("super();\t\t\t\t");
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
        for(final IQLAttribute attr : attributes) {
          _builder.append("\t\t");
          String _createCloneStatements = this.createCloneStatements(attr, "po", context);
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
      {
        for(final IQLAttribute attr_1 : aoAttributes) {
          _builder.append("\t\t");
          String _createCloneStatements_1 = this.createCloneStatements(attr_1, "ao", context);
          _builder.append(_createCloneStatements_1, "\t\t");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        if (hasInit) {
          _builder.append("\t\t");
          _builder.append("initialize();");
          _builder.newLine();
        }
      }
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      {
        for(final JvmMember m : members) {
          _builder.append("\t");
          String _compile_8 = this.compile(m, context);
          _builder.append(_compile_8, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("@");
      String _simpleName_4 = Override.class.getSimpleName();
      _builder.append(_simpleName_4, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("public ");
      String _simpleName_5 = AbstractPipe.OutputMode.class.getSimpleName();
      _builder.append(_simpleName_5, "\t");
      _builder.append(" getOutputMode() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("return ");
      String _simpleName_6 = AbstractPipe.OutputMode.class.getSimpleName();
      _builder.append(_simpleName_6, "\t\t");
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
        for(final IQLAttribute attr_2 : attributes) {
          _builder.append("\t");
          String pName = this.helper.firstCharUpperCase(attr_2.getSimpleName());
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          String type = this.typeCompiler.compile(attr_2.getType(), context, false);
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
          String _simpleName_7 = attr_2.getSimpleName();
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
        if ((!hasProcessPunctuation)) {
          _builder.append("\t");
          _builder.append("@Override");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("public void processPunctuation(");
          String _simpleName_8 = IPunctuation.class.getSimpleName();
          _builder.append(_simpleName_8, "\t");
          _builder.append(" punctuation, int port) {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("sendPunctuation(punctuation);");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("}");
          _builder.newLine();
          _builder.append("\t");
          _builder.newLine();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      {
        if ((!hasProcessNext)) {
          _builder.append("\t");
          _builder.append("@Override");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("protected void process_next(");
          String _compile_9 = this.typeCompiler.compile(read, context, false);
          _builder.append(_compile_9, "\t");
          _builder.append(" object, int port) {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("transfer(object);");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("}");
          _builder.newLine();
          _builder.append("\t");
          _builder.newLine();
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
              JvmTypeReference type_1 = decl.getRef();
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              CharSequence _createGetterMethod_2 = this.createGetterMethod(type_1, a_1.getInit().getArgsMap(), context);
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
  public String compile(final JvmMember m, final IODLGeneratorContext context) {
    String _xifexpression = null;
    if ((m instanceof ODLParameter)) {
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
  
  public String compile(final ODLParameter a, final IODLGeneratorContext context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private ");
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
  
  public String compile(final ODLMethod m, final IODLGeneratorContext context) {
    String _xifexpression = null;
    if ((m.isValidate() && (!Objects.equal(m.getSimpleName(), null)))) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("private boolean validate");
      String _simpleName = m.getSimpleName();
      _builder.append(_simpleName);
      _builder.append("()");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      String _compile = this.stmtCompiler.compile(m.getBody(), context);
      _builder.append(_compile, "\t");
      _builder.append("\t");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _xifexpression = _builder.toString();
    } else {
      String _xifexpression_1 = null;
      if ((m.isValidate() && Objects.equal(m.getSimpleName(), null))) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("protected boolean validate()");
        _builder_1.newLine();
        _builder_1.append("\t");
        String _compile_1 = this.stmtCompiler.compile(m.getBody(), context);
        _builder_1.append(_compile_1, "\t");
        _builder_1.append("\t");
        _builder_1.newLineIfNotEmpty();
        _builder_1.newLine();
        _xifexpression_1 = _builder_1.toString();
      } else {
        String _xifexpression_2 = null;
        boolean _isOn = m.isOn();
        if (_isOn) {
          String _xblockexpression = null;
          {
            String className = this.helper.getClassName(m);
            String returnT = "";
            IEventMethod eventMethod = EventMethodsFactory.getInstance().getEventMethod(context.isAo(), m.getSimpleName(), m.getParameters());
            if (((!Objects.equal(m.getReturnType(), null)) && (!m.getSimpleName().equalsIgnoreCase(className)))) {
              returnT = this.typeCompiler.compile(m.getReturnType(), context, false);
            } else {
              if ((Objects.equal(m.getReturnType(), null) && (!m.getSimpleName().equalsIgnoreCase(className)))) {
                returnT = "void";
              }
            }
            StringConcatenation _builder_2 = new StringConcatenation();
            {
              boolean _isOverride = eventMethod.isOverride();
              if (_isOverride) {
                _builder_2.append("@Override");
                _builder_2.newLine();
              }
            }
            _builder_2.append("public ");
            _builder_2.append(returnT);
            _builder_2.append(" ");
            String _methodName = eventMethod.getMethodName();
            _builder_2.append(_methodName);
            _builder_2.append("(");
            {
              EList<JvmFormalParameter> _parameters = m.getParameters();
              boolean _notEquals = (!Objects.equal(_parameters, null));
              if (_notEquals) {
                final Function1<JvmFormalParameter, String> _function = (JvmFormalParameter p) -> {
                  return this.compile(p, context);
                };
                String _join = IterableExtensions.join(ListExtensions.<JvmFormalParameter, String>map(m.getParameters(), _function), ", ");
                _builder_2.append(_join);
              }
            }
            _builder_2.append(")");
            _builder_2.newLineIfNotEmpty();
            _builder_2.append("\t");
            String _compile_2 = this.stmtCompiler.compile(m.getBody(), context);
            _builder_2.append(_compile_2, "\t");
            _builder_2.append("\t");
            _builder_2.newLineIfNotEmpty();
            _builder_2.newLine();
            _xblockexpression = _builder_2.toString();
          }
          _xifexpression_2 = _xblockexpression;
        } else {
          _xifexpression_2 = super.compile(m, context);
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String createCloneStatements(final IQLAttribute attr, final String varName, final IODLGeneratorContext context) {
    String _xblockexpression = null;
    {
      String name = attr.getSimpleName();
      String pName = this.helper.firstCharUpperCase(name);
      JvmTypeReference type = attr.getType();
      String content = "";
      boolean ifStmt = true;
      boolean _isList = this.lookUp.isList(type);
      if (_isList) {
        context.addImport(IQLUtils.class.getCanonicalName());
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("this.");
        _builder.append(name);
        _builder.append(" = ");
        String _simpleName = IQLUtils.class.getSimpleName();
        _builder.append(_simpleName);
        _builder.append(".copyList(");
        _builder.append(varName);
        _builder.append(".get");
        _builder.append(pName);
        _builder.append("());");
        content = _builder.toString();
      } else {
        boolean _isMap = this.lookUp.isMap(type);
        if (_isMap) {
          context.addImport(IQLUtils.class.getCanonicalName());
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("this.");
          _builder_1.append(name);
          _builder_1.append(" = ");
          String _simpleName_1 = IQLUtils.class.getSimpleName();
          _builder_1.append(_simpleName_1);
          _builder_1.append(".copyMap(");
          _builder_1.append(varName);
          _builder_1.append(".get");
          _builder_1.append(pName);
          _builder_1.append("());");
          content = _builder_1.toString();
        } else {
          boolean _isClonable = this.lookUp.isClonable(type);
          if (_isClonable) {
            StringConcatenation _builder_2 = new StringConcatenation();
            _builder_2.append("this.");
            _builder_2.append(name);
            _builder_2.append(" = ");
            _builder_2.append(varName);
            _builder_2.append(".get");
            _builder_2.append(pName);
            _builder_2.append("().clone();");
            content = _builder_2.toString();
          } else {
            ifStmt = false;
            StringConcatenation _builder_3 = new StringConcatenation();
            _builder_3.append("this.");
            _builder_3.append(name);
            _builder_3.append(" = ");
            _builder_3.append(varName);
            _builder_3.append(".get");
            _builder_3.append(pName);
            _builder_3.append("();");
            content = _builder_3.toString();
          }
        }
      }
      String _xifexpression = null;
      if (ifStmt) {
        StringConcatenation _builder_4 = new StringConcatenation();
        _builder_4.append("if (");
        _builder_4.append(varName);
        _builder_4.append(".get");
        _builder_4.append(pName);
        _builder_4.append("() != null)");
        _builder_4.newLineIfNotEmpty();
        _builder_4.append("\t");
        _builder_4.append(content, "\t");
        _builder_4.newLineIfNotEmpty();
        _xifexpression = _builder_4.toString();
      } else {
        StringConcatenation _builder_5 = new StringConcatenation();
        _builder_5.append(content);
        _xifexpression = _builder_5.toString();
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
}
