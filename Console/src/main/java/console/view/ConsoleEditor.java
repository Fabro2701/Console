package console.view;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import console.Constants;

public class ConsoleEditor extends JEditorPane{
	ConsoleKeyListener keylist;
	char last;
	StringBuilder query;
	
	public ConsoleEditor() {
		super();
		query = new StringBuilder();
		
		this.putClientProperty("caretWidth", 2);
		this.setBackground(Color.black);
		this.setForeground(Color.green);
		this.setCaretColor(Color.green);
		this.init();

		this.addKeyListener(new ConsoleKeyListener(this));
		
	}
	private void init() {
		this.setEntry();
	}
	public void notify(char c) {

		switch(c) {
		case '\n':
			this.query = new StringBuilder();
			this.setEntry();
			break;
		}
	}
	private class ConsoleKeyListener extends KeyAdapter{
		ConsoleEditor editor;
		protected ConsoleKeyListener(ConsoleEditor editor){
			this.editor = editor;
		}
		@Override
	    public void keyPressed(KeyEvent e) {
			SwingUtilities.invokeLater(()->{editor.last = e.getKeyChar();
			editor.query.append(editor.last);
			if(editor.last=='\n')editor.notify(editor.last);});
			
	    }
	}
	private void setEntry() {
		Document doc = this.getDocument();
		try {
			doc.insertString(doc.getLength(), Constants.entryString, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
