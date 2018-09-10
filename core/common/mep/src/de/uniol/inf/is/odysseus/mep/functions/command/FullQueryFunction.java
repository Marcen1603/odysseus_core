package de.uniol.inf.is.odysseus.mep.functions.command;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;

public class FullQueryFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = -3055859858120956882L;

	public FullQueryFunction() {
		super("fullQuery", null);
	}
	
	@Override public TargetedCommand<?> getValue() {
		return new TargetedCommand<Number>(getTargets(), false) {
			@Override public boolean run(Number queryId) {
				getExecutor().partialQuery(queryId.intValue(), 100, getSession());
				return true;
			}
		};
	}
}
