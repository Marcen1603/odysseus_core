
package de.uniol.inf.is.odysseus.sensormanagement.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r requirement.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="requirement">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MANDATORY"/>
 *     &lt;enumeration value="OPTIONAL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "requirement")
@XmlEnum
@SuppressWarnings(value = { "all" })
	public enum Requirement {

    MANDATORY,
    OPTIONAL;

    public String value() {
        return name();
    }

    public static Requirement fromValue(String v) {
        return valueOf(v);
    }

}
