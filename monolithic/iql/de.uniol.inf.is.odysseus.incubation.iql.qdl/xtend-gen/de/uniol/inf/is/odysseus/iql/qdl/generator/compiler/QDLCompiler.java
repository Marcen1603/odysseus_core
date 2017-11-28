package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.IQDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.AbstractQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.QDLSourceImpl;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.Operator;
import de.uniol.inf.is.odysseus.iql.qdl.types.source.Source;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class QDLCompiler extends AbstractIQLCompiler<IQDLCompilerHelper, IQDLGeneratorContext, IQDLTypeCompiler, IQDLStatementCompiler, IQDLTypeDictionary, IQDLTypeUtils> implements IQDLCompiler {
  @Inject
  private IQDLMetadataMethodCompiler methodCompiler;
  
  @Inject
  public QDLCompiler(final IQDLCompilerHelper helper, final IQDLTypeCompiler typeCompiler, final IQDLStatementCompiler stmtCompiler, final IQDLTypeDictionary typeDictionary, final IQDLTypeUtils typeUtils) {
    super(helper, typeCompiler, stmtCompiler, typeDictionary, typeUtils);
  }
  
  @Override
  public String compile(final QDLQuery q, final IQDLGeneratorContext context) {
    String _xblockexpression = null;
    {
      StringBuilder builder = new StringBuilder();
      EObject _eContainer = q.eContainer();
      IQLModelElement element = ((IQLModelElement) _eContainer);
      builder.append(this.compileQuery(element, q, context));
      context.addImport(AbstractQDLQuery.class.getCanonicalName());
      context.addImport(Collection.class.getCanonicalName());
      context.addImport(Operator.class.getCanonicalName());
      context.addImport(ArrayList.class.getCanonicalName());
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
  
  public String compileQuery(final IQLModelElement element, final QDLQuery q, final IQDLGeneratorContext context) {
    String _xblockexpression = null;
    {
      String name = q.getSimpleName();
      String superClass = AbstractQDLQuery.class.getSimpleName();
      IQLMetadataList _metadataList = q.getMetadataList();
      boolean metadata = (_metadataList != null);
      IQLStatement _statements = q.getStatements();
      IQLStatementBlock block = ((IQLStatementBlock) _statements);
      Collection<IQLNewExpression> newExpressions = this.helper.getNewExpressions(q);
      Collection<IQLVariableStatement> varStmts = this.helper.getVarStatements(q);
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
      _builder.append(name);
      _builder.append(" extends ");
      _builder.append(superClass);
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public ");
      _builder.append(name, "\t");
      _builder.append("() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("super();");
      _builder.newLine();
      {
        if (metadata) {
          _builder.append("\t\t");
          _builder.append(IIQLMetadataMethodCompiler.CREATE_METADATA_METHOD_NAME, "\t\t");
          _builder.append("();");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public String getName() {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("return \"");
      String _simpleName = q.getSimpleName();
      _builder.append(_simpleName, "\t\t");
      _builder.append("\";");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public ");
      String _simpleName_1 = Collection.class.getSimpleName();
      _builder.append(_simpleName_1, "\t");
      _builder.append("<");
      String _simpleName_2 = Operator.class.getSimpleName();
      _builder.append(_simpleName_2, "\t");
      _builder.append("> execute() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      String _simpleName_3 = Collection.class.getSimpleName();
      _builder.append(_simpleName_3, "\t\t");
      _builder.append("<");
      String _simpleName_4 = Operator.class.getSimpleName();
      _builder.append(_simpleName_4, "\t\t");
      _builder.append("> operators = new ");
      String _simpleName_5 = ArrayList.class.getSimpleName();
      _builder.append(_simpleName_5, "\t\t");
      _builder.append("<>();");
      _builder.newLineIfNotEmpty();
      {
        Collection<String> _sources = this.typeDictionary.getSources();
        for(final String source : _sources) {
          _builder.append("\t \t");
          context.addImport(StreamAO.class.getCanonicalName());
          _builder.newLineIfNotEmpty();
          _builder.append("\t \t");
          context.addImport(Source.class.getCanonicalName());
          _builder.append("\t\t");
          _builder.newLineIfNotEmpty();
          _builder.append("\t \t");
          context.addImport(QDLSourceImpl.class.getCanonicalName());
          _builder.newLineIfNotEmpty();
          _builder.append("\t \t");
          String _simpleName_6 = Source.class.getSimpleName();
          _builder.append(_simpleName_6, "\t \t");
          _builder.append(" ");
          _builder.append(source, "\t \t");
          _builder.append(" = new ");
          String _simpleName_7 = QDLSourceImpl.class.getSimpleName();
          _builder.append(_simpleName_7, "\t \t");
          _builder.append("(\"");
          _builder.append(source, "\t \t");
          _builder.append("\");");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        EList<IQLStatement> _statements_1 = block.getStatements();
        for(final IQLStatement stmt : _statements_1) {
          _builder.append("\t \t");
          String _compile = this.stmtCompiler.compile(stmt, context);
          _builder.append(_compile, "\t \t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t\t");
      _builder.append("return operators;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      {
        for(final IQLVariableStatement a : varStmts) {
          _builder.append("\t");
          JvmIdentifiableElement _var = a.getVar();
          IQLVariableDeclaration decl = ((IQLVariableDeclaration) _var);
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          JvmTypeReference type = decl.getRef();
          _builder.newLineIfNotEmpty();
          {
            if ((((a.getInit() != null) && (a.getInit().getArgsMap() != null)) && (a.getInit().getArgsMap().getElements().size() > 0))) {
              _builder.append("\t");
              CharSequence _createGetterMethod = this.createGetterMethod(type, a.getInit().getArgsMap(), context);
              _builder.append(_createGetterMethod, "\t");
              _builder.newLineIfNotEmpty();
            } else {
              if ((((a.getInit() != null) && (a.getInit().getArgsList() != null)) && this.helper.isOperator(type))) {
                _builder.append("\t");
                CharSequence _createGetterMethod_1 = this.createGetterMethod(type, a.getInit().getArgsMap(), context);
                _builder.append(_createGetterMethod_1, "\t");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
      _builder.append("\t\t");
      _builder.newLine();
      {
        for(final IQLNewExpression e : newExpressions) {
          {
            if (((e.getArgsMap() != null) && (e.getArgsMap().getElements().size() > 0))) {
              _builder.append("\t");
              CharSequence _createGetterMethod_2 = this.createGetterMethod(e.getRef(), e.getArgsMap(), context);
              _builder.append(_createGetterMethod_2, "\t");
              _builder.newLineIfNotEmpty();
            } else {
              boolean _isOperator = this.helper.isOperator(e.getRef());
              if (_isOperator) {
                _builder.append("\t");
                CharSequence _createGetterMethod_3 = this.createGetterMethod(e.getRef(), e.getArgsMap(), context);
                _builder.append(_createGetterMethod_3, "\t");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
      _builder.append("\t");
      _builder.newLine();
      {
        if (metadata) {
          _builder.append("\t");
          String _compile_1 = this.methodCompiler.compile(q.getMetadataList(), context);
          _builder.append(_compile_1, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("} ");
      _builder.newLine();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  @Override
  public CharSequence createGetterMethod(final JvmTypeReference typeRef, final IQLArgumentsMap map, final IQDLGeneratorContext context) {
    CharSequence _xifexpression = null;
    boolean _isOperator = this.helper.isOperator(typeRef);
    if (_isOperator) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("private ");
      String _compile = this.typeCompiler.compile(typeRef, context, false);
      _builder.append(_compile);
      _builder.append(" getOperator");
      String _shortName = this.typeUtils.getShortName(typeRef, false);
      _builder.append(_shortName);
      int _hashCode = typeRef.hashCode();
      _builder.append(_hashCode);
      _builder.append("(");
      String _compile_1 = this.typeCompiler.compile(typeRef, context, false);
      _builder.append(_compile_1);
      _builder.append(" type, ");
      String _simpleName = Collection.class.getSimpleName();
      _builder.append(_simpleName);
      _builder.append("<");
      String _simpleName_1 = Operator.class.getSimpleName();
      _builder.append(_simpleName_1);
      _builder.append("> operators");
      {
        if (((map != null) && (map.getElements().size() > 0))) {
          _builder.append(", ");
          final Function1<IQLArgumentsMapKeyValue, String> _function = (IQLArgumentsMapKeyValue el) -> {
            return super.compile(el, typeRef, context);
          };
          String _join = IterableExtensions.join(ListExtensions.<IQLArgumentsMapKeyValue, String>map(map.getElements(), _function), ", ");
          _builder.append(_join);
        }
      }
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("operators.add(type);");
      _builder.newLine();
      {
        if ((map != null)) {
          {
            EList<IQLArgumentsMapKeyValue> _elements = map.getElements();
            for(final IQLArgumentsMapKeyValue el : _elements) {
              {
                JvmIdentifiableElement _key = el.getKey();
                if ((_key instanceof JvmOperation)) {
                  {
                    boolean _isParameter = this.helper.isParameter(el.getKey().getSimpleName().substring(3), typeRef);
                    if (_isParameter) {
                      _builder.append("\t");
                      _builder.append("type.setParameter(\"");
                      String _substring = el.getKey().getSimpleName().substring(3);
                      _builder.append(_substring, "\t");
                      _builder.append("\", ");
                      String _simpleName_2 = el.getKey().getSimpleName();
                      _builder.append(_simpleName_2, "\t");
                      _builder.append(");");
                      _builder.newLineIfNotEmpty();
                    } else {
                      _builder.append("\t");
                      _builder.append("type.");
                      String _simpleName_3 = el.getKey().getSimpleName();
                      _builder.append(_simpleName_3, "\t");
                      _builder.append("(");
                      String _simpleName_4 = el.getKey().getSimpleName();
                      _builder.append(_simpleName_4, "\t");
                      _builder.append(");");
                      _builder.newLineIfNotEmpty();
                    }
                  }
                } else {
                  {
                    boolean _isParameter_1 = this.helper.isParameter(el.getKey().getSimpleName(), typeRef);
                    if (_isParameter_1) {
                      _builder.append("\t");
                      _builder.append("type.setParameter(\"");
                      String _simpleName_5 = el.getKey().getSimpleName();
                      _builder.append(_simpleName_5, "\t");
                      _builder.append("\", ");
                      String _simpleName_6 = el.getKey().getSimpleName();
                      _builder.append(_simpleName_6, "\t");
                      _builder.append(");");
                      _builder.newLineIfNotEmpty();
                    } else {
                      _builder.append("\t");
                      _builder.append("type.");
                      String _simpleName_7 = el.getKey().getSimpleName();
                      _builder.append(_simpleName_7, "\t");
                      _builder.append(" = ");
                      String _simpleName_8 = el.getKey().getSimpleName();
                      _builder.append(_simpleName_8, "\t");
                      _builder.append(";");
                      _builder.newLineIfNotEmpty();
                    }
                  }
                }
              }
            }
          }
        }
      }
      _builder.append("\t");
      _builder.append("return type;");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xifexpression = _builder;
    } else {
      _xifexpression = super.createGetterMethod(typeRef, map, context);
    }
    return _xifexpression;
  }
}
