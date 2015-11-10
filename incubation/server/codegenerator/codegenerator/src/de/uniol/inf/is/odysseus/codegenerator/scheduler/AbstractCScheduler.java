package de.uniol.inf.is.odysseus.codegenerator.scheduler;

import org.osgi.service.component.ComponentContext;

/**
 * Abstract class for scheduler
 * 
 * @author MarcPreuschaft
 *
 */
public abstract class AbstractCScheduler implements ICScheduler{
	
	//name of the scheduler
	private String name = "";
	//name of the targetplatform 
	private String targetPlatform = "";
	//name of the packageName 
	private String packageName = "";
	
	private ComponentContext context;
	
	public AbstractCScheduler(String name) {
		this.name = name;
	}
	
	public AbstractCScheduler(String name, String targetPlatform) {
		this.name = name;
		this.targetPlatform = targetPlatform;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTargetPlatform() {
		return targetPlatform;
	}
	
	public void setTragetPlattform(String targetPlatform) {
		this.targetPlatform = targetPlatform;
	}


	/*
	 * call by osgi
	 */
	protected void activate(ComponentContext context) {
		this.context = context;
		initRule();
	}

	private void initRule() {
		String targetPlattform = (String) this.context.getProperties().get(
				"targetPlatform");
		setTragetPlattform(targetPlattform);
	}

	public String getPackageName() {
		return packageName;
	}

	@Override
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	

}
