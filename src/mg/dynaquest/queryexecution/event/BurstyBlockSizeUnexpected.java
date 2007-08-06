package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * @author  Marco Grawunder
 */
public class BurstyBlockSizeUnexpected extends InformationEvent {
	// private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1021171783910707807L;
	/**
	 * @uml.property  name="blocksize"
	 */
	private int blocksize;

	public BurstyBlockSizeUnexpected(TriggeredPlanOperator source, int blocksize) {
		super(source, "Bursty Block Unexpected");
		this.blocksize = blocksize;
	}

	/**
	 * @return  the blocksize
	 * @uml.property  name="blocksize"
	 */
	public int getBlocksize() {
		return blocksize;
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.BurstyBlockSizeUnexpected;
	}

}

