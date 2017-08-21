package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;

@SuppressWarnings("all")
public abstract class AbstractIQLTypeCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, E extends IIQLExpressionCompiler<G>, F extends IIQLTypeDictionary, U extends IIQLTypeUtils> implements IIQLTypeCompiler<G> {
  protected H helper;
  
  protected F typeDictionary;
  
  protected U typeUtils;
  
  public AbstractIQLTypeCompiler(final H helper, final F typeDictionary, final U typeUtils) {
    this.helper = helper;
    this.typeDictionary = typeDictionary;
    this.typeUtils = typeUtils;
  }
  
  @Override
  public String compile(final JvmTypeReference typeRef, final G context, final boolean wrapper) {
    boolean w = wrapper;
    String result = "";
    String nodeText = this.helper.getNodeText(typeRef);
    boolean _equals = Objects.equal(typeRef, null);
    if (_equals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("void");
      return _builder.toString();
    }
    if ((typeRef instanceof IQLArrayTypeRef)) {
      w = true;
      boolean _notEquals = (!Objects.equal(nodeText, null));
      if (_notEquals) {
        nodeText = nodeText.replace("[", "").replace("]", "").trim();
      }
      IQLArrayTypeRef arrayTypeRef = ((IQLArrayTypeRef) typeRef);
      JvmType _type = arrayTypeRef.getType();
      IQLArrayType arrayType = ((IQLArrayType) _type);
      int _size = arrayType.getDimensions().size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        int _size_1 = arrayType.getDimensions().size();
        ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size_1, true);
        for (final Integer i : _doubleDotLessThan) {
          String _result = result;
          StringConcatenation _builder_1 = new StringConcatenation();
          String _simpleName = List.class.getSimpleName();
          _builder_1.append(_simpleName);
          _builder_1.append("<");
          result = (_result + _builder_1);
        }
      }
    }
    String _result_1 = result;
    String _compile = this.compile(typeRef, context, nodeText, w);
    result = (_result_1 + _compile);
    if ((typeRef instanceof IQLArrayTypeRef)) {
      IQLArrayTypeRef arrayTypeRef_1 = ((IQLArrayTypeRef) typeRef);
      JvmType _type_1 = arrayTypeRef_1.getType();
      IQLArrayType arrayType_1 = ((IQLArrayType) _type_1);
      int _size_2 = arrayType_1.getDimensions().size();
      boolean _greaterThan_1 = (_size_2 > 0);
      if (_greaterThan_1) {
        int _size_3 = arrayType_1.getDimensions().size();
        ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, _size_3, true);
        for (final Integer i_1 : _doubleDotLessThan_1) {
          String _result_2 = result;
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append(">");
          result = (_result_2 + _builder_2);
        }
        context.addImport(List.class.getCanonicalName());
      }
    }
    return result;
  }
  
  public String compile(final JvmTypeReference typeRef, final G context, final String nodeText, final boolean wrapper) {
    String _xblockexpression = null;
    {
      JvmType innerType = this.typeUtils.getInnerType(typeRef, false);
      boolean _isImportNeeded = this.typeDictionary.isImportNeeded(innerType, nodeText);
      if (_isImportNeeded) {
        context.addImport(this.typeDictionary.getImportName(innerType));
      }
      StringConcatenation _builder = new StringConcatenation();
      String _simpleName = this.typeDictionary.getSimpleName(innerType, nodeText, wrapper, false);
      _builder.append(_simpleName);
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
}
