package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultPipeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSinkContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSourceContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParam;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamConstruct;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamSetter;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.tabs.DefaultGraphArea;
import de.uniol.inf.is.odysseus.visualquerylanguage.validation.ObjectFactory;

public class SWTParameterArea implements ISWTParameterListener {

	private final Logger log = LoggerFactory.getLogger(SWTParameterArea.class);

	private Composite comp;
	private INodeView<INodeContent> nodeView;

	private Table table = null;
	private TableEditor editor = null;

	private DefaultGraphArea area;

	public SWTParameterArea(final DefaultGraphArea area, Composite parent,
			final INodeView<INodeContent> nodeView) {

		this.area = area;
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

		Label typLabel = new Label(comp, SWT.LEFT);
		typLabel.setText("Typ: " + content.getType());

		if (!content.isOnlySource()
				&& (!content.getConstructParameterList().isEmpty() || !content
						.getSetterParameterList().isEmpty())) {
			table = new Table(comp, SWT.BORDER);
			TableColumn tc1 = new TableColumn(table, SWT.LEFT);
			TableColumn tc2 = new TableColumn(table, SWT.LEFT);
			tc1.setText("Name");
			tc2.setText("Wert");
			tc1.setWidth(70);
			tc2.setWidth(70);
		}

		if (table != null) {
			editor = new TableEditor(table);
			editor.horizontalAlignment = SWT.LEFT;
			editor.grabHorizontal = true;
			table.addListener(SWT.MouseDown, this);
			final int EDITABLECOLUMN = 1;

			table.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					// Clean up any previous editor control
					Control oldEditor = editor.getEditor();
					if (oldEditor != null)
						oldEditor.dispose();

					// Identify the selected row
					TableItem item = (TableItem) e.item;
					if (item == null)
						return;

					// The control that will be the editor must be a child of
					// the Table
					Text newEditor = new Text(table, SWT.NONE);
					newEditor.setText(item.getText(EDITABLECOLUMN));
					newEditor.addModifyListener(new ModifyListener() {
						public void modifyText(ModifyEvent me) {
							Text text = (Text) editor.getEditor();
							editor.getItem().setText(EDITABLECOLUMN,
									text.getText());
						}
					});
					newEditor.selectAll();
					newEditor.setFocus();
					editor.setEditor(newEditor, item, EDITABLECOLUMN);
				}
			});

			table.setHeaderVisible(true);

			TableItem pcItem;
			TableItem scItem;

			for (IParamConstruct<?> cParam : content
					.getConstructParameterList()) {
				pcItem = new TableItem(table, SWT.NONE);
				String value = "";
				if (cParam.getValue() != null) {
					value = cParam.getValue().toString();
				}
				pcItem.setText(new String[] { cParam.getName(), value });
				pcItem.setData(cParam);
			}

			for (IParamSetter<?> sParam : content.getSetterParameterList()) {
				scItem = new TableItem(table, SWT.NONE);
				String value = "";
				if (sParam.getValue() != null) {
					value = sParam.getValue().toString();
				}
				scItem.setText(new String[] { sParam.getName(), value });
				scItem.setData(sParam);
			}
		}
	}

	public void dispose() {
		this.comp.dispose();

		log.debug("ParameterArea for " + nodeView + " disposed");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(TableItem item, Object value) {
		if (item != null) {
			if (item.getData() instanceof IParam<?>) {
				try {
					((IParam) item.getData()).setValue(value);
					item.setText(1, value.toString());
					final Text text = new Text(table, SWT.NONE);
					text.setText(value.toString());
					editor.setEditor(text, item, 1);
					editor.layout();
					repaint();
				} catch (Exception e) {
					log.error("Wrong Value for Paramtype.");
					area.getStatusLine().setErrorText(
							"Falsche Werte eingegeben.");
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleEvent(Event event) {
		Rectangle clientArea = table.getClientArea();
		Point pt = new Point(event.x, event.y);
		int index = table.getTopIndex();
		while (index < table.getItemCount()) {
			boolean visible = false;
			final TableItem item = table.getItem(index);
			Rectangle rect = item.getBounds(1);
			if (rect.contains(pt)) {
				final int column = 1;
				final Text text = new Text(table, SWT.NONE);
				Listener textListener = new Listener() {
					public void handleEvent(final Event e) {
							Object obj = null;
							switch (e.type) {
							case SWT.FocusOut:
								obj = ObjectFactory.getInstance().getParamType(
										text.getText(),
										((IParam<?>) (editor.getItem()
												.getData())).getType());
								if (obj != null) {
									IParam<?> data = null;
									if (editor.getItem().getData() instanceof IParam<?>) {
										data = (IParam<?>) editor.getItem().getData();
									}
									if (data != null && !data.hasEditor()) {
										table.setEnabled(true);
									try {
										((IParam) (editor.getItem().getData()))
												.setValue(obj);
									} catch (Exception e2) {
										log.error("Wrong Value for Paramtype.");
										area.getStatusLine().setErrorText(
												"Falsche Werte eingegeben.");
									}
								}
								}
								if (obj == null) {
									text.dispose();
									e.doit = false;
								} else {
									item.setText(column, text.getText());
								}
								text.dispose();
								break;
							case SWT.Traverse:
								switch (e.detail) {
								case SWT.TRAVERSE_RETURN:
									if (editor.getItem().getData() instanceof IParam<?>) {
										obj = ObjectFactory.getInstance()
												.getParamType(
														text.getText(),
														((IParam<?>) (editor
																.getItem()
																.getData()))
																.getType());
										if (obj instanceof Integer) {
											((IParam<Integer>) (editor
													.getItem().getData()))
													.setValue((Integer) obj);
										}
										if (obj instanceof String) {
											((IParam<String>) (editor.getItem()
													.getData()))
													.setValue((String) obj);
										}
									}
									if (obj == null) {
										text.dispose();
										e.doit = false;
									} else {
										item.setText(column, text.getText());
									}
									// FALL THROUGH
								case SWT.TRAVERSE_ESCAPE:
									text.dispose();
									e.doit = false;
								}
								break;
							}
						}
				};
				text.addListener(SWT.FocusOut, textListener);
				text.addListener(SWT.Traverse, textListener);
				editor.setEditor(text, item, 1);
				if (editor.getItem().getData() instanceof IParam<?>) {
					if (((IParam) editor.getItem().getData()).hasEditor()) {
						Collection<SDFAttributeList> inputs = new ArrayList<SDFAttributeList>();
						for (LogicalSubscription subscription : nodeView
								.getModelNode().getContent().getOperator()
								.getSubscribedTo()) {
							inputs.add(subscription.getTarget()
									.getOutputSchema());
						}
						SWTParameterEditor paramEditor = new SWTParameterEditor(
								SWTMainWindow.getShell(), nodeView
										.getModelNode().getContent(), inputs,
								item);
						paramEditor.addSWTParameterListener(this);
					}
				}
				text.setText(item.getText(1));
				text.selectAll();
				text.setFocus();
				return;
			}
			if (!visible && rect.intersects(clientArea)) {
				visible = true;
			}
			if (!visible)
				return;
			index++;
		}
	}
	
	private void repaint() {
		Collection<INodeView<INodeContent>> selected = area.getRenderer().getSelector().getSelected();
		area.getRenderer().getSelector().unselectAll();
		area.getRenderer().getSelector().select(selected);
	}
}
