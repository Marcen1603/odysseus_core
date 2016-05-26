/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Operator</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getOperator()
 * @model
 * @generated
 */
public enum Operator implements Enumerator
{
  /**
   * The '<em><b>Less Then</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LESS_THEN_VALUE
   * @generated
   * @ordered
   */
  LESS_THEN(0, "lessThen", "<"),

  /**
   * The '<em><b>Greater Then</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #GREATER_THEN_VALUE
   * @generated
   * @ordered
   */
  GREATER_THEN(1, "greaterThen", ">"),

  /**
   * The '<em><b>Less Equal</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LESS_EQUAL_VALUE
   * @generated
   * @ordered
   */
  LESS_EQUAL(2, "lessEqual", "<="),

  /**
   * The '<em><b>Greater Equal</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #GREATER_EQUAL_VALUE
   * @generated
   * @ordered
   */
  GREATER_EQUAL(3, "greaterEqual", ">="),

  /**
   * The '<em><b>Equal</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #EQUAL_VALUE
   * @generated
   * @ordered
   */
  EQUAL(4, "equal", "="),

  /**
   * The '<em><b>Not Equal</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #NOT_EQUAL_VALUE
   * @generated
   * @ordered
   */
  NOT_EQUAL(5, "notEqual", "!="),

  /**
   * The '<em><b>Sum</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SUM_VALUE
   * @generated
   * @ordered
   */
  SUM(6, "sum", "+"),

  /**
   * The '<em><b>Div</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #DIV_VALUE
   * @generated
   * @ordered
   */
  DIV(7, "div", "/"),

  /**
   * The '<em><b>Sub</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SUB_VALUE
   * @generated
   * @ordered
   */
  SUB(8, "sub", "-"),

  /**
   * The '<em><b>Multiplicity</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MULTIPLICITY_VALUE
   * @generated
   * @ordered
   */
  MULTIPLICITY(9, "multiplicity", "*");

  /**
   * The '<em><b>Less Then</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Less Then</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #LESS_THEN
   * @model name="lessThen" literal="<"
   * @generated
   * @ordered
   */
  public static final int LESS_THEN_VALUE = 0;

  /**
   * The '<em><b>Greater Then</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Greater Then</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #GREATER_THEN
   * @model name="greaterThen" literal=">"
   * @generated
   * @ordered
   */
  public static final int GREATER_THEN_VALUE = 1;

  /**
   * The '<em><b>Less Equal</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Less Equal</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #LESS_EQUAL
   * @model name="lessEqual" literal="<="
   * @generated
   * @ordered
   */
  public static final int LESS_EQUAL_VALUE = 2;

  /**
   * The '<em><b>Greater Equal</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Greater Equal</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #GREATER_EQUAL
   * @model name="greaterEqual" literal=">="
   * @generated
   * @ordered
   */
  public static final int GREATER_EQUAL_VALUE = 3;

  /**
   * The '<em><b>Equal</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Equal</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #EQUAL
   * @model name="equal" literal="="
   * @generated
   * @ordered
   */
  public static final int EQUAL_VALUE = 4;

  /**
   * The '<em><b>Not Equal</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Not Equal</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #NOT_EQUAL
   * @model name="notEqual" literal="!="
   * @generated
   * @ordered
   */
  public static final int NOT_EQUAL_VALUE = 5;

  /**
   * The '<em><b>Sum</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Sum</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #SUM
   * @model name="sum" literal="+"
   * @generated
   * @ordered
   */
  public static final int SUM_VALUE = 6;

  /**
   * The '<em><b>Div</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Div</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #DIV
   * @model name="div" literal="/"
   * @generated
   * @ordered
   */
  public static final int DIV_VALUE = 7;

  /**
   * The '<em><b>Sub</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Sub</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #SUB
   * @model name="sub" literal="-"
   * @generated
   * @ordered
   */
  public static final int SUB_VALUE = 8;

  /**
   * The '<em><b>Multiplicity</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Multiplicity</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #MULTIPLICITY
   * @model name="multiplicity" literal="*"
   * @generated
   * @ordered
   */
  public static final int MULTIPLICITY_VALUE = 9;

  /**
   * An array of all the '<em><b>Operator</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final Operator[] VALUES_ARRAY =
    new Operator[]
    {
      LESS_THEN,
      GREATER_THEN,
      LESS_EQUAL,
      GREATER_EQUAL,
      EQUAL,
      NOT_EQUAL,
      SUM,
      DIV,
      SUB,
      MULTIPLICITY,
    };

  /**
   * A public read-only list of all the '<em><b>Operator</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<Operator> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Operator</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Operator get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Operator result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Operator</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Operator getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Operator result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Operator</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Operator get(int value)
  {
    switch (value)
    {
      case LESS_THEN_VALUE: return LESS_THEN;
      case GREATER_THEN_VALUE: return GREATER_THEN;
      case LESS_EQUAL_VALUE: return LESS_EQUAL;
      case GREATER_EQUAL_VALUE: return GREATER_EQUAL;
      case EQUAL_VALUE: return EQUAL;
      case NOT_EQUAL_VALUE: return NOT_EQUAL;
      case SUM_VALUE: return SUM;
      case DIV_VALUE: return DIV;
      case SUB_VALUE: return SUB;
      case MULTIPLICITY_VALUE: return MULTIPLICITY;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final int value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String name;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String literal;

  /**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private Operator(int value, String name, String literal)
  {
    this.value = value;
    this.name = name;
    this.literal = literal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLiteral()
  {
    return literal;
  }

  /**
   * Returns the literal value of the enumerator, which is its string representation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    return literal;
  }
  
} //Operator
