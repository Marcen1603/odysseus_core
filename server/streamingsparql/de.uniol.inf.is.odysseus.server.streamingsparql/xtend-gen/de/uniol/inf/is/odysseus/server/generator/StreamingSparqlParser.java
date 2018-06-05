package de.uniol.inf.is.odysseus.server.generator;

import com.google.common.base.Objects;
import com.google.common.collect.Iterators;
import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.server.generator.IStreamingSparqlParser;
import de.uniol.inf.is.odysseus.server.generator.StreamingSparqlTriplePatternMatching;
import de.uniol.inf.is.odysseus.server.generator.TripleToJoin;
import de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate;
import de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation;
import de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause;
import de.uniol.inf.is.odysseus.server.streamingsparql.Filesinkclause;
import de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause;
import de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode;
import de.uniol.inf.is.odysseus.server.streamingsparql.GroupBy;
import de.uniol.inf.is.odysseus.server.streamingsparql.GroupGraphPatternSub;
import de.uniol.inf.is.odysseus.server.streamingsparql.IRI;
import de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause;
import de.uniol.inf.is.odysseus.server.streamingsparql.NamedVariable;
import de.uniol.inf.is.odysseus.server.streamingsparql.Operator;
import de.uniol.inf.is.odysseus.server.streamingsparql.Prefix;
import de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList;
import de.uniol.inf.is.odysseus.server.streamingsparql.SPARQLQuery;
import de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery;
import de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject;
import de.uniol.inf.is.odysseus.server.streamingsparql.UnNamedVariable;
import de.uniol.inf.is.odysseus.server.streamingsparql.Variable;
import de.uniol.inf.is.odysseus.server.streamingsparql.WhereClause;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class StreamingSparqlParser implements IStreamingSparqlParser {
  @Inject
  private ParseHelper<SPARQLQuery> helper;
  
  private SelectQuery qry;
  
  private HashMap<String, String> prefixMap = new HashMap<String, String>();
  
  private HashMap<String, String> streamMap = new HashMap<String, String>();
  
  private LinkedHashMap<String, String> joinMap = new LinkedHashMap<String, String>();
  
  private HashMap<String, String> unionMap = new HashMap<String, String>();
  
  private HashMap<String, StreamingSparqlTriplePatternMatching> triplePatternMatchingMap = new HashMap<String, StreamingSparqlTriplePatternMatching>();
  
  private int cnt = 0;
  
  @Override
  public String parse(final Resource resource) {
    String _xblockexpression = null;
    {
      this.prefixMap.clear();
      this.streamMap.clear();
      this.joinMap.clear();
      this.triplePatternMatchingMap.clear();
      this.unionMap.clear();
      TreeIterator<EObject> _allContents = resource.getAllContents();
      Iterator<SelectQuery> _filter = Iterators.<SelectQuery>filter(_allContents, SelectQuery.class);
      SelectQuery _next = _filter.next();
      this.qry = _next;
      _xblockexpression = this.parse(this.qry);
    }
    return _xblockexpression;
  }
  
  public String parse(final SelectQuery q) {
    String _xblockexpression = null;
    {
      String last = "";
      EList<Prefix> _prefixes = q.getPrefixes();
      for (final Prefix prefix : _prefixes) {
        {
          String _iref = prefix.getIref();
          String _replace = _iref.replace(">", "");
          String pf = _replace.replace("<", "");
          boolean _endsWith = pf.endsWith("/");
          if (_endsWith) {
            int _length = pf.length();
            int _minus = (_length - 1);
            String _substring = pf.substring(0, _minus);
            pf = _substring;
          }
          String _name = prefix.getName();
          this.prefixMap.put(_name, pf);
        }
      }
      EList<DatasetClause> _datasetClauses = q.getDatasetClauses();
      for (final DatasetClause clause : _datasetClauses) {
        String _type = clause.getType();
        boolean _tripleEquals = (_type == null);
        if (_tripleEquals) {
          String _name = clause.getName();
          IRI _dataSet = clause.getDataSet();
          String _value = _dataSet.getValue();
          String _replace = _value.replace(">", "");
          String _replace_1 = _replace.replace("<", "");
          this.streamMap.put(_name, _replace_1);
        } else {
          String _type_1 = clause.getType();
          boolean _equals = _type_1.equals("TIME");
          if (_equals) {
            String _name_1 = clause.getName();
            StringConcatenation _builder = new StringConcatenation();
            String _name_2 = clause.getName();
            _builder.append(_name_2, "");
            _builder.append(" = TIMEWINDOW({SIZE = ");
            {
              String _unit = clause.getUnit();
              boolean _tripleNotEquals = (_unit != null);
              if (_tripleNotEquals) {
                _builder.append("[");
                int _size = clause.getSize();
                _builder.append(_size, "");
                _builder.append(", \'");
                String _unit_1 = clause.getUnit();
                _builder.append(_unit_1, "");
                _builder.append("\']");
              } else {
                int _size_1 = clause.getSize();
                _builder.append(_size_1, "");
              }
            }
            {
              int _advance = clause.getAdvance();
              boolean _tripleNotEquals_1 = (_advance != 0);
              if (_tripleNotEquals_1) {
                _builder.append(", advance = ");
                int _advance_1 = clause.getAdvance();
                _builder.append(_advance_1, "");
              }
            }
            _builder.append("}, ");
            IRI _dataSet_1 = clause.getDataSet();
            String _value_1 = _dataSet_1.getValue();
            String _replace_2 = _value_1.replace(">", "");
            String _replace_3 = _replace_2.replace("<", "");
            _builder.append(_replace_3, "");
            _builder.append(")");
            this.streamMap.put(_name_1, _builder.toString());
          } else {
            String _type_2 = clause.getType();
            boolean _equals_1 = _type_2.equals("ELEMENT");
            if (_equals_1) {
              String _name_3 = clause.getName();
              StringConcatenation _builder_1 = new StringConcatenation();
              String _name_4 = clause.getName();
              _builder_1.append(_name_4, "");
              _builder_1.append(" = ELEMENTWINDOW({SIZE = ");
              int _size_2 = clause.getSize();
              _builder_1.append(_size_2, "");
              _builder_1.append(" ");
              {
                int _advance_2 = clause.getAdvance();
                boolean _tripleNotEquals_2 = (_advance_2 != 0);
                if (_tripleNotEquals_2) {
                  _builder_1.append(", advance = ");
                  int _advance_3 = clause.getAdvance();
                  _builder_1.append(_advance_3, "");
                }
              }
              _builder_1.append("}, ");
              IRI _dataSet_2 = clause.getDataSet();
              String _value_2 = _dataSet_2.getValue();
              String _replace_4 = _value_2.replace(">", "");
              String _replace_5 = _replace_4.replace("<", "");
              _builder_1.append(_replace_5, "");
              _builder_1.append(")");
              this.streamMap.put(_name_3, _builder_1.toString());
            }
          }
        }
      }
      WhereClause _whereClause = q.getWhereClause();
      EList<InnerWhereClause> _whereclauses = _whereClause.getWhereclauses();
      for (final InnerWhereClause whereClause : _whereclauses) {
        GroupGraphPatternSub _groupGraphPattern = whereClause.getGroupGraphPattern();
        EList<TriplesSameSubject> _graphPatterns = _groupGraphPattern.getGraphPatterns();
        for (final TriplesSameSubject triple : _graphPatterns) {
          EList<PropertyList> _propertyList = triple.getPropertyList();
          for (final PropertyList propob : _propertyList) {
            {
              DatasetClause _name_5 = whereClause.getName();
              String _name_6 = _name_5.getName();
              String _get = this.streamMap.get(_name_6);
              boolean _contains = _get.contains("=");
              if (_contains) {
                String _xifexpression = null;
                GraphNode _object = propob.getObject();
                Variable _variable = _object.getVariable();
                boolean _tripleEquals_1 = (_variable == null);
                if (_tripleEquals_1) {
                  GraphNode _object_1 = propob.getObject();
                  _xifexpression = _object_1.getLiteral();
                } else {
                  GraphNode _object_2 = propob.getObject();
                  Variable _variable_1 = _object_2.getVariable();
                  UnNamedVariable _unnamed = _variable_1.getUnnamed();
                  String _name_7 = _unnamed.getName();
                  _xifexpression = ("?" + _name_7);
                }
                String object = _xifexpression;
                GraphNode _subject = triple.getSubject();
                Variable _variable_2 = _subject.getVariable();
                UnNamedVariable _unnamed_1 = _variable_2.getUnnamed();
                String _name_8 = _unnamed_1.getName();
                String _plus = ("?" + _name_8);
                GraphNode _property = propob.getProperty();
                Variable _variable_3 = _property.getVariable();
                NamedVariable _property_1 = _variable_3.getProperty();
                Prefix _prefix = _property_1.getPrefix();
                String _name_9 = _prefix.getName();
                String _get_1 = this.prefixMap.get(_name_9);
                String _plus_1 = (_get_1 + "/");
                GraphNode _property_2 = propob.getProperty();
                Variable _variable_4 = _property_2.getVariable();
                NamedVariable _property_3 = _variable_4.getProperty();
                String _name_10 = _property_3.getName();
                String _plus_2 = (_plus_1 + _name_10);
                DatasetClause _name_11 = whereClause.getName();
                String _name_12 = _name_11.getName();
                final StreamingSparqlTriplePatternMatching triplePattern = new StreamingSparqlTriplePatternMatching(_plus, _plus_2, object, _name_12, ("pattern" + Integer.valueOf(this.cnt)));
                last = ("pattern" + Integer.valueOf(this.cnt));
                this.triplePatternMatchingMap.put(last, triplePattern);
              } else {
                String _xifexpression_1 = null;
                GraphNode _object_3 = propob.getObject();
                Variable _variable_5 = _object_3.getVariable();
                boolean _tripleEquals_2 = (_variable_5 == null);
                if (_tripleEquals_2) {
                  GraphNode _object_4 = propob.getObject();
                  _xifexpression_1 = _object_4.getLiteral();
                } else {
                  GraphNode _object_5 = propob.getObject();
                  Variable _variable_6 = _object_5.getVariable();
                  UnNamedVariable _unnamed_2 = _variable_6.getUnnamed();
                  String _name_13 = _unnamed_2.getName();
                  _xifexpression_1 = ("?" + _name_13);
                }
                String object_1 = _xifexpression_1;
                GraphNode _subject_1 = triple.getSubject();
                Variable _variable_7 = _subject_1.getVariable();
                UnNamedVariable _unnamed_3 = _variable_7.getUnnamed();
                String _name_14 = _unnamed_3.getName();
                String _plus_3 = ("?" + _name_14);
                GraphNode _property_4 = propob.getProperty();
                Variable _variable_8 = _property_4.getVariable();
                NamedVariable _property_5 = _variable_8.getProperty();
                Prefix _prefix_1 = _property_5.getPrefix();
                String _name_15 = _prefix_1.getName();
                String _get_2 = this.prefixMap.get(_name_15);
                String _plus_4 = (_get_2 + "/");
                GraphNode _property_6 = propob.getProperty();
                Variable _variable_9 = _property_6.getVariable();
                NamedVariable _property_7 = _variable_9.getProperty();
                String _name_16 = _property_7.getName();
                String _plus_5 = (_plus_4 + _name_16);
                DatasetClause _name_17 = whereClause.getName();
                String _name_18 = _name_17.getName();
                String _get_3 = this.streamMap.get(_name_18);
                final StreamingSparqlTriplePatternMatching triplePattern_1 = new StreamingSparqlTriplePatternMatching(_plus_3, _plus_5, object_1, _get_3, ("pattern" + Integer.valueOf(this.cnt)));
                last = ("pattern" + Integer.valueOf(this.cnt));
                this.triplePatternMatchingMap.put(last, triplePattern_1);
              }
              this.cnt++;
            }
          }
        }
      }
      final ArrayList<String> allVariables = new ArrayList<String>();
      Set<Map.Entry<String, StreamingSparqlTriplePatternMatching>> _entrySet = this.triplePatternMatchingMap.entrySet();
      for (final Map.Entry<String, StreamingSparqlTriplePatternMatching> pattern : _entrySet) {
        {
          StreamingSparqlTriplePatternMatching _value_3 = pattern.getValue();
          String _subject = _value_3.getSubject();
          allVariables.add(_subject);
          StreamingSparqlTriplePatternMatching _value_4 = pattern.getValue();
          String _object = _value_4.getObject();
          allVariables.add(_object);
        }
      }
      LinkedHashSet<String> _findDuplicates = this.findDuplicates(allVariables);
      final List<String> duplicates = IterableExtensions.<String>toList(_findDuplicates);
      final LinkedHashMap<String, List<StreamingSparqlTriplePatternMatching>> varToPattern = new LinkedHashMap<String, List<StreamingSparqlTriplePatternMatching>>();
      for (final String variable : duplicates) {
        Collection<StreamingSparqlTriplePatternMatching> _values = this.triplePatternMatchingMap.values();
        final Function1<StreamingSparqlTriplePatternMatching, Boolean> _function = (StreamingSparqlTriplePatternMatching p) -> {
          return Boolean.valueOf((p.getSubject().equals(variable) || p.getObject().equals(variable)));
        };
        Iterable<StreamingSparqlTriplePatternMatching> _filter = IterableExtensions.<StreamingSparqlTriplePatternMatching>filter(_values, _function);
        List<StreamingSparqlTriplePatternMatching> _list = IterableExtensions.<StreamingSparqlTriplePatternMatching>toList(_filter);
        varToPattern.put(variable, _list);
      }
      ArrayList<TripleToJoin> listToJoin = new ArrayList<TripleToJoin>();
      boolean _isEmpty = listToJoin.isEmpty();
      if (_isEmpty) {
        final String v = duplicates.get(0);
        List<StreamingSparqlTriplePatternMatching> _get = varToPattern.get(v);
        int _size_3 = _get.size();
        int _minus = (_size_3 - 1);
        ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _minus, true);
        for (final Integer i : _doubleDotLessThan) {
          List<StreamingSparqlTriplePatternMatching> _get_1 = varToPattern.get(v);
          StreamingSparqlTriplePatternMatching _get_2 = _get_1.get((i).intValue());
          List<StreamingSparqlTriplePatternMatching> _get_3 = varToPattern.get(v);
          StreamingSparqlTriplePatternMatching _get_4 = _get_3.get(((i).intValue() + 1));
          String _replace_6 = v.replace("?", "");
          TripleToJoin _tripleToJoin = new TripleToJoin(_get_2, _get_4, _replace_6);
          listToJoin.add(_tripleToJoin);
        }
        duplicates.remove(v);
      }
      Iterator<String> duplicatesIterator = duplicates.iterator();
      while ((!IteratorExtensions.isEmpty(duplicatesIterator))) {
        {
          final ArrayList<TripleToJoin> toAdd = this.listToJoinIteration(duplicatesIterator, varToPattern, listToJoin);
          if (((toAdd != null) && (!toAdd.isEmpty()))) {
            listToJoin.addAll(toAdd);
          }
          boolean _hasNext = duplicatesIterator.hasNext();
          boolean _not = (!_hasNext);
          if (_not) {
            Iterator<String> _iterator = duplicates.iterator();
            duplicatesIterator = _iterator;
          }
        }
      }
      final ArrayList<TripleToJoin> listOfMultiplePredicates = new ArrayList<TripleToJoin>();
      final ArrayList<TripleToJoin> newlist = new ArrayList<TripleToJoin>();
      final ArrayList<TripleToJoin> listwithmorethanonepattern2 = new ArrayList<TripleToJoin>();
      for (final TripleToJoin element : listToJoin) {
        {
          final Function1<TripleToJoin, Boolean> _function_1 = (TripleToJoin p) -> {
            StreamingSparqlTriplePatternMatching _pattern2 = p.getPattern2();
            StreamingSparqlTriplePatternMatching _pattern2_1 = element.getPattern2();
            return Boolean.valueOf(Objects.equal(_pattern2, _pattern2_1));
          };
          Iterable<TripleToJoin> _filter_1 = IterableExtensions.<TripleToJoin>filter(listToJoin, _function_1);
          final List<TripleToJoin> j = IterableExtensions.<TripleToJoin>toList(_filter_1);
          int _size_4 = j.size();
          boolean _equals_2 = (_size_4 == 1);
          if (_equals_2) {
            newlist.addAll(j);
          } else {
            if (((j.size() > 1) && (!listwithmorethanonepattern2.containsAll(j)))) {
              listwithmorethanonepattern2.addAll(j);
            }
          }
        }
      }
      newlist.addAll(listwithmorethanonepattern2);
      listToJoin = newlist;
      for (final TripleToJoin element_1 : listToJoin) {
        {
          final Function1<TripleToJoin, Boolean> _function_1 = (TripleToJoin p) -> {
            StreamingSparqlTriplePatternMatching _pattern2 = p.getPattern2();
            StreamingSparqlTriplePatternMatching _pattern2_1 = element_1.getPattern2();
            return Boolean.valueOf(Objects.equal(_pattern2, _pattern2_1));
          };
          Iterable<TripleToJoin> _filter_1 = IterableExtensions.<TripleToJoin>filter(listToJoin, _function_1);
          final List<TripleToJoin> j = IterableExtensions.<TripleToJoin>toList(_filter_1);
          boolean _isEmpty_1 = this.joinMap.isEmpty();
          if (_isEmpty_1) {
            StreamingSparqlTriplePatternMatching _pattern1 = element_1.getPattern1();
            String _id = _pattern1.getId();
            String _plus = ((("join" + Integer.valueOf(this.cnt)) + " = join({predicate = \'") + _id);
            String _plus_1 = (_plus + ".");
            String _variable = element_1.getVariable();
            String _plus_2 = (_plus_1 + _variable);
            String _plus_3 = (_plus_2 + 
              " = ");
            StreamingSparqlTriplePatternMatching _pattern2 = element_1.getPattern2();
            String _id_1 = _pattern2.getId();
            String _plus_4 = (_plus_3 + _id_1);
            String _plus_5 = (_plus_4 + ".");
            String _variable_1 = element_1.getVariable();
            String _plus_6 = (_plus_5 + _variable_1);
            String _plus_7 = (_plus_6 + "\'}, ");
            StreamingSparqlTriplePatternMatching _pattern1_1 = element_1.getPattern1();
            String _id_2 = _pattern1_1.getId();
            String _plus_8 = (_plus_7 + _id_2);
            String _plus_9 = (_plus_8 + ", ");
            StreamingSparqlTriplePatternMatching _pattern2_1 = element_1.getPattern2();
            String _id_3 = _pattern2_1.getId();
            String _plus_10 = (_plus_9 + _id_3);
            String _plus_11 = (_plus_10 + ")");
            this.joinMap.put(("join" + Integer.valueOf(this.cnt)), _plus_11);
            last = ("join" + Integer.valueOf(this.cnt));
            this.cnt++;
          } else {
            boolean _containsAll = listOfMultiplePredicates.containsAll(j);
            boolean _not = (!_containsAll);
            if (_not) {
              StringConcatenation _builder_2 = new StringConcatenation();
              _builder_2.append("join");
              _builder_2.append(this.cnt, "");
              _builder_2.append(" = join({predicate = \'");
              {
                boolean _hasElements = false;
                for(final TripleToJoin elem : j) {
                  if (!_hasElements) {
                    _hasElements = true;
                  } else {
                    _builder_2.appendImmediate(" && ", "");
                  }
                  StreamingSparqlTriplePatternMatching _pattern1_2 = elem.getPattern1();
                  String _id_4 = _pattern1_2.getId();
                  _builder_2.append(_id_4, "");
                  _builder_2.append(".");
                  String _variable_2 = elem.getVariable();
                  _builder_2.append(_variable_2, "");
                  _builder_2.append(" = ");
                  StreamingSparqlTriplePatternMatching _pattern2_2 = elem.getPattern2();
                  String _id_5 = _pattern2_2.getId();
                  _builder_2.append(_id_5, "");
                  _builder_2.append(".");
                  String _variable_3 = elem.getVariable();
                  _builder_2.append(_variable_3, "");
                }
              }
              _builder_2.append("\'}, ");
              StreamingSparqlTriplePatternMatching _pattern2_3 = element_1.getPattern2();
              String _id_6 = _pattern2_3.getId();
              _builder_2.append(_id_6, "");
              _builder_2.append(", join");
              _builder_2.append((this.cnt - 1), "");
              _builder_2.append(")");
              String qry = _builder_2.toString();
              this.joinMap.put(("join" + Integer.valueOf(this.cnt)), qry);
              last = ("join" + Integer.valueOf(this.cnt));
              this.cnt++;
            }
          }
          int _size_4 = j.size();
          boolean _greaterThan = (_size_4 > 1);
          if (_greaterThan) {
            listOfMultiplePredicates.addAll(j);
          }
        }
      }
      String filter = "";
      Filterclause _filterclause = q.getFilterclause();
      boolean _tripleNotEquals_3 = (_filterclause != null);
      if (_tripleNotEquals_3) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("filter=SELECT({predicate=\'");
        Filterclause _filterclause_1 = q.getFilterclause();
        Variable _left = _filterclause_1.getLeft();
        UnNamedVariable _unnamed = _left.getUnnamed();
        String _name_5 = _unnamed.getName();
        _builder_2.append(_name_5, "");
        _builder_2.append(" ");
        Filterclause _filterclause_2 = q.getFilterclause();
        Operator _operator = _filterclause_2.getOperator();
        _builder_2.append(_operator, "");
        _builder_2.append(" ");
        Filterclause _filterclause_3 = q.getFilterclause();
        Variable _right = _filterclause_3.getRight();
        UnNamedVariable _unnamed_1 = _right.getUnnamed();
        String _name_6 = _unnamed_1.getName();
        _builder_2.append(_name_6, "");
        _builder_2.append("\'},");
        _builder_2.append(last, "");
        _builder_2.append(")");
        filter = _builder_2.toString();
        last = "filter";
      }
      String aggregate = "";
      Aggregate _aggregateClause = q.getAggregateClause();
      boolean _tripleNotEquals_4 = (_aggregateClause != null);
      if (_tripleNotEquals_4) {
        final Aggregate aggregateClause = q.getAggregateClause();
        String name = "aggregate";
        String aggregations = "";
        String groupBy = "";
        EList<Aggregation> _aggregations = aggregateClause.getAggregations();
        boolean _tripleNotEquals_5 = (_aggregations != null);
        if (_tripleNotEquals_5) {
          StringConcatenation _builder_3 = new StringConcatenation();
          _builder_3.append("AGGREGATIONS=[");
          {
            EList<Aggregation> _aggregations_1 = aggregateClause.getAggregations();
            boolean _hasElements = false;
            for(final Aggregation clause_1 : _aggregations_1) {
              if (!_hasElements) {
                _hasElements = true;
              } else {
                _builder_3.appendImmediate(",", "");
              }
              _builder_3.append("[\'");
              String _function_1 = clause_1.getFunction();
              _builder_3.append(_function_1, "");
              _builder_3.append("\',\'");
              Variable _varToAgg = clause_1.getVarToAgg();
              UnNamedVariable _unnamed_2 = _varToAgg.getUnnamed();
              String _name_7 = _unnamed_2.getName();
              _builder_3.append(_name_7, "");
              _builder_3.append("\',\'");
              String _aggName = clause_1.getAggName();
              _builder_3.append(_aggName, "");
              _builder_3.append("\'");
              {
                String _datatype = clause_1.getDatatype();
                boolean _tripleNotEquals_6 = (_datatype != null);
                if (_tripleNotEquals_6) {
                  _builder_3.append(",\'");
                  String _datatype_1 = clause_1.getDatatype();
                  _builder_3.append(_datatype_1, "");
                  _builder_3.append("\'");
                }
              }
              _builder_3.append("]");
            }
          }
          _builder_3.append("]");
          aggregations = _builder_3.toString();
        }
        GroupBy _groupby = aggregateClause.getGroupby();
        boolean _tripleNotEquals_7 = (_groupby != null);
        if (_tripleNotEquals_7) {
          StringConcatenation _builder_4 = new StringConcatenation();
          _builder_4.append("group_by = [");
          {
            GroupBy _groupby_1 = aggregateClause.getGroupby();
            EList<Variable> _variables = _groupby_1.getVariables();
            boolean _hasElements_1 = false;
            for(final Variable v_1 : _variables) {
              if (!_hasElements_1) {
                _hasElements_1 = true;
              } else {
                _builder_4.appendImmediate(",", "");
              }
              _builder_4.append("\'");
              UnNamedVariable _unnamed_3 = v_1.getUnnamed();
              String _name_8 = _unnamed_3.getName();
              _builder_4.append(_name_8, "");
              _builder_4.append("\'");
            }
          }
          _builder_4.append("]");
          groupBy = _builder_4.toString();
        }
        StringConcatenation _builder_5 = new StringConcatenation();
        _builder_5.append(name, "");
        _builder_5.append(" = AGGREGATE({");
        {
          boolean _equals_2 = aggregations.equals("");
          boolean _not = (!_equals_2);
          if (_not) {
            _builder_5.append(" ");
            _builder_5.append(aggregations, "");
          }
        }
        {
          boolean _equals_3 = groupBy.equals("");
          boolean _not_1 = (!_equals_3);
          if (_not_1) {
            _builder_5.append(", ");
            _builder_5.append(groupBy, "");
          }
        }
        _builder_5.append("}, ");
        _builder_5.append(last, "");
        _builder_5.append(")");
        aggregate = _builder_5.toString();
        last = name;
      }
      StringConcatenation _builder_6 = new StringConcatenation();
      _builder_6.append("result = project({attributes = [");
      {
        EList<Variable> _variables_1 = q.getVariables();
        boolean _hasElements_2 = false;
        for(final Variable variable_1 : _variables_1) {
          if (!_hasElements_2) {
            _hasElements_2 = true;
          } else {
            _builder_6.appendImmediate(",", "");
          }
          _builder_6.append("\'");
          UnNamedVariable _unnamed_4 = variable_1.getUnnamed();
          String _name_9 = _unnamed_4.getName();
          _builder_6.append(_name_9, "");
          _builder_6.append("\'");
        }
      }
      _builder_6.append("]},");
      _builder_6.append(last, "");
      _builder_6.append(")");
      _builder_6.newLineIfNotEmpty();
      String projection = _builder_6.toString();
      final StringBuilder query = new StringBuilder();
      query.append("#PARSER PQL\n");
      String _method = q.getMethod();
      boolean _tripleNotEquals_8 = (_method != null);
      if (_tripleNotEquals_8) {
        String _method_1 = q.getMethod();
        String _plus = (_method_1 + "\n");
        query.append(_plus);
      } else {
        query.append("#ADDQUERY\n");
      }
      Set<Map.Entry<String, String>> _entrySet_1 = this.streamMap.entrySet();
      for (final Map.Entry<String, String> win : _entrySet_1) {
        String _value_3 = win.getValue();
        boolean _contains = _value_3.contains("=");
        if (_contains) {
          String _value_4 = win.getValue();
          String _plus_1 = (_value_4 + "\n");
          query.append(_plus_1);
        }
      }
      Set<Map.Entry<String, StreamingSparqlTriplePatternMatching>> _entrySet_2 = this.triplePatternMatchingMap.entrySet();
      for (final Map.Entry<String, StreamingSparqlTriplePatternMatching> triple_1 : _entrySet_2) {
        StreamingSparqlTriplePatternMatching _value_5 = triple_1.getValue();
        String _stmt = _value_5.getStmt();
        String _plus_2 = (_stmt + "\n");
        query.append(_plus_2);
      }
      Set<Map.Entry<String, String>> _entrySet_3 = this.joinMap.entrySet();
      for (final Map.Entry<String, String> triple_2 : _entrySet_3) {
        String _value_6 = triple_2.getValue();
        String _plus_3 = (_value_6 + "\n");
        query.append(_plus_3);
      }
      Set<Map.Entry<String, String>> _entrySet_4 = this.unionMap.entrySet();
      for (final Map.Entry<String, String> triple_3 : _entrySet_4) {
        String _value_7 = triple_3.getValue();
        String _plus_4 = (_value_7 + "\n");
        query.append(_plus_4);
      }
      Filterclause _filterclause_4 = q.getFilterclause();
      boolean _tripleNotEquals_9 = (_filterclause_4 != null);
      if (_tripleNotEquals_9) {
        query.append((filter + "\n"));
      }
      Aggregate _aggregateClause_1 = q.getAggregateClause();
      boolean _tripleNotEquals_10 = (_aggregateClause_1 != null);
      if (_tripleNotEquals_10) {
        query.append((aggregate + "\n"));
      }
      query.append(projection);
      Filesinkclause _filesinkclause = q.getFilesinkclause();
      boolean _tripleNotEquals_11 = (_filesinkclause != null);
      if (_tripleNotEquals_11) {
        Filesinkclause _filesinkclause_1 = q.getFilesinkclause();
        String _path = _filesinkclause_1.getPath();
        String _replace_7 = _path.replace("/", "\\");
        String _plus_5 = ("res = CSVFILESINK({SINK = \'sink\', FILENAME = \'" + _replace_7);
        String _plus_6 = (_plus_5 + "\'}, result)");
        query.append(_plus_6);
      }
      InputOutput.<StringBuilder>print(query);
      _xblockexpression = query.toString();
    }
    return _xblockexpression;
  }
  
  public LinkedHashSet<String> findDuplicates(final Collection<String> list) {
    final LinkedHashSet<String> duplicates = new LinkedHashSet<String>();
    final HashSet<String> uniques = new HashSet<String>();
    for (final String t : list) {
      boolean _add = uniques.add(t);
      boolean _not = (!_add);
      if (_not) {
        duplicates.add(t);
      }
    }
    return duplicates;
  }
  
  public ArrayList<TripleToJoin> listToJoinIteration(final Iterator<String> duplicatesIterator, final LinkedHashMap<String, List<StreamingSparqlTriplePatternMatching>> varToPattern, final ArrayList<TripleToJoin> listToJoin) {
    final Iterator<TripleToJoin> listToJoinIterator = listToJoin.iterator();
    final ArrayList<TripleToJoin> listToAdd = new ArrayList<TripleToJoin>();
    final String variable = duplicatesIterator.next();
    final List<StreamingSparqlTriplePatternMatching> patterns = varToPattern.get(variable);
    while (listToJoinIterator.hasNext()) {
      {
        final TripleToJoin joinElement = listToJoinIterator.next();
        final Function1<StreamingSparqlTriplePatternMatching, Boolean> _function = (StreamingSparqlTriplePatternMatching p) -> {
          return Boolean.valueOf((Objects.equal(p, joinElement.getPattern1()) || Objects.equal(p, joinElement.getPattern2())));
        };
        Iterable<StreamingSparqlTriplePatternMatching> _filter = IterableExtensions.<StreamingSparqlTriplePatternMatching>filter(patterns, _function);
        List<StreamingSparqlTriplePatternMatching> filteredList = IterableExtensions.<StreamingSparqlTriplePatternMatching>toList(_filter);
        int _size = filteredList.size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          StreamingSparqlTriplePatternMatching _get = filteredList.get(0);
          patterns.remove(_get);
          StreamingSparqlTriplePatternMatching _get_1 = filteredList.get(0);
          StreamingSparqlTriplePatternMatching _get_2 = patterns.get(0);
          String _replace = variable.replace("?", "");
          TripleToJoin _tripleToJoin = new TripleToJoin(_get_1, _get_2, _replace);
          listToAdd.add(_tripleToJoin);
          int _size_1 = patterns.size();
          int _minus = (_size_1 - 1);
          ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _minus, true);
          for (final Integer k : _doubleDotLessThan) {
            StreamingSparqlTriplePatternMatching _get_3 = patterns.get((k).intValue());
            StreamingSparqlTriplePatternMatching _get_4 = patterns.get(((k).intValue() + 1));
            String _replace_1 = variable.replace("?", "");
            TripleToJoin _tripleToJoin_1 = new TripleToJoin(_get_3, _get_4, _replace_1);
            listToAdd.add(_tripleToJoin_1);
          }
          duplicatesIterator.remove();
          return listToAdd;
        }
      }
    }
    return null;
  }
  
  @Override
  public String parse(final String query, final ISession user, final IDataDictionary dd, final Context context, final IMetaAttribute metaAttribute, final IServerExecutor executor) {
    try {
      String _xblockexpression = null;
      {
        final SPARQLQuery q = this.helper.parse(query);
        String _xifexpression = null;
        if ((q instanceof SelectQuery)) {
          _xifexpression = this.parse(((SelectQuery)q));
        }
        _xblockexpression = _xifexpression;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
