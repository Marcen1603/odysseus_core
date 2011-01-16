package windperformancercp.controller;

import java.util.ArrayList;

public class MainController implements IController {

	static SourceController srcControl;
	static PMController pmControl;
	
	public MainController(){
		//System.out.println(this.toString()+": mainController says hi");
		srcControl = SourceController.getInstance();
		pmControl = PMController.getInstance();
		
		srcControl.setBrotherControl(pmControl);
		pmControl.setBrotherControl(srcControl);
		
		
	}
	
	@Override
	public ArrayList<?> getContent(){return null;}
}
