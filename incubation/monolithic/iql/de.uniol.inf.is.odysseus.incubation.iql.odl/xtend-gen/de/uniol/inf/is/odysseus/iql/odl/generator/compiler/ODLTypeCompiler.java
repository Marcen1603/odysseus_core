package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import javax.inject.Inject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmTypeReference;

@SuppressWarnings("all")
public class ODLTypeCompiler extends AbstractIQLTypeCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLExpressionCompiler, ODLTypeFactory> {
  @Inject
  public ODLTypeCompiler(final ODLCompilerHelper helper, final ODLTypeFactory typeFactory) {
    super(helper, typeFactory);
  }
  
  public String compile(final JvmTypeReference typeRef, final ODLGeneratorContext context, final String nodeText, final boolean wrapper) {
    String result = super.compile(typeRef, context, nodeText, wrapper);
    ODLParameter parameter = EcoreUtil2.<ODLParameter>getContainerOfType(typeRef, ODLParameter.class);
    boolean _and = false;
    boolean _and_1 = false;
    boolean _notEquals = (!Objects.equal(parameter, null));
    if (!_notEquals) {
      _and_1 = false;
    } else {
      boolean _isArray = this.typeFactory.isArray(typeRef);
      boolean _not = (!_isArray);
      _and_1 = _not;
    }
    if (!_and_1) {
      _and = false;
    } else {
      boolean _isList = this.typeFactory.isList(typeRef);
      _and = _isList;
    }
    if (_and) {
      JvmTypeReference listElement = this.typeFactory.getListElementType(parameter);
      String _result = result;
      String _compile = this.compile(listElement, context, true);
      String _plus = ("<" + _compile);
      String _plus_1 = (_plus + ">");
      result = (_result + _plus_1);
    } else {
      boolean _and_2 = false;
      boolean _and_3 = false;
      boolean _notEquals_1 = (!Objects.equal(parameter, null));
      if (!_notEquals_1) {
        _and_3 = false;
      } else {
        boolean _isArray_1 = this.typeFactory.isArray(typeRef);
        boolean _not_1 = (!_isArray_1);
        _and_3 = _not_1;
      }
      if (!_and_3) {
        _and_2 = false;
      } else {
        boolean _isMap = this.typeFactory.isMap(typeRef);
        _and_2 = _isMap;
      }
      if (_and_2) {
        JvmTypeReference key = this.typeFactory.getMapKeyType(parameter);
        JvmTypeReference value = this.typeFactory.getMapValueType(parameter);
        String _result_1 = result;
        String _compile_1 = this.compile(key, context, true);
        String _plus_2 = ("<" + _compile_1);
        String _plus_3 = (_plus_2 + ",");
        String _compile_2 = this.compile(value, context, true);
        String _plus_4 = (_plus_3 + _compile_2);
        String _plus_5 = (_plus_4 + ">");
        result = (_result_1 + _plus_5);
      }
    }
    return result;
  }
}
