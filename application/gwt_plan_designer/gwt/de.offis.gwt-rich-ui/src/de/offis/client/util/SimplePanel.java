package de.offis.client.util;

import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Kopie des Standard SimplePanel aus GWT 2.1.1 bei dem lediglich die lokale
 * Variable "widget" auf nicht-privat geändert wurde
 * (vorher private Widget widget;). Dies ist noetig um darauf aufbauend die
 * Klasse SimpleLayoutPanel aufzubauen. Beide Klassen koennen entfernt werden
 * sobald das GWT direkte unterstuetzung fuer ein SimpleLayoutPanel bietet.
 * Beide Klassen wurden sowieso schon aus dem GWT SVN kopiert da dort ein
 * betreffender Patch eingereicht wurde.
 */
public class SimplePanel extends Panel implements HasOneWidget {

  Widget widget;

  /**
   * Creates an empty panel that uses a DIV for its contents.
   */
  public SimplePanel() {
    this(DOM.createDiv());
  }

  /**
   * Creates an empty panel that uses the specified browser element for its
   * contents.
   *
   * @param elem the browser element to use
   */
  protected SimplePanel(Element elem) {
    setElement(elem);
  }

  /**
   * Adds a widget to this panel.
   *
   * @param w the child widget to be added
   */
  @Override
  public void add(Widget w) {
    // Can't add() more than one widget to a SimplePanel.
    if (getWidget() != null) {
      throw new IllegalStateException("SimplePanel can only contain one child widget");
    }
    setWidget(w);
  }

  /**
   * Gets the panel's child widget.
   *
   * @return the child widget, or <code>null</code> if none is present
   */
  public Widget getWidget() {
    return widget;
  }

  public Iterator<Widget> iterator() {
    // Return a simple iterator that enumerates the 0 or 1 elements in this
    // panel.
    return new Iterator<Widget>() {
      boolean hasElement = widget != null;
      Widget returned = null;

      public boolean hasNext() {
        return hasElement;
      }

      public Widget next() {
        if (!hasElement || (widget == null)) {
          throw new NoSuchElementException();
        }
        hasElement = false;
        return (returned = widget);
      }

      public void remove() {
        if (returned != null) {
          SimplePanel.this.remove(returned);
        }
      }
    };
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
      getContainerElement().removeChild(w.getElement());

      // Logical detach.
      widget = null;
    }
    return true;
  }

  public void setWidget(IsWidget w) {
    setWidget(asWidgetOrNull(w));
  }

  /**
   * Sets this panel's widget. Any existing child widget will be removed.
   *
   * @param w the panel's new widget, or <code>null</code> to clear the panel
   */
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
      DOM.appendChild(getContainerElement(), widget.getElement());

      adopt(w);
    }
  }

  /**
   * Override this method to specify that an element other than the root element
   * be the container for the panel's child widget. This can be useful when you
   * want to create a simple panel that decorates its contents.
   *
   * Note that this method continues to return the
   * {@link com.google.gwt.user.client.Element} class defined in the
   * <code>User</code> module to maintain backwards compatibility.
   *
   * @return the element to be used as the panel's container
   */
  protected com.google.gwt.user.client.Element getContainerElement() {
    return getElement();
  }
}
