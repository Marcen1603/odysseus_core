package de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.data;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * This class extends the swt.Composite and builds an Entry with a message (e.g.
 * warning or error) in the warningserrors-Dashboard-Part
 * 
 * @author MarkMilster
 * 
 */
public class Entry extends Composite {

	private int wka_id;
	private int farm_id;

	/**
	 * @return the wka_id
	 */
	public int getWka_id() {
		return wka_id;
	}

	/**
	 * @param wka_id
	 *            the wka_id to set
	 */
	public void setWka_id(int wka_id) {
		this.wka_id = wka_id;
		repaintValues();
	}

	/**
	 * @return the farm_id
	 */
	public int getFarm_id() {
		return farm_id;
	}

	/**
	 * @param farm_id
	 *            the farm_id to set
	 */
	public void setFarm_id(int farm_id) {
		this.farm_id = farm_id;
		repaintValues();
	}

	/**
	 * The value_type is the type of measurement which creates this message
	 * 
	 * @return the value_type as String
	 */
	public String getValue_type() {
		return value_type;
	}

	/**
	 * The value_type is the type of measurement which creates this message
	 * 
	 * @param value_type
	 *            the value_type to set as String
	 */
	public void setValue_type(String value_type) {
		this.value_type = value_type;
		repaintValues();
	}

	private String value_type;
	protected Text lblFarm_id;
	protected Text lblWka_id;
	protected Text lblValueType;

	/**
	 * Contructor to create a Entry in the specified parent Composite and with
	 * the specified swt-style
	 * 
	 * @param parent
	 *            The parent-Composite
	 * @param style
	 *            The swt-Style given as an int
	 */
	public Entry(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(3, false));

		lblFarm_id = new Text(this, SWT.NONE);
		lblWka_id = new Text(this, SWT.NONE);
		lblValueType = new Text(this, SWT.NONE);
		value_type = "";
	}

	/**
	 * This Method repaints all Values shown by this Entry
	 */
	private void repaintValues() {
		lblFarm_id.setText(String.valueOf(this.farm_id));
		;
		lblWka_id.setText(String.valueOf(this.wka_id));
		;
		lblValueType.setText(this.value_type);
		this.layout();
	}

}
