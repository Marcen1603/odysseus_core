package windscadaanwendung.hd.ae;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class AEEntryComp extends Composite {

	protected Text lblFarm_id;
	protected Text lblWka_id;
	protected Text lblValueType;
	private Text time;
	private Button btnConfirm;
	private Text comment;
	private AEEntry aeEntry;

	public AEEntryComp(Composite parent, int style, AEEntry aeEntry) {
		super(parent, style);
		setLayout(new GridLayout(6, false));
		
		this.aeEntry = aeEntry;
		
		time = new Text(this, SWT.NONE);
		time.setText(aeEntry.getTimestamp());
		
		lblFarm_id = new Text(this, SWT.NONE);
		lblFarm_id.setText(String.valueOf(aeEntry.getFarmId()));
		
		lblWka_id = new Text(this, SWT.NONE);
		lblWka_id.setText(String.valueOf(aeEntry.getWkaId()));
		
		lblValueType = new Text(this, SWT.NONE);
		lblValueType.setText(aeEntry.getValueType());
		
		btnConfirm = new Button(this, SWT.CHECK);
		btnConfirm.setSelection(aeEntry.isConfirm());
		
		comment = new Text(this, SWT.NONE);
		comment.setText(aeEntry.getComment());
	}

	/**
	 * @return the aeEntry
	 */
	public AEEntry getAeEntry() {
		return aeEntry;
	}
	
}
