package mg.dynaquest.queryexecution.po.relational;

import mg.dynaquest.queryexecution.event.POException;



public class TestSelectLoaderPO extends SelectLoaderPO {
	
	/**
	 * @uml.property  name="count"
	 */
	int count = 0;
	
	public TestSelectLoaderPO (){
		super();
	}
	
	public TestSelectLoaderPO(String filename, char delimiter,
			int[] restrictToAttribs) {
		super(filename, delimiter, restrictToAttribs);
	}
	
	@Override
	public Object process_next() throws POException {
		
		Object ret = super.process_next();
		//System.err.println("Zähler: "+count);
		if(count%4 == 0){
			try{
				Thread.sleep(1000);
				
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		count++;
		return ret;
	}

	
}
