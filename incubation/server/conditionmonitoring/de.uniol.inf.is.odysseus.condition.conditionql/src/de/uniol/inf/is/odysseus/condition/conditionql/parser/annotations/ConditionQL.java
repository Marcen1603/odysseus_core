package de.uniol.inf.is.odysseus.condition.conditionql.parser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.uniol.inf.is.odysseus.condition.conditionql.parser.enums.ConditionAlgorithm;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConditionQL {
	public ConditionAlgorithm conditionAlgorithm();

}
