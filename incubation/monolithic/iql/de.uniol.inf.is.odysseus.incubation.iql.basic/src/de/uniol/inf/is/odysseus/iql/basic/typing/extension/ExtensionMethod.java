package de.uniol.inf.is.odysseus.iql.basic.typing.extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExtensionMethod {
	boolean ignoreFirstParameter() default true;
}
