package de.uniol.inf.is.odysseus.context.cql;

import java.util.ArrayList;
import java.util.List;
import de.uniol.inf.is.odysseus.context.ContextManagementException;
import de.uniol.inf.is.odysseus.context.store.ContextStoreManager;
import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.context.store.types.MultiElementStore;
import de.uniol.inf.is.odysseus.context.store.types.PartitionedMultiElementStore;
import de.uniol.inf.is.odysseus.context.store.types.SingleElementStore;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Command;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ContextStoreType;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateContextStore;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.DropContextStore;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Query;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SchemaDefinition;
import de.uniol.inf.is.odysseus.parser.cql2.server.IExtension;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;

public class ContextParser implements IExtension {

	private ISession user;
	private IDataDictionary dictionary;
	private List<IExecutorCommand> commands;
	private IMetaAttribute metaattribute;

	@Override
	public Object parse(Query command) {
		throw new QueryParseException("this method is not implemented");
	}

	@Override
	public Object parse(Command command) {
		if (command instanceof CreateContextStore) {
			CreateContextStore cast = ((CreateContextStore) command);
			createContextStore(cast.getAttributes().getName(), cast.getContextType(), cast.getAttributes(), user,
					dictionary, metaattribute, commands);
		} else if (command instanceof DropContextStore) {
			DropContextStore cast = ((DropContextStore) command);
			String name = cast.getName();
			if (ContextStoreManager.storeExists(name))
				ContextStoreManager.removeStore(name);
			else if (cast.getExists() != null)
				throw new QueryParseException("There is no store named \"" + name + "\"");
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createContextStore(String name, ContextStoreType contextType, SchemaDefinition definitions,
			ISession session, IDataDictionary datadictionary, IMetaAttribute metaAttribute,
			List<IExecutorCommand> commands) {
		String schemaName = "ContextStore:" + name;
		List<SDFAttribute> attributes = new ArrayList<>();
		for (int i = 0; i < definitions.getArguments().size() - 1; i = i + 2) {
			SDFAttribute newAttribute = new SDFAttribute(schemaName, definitions.getArguments().get(i),
					new SDFDatatype(definitions.getArguments().get(i + 1)));
			attributes.add(newAttribute);
		}
		SDFSchema schema = SDFSchemaFactory.createNewTupleSchema(schemaName, attributes);

		ITimeIntervalSweepArea sa;
		try {
			sa = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(DefaultTISweepArea.NAME);
		} catch (InstantiationException | IllegalAccessException e1) {
			throw new QueryParseException(e1);
		}

		int size = -1;
		int partitionedBy = -1;
		switch (contextType.getType().toUpperCase()) {
		case ("SINGLE"):
			size = 1;
			break;
		case ("MULTI"):
			size = contextType.getSize();
			partitionedBy = contextType.getPartition();
			break;
		default:
			throw new QueryParseException("Type for context store does not exist!");
		}

		IContextStore<Tuple<? extends ITimeInterval>> store;
		if (size == 1) {
			store = new SingleElementStore<Tuple<? extends ITimeInterval>>(name, schema);
		} else {
			if (partitionedBy < 0)
				store = new MultiElementStore<Tuple<? extends ITimeInterval>>(name, schema, size, sa);
			else {
				if (partitionedBy >= schema.size())
					throw new QueryParseException("Partition key index is not compatiple with the schema!");
				store = new PartitionedMultiElementStore<Tuple<? extends ITimeInterval>>(name, schema, size,
						partitionedBy);
			}
		}

		try {
			ContextStoreManager.addStore(name, store);
		} catch (ContextManagementException e) {
			throw new QueryParseException(e);
		}
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void setUser(ISession user) {
		this.user = user;
	}

	@Override
	public void setDataDictionary(IDataDictionary dd) {
		this.dictionary = dd;
	}

	@Override
	public void setCommands(List<IExecutorCommand> commands) {
		this.commands = commands;
	}

	@Override
	public void setMetaAttribute(IMetaAttribute metaAttribute) {
		this.metaattribute = metaAttribute;
	}

}
