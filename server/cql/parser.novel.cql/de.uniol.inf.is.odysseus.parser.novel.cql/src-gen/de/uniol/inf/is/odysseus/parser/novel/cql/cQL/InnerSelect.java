/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.cQL;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inner Select</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.InnerSelect#getSelect <em>Select</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getInnerSelect()
 * @model
 * @generated
 */
public interface InnerSelect extends EObject
{
  /**
   * Returns the value of the '<em><b>Select</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Select</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Select</em>' containment reference.
   * @see #setSelect(Select)
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getInnerSelect_Select()
   * @model containment="true"
   * @generated
   */
  Select getSelect();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.InnerSelect#getSelect <em>Select</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Select</em>' containment reference.
   * @see #getSelect()
   * @generated
   */
  void setSelect(Select value);

} // InnerSelect
