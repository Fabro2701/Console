package console;

import javax.swing.SwingUtilities;

import console.view.AbstractConsoleGUI;

public class Main {
	public static void main(String args[]) {
		SwingUtilities.invokeLater(()->{new AbstractConsoleGUI(700,400).setVisible(true);});
		
	}
}
