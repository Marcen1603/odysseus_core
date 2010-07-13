package de.uniol.inf.is.odysseus.rcp.editor.editorpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;

public class MyEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart arg0, Object object) {
		if( object instanceof OperatorPlan ) {
			return new OperatorPlanEditPart((OperatorPlan)object);
		}
		if (object instanceof Operator) {
			return new OperatorEditPart((Operator) object);
		} else {
			assert(false);
			return null;
		}
	}

}
