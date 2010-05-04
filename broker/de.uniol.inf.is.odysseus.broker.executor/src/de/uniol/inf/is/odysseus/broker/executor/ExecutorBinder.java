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

import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerWrapperPlanFactory;
import de.uniol.inf.is.odysseus.broker.transaction.GraphUtils;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;

/**
 * The ExecutorBinder extends the console to provide some commands for the broker.
 * 
 * @author Dennis Geesen
 */
public class ExecutorBinder implements CommandProvider{
		
	
	/** The logger. */
	private Logger logger = LoggerFactory.getLogger("Broker Executor");
	
	
	
	public void _cycleTest(CommandInterpreter ci){
		ci.execute("useBrokerConfig on");
		ci.execute("CREATE STREAM video(timestamp LONG, id INTEGER, interval INTEGER, type STRING, posx DOUBLE, posy DOUBLE) CHANNEL localhost : 65058;");
		ci.execute("CREATE BROKER zyklus(timestamp LONG, id INTEGER, interval INTEGER, type STRING, posx DOUBLE, posy DOUBLE) QUEUE (timestamp LONG)");
		ci.execute("SELECT * INTO zyklus FROM video[SIZE 2 ADVANCE 1 ON interval] WHERE interval=0;");		
		int anzahl = Integer.parseInt(getArgs(ci).get(0));
		for(int number = 1;number<=anzahl;number++){		
			String stream = "CREATE STREAM stream"+number+"(timestamp LONG, id INTEGER, interval INTEGER, type STRING, posx DOUBLE, posy DOUBLE) CHANNEL localhost : 65056;";		
			String anfrage  = "SELECT stream"+number+".timestamp, stream"+number+".id, stream"+number+".interval, stream"+number+".type, stream"+number+".posx, stream"+number+".posy INTO zyklus FROM BROKER zyklus QUEUE BY (SELECT interval FROM stream"+number+" [SIZE 2 ADVANCE 1 ON interval] WHERE interval>0), stream"+number+" [SIZE 2 ADVANCE 1 ON interval] WHERE zyklus.id=stream"+number+".id AND stream"+number+".interval>0";
			ci.execute(stream);
			ci.execute(anfrage);
		}
		ci.execute("SELECT * FROM zyklus METRIC ON timestamp");
		ci.execute("schedule");
	}
	
	/**
	 * Switching one or all brokers into debug mode
	 * 
	 * @param ci the CommandInterpreter
	 */
	public void _brokerDebug(CommandInterpreter ci){
		List<String> args = this.getArgs(ci);
		if(args.size()==0){
			ci.println("No value (on | off) given");
			for(BrokerPO<?> broker : BrokerWrapperPlanFactory.getAllBrokerPOs()){
				ci.println("******** Switching debug mode for all brokers ********");
				broker.setPrintDebug(!broker.isPrintDebug());
				ci.println("Switched debug mode of \""+broker.getIdentifier()+"\" to "+broker.isPrintDebug());				
			}
		}else{
			if(args.size()>=1){				
				try{
					boolean switchTo = toBoolean(args.get(0));
					if(args.size()==2){
						BrokerPO<?> broker = BrokerWrapperPlanFactory.getPlan(args.get(1));			
						broker.setPrintDebug(switchTo);
						ci.println("Switched debug mode of \""+broker.getIdentifier()+"\" to "+broker.isPrintDebug());
					}else{
						for(BrokerPO<?> broker : BrokerWrapperPlanFactory.getAllBrokerPOs()){
							ci.println("******** Switching debug mode for all brokers to "+switchTo+" ********");
							broker.setPrintDebug(switchTo);
							ci.println("Switched debug mode of \""+broker.getIdentifier()+"\" to "+broker.isPrintDebug());				
						}
					}
				}catch(Exception e){
					ci.println(e.getMessage());
				}
			}
						
		}
	}
	
	/**
	 * Prints all ports and transactions for all brokers or a specific broker
	 *
	 * @param ci the CommandInterpreter of the console
	 */
	public void _brokerPorts(CommandInterpreter ci){
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
	
	/**
	 * Prints the ports for a given broker.
	 *
	 * @param broker the broker
	 * @return the result string
	 */
	@SuppressWarnings("unchecked")
	private String printPorts(BrokerPO<?> broker){
		
		StringBuilder builder = new StringBuilder();
		builder.append("Reading Ports:\n");
		builder.append("--------------\n");
		for(PhysicalSubscription<? extends ISink> sub : broker.getSubscriptions()){			
			String type = BrokerDictionary.getInstance().getReadTypeForPort(broker.getIdentifier(), sub.getSourceOutPort()).toString();
			builder.append(sub.getSourceOutPort()+" ("+sub.getTarget()+") - ["+type+"]\n");
		}
		builder.append("\n");
		builder.append("Writing Ports:\n");
		builder.append("--------------\n");
		for(PhysicalSubscription<? extends ISource> sub : broker.getSubscribedToSource()){
			String type = BrokerDictionary.getInstance().getWriteTypeForPort(broker.getIdentifier(), sub.getSinkInPort()).toString();
			builder.append(sub.getSinkInPort()+" ("+sub.getTarget()+") - ["+type+"]\n");
		}
		return builder.toString();
	}

	/**
	 * Prints the physical plan for all or a specific broker.
	 *
	 * @param ci the CommandInterpreter of the console
	 */
	public void _brokerPlan(CommandInterpreter ci){
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
	
	/**
	 * Prints the current content of all or a specific broker.
	 *
	 * @param ci the ci
	 */
	public void _brokerContent(CommandInterpreter ci){
		List<String> args = this.getArgs(ci);
		if(args.size()==0){
			for(BrokerPO<?> broker : BrokerWrapperPlanFactory.getAllBrokerPOs()){
				ci.println("******** BROKER: "+broker.getIdentifier()+" ********");
				ci.println(broker.getContent());
				ci.println("");				
			}
		}else{
			String brokerPOName = args.get(0);
			BrokerPO<?> broker = BrokerWrapperPlanFactory.getPlan(brokerPOName);
			ci.println(broker.getContent());
			ci.println("");
		}
	}

		
	/**
	 * Reads a file from the current working directory and executes each line as if it comes from the console
	 *
	 * @param ci the CommandInterpreter of the console
	 */
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
			
	/**
	 * Binds the executor
	 *
	 * @param executor the executor to bind
	 */
	
	public void bindExecutor(IAdvancedExecutor executor) {		
		logger.info("Broker Executor bound");
	}
	
	/**
	 * Unbinds the executor.
	 *
	 * @param executor the executor to unbind
	 */
	public void unbindExecutor(IAdvancedExecutor executor) {		
	}
	
	
		
	/* (non-Javadoc)
	 * @see org.eclipse.osgi.framework.console.CommandProvider#getHelp()
	 */
	@Override
	public String getHelp() {		
		StringBuffer buffer = new StringBuffer();
		buffer.append("---Odysseus Broker Commands---");
		buffer.append("\tbrokerContent [name] - returns the current content of each broker or only of the broker with the given name\n");
		buffer.append("\tbrokerDebug [on|off] [name] - switch one broker or all brokers into debug mode if they are not in or vice versa.\n");		
		buffer.append("\tbrokerPlan [name] - returns the current physical plan of each broker or only of the broker with the given name\n");
		buffer.append("\tbrokerPorts [name] - returns the current ports and transaction types of each broker or only of the broker with the given name\n");
		buffer.append("\trunfile f - reads the file f from the current working directory and executes each line as if it has came from the console\n");		
		return buffer.toString();

	}
	
	
	/************************
	 * HELPERS
	 ***********************/
	
	/**
	 * Converts specified arguments of a CommandInterpretor into a list of strings
	 * @param ci the CommandInterpreter
	 * @return a list of arguments
	 */
	
	private List<String> getArgs(CommandInterpreter ci){
		List<String> args = new ArrayList<String>();
		String current = ci.nextArgument();
		while(current!=null){
			args.add(current);
			current = ci.nextArgument();	
		}
		return args;
	}
	
	/**
	 * Converts all specified arguments to a single string
	 *
	 * @param ci the CommandInterpreter
	 * @return the result string
	 */
	private String getArgsAsString(CommandInterpreter ci){
		return getArgsAsString(getArgs(ci));
	}
	
	/**
	 * Converts a list of arguments to a single string
	 *
	 * @param args a list of arguments
	 * @return the a single string
	 */
	private String getArgsAsString(List<String> args){
		StringBuilder builder = new StringBuilder();
		for(String arg: args){
			builder.append(arg+" ");
		}
		return builder.toString();
	}

	/**
	 * Converts a string to boolean
	 * Valid values are: true, on, 1, false, off, 0
	 * 
	 * @param string the string to convert
	 * @return true, if string represents a true value
	 */
	private boolean toBoolean(String string) {
		if (string.equalsIgnoreCase("true")) {
			return true;
		}
		if (string.equalsIgnoreCase("on")) {
			return true;
		}
		if (string.equalsIgnoreCase("1")) {
			return true;
		}
		if (string.equalsIgnoreCase("false")) {
			return false;
		}
		if (string.equalsIgnoreCase("off")) {
			return false;
		}
		if (string.equalsIgnoreCase("0")) {
			return false;
		}
		throw new IllegalArgumentException("can't convert '" + string
				+ "' to boolean value");
	}
	
		
}
