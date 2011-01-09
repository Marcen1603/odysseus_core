package windperformancercp.controler;

public class MainControler implements IControler {

	SourceControler srcControl;
	PMControler pmControl;
	
	public MainControler(){
		srcControl = new SourceControler(this);
		pmControl = new PMControler(this);
	}
}
