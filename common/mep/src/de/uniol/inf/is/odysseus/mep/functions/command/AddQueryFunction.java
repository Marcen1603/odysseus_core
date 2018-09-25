package de.uniol.inf.is.odysseus.mep.functions.command;

import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.core.collection.Context;

public class AddQueryFunction extends AbstractFunction<Command> 
{
	private static final long serialVersionUID = 6694429176108811572L;

	public AddQueryFunction() {
		super("addQuery", 2, new SDFDatatype[][] {{SDFDatatype.STRING}, {SDFDatatype.STRING}}, SDFDatatype.COMMAND);
	}
	
	@Override public Command getValue() {
		final String queryText = (String) getInputValue(0);
		final String parser = (String) getInputValue(1);
		
		return new Command() {
			@Override public boolean run() {
				getExecutor().addQuery(queryText, parser, getSession(), new Context());
				return true;
			}
		};
	}
}
