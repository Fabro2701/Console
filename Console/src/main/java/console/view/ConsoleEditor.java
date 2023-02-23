package console.view;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
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
	QueryHistory queryHistory;
	CommandController cmdCtrl;
	
	Pattern allowedChars = Pattern.compile("[\\d\\w\\s._-]");
	
	public ConsoleEditor(CommandController cmdCtrl) {
		super();
		this.cmdCtrl = cmdCtrl;
		this.cmdCtrl.setEditor(this);
		query = new StringBuilder();
		queryHistory = new QueryHistory(5);
		
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
				this.queryHistory.push(this.query.toString());
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
					editor.removeCurrentQuery();
					String s = editor.queryHistory.getPreviousQuery();
					editor.query = new StringBuilder(s);
					editor.insertString(s, true);
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
	public void removeCurrentQuery() {
		Document doc = this.getDocument();
		try {
			doc.remove(doc.getLength()-this.query.length(), this.query.length());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
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
	private static class QueryHistory{
		private Node current, last;
		int capacity, used;
		public QueryHistory(int capacity) {
			this.capacity = capacity;
			this.used = 0;
		}
		private class Node{
			Node next,prev;
			String value;
			@Override public String toString() {return value;}
		}
		public void push(String query) {
			if(current==null) {
				current = new Node();
				current.value = query;
				current.next = current;
				current.prev = current;
				last = current;
				used++;
			}
			else {
				if(used<capacity) {
					Node newnode = new Node();
					newnode.value = query;
					newnode.prev = current;
					newnode.next = current.next;

					current.next.prev = newnode;
					current.next = newnode;
					
					current = newnode;
					used++;
				}
				else {
					current = current.next;
					current.value = query;
				}
				last = current;
			}
		}
		public String getPreviousQuery() {
			String aux = last.value;
			last = last.prev;
			return aux;
		}
	}
	public static void main(String args[]) {
		QueryHistory q = new QueryHistory(3);
		q.push("1");
		System.out.println(q.getPreviousQuery());
		q.push("2");
		System.out.println(q.getPreviousQuery());
		q.push("3");
		System.out.println(q.getPreviousQuery());
		q.push("4");
		System.out.println(q.getPreviousQuery());
		System.out.println(q.getPreviousQuery());
		System.out.println(q.getPreviousQuery());
		System.out.println(q.getPreviousQuery());
		System.out.println(q.getPreviousQuery());
		System.out.println(q.getPreviousQuery());
	}
}
