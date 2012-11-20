package de.offis.client.util;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;


/**
 * Siehe "eigene" Implementierung von SimplePanel. erweitert das SimplePanel
 * insofern das es mit den ganzen LayoutPanels zusammenarbeitet und
 * Größenänderungen weiterreicht.
 *
 * A simple panel that {@link ProvidesResize} to its one child.
 */
public class SimpleLayoutPanel extends SimplePanel implements RequiresResize,
    ProvidesResize {

  private Layer layer;
  private final Layout layout;

  public SimpleLayoutPanel() {
    layout = new Layout(getElement());
  }

  public void onResize() {
    if (widget instanceof RequiresResize) {
      ((RequiresResize) widget).onResize();
    }
  }

  @Override
  public boolean remove(Widget w) {
    // Validate.
    if (widget != w) {
      return false;
    }

    // Orphan.
    try {
      orphan(w);
    } finally {
      // Physical detach.
      layout.removeChild(layer);
      layer = null;

      // Logical detach.
      widget = null;
    }
    return true;
  }

  @Override
  public void setWidget(Widget w) {
    // Validate
    if (w == widget) {
      return;
    }

    // Detach new child.
    if (w != null) {
      w.removeFromParent();
    }

    // Remove old child.
    if (widget != null) {
      remove(widget);
    }

    // Logical attach.
    widget = w;

    if (w != null) {
      // Physical attach.
      layer = layout.attachChild(widget.getElement(), widget);
      layer.setTopHeight(0.0, Unit.PX, 100.0, Unit.PCT);
      layer.setLeftWidth(0.0, Unit.PX, 100.0, Unit.PCT);

      adopt(w);

      // Update the layout.
      layout.layout();
      onResize();
    }
  }

  @Override
  protected void onLoad() {
    layout.onAttach();
  }

  @Override
  protected void onUnload() {
    layout.onDetach();
  }
}