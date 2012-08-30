package de.offis.salsa.obsrec.ui;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.core.animation.timing.TimingTarget;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.offis.salsa.obsrec.ObjWorldListener;
import de.offis.salsa.obsrec.Objectworld;
import de.offis.salsa.obsrec.ui.events.ObjectRulesUserChangedEvent;
import de.offis.salsa.obsrec.ui.events.ScanSegmentationUserChangedEvent;

public class BoxWorldWindow implements ObjWorldListener, TimingTarget {

	private EventBus events = new EventBus();
	
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
		this.boxworld = new BoxWorld(events);
		this.boxworld.setVisible(true);
		
		this.boxworld.setLmsPanelContent(this.widget);
		
		this.objWorld.addObjectWorldListener(this);
		
		this.boxworld.setScanSegmentations(objWorld.getRegisteredSegmenter());
		
		ArrayList<String> objrules = objWorld.getRegisteredObjectRules();
		ArrayList<Boolean> states = new ArrayList<Boolean>();
		for(String s : objrules){
			states.add(Boolean.FALSE);
		}
		this.boxworld.setObjectRules(objrules, states);
		
		events.register(this);
	}
	
	private void initGfxTimer(){
		TimingSource ts = new SwingTimerTimingSource();
		ts.init();
		
		Animator.setDefaultTimingSource(ts);		
		animator = new Animator.Builder().setDuration(30, TimeUnit.MILLISECONDS).setRepeatCount(Animator.INFINITE).addTarget(this).build();
		animator.start();
	}
	
	@Subscribe
	public void handleEvent(ScanSegmentationUserChangedEvent event){
		objWorld.setActiveSegmenter(event.getScanSegmentation());
	}
	
	@Subscribe
	public void handleEvent(ObjectRulesUserChangedEvent event){
		objWorld.setActiveObjectRules(event.getActive());
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
