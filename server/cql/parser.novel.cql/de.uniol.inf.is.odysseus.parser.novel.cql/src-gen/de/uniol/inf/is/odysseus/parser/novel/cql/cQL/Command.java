/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.cQL;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Command</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Command#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getCommand()
 * @model
 * @generated
 */
public interface Command extends EObject
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
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getCommand_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Command#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

} // Command
