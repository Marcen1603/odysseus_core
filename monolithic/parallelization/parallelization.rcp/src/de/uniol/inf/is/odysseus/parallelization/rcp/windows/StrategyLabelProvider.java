package de.uniol.inf.is.odysseus.parallelization.rcp.windows;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class StrategyLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		StrategySelectionRow row = (StrategySelectionRow) element;
        switch (columnIndex) {
            case 0 :
            	result = row.getLogicalOperator().getClass().getSimpleName();
                break;
            case 1 :
            	result = row.getLogicalOperator().getUniqueIdentifier();
                break;
            case 2 :
            	result = row.getEndOperatorId();
                break;
            case 3 :
            	result = row.getCustomDegrees();
                break;
            case 4 :
                result = row.getStrategy().getName();
                break;
            case 5 :
                result = row.getFragmentType().getSimpleName();
                break;
            }
        return result;
	}


}
