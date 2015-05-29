package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CreateViewCommand extends AbstractExecutorCommand {

	private ILogicalOperator rootAO;
	private String name;

	public CreateViewCommand(String name, ILogicalOperator rootAO,
			ISession caller) {
		super(caller);
		this.rootAO = rootAO;
		this.name = name;
		if (!rootAO.isSourceOperator()){
			throw new TransformationException("Can only create a view on sources not on sinks");
		}
		if (name.contains("-")){
			throw new TransformationException("'-' is not allowed in view names");
		}
	}

	@Override
	public void execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {		
		RenameAO rename = new RenameAO();
		rename.setKeepPosition(true);
		rename.subscribeTo(rootAO, rootAO.getOutputSchema());
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		for (SDFAttribute old : rootAO.getOutputSchema()) {
			attributes.add(new SDFAttribute(name, old.getAttributeName(), old));
		}
		SDFSchema schema = SDFSchemaFactory.createNewWithAttributes(name, attributes, rootAO.getOutputSchema());
		rename.setOutputSchema(schema);

		//		for (SDFAttribute old : rootAO.getOutputSchema()) {
//			attributes.add(new SDFAttribute(old.getSourceName(), old.getAttributeName(), old));
//		}
//		rename.setOutputSchema(new SDFSchema(rootAO.getOutputSchema().getURI(), rootAO.getOutputSchema(),
//				attributes));
		dd.setView(name, rename, getCaller());
	}

	@Override
	public String toString() {
		return "Create VIEW " + name;
	}
	
	public String getName() {
		return name;
	}


}
