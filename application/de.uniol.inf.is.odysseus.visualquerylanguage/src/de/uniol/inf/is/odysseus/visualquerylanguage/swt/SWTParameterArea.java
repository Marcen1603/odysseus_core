package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultPipeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSinkContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSourceContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParam;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamConstruct;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamSetter;

public class SWTParameterArea {

	private final Logger log = LoggerFactory.getLogger(SWTParameterArea.class);

	private Composite comp;
	private INodeView<INodeContent> nodeView;

	public SWTParameterArea(Composite parent,
			final INodeView<INodeContent> nodeView) {

		this.nodeView = nodeView;
		INodeContent content = nodeView.getModelNode().getContent();

		comp = new Composite(parent, SWT.BORDER);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 3;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		comp.setLayout(gridLayout);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		comp.setLayoutData(data);

		Label nameLabel = new Label(comp, SWT.LEFT);
		if (content instanceof DefaultSourceContent) {
			nameLabel.setText("Sourcename: " + content.getName());
		} else if (content instanceof DefaultSinkContent) {
			nameLabel.setText("Sinkname: " + content.getName());
		} else if (content instanceof DefaultPipeContent) {
			nameLabel.setText("Pipename: " + content.getName());
		}

		if (content.getOperator().getOutputSchema() != null
				&& !content.getOperator().getOutputSchema().isEmpty()) {
			Label typLabel = new Label(comp, SWT.LEFT);
			String outPut = "";
			for (SDFAttribute att : content.getOperator().getOutputSchema()) {
				if (outPut.isEmpty()) {
					outPut = att.toString();
				} else {
					outPut += ", " + att.toString();
				}
			}
			typLabel.setText("Ausgabeschema: " + outPut);

		}

		if (!content.isOnlySource()
				&& (!content.getConstructParameterList().isEmpty() || !content
						.getSetterParameterList().isEmpty())) {

			for (IParamConstruct<?> cParam : content
					.getConstructParameterList()) {
				final ActualComp cComp = new ActualComp(comp, SWT.NONE);
				GridLayout gl = new GridLayout(3, false);
				cComp.setLayout(gl);
				GridData gd = new GridData(GridData.FILL_BOTH);
				cComp.setLayoutData(gd);

				Button cButton = new Button(cComp, SWT.PUSH);
				cButton.setText("Editor");
				GridData buttonGridData = new GridData();
				cButton.setLayoutData(buttonGridData);
				cButton.setEnabled(false);
				Text t = new Text(cComp, SWT.SINGLE);
				GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);
				t.setLayoutData(textGridData);
				if (EditorChecker.getInstance().hasPredicateEditor(
						cParam.getEditor())) {
					cButton.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(SelectionEvent e) {
							Collection<SDFAttributeList> inputs = new ArrayList<SDFAttributeList>();
							for (LogicalSubscription subscription : nodeView
									.getModelNode().getContent().getOperator()
									.getSubscribedTo()) {
								inputs.add(subscription.getTarget()
										.getOutputSchema());
							}
							SWTPredicateEditor paramEditor = new SWTPredicateEditor(
									SWTMainWindow.getShell(), nodeView
											.getModelNode().getContent(),
									inputs, cComp);
							paramEditor.addSWTParameterListener(cComp);
						}
					});
					cButton.setEnabled(true);
				} else if (EditorChecker.getInstance().hasWindowEditor(
						cParam.getEditor())) {
					cButton.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(SelectionEvent e) {
							SWTWindowEditor windowEditor = new SWTWindowEditor(SWTMainWindow.getShell());
							windowEditor.addSWTParameterListener(cComp);
						}
					});
					cButton.setEnabled(true);
				} else if(EditorChecker.getInstance().hasGroupingEditor(cParam.getEditor())) {
					cButton.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(SelectionEvent e) {
							Collection<SDFAttributeList> inputs = new ArrayList<SDFAttributeList>();
							for (LogicalSubscription subscription : nodeView
									.getModelNode().getContent().getOperator()
									.getSubscribedTo()) {
								inputs.add(subscription.getTarget()
										.getOutputSchema());
							}
							SWTGroupingEditor paramEditor = new SWTGroupingEditor(
									SWTMainWindow.getShell(), inputs);
							paramEditor.addSWTParameterListener(cComp);
						}
					});
					cButton.setEnabled(true);
				}else if(EditorChecker.getInstance().hasAggregateEditor(cParam.getEditor())) {
					cButton.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(SelectionEvent e) {
							Collection<SDFAttributeList> inputs = new ArrayList<SDFAttributeList>();
							for (LogicalSubscription subscription : nodeView
									.getModelNode().getContent().getOperator()
									.getSubscribedTo()) {
								inputs.add(subscription.getTarget()
										.getOutputSchema());
							}
							SWTAggregateEditor paramEditor = new SWTAggregateEditor(
									SWTMainWindow.getShell(), inputs);
							paramEditor.addSWTParameterListener(cComp);
						}
					});
					cButton.setEnabled(true);
				}
				String value = "";
				if (cParam.getValue() != null) {
					value = cParam.getValue().toString();
				}
				t.setText(value);
				t.setData(cParam);
			}

			for (IParamSetter<?> sParam : content.getSetterParameterList()) {
				final ActualComp sComp = new ActualComp(comp, SWT.NONE);
				GridLayout gl = new GridLayout(3, false);
				sComp.setLayout(gl);
				GridData gd = new GridData(GridData.FILL_BOTH);
				sComp.setLayoutData(gd);

				Label l = new Label(sComp, SWT.NONE);
				l.setText(sParam.getSetter());
				GridData labelGridData = new GridData();
				labelGridData.widthHint = 120;
				l.setLayoutData(labelGridData);

				Button sButton = new Button(sComp, SWT.PUSH);
				sButton.setText("Editor");
				GridData buttonGridData = new GridData();
				sButton.setLayoutData(buttonGridData);
				sButton.setEnabled(false);
				Text t = new Text(sComp, SWT.SINGLE);
				GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);
				t.setLayoutData(textGridData);
				String value = "";
				if (sParam.getValue() != null) {
					value = sParam.getValue().toString();
				}
				t.setText(value);
				t.setData(sParam);
				if (EditorChecker.getInstance().hasPredicateEditor(
						sParam.getEditor())) {
					sButton.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(SelectionEvent e) {
							Collection<SDFAttributeList> inputs = new ArrayList<SDFAttributeList>();
							for (LogicalSubscription subscription : nodeView
									.getModelNode().getContent().getOperator()
									.getSubscribedTo()) {
								inputs.add(subscription.getTarget()
										.getOutputSchema());
							}
							SWTPredicateEditor paramEditor = new SWTPredicateEditor(
									SWTMainWindow.getShell(), nodeView
											.getModelNode().getContent(), inputs,
									sComp);
							paramEditor.addSWTParameterListener(sComp);
						}
					});
					sButton.setEnabled(true);
					t.setEditable(false);
					Color c = new Color(Display.getCurrent(), 255, 255, 255);
					t.setBackground(c);
				} else if(EditorChecker.getInstance().hasWindowEditor(sParam.getEditor())) {
					sButton.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(SelectionEvent e) {
							SWTWindowEditor paramEditor = new SWTWindowEditor(
									SWTMainWindow.getShell());
							paramEditor.addSWTParameterListener(sComp);
						}
					});
					sButton.setEnabled(true);
					t.setEditable(false);
					Color c = new Color(Display.getCurrent(), 255, 255, 255);
					t.setBackground(c);
				} else if(EditorChecker.getInstance().hasGroupingEditor(sParam.getEditor())) {
					sButton.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(SelectionEvent e) {
							Collection<SDFAttributeList> inputs = new ArrayList<SDFAttributeList>();
							for (LogicalSubscription subscription : nodeView
									.getModelNode().getContent().getOperator()
									.getSubscribedTo()) {
								inputs.add(subscription.getTarget()
										.getOutputSchema());
							}
							SWTGroupingEditor paramEditor = new SWTGroupingEditor(
									SWTMainWindow.getShell(), inputs);
							paramEditor.addSWTParameterListener(sComp);
						}
					});
					sButton.setEnabled(true);
				}else if(EditorChecker.getInstance().hasAggregateEditor(sParam.getEditor())) {
					sButton.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(SelectionEvent e) {
							Collection<SDFAttributeList> inputs = new ArrayList<SDFAttributeList>();
							for (LogicalSubscription subscription : nodeView
									.getModelNode().getContent().getOperator()
									.getSubscribedTo()) {
								inputs.add(subscription.getTarget()
										.getOutputSchema());
							}
							SWTAggregateEditor paramEditor = new SWTAggregateEditor(
									SWTMainWindow.getShell(), inputs);
							paramEditor.addSWTParameterListener(sComp);
						}
					});
					sButton.setEnabled(true);
				} else {
					t.addModifyListener(new ModifyListener() {

						@SuppressWarnings("unchecked")
						@Override
						public void modifyText(ModifyEvent e) {
							((IParam) ((Text) e.getSource()).getData())
									.setValue(((Text) e.getSource()).getText());
						}
					});
				}
			}
		}
	}

	public void dispose() {
		this.comp.dispose();

		log.debug("ParameterArea for " + nodeView + " disposed");
	}

	private class ActualComp extends Composite implements ISWTParameterListener {

		public ActualComp(Composite parent, int style) {
			super(parent, style);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setValue(Object value) {
			Text t = null;
			for (Control c : this.getChildren()) {
				if (c instanceof Text) {
					t = (Text) c;
				}
			}
			t.setText(value.toString());
			if (t.getData() instanceof IParam) {
				((IParam) t.getData()).setValue(value);
			}
		}

	}

}
