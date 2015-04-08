
package de.uniol.inf.is.odysseus.sensormanagement.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r queryState.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="queryState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="INACTIVE"/>
 *     &lt;enumeration value="PARTIAL"/>
 *     &lt;enumeration value="RUNNING"/>
 *     &lt;enumeration value="PARTIAL_SUSPENDED"/>
 *     &lt;enumeration value="SUSPENDED"/>
 *     &lt;enumeration value="UNDEF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "queryState")
@XmlEnum
@SuppressWarnings(value = { "all" })
	public enum QueryState {

    INACTIVE,
    PARTIAL,
    RUNNING,
    PARTIAL_SUSPENDED,
    SUSPENDED,
    UNDEF;

    public String value() {
        return name();
    }

    public static QueryState fromValue(String v) {
        return valueOf(v);
    }

}
