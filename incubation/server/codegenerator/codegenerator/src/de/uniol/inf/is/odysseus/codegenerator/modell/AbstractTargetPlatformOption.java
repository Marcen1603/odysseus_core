package de.uniol.inf.is.odysseus.codegenerator.modell;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public abstract class AbstractTargetPlatformOption implements ITargetPlatformOption{
	
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
