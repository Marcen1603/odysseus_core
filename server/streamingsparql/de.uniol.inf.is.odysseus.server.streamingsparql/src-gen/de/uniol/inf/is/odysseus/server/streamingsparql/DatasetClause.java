/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dataset Clause</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getDataSet <em>Data Set</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getName <em>Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getType <em>Type</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getSize <em>Size</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getAdvance <em>Advance</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getUnit <em>Unit</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getDatasetClause()
 * @model
 * @generated
 */
public interface DatasetClause extends EObject
{
  /**
   * Returns the value of the '<em><b>Data Set</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Data Set</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Data Set</em>' containment reference.
   * @see #setDataSet(IRI)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getDatasetClause_DataSet()
   * @model containment="true"
   * @generated
   */
  IRI getDataSet();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getDataSet <em>Data Set</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Data Set</em>' containment reference.
   * @see #getDataSet()
   * @generated
   */
  void setDataSet(IRI value);

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
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getDatasetClause_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see #setType(String)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getDatasetClause_Type()
   * @model
   * @generated
   */
  String getType();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see #getType()
   * @generated
   */
  void setType(String value);

  /**
   * Returns the value of the '<em><b>Size</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Size</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Size</em>' attribute.
   * @see #setSize(int)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getDatasetClause_Size()
   * @model
   * @generated
   */
  int getSize();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getSize <em>Size</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Size</em>' attribute.
   * @see #getSize()
   * @generated
   */
  void setSize(int value);

  /**
   * Returns the value of the '<em><b>Advance</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Advance</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Advance</em>' attribute.
   * @see #setAdvance(int)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getDatasetClause_Advance()
   * @model
   * @generated
   */
  int getAdvance();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getAdvance <em>Advance</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Advance</em>' attribute.
   * @see #getAdvance()
   * @generated
   */
  void setAdvance(int value);

  /**
   * Returns the value of the '<em><b>Unit</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Unit</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Unit</em>' attribute.
   * @see #setUnit(String)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getDatasetClause_Unit()
   * @model
   * @generated
   */
  String getUnit();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getUnit <em>Unit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Unit</em>' attribute.
   * @see #getUnit()
   * @generated
   */
  void setUnit(String value);

} // DatasetClause
