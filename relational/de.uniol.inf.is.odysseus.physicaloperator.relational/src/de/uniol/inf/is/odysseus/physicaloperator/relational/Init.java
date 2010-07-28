/**
 * 
 */
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @version 1.0
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
public @interface Init {
	boolean meta() default false;
	
}
