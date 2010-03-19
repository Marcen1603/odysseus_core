package de.uniol.inf.is.odysseus.broker.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerWrapperPlanFactory;
import de.uniol.inf.is.odysseus.broker.transaction.GraphUtils;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;

public class ExecutorBinder implements CommandProvider{
		
	
	private Logger logger = LoggerFactory.getLogger("Broker Executor");
	private IAdvancedExecutor executor;	
		
	
	public void _brokerPrintPorts(CommandInterpreter ci){
		List<String> args = this.getArgs(ci);
		if(args.size()==0){
			for(BrokerPO<?> broker : BrokerWrapperPlanFactory.getAllBrokerPOs()){
				ci.println("******** Ports for broker \""+broker.getIdentifier()+"\" ********");
				ci.println(printPorts(broker));				
			}
		}else{
			BrokerPO<?> broker = BrokerWrapperPlanFactory.getPlan(args.get(0));
			ci.println("Ports for broker: "+broker.getIdentifier());
			ci.println(printPorts(broker));			
		}
	}
	
	@SuppressWarnings("unchecked")
	private String printPorts(BrokerPO<?> broker){
		
		StringBuilder builder = new StringBuilder();
		builder.append("Reading Ports:\n");
		builder.append("--------------\n");
		for(PhysicalSubscription<? extends ISink> sub : broker.getSubscriptions()){
			builder.append(sub.getSourceOutPort()+" ("+sub.getTarget()+")\n");
		}
		builder.append("\n");
		builder.append("Writing Ports:\n");
		builder.append("--------------\n");
		for(PhysicalSubscription<? extends ISource> sub : broker.getSubscribedToSource()){
			builder.append(sub.getSinkInPort()+" ("+sub.getTarget()+")\n");
		}
		return builder.toString();
	}

	public void _brokerPrintPlan(CommandInterpreter ci){
		List<String> args = this.getArgs(ci);
		if(args.size()==0){
			for(BrokerPO<?> broker : BrokerWrapperPlanFactory.getAllBrokerPOs()){
				ci.println("******** BROKER: "+broker.getIdentifier()+" ********");
				ci.println(GraphUtils.planToString(broker, ""));				
			}
		}else{
			String brokerPOName = args.get(0);
			BrokerPO<?> broker = BrokerWrapperPlanFactory.getPlan(brokerPOName);
			ci.println(GraphUtils.planToString(broker, ""));
		}
	}

		
	public void _runfile(CommandInterpreter ci) {
		String args = this.getArgsAsString(ci);						
		File file = new File(args);
			logger.debug("--- running macro from file: " + file.getAbsolutePath()+ " ---");
			
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(file));
			
			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				zeile = zeile.trim();
				if(!zeile.isEmpty()){
					logger.debug("Running command: " + zeile);
					ci.execute(zeile);					
				}
			}
			} catch (FileNotFoundException e) {
				logger.error("File not found ("+file.getAbsolutePath()+")");
			} catch (IOException e) {
				logger.error("Error while reading from file");
			}
			ci.println("--- macro from file " + file.getAbsolutePath() + " done ---");
		
	}
			
	/******************************
	 * SERVICE BINDINGS
	 ******************************/
	
	public void bindExecutor(IAdvancedExecutor executor) {		
		this.executor = executor;
		logger.info("Broker Executor bound");
	}
	
	public void unbindExecutor(IAdvancedExecutor executor) {
		this.executor = null;		
	}
	
	
		
	@Override
	public String getHelp() {		
		return "";
	}
	
	
	/************************
	 * HELPERS
	 ************************/
	
	private List<String> getArgs(CommandInterpreter ci){
		List<String> args = new ArrayList<String>();
		String current = ci.nextArgument();
		while(current!=null){
			args.add(current);
			current = ci.nextArgument();	
		}
		return args;
	}
	private String getArgsAsString(CommandInterpreter ci){
		return getArgsAsString(getArgs(ci));
	}
	
	private String getArgsAsString(List<String> args){
		StringBuilder builder = new StringBuilder();
		for(String arg: args){
			builder.append(arg+" ");
		}
		return builder.toString();
	}

	
	
	
		
}
