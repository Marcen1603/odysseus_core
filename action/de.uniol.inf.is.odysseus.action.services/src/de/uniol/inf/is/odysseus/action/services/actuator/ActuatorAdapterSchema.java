package de.uniol.inf.is.odysseus.action.services.actuator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for {@link ActuatorAdapter}s. 
 * <show> specifies if method should be shown in reduced schema.
 * <provide> specifies if method should be provided by actuator; default: true.
 * If <provide> is false, show attribute is always false.
 * @author Simon Flandergan
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActuatorAdapterSchema {
	boolean show() default true;
	boolean provide() default true;
}
