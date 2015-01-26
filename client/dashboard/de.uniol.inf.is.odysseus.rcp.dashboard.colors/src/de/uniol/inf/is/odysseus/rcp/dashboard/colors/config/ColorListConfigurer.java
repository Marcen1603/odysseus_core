package de.uniol.inf.is.odysseus.rcp.dashboard.colors.config;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.colors.parts.ColorListDashboardPart;

/**
 * The Configurer builds the GUI to configure a specific Dashboard-Part
 * 
 * @author MarkMilster
 * 
 */
public class ColorListConfigurer extends
		AbstractDashboardPartConfigurer<ColorListDashboardPart> {

	private ColorListDashboardPart dashboardPart;

	@Override
	public void init(ColorListDashboardPart dashboardPartToConfigure,
			Collection<IPhysicalOperator> roots) {
		dashboardPart = dashboardPartToConfigure;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(2, false));
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createMaxDataControls(topComposite);
		createUpdateIntervalControls(topComposite);
		createTimestampControls(topComposite);
		createFixedTimestampControls(topComposite);
		createMovingTimestampControls(topComposite);
		createTimestampTextControls(topComposite);
		createColorSizeControls(topComposite);
		createLineSpaceControls(topComposite);
		createShowHeartbeatsControls(topComposite);
		createWarningAndErrorControls(topComposite);
		createRGBIndexControls(topComposite);
	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createRGBIndexControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Red Index");
		final Text redIndex = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getRedIndex()));
		redIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		redIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setRedIndex(Integer.parseInt(redIndex.getText()));
				fireListener();
			}
		});
		DashboardPartUtil.createLabel(topComposite, "Green Index");
		final Text greenIndex = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getGreenIndex()));
		greenIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		greenIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setGreenIndex(Integer.parseInt(greenIndex
						.getText()));
				fireListener();
			}
		});
		DashboardPartUtil.createLabel(topComposite, "Blue Index");
		final Text blueIndex = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getBlueIndex()));
		blueIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		blueIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setBlueIndex(Integer.parseInt(blueIndex.getText()));
				fireListener();
			}
		});
	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createWarningAndErrorControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Show Warnings");
		final Combo comboWarnings = createBooleanComboDropDown(topComposite,
				dashboardPart.isShowWarnings());
		comboWarnings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setShowHeartbeats(comboWarnings
						.getSelectionIndex() == 0);
				fireListener();
			}
		});
		DashboardPartUtil.createLabel(topComposite, "Show Errors");
		final Combo comboErrors = createBooleanComboDropDown(topComposite,
				dashboardPart.isShowErrors());
		comboErrors.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setShowHeartbeats(comboErrors.getSelectionIndex() == 0);
				fireListener();
			}
		});

		DashboardPartUtil.createLabel(topComposite, "Warningindex");
		final Text warningIndex = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getWarningIndex()));
		warningIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		warningIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setWarningIndex(Integer.parseInt(warningIndex
						.getText()));
				fireListener();
			}
		});
		DashboardPartUtil.createLabel(topComposite, "Errorindex");
		final Text errorIndex = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getErrorIndex()));
		errorIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		errorIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setErrorIndex(Integer.parseInt(errorIndex
						.getText()));
				fireListener();
			}
		});
	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createLineSpaceControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Line Space");
		final Text lineSpaceText = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getLineSpace()));
		lineSpaceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lineSpaceText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setLineSpace(Integer.valueOf(lineSpaceText
						.getText()));
				fireListener();
			}
		});
	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createTimestampTextControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Timestamp Textsize");
		final Text TimestampTextsizeText = DashboardPartUtil.createText(
				topComposite,
				String.valueOf(dashboardPart.getTimestampTextSize()));
		TimestampTextsizeText.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		TimestampTextsizeText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setTimestampTextSize(Integer
						.valueOf(TimestampTextsizeText.getText()));
				fireListener();
			}
		});
	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createColorSizeControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Color width");
		final Text colorWidthText = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getColorWidth()));
		colorWidthText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		colorWidthText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setColorWidth(Integer.valueOf(colorWidthText
						.getText()));
				fireListener();
			}
		});

		DashboardPartUtil.createLabel(topComposite, "Color height");
		final Text colorHeightText = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getColorHeight()));
		colorHeightText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		colorHeightText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setColorWidth(Integer.valueOf(colorHeightText
						.getText()));
				fireListener();
			}
		});
	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createMovingTimestampControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite,
				"Distance Moving Timestamps");
		final Text distanceMovingTimestampsText = DashboardPartUtil.createText(
				topComposite,
				String.valueOf(dashboardPart.getMovingTimestampDiffElements()));
		distanceMovingTimestampsText.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		distanceMovingTimestampsText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setMovingTimestampDiffElements(Long
						.valueOf(distanceMovingTimestampsText.getText()));
				fireListener();
			}
		});
	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createFixedTimestampControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite,
				"Difference Fixed Timestamps");
		final Text diffFixedTimestampsText = DashboardPartUtil.createText(
				topComposite, String.valueOf(dashboardPart
						.getFixedTimestampDiffMilliseconds()));
		diffFixedTimestampsText.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		diffFixedTimestampsText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setFixedTimestampDiffMilliseconds(Long
						.valueOf(diffFixedTimestampsText.getText()));
				fireListener();
			}
		});
	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createTimestampControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Timestamp Index");
		final Text timestampIndexText = DashboardPartUtil
				.createText(topComposite,
						String.valueOf(dashboardPart.getTimestampIndex()));
		timestampIndexText
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		timestampIndexText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setTimestampIndex(Integer
						.valueOf(timestampIndexText.getText()));
				fireListener();
			}
		});

		DashboardPartUtil.createLabel(topComposite, "Fixed Timestamps");
		final Combo comboLocked = createBooleanComboDropDown(topComposite,
				dashboardPart.isFixedTimestamps());
		comboLocked.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setFixedTimestamps(comboLocked
						.getSelectionIndex() == 0);
				fireListener();
			}
		});
	}

	@Override
	public void dispose() {

	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createMaxDataControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Max Elements");
		final Text maxElementsText = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getMaxElements()));
		maxElementsText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		maxElementsText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setMaxElements(Integer.valueOf(maxElementsText
						.getText()));
				fireListener();
			}
		});

		DashboardPartUtil.createLabel(topComposite, "Max Elements per Line");
		final Text maxElementsLineText = DashboardPartUtil.createText(
				topComposite,
				String.valueOf(dashboardPart.getMaxElementsLine()));
		maxElementsLineText
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		maxElementsLineText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setMaxElementsLine(Integer
						.valueOf(maxElementsLineText.getText()));
				fireListener();
			}
		});
	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createUpdateIntervalControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Update interval (ms)");
		final Text attributesInput = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getUpdateInterval()));
		attributesInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		attributesInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setUpdateInterval(Long.valueOf(attributesInput
						.getText()));
				fireListener();
			}
		});
	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createShowHeartbeatsControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Show Heartbeats");
		final Combo comboLocked = createBooleanComboDropDown(topComposite,
				dashboardPart.isShowHeartbeats());
		comboLocked.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setShowHeartbeats(comboLocked.getSelectionIndex() == 0);
				fireListener();
			}
		});
	}

	/**
	 * Creates a Combo with boolean values as used in other Configurers
	 * 
	 * @param tableComposite
	 * @param isSetToTrue
	 * @return
	 */
	private static Combo createBooleanComboDropDown(Composite tableComposite,
			boolean isSetToTrue) {
		Combo comboDropDown = new Combo(tableComposite, SWT.DROP_DOWN
				| SWT.BORDER | SWT.READ_ONLY);
		comboDropDown.add("true");
		comboDropDown.add("false");
		comboDropDown.select(isSetToTrue ? 0 : 1);
		comboDropDown.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return comboDropDown;
	}

}
