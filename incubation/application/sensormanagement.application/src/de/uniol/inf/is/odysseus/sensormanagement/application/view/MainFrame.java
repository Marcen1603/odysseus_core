package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;

import de.uniol.inf.is.odysseus.sensormanagement.application.Application;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session.PresentationStyle;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.live.LiveSession;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.PlaybackScene;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.PlaybackSession;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.scene.Scene;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.Utilities;

@SuppressWarnings("serial")
public class MainFrame extends JFrame 
{
	private JTabbedPane sessionTabbedPane;
	private List<Session> sessions;
	private JMenuBar menuBar;
	
	private JFileChooser sceneFileChooser;
	
	/**
	 * Create the frame.
	 */
	public MainFrame() 
	{
		setTitle("Admin Client");
		addWindowListener(	new WindowAdapter() 
							{ 
								public void windowClosing(WindowEvent arg0) { onClose(); }
							});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1077, 605);
		
		sceneFileChooser = new JFileChooser();
		sceneFileChooser.setFileFilter(new FileFilter() 
			{
				@Override public String getDescription()
				{ 
					return "Scenario files (*.xml)"; 
				}
				
				@Override public boolean accept(File file) 
				{
					return file.isDirectory() || FilenameUtils.getExtension(file.getName()).equals("xml");
				}
			});		
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenu startLiveSessionMenu = new JMenu("Start live session");
		fileMenu.add(startLiveSessionMenu);
		
		JMenuItem liveSessionNewSceneMenu = new JMenuItem("New scene");
		startLiveSessionMenu.add(liveSessionNewSceneMenu);
		
		JMenuItem mntmFromExistingScene = new JMenuItem("From existing scene...");
		mntmFromExistingScene.addActionListener(new ActionListener() 
												{
													public void actionPerformed(ActionEvent arg0) { startLiveSessionFromSceneMenuItemClick(); }
												});
		startLiveSessionMenu.add(mntmFromExistingScene);
		liveSessionNewSceneMenu.addActionListener(new ActionListener() 
												{
													public void actionPerformed(ActionEvent e) { startLiveSessionMenuItemClick(); }
												});
		
		JMenu startPlaybackSessionMenu = new JMenu("Start playback session");
		fileMenu.add(startPlaybackSessionMenu);
		
		JMenuItem playbackSessionFromSceneMenu = new JMenuItem("From scene...");
		startPlaybackSessionMenu.add(playbackSessionFromSceneMenu);
		
		JMenuItem playbackSessionFromDirectoryMenu = new JMenuItem("From directory...");
		startPlaybackSessionMenu.add(playbackSessionFromDirectoryMenu);
		playbackSessionFromSceneMenu.addActionListener(	new ActionListener() 
											{
												public void actionPerformed(ActionEvent arg0) { startPlaybackSessionMenuClick(); }
											});
		
		JMenuItem stopCurrentSessionMenu = new JMenuItem("Stop current session");
		stopCurrentSessionMenu.addActionListener(new ActionListener() 
												{
													public void actionPerformed(ActionEvent e) { stopCurrentSessionMenuItemClick(); }
												});
		fileMenu.add(stopCurrentSessionMenu);
		
		JSeparator separator = new JSeparator();
		fileMenu.add(separator);
		
		JMenuItem closeMenu = new JMenuItem("Close");
		closeMenu.addActionListener(new ActionListener() 
									{
										public void actionPerformed(ActionEvent e) { closeMenuItemClick(); }
									});
		fileMenu.add(closeMenu);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		sessionTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		contentPane.add(sessionTabbedPane, BorderLayout.CENTER);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setPreferredSize(new Dimension(13, 23));
		toolBar.setMinimumSize(new Dimension(13, 23));
		toolBar.setMaximumSize(new Dimension(32767, 23));
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton startRecordingButton = new JButton("Start recording");
		startRecordingButton.addActionListener(	new ActionListener() 
												{
													public void actionPerformed(ActionEvent arg0) { getCurrentSession().startLoggingButtonClick(); }
												});
		toolBar.add(startRecordingButton);
		
		JButton stopRecordingButton = new JButton("Stop recording");
		stopRecordingButton.addActionListener(	new ActionListener() 
												{
													public void actionPerformed(ActionEvent arg0) { getCurrentSession().stopLoggingButtonClick(); }
												});		
		toolBar.add(stopRecordingButton);
		
		JButton startLiveViewButton = new JButton("Start view");
		toolBar.add(startLiveViewButton);
		
		JButton stopLiveViewButton = new JButton("Stop view");
		toolBar.add(stopLiveViewButton);
		
		JButton btnNewButton = new JButton("Start/Pause playback");
		btnNewButton.addActionListener(	new ActionListener() 
										{
											public void actionPerformed(ActionEvent arg0) { getCurrentSession().debugBtn(0); }
										});
		toolBar.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Rewind playback");
		btnNewButton_1.addActionListener(	new ActionListener() 
											{
												public void actionPerformed(ActionEvent arg0) { getCurrentSession().debugBtn(1); }
											});
		toolBar.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Change session presentation");
		btnNewButton_2.addActionListener(	new ActionListener() 
											{
												public void actionPerformed(ActionEvent arg0) { changeSessionPresentation(); }
											});
		
		JButton btnSingleStep = new JButton("Single step");
		btnSingleStep.addActionListener(new ActionListener() 
										{
											public void actionPerformed(ActionEvent arg0) { getCurrentSession().debugBtn(2); }
										});
		toolBar.add(btnSingleStep);
		toolBar.add(btnNewButton_2);
		
		JButton btnCopyTimestamp = new JButton("Copy timestamp");
		btnCopyTimestamp.addActionListener(	new ActionListener() 
											{
												public void actionPerformed(ActionEvent arg0) { copyTimeStamp(); }
											});
		toolBar.add(btnCopyTimestamp);
		
		JButton btnNewButton_3 = new JButton("Toggle fullscreen");
		btnNewButton_3.addActionListener(	new ActionListener() 
											{
												public void actionPerformed(ActionEvent arg0) { toggleFullScreen(); }
											});
		toolBar.add(btnNewButton_3);
		stopLiveViewButton.addActionListener(new ActionListener() 
											{
												public void actionPerformed(ActionEvent arg0) { getCurrentSession().stopLiveViewBtnClick(); }
											});
		startLiveViewButton.addActionListener(new ActionListener() 
											 {
												 public void actionPerformed(ActionEvent arg0) { getCurrentSession().startLiveViewBtnClick(); }
											 });
	}

	protected void toggleFullScreen() 
	{
		boolean visible = !menuBar.isVisible();		
		menuBar.setVisible(visible);
		
		Session s = getCurrentSession();
		if (s != null) s.toggleFullscreen(!visible);
	}

	protected void copyTimeStamp() 
	{
		Session s = getCurrentSession();
		
		if (s instanceof PlaybackSession)
		{
			PlaybackSession ps = (PlaybackSession) s;
		
			String string = Double.toString(ps.getNow()) + " " + Utilities.stringFromDoubleTime(ps.getNow());
			StringSelection stringSelection = new StringSelection(string);
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(stringSelection, null);		
		}
	}

	protected void changeSessionPresentation() 
	{
		Session s = getCurrentSession();
		
		if (s.getPresentationStyle() == PresentationStyle.TABBED)
			s.setPresentationStyle(PresentationStyle.TILED);
		else
			s.setPresentationStyle(PresentationStyle.TABBED);
	}

	protected void stopCurrentSessionMenuItemClick() 
	{
		Session s = getCurrentSession();
		if (s != null)
		{
			removeSession(s);
			s.remove();			
		}
	}

	protected void startLiveSessionMenuItemClick() 
	{
		startLiveSession(new Scene(null, "Live Session", null));
	}

	protected void startLiveSessionFromSceneMenuItemClick() 
	{
		try 
		{
			if (sceneFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
				startLiveSession(Scene.fromFile(sceneFileChooser.getSelectedFile()));
		} catch (IOException e) {
			Application.showException(e);
		}
	}
	
	private void startLiveSession(Scene scene)
	{
		LiveSession liveSession = new LiveSession(scene);
		try
		{
			liveSession.addSensorBox("192.168.1.101:9669");
			liveSession.addSensorBox("192.168.1.102:9669");
			liveSession.addSensorBox("192.168.1.103:9669");
			liveSession.addSensorBox("192.168.1.104:9669");
			
			liveSession.addSensorBox("192.168.1.2:9669");
			liveSession.addSensorBox("192.168.1.3:9669");
			liveSession.addSensorBox("127.0.0.1:9669");
			
			liveSession.addSensorBox("192.168.178.27:9669");
			
			addSession(liveSession);
		}
		catch (Exception e)
		{
			liveSession.remove();
			Application.showException(e);
		}		
	}
	
	protected void closeMenuItemClick() 
	{
        setVisible(false);
        dispose();
    }
	
	protected void startPlaybackSessionMenuClick() 
	{
		int returnVal = sceneFileChooser.showOpenDialog(this);		
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			try 
			{			
				PlaybackScene scene = (PlaybackScene) Scene.fromFile(sceneFileChooser.getSelectedFile());
			
				PlaybackSession playbackSession = new PlaybackSession(scene);
				Application.getMainFrame().addSession(playbackSession);
			}
			catch (Exception e)
			{
				Application.showException(e);
			}							
		}
	}
	     
	public Session getCurrentSession()
	{
		int idx = sessionTabbedPane.getSelectedIndex();
		if (idx >= 0)
			return sessions.get(idx);
		else
			return null;
	}

	public void addSession(Session session)
	{
		sessionTabbedPane.addTab(session.getSessionName(), session);
		sessionTabbedPane.setSelectedComponent(session);
		sessions.add(session);
	}
	
	public void removeSession(Session session)
	{
		session.getParent().remove(session);
		sessions.remove(session);
	}
	
	public void onCreate()
	{			
		sessions = new ArrayList<>();
	}
			
	private void onClose() 
	{
		for (Session session : sessions)
			session.remove();
	}		

	public JTabbedPane getSessionTabbedPane() 
	{
		return sessionTabbedPane;
	}
}
