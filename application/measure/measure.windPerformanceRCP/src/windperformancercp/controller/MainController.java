package windperformancercp.controller;

import java.util.ArrayList;

public class MainController implements IController {

	SourceController srcControl;
	PMController pmControl;
	
	public MainController(){
		srcControl = SourceController.getInstance();
		pmControl = PMController.getInstance();
		//System.out.println(this.toString()+": mainController says hi");
	}
	
	@Override
	public ArrayList<?> getContent(){return null;}
}
