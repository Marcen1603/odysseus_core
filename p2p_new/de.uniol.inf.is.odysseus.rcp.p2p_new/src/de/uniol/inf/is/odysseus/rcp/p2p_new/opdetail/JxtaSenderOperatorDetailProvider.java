package de.uniol.inf.is.odysseus.rcp.p2p_new.opdetail;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.rcp.server.AbstractOperatorDetailProvider;

@SuppressWarnings("rawtypes")
public class JxtaSenderOperatorDetailProvider extends AbstractOperatorDetailProvider<JxtaSenderPO> {

	private static final String JXTA_TITLE = "JXTA";

	@Override
	public void createPartControl(Composite parent, JxtaSenderPO operator) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public String getTitle() {
		return JXTA_TITLE;
	}

	@Override
	protected Class<? extends JxtaSenderPO> getOperatorType() {
		return JxtaSenderPO.class;
	}
}
