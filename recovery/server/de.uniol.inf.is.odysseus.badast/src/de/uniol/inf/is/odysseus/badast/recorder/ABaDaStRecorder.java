package de.uniol.inf.is.odysseus.badast.recorder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BaDaSt recorder classes need to have a distinct type, which identifies for
 * which type of data source the recorder can be used. Additionally, each class
 * needs parameters to configure the connection to the source. <br />
 * <br />
 * All implementations of {@link IBaDaStRecorder} need to be annotated with
 * this annotation.
 * 
 * @author Michael Brand
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface ABaDaStRecorder {

	/**
	 * Gets the type.
	 * 
	 * @return A String identifying the recorder type.
	 */
	public String type();

	/**
	 * Gets the parameters.
	 * 
	 * @return All keys of the configuration for that recorder type.
	 */
	public String[] parameters();

}