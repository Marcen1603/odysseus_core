package windperformancercp.controler;

public class PMControler implements IControler {
	
	IControler _control;
	
	public PMControler(IControler control){
		this._control = control;
	}
}
