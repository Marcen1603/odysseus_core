package de.uniol.inf.is.odysseus.rcp.login.view;

import java.util.Collection;

import de.uniol.inf.is.odysseus.rcp.login.ILoginContribution;
import de.uniol.inf.is.odysseus.rcp.login.ILoginContributionContainer;

public interface ILoginWindow extends ILoginContributionContainer {

	public void show( Collection<ILoginContribution> contributions, boolean showWindowAgainSetting );
	
	public boolean isOK();
	public boolean isCancel();
	public boolean isShowAgain();
}
