package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization;
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
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.AbstractQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.DefaultQDLSource;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
  
  public String compile(final QDLQuery q, final IQDLGeneratorContext context) {
    String _xblockexpression = null;
    {
      StringBuilder builder = new StringBuilder();
      EObject _eContainer = q.eContainer();
      IQLModelElement element = ((IQLModelElement) _eContainer);
      String _compileQuery = this.compileQuery(element, q, context);
      builder.append(_compileQuery);
      String _canonicalName = AbstractQDLQuery.class.getCanonicalName();
      context.addImport(_canonicalName);
      String _canonicalName_1 = Collection.class.getCanonicalName();
      context.addImport(_canonicalName_1);
      String _canonicalName_2 = IQDLOperator.class.getCanonicalName();
      context.addImport(_canonicalName_2);
      String _canonicalName_3 = ArrayList.class.getCanonicalName();
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
  
  public String compileQuery(final IQLModelElement element, final QDLQuery q, final IQDLGeneratorContext context) {
    String _xblockexpression = null;
    {
      String name = q.getSimpleName();
      String superClass = AbstractQDLQuery.class.getSimpleName();
      IQLMetadataList _metadataList = q.getMetadataList();
      boolean metadata = (!Objects.equal(_metadataList, null));
      IQLStatement _statements = q.getStatements();
      IQLStatementBlock block = ((IQLStatementBlock) _statements);
      Collection<IQLNewExpression> newExpressions = this.helper.getNewExpressions(q);
      Collection<IQLVariableStatement> varStmts = this.helper.getVarStatements(q);
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      {
        EList<IQLJavaMetadata> _javametadata = element.getJavametadata();
        for(final IQLJavaMetadata j : _javametadata) {
          IQLJava _java = j.getJava();
          String text = _java.getText();
          _builder.newLineIfNotEmpty();
          _builder.append(text, "");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("@SuppressWarnings(\"all\")");
      _builder.newLine();
      _builder.append("public class ");
      _builder.append(name, "");
      _builder.append(" extends ");
      _builder.append(superClass, "");
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
      _builder.append("public ");
      String _simpleName = Collection.class.getSimpleName();
      _builder.append(_simpleName, "\t");
      _builder.append("<");
      String _simpleName_1 = IQDLOperator.class.getSimpleName();
      _builder.append(_simpleName_1, "\t");
      _builder.append("> execute() {");
      _builder.newLineIfNotEmpty();
      {
        Collection<String> _sources = this.typeDictionary.getSources();
        for(final String source : _sources) {
          _builder.append("\t \t");
          String _canonicalName = StreamAO.class.getCanonicalName();
          context.addImport(_canonicalName);
          _builder.newLineIfNotEmpty();
          _builder.append("\t \t");
          String _canonicalName_1 = DefaultQDLSource.class.getCanonicalName();
          context.addImport(_canonicalName_1);
          _builder.append("\t\t\t \t\t");
          _builder.newLineIfNotEmpty();
          _builder.append("\t \t");
          String _simpleName_2 = DefaultQDLSource.class.getSimpleName();
          _builder.append(_simpleName_2, "\t \t");
          _builder.append(" ");
          _builder.append(source, "\t \t");
          _builder.append(" = getSource(\"");
          _builder.append(source, "\t \t");
          _builder.append("\");");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t \t");
      String _simpleName_3 = Collection.class.getSimpleName();
      _builder.append(_simpleName_3, "\t \t");
      _builder.append("<");
      String _simpleName_4 = IQDLOperator.class.getSimpleName();
      _builder.append(_simpleName_4, "\t \t");
      _builder.append("> operators = new ");
      String _simpleName_5 = ArrayList.class.getSimpleName();
      _builder.append(_simpleName_5, "\t \t");
      _builder.append("<>();");
      _builder.newLineIfNotEmpty();
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
            boolean _and = false;
            boolean _and_1 = false;
            IQLVariableInitialization _init = a.getInit();
            boolean _notEquals = (!Objects.equal(_init, null));
            if (!_notEquals) {
              _and_1 = false;
            } else {
              IQLVariableInitialization _init_1 = a.getInit();
              IQLArgumentsMap _argsMap = _init_1.getArgsMap();
              boolean _notEquals_1 = (!Objects.equal(_argsMap, null));
              _and_1 = _notEquals_1;
            }
            if (!_and_1) {
              _and = false;
            } else {
              IQLVariableInitialization _init_2 = a.getInit();
              IQLArgumentsMap _argsMap_1 = _init_2.getArgsMap();
              EList<IQLArgumentsMapKeyValue> _elements = _argsMap_1.getElements();
              int _size = _elements.size();
              boolean _greaterThan = (_size > 0);
              _and = _greaterThan;
            }
            if (_and) {
              _builder.append("\t");
              IQLVariableInitialization _init_3 = a.getInit();
              IQLArgumentsMap _argsMap_2 = _init_3.getArgsMap();
              CharSequence _createGetterMethod = this.createGetterMethod(type, _argsMap_2, context);
              _builder.append(_createGetterMethod, "\t");
              _builder.newLineIfNotEmpty();
            } else {
              boolean _and_2 = false;
              boolean _and_3 = false;
              IQLVariableInitialization _init_4 = a.getInit();
              boolean _notEquals_2 = (!Objects.equal(_init_4, null));
              if (!_notEquals_2) {
                _and_3 = false;
              } else {
                IQLVariableInitialization _init_5 = a.getInit();
                IQLArgumentsList _argsList = _init_5.getArgsList();
                boolean _notEquals_3 = (!Objects.equal(_argsList, null));
                _and_3 = _notEquals_3;
              }
              if (!_and_3) {
                _and_2 = false;
              } else {
                boolean _isOperator = this.helper.isOperator(type);
                _and_2 = _isOperator;
              }
              if (_and_2) {
                _builder.append("\t");
                IQLVariableInitialization _init_6 = a.getInit();
                IQLArgumentsMap _argsMap_3 = _init_6.getArgsMap();
                CharSequence _createGetterMethod_1 = this.createGetterMethod(type, _argsMap_3, context);
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
            boolean _and_4 = false;
            IQLArgumentsMap _argsMap_4 = e.getArgsMap();
            boolean _notEquals_4 = (!Objects.equal(_argsMap_4, null));
            if (!_notEquals_4) {
              _and_4 = false;
            } else {
              IQLArgumentsMap _argsMap_5 = e.getArgsMap();
              EList<IQLArgumentsMapKeyValue> _elements_1 = _argsMap_5.getElements();
              int _size_1 = _elements_1.size();
              boolean _greaterThan_1 = (_size_1 > 0);
              _and_4 = _greaterThan_1;
            }
            if (_and_4) {
              _builder.append("\t");
              JvmTypeReference _ref = e.getRef();
              IQLArgumentsMap _argsMap_6 = e.getArgsMap();
              CharSequence _createGetterMethod_2 = this.createGetterMethod(_ref, _argsMap_6, context);
              _builder.append(_createGetterMethod_2, "\t");
              _builder.newLineIfNotEmpty();
            } else {
              JvmTypeReference _ref_1 = e.getRef();
              boolean _isOperator_1 = this.helper.isOperator(_ref_1);
              if (_isOperator_1) {
                _builder.append("\t");
                JvmTypeReference _ref_2 = e.getRef();
                IQLArgumentsMap _argsMap_7 = e.getArgsMap();
                CharSequence _createGetterMethod_3 = this.createGetterMethod(_ref_2, _argsMap_7, context);
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
          IQLMetadataList _metadataList_1 = q.getMetadataList();
          String _compile_1 = this.methodCompiler.compile(_metadataList_1, context);
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
  
  public CharSequence createGetterMethod(final JvmTypeReference typeRef, final IQLArgumentsMap map, final IQDLGeneratorContext context) {
    CharSequence _xifexpression = null;
    boolean _isOperator = this.helper.isOperator(typeRef);
    if (_isOperator) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("private ");
      String _compile = this.typeCompiler.compile(typeRef, context, false);
      _builder.append(_compile, "");
      _builder.append(" getOperator");
      String _shortName = this.typeUtils.getShortName(typeRef, false);
      _builder.append(_shortName, "");
      int _hashCode = typeRef.hashCode();
      _builder.append(_hashCode, "");
      _builder.append("(");
      String _compile_1 = this.typeCompiler.compile(typeRef, context, false);
      _builder.append(_compile_1, "");
      _builder.append(" type, ");
      String _simpleName = Collection.class.getSimpleName();
      _builder.append(_simpleName, "");
      _builder.append("<");
      String _simpleName_1 = IQDLOperator.class.getSimpleName();
      _builder.append(_simpleName_1, "");
      _builder.append("> operators");
      {
        boolean _and = false;
        boolean _notEquals = (!Objects.equal(map, null));
        if (!_notEquals) {
          _and = false;
        } else {
          EList<IQLArgumentsMapKeyValue> _elements = map.getElements();
          int _size = _elements.size();
          boolean _greaterThan = (_size > 0);
          _and = _greaterThan;
        }
        if (_and) {
          _builder.append(", ");
          EList<IQLArgumentsMapKeyValue> _elements_1 = map.getElements();
          final Function1<IQLArgumentsMapKeyValue, String> _function = new Function1<IQLArgumentsMapKeyValue, String>() {
            public String apply(final IQLArgumentsMapKeyValue el) {
              return QDLCompiler.super.compile(el, typeRef, context);
            }
          };
          List<String> _map = ListExtensions.<IQLArgumentsMapKeyValue, String>map(_elements_1, _function);
          String _join = IterableExtensions.join(_map, ", ");
          _builder.append(_join, "");
        }
      }
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("operators.add(type);");
      _builder.newLine();
      {
        boolean _notEquals_1 = (!Objects.equal(map, null));
        if (_notEquals_1) {
          {
            EList<IQLArgumentsMapKeyValue> _elements_2 = map.getElements();
            for(final IQLArgumentsMapKeyValue el : _elements_2) {
              _builder.append("\t");
              JvmIdentifiableElement _key = el.getKey();
              String attrName = _key.getSimpleName();
              _builder.newLineIfNotEmpty();
              {
                boolean _isParameter = this.helper.isParameter(attrName, typeRef);
                if (_isParameter) {
                  _builder.append("\t");
                  _builder.append("type.setParameter(\"");
                  _builder.append(attrName, "\t");
                  _builder.append("\", ");
                  _builder.append(attrName, "\t");
                  _builder.append(");");
                  _builder.newLineIfNotEmpty();
                } else {
                  {
                    JvmIdentifiableElement _key_1 = el.getKey();
                    if ((_key_1 instanceof JvmOperation)) {
                      _builder.append("\t");
                      _builder.append("type.");
                      JvmIdentifiableElement _key_2 = el.getKey();
                      String _simpleName_2 = _key_2.getSimpleName();
                      _builder.append(_simpleName_2, "\t");
                      _builder.append("(");
                      JvmIdentifiableElement _key_3 = el.getKey();
                      String _simpleName_3 = _key_3.getSimpleName();
                      _builder.append(_simpleName_3, "\t");
                      _builder.append(");\t\t\t\t");
                      _builder.newLineIfNotEmpty();
                    } else {
                      _builder.append("\t");
                      _builder.append("type.");
                      JvmIdentifiableElement _key_4 = el.getKey();
                      String _simpleName_4 = _key_4.getSimpleName();
                      _builder.append(_simpleName_4, "\t");
                      _builder.append(" = ");
                      JvmIdentifiableElement _key_5 = el.getKey();
                      String _simpleName_5 = _key_5.getSimpleName();
                      _builder.append(_simpleName_5, "\t");
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
