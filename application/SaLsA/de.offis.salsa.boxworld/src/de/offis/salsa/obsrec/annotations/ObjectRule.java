package de.offis.salsa.obsrec.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.offis.salsa.obsrec.models.ObjectType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ObjectRule {
	ObjectType typeCategory();
	String name();
}
