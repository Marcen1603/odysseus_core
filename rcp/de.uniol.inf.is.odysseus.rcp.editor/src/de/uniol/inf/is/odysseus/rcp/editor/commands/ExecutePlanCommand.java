package de.uniol.inf.is.odysseus.rcp.editor.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.editor.LogicalPlanEditorPart;
import de.uniol.inf.is.odysseus.rcp.editor.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorConnection;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.transformation.helper.relational.RelationalTransformationHelper;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;

public class ExecutePlanCommand extends AbstractHandler implements IHandler {

	@SuppressWarnings("unchecked")
	private static ParameterTransformationConfiguration trafoConfigParam = 
		new ParameterTransformationConfiguration(
				new TransformationConfiguration(
						new RelationalTransformationHelper(), 
						"relational", 
						ITimeInterval.class));
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	
		if( Activator.getExecutor() == null ) {
			System.out.println("AdvancedExecutor is null");
			return null;
		}
		
		// Aktiven Editor holen
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		try {
			if( editorPart instanceof LogicalPlanEditorPart ) {
				LogicalPlanEditorPart part = (LogicalPlanEditorPart)editorPart;
				
				// Plan validieren
				if( !validatePlan( part.getOperatorPlan() ) ) {
					System.out.println("Folgende Fehler sind im OperatorPlan:");
					for( String txt : errorTexts ) 
						System.out.println(txt);
					
					return null;
				}
				// Logischen Plan aufbauen
				buildLogicalPlan( part.getOperatorPlan() );
				
			}
		} catch( Exception ex ) {
			ex.printStackTrace();
			new ExceptionWindow(ex);
		}
		
		return null;
	}
	
	private List<String> errorTexts = new ArrayList<String>();
	private List<Operator> operators = new ArrayList<Operator>();
	private List<ILogicalOperator> logicals = new ArrayList<ILogicalOperator>();
	private Map<Operator, ILogicalOperator> hashMap = new HashMap<Operator, ILogicalOperator>();

	private boolean validatePlan(OperatorPlan operatorPlan) {
		errorTexts.clear();
		for( Operator op : operatorPlan.getOperators() ) {
			if( !op.getOperatorBuilder().validate() ) {
				List<Exception> exceptions = op.getOperatorBuilder().getErrors();
				
				// Text bauen
				for( Exception ex : exceptions ) {
					errorTexts.add(op.getOperatorBuilderName() + ": " + ex.getMessage());
				}
				
			}
		}
		return errorTexts.size() == 0;
	}

	private void buildLogicalPlan( OperatorPlan plan ) {
		
		for( Operator op : plan.getOperators() ) 
			operators.add(op);
		
		Operator operator = findOperatorWithResolvedInputs(operators);
		while( operator != null ) {
			
			try {
				// Log operator bauen
				ILogicalOperator log = operator.getOperatorBuilder().createOperator();
				logicals.add(log);
				hashMap.put(operator, log);
				
				// und mit vorhandenen verbinden
				int sinkCounter = 0;
				for( OperatorConnection conn : operator.getConnectionsAsTarget()) {
					ILogicalOperator logOperator = hashMap.get(conn.getSource());
					if( logOperator == null ) {
						System.out.println("Fehler beim Bauen.. Source nicht gefunden");
						return;
					}
					
					log.subscribeToSource(logOperator, sinkCounter, log.getNumberOfInputs(), log.getOutputSchema());
					sinkCounter++;
				}
				
				operators.remove(operator);
				
			} catch( Exception ex ) {
				System.out.println("Fehler beim Bauen des Logischen Plans : ");
				ex.printStackTrace();
				return;
			}
			
			operator = findOperatorWithResolvedInputs(operators);
		}
		
		if( !operators.isEmpty() ) {
			System.out.println("Nach Abarbeitung ist die Liste nicht leer!");
			return;
		} 
		
		try {
			Activator.getExecutor().addQuery(logicals.get(logicals.size()-1), new User("TestUser"), trafoConfigParam);
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}
	
	private Operator findOperatorWithResolvedInputs( List<Operator> candidates ) {
		
		for( Operator op : candidates ) {
			
			if( op.getConnectionsAsTarget().size() == 0 ) 
				return op;
			else {
				
				boolean completelyResolved = true;
				for( OperatorConnection conn : op.getConnectionsAsTarget() ) {
					if( candidates.contains(conn.getSource()))  {
						completelyResolved = false;
						break;
					}
				}
				
				if( completelyResolved ) 
					return op;
			}
		}
		
		return null;
		
	}

}
