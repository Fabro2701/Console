package console.view;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import console.Constants;
import console.model.CommandController;

public class ConsoleEditor extends JEditorPane{
	ConsoleKeyListener keylist;
	char last;
	StringBuilder query;
	String lastQuery;
	CommandController cmdCtrl;
	
	Pattern allowedChars = Pattern.compile("[\\d\\w\\s._-]");
	
	public ConsoleEditor(CommandController cmdCtrl) {
		super();
		this.cmdCtrl = cmdCtrl;
		this.cmdCtrl.setEditor(this);
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
			if(this.query.length()!=0) {
				this.lastQuery = this.query.toString();
				this.cmdCtrl.execute(query.toString());
				this.query = new StringBuilder();
			}
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
			SwingUtilities.invokeLater(()->{
				switch(e.getKeyCode()) {
				case KeyEvent.VK_UP:
					editor.query = new StringBuilder(editor.lastQuery);
					editor.insertString(editor.query.toString(), true);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					break;
				}
			
				editor.last = e.getKeyChar();
				Matcher m = allowedChars.matcher(editor.last+"");
				
				if(editor.last=='\n')editor.notify(editor.last);
				else if(m.find())editor.query.append(editor.last);
			});
	    }
	}
	private void setEntry() {
		insertString(Constants.entryString, true);
	}
	public void insertString(String s) {
		insertString(s, false);
	}
	public void insertString(String s, boolean moveCaret) {
		Document doc = this.getDocument();
		try {
			doc.insertString(doc.getLength(), s, null);
			if(moveCaret)this.setCaretPosition(doc.getLength()); 
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
