package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.QDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLExpressionParser;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;

@SuppressWarnings("all")
public class QDLStatementCompiler extends AbstractIQLStatementCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler, QDLExpressionCompiler, QDLTypeUtils, QDLExpressionParser, QDLLookUp> {
  @Inject
  public QDLStatementCompiler(final QDLCompilerHelper helper, final QDLExpressionCompiler exprCompiler, final QDLTypeCompiler typeCompiler, final QDLTypeUtils typeUtils, final QDLExpressionParser exprParser, final QDLLookUp lookUp) {
    super(helper, exprCompiler, typeCompiler, typeUtils, exprParser, lookUp);
  }
  
  public String compile(final IQLVariableInitialization init, final JvmTypeReference typeRef, final QDLGeneratorContext context) {
    String _xifexpression = null;
    boolean _isOperator = this.helper.isOperator(typeRef);
    if (_isOperator) {
      String _xifexpression_1 = null;
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
        String _xblockexpression = null;
        {
          IQLArgumentsList _argsList = init.getArgsList();
          EList<IQLExpression> _elements_1 = _argsList.getElements();
          JvmExecutable constructor = this.lookUp.findPublicConstructor(typeRef, _elements_1);
          IQLArgumentsList _argsList_1 = init.getArgsList();
          EList<IQLExpression> _elements_2 = _argsList_1.getElements();
          int _size_1 = _elements_2.size();
          boolean args = (_size_1 > 0);
          String _xifexpression_2 = null;
          boolean _notEquals_1 = (!Objects.equal(constructor, null));
          if (_notEquals_1) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("getOperator");
            String _shortName = this.typeUtils.getShortName(typeRef, false);
            _builder.append(_shortName, "");
            int _hashCode = typeRef.hashCode();
            _builder.append(_hashCode, "");
            _builder.append("(new ");
            String _compile = this.typeCompiler.compile(typeRef, context, false);
            _builder.append(_compile, "");
            _builder.append("(\"");
            String _shortName_1 = this.typeUtils.getShortName(typeRef, false);
            _builder.append(_shortName_1, "");
            _builder.append("\"");
            {
              if (args) {
                _builder.append(", ");
              }
            }
            IQLArgumentsList _argsList_2 = init.getArgsList();
            EList<JvmFormalParameter> _parameters = constructor.getParameters();
            String _compile_1 = this.exprCompiler.compile(_argsList_2, _parameters, context);
            _builder.append(_compile_1, "");
            _builder.append("), operators, ");
            IQLArgumentsMap _argsMap_2 = init.getArgsMap();
            String _compile_2 = this.exprCompiler.compile(_argsMap_2, typeRef, context);
            _builder.append(_compile_2, "");
            _builder.append(")");
            _xifexpression_2 = _builder.toString();
          } else {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append("getOperator");
            String _shortName_2 = this.typeUtils.getShortName(typeRef, false);
            _builder_1.append(_shortName_2, "");
            int _hashCode_1 = typeRef.hashCode();
            _builder_1.append(_hashCode_1, "");
            _builder_1.append("(new ");
            String _compile_3 = this.typeCompiler.compile(typeRef, context, false);
            _builder_1.append(_compile_3, "");
            _builder_1.append("(\"");
            String _shortName_3 = this.typeUtils.getShortName(typeRef, false);
            _builder_1.append(_shortName_3, "");
            _builder_1.append("\"");
            {
              if (args) {
                _builder_1.append(", ");
              }
            }
            IQLArgumentsList _argsList_3 = init.getArgsList();
            String _compile_4 = this.exprCompiler.compile(_argsList_3, context);
            _builder_1.append(_compile_4, "");
            _builder_1.append("), operators, ");
            IQLArgumentsMap _argsMap_3 = init.getArgsMap();
            String _compile_5 = this.exprCompiler.compile(_argsMap_3, typeRef, context);
            _builder_1.append(_compile_5, "");
            _builder_1.append(")");
            _xifexpression_2 = _builder_1.toString();
          }
          _xblockexpression = _xifexpression_2;
        }
        _xifexpression_1 = _xblockexpression;
      } else {
        String _xifexpression_2 = null;
        IQLArgumentsList _argsList = init.getArgsList();
        boolean _notEquals_1 = (!Objects.equal(_argsList, null));
        if (_notEquals_1) {
          String _xblockexpression_1 = null;
          {
            IQLArgumentsList _argsList_1 = init.getArgsList();
            EList<IQLExpression> _elements_1 = _argsList_1.getElements();
            JvmExecutable constructor = this.lookUp.findPublicConstructor(typeRef, _elements_1);
            IQLArgumentsList _argsList_2 = init.getArgsList();
            EList<IQLExpression> _elements_2 = _argsList_2.getElements();
            int _size_1 = _elements_2.size();
            boolean args = (_size_1 > 0);
            String _xifexpression_3 = null;
            boolean _notEquals_2 = (!Objects.equal(constructor, null));
            if (_notEquals_2) {
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("getOperator");
              String _shortName = this.typeUtils.getShortName(typeRef, false);
              _builder.append(_shortName, "");
              int _hashCode = typeRef.hashCode();
              _builder.append(_hashCode, "");
              _builder.append("(new ");
              String _compile = this.typeCompiler.compile(typeRef, context, false);
              _builder.append(_compile, "");
              _builder.append("(\"");
              String _shortName_1 = this.typeUtils.getShortName(typeRef, false);
              _builder.append(_shortName_1, "");
              _builder.append("\"");
              {
                if (args) {
                  _builder.append(", ");
                }
              }
              IQLArgumentsList _argsList_3 = init.getArgsList();
              EList<JvmFormalParameter> _parameters = constructor.getParameters();
              String _compile_1 = this.exprCompiler.compile(_argsList_3, _parameters, context);
              _builder.append(_compile_1, "");
              _builder.append("), operators)");
              _xifexpression_3 = _builder.toString();
            } else {
              StringConcatenation _builder_1 = new StringConcatenation();
              _builder_1.append("getOperator");
              String _shortName_2 = this.typeUtils.getShortName(typeRef, false);
              _builder_1.append(_shortName_2, "");
              int _hashCode_1 = typeRef.hashCode();
              _builder_1.append(_hashCode_1, "");
              _builder_1.append("(new ");
              String _compile_2 = this.typeCompiler.compile(typeRef, context, false);
              _builder_1.append(_compile_2, "");
              _builder_1.append("(\"");
              String _shortName_3 = this.typeUtils.getShortName(typeRef, false);
              _builder_1.append(_shortName_3, "");
              _builder_1.append("\"");
              {
                if (args) {
                  _builder_1.append(", ");
                }
              }
              IQLArgumentsList _argsList_4 = init.getArgsList();
              String _compile_3 = this.exprCompiler.compile(_argsList_4, context);
              _builder_1.append(_compile_3, "");
              _builder_1.append("), operators)");
              _xifexpression_3 = _builder_1.toString();
            }
            _xblockexpression_1 = _xifexpression_3;
          }
          _xifexpression_2 = _xblockexpression_1;
        } else {
          _xifexpression_2 = super.compile(init, typeRef, context);
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    } else {
      String _xifexpression_3 = null;
      boolean _isSource = this.helper.isSource(typeRef);
      if (_isSource) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("getSource(\"");
        String _shortName = this.typeUtils.getShortName(typeRef, false);
        _builder.append(_shortName, "");
        _builder.append("\")");
        _xifexpression_3 = _builder.toString();
      } else {
        _xifexpression_3 = super.compile(init, typeRef, context);
      }
      _xifexpression = _xifexpression_3;
    }
    return _xifexpression;
  }
}
