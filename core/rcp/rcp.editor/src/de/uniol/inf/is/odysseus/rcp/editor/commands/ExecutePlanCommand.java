package de.uniol.inf.is.odysseus.rcp.editor.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.rcp.editor.LogicalPlanEditorPart;
import de.uniol.inf.is.odysseus.rcp.editor.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.transformation.helper.relational.RelationalTransformationHelper;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class ExecutePlanCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		if (Activator.getExecutor() == null) {
			System.out.println("AdvancedExecutor is null");
			return null;
		}

		// Aktiven Editor holen
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		try {
			if (editorPart instanceof LogicalPlanEditorPart) {
				LogicalPlanEditorPart part = (LogicalPlanEditorPart) editorPart;

				// Plan validieren
				if (!validatePlan(part.getOperatorPlan())) {
					System.out.println("Folgende Fehler sind im OperatorPlan:");
					for (String txt : errorTexts)
						System.out.println(txt);

					return null;
				}
				// Logischen Plan aufbauen
				buildLogicalPlan(part.getOperatorPlan(), part.getUser());

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			new ExceptionWindow(ex);
		}

		return null;
	}

	private List<String> errorTexts = new ArrayList<String>();

	private boolean validatePlan(OperatorPlan operatorPlan) {
		errorTexts.clear();
		for (Operator op : operatorPlan.getOperators()) {
			if (!op.getOperatorBuilder().validate()) {
				List<Exception> exceptions = op.getOperatorBuilder().getErrors();

				// Text bauen
				for (Exception ex : exceptions) {
					errorTexts.add(op.getOperatorBuilderName() + ": " + ex.getMessage());
				}

			}
		}
		return errorTexts.size() == 0;
	}

	@SuppressWarnings("unchecked")
	private void buildLogicalPlan(OperatorPlan plan, User user) {

		// oberste Operatoren finden
		List<Operator> sinks = new ArrayList<Operator>();
		for (Operator op : plan.getOperators()) {
			if (op.getConnectionsAsSource().size() == 0)
				sinks.add(op);
		}

		// für jede Senke den logischen Plan ausführen
		try {
			for (Operator sink : sinks) {
				Activator.getExecutor().addQuery(sink.getLogicalOperator(), user,
						new ParameterTransformationConfiguration(new TransformationConfiguration(new RelationalTransformationHelper(), "relational", ITimeInterval.class)));
			}
		} catch (PlanManagementException ex) {
			ex.printStackTrace();
		}
	}
}
