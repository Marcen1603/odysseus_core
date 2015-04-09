package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.command.ICommandProvider;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;

public class CommandPO<T extends IStreamObject<?>> extends AbstractSink<T> 
{
	static Logger LOG = LoggerFactory.getLogger(SenderPO.class);

	Map<ICommandProvider, Command> listenerMap = new HashMap<>();
	
	private final String commandName;
	
	public CommandPO(String commandName) 
	{
		this.commandName = commandName.toLowerCase();
	}

	public CommandPO(CommandPO<T> other) 
	{
		super(other);
		
		this.commandName = other.commandName;
	}

	@Override
	protected void process_next(T object, int port) 
	{
		try
		{
			for (Entry<ICommandProvider, Command> e : listenerMap.entrySet())
			{
				if (commandName == null)
				{
					String curCommandName = null;
					if (object instanceof Tuple<?>)
					{
						for (SDFAttribute attr : getOutputSchema(port).getAttributes())
						{
							if (attr.getAttributeName().equalsIgnoreCase("Command"))
								curCommandName = (String) ((Tuple<?>) object).getAttribute(attr.getNumber());
						}
					}
					else
					if (object instanceof KeyValueObject<?>)
						curCommandName = (String) ((KeyValueObject<?>) object).getAttribute("Command");
					else
						throw new UnsupportedOperationException("Can only process Tuple or KeyValueObject");
					
					if (curCommandName == null)
						throw new IllegalArgumentException("Could not get \"Command\" attribute");
					
					Command cmd = e.getKey().getCommandByName(curCommandName.toLowerCase(), getOutputSchema(port));
					if (cmd == null)
						throw new IllegalArgumentException("Could not find command \"" + commandName + "\"");
					
					cmd.run(object);
				}
				else
				{
					System.out.println("Run command \"" + commandName + "\" with " + object);
					e.getValue().run(object);	
				}
			}
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage());
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) 
	{
	}

	@Override
	protected void process_open() throws OpenFailedException 
	{
		if (!isOpen()) 
		{
			super.process_open();
		}
	}

	@Override
	protected void process_close() 
	{
		if (isOpen()) 
		{
			super.process_close();
		}
	}

	@Override
	public AbstractSink<T> clone() 
	{
		return new CommandPO<T>(this);
	}

	public void addCommandListener(ICommandProvider listener, SDFSchema schema) 
	{
		if (!listenerMap.containsKey(listener))
		{
			if (commandName == null)
				listenerMap.put(listener, null);
			else
			{
				Command command = listener.getCommandByName(commandName, schema);
				
				if (command == null)
					throw new IllegalArgumentException("Could not find command \"" + commandName + "\"");
					
				listenerMap.put(listener, command);
			}
		}
	}

	public void removeCommandListener(ICommandProvider listener) 
	{
		listenerMap.remove(listener);
	}
	
}

