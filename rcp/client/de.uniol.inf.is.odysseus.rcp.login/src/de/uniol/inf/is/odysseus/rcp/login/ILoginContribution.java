package de.uniol.inf.is.odysseus.rcp.login;

import java.util.Collection;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;

public interface ILoginContribution {

	public void onInit();
	public void onLoad( Map<String, String> savedConfig );
	
	public boolean isValid();
	
	public String getTitle();
	public void createPartControl( Composite parent, ILoginContributionContainer container);
	public void dispose();
	
	public Map<String, String> onSave();
	public boolean onFinish(Collection<ILoginContribution> otherContributions);
	public int getPriority();
}
