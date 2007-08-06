package mg.dynaquest.sourcedescription.rdftree;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class RdfTreeFrame_AboutBoxPanel1 extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7445266761696967282L;

	/**
	 * @uml.property  name="border"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Border border = BorderFactory.createEtchedBorder();

	/**
	 * @uml.property  name="layoutMain"
	 */
	private GridBagLayout layoutMain = new GridBagLayout();

	/**
	 * @uml.property  name="labelCompany"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JLabel labelCompany = new JLabel();

	/**
	 * @uml.property  name="labelCopyright"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JLabel labelCopyright = new JLabel();

	/**
	 * @uml.property  name="labelAuthor"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JLabel labelAuthor = new JLabel();

	/**
	 * @uml.property  name="labelTitle"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JLabel labelTitle = new JLabel();

	public RdfTreeFrame_AboutBoxPanel1() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.setLayout(layoutMain);
		this.setBorder(border);
		labelTitle.setText("Title");
		labelAuthor.setText("Author");
		labelCopyright.setText("Copyright");
		labelCompany.setText("Company");
		this.add(labelTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						15, 0, 15), 0, 0));
		this.add(labelAuthor, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						15, 0, 15), 0, 0));
		this.add(labelCopyright, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						15, 0, 15), 0, 0));
		this.add(labelCompany, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						15, 5, 15), 0, 0));
	}
}