package de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.ISportsQLParameter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SportsQLParameter {
	 public String name();
	 public Class<? extends ISportsQLParameter> parameterClass();
	 public boolean mandatory();
}

