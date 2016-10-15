package de.uniol.inf.is.odysseus.mep.functions.command;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class UpdateProtocolHandlerOptionMapFunction extends AbstractTargetedCommandFunction
{
	private static final long serialVersionUID = -3994610795368389546L;

	public UpdateProtocolHandlerOptionMapFunction() {
		super("updateProtocolOption", new SDFDatatype[][] {{SDFDatatype.STRING},{SDFDatatype.STRING}});
	}

	@Override public TargetedCommand<?> getValue() {
		final String key = ((String) getInputValue(1));
		final String value = ((String) getInputValue(2));

		return new TargetedCommand<IProtocolHandler<?>>(getTargets(), true) {
			@Override public boolean run(IProtocolHandler<?> protocolHandler) {
				protocolHandler.updateOption(key, value);
				return true;
			}
		};
	}
}
