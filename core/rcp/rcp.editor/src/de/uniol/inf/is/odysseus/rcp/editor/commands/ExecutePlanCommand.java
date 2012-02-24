/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.editor.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.OdysseusRCPEditorPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.editors.LogicalPlanEditor;
import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;
import de.uniol.inf.is.odysseus.rcp.windows.ExceptionWindow;


public class ExecutePlanCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		if (OdysseusRCPEditorPlugIn.getExecutor() == null) {
			System.out.println("AdvancedExecutor is null");
			return null;
		}

		// Aktiven Editor holen
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		try {
			if (editorPart instanceof LogicalPlanEditor) {
				LogicalPlanEditor part = (LogicalPlanEditor) editorPart;

				// Plan validieren
				if (!validatePlan(part.getOperatorPlan())) {
					System.out.println("Folgende Fehler sind im OperatorPlan:");
					for (String txt : errorTexts)
						System.out.println(txt);

					return null;
				}
				// Logischen Plan aufbauen
				buildLogicalPlan(part.getOperatorPlan(), OdysseusRCPPlugIn.getActiveSession());

			}
		} catch (Exception ex) {
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
	
	private void buildLogicalPlan(OperatorPlan plan, ISession user) {

		// oberste Operatoren finden
		List<Operator> sinks = new ArrayList<Operator>();
		for (Operator op : plan.getOperators()) {
			if (op.getConnectionsAsSource().size() == 0)
				sinks.add(op);
		}

		// für jede Senke den logischen Plan ausführen
		try {
			for (Operator sink : sinks) {
				printLogicalPlan(sink.getLogicalOperator(), 0);
				OdysseusRCPEditorPlugIn.getExecutor().addQuery(sink.getLogicalOperator(), user, "Standard");
			}
		} catch (PlanManagementException ex) {
			ex.printStackTrace();
		}
	}
	
	private void printLogicalPlan(ILogicalOperator op, int lvl) {
		for( int i = 0; i < lvl; i++ ) 
			System.out.print("\t");
		
		System.out.println(op);
		
		for( LogicalSubscription sub : op.getSubscribedToSource()) {
			printLogicalPlan(sub.getTarget(), lvl + 1 );
		}
	}
}
