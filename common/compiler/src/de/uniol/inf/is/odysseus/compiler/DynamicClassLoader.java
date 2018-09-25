package de.uniol.inf.is.odysseus.compiler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by trung on 5/3/15.
 */
public class DynamicClassLoader extends ClassLoader {

    private Map<String, CompiledCode> customCompiledCode = new HashMap<>();
	private Collection<ClassLoader> alternativeClassLoader;

    public DynamicClassLoader(ClassLoader parent, Collection<ClassLoader> alternativeClassLoader) {
        super(parent);
        this.alternativeClassLoader = alternativeClassLoader;
    }

    public void setCode(CompiledCode cc) {
        customCompiledCode.put(cc.getName(), cc);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        CompiledCode cc = customCompiledCode.get(name);
        if (cc == null) {
        	try{
        		return super.findClass(name);
        	}catch(Exception e){
        		for (ClassLoader cl: alternativeClassLoader){
        			try{
        				return cl.loadClass(name);
        			}catch(Exception ex){

        			}
        		}
        	}
        	throw new ClassNotFoundException(name);
        }
        byte[] byteCode = cc.getByteCode();
        return defineClass(name, byteCode, 0, byteCode.length);
    }
}
