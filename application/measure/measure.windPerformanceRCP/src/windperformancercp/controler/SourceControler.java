package windperformancercp.controler;

import java.util.ArrayList;

import windperformancercp.model.AttributeTable;
import windperformancercp.model.ISource;
import windperformancercp.model.SourceTable;
import windperformancercp.views.AttributeDialog;
import windperformancercp.views.SourceDialog;

public class SourceControler implements IControler {

	IControler _control;
	ArrayList<ISource> sources;
	AttributeDialog attDialog;
	AttributeTable attTable;
	SourceDialog srcDialog;
	SourceTable srcTable;
	//ManageSourceView msrcView;
	
	public SourceControler (IControler control){
		this._control = control;
		sources = new ArrayList<ISource>();
	}
}
