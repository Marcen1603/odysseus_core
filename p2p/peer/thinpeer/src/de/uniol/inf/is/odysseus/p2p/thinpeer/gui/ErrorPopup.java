/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.p2p.thinpeer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ErrorPopup extends javax.swing.JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton closePopup;
	private JLabel error;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ErrorPopup inst = new ErrorPopup("test");
				inst.setVisible(true);
			}
		});
	}

	public ErrorPopup(String error) {
		super(new JFrame());
		initGUI(error);
	}

	private void initGUI(String errorText) {
		try {
			{
				getContentPane().setLayout(null);
				{
					closePopup = new JButton();
					getContentPane().add(closePopup);
					closePopup.setText("closePopup");
					closePopup.setBounds(153, 228, 80, 22);
					closePopup.addActionListener(this);
					closePopup.setActionCommand("closePopup");
				}
				{
					error = new JLabel();
					getContentPane().add(error);
					error.setText(errorText);
					error.setBounds(29, 21, 335, 67);
					error.setFont(new java.awt.Font("AlArabiya", 0, 14));
				}
			}
			this.setSize(398, 300);
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("closePopup".equals(e.getActionCommand())) {
			this.setVisible(false);
		}

	}

}
