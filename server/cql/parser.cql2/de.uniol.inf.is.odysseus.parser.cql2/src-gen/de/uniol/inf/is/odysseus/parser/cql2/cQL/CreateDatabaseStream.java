/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.cQL;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Create Database Stream</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseStream#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseStream#getDatabase <em>Database</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseStream#getTable <em>Table</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseStream#getSize <em>Size</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseStream#getUnit <em>Unit</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getCreateDatabaseStream()
 * @model
 * @generated
 */
public interface CreateDatabaseStream extends EObject
{
  /**
   * Returns the value of the '<em><b>Attributes</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Attributes</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Attributes</em>' containment reference.
   * @see #setAttributes(SchemaDefinition)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getCreateDatabaseStream_Attributes()
   * @model containment="true"
   * @generated
   */
  SchemaDefinition getAttributes();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseStream#getAttributes <em>Attributes</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Attributes</em>' containment reference.
   * @see #getAttributes()
   * @generated
   */
  void setAttributes(SchemaDefinition value);

  /**
   * Returns the value of the '<em><b>Database</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Database</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Database</em>' attribute.
   * @see #setDatabase(String)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getCreateDatabaseStream_Database()
   * @model
   * @generated
   */
  String getDatabase();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseStream#getDatabase <em>Database</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Database</em>' attribute.
   * @see #getDatabase()
   * @generated
   */
  void setDatabase(String value);

  /**
   * Returns the value of the '<em><b>Table</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Table</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Table</em>' attribute.
   * @see #setTable(String)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getCreateDatabaseStream_Table()
   * @model
   * @generated
   */
  String getTable();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseStream#getTable <em>Table</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Table</em>' attribute.
   * @see #getTable()
   * @generated
   */
  void setTable(String value);

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
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getCreateDatabaseStream_Size()
   * @model
   * @generated
   */
  int getSize();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseStream#getSize <em>Size</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Size</em>' attribute.
   * @see #getSize()
   * @generated
   */
  void setSize(int value);

  /**
   * Returns the value of the '<em><b>Unit</b></em>' attribute.
   * The literals are from the enumeration {@link de.uniol.inf.is.odysseus.parser.cql2.cQL.Time}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Unit</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Unit</em>' attribute.
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.Time
   * @see #setUnit(Time)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getCreateDatabaseStream_Unit()
   * @model
   * @generated
   */
  Time getUnit();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseStream#getUnit <em>Unit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Unit</em>' attribute.
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.Time
   * @see #getUnit()
   * @generated
   */
  void setUnit(Time value);

} // CreateDatabaseStream
