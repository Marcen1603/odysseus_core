package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;


public class DashboardKeyHandler implements KeyListener {

	private final Dashboard dashboard;
	
	public DashboardKeyHandler( Dashboard dashboard ) {
		// Preconditions.checkNotNull(dashboard, "Dashboard for key handling must not be null!");
		
		this.dashboard = dashboard;
		this.dashboard.getControl().addKeyListener(this);
	}
	
	public void dispose() {
		this.dashboard.getControl().removeKeyListener(this);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		Optional<DashboardPartPlacement> optSelectedPart = dashboard.getSelectedDashboardPartPlacement();
		if( optSelectedPart.isPresent() ) {
			if( isCtrlPressed(e) && e.keyCode == SWT.DEL) {
				DashboardPartPlacement selectedPart = optSelectedPart.get();
				dashboard.remove(selectedPart);
				
				dashboard.update();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// do nothing here
	}

	private static boolean isCtrlPressed(KeyEvent e) {
		return (e.stateMask & SWT.CTRL ) != 0;
	}
}
