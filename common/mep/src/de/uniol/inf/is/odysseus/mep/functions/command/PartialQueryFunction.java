package de.uniol.inf.is.odysseus.mep.functions.command;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;

public class PartialQueryFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = 4514411378586416797L;

	public PartialQueryFunction() {
		super("partialQuery", null);
	}
	
	@Override public TargetedCommand<?> getValue() {
		return new TargetedCommand<Number>(getTargets(), false) {
			@Override public boolean run(Number queryId) {
				getExecutor().partialQuery(queryId.intValue(), 50, getSession());
				return true;
			}
		};
	}
}
