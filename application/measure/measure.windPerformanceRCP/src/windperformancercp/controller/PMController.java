package windperformancercp.controller;

import java.util.ArrayList;

public class PMController implements IController {
	
	IController _control;
	
	public PMController(IController control){
		this._control = control;
	}

	@Override
	public ArrayList<?> getContent() {
		// TODO Auto-generated method stub
		return null;
	}
}
