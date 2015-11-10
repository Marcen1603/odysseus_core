package de.uniol.inf.is.odysseus.codegenerator.modell;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Abstract class for the special targetPlatform options
 * provide a generic parse methode for the special options
 * 
 * @author MarcPreuschaft
 *
 */
public abstract class AbstractTargetPlatformOption implements ITargetPlatformOption{
	
	/**
	 * a generic method to parse the special options. 
	 * The class who extended this class must create
	 * setter and getter method for every special option.
	 * Beware the special option name and the variable must 
	 * have the same name!
	 * 
	 * example @JreTargetplatformOption
	 */
	@Override
	public void parse(TransformationParameter parameter){
		Map<String, String> options = parameter.getOptions();
		
		try {
			BeanUtils.populate(this, options);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}


}
