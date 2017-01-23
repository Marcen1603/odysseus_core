package de.uniol.inf.is.odysseus.admission.status.loadshedding;

/**
 * Provides the status for complex load shedding in consideration of priorities.
 * 
 * The CPU load together witch the latency and queue lengths of each query is used in this status component.
 * 
 * @author Jannes
 *
 */
public class ComplexLoadSheddingWPAdmissionStatusComponent extends AbstractLoadSheddingAdmissionStatusComponent {

	private final String NAME = "complexWP";
	
	public ComplexLoadSheddingWPAdmissionStatusComponent() {
		LoadSheddingAdmissionStatusRegistry.addLoadSheddingAdmissionComponent(this);
	}

	@Override
	public void runLoadShedding() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollBackLoadShedding() {
		// TODO Auto-generated method stub

	}

	@Override
	public void measureStatus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getComponentName() {
		return NAME;
	}

}
