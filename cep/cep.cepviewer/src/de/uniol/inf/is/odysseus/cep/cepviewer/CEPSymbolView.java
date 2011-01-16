package de.uniol.inf.is.odysseus.cep.cepviewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;

/**
 * This class defines the view which shows the SymbolTable of the current
 * selected Instance.
 * 
 * @author Christian
 */
public class CEPSymbolView extends ViewPart {

	// the ID of this view
	public static final String ID = "de.uniol.inf.is.odysseus.cep.cepviewer.symboltableview";

	// the widget which holds the informations
	private Text text;

	/**
	 * This is the constructor.
	 */
	public CEPSymbolView() {
		super();
	}

	/**
	 * This method clears the view.
	 */
	public void clear() {
		this.text.setText("");
	}

	/**
	 * This method creates the CEPSymbolView by creating the Text widget.
	 * 
	 * @param parent
	 *            is the widget which contains the query view.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		this.text = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL);
		this.text.setEditable(false);
		this.text.setBackground(parent.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
	}

	/**
	 * This method displays the information of the given instance in the view.
	 * 
	 * @param instance
	 *            is a CEPInstanz holding the StateMachine
	 */
	public void setContent(CEPInstance instance) {
		String output = "";
		for (CepVariable key : instance.getInstance().getSymTab().getKeys()) {
			output = output.concat(key.getVariableName()).concat(
					StringConst.EQUALS_SIGN);
				output += instance.getInstance().getSymTab().getValue(key);
				output = output.concat(StringConst.BREAK);
		}
		this.text.setText(output);
	}

	/**
	 * This method is called to set the focus to this view.
	 */
	public void setFocus() {
		// do nothing
	}

}
