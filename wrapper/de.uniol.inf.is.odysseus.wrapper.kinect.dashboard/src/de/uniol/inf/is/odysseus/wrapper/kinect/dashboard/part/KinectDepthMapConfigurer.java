package de.uniol.inf.is.odysseus.wrapper.kinect.dashboard.part;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;

public class KinectDepthMapConfigurer extends AbstractDashboardPartConfigurer<KinectDepthMapDashboardPart> {

	private KinectDepthMapDashboardPart dashboardPart;

	@Override
	public void init(KinectDepthMapDashboardPart dashboardPartToConfigure, Collection<IPhysicalOperator> roots ) {
		dashboardPart = dashboardPartToConfigure;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(2, false));
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createFPSCounter(topComposite);
	}

	@Override
	public void dispose() {

	}

    private void createFPSCounter(Composite topComposite) {
        createLabel(topComposite, "Show FPS Counter");
        final Combo fpsCounterCombo = createBooleanComboDropDown(topComposite, dashboardPart.getFpsShow());
        fpsCounterCombo.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                dashboardPart.setFpsShow(fpsCounterCombo.getSelectionIndex() == 0);
                fireListener();
            }
        });
    }

	private static Combo createBooleanComboDropDown(Composite tableComposite, boolean isSetToTrue) {
		Combo comboDropDown = new Combo(tableComposite, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		comboDropDown.add("true");
		comboDropDown.add("false");
		comboDropDown.select(isSetToTrue ? 0 : 1);
		comboDropDown.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return comboDropDown;
	}

	private static void createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
	}
}
