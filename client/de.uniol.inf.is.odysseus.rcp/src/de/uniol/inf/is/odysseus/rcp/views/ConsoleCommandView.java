package de.uniol.inf.is.odysseus.rcp.views;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class ConsoleCommandView extends ViewPart {

	private static final Logger LOG = LoggerFactory.getLogger(ConsoleCommandView.class);
	private static final Collection<CommandProvider> COMMAND_PROVIDERS = Lists.newArrayList();

	private Text outputText;
	private Combo inputCombo;

	// called by OSGi-DS
	public static void bindCommandProvider(CommandProvider serv) {
		COMMAND_PROVIDERS.add(serv);
	}

	// called by OSGi-DS
	public static void unbindCommandProvider(CommandProvider serv) {
		COMMAND_PROVIDERS.remove(serv);
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());

		outputText = createOutputText(parent);

		Composite inputComposite = new Composite(parent, SWT.NONE);
		inputComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		inputComposite.setLayout(new GridLayout(2, false));

		Label inputLabel = new Label(inputComposite, SWT.NONE);
		inputLabel.setText(">");

		inputCombo = new Combo(inputComposite, SWT.BORDER);
		inputCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		inputCombo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) { // Return-key
					Combo inputCombo = (Combo) e.getSource();
					String commandText = inputCombo.getText().trim();

					if (commandText.equalsIgnoreCase("clear")) {
						outputText.setText("");
					} else if (commandText.equalsIgnoreCase("help")) {
						printHelp();
					} else {
						sendCommand(commandText);
					}

					addCommandToHistory(inputCombo, commandText);

					inputCombo.setText("");
				}
			}

		});
	}

	private static Text createOutputText(Composite parent) {
		Text text = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		setFont(text);

		text.setLayoutData(new GridData(GridData.FILL_BOTH));

		return text;
	}

	private static void setFont(Text text) {
		Font initialFont = text.getFont();
		FontData[] fontData = initialFont.getFontData();
		for (int i = 0; i < fontData.length; i++) {
			fontData[i].setName("Courier");
		}
		Font newFont = new Font(Display.getCurrent(), fontData);
		text.setFont(newFont);
	}

	private static void addCommandToHistory(Combo inputCombo, String commandText) {
		inputCombo.add(commandText);
		if (inputCombo.getItemCount() > 10) {
			inputCombo.remove(10);
		}
	}

	@Override
	public void setFocus() {
		inputCombo.setFocus();
	}

	private void printHelp() {
		outputText.append("\n> help\n");
		for (CommandProvider provider : COMMAND_PROVIDERS) {
			outputText.append(provider.getHelp() + "\n");
		}
	}

	private void sendCommand(String text) {
		if (Strings.isNullOrEmpty(text)) {
			return;
		}
		String[] splitted = text.split("\\ ", 2);
		String command = splitted[0];
		String parameters = splitted.length > 1 ? splitted[1] : null;

		Optional<Method> optMethod = determineCommandMethod(command);
		Optional<CommandProvider> optProvider = determineProvider(command);
		if (!Strings.isNullOrEmpty(outputText.getText())) {
			outputText.append("\n");
		}

		if (!optMethod.isPresent()) {
			outputText.append("No such command: " + command);
		}

		outputText.append("> " + text + "\n");

		CommandInterpreter ci = new TextCommandInterpreter(outputText, parameters != null ? parameters.split("\\ ") : new String[0]);
		try {
			optMethod.get().invoke(optProvider.get(), ci);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOG.error("Could not execute command {}", command, e);
		}
	}

	private static Optional<Method> determineCommandMethod(String command) {
		for (CommandProvider provider : COMMAND_PROVIDERS) {
			try {
				return Optional.of(provider.getClass().getMethod("_" + command, CommandInterpreter.class));
			} catch (NoSuchMethodException e) {
			}
		}

		return Optional.absent();
	}

	private static Optional<CommandProvider> determineProvider(String command) {
		for (CommandProvider provider : COMMAND_PROVIDERS) {
			try {
				provider.getClass().getMethod("_" + command, CommandInterpreter.class);
				return Optional.of(provider);
			} catch (NoSuchMethodException e) {
			}
		}

		return Optional.absent();
	}

}
