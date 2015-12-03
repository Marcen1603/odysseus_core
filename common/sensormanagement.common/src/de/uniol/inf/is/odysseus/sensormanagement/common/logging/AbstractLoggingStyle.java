package de.uniol.inf.is.odysseus.sensormanagement.common.logging;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name = "abstractLoggingStyle")
@XmlAccessorType (XmlAccessType.FIELD)
@XmlSeeAlso({DefaultLoggingStyle.class, IntervalLoggingStyle.class})
public class AbstractLoggingStyle {
	public String name;
}
