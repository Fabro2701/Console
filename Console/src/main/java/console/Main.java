package console;

import javax.swing.SwingUtilities;

import console.model.CommandController;
import console.model.TestOptionsModel;
import console.view.AbstractConsoleGUI;

public class Main {
	public static void main(String args[]) {
		SwingUtilities.invokeLater(()->{
			new AbstractConsoleGUI(700,400,
								   new CommandController(new TestOptionsModel()))
			.setVisible(true);
			});
		
	}
}
