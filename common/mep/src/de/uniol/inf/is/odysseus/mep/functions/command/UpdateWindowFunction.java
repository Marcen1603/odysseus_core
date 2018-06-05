package de.uniol.inf.is.odysseus.mep.functions.command;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.core.physicaloperator.IUpdateableWindow;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class UpdateWindowFunction extends AbstractTargetedCommandFunction
{
	private static final long serialVersionUID = -3994610795368389546L;

	public UpdateWindowFunction() {
		super("updateTimeWindow", new SDFDatatype[][] {SDFDatatype.DISCRETE_NUMBERS, SDFDatatype.DISCRETE_NUMBERS});
	}

	@Override public TargetedCommand<?> getValue() {
		final long size = ((Number) getInputValue(1)).longValue();
		final long advance = ((Number) getInputValue(2)).longValue();

		return new TargetedCommand<IUpdateableWindow>(getTargets(), true) {
			@Override public boolean run(IUpdateableWindow windowPO) {
				if (size > 0){
					windowPO.setWindowSize(size);
				}
				if (advance > 0){
					windowPO.setWindowAdvance(advance);
				}
				return true;
			}
		};
	}
}
