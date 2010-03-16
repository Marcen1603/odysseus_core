package de.uniol.inf.is.odysseus.broker.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.QueryBuildParameter;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.datastructure.Query;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;

public class ExecutorBinder implements CommandProvider{
		
	private List<Query> currentTranslatedPlans = new ArrayList<Query>();
	private List<Query> currentRestructuredPlans = new ArrayList<Query>();
	private List<Query> currentTransformedPlans = new ArrayList<Query>();
	private Map<String, QueryBuildParameter> queries = new HashMap<String, QueryBuildParameter>();
	private Logger logger = LoggerFactory.getLogger("Broker Executor");
	private IAdvancedExecutor executor;
	private ICompiler compiler;
	private String currentParser;
	private TransformationConfiguration transformationConfiguration = new TransformationConfiguration("relational", ITimeInterval.class.getName());
	

	/**
	 * USAGE: "addonce SELECT ... ; [<S>|<E>]"
	 * @param ci
	 * @throws Exception 
	 */
	public void _addonce(CommandInterpreter ci) throws Exception{
				
		String command = getArgsAsString(ci);
		String[] queriesInput = command.split(";");
		
		QueryBuildParameter parameters = new QueryBuildParameter();
		parameters.set(new ParameterTransformationConfiguration(this.transformationConfiguration));		
		int count = queriesInput.length;
		for(int i=0;i<count;i++){			
			queriesInput[i] = queriesInput[i].trim();
		}		
		if(queriesInput[queriesInput.length-1].endsWith("<S>")){
			parameters.set(new ParameterDefaultRoot(new ConsoleSink()));
			queriesInput[queriesInput.length-1] = queriesInput[queriesInput.length-1].substring(0,queriesInput[queriesInput.length-1].length()-3).trim();
		}else if(queriesInput[queriesInput.length-1].endsWith("<E>")){
			parameters.set(new ParameterDefaultRoot(getEclipseConsoleOutput()));
			queriesInput[queriesInput.length-1] = queriesInput[queriesInput.length-1].substring(0,queriesInput[queriesInput.length-1].length()-3).trim();
		}
						
		for(int i=0;i<count;i++){			
			String queryString = queriesInput[i];
			queries.put(queryString, parameters);
		}		
		logger.info(count+" queries added");
	}
			
	public void _translate(CommandInterpreter ci) throws QueryParseException, Exception{
		logger.debug("Translating queries...");
		for(Entry<String, QueryBuildParameter> query: this.queries.entrySet()){
			logger.debug("Translating with "+getCurrentParser()+": \""+query.getKey()+"\"...");
			List<ILogicalOperator> tops = this.compiler.translateQuery(query.getKey(), getCurrentParser());
			logger.debug("Translating done.");
			for(ILogicalOperator top : tops){
				Query newOne = new Query(top, query.getValue());
				currentTranslatedPlans.add(newOne);
			}						
		}
		logger.debug("All queries translated");
		this.queries.clear();
	}
	
	public void _restruct(CommandInterpreter ci){
		logger.debug("Restructuring translated queries...");
		for(Query op: this.currentTranslatedPlans){						
			ILogicalOperator restructOp = this.compiler.restructPlan(op.getLogicalPlan());
			op.setLogicalPlan(restructOp);
			currentRestructuredPlans.add(op);
		}
		logger.debug("Restructuring done.");
		this.currentTranslatedPlans.clear();
	}
	
	public void _transform(CommandInterpreter ci) throws TransformationException, PlanManagementException{
		logger.debug("Transforming restructured queries...");
		
		for(Query top : this.currentRestructuredPlans){						
			IPhysicalOperator phyOp = this.compiler.transform(top.getLogicalPlan(), top.getBuildParameter().getTransformationConfiguration());
			top.initializePhysicalPlan(phyOp);
			this.currentTransformedPlans.add(top);
		}
		logger.debug("Transformation done.");
		this.currentRestructuredPlans.clear();
	}
	
	public void _execute(CommandInterpreter ci) throws PlanManagementException{		
		logger.debug("Executing transformed queries...");		
		for(Query physicalPlan : this.currentTransformedPlans){
			int id = this.executor.addQuery(physicalPlan);
			logger.debug("Query "+id+" executed");
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
	
	public void bindCompiler(ICompiler compiler){
		this.compiler = compiler;
		logger.debug("Compiler bound");
	}
	
	public void unbindCompiler(ICompiler compiler){
		this.compiler = null;
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
	
	private String getCurrentParser() throws Exception {
		if (this.currentParser != null && this.currentParser.length() > 0) {
			return currentParser;
		}

		Iterator<String> parsers = this.executor.getSupportedQueryParser()
				.iterator();
		if (parsers != null && parsers.hasNext()) {
			this.currentParser = parsers.next();
			return this.currentParser;
		}
		throw new Exception("No parser found");
	}
	
	
	private IPhysicalOperator getEclipseConsoleOutput() {
		try {

			Class<?> eclipseConsoleSink = Class
					.forName("de.uniol.inf.is.odysseus.broker.console.client.EclipseConsoleSink");
			Object ecs = eclipseConsoleSink.newInstance();
			IPhysicalOperator ecSink = (IPhysicalOperator) ecs;
			return ecSink;
		}catch(ClassNotFoundException e){
			logger.error("Eclipse Console Plugin is missing!");
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	
		
}
