package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLExpressionCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import java.util.List;
import org.eclipse.emf.common.util.EList;
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
    if ((typeRef == null)) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("void");
      return _builder.toString();
    }
    if ((typeRef instanceof IQLArrayTypeRef)) {
      w = true;
      if ((nodeText != null)) {
        String _replace = nodeText.replace("[", "");
        String _replace_1 = _replace.replace("]", "");
        String _trim = _replace_1.trim();
        nodeText = _trim;
      }
      IQLArrayTypeRef arrayTypeRef = ((IQLArrayTypeRef) typeRef);
      JvmType _type = arrayTypeRef.getType();
      IQLArrayType arrayType = ((IQLArrayType) _type);
      EList<String> _dimensions = arrayType.getDimensions();
      int _size = _dimensions.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        EList<String> _dimensions_1 = arrayType.getDimensions();
        int _size_1 = _dimensions_1.size();
        ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size_1, true);
        for (final Integer i : _doubleDotLessThan) {
          String _result = result;
          StringConcatenation _builder_1 = new StringConcatenation();
          String _simpleName = List.class.getSimpleName();
          _builder_1.append(_simpleName, "");
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
      EList<String> _dimensions_2 = arrayType_1.getDimensions();
      int _size_2 = _dimensions_2.size();
      boolean _greaterThan_1 = (_size_2 > 0);
      if (_greaterThan_1) {
        EList<String> _dimensions_3 = arrayType_1.getDimensions();
        int _size_3 = _dimensions_3.size();
        ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, _size_3, true);
        for (final Integer i_1 : _doubleDotLessThan_1) {
          String _result_2 = result;
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append(">");
          result = (_result_2 + _builder_2);
        }
        String _canonicalName = List.class.getCanonicalName();
        context.addImport(_canonicalName);
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
        String _importName = this.typeDictionary.getImportName(innerType);
        context.addImport(_importName);
      }
      StringConcatenation _builder = new StringConcatenation();
      String _simpleName = this.typeDictionary.getSimpleName(innerType, nodeText, wrapper, false);
      _builder.append(_simpleName, "");
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
}
