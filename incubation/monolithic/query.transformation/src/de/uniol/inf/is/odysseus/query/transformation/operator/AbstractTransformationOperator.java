package de.uniol.inf.is.odysseus.query.transformation.operator;

import java.util.HashSet;
import java.util.Set;




public abstract class AbstractTransformationOperator implements IOperator {
	
	public Class<?> implClass = null;
	public String name = "";
	public String targetPlatform = "";
	public Set<String> importList = new HashSet<String>();
	
	
	public AbstractTransformationOperator(){
		defineImports();
	}
	
	public void addImport(Set<String> fullClassName){
		importList.addAll(fullClassName);
	}
	
	public void addImport(String packageName, String simpleClassName){
		importList.add(packageName+"."+simpleClassName);
	}
	
	public Set<String> getNeededImports() {
		return importList;
	}

	public String getName() {
		return name;
	}
	
	public String getTargetPlatform() {
		return targetPlatform;
	}
	
	
	protected void defaultImports(){
		if(implClass  != null){
			importList.add(implClass.getPackage().getName()+"."+implClass.getSimpleName());
		}	
	}
	
}
