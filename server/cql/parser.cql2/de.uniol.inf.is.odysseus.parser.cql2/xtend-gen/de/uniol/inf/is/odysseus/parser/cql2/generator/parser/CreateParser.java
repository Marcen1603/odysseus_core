package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.AccessFramework;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Create;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateAccessFramework;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateChannelFormatViaFile;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateChannelFrameworkViaPort;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseSink;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseStream;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateView;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.InnerSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.InnerSelect2;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SchemaDefinition;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StreamTo;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Time;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.OperatorCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ICreateParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ISelectParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class CreateParser implements ICreateParser {
  @Inject
  private ISelectParser selectParser;
  
  @Inject
  private ICacheService cacheService;
  
  @Inject
  private IUtilityService utilityService;
  
  @Inject
  private AbstractPQLOperatorBuilder builder;
  
  private final String regex = ",$";
  
  @Override
  public void parseCreate(final Create statement) {
    EObject _create = statement.getCreate();
    if ((_create instanceof CreateView)) {
      EObject _create_1 = statement.getCreate();
      this.parseCreateView(((CreateView) _create_1));
    } else {
      EObject _create_2 = statement.getCreate();
      if ((_create_2 instanceof CreateAccessFramework)) {
        EObject _create_3 = statement.getCreate();
        String _type = statement.getType();
        this.parseCreateAccessFramework(((CreateAccessFramework) _create_3), _type);
      } else {
        EObject _create_4 = statement.getCreate();
        if ((_create_4 instanceof CreateChannelFormatViaFile)) {
          EObject _create_5 = statement.getCreate();
          this.parseCreateStreamFile(((CreateChannelFormatViaFile) _create_5));
        } else {
          EObject _create_6 = statement.getCreate();
          if ((_create_6 instanceof CreateChannelFrameworkViaPort)) {
            EObject _create_7 = statement.getCreate();
            this.parseCreateStreamChannel(((CreateChannelFrameworkViaPort) _create_7));
          } else {
            EObject _create_8 = statement.getCreate();
            if ((_create_8 instanceof CreateDatabaseStream)) {
              EObject _create_9 = statement.getCreate();
              this.parseCreateDatabaseStream(((CreateDatabaseStream) _create_9));
            } else {
              EObject _create_10 = statement.getCreate();
              if ((_create_10 instanceof CreateDatabaseSink)) {
                EObject _create_11 = statement.getCreate();
                this.parseCreateDatabaseSink(((CreateDatabaseSink) _create_11));
              }
            }
          }
        }
      }
    }
  }
  
  private CharSequence parseCreateView(final CreateView view) {
    InnerSelect _select = view.getSelect();
    SimpleSelect _select_1 = _select.getSelect();
    SimpleSelect select = ((SimpleSelect) _select_1);
    this.selectParser.parse(select);
    String viewName = view.getName();
    OperatorCache _operatorCache = this.cacheService.getOperatorCache();
    _operatorCache.addSink(viewName);
    return viewName;
  }
  
  private final String SINK_INPUT_KEYWORD = "--INPUT--";
  
  private final String VIEW = "VIEW_KEY_";
  
  private String parseCreateDatabaseSink(final CreateDatabaseSink sink) {
    String _xblockexpression = null;
    {
      Map<String, String> args = CollectionLiterals.<String, String>newHashMap();
      String _database = sink.getDatabase();
      args.put("connection", _database);
      String _table = sink.getTable();
      args.put("table", _table);
      String type = "";
      Set<String> _keySet = CQLGenerator.databaseConnections.keySet();
      String _database_1 = sink.getDatabase();
      boolean _contains = _keySet.contains(_database_1);
      if (_contains) {
        String _database_2 = sink.getDatabase();
        String _get = CQLGenerator.databaseConnections.get(_database_2);
        type = _get;
      } else {
        String _database_3 = sink.getDatabase();
        String _plus = ("Database connection " + _database_3);
        String _plus_1 = (_plus + " could not be found");
        throw new IllegalArgumentException(_plus_1);
      }
      args.put("type", type);
      args.put("input", this.SINK_INPUT_KEYWORD);
      String _xifexpression = null;
      String _option = sink.getOption();
      boolean _tripleNotEquals = (_option != null);
      if (_tripleNotEquals) {
        String _xifexpression_1 = null;
        String _option_1 = sink.getOption();
        String _upperCase = _option_1.toUpperCase();
        boolean _equals = _upperCase.equals("DROP");
        if (_equals) {
          _xifexpression_1 = args.put("drop", "true");
        } else {
          _xifexpression_1 = args.put("truncate", "true");
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  private int getTimeInMilliseconds(final String time, final int value) {
    String _upperCase = time.toUpperCase();
    switch (_upperCase) {
      case "MILLISECONDS":
      case "MILLISECOND":
        return value;
      case "SECONDS":
      case "SECOND":
        return (value * 1000);
      case "MINUTES":
      case "MINUTE":
        return (value * (60 * 1000));
      case "HOURS":
      case "HOUR":
        return (value * ((60 * 60) * 1000));
      case "DAYS":
      case "DAY":
        return (value * (((24 * 60) * 60) * 1000));
      case "WEEKS":
      case "WEEK":
        return (value * ((((7 * 24) * 60) * 60) * 1000));
      default:
        return 0;
    }
  }
  
  public String parseCreateDatabaseStream(final CreateDatabaseStream stream) {
    Map<String, String> args = CollectionLiterals.<String, String>newHashMap();
    String _database = stream.getDatabase();
    args.put("connection", _database);
    String _table = stream.getTable();
    args.put("table", _table);
    SchemaDefinition _attributes = stream.getAttributes();
    CharSequence _extractSchema = this.extractSchema(_attributes);
    String _string = _extractSchema.toString();
    args.put("attributes", _string);
    String operator = "";
    Time _unit = stream.getUnit();
    String _name = _unit.getName();
    int _size = stream.getSize();
    int _timeInMilliseconds = this.getTimeInMilliseconds(_name, _size);
    String waitMillis = Integer.valueOf(_timeInMilliseconds).toString();
    boolean _equals = waitMillis.equals("0.0");
    boolean _not = (!_equals);
    if (_not) {
      args.put("waiteach", waitMillis);
    }
    OperatorCache _operatorCache = this.cacheService.getOperatorCache();
    SchemaDefinition _attributes_1 = stream.getAttributes();
    String _name_1 = _attributes_1.getName();
    String _plus = (this.VIEW + _name_1);
    return _operatorCache.add(_plus, operator);
  }
  
  private CharSequence parseCreateAccessFramework(final CreateAccessFramework create, final String type) {
    String operator = null;
    String _upperCase = type.toUpperCase();
    switch (_upperCase) {
      case "STREAM":
        operator = "ACCESS";
        break;
      case "SINK":
        operator = "SENDER";
        break;
    }
    AccessFramework _pars = create.getPars();
    SchemaDefinition _attributes = create.getAttributes();
    SchemaDefinition _attributes_1 = create.getAttributes();
    String _name = _attributes_1.getName();
    CharSequence _buildCreate1 = this.buildCreate1(operator, _pars, _attributes, _name);
    String _string = _buildCreate1.toString();
    operator = _string;
    String _upperCase_1 = type.toUpperCase();
    boolean _equals = _upperCase_1.equals("SINK");
    if (_equals) {
      boolean _contains = operator.contains(this.SINK_INPUT_KEYWORD);
      boolean _not = (!_contains);
      if (_not) {
        OperatorCache _operatorCache = this.cacheService.getOperatorCache();
        SchemaDefinition _attributes_2 = create.getAttributes();
        String _name_1 = _attributes_2.getName();
        String _plus = (this.VIEW + _name_1);
        return _operatorCache.add(_plus, operator);
      } else {
        OperatorCache _operatorCache_1 = this.cacheService.getOperatorCache();
        Map<String, String> _sinks = _operatorCache_1.getSinks();
        SchemaDefinition _attributes_3 = create.getAttributes();
        String _name_2 = _attributes_3.getName();
        String _plus_1 = (this.VIEW + _name_2);
        _sinks.put(_plus_1, operator);
      }
    } else {
      OperatorCache _operatorCache_2 = this.cacheService.getOperatorCache();
      SchemaDefinition _attributes_4 = create.getAttributes();
      String _name_3 = _attributes_4.getName();
      String _plus_2 = (this.VIEW + _name_3);
      _operatorCache_2.add(_plus_2, operator);
    }
    return "";
  }
  
  private CharSequence extractSchema(final SchemaDefinition schema) {
    ArrayList<String> attributenames = CollectionLiterals.<String>newArrayList();
    ArrayList<String> datatypes = CollectionLiterals.<String>newArrayList();
    for (int i = 0; (i < (schema.getArguments().size() - 1)); i = (i + 2)) {
      {
        EList<String> _arguments = schema.getArguments();
        String _get = _arguments.get(i);
        attributenames.add(_get);
        EList<String> _arguments_1 = schema.getArguments();
        String _get_1 = _arguments_1.get((i + 1));
        datatypes.add(_get_1);
      }
    }
    return this.utilityService.generateKeyValueString(attributenames, datatypes, ",");
  }
  
  private CharSequence parseCreateStreamFile(final CreateChannelFormatViaFile file) {
    Map<String, String> args = CollectionLiterals.<String, String>newHashMap();
    SchemaDefinition _attributes = file.getAttributes();
    String _name = _attributes.getName();
    args.put("source", _name);
    args.put("wrapper", "GenericPull");
    String _type = file.getType();
    args.put("protocol", _type);
    args.put("transport", "File");
    args.put("datahandler", "Tuple");
    SchemaDefinition _attributes_1 = file.getAttributes();
    CharSequence _extractSchema = this.extractSchema(_attributes_1);
    String _string = _extractSchema.toString();
    args.put("schema", _string);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[\'filename\',\'");
    String _filename = file.getFilename();
    _builder.append(_filename, "");
    _builder.append("\'],[\'delimiter\',\';\'],[\'textDelimiter\',\"\'\"]");
    args.put("options", _builder.toString());
    String operator = this.builder.build(AccessAO.class, args);
    OperatorCache _operatorCache = this.cacheService.getOperatorCache();
    SchemaDefinition _attributes_2 = file.getAttributes();
    String _name_1 = _attributes_2.getName();
    String _plus = (this.VIEW + _name_1);
    return _operatorCache.add(_plus, operator);
  }
  
  private CharSequence parseCreateStreamChannel(final CreateChannelFrameworkViaPort channel) {
    Map<String, String> args = CollectionLiterals.<String, String>newHashMap();
    SchemaDefinition _attributes = channel.getAttributes();
    String _name = _attributes.getName();
    args.put("source", _name);
    args.put("wrapper", "GenericPush");
    args.put("protocol", "SizeByteBuffer");
    args.put("transport", "NonBlockingTcp");
    args.put("datahandler", "Tuple");
    SchemaDefinition _attributes_1 = channel.getAttributes();
    CharSequence _extractSchema = this.extractSchema(_attributes_1);
    String _string = _extractSchema.toString();
    args.put("schema", _string);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[\'port\',\'");
    int _port = channel.getPort();
    _builder.append(_port, "");
    _builder.append("\'],[\'host\', \'");
    String _host = channel.getHost();
    _builder.append(_host, "");
    _builder.append("\']");
    args.put("options", _builder.toString());
    String operator = this.builder.build(AccessAO.class, args);
    OperatorCache _operatorCache = this.cacheService.getOperatorCache();
    SchemaDefinition _attributes_2 = channel.getAttributes();
    String _name_1 = _attributes_2.getName();
    String _plus = (this.VIEW + _name_1);
    return _operatorCache.add(_plus, operator);
  }
  
  @Override
  public void parseStreamTo(final StreamTo query) {
    String lastOperator = "";
    String sink = "";
    OperatorCache _operatorCache = this.cacheService.getOperatorCache();
    Map<String, String> sinks = _operatorCache.getSinks();
    Set<String> _keySet = sinks.keySet();
    String _name = query.getName();
    String _plus = (this.VIEW + _name);
    boolean _contains = _keySet.contains(_plus);
    if (_contains) {
      String _name_1 = query.getName();
      String _plus_1 = (this.VIEW + _name_1);
      String _get = sinks.get(_plus_1);
      sink = _get;
    } else {
      Set<String> _keySet_1 = sinks.keySet();
      String _name_2 = query.getName();
      boolean _contains_1 = _keySet_1.contains(_name_2);
      if (_contains_1) {
        String _name_3 = query.getName();
        String _get_1 = sinks.get(_name_3);
        sink = _get_1;
      }
    }
    InnerSelect2 _statement = query.getStatement();
    boolean _tripleNotEquals = (_statement != null);
    if (_tripleNotEquals) {
      InnerSelect2 _statement_1 = query.getStatement();
      SimpleSelect _select = _statement_1.getSelect();
      this.selectParser.parse(((SimpleSelect) _select));
      OperatorCache _operatorCache_1 = this.cacheService.getOperatorCache();
      String _last = _operatorCache_1.last();
      lastOperator = _last;
    } else {
      String _inputname = query.getInputname();
      lastOperator = _inputname;
    }
    boolean _notEquals = (!Objects.equal(sink, ""));
    if (_notEquals) {
      String _replace = sink.replace("--INPUT--", lastOperator);
      sink = _replace;
      OperatorCache _operatorCache_2 = this.cacheService.getOperatorCache();
      boolean _isBACKUPState = _operatorCache_2.isBACKUPState();
      if (_isBACKUPState) {
        OperatorCache _operatorCache_3 = this.cacheService.getOperatorCache();
        _operatorCache_3.changeToBACKUP();
      }
      String _name_4 = query.getName();
      sinks.remove(_name_4);
      OperatorCache _operatorCache_4 = this.cacheService.getOperatorCache();
      String _name_5 = query.getName();
      _operatorCache_4.add(sink, _name_5);
    } else {
      OperatorCache _operatorCache_5 = this.cacheService.getOperatorCache();
      Map<String, String> _streamTo = _operatorCache_5.getStreamTo();
      String _name_6 = query.getName();
      String _name_7 = query.getName();
      _streamTo.put(_name_6, _name_7);
      OperatorCache _operatorCache_6 = this.cacheService.getOperatorCache();
      _operatorCache_6.changeToBACKUP();
    }
  }
  
  private CharSequence buildCreate1(final String type, final AccessFramework pars, final SchemaDefinition schema, final String name) {
    Class<?> t = null;
    String input = "--INPUT--";
    boolean _equals = type.equals("ACCESS");
    if (_equals) {
      t = AccessAO.class;
    } else {
      t = SenderAO.class;
    }
    OperatorCache _operatorCache = this.cacheService.getOperatorCache();
    Map<String, String> streamTo = _operatorCache.getStreamTo();
    Set<String> _keySet = streamTo.keySet();
    boolean _contains = _keySet.contains(name);
    if (_contains) {
      String _get = streamTo.get(name);
      input = _get;
    }
    Map<String, String> argss = CollectionLiterals.<String, String>newHashMap();
    boolean _equals_1 = t.equals(AccessAO.class);
    if (_equals_1) {
      argss.put("source", name);
    } else {
      argss.put("sink", name);
    }
    String _wrapper = pars.getWrapper();
    argss.put("wrapper", _wrapper);
    String _protocol = pars.getProtocol();
    argss.put("protocol", _protocol);
    String _transport = pars.getTransport();
    argss.put("transport", _transport);
    String _datahandler = pars.getDatahandler();
    argss.put("datahandler", _datahandler);
    String _xifexpression = null;
    boolean _containsKey = argss.containsKey("source");
    if (_containsKey) {
      CharSequence _extractSchema = this.extractSchema(schema);
      _xifexpression = _extractSchema.toString();
    } else {
      _xifexpression = null;
    }
    argss.put("schema", _xifexpression);
    EList<String> _keys = pars.getKeys();
    EList<String> _values = pars.getValues();
    String _generateKeyValueString = this.utilityService.generateKeyValueString(_keys, _values, ",");
    argss.put("options", _generateKeyValueString);
    String _xifexpression_1 = null;
    boolean _containsKey_1 = argss.containsKey("sink");
    if (_containsKey_1) {
      _xifexpression_1 = input;
    } else {
      _xifexpression_1 = null;
    }
    argss.put("input", _xifexpression_1);
    final StringBuilder b = new StringBuilder();
    Collection<SystemSource> _systemSources = this.cacheService.getSystemSources();
    Stream<SystemSource> _stream = _systemSources.stream();
    final Predicate<SystemSource> _function = (SystemSource e) -> {
      return e.isMeta();
    };
    Stream<SystemSource> _filter = _stream.filter(_function);
    final Consumer<SystemSource> _function_1 = (SystemSource e) -> {
      String _name = e.getName();
      StringBuilder _append = b.append(_name);
      /* (_append + ","); */
    };
    _filter.forEach(_function_1);
    String _string = b.toString();
    String _replaceAll = _string.replaceAll(this.regex, "");
    argss.put("metaattribute", _replaceAll);
    return this.builder.build(t, argss);
  }
}
