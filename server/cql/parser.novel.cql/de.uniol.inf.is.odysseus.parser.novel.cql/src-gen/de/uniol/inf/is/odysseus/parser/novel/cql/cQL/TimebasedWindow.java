/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.cQL;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Timebased Window</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.TimebasedWindow#getSize <em>Size</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.TimebasedWindow#getUnit <em>Unit</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.TimebasedWindow#getAdvance_size <em>Advance size</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.TimebasedWindow#getAdvance_unit <em>Advance unit</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getTimebasedWindow()
 * @model
 * @generated
 */
public interface TimebasedWindow extends WindowOperator
{
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
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getTimebasedWindow_Size()
   * @model
   * @generated
   */
  int getSize();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.TimebasedWindow#getSize <em>Size</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Size</em>' attribute.
   * @see #getSize()
   * @generated
   */
  void setSize(int value);

  /**
   * Returns the value of the '<em><b>Unit</b></em>' attribute.
   * The literals are from the enumeration {@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Time}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Unit</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Unit</em>' attribute.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Time
   * @see #setUnit(Time)
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getTimebasedWindow_Unit()
   * @model
   * @generated
   */
  Time getUnit();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.TimebasedWindow#getUnit <em>Unit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Unit</em>' attribute.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Time
   * @see #getUnit()
   * @generated
   */
  void setUnit(Time value);

  /**
   * Returns the value of the '<em><b>Advance size</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Advance size</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Advance size</em>' attribute.
   * @see #setAdvance_size(int)
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getTimebasedWindow_Advance_size()
   * @model
   * @generated
   */
  int getAdvance_size();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.TimebasedWindow#getAdvance_size <em>Advance size</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Advance size</em>' attribute.
   * @see #getAdvance_size()
   * @generated
   */
  void setAdvance_size(int value);

  /**
   * Returns the value of the '<em><b>Advance unit</b></em>' attribute.
   * The literals are from the enumeration {@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Time}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Advance unit</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Advance unit</em>' attribute.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Time
   * @see #setAdvance_unit(Time)
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getTimebasedWindow_Advance_unit()
   * @model
   * @generated
   */
  Time getAdvance_unit();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.TimebasedWindow#getAdvance_unit <em>Advance unit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Advance unit</em>' attribute.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Time
   * @see #getAdvance_unit()
   * @generated
   */
  void setAdvance_unit(Time value);

} // TimebasedWindow
