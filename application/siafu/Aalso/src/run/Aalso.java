/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package run;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Opens the control window to start (and stop) a specific amount of simulations
 * 
 * @author Jan-Benno Meyer zu Holte
 *
 */
public class Aalso {

	public static void main(String[] args){
		AalsoDialog dialogWindow = new AalsoDialog();
		dialogWindow.setBounds(10, 10, 420, 180);
		dialogWindow.setVisible(true);
	}
}

/**
 * The Window containing the fields to start (and stop) simulations
 * 
 * @author Jan-Benno Meyer zu Holte
 *
 */
class AalsoDialog extends JFrame {
	
	private static final long serialVersionUID = 1L;
	JLabel numOfSimLabel;
	JTextField numOfSimulations;
	JTextArea infoField;
	JButton startButton;
	JButton stopButton;
	JCheckBox useGuiCheckBox;
	
	ArrayList<Process> simulations;
	
	/**
	 * Opens the window and initializes the global variables
	 */
	public AalsoDialog() {
				
		try {
			// Lösche Inhalt der Datei mit den CQL Creates
			FileOutputStream out = new FileOutputStream("OdysCreates.aalso");
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		this.getContentPane().setLayout(null);
		
		simulations = new ArrayList<Process>();
		
		this.initWindow();
		
		this.addWindowListener(new WindowListener() {

			// ... (einige Methoden hier weggelassen, sie Quellcode unten)

			@Override
			public void windowClosing(WindowEvent e) {
				int counter = 0;
				// Close opened simulations if existing
				for(Process tempProc : simulations){
					try {
						tempProc.destroy();
						String[] info = infoField.getText().split("\n");
						if(info.length >= 5){
							infoField.setText(info[1]+"\n"+info[2]+"\n"+info[3]+"\n"+info[4] + "\n Simulation "+ counter +" stopped");
						} else {
							infoField.setText(infoField.getText() + "\n Simulation "+ counter +" stopped");
						}
						TimeUnit.SECONDS.sleep(1);
						counter++;
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
				try {
					// Delete the content of the file with the CQL Creates
					FileOutputStream out = new FileOutputStream("OdysCreates.aalso");
					out.close();
				} catch(IOException ex) {
					ex.printStackTrace();
				}
				System.exit(0);
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			// ...
		});	
	}

	/**
	 * initializes the window's items and functions (eventLsitener)
	 */
	protected void initWindow() {
		numOfSimLabel = new JLabel("Anzahl der Simulationen:");
		numOfSimulations = new JTextField();
		useGuiCheckBox = new JCheckBox("Use GUI ", false);
		infoField = new JTextArea();
		startButton = new JButton("Start");
		stopButton = new JButton("Stop");
		
		numOfSimLabel.setBounds(5,10,150,25);
		numOfSimulations.setBounds(155,10,50,25);
		useGuiCheckBox.setBounds(210, 10, 150, 25);
		infoField.setBounds(110,40,285,80);
		startButton.setBounds(5,40,100,30);
		stopButton.setBounds(5,80,100,30);
		
		stopButton.setEnabled(false);
		infoField.setEditable(false);
		
		startButton.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {
				buttonStartSimulations();
			}
		});
		
		stopButton.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {
				buttonStopSimulations();
			}
		});
		
		// Elemente dem Fenster hinzufügen:
		this.getContentPane().add(numOfSimLabel);
		this.getContentPane().add(numOfSimulations);
		this.getContentPane().add(useGuiCheckBox);
		this.getContentPane().add(infoField);
		this.getContentPane().add(startButton);
		this.getContentPane().add(stopButton);
		
		this.pack();
	}
	
	/**
	 * Actions performed on stop button click
	 */
	protected void buttonStopSimulations() {
		try {
			// Lösche Inhalt der Datei mit den CQL Creates
			FileOutputStream out = new FileOutputStream("OdysCreates.aalso");
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		int counter = 1;
		// Close all running simulations
		for(Process tempProc : simulations){
			try {
				tempProc.destroy();
				String[] info = infoField.getText().split("\n");
				if(info.length >= 5){
					infoField.setText(info[1]+"\n"+info[2]+"\n"+info[3]+"\n"+info[4] + "\n Simulation "+ counter +" stopped");
				} else {
					infoField.setText(infoField.getText() + "\n Simulation "+ counter +" stopped");
				}
				Thread.sleep(1000);
				counter++;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		stopButton.setEnabled(false);
		startButton.setEnabled(true);
		simulations.clear();
	}

	/**
	 * Actions performed on start button click
	 */
	public void buttonStartSimulations() {
		int numOfSim = 0;
		try {
			numOfSim = Integer.parseInt(numOfSimulations.getText());
			String configPath = "configs/IDEAAL/gui/config";
			String simulationPath = "Simulations/IDEAAL.jar";
			/* Start the simulations either with gui or without
			 * It is recommended to always use the java Runtime.exec() method even though you can't stop the simulations afterwars.
			 * A bug in the jdk that is declared "will not fix" prevents setting the heap space for JAR-File processes. this means
			 * that some simulations wont work porperly if created by the ProcessBuilder!
			 *
			 */
			if(useGuiCheckBox.isSelected()){
				// Open simulations using the ProcessBuilder (working code)
//				for(int i = 1; i <= numOfSim; i++){
//					String curConfig = configPath + i + ".xml";
//					ProcessBuilder pb = new ProcessBuilder("java", "-jar", "-Xms100m", "-Xmx100m" , "Siafu.jar", "-c=" + curConfig, " -s=" + simulationPath);
//					try {
//						Process process = pb.start();
//						pb.redirectErrorStream(true);
//						simulations.add(process);
//						String[] info = infoField.getText().split("\n");
//						if(info.length >= 5){
//							infoField.setText(info[1]+"\n"+info[2]+"\n"+info[3]+"\n"+info[4] + "\n Simulation "+ i +" started");
//						} else {
//							infoField.setText(infoField.getText() + "\n Simulation "+ i +" started");
//						}
//						Thread.sleep(1000);
//					} catch (Exception e) {
//						System.out.println(e.getMessage());
//					}
//				}
//				startButton.setEnabled(false);
//				stopButton.setEnabled(true);
				// Open simulations using the runtime.exec command
				configPath = "configs/IDEAAL/gui/config";
				for(int i = 1; i <= numOfSim; i++){
					String curConfig = configPath + i + ".xml";
					String cmd = "java -Xms100m -Xmx100m -jar \"Siafu.jar\" -c=\"" + curConfig + "\" -s=\"" + simulationPath + "\"";
			        try {
			            Runtime.getRuntime().exec(cmd);
			            Thread.sleep(1000);
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
				}
				System.exit(0);
			} else {
				configPath = "configs/IDEAAL/nongui/config";
				for(int i = 1; i <= numOfSim; i++){
					String curConfig = configPath + i + ".xml";
					String cmd = "java -Xms100m -Xmx100m -jar \"Siafu.jar\" -c=\"" + curConfig + "\" -s=\"" + simulationPath + "\"";
			        try {
			            Runtime.getRuntime().exec(cmd);
			            Thread.sleep(1000);
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
				}
				System.exit(0);
			}
		} catch (NumberFormatException e) {
			
		}
	}

}
