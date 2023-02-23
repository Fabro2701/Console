package console;

import javax.swing.SwingUtilities;

import console.model.CommandController;
import console.view.AbstractConsoleGUI;

public class Main {
	public static void main(String args[]) {
		System.out.println((-2)%5);
		SwingUtilities.invokeLater(()->{new AbstractConsoleGUI(700,400,new CommandController()).setVisible(true);});
		
	}
}
