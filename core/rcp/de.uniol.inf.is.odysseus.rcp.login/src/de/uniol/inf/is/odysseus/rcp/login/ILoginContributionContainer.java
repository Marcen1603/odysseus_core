package de.uniol.inf.is.odysseus.rcp.login;

public interface ILoginContributionContainer {

	public void setMessage( String message );
	public void setErrorMessage( String message );
	public void setInformationMessage( String message );
	public void setWarningMessage( String message );
	
	public void changed();
	public void show( ILoginContribution contributionToShow );
}
