/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    Experimenter.java
 *    Copyright (C) 1999 University of Waikato, Hamilton, New Zealand
 *
 */

package weka.gui.experiment;

import weka.core.Memory;
import weka.experiment.Experiment;
import weka.gui.LookAndFeel;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/** 
 * The main class for the experiment environment. Lets the user create,
 * open, save, configure, run experiments, and analyse experimental results.
 *
 * @author Len Trigg (trigg@cs.waikato.ac.nz)
 * @version $Revision: 7193 $
 */
public class Experimenter
  extends JPanel {

  /** for serialization */
  private static final long serialVersionUID = -5751617505738193788L;

  /** The panel for configuring the experiment */
  protected SetupModePanel m_SetupPanel;

  /** The panel for running the experiment */
  protected RunPanel m_RunPanel;

  /** The panel for analysing experimental results */
  protected ResultsPanel m_ResultsPanel;

  /** The tabbed pane that controls which sub-pane we are working with */
  protected JTabbedPane m_TabbedPane = new JTabbedPane();

  /** True if the class attribute is the first attribute for all
      datasets involved in this experiment. */
  protected boolean m_ClassFirst = false;

  /**
   * Creates the experiment environment gui with no initial experiment
   */
  public Experimenter(boolean classFirst) {

	  System.out.println("[DEBUGGER] ---- " + Messages.getInstance().getString("Experimenter_TabbedPane_Setup_Key_Text"));
	  
    m_SetupPanel = new SetupModePanel();
    m_ResultsPanel = new ResultsPanel();
    m_RunPanel = new RunPanel();
    m_RunPanel.setResultsPanel(m_ResultsPanel);
    
    m_ClassFirst = classFirst;

    m_TabbedPane.addTab(Messages.getInstance().getString("Experimenter_TabbedPane_Setup_Key_Text"), null, m_SetupPanel, Messages.getInstance().getString("Experimenter_TabbedPane_Setup_Value_Text"));
    m_TabbedPane.addTab(Messages.getInstance().getString("Experimenter_TabbedPane_Run_Key_Text"), null, m_RunPanel, Messages.getInstance().getString("Experimenter_TabbedPane_Run_Value_Text"));
    m_TabbedPane.addTab(Messages.getInstance().getString("Experimenter_TabbedPane_Analyse_Key_Text"), null, m_ResultsPanel, Messages.getInstance().getString("Experimenter_TabbedPane_Analyse_Value_Text"));
    m_TabbedPane.setSelectedIndex(0);
    m_TabbedPane.setEnabledAt(1, false);
    m_SetupPanel.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
	//System.err.println("Updated experiment");
	Experiment exp = m_SetupPanel.getExperiment();
	exp.classFirst(m_ClassFirst);
	m_RunPanel.setExperiment(exp);
	//m_ResultsPanel.setExperiment(exp);
	m_TabbedPane.setEnabledAt(1, true);
      }
    });
    setLayout(new BorderLayout());
    add(m_TabbedPane, BorderLayout.CENTER);
  }


  /** variable for the Experimenter class which would be set to null by the memory 
      monitoring thread to free up some memory if we running out of memory
   */
  private static Experimenter m_experimenter;

  /** for monitoring the Memory consumption */
  private static Memory m_Memory = new Memory(true);

  /**
   * Tests out the experiment environment.
   *
   * @param args ignored.
   */
  public static void main(String [] args) {
    weka.core.logging.Logger.log(weka.core.logging.Logger.Level.INFO, Messages.getInstance().getString("Experimenter_Main_LoggingStarted_Text"));    
    LookAndFeel.setLookAndFeel();
    
    try {
      // uncomment to disable the memory management:
      //m_Memory.setEnabled(false);

      boolean classFirst = false;
      if (args.length > 0) {
	classFirst = args[0].equals("CLASS_FIRST");
      }
      m_experimenter = new Experimenter(classFirst);
      final JFrame jf = new JFrame(Messages.getInstance().getString("Experimenter_Main_WekaExperimentEnvironment_JFrame_Text"));
      jf.getContentPane().setLayout(new BorderLayout());
      jf.getContentPane().add(m_experimenter, BorderLayout.CENTER);
      jf.addWindowListener(new WindowAdapter() {
	public void windowClosing(WindowEvent e) {
	  jf.dispose();
	  System.exit(0);
	}
      });
      jf.pack();
      jf.setSize(800, 600);
      jf.setVisible(true);
      
      Image icon = Toolkit.getDefaultToolkit().
        getImage(m_experimenter.getClass().getClassLoader().getResource("weka/gui/weka_icon_new_48.png"));
      jf.setIconImage(icon);

      Thread memMonitor = new Thread() {
        public void run() {
          while(true) {
            try {
              this.sleep(4000);
              
              System.gc();

              if (m_Memory.isOutOfMemory()) {
                // clean up
                jf.dispose();
                m_experimenter = null;
                System.gc();

                // stop threads
                m_Memory.stopThreads();

                // display error
                System.err.println(Messages.getInstance().getString("Experimenter_Main_Error_Text_First"));
                m_Memory.showOutOfMemory();
                System.err.println(Messages.getInstance().getString("Experimenter_Main_Error_Text_Second"));
                System.exit(-1);
              }

            } catch(InterruptedException ex) { ex.printStackTrace(); }
          }
        }
      };

      memMonitor.setPriority(Thread.NORM_PRIORITY);
      memMonitor.start();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.err.println(ex.getMessage());
    }
  }
}
