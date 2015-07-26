package de.uniol.inf.is.odysseus.badast;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO javaDoc
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface ABaDaStReader {

	public String type();
	
	public String[] parameters();
	
}