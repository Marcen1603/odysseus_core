package windscadaanwendung.hd.ae;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * This class is the GUI of a AEEntry. It extends a SWT-Composite.
 * 
 * @author MarkMilster
 * 
 */
public class AEEntryComp extends Composite {

	protected Text lblFarm_id;
	protected Text lblWka_id;
	protected Text lblValueType;
	private Text time;
	private Button btnConfirm;
	private Text comment;
	public AEEntry aeEntry;
	private boolean changed = false;

	/**
	 * Flag if there where changes made in the GUI
	 * 
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}

	/**
	 * Set this if you change the GUI, e.g. insert something into the textfield.
	 * 
	 * @param changed
	 *            the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/**
	 * 
	 * @param parent
	 *            The parent Composite
	 * @param style
	 *            The style
	 * @param aeEntry
	 *            The AEEntry with the data which should be shown by this GUI
	 */
	public AEEntryComp(Composite parent, int style, final AEEntry aeEntry) {
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
		btnConfirm.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				aeEntry.setConfirm(btnConfirm.getSelection());
				setChanged(true);
			}

		});

		comment = new Text(this, SWT.NONE);
		comment.setText(aeEntry.getComment());
		comment.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				aeEntry.setComment(comment.getText());
				setChanged(true);
			}

		});
	}

	/**
	 * The data which are shown by this GUI
	 * 
	 * @return the aeEntry
	 */
	public AEEntry getAeEntry() {
		return aeEntry;
	}

}
