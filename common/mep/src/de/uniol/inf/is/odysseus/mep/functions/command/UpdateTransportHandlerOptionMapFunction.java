package de.uniol.inf.is.odysseus.mep.functions.command;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class UpdateTransportHandlerOptionMapFunction extends AbstractTargetedCommandFunction
{
	private static final long serialVersionUID = -3994610795368389546L;

	public UpdateTransportHandlerOptionMapFunction() {
		super("updateTransportOption", new SDFDatatype[][] {{SDFDatatype.STRING},{SDFDatatype.STRING}});
	}

	@Override public TargetedCommand<?> getValue() {
		final String key = ((String) getInputValue(1));
		final String value = ((String) getInputValue(2));

		return new TargetedCommand<ITransportHandler>(getTargets(), true) {
			@Override public boolean run(ITransportHandler transportHandler) {
				transportHandler.updateOption(key, value);
				return true;
			}
		};
	}
}
