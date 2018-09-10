package de.uniol.inf.is.odysseus.mep.functions.command;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;

public class ResumeQueryFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = 4707689772528005919L;

	public ResumeQueryFunction() {
		super("resumeQuery", null);
	}
	
	@Override public TargetedCommand<?> getValue() {
		return new TargetedCommand<Number>(getTargets(), false) {
			@Override public boolean run(Number queryId) {
				getExecutor().resumeQuery(queryId.intValue(), getSession());
				return true;
			}
		};
	}
}
