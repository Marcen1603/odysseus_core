/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql.util;

import de.uniol.inf.is.odysseus.server.streamingsparql.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage
 * @generated
 */
public class StreamingsparqlSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static StreamingsparqlPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StreamingsparqlSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = StreamingsparqlPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case StreamingsparqlPackage.SPARQL_QUERY:
      {
        SPARQLQuery sparqlQuery = (SPARQLQuery)theEObject;
        T result = caseSPARQLQuery(sparqlQuery);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.PREFIX:
      {
        Prefix prefix = (Prefix)theEObject;
        T result = casePrefix(prefix);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.BASE:
      {
        Base base = (Base)theEObject;
        T result = caseBase(base);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.SELECT_QUERY:
      {
        SelectQuery selectQuery = (SelectQuery)theEObject;
        T result = caseSelectQuery(selectQuery);
        if (result == null) result = caseSPARQLQuery(selectQuery);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.AGGREGATE:
      {
        Aggregate aggregate = (Aggregate)theEObject;
        T result = caseAggregate(aggregate);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.GROUP_BY:
      {
        GroupBy groupBy = (GroupBy)theEObject;
        T result = caseGroupBy(groupBy);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.AGGREGATION:
      {
        Aggregation aggregation = (Aggregation)theEObject;
        T result = caseAggregation(aggregation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.FILESINKCLAUSE:
      {
        Filesinkclause filesinkclause = (Filesinkclause)theEObject;
        T result = caseFilesinkclause(filesinkclause);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.FILTERCLAUSE:
      {
        Filterclause filterclause = (Filterclause)theEObject;
        T result = caseFilterclause(filterclause);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.GROUP_CLAUSE:
      {
        GroupClause groupClause = (GroupClause)theEObject;
        T result = caseGroupClause(groupClause);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.DATASET_CLAUSE:
      {
        DatasetClause datasetClause = (DatasetClause)theEObject;
        T result = caseDatasetClause(datasetClause);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.WHERE_CLAUSE:
      {
        WhereClause whereClause = (WhereClause)theEObject;
        T result = caseWhereClause(whereClause);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.INNER_WHERE_CLAUSE:
      {
        InnerWhereClause innerWhereClause = (InnerWhereClause)theEObject;
        T result = caseInnerWhereClause(innerWhereClause);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.GROUP_GRAPH_PATTERN_SUB:
      {
        GroupGraphPatternSub groupGraphPatternSub = (GroupGraphPatternSub)theEObject;
        T result = caseGroupGraphPatternSub(groupGraphPatternSub);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.TRIPLES_SAME_SUBJECT:
      {
        TriplesSameSubject triplesSameSubject = (TriplesSameSubject)theEObject;
        T result = caseTriplesSameSubject(triplesSameSubject);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.PROPERTY_LIST:
      {
        PropertyList propertyList = (PropertyList)theEObject;
        T result = casePropertyList(propertyList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.GRAPH_NODE:
      {
        GraphNode graphNode = (GraphNode)theEObject;
        T result = caseGraphNode(graphNode);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.VARIABLE:
      {
        Variable variable = (Variable)theEObject;
        T result = caseVariable(variable);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.UN_NAMED_VARIABLE:
      {
        UnNamedVariable unNamedVariable = (UnNamedVariable)theEObject;
        T result = caseUnNamedVariable(unNamedVariable);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.NAMED_VARIABLE:
      {
        NamedVariable namedVariable = (NamedVariable)theEObject;
        T result = caseNamedVariable(namedVariable);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.IRI:
      {
        IRI iri = (IRI)theEObject;
        T result = caseIRI(iri);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.TYPE_TAG:
      {
        TypeTag typeTag = (TypeTag)theEObject;
        T result = caseTypeTag(typeTag);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case StreamingsparqlPackage.LANG_TAG:
      {
        LangTag langTag = (LangTag)theEObject;
        T result = caseLangTag(langTag);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>SPARQL Query</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>SPARQL Query</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSPARQLQuery(SPARQLQuery object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Prefix</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Prefix</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePrefix(Prefix object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Base</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Base</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBase(Base object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Select Query</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Select Query</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSelectQuery(SelectQuery object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Aggregate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Aggregate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAggregate(Aggregate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Group By</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Group By</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGroupBy(GroupBy object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Aggregation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Aggregation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAggregation(Aggregation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Filesinkclause</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Filesinkclause</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFilesinkclause(Filesinkclause object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Filterclause</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Filterclause</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFilterclause(Filterclause object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Group Clause</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Group Clause</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGroupClause(GroupClause object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Dataset Clause</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Dataset Clause</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDatasetClause(DatasetClause object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Where Clause</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Where Clause</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseWhereClause(WhereClause object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Inner Where Clause</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Inner Where Clause</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInnerWhereClause(InnerWhereClause object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Group Graph Pattern Sub</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Group Graph Pattern Sub</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGroupGraphPatternSub(GroupGraphPatternSub object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Triples Same Subject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Triples Same Subject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTriplesSameSubject(TriplesSameSubject object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Property List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Property List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePropertyList(PropertyList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Graph Node</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Graph Node</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGraphNode(GraphNode object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Variable</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Variable</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseVariable(Variable object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Un Named Variable</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Un Named Variable</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseUnNamedVariable(UnNamedVariable object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Named Variable</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Named Variable</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNamedVariable(NamedVariable object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IRI</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IRI</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIRI(IRI object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Type Tag</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Type Tag</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTypeTag(TypeTag object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Lang Tag</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Lang Tag</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLangTag(LangTag object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //StreamingsparqlSwitch
