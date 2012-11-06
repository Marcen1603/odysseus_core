
package de.uniol.inf.is.odysseus.webservice.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for requirement.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
