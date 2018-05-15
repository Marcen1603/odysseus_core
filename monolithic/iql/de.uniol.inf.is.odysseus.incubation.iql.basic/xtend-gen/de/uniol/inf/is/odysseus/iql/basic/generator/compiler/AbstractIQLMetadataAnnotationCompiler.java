package de.uniol.inf.is.odysseus.iql.basic.generator.compiler;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMapElement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleBoolean;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleDouble;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleInt;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleNull;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public abstract class AbstractIQLMetadataAnnotationCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, U extends IIQLTypeUtils> implements IIQLMetadataAnnotationCompiler<G> {
  protected H helper;
  
  protected T typeCompiler;
  
  protected U typeUtils;
  
  public AbstractIQLMetadataAnnotationCompiler(final H helper, final T typeCompiler, final U typeUtils) {
    this.helper = helper;
    this.typeCompiler = typeCompiler;
    this.typeUtils = typeUtils;
  }
  
  public String compile(final IQLMetadataList o, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    EList<IQLMetadata> _elements = o.getElements();
    final Function1<IQLMetadata, String> _function = (IQLMetadata e) -> {
      return this.compile(e, c);
    };
    List<String> _map = ListExtensions.<IQLMetadata, String>map(_elements, _function);
    String _join = IterableExtensions.join(_map, ", ");
    _builder.append(_join, "");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String compile(final IQLMetadata o, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = o.getName();
    _builder.append(_name, "");
    _builder.append(" = ");
    IQLMetadataValue _value = o.getValue();
    String _compile = this.compile(_value, c);
    _builder.append(_compile, "");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String compile(final IQLMetadataValue o, final G c) {
    if ((o instanceof IQLMetadataValueSingleInt)) {
      return this.compile(((IQLMetadataValueSingleInt) o), c);
    } else {
      if ((o instanceof IQLMetadataValueSingleDouble)) {
        return this.compile(((IQLMetadataValueSingleDouble) o), c);
      } else {
        if ((o instanceof IQLMetadataValueSingleString)) {
          return this.compile(((IQLMetadataValueSingleString) o), c);
        } else {
          if ((o instanceof IQLMetadataValueSingleBoolean)) {
            return this.compile(((IQLMetadataValueSingleBoolean) o), c);
          } else {
            if ((o instanceof IQLMetadataValueSingleNull)) {
              return this.compile(((IQLMetadataValueSingleNull) o), c);
            } else {
              if ((o instanceof IQLMetadataValueSingleTypeRef)) {
                return this.compile(((IQLMetadataValueSingleTypeRef) o), c);
              } else {
                if ((o instanceof IQLMetadataValueList)) {
                  return this.compile(((IQLMetadataValueList) o), c);
                } else {
                  if ((o instanceof IQLMetadataValueMap)) {
                    return this.compile(((IQLMetadataValueMap) o), c);
                  }
                }
              }
            }
          }
        }
      }
    }
    return "";
  }
  
  public String compile(final IQLMetadataValueSingleInt o, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    int _value = o.getValue();
    _builder.append(_value, "");
    return _builder.toString();
  }
  
  public String compile(final IQLMetadataValueSingleDouble o, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    double _value = o.getValue();
    _builder.append(_value, "");
    return _builder.toString();
  }
  
  public String compile(final IQLMetadataValueSingleString o, final G c) {
    String _xifexpression = null;
    JvmTypeReference _expectedTypeRef = c.getExpectedTypeRef();
    boolean _tripleNotEquals = (_expectedTypeRef != null);
    if (_tripleNotEquals) {
      String _xifexpression_1 = null;
      JvmTypeReference _expectedTypeRef_1 = c.getExpectedTypeRef();
      boolean _isCharacter = this.typeUtils.isCharacter(_expectedTypeRef_1);
      if (_isCharacter) {
        String _value = o.getValue();
        String _plus = ("\'" + _value);
        return (_plus + "\'");
      } else {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("\"");
        String _value_1 = o.getValue();
        _builder.append(_value_1, "");
        _builder.append("\"");
        _xifexpression_1 = _builder.toString();
      }
      _xifexpression = _xifexpression_1;
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("\"");
      String _value_2 = o.getValue();
      _builder_1.append(_value_2, "");
      _builder_1.append("\"");
      _xifexpression = _builder_1.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLMetadataValueSingleBoolean o, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    boolean _isValue = o.isValue();
    _builder.append(_isValue, "");
    return _builder.toString();
  }
  
  public String compile(final IQLMetadataValueSingleNull o, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("null");
    return _builder.toString();
  }
  
  public String compile(final IQLMetadataValueSingleTypeRef o, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    JvmTypeReference _value = o.getValue();
    String _compile = this.typeCompiler.compile(_value, c, true);
    _builder.append(_compile, "");
    _builder.append(".class");
    return _builder.toString();
  }
  
  public String compile(final IQLMetadataValueList o, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    EList<IQLMetadataValue> _elements = o.getElements();
    final Function1<IQLMetadataValue, String> _function = (IQLMetadataValue e) -> {
      return this.compile(e, c);
    };
    List<String> _map = ListExtensions.<IQLMetadataValue, String>map(_elements, _function);
    String _join = IterableExtensions.join(_map, ", ");
    _builder.append(_join, "");
    _builder.append("}");
    return _builder.toString();
  }
  
  public String compile(final IQLMetadataValueMap o, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    EList<IQLMetadataValueMapElement> _elements = o.getElements();
    final Function1<IQLMetadataValueMapElement, String> _function = (IQLMetadataValueMapElement e) -> {
      return this.compile(e, c);
    };
    List<String> _map = ListExtensions.<IQLMetadataValueMapElement, String>map(_elements, _function);
    String _join = IterableExtensions.join(_map, ", ");
    _builder.append(_join, "");
    _builder.append(")");
    return _builder.toString();
  }
  
  public String compile(final IQLMetadataValueMapElement o, final G c) {
    StringConcatenation _builder = new StringConcatenation();
    IQLMetadataValue _key = o.getKey();
    String _compile = this.compile(_key, c);
    _builder.append(_compile, "");
    _builder.append(" = ");
    IQLMetadataValue _value = o.getValue();
    String _compile_1 = this.compile(_value, c);
    _builder.append(_compile_1, "");
    return _builder.toString();
  }
}
