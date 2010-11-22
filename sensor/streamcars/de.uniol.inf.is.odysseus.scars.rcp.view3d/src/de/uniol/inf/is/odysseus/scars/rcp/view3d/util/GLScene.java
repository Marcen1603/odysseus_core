package de.uniol.inf.is.odysseus.scars.rcp.view3d.util;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.glu.GLU;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Stellt die Grundfunktionalitäten für die Verwendung von OpenGL zur Verfügung.
 * Nutzer sollten für die eingene Anwendung von dieser Klasse ableiten und die
 * zu erweiternden Methoden überschreiben:
 * <p>
 * <code>init()</code> für die Initialisierung von OpenGL.
 * <code>resizeScene()</code> für Einstellungen nach einer Größenänderung des
 * GLCanvas( z.B. Fenstergröße). <code>draw()</code> Methode zum konkreten
 * Zeichnen mittels OpenGL.
 * <p>
 * Diese Klasse und dessen Ableitungen sollten mit der Klasse
 * <code>GLRefresher</code> verwendet werden.
 * 
 * @author Timo Michelsen
 * 
 */
public class GLScene {

	private GLCanvas canvas;
	private GLContext glContext;
	private Composite parent;

	/**
	 * Konstruktor. Erstellt eine neue <code>GLScene</code>-Instanz. Es wird ein
	 * Canvas für die grafische Ausgabe von OpenGL erzeugt und dem angegebenen
	 * SWT-Steuerelement hinzugefügt. Anschließend wird <code>init()</code>
	 * aufgerufen, um erste Einstellungen bzgl. OpenGL vorzunehmen.
	 * 
	 * @param parent
	 *            Übergeordnetes Steuerelement (SWT), worin das GLCanvas
	 *            hinzugefügt wird. Darf nicht null sein.
	 */
	public GLScene(Composite parent) {
		setParent(parent);
		init();
	}

	/**
	 * Initialisiert OpenGL. Hintergrundfarbe wird auf schwarz gesetzt.
	 * Einstellungen werden für eine 3D-Darstellungen vorgenommen.
	 * <code>Init()</code> wird vom Konstruktor der GLScene-Klasse aufgerufen.
	 * <p>
	 * Nutzer können diese Funktion überschreiben um andere/zusätzliche
	 * Einstellungen bei der Initialisierung vorzunehmen.
	 */
	protected void init() {
		GL gl = getGLContext().getGL();

		gl.glClearColor(0, 0, 0, 1);
		gl.glClearDepth(1);
		gl.glDepthFunc(GL.GL_LESS);

		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		resizeScene();
	}

	/**
	 * Wird aufgerufen, wenn sich das GLCanvas in der Größe ändert. Das kann zum
	 * Bespiel dann geschehen, wenn auf Vollbild geschaltet wird oder die
	 * Fenstergröße verändert wird. In dieser Methode werden Einstellungen bzgl.
	 * der Perspektive vorgenommen. Diese sind abhängig von der aktuellen
	 * Fenstergröße. Dabei werden die Einstellungen unter der Annahme einer
	 * 3D-Umgebung vorgenommen.
	 * <p>
	 * Nutzer sollten diese Methode überschreiben, wenn sie zusätzliche
	 * Einstellungen bei einer Größenänderung vornehmen wollen. Wichtig ist,
	 * dass <code>super.resizeScene()</code> aufgerufen wird.
	 */
	protected void resizeScene() {
		Rectangle clientArea = getCanvas().getClientArea();
		getGLContext().makeCurrent();

		GL gl = getGLContext().getGL();
		gl.glViewport(0, 0, clientArea.width, clientArea.height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		GLU glu = new GLU();
		glu.gluPerspective(45.0f, (float) clientArea.width / (float) clientArea.height, 0.1f, 100.0f);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		getGLContext().release();
	}

	/**
	 * Führt die Methode <code>draw()</code> aus und zeigt dessen Ergebnisse an.
	 * Diese Methode kann nicht überschrieben werden.
	 */
	public final void render() {
		if (!getCanvas().isCurrent())
			getCanvas().setCurrent();

		draw();

		getCanvas().swapBuffers();
	}

	/**
	 * In dieser Methode werden die konkreten OpenGL-Zeichenbefehle aufgerufen.
	 * Diese Implementierung löscht nur den Bildschirm und den Tiefenpuffer.
	 * <p>
	 * Für eigene Zeichnungen sollte diese Methode überschrieben werden.
	 */
	protected void draw() {
		GL gl = getGLContext().getGL();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
	}

	/**
	 * Wird aufgerufen, wenn das GLCanvas disposed werden soll. Diese
	 * Implementierung tut nichts.
	 */
	protected void dispose() {

	}

	/**
	 * Gibt zurück, ob der GLCanvas bereits disposed wurde. Ist dies der Fall
	 * wird <code>true</code> zurückgegeben, ansonsten <code>false</code>.
	 * 
	 * @return <code>true</code>, wenn GLCanvas bereits disposed wurde,
	 *         <code>false</code> sonst.
	 */
	public boolean isDisposed() {
		return getCanvas().isDisposed();
	}

	/**
	 * Setzt den Ereignisfokus auf das GLCanvas.
	 */
	public void setFocus() {
		getCanvas().setFocus();
	}

	/**
	 * Liefert ein GLCanvas, die Oberfläche, worin OpenGL seine Zeichnungen
	 * darstellt.
	 * <p>
	 * Ist bisher kein GLCanvas vorhanden, so wird einer instanziiert. Dabei
	 * wird die Methode <code>createNewCanvas()</code> aufgerufen.
	 * <p>
	 * Sollten Nutzer einen speziellen GLCanvas verwenden wollen, so sollte die
	 * <code>createNewCanvas()</code> überschrieben werden.
	 * 
	 * @return GLCanvas, welches von OpenGL verwendet wird.
	 */
	public final GLCanvas getCanvas() {
		if (canvas == null) {
			canvas = createNewCanvas();
			Assert.isNotNull(canvas);
		}
		return canvas;
	}

	/**
	 * Erstellt eine neue GLCanvas. Dieser Canvas nutzt DoubleBuffering und
	 * nutzt den gesamten Platz, der zur Verfügung steht. Zusätzlich werden
	 * Listener für <code>resizeScene()</code> und <code>dispose()</code>
	 * registriert.
	 * <p>
	 * Nutzer, welche einen speziellen GLCanvas benötigen, können diese Methode
	 * überschreiben. Es sollte dabei darauf geachtet werden, dass nicht
	 * <code>null</code> zurückgegeben wird.
	 * 
	 * @return <code>GLCanvas</code> mit DoubleBuffering und einigen Listenern.
	 */
	protected GLCanvas createNewCanvas() {
		GLData data = new GLData();
		data.depthSize = 1;
		data.doubleBuffer = true;

		canvas = new GLCanvas(getParent(), SWT.NO_BACKGROUND, data);
		canvas.setSize(parent.getClientArea().width, parent.getClientArea().height);

		canvas.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				resizeScene();
			}
		});
		canvas.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				dispose();
			}
		});
		return canvas;
	}

	/**
	 * Liefert den aktuell verwendeten GLContext. Mittels dieser kann unter
	 * Anderem auf die aktuelle GL-Instanz zugegriffen werden (über
	 * <code>getGL()</code>). Ist noch kein GLContext vorhanden, so wird dieser
	 * mittels der Methode <code>createNewGLContext()</code> erstellt. Nutzer
	 * sollten diese anpassen, wenn spezielle <code>GLContext</code>-Instanzen
	 * notwendig sind.
	 * 
	 * @return <code>GLContext</code> für die OpenGL-Nutzung.
	 */
	public final GLContext getGLContext() {
		if (glContext == null) {
			glContext = createNewGLContext();
			Assert.isNotNull(glContext);
		}
		return glContext;
	}

	/**
	 * Erstellt ein neues GLContext. In dieser Implementierung wird die
	 * Standardversion instanziiert.
	 * <p>
	 * Diese Methode sollte überschrieben werden, wenn der Nutzer ein spezielles
	 * GLContext benötigt. Es sollte sichergestellt sein, dass nicht
	 * <code>null</code> zurückgegeben wird.
	 * 
	 * @return Eine neue Instanz der GLContext-Klasse.
	 */
	protected GLContext createNewGLContext() {
		getCanvas().setCurrent();
		return GLDrawableFactory.getFactory().createExternalGLContext();
	}

	/**
	 * Liefert die aktuelle Display-Instanz, die in SWT verwendet wird.
	 * 
	 * @return Aktuelle Display-Instanz.
	 */
	public final Display getDisplay() {
		return getCanvas().getDisplay();
	}

	/**
	 * Liefert das übergeordnete SWT-Steuerelement. Dies entspricht genau dem
	 * Steuerelement, welches dem Konstruktor übergeben wurde.
	 * 
	 * @return Übergeordnete SWT-Steuerelement.
	 */
	public final Composite getParent() {
		return parent;
	}

	// setzt das übergeordnete SWT-Steuerelement
	// prüft auf null-Parameter
	private void setParent(Composite parent) {
		Assert.isNotNull(parent);
		this.parent = parent;
	}

}
