package de.uniol.inf.is.odysseus.rcp.p2p_new.opdetail;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.rcp.server.AbstractOperatorDetailProvider;

@SuppressWarnings("rawtypes")
public class JxtaReceiverOperatorDetailProvider extends AbstractOperatorDetailProvider<JxtaReceiverPO> {

	private static final String JXTA_TITLE = "JXTA";

	@Override
	public String getTitle() {
		return JXTA_TITLE;
	}
	
	@Override
	public void createPartControl(Composite parent, JxtaReceiverPO operator) {
		
	}

	@Override
	public void dispose() {
		
	}
	
	@Override
	protected Class<? extends JxtaReceiverPO> getOperatorType() {
		return JxtaReceiverPO.class;
	}
}
