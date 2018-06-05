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
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLMetadataMethodCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmTypeReference;

@SuppressWarnings("all")
public abstract class AbstractIQLMetadataMethodCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>> implements IIQLMetadataMethodCompiler<G> {
  public final static String ADD_METADATA_METHOD_NAME = "addMetadata";
  
  public final static String METADATA_VALUE_VAR_NAME = "metadataValue";
  
  protected H helper;
  
  protected T typeCompiler;
  
  @Inject
  protected IIQLTypeUtils typeUtils;
  
  public AbstractIQLMetadataMethodCompiler(final H helper, final T typeCompiler) {
    this.helper = helper;
    this.typeCompiler = typeCompiler;
  }
  
  @Override
  public String compile(final IQLMetadataList o, final G context) {
    String _xblockexpression = null;
    {
      AtomicInteger counter = new AtomicInteger(0);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("public void ");
      _builder.append(IIQLMetadataMethodCompiler.CREATE_METADATA_METHOD_NAME, "");
      _builder.append("() {");
      _builder.newLineIfNotEmpty();
      {
        EList<IQLMetadata> _elements = o.getElements();
        for(final IQLMetadata m : _elements) {
          _builder.append("\t");
          int _incrementAndGet = counter.incrementAndGet();
          String varName = (AbstractIQLMetadataMethodCompiler.METADATA_VALUE_VAR_NAME + Integer.valueOf(_incrementAndGet));
          _builder.newLineIfNotEmpty();
          {
            IQLMetadataValue _value = m.getValue();
            boolean _tripleNotEquals = (_value != null);
            if (_tripleNotEquals) {
              _builder.append("\t");
              IQLMetadataValue _value_1 = m.getValue();
              String _compile = this.compile(_value_1, varName, counter, context);
              _builder.append(_compile, "\t");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append(AbstractIQLMetadataMethodCompiler.ADD_METADATA_METHOD_NAME, "\t");
              _builder.append("(\"");
              String _name = m.getName();
              _builder.append(_name, "\t");
              _builder.append("\",");
              _builder.append(varName, "\t");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
            } else {
              _builder.append("\t");
              _builder.append(AbstractIQLMetadataMethodCompiler.ADD_METADATA_METHOD_NAME, "\t");
              _builder.append("(\"");
              String _name_1 = m.getName();
              _builder.append(_name_1, "\t");
              _builder.append("\",null);");
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
  
  public String compile(final IQLMetadataValue o, final String varName, final AtomicInteger counter, final G context) {
    if ((o instanceof IQLMetadataValueSingleInt)) {
      return this.compile(((IQLMetadataValueSingleInt) o), varName, context);
    } else {
      if ((o instanceof IQLMetadataValueSingleDouble)) {
        return this.compile(((IQLMetadataValueSingleDouble) o), varName, context);
      } else {
        if ((o instanceof IQLMetadataValueSingleString)) {
          return this.compile(((IQLMetadataValueSingleString) o), varName, context);
        } else {
          if ((o instanceof IQLMetadataValueSingleBoolean)) {
            return this.compile(((IQLMetadataValueSingleBoolean) o), varName, context);
          } else {
            if ((o instanceof IQLMetadataValueSingleNull)) {
              return this.compile(((IQLMetadataValueSingleNull) o), varName, context);
            } else {
              if ((o instanceof IQLMetadataValueSingleTypeRef)) {
                return this.compile(((IQLMetadataValueSingleTypeRef) o), varName, context);
              } else {
                if ((o instanceof IQLMetadataValueList)) {
                  return this.compile(((IQLMetadataValueList) o), varName, counter, context);
                } else {
                  if ((o instanceof IQLMetadataValueMap)) {
                    return this.compile(((IQLMetadataValueMap) o), varName, counter, context);
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
  
  public String compile(final IQLMetadataValueSingleInt o, final String varName, final G context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("int ");
    _builder.append(varName, "");
    _builder.append(" = ");
    int _value = o.getValue();
    _builder.append(_value, "");
    _builder.append(";");
    return _builder.toString();
  }
  
  public String compile(final IQLMetadataValueSingleDouble o, final String varName, final G context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("double ");
    _builder.append(varName, "");
    _builder.append(" = ");
    double _value = o.getValue();
    _builder.append(_value, "");
    _builder.append(";");
    return _builder.toString();
  }
  
  public String compile(final IQLMetadataValueSingleString o, final String varName, final G context) {
    String _xifexpression = null;
    JvmTypeReference _expectedTypeRef = context.getExpectedTypeRef();
    boolean _tripleNotEquals = (_expectedTypeRef != null);
    if (_tripleNotEquals) {
      String _xifexpression_1 = null;
      JvmTypeReference _expectedTypeRef_1 = context.getExpectedTypeRef();
      boolean _isCharacter = this.typeUtils.isCharacter(_expectedTypeRef_1);
      if (_isCharacter) {
        String _value = o.getValue();
        String _plus = ((("String " + varName) + " = \'") + _value);
        return (_plus + "\';");
      } else {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("String ");
        _builder.append(varName, "");
        _builder.append(" = \"");
        String _value_1 = o.getValue();
        _builder.append(_value_1, "");
        _builder.append("\";");
        _xifexpression_1 = _builder.toString();
      }
      _xifexpression = _xifexpression_1;
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("String ");
      _builder_1.append(varName, "");
      _builder_1.append(" = \"");
      String _value_2 = o.getValue();
      _builder_1.append(_value_2, "");
      _builder_1.append("\";");
      _xifexpression = _builder_1.toString();
    }
    return _xifexpression;
  }
  
  public String compile(final IQLMetadataValueSingleBoolean o, final String varName, final G context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("boolean ");
    _builder.append(varName, "");
    _builder.append(" = ");
    boolean _isValue = o.isValue();
    _builder.append(_isValue, "");
    _builder.append(";");
    return _builder.toString();
  }
  
  public String compile(final IQLMetadataValueSingleNull o, final String varName, final G context) {
    StringConcatenation _builder = new StringConcatenation();
    String _simpleName = Object.class.getSimpleName();
    _builder.append(_simpleName, "");
    _builder.append(" ");
    _builder.append(varName, "");
    _builder.append(" = null;");
    return _builder.toString();
  }
  
  public String compile(final IQLMetadataValueSingleTypeRef o, final String varName, final G context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Class<?> ");
    _builder.append(varName, "");
    _builder.append(" = ");
    JvmTypeReference _value = o.getValue();
    String _compile = this.typeCompiler.compile(_value, context, true);
    _builder.append(_compile, "");
    _builder.append(".class; ");
    return _builder.toString();
  }
  
  public String compile(final IQLMetadataValueList o, final String varName, final AtomicInteger counter, final G context) {
    String _xblockexpression = null;
    {
      String _canonicalName = List.class.getCanonicalName();
      context.addImport(_canonicalName);
      String _canonicalName_1 = ArrayList.class.getCanonicalName();
      context.addImport(_canonicalName_1);
      StringConcatenation _builder = new StringConcatenation();
      String _simpleName = List.class.getSimpleName();
      _builder.append(_simpleName, "");
      _builder.append("<");
      String _simpleName_1 = Object.class.getSimpleName();
      _builder.append(_simpleName_1, "");
      _builder.append("> ");
      _builder.append(varName, "");
      _builder.append(" = new ");
      String _simpleName_2 = ArrayList.class.getSimpleName();
      _builder.append(_simpleName_2, "");
      _builder.append("<>(); ");
      _builder.newLineIfNotEmpty();
      {
        EList<IQLMetadataValue> _elements = o.getElements();
        for(final IQLMetadataValue e : _elements) {
          int _incrementAndGet = counter.incrementAndGet();
          String name = (AbstractIQLMetadataMethodCompiler.METADATA_VALUE_VAR_NAME + Integer.valueOf(_incrementAndGet));
          _builder.newLineIfNotEmpty();
          String _compile = this.compile(e, name, counter, context);
          _builder.append(_compile, "");
          _builder.newLineIfNotEmpty();
          _builder.append(varName, "");
          _builder.append(".add(");
          _builder.append(name, "");
          _builder.append(");");
          _builder.newLineIfNotEmpty();
        }
      }
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  public String compile(final IQLMetadataValueMap o, final String varName, final AtomicInteger counter, final G context) {
    String _xblockexpression = null;
    {
      String _canonicalName = Map.class.getCanonicalName();
      context.addImport(_canonicalName);
      String _canonicalName_1 = HashMap.class.getCanonicalName();
      context.addImport(_canonicalName_1);
      StringConcatenation _builder = new StringConcatenation();
      String _simpleName = Map.class.getSimpleName();
      _builder.append(_simpleName, "");
      _builder.append("<");
      String _simpleName_1 = Object.class.getSimpleName();
      _builder.append(_simpleName_1, "");
      _builder.append(",");
      String _simpleName_2 = Object.class.getSimpleName();
      _builder.append(_simpleName_2, "");
      _builder.append("> ");
      _builder.append(varName, "");
      _builder.append(" = new ");
      String _simpleName_3 = HashMap.class.getSimpleName();
      _builder.append(_simpleName_3, "");
      _builder.append("<>(); ");
      _builder.newLineIfNotEmpty();
      {
        EList<IQLMetadataValueMapElement> _elements = o.getElements();
        for(final IQLMetadataValueMapElement e : _elements) {
          int _incrementAndGet = counter.incrementAndGet();
          String keyVar = (AbstractIQLMetadataMethodCompiler.METADATA_VALUE_VAR_NAME + Integer.valueOf(_incrementAndGet));
          _builder.newLineIfNotEmpty();
          int _incrementAndGet_1 = counter.incrementAndGet();
          String valueVar = (AbstractIQLMetadataMethodCompiler.METADATA_VALUE_VAR_NAME + Integer.valueOf(_incrementAndGet_1));
          _builder.newLineIfNotEmpty();
          IQLMetadataValue _key = e.getKey();
          String _compile = this.compile(_key, keyVar, counter, context);
          _builder.append(_compile, "");
          _builder.newLineIfNotEmpty();
          IQLMetadataValue _value = e.getValue();
          String _compile_1 = this.compile(_value, valueVar, counter, context);
          _builder.append(_compile_1, "");
          _builder.newLineIfNotEmpty();
          _builder.append(varName, "");
          _builder.append(".put(");
          _builder.append(keyVar, "");
          _builder.append(", ");
          _builder.append(valueVar, "");
          _builder.append(");");
          _builder.newLineIfNotEmpty();
        }
      }
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
}
