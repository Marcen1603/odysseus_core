package de.uniol.inf.is.odysseus.rcp.editor.script.blocks;

import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.editor.script.service.ServicesBinder;

public class MetadataVisualOdysseusScriptBlock implements IVisualOdysseusScriptBlock {

	private final String[] metadataCombinations;
	
	private String[] selectedMetadataNames;
	private int selectionIndex;

	public MetadataVisualOdysseusScriptBlock(List<String> metadataNames) {
		Preconditions.checkNotNull(metadataNames, "metadataNames must not be null!");

		selectedMetadataNames = metadataNames.toArray(new String[0]);
		metadataCombinations = determineMetadataCombinationNames();
		selectionIndex = determineIndex(selectedMetadataNames, metadataCombinations);
		if( selectionIndex == 0 ) {
			selectedMetadataNames = metadataCombinations[0].split(",");
		}
	}

	public MetadataVisualOdysseusScriptBlock() {
		this(Lists.newArrayList());
	}

	@Override
	public String getTitle() {
		return "Metadata";
	}

	@Override
	public void createPartControl(Composite parent, IVisualOdysseusScriptContainer container) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(3, true));
		
		Composite leftComp = new Composite( comp, SWT.NONE);
		GridData leftGridData = new GridData(GridData.FILL_HORIZONTAL);
		leftGridData.heightHint = 1;
		leftComp.setLayoutData(leftGridData);

		Combo availMetadataCombo = new Combo(comp, SWT.DROP_DOWN | SWT.READ_ONLY);
		availMetadataCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		availMetadataCombo.setItems(metadataCombinations);
		availMetadataCombo.select(selectionIndex);
		availMetadataCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( availMetadataCombo.getSelectionIndex() != selectionIndex) {
					selectionIndex = availMetadataCombo.getSelectionIndex();
					selectedMetadataNames = availMetadataCombo.getItem(selectionIndex).split(",");
		
					container.setDirty(true);
				}
			}
		});
		
		Composite rightComp = new Composite( comp, SWT.NONE);
		GridData rightGridData = new GridData(GridData.FILL_HORIZONTAL);
		rightGridData.heightHint = 1;
		rightComp.setLayoutData(rightGridData);
	}

	private static String[] determineMetadataCombinationNames() {
		Set<String> availableMetadataCombinations = ServicesBinder.getExecutor().getMetadataNames(OdysseusRCPPlugIn.getActiveSession());
		return availableMetadataCombinations.toArray(new String[0]);
//		List<String> metadataTitles = Lists.newArrayList();
//
//		for (SortedSet<String> combination : availableMetadataCombinations) {
//			
//			List<String> metadataNames = Lists.newArrayList();
//			String[] combinationStringArray = combination.toArray(new String[0]);
//			for (int i = 0; i < combinationStringArray.length; i++) {
//				IMetaAttribute type = TempMetadataRegistry.getMetadataType(combinationStringArray[i]);
//				String attributeName = type.getName();
//				String[] attributeNameParts = attributeName.split(",");
//				for( String attributeNamePart : attributeNameParts ) {
//					if( !metadataNames.contains(attributeNamePart) ) {
//						metadataNames.add(attributeNamePart);
//					}
//				}
//			}
//
//			StringBuilder nameBuilder = new StringBuilder();
//			for( int i = 0; i < metadataNames.size(); i++ ) {
//				if( i > 0 ) {
//					nameBuilder.append(",");
//				}
//				
//				nameBuilder.append(metadataNames.get(i));
//			}
//			metadataTitles.add(nameBuilder.toString());
//		}
//
//		return metadataTitles.toArray(new String[0]);
	}

	private static int determineIndex(String[] arrayToFind, String[] combinations) {
		for (int i = 0; i < combinations.length; i++) {
			String combination = combinations[i];
			
			String[] parts = combination.split(",");
			if( parts.length != arrayToFind.length ) {
				continue;
			}
			
			boolean isOK = true;
			for (String toFind : arrayToFind) {
				if (!combination.contains(toFind)) {
					isOK = false;
					break;
				}
			}

			if (isOK) {
				return i;
			}
		}
		return 0; // select first item as fallback
	}

	@Override
	public void dispose() {
		// do nothing
	}

	@Override
	public String generateOdysseusScript() throws VisualOdysseusScriptException {
		StringBuilder sb = new StringBuilder();
		
		for( String selectedMetadataName : selectedMetadataNames ) {
			if( sb.length() > 0 ) {
				sb.append("\n");
			}
			
			sb.append("#METADATA ").append(selectedMetadataName);
		}
		
		return sb.toString();
	}

}
