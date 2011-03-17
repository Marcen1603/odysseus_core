package de.uniol.inf.is.odysseus.logicaloperator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LogicalOperator {
	public String name();
	public int minInputPorts();
	public int maxInputPorts();
}
