package de.uniol.inf.is.odysseus.mep.functions.command;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;

public class StartQueryFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = -2104432159434551045L;

	public StartQueryFunction() {
		super("startQuery", null);
	}
	
	@Override public TargetedCommand<?> getValue() {
		return new TargetedCommand<Number>(getTargets(), false) {
			@Override public boolean run(Number queryId) {
				getExecutor().startQuery(queryId.intValue(), getSession());
				return true;
			}
		};
	}
}
