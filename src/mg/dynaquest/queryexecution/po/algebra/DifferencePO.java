/*
 * Created on 07.12.2004
 *
 */
package mg.dynaquest.queryexecution.po.algebra;



/**
 * @author Marco Grawunder
 *
 */
public class DifferencePO extends BinaryAlgebraPO{

    /**
     * @param differencePO
     */
    public DifferencePO(DifferencePO differencePO) {
        super(differencePO);
        setPOName("DifferencePO");
    }

    /**
     * 
     */
    public DifferencePO() {
        super();
        setPOName("DifferencePO");
    }

	public @Override
	SupportsCloneMe cloneMe() {	
		return new DifferencePO(this);
	}



  

}
