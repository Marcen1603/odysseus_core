/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.sensor.ontology;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public final class QU {

    /**
     * <p>
     * The RDF model that holds the vocabulary terms
     * </p>
     */
    private static Model m_model = ModelFactory.createDefaultModel();

    /**
     * <p>
     * The namespace of the vocabulary as a string ({@value})
     * </p>
     */
    public static final String NS = "http://purl.oclc.org/NET/ssnx/qu/qu#";

    /**
     * <p>
     * The namespace of the vocabulary as a string
     * </p>
     * 
     * @see #NS
     */
    public static String getURI() {
        return NS;
    }

    /**
     * <p>
     * The namespace of the vocabulary as a resource
     * </p>
     */
    public static final Resource NAMESPACE = m_model.createResource(NS);

    // Vocabulary properties
    // /////////////////////////

    /** unit: Specification of the associated Unit. */
    public static final Property unit = m_model.createProperty(NS + "unit");
    /** quantityKind: Specification of the associated QuantityKind. */
    public static final Property quantityKind = m_model.createProperty(NS + "quantityKind");
    /**
     * baseUnit: Ordered set of Unit that specifies the base units of the system
     * of units. A "base unit" is defined in [VIM] as a
     * "measurement unit that is adopted by convention for a base quantity,"
     * i.e.
     * it is the (preferred) unit in which base quantities of the associated
     * systemOfQuantities are expressed.
     */
    public static final Property baseUnit = m_model.createProperty(NS + "baseUnit");
    /**
     * baseQuantityKind: Ordered set of QuantityKind that specifies the base
     * quantities of the system of quantities. This is a subset of the complete
     * quantityKind list. The base quantities define the basis for the quantity
     * dimension of a kind of quantity.
     */
    public static final Property baseQuantityKind = m_model.createProperty(NS + "baseQuantityKind");
    /**
     * exponent: Rational number that specifies the exponent of the power to
     * which the unit is raised.
     */
    public static final Property exponent = m_model.createProperty(NS + "exponent");
    /** definitionURI: URI that references an external definition. */
    public static final Property definitionURI = m_model.createProperty(NS + "definitionURI");
    /**
     * quantity kind factor: Rational number that specifies the factor in the
     * quantity conversion relationship.
     */
    public static final Property quantityKindFactor = m_model.createProperty(NS + "quantityKindFactor");
    /**
     * unitMultipleFactor: Specifies the multiple or submultiple multiplication
     * factor.
     */
    public static final Property unitMultipleFactor = m_model.createProperty(NS + "unitMultipleFactor");
    /**
     * baseDimension: A Reference to the QuantityKind that represents the base
     * quantity dimension in the factor.
     */
    public static final Property baseDimension = m_model.createProperty(NS + "baseDimension");
    /** numericalValue: Specifies the numerical value. */
    public static final Property numericalValue = m_model.createProperty(NS + "numericalValue");
    /** quantityComponent: Quantities attached to a system. */
    public static final Property quantityComponent = m_model.createProperty(NS + "quantityComponent");
    /** symbol: Short symbolic name. */
    public static final Property symbol = m_model.createProperty(NS + "symbol");
    /**
     * unitComponent: Ordered set of Unit that specifies the units that are
     * known in the system.
     */
    public static final Property unitComponent = m_model.createProperty(NS + "unitComponent");
    /**
     * scaleValueDefinition: Ordered set of ScaleValueDefinition that specifies
     * the defined numerical value(s) and textual definition(s) for the
     * measurement scale.
     */
    public static final Property scaleValueDefinition = m_model.createProperty(NS + "scaleValueDefinition");
    /**
     * conversionFactor: Rational number that specifies the factor in the unit
     * conversion relationship.
     */
    public static final Property conversionFactor = m_model.createProperty(NS + "conversionFactor");
    /** factorQuantityKind: Specification of the associated QuantityKind. */
    public static final Property factorQuantityKind = m_model.createProperty(NS + "factorQuantityKind");
    /** unitKind: Specification of the associated Unit "Kind". */
    public static final Property unitKind = m_model.createProperty(NS + "unitKind");
    /** description: Textual description */
    public static final Property description = m_model.createProperty(NS + "description");
    /**
     * conversionExpression: Specifies the unit conversion relationship in some
     * expression syntax.
     */
    public static final Property expression = m_model.createProperty(NS + "expression");
    /**
     * symbolicExpression: Symbolic expression of the quantity dimension's
     * product of powers, in terms of symbols of the kinds of quantity that
     * represent the base kinds of quantity and their exponents.
     */
    public static final Property symbolicExpression = m_model.createProperty(NS + "symbolicExpression");
    /** propertyType: Specification of the associated ProeprtyType. */
    public static final Property propertyType = m_model.createProperty(NS + "propertyType");
    /**
     * referenceUnit: Specifies the unit with respect to which the
     * ConversionBasedUnit is defined.
     */
    public static final Property referenceUnit = m_model.createProperty(NS + "referenceUnit");
    /**
     * dimension: Derived ordered set of Dimension. The actual dimension of a
     * QuantityKind depends on the list of baseQuantityKind that are specified
     * in an actual SystemOfQuantities, see the DerivedDimensions constraint.
     */
    public static final Property dimension = m_model.createProperty(NS + "dimension");
    /**
     * dimension factor: Rational number that specifies the factor in the
     * dimension conversion relationship.
     */
    public static final Property dimensionFactor = m_model.createProperty(NS + "dimensionFactor");
    /**
     * generalQuantityKind: A generalization relationship between two kinds of
     * quantities.
     */
    public static final Property generalQuantityKind = m_model.createProperty(NS + "generalQuantityKind");
    /**
     * prefix: Ordered set of Prefix that specifies the prefixes for multiples
     * and submultiples of units in the system
     */
    public static final Property prefix = m_model.createProperty(NS + "prefix");
    /**
     * conversionOffset: Rational number that specifies the offset in the unit
     * conversion relationship.
     */
    public static final Property conversionOffset = m_model.createProperty(NS + "conversionOffset");
    /** code: A code is a string that uniquely identifies an individual. */
    public static final Property code = m_model.createProperty(NS + "code");
    /**
     * specificQuantityKind: A specialization relationship between two kinds of
     * quantities.
     */
    public static final Property specificQuantityKind = m_model.createProperty(NS + "specificQuantityKind");
    /** scale: Specification of a Scale that is associated to the QuantityKind. */
    public static final Property scale = m_model.createProperty(NS + "scale");
    /**
     * unit factor: Rational number that specifies the factor in the unit
     * conversion relationship.
     */
    public static final Property unitFactor = m_model.createProperty(NS + "unitFactor");
    /**
     * systemOfQuantities: Reference to the SystemOfQuantities for which the
     * units are specified.
     */
    public static final Property systemOfQuantities = m_model.createProperty(NS + "systemOfQuantities");
    /** name. */
    public static final Property name = m_model.createProperty(NS + "name");

    // Vocabulary classes
    // /////////////////////////

    /**
     * Scale: A Scale represents the [VIM] concept of a "measurement scale" that
     * is defined as an
     * "ordered set of quantity values ofquantities of a given kind of quantity used in ranking, according to magnitude, quantities of that kind."
     * A Scale specifies oneor more fixed values that have a specific
     * significance in the definition of the associating QuantityKind.For
     * example
     * the "thermodynamic temperature" kind of quantity is defined by specifying
     * the values of 0 and 273.16 kelvin asthe temperatures of absolute zero and
     * the triple point of water respectively.A Scale does not always need to
     * specify a unit. For example the "Rockwell C Hardness Scale" or the
     * "Beaufort Wind ForceScale" are ordinal scales that do not have a
     * particular associated unit. Similarly, subjective scales for a "priority"
     * or "risk" kindof quantity with e.g. value definitions 0 for "low", 1 for
     * "medium" and 3 for "high" do not have a particular associated unit.
     */
    public static final Resource Scale = m_model.createResource(NS + "Scale");
    /**
     * Unit: A Unit is an abstract classifier that represents the [VIM] concept
     * of "measurement unit" that is defined as
     * "real scalar quantity,defined and adopted by convention, with which any other quantity of the same kind can be compared to express the ratio of thetwo quantities as a number."
     */
    public static final Resource Unit = m_model.createResource(NS + "Unit");
    /**
     * UnitFactor: A UnitFactor represents a factor in the product of powers
     * that defines a DerivedUnit.
     */
    public static final Resource UnitFactor = m_model.createResource(NS + "UnitFactor");
    /**
     * SimpleUnit: A SimpleUnit is a Unit that represents a measurement unit
     * that does not depend on any other Unit. Typically a base unit wouldbe
     * specified as a SimpleUnit.
     */
    public static final Resource SimpleUnit = m_model.createResource(NS + "SimpleUnit");
    /**
     * ScaleValueDefinition: A ScaleValueDefinition represents a specific value
     * for a measurement scale.
     */
    public static final Resource ScaleValueDefinition = m_model.createResource(NS + "ScaleValueDefinition");
    /**
     * PrefixedUnit: A Prefix represents a named multiple or submultiple
     * multiplication factor used in the specification of a PrefixedUnit.
     * ASystemOfUnits may specify a set of prefixes.
     */
    public static final Resource PrefixedUnit = m_model.createResource(NS + "PrefixedUnit");
    /**
     * PropertyKind : A PropertyKind is a meta-property which helps to
     * characterise categories of properties e.g. scalar, vector
     */
    public static final Resource PropertyKind = m_model.createResource(NS + "PropertyKind");
    /**
     * DerivedUnit: A DerivedUnit is a Unit that represents a measurement unit
     * that is defined as a product of powers of one or more othermeasurement
     * units.For example the measurement unit "metre per second" for "velocity"
     * is specified as the product of "metre" to the power onetimes "second" to
     * the power minus one
     */
    public static final Resource DerivedUnit = m_model.createResource(NS + "DerivedUnit");
    /**
     * QuantityKindFactor: A QuantityKindFactor represents a factor in the
     * product of powers that defines a DerivedQuantityKind.
     */
    public static final Resource QuantityKindFactor = m_model.createResource(NS + "QuantityKindFactor");
    /**
     * SystemOfQuantities: A SystemOfQuantities represents the [VIM] concept of
     * 'system of quantities' that is defined as a 'set of quantities together
     * with a set of non-contradictory equations relating those quantities'. It
     * collects a list of QuantityKind that specifies the kinds of quantity that
     * are known in the system.The International System of Quantities (ISQ) is
     * an
     * example of a SystemOfQuantities, defined in ISO 31 and ISO/IEC 80000.
     */
    public static final Resource SystemOfQuantities = m_model.createResource(NS + "SystemOfQuantities");
    /**
     * SystemOfUnits: A SystemOfUnits represents the [VIM] concept of 'system of
     * units' that is defined as 'set of base units and derived units, together
     * with their multiples and submultiples, defined in accordance with given
     * rules, for a given system of quantities'. It collects a list of Unit that
     * are known in the system. A SysML SystemOfUnits only optionally defines
     * multiples and submultiples.
     */
    public static final Resource SystemOfUnits = m_model.createResource(NS + "SystemOfUnits");
    /**
     * QuantityKind: A QuantityKind is an abstract classifier that represents
     * the [VIM] concept of "kind of quantity" that is defined as
     * "aspectcommon to mutually comparable quantities." A QuantityKind
     * represents the essence of a quantity without any numericalvalue or unit.
     * Quantities of the same kind within a given system of quantities have the
     * same quantity dimension. However,quantities of the same dimension are not
     * necessarily of the same kind.
     */
    public static final Resource QuantityKind = m_model.createResource(NS + "QuantityKind");
    /**
     * ConversionBasedUnit: A ConversionBasedUnit is an abstract classifier that
     * is a Unit that represents a measurement unit that is defined with respect
     * toanother reference unit through an explicit conversion relationship.
     */
    public static final Resource ConversionBasedUnit = m_model.createResource(NS + "ConversionBasedUnit");
    /**
     * Dimension: A Dimension represents the [VIM] concept of 'quantity
     * dimension' that is defined as 'expression of the dependence of a quantity
     * on the base quantities of a system of quantities as a product of powers
     * of
     * factors corresponding to the base quantities, omitting any numerical
     * factor.'For example in the ISQ the quantity dimension of 'force' is
     * denoted by dim F = Lï¿½Mï¿½T^2, where 'F' is the symbol for 'force', and
     * 'L', 'M', 'T' are the symbols for the ISQ base quantities 'length',
     * 'mass'
     * and 'time' respectively.The Dimension of any QuantityKind can be derived
     * through the algorithm that is defined in C.5.3.20 with
     * SystemOfQuantities.
     * The actual Dimension for a given QuantityKind depends on the choice of
     * baseQuantityKind specified in a SystemOfQuantities.
     */
    public static final Resource Dimension = m_model.createResource(NS + "Dimension");
    /**
     * DimensionFactor: A DimensionFactor represents a factor in the product of
     * powers that defines a Dimension.
     */
    public static final Resource DimensionFactor = m_model.createResource(NS + "DimensionFactor");
    /**
     * GeneralConversionUnit: A GeneralConversionUnit is a ConversionBasedUnit
     * that represents a measurement unit that is defined with respect to
     * anotherreference measurement unit through a conversion relationship
     * expressed in some syntax through a general mathematicalexpression.The
     * unit
     * conversion relationship is defined by the following equation:valueRU /
     * valueCU = f(valueRU, valueCU)where:valueRU is the quantity value
     * expressed
     * in the referenceUnit, and,valueCU is the quantity value expressed in the
     * GeneralConversionUnit, and,f(valueRU, valueCU) is a mathematical
     * expression that includes valueRU and valueCU.
     */
    public static final Resource GeneralConversionUnit = m_model.createResource(NS + "GeneralConversionUnit");
    /**
     * LinearConversionUnit: A LinearConversionUnit is a ConversionBasedUnit
     * that represents a measurement unit that is defined with respect to
     * anothermeasurement reference unit through a linear conversion
     * relationship
     * with a conversion factor.The unit conversion relationship is defined by
     * the following equation:valueRU = factor · valueCU,where:valueRU is the
     * quantity value expressed in the referenceUnit, and,valueCU is the
     * quantity
     * value expressed in the LinearConversionUnit.
     */
    public static final Resource LinearConversionUnit = m_model.createResource(NS + "LinearConversionUnit");
    /**
     * DerivedQuantityKind: A DerivedQuantityKind is a QuantityKind that
     * represents a kind of quantity that is defined as a product of powers of
     * one ormore other kinds of quantity. A DerivedQuantityKind may also be
     * used
     * to define a synonym kind of quantity for another kindof quantity.For
     * example "velocity" can be specified as the product of "length" to the
     * power one times "time" to the power minus one, andsubsequently "speed"
     * can
     * be specified as "velocity" to the power one.
     */
    public static final Resource DerivedQuantityKind = m_model.createResource(NS + "DerivedQuantityKind");
    /**
     * SpecializedQuantityKind: A SpecializedQuantityKind is a QuantityKind that
     * represents a kind of quantity that is a specialization of another kind
     * ofquantity.For example, "distance", "width", "depth", "radius" and
     * "wavelength" can all be specified as specializations of the
     * "length"SimpleQuantityKind.
     */
    public static final Resource SpecializedQuantityKind = m_model.createResource(NS + "SpecializedQuantityKind");
    /**
     * Prefix: A Prefix represents a named multiple or submultiple
     * multiplication factor used in the specification of a PrefixedUnit.
     * ASystemOfUnits may specify a set of prefixes.
     */
    public static final Resource Prefix = m_model.createResource(NS + "Prefix");
    /**
     * SimpleQuantityKind: A SimpleQuantityKind is a QuantityKind that
     * represents a kind of quantity that does not depend on any other
     * QuantityKind.Typically a base quantity would be specified as a
     * SimpleQuantityKind.
     */
    public static final Resource SimpleQuantityKind = m_model.createResource(NS + "SimpleQuantityKind");

    /**
     * Class constructor.
     * 
     */
    private QU() {

    }
}
