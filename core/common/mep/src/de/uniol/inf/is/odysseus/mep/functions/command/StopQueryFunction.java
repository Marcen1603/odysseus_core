package de.uniol.inf.is.odysseus.mep.functions.command;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;

public class StopQueryFunction extends AbstractTargetedCommandFunction 
{
	private static final long serialVersionUID = 6163820701550337541L;

	public StopQueryFunction() {
		super("stopQuery", null);
	}
	
	@Override public TargetedCommand<?> getValue() {
		return new TargetedCommand<Number>(getTargets(), false) {
			@Override public boolean run(Number queryId) {
				getExecutor().stopQuery(queryId.intValue(), getSession());
				return true;
			}
		};
	}
}
