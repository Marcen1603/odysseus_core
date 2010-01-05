package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.action.exception.ActuatorException;
import de.uniol.inf.is.odysseus.action.output.IActuator;

/**
 * ActuatorManager for {@link ActuatorAdaper}s.
 * Syntax for ActuatorAdapter descriptions:
 * <fullclassname(parametername:parametertype[,parametername:parametertype]*)>.
 * Parametertypes can only be atomic types or a String! 
 * @author Simon Flandergan
 *
 */
public class ActuatorAdapterManager implements IActuatorManager{
	private HashMap<String, ActuatorAdapter> adapters;
	
	private static Pattern constructorPattern = Pattern.compile(
			"([A-Z][A-Z0-9_\\.]*)" + //fullclass name
			"\\((.*)\\)", Pattern.CASE_INSENSITIVE); //parameters 
	private static Pattern paramPattern = Pattern.compile("[^,]+");
	private static Pattern paramTypePattern = Pattern.compile("(.*):(.*)");
	
	public ActuatorAdapterManager () {
		this.adapters = new HashMap<String, ActuatorAdapter>();
	}
	

	@Override
	public void createActuator(String name, String description) throws ActuatorException{
		name = name.trim();
		//error handling
		if (name.equals("")){
			throw new ActuatorException("Name cannot be empty.");
		}
		if (this.adapters.get(name)!=null){
			throw new ActuatorException("Actuator with name: "+name+" is already registered");
		}
		//Parse description
		String className = "";
		Object[] constructorParams = null;
		Class<?>[] constructorParamTypes = null;
		Matcher matcher = constructorPattern.matcher(description);
		if (matcher.matches()){
			matcher.reset();
			if (matcher.find()){
				className = matcher.group(1);
				String parameters = matcher.group(2);
				parameters = parameters.trim();
				if (parameters.length()>0){
					Matcher paramMatcher = paramPattern.matcher(parameters);
					ArrayList<Object> params = new ArrayList<Object>();
					ArrayList<Class<?>> paramTypes = new ArrayList<Class<?>>();
					while (paramMatcher.find()){
						String param = paramMatcher.group().trim();
						Matcher typeMatcher = paramTypePattern.matcher(param);
						if (typeMatcher.find()){
							String type = typeMatcher.group(2).toLowerCase();
							if (type.equals("double")){
								params.add(Double.valueOf(typeMatcher.group(1)));
								paramTypes.add(double.class);
							}else if (type.equals("float")){
								params.add(Float.valueOf(typeMatcher.group(1)));
								paramTypes.add(float.class);
							}else if (type.equals("long")){
								params.add(Long.valueOf(typeMatcher.group(1)));
								paramTypes.add(long.class);
							}else if (type.equals("int")){
								params.add(Integer.valueOf(typeMatcher.group(1)));
								paramTypes.add(int.class);
							}else if (type.equals("short")){
								params.add(Short.valueOf(typeMatcher.group(1)));
								paramTypes.add(short.class);
							}else if (type.equals("byte")){
								params.add(Byte.valueOf(typeMatcher.group(1)));
								paramTypes.add(byte.class);
							}else if (type.equals("char")){
								params.add(typeMatcher.group(1).charAt(0));
								paramTypes.add(char.class);
							}else if (type.equals("boolean")){
								params.add(Boolean.valueOf(typeMatcher.group(1)));
								paramTypes.add(boolean.class);
							}else {
								params.add(typeMatcher.group(1));
								paramTypes.add(String.class);
							}
						}else {
							params.add(param);
							paramTypes.add(String.class);
						}
					}
					constructorParams = params.toArray();
					constructorParamTypes = new Class<?>[paramTypes.size()];
					paramTypes.toArray(constructorParamTypes);
				}else {
					constructorParams = new Object[0];
					constructorParamTypes = new Class<?>[0];
				}
			}
		}
		
		//try to create Adapter
		try {
			Class<?> adapterClass = Class.forName(className);
			for (Constructor<?> constructor : adapterClass.getConstructors()){
				Class<?>[] parameters = constructor.getParameterTypes();
				if (parameters.length != constructorParamTypes.length){
					continue;
				}else {
					//check types
					boolean compatible = true;
					for (int i=0; i<parameters.length;i++){
						//if ()
						if (parameters[i] != constructorParamTypes[i]){
							compatible = false;
							break;
						}
					}
					//create object
					if (compatible){
						this.adapters.put(name, (ActuatorAdapter)constructor.newInstance(constructorParams));
						return;
					}
				}
			}
		} catch (ClassNotFoundException e) {
			throw new ActuatorException("Class : "+className+"was not found.");
		} catch (IllegalArgumentException e) {
			throw new ActuatorException(e.getMessage());
		} catch (InstantiationException e) {
			throw new ActuatorException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ActuatorException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new ActuatorException(e.getMessage());
		} catch (ClassCastException e){
			throw new ActuatorException("Adapter is not derived from ActuatorAdapter.class");
		}
		
		throw new ActuatorException("Constructor undefined");
	}

	@Override
	public List<ActuatorAdapterMethod> getSchema(String name) {
		ActuatorAdapter adapter = this.adapters.get(name);
		if (adapter != null){
			return adapter.getSchema();
		}
		return null;
	}


	@Override
	public String getName() {
		return "ActuatorAdapterManager";
	}


	@Override
	public IActuator getActuator(String name) throws ActuatorException {
		IActuator actuator = this.adapters.get(name);
		if (actuator == null){
			throw new ActuatorException("Adapter <"+name+"> does not exist.");
		}
		return actuator;
	}
}
