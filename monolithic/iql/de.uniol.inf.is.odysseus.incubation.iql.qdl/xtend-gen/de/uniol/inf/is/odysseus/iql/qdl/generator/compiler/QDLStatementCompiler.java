package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.IQDLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLStatementCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.IQDLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.IQDLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;

@SuppressWarnings("all")
public class QDLStatementCompiler extends AbstractIQLStatementCompiler<IQDLCompilerHelper, IQDLGeneratorContext, IQDLTypeCompiler, IQDLExpressionCompiler, IQDLTypeUtils, IQDLExpressionEvaluator, IQDLLookUp> implements IQDLStatementCompiler {
  @Inject
  public QDLStatementCompiler(final IQDLCompilerHelper helper, final IQDLExpressionCompiler exprCompiler, final IQDLTypeCompiler typeCompiler, final IQDLTypeUtils typeUtils, final IQDLExpressionEvaluator exprEvaluator, final IQDLLookUp lookUp) {
    super(helper, exprCompiler, typeCompiler, typeUtils, exprEvaluator, lookUp);
  }
  
  @Override
  public String compile(final IQLVariableInitialization init, final JvmTypeReference typeRef, final IQDLGeneratorContext context) {
    String _xifexpression = null;
    boolean _isOperator = this.helper.isOperator(typeRef);
    if (_isOperator) {
      String _xifexpression_1 = null;
      if ((((init.getArgsList() != null) && (init.getArgsMap() != null)) && (init.getArgsMap().getElements().size() > 0))) {
        String _xblockexpression = null;
        {
          IQLArgumentsList _argsList = init.getArgsList();
          EList<IQLExpression> _elements = _argsList.getElements();
          JvmExecutable constructor = this.lookUp.findPublicConstructor(typeRef, _elements);
          IQLArgumentsList _argsList_1 = init.getArgsList();
          EList<IQLExpression> _elements_1 = _argsList_1.getElements();
          int _size = _elements_1.size();
          boolean args = (_size > 0);
          String _xifexpression_2 = null;
          if ((constructor != null)) {
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
            IQLArgumentsMap _argsMap = init.getArgsMap();
            String _compile_2 = this.exprCompiler.compile(_argsMap, typeRef, context);
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
            IQLArgumentsMap _argsMap_1 = init.getArgsMap();
            String _compile_5 = this.exprCompiler.compile(_argsMap_1, typeRef, context);
            _builder_1.append(_compile_5, "");
            _builder_1.append(")");
            _xifexpression_2 = _builder_1.toString();
          }
          _xblockexpression = _xifexpression_2;
        }
        _xifexpression_1 = _xblockexpression;
      } else {
        String _xifexpression_2 = null;
        if (((init.getArgsMap() != null) && (init.getArgsMap().getElements().size() > 0))) {
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
          _builder.append("\"), operators, ");
          IQLArgumentsMap _argsMap = init.getArgsMap();
          String _compile_1 = this.exprCompiler.compile(_argsMap, typeRef, context);
          _builder.append(_compile_1, "");
          _builder.append(")");
          _xifexpression_2 = _builder.toString();
        } else {
          String _xifexpression_3 = null;
          IQLArgumentsList _argsList = init.getArgsList();
          boolean _tripleNotEquals = (_argsList != null);
          if (_tripleNotEquals) {
            String _xblockexpression_1 = null;
            {
              IQLArgumentsList _argsList_1 = init.getArgsList();
              EList<IQLExpression> _elements = _argsList_1.getElements();
              JvmExecutable constructor = this.lookUp.findPublicConstructor(typeRef, _elements);
              IQLArgumentsList _argsList_2 = init.getArgsList();
              EList<IQLExpression> _elements_1 = _argsList_2.getElements();
              int _size = _elements_1.size();
              boolean args = (_size > 0);
              String _xifexpression_4 = null;
              if ((constructor != null)) {
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
                IQLArgumentsList _argsList_3 = init.getArgsList();
                EList<JvmFormalParameter> _parameters = constructor.getParameters();
                String _compile_3 = this.exprCompiler.compile(_argsList_3, _parameters, context);
                _builder_1.append(_compile_3, "");
                _builder_1.append("), operators)");
                _xifexpression_4 = _builder_1.toString();
              } else {
                StringConcatenation _builder_2 = new StringConcatenation();
                _builder_2.append("getOperator");
                String _shortName_4 = this.typeUtils.getShortName(typeRef, false);
                _builder_2.append(_shortName_4, "");
                int _hashCode_2 = typeRef.hashCode();
                _builder_2.append(_hashCode_2, "");
                _builder_2.append("(new ");
                String _compile_4 = this.typeCompiler.compile(typeRef, context, false);
                _builder_2.append(_compile_4, "");
                _builder_2.append("(\"");
                String _shortName_5 = this.typeUtils.getShortName(typeRef, false);
                _builder_2.append(_shortName_5, "");
                _builder_2.append("\"");
                {
                  if (args) {
                    _builder_2.append(", ");
                  }
                }
                IQLArgumentsList _argsList_4 = init.getArgsList();
                String _compile_5 = this.exprCompiler.compile(_argsList_4, context);
                _builder_2.append(_compile_5, "");
                _builder_2.append("), operators)");
                _xifexpression_4 = _builder_2.toString();
              }
              _xblockexpression_1 = _xifexpression_4;
            }
            _xifexpression_3 = _xblockexpression_1;
          } else {
            _xifexpression_3 = super.compile(init, typeRef, context);
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    } else {
      String _xifexpression_4 = null;
      boolean _isSource = this.helper.isSource(typeRef);
      if (_isSource) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("new ");
        String _compile_2 = this.typeCompiler.compile(typeRef, context, false);
        _builder_1.append(_compile_2, "");
        _builder_1.append("(\"");
        String _shortName_2 = this.typeUtils.getShortName(typeRef, false);
        _builder_1.append(_shortName_2, "");
        _builder_1.append("\")");
        _xifexpression_4 = _builder_1.toString();
      } else {
        _xifexpression_4 = super.compile(init, typeRef, context);
      }
      _xifexpression = _xifexpression_4;
    }
    return _xifexpression;
  }
}
