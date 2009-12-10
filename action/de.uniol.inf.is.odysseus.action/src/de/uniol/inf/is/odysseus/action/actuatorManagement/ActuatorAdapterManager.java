package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.action.exception.ActuatorException;
import de.uniol.inf.is.odysseus.action.output.ActuatorAdapter;


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

	public IActuator getActuator(String name) {
		return this.adapters.get(name);
	}

	@Override
	public void createActuator(String name, String description) throws ActuatorException{
		//Parse description
		String className = "";
		Object[] constructorParams = null;
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
					while (paramMatcher.find()){
						String param = paramMatcher.group().trim();
						System.out.println(param);
						Matcher typeMatcher = paramTypePattern.matcher(param);
						if (typeMatcher.find()){
							String type = typeMatcher.group(2).toLowerCase();
							if (type.equals("double")){
								params.add(Double.valueOf(typeMatcher.group(1)).doubleValue());
							}else if (type.equals("float")){
								params.add(Float.valueOf(typeMatcher.group(1)).floatValue());
							}else if (type.equals("long")){
								params.add(Long.valueOf(typeMatcher.group(1)).longValue());
							}else if (type.equals("int")){
								params.add(Integer.valueOf(typeMatcher.group(1)).intValue());
							}else if (type.equals("short")){
								params.add(Short.valueOf(typeMatcher.group(1)).shortValue());
							}else if (type.equals("byte")){
								params.add(Byte.valueOf(typeMatcher.group(1)).byteValue());
							}else if (type.equals("char")){
								params.add(typeMatcher.group(1).charAt(0));
							}else if (type.equals("boolean")){
								params.add(Boolean.valueOf(typeMatcher.group(1)).booleanValue());
							}
						}else {
							params.add(param);
						}
					}
					constructorParams = params.toArray();
				}else {
					constructorParams = new Object[0];
				}
			}
		}
		
		//try to create Adapter
		try {
			Class<?> adapterClass = Class.forName(className);
			for (Constructor<?> constructor : adapterClass.getConstructors()){
				Class<?>[] parameters = constructor.getParameterTypes();
				if (parameters.length != constructorParams.length){
					continue;
				}else {
					//check types
					boolean compatible = true;
					for (int i=0; i<parameters.length;i++){
						//if ()
						if (parameters[i] != constructorParams[i].getClass()){
							compatible = false;
							break;
						}
					}
					//create object
					if (compatible){
						this.adapters.put(className, (ActuatorAdapter)constructor.newInstance(constructorParams));
					}
				}
			}
		} catch (ClassNotFoundException e) {
			throw new ActuatorException(e.getMessage());
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
	public List<ActuatorMethod> getSchema(String name) {
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
	
	public static void main(String[] args) {
		String query = "a.b2.class(1:double, abc)";
		ActuatorAdapterManager m = new ActuatorAdapterManager();
		try {
			m.createActuator("a", query);
		} catch (ActuatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
