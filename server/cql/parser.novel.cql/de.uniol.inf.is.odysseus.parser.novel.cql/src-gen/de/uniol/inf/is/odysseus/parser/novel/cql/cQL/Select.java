/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.cQL;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Select</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getName <em>Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getDistinct <em>Distinct</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getAggregations <em>Aggregations</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getFunctions <em>Functions</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getExpressions <em>Expressions</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getSources <em>Sources</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getPredicates <em>Predicates</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getOrder <em>Order</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getHaving <em>Having</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getSelect()
 * @model
 * @generated
 */
public interface Select extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getSelect_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Distinct</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Distinct</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Distinct</em>' attribute.
   * @see #setDistinct(String)
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getSelect_Distinct()
   * @model
   * @generated
   */
  String getDistinct();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getDistinct <em>Distinct</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Distinct</em>' attribute.
   * @see #getDistinct()
   * @generated
   */
  void setDistinct(String value);

  /**
   * Returns the value of the '<em><b>Attributes</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Attributes</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Attributes</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getSelect_Attributes()
   * @model containment="true"
   * @generated
   */
  EList<Attribute> getAttributes();

  /**
   * Returns the value of the '<em><b>Aggregations</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Aggregation}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Aggregations</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Aggregations</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getSelect_Aggregations()
   * @model containment="true"
   * @generated
   */
  EList<Aggregation> getAggregations();

  /**
   * Returns the value of the '<em><b>Functions</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Function}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Functions</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Functions</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getSelect_Functions()
   * @model containment="true"
   * @generated
   */
  EList<Function> getFunctions();

  /**
   * Returns the value of the '<em><b>Expressions</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.FunctionExpression}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Expressions</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expressions</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getSelect_Expressions()
   * @model containment="true"
   * @generated
   */
  EList<FunctionExpression> getExpressions();

  /**
   * Returns the value of the '<em><b>Sources</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Source}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sources</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sources</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getSelect_Sources()
   * @model containment="true"
   * @generated
   */
  EList<Source> getSources();

  /**
   * Returns the value of the '<em><b>Predicates</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Predicates</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Predicates</em>' containment reference.
   * @see #setPredicates(ExpressionsModel)
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getSelect_Predicates()
   * @model containment="true"
   * @generated
   */
  ExpressionsModel getPredicates();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getPredicates <em>Predicates</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Predicates</em>' containment reference.
   * @see #getPredicates()
   * @generated
   */
  void setPredicates(ExpressionsModel value);

  /**
   * Returns the value of the '<em><b>Order</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Order</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Order</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getSelect_Order()
   * @model containment="true"
   * @generated
   */
  EList<Attribute> getOrder();

  /**
   * Returns the value of the '<em><b>Having</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Having</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Having</em>' containment reference.
   * @see #setHaving(ExpressionsModel)
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getSelect_Having()
   * @model containment="true"
   * @generated
   */
  ExpressionsModel getHaving();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select#getHaving <em>Having</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Having</em>' containment reference.
   * @see #getHaving()
   * @generated
   */
  void setHaving(ExpressionsModel value);

} // Select
