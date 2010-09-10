package de.uniol.inf.is.odysseus.console;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Help {
	public String parameter() default "";
	public String description() default "<no description available>";
}
