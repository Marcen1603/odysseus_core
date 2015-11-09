package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CommandAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CommandPO extends AbstractSink<Tuple<IMetaAttribute>> 
{
	static Logger LOG = LoggerFactory.getLogger(SenderPO.class);

	private final RelationalExpression<IMetaAttribute> commandExpression;
	private final IServerExecutor executor;
	private ISession caller;
	
	public CommandPO(CommandAO ao, IServerExecutor executor) {
		Preconditions.checkNotNull(ao, "ao must not be null!");

		this.executor = executor;
		this.commandExpression = new RelationalExpression<IMetaAttribute>(ao.getCommandExpression());
		this.commandExpression.initVars(ao.getInputSchema(0));
	}
	
	public CommandPO(CommandPO other) {
		this.executor = other.executor;
		this.commandExpression = other.commandExpression;
	}
	
	@Override
	protected void process_open() throws OpenFailedException 
	{
		List<ISession> callers = getSessions();
		if (callers.size() != 1) {
			throw new OpenFailedException("This operator cannot be sharded");
		}
		caller = callers.get(0);
	}	

	// This method tries to resolve a name into an operator, a transport handler or a protocol handler
	// TODO: Implement global Odysseus naming scheme?
	@SuppressWarnings({ "rawtypes" })
	private static Object resolveName(IServerExecutor executor, ISession caller, String name)
	{
		// Temporary hack to address transport and protocol handlers 
		boolean isTransport = false, isProtocol = false;
		if (name.endsWith(".transport")) {
			isTransport = true;
			name = name.substring(0, name.length() - ".transport".length());
		} else
		if (name.endsWith(".protocol")) {
			isProtocol = true;
			name = name.substring(0, name.length() - ".protocol".length());
		}			
		
       	Resource id = new Resource(caller.getUser(), name);
       	IPhysicalOperator targetOperator = executor.getDataDictionary(caller).getOperator(id, caller);
       	if (targetOperator == null) 
       	{
       		LOG.warn("Could not resolve target " + name);
       		return null;
       	} 
       	else if (!isTransport && !isProtocol) 
       	{       	
       		return targetOperator;
       	} 
       	else if (targetOperator instanceof ReceiverPO) 
       	{
			ReceiverPO receiver = (ReceiverPO) targetOperator;
       		
       		if (isProtocol) {
       			return receiver.getProtocolHandler();
       		} else {
       			return ((AbstractProtocolHandler) receiver.getProtocolHandler()).getTransportHandler();
       		}
       	} 
       	else 
       	{
       		LOG.warn("Operator must be ReceiverPO if .transport or .protocol is specified!");
       		return null;
       	}
	}
	
	@Override
	protected void process_next(Tuple<IMetaAttribute> object, int port) 
	{
		try {
			List<Tuple<IMetaAttribute>> preProcessResult = null;
			Command command = (Command) commandExpression.evaluate(object, getSessions(), preProcessResult);
			
			if (command instanceof TargetedCommand) {
				TargetedCommand<?> tCommand = (TargetedCommand<?>) command;
				if (tCommand.needsTargetsResolved())
				{
					List<Object> targets = tCommand.getTargets();
					List<Object> resolvedTargets = new ArrayList<>(targets.size());
					for (Object target : targets)
						resolvedTargets.add(resolveName(executor, caller, target.toString()));
					tCommand.setResolvedTargets(resolvedTargets);
				}
			}

			command.setSession(caller);
			command.setExecutor(executor);			
			command.run();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
		
	@Override
	public AbstractSink<Tuple<IMetaAttribute>> clone() {
		return new CommandPO(this);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		
	}
	
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}	
}

