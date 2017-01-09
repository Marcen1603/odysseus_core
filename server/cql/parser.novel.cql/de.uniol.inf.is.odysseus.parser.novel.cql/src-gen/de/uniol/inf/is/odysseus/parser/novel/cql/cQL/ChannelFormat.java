/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.cQL;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Channel Format</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ChannelFormat#getStream <em>Stream</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ChannelFormat#getView <em>View</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getChannelFormat()
 * @model
 * @generated
 */
public interface ChannelFormat extends EObject
{
  /**
   * Returns the value of the '<em><b>Stream</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Stream</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Stream</em>' containment reference.
   * @see #setStream(ChannelFormatStream)
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getChannelFormat_Stream()
   * @model containment="true"
   * @generated
   */
  ChannelFormatStream getStream();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ChannelFormat#getStream <em>Stream</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Stream</em>' containment reference.
   * @see #getStream()
   * @generated
   */
  void setStream(ChannelFormatStream value);

  /**
   * Returns the value of the '<em><b>View</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>View</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>View</em>' containment reference.
   * @see #setView(ChannelFormatView)
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage#getChannelFormat_View()
   * @model containment="true"
   * @generated
   */
  ChannelFormatView getView();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ChannelFormat#getView <em>View</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>View</em>' containment reference.
   * @see #getView()
   * @generated
   */
  void setView(ChannelFormatView value);

} // ChannelFormat
