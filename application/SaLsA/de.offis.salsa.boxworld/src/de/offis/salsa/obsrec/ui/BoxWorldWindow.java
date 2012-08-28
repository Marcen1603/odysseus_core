package de.offis.salsa.obsrec.ui;

import java.util.concurrent.TimeUnit;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.core.animation.timing.TimingTarget;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import de.offis.salsa.obsrec.ObjWorldListener;
import de.offis.salsa.obsrec.Objectworld;

public class BoxWorldWindow implements ObjWorldListener, TimingTarget {

	private Objectworld objWorld;
	private BoxWorldPanel widget;
	private BoxWorld boxworld;
	
	private Animator animator;
	
	public BoxWorldWindow(Objectworld objWorld) {
		this.objWorld = objWorld;
		
		initComponents();		
		initGfxTimer();
	}
	
	private void initComponents(){
		this.widget = new BoxWorldPanel(objWorld);
		this.boxworld = new BoxWorld();
		this.boxworld.setVisible(true);
		
		this.boxworld.setLmsPanelContent(this.widget);
		
		this.objWorld.addObjectWorldListener(this);
		
		this.boxworld.setScanSegmentations(objWorld.getRegisteredSegmenter());
	}
	
	private void initGfxTimer(){
		TimingSource ts = new SwingTimerTimingSource();
		ts.init();
		
		Animator.setDefaultTimingSource(ts);		
		animator = new Animator.Builder().setDuration(30, TimeUnit.MILLISECONDS).setRepeatCount(Animator.INFINITE).addTarget(this).build();
		animator.start();
	}

	@Override
	public void onChange() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin(Animator source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void end(Animator source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void repeat(Animator source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reverse(Animator source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void timingEvent(Animator source, double fraction) {
		widget.repaint();
	}
}
