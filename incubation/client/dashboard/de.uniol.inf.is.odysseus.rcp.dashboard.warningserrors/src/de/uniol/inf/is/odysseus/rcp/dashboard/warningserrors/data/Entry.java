package de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.data;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

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
	 * @param wka_id the wka_id to set
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
	 * @param farm_id the farm_id to set
	 */
	public void setFarm_id(int farm_id) {
		this.farm_id = farm_id;
		repaintValues();
	}

	/**
	 * @return the value_type
	 */
	public String getValue_type() {
		return value_type;
	}

	/**
	 * @param value_type the value_type to set
	 */
	public void setValue_type(String value_type) {
		this.value_type = value_type;
		repaintValues();
	}

	private String value_type;
	protected Text lblFarm_id;
	protected Text lblWka_id;
	protected Text lblValueType;

	public Entry(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		
		lblFarm_id = new Text(this, SWT.NONE);
		lblWka_id = new Text(this, SWT.NONE);
		lblValueType = new Text(this, SWT.NONE);
		value_type = "";
	}
	
	private void repaintValues() {
		lblFarm_id.setText(String.valueOf(this.farm_id));;
		lblWka_id.setText(String.valueOf(this.wka_id));;
		lblValueType.setText(this.value_type);
		this.layout();
	}
	
}
