package de.uniol.inf.is.odysseus.mep.functions.command;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;

public class RemoveQueryFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = 5447937084488322288L;

	public RemoveQueryFunction() {
		super("removeQuery", null);
	}
	
	@Override public TargetedCommand<?> getValue() {
		return new TargetedCommand<Number>(getTargets(), false) {
			@Override protected boolean run(Number queryId) {
				getExecutor().removeQuery(queryId.intValue(), getSession());
				return true;
			}
		};
	}
}
