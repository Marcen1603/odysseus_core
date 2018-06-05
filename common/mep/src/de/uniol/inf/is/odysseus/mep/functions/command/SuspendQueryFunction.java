package de.uniol.inf.is.odysseus.mep.functions.command;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;

public class SuspendQueryFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = -3414707787752681640L;

	public SuspendQueryFunction() {
		super("suspendQuery", null);
	}
	
	@Override public TargetedCommand<?> getValue() {
		return new TargetedCommand<Number>(getTargets(), false) {
			@Override public boolean run(Number queryId) {
				getExecutor().suspendQuery(queryId.intValue(), getSession());
				return true;
			}
		};
	}
}
