package console.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class AbstractConsoleGUI extends JFrame{
	ConsoleEditor editor;
	JScrollPane scrollpane;
	public AbstractConsoleGUI(int w, int h) {
		Dimension d = new Dimension(w,h);
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.setMaximumSize(d);
		
		
		scrollpane = new JScrollPane();
		editor = new ConsoleEditor();
		
		scrollpane.setViewportView(editor);
		this.setContentPane(scrollpane);
		
		this.setLocationRelativeTo(null);
		//this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
	}
}
