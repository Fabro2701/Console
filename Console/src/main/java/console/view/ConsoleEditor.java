package console.view;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.Utilities;

import console.Constants;
import console.model.CommandController;

/**
 * ConsoleEditor represents the terminal UI.
 * Sends the queries(commands) to {@code CommandController}.
 * And handles everything related to the queries history and the messages received from {@code OptionsModel} 
 * 
 * @author Fabrizio Ortega
 *
 */
public class ConsoleEditor extends JTextPane{
	char last; 
	QueryHistory queryHistory;
	CommandController cmdCtrl;
	
	public static Style errorStyle;
	public static Style commandStyle;
	public static Style infoStyle;
	static {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		commandStyle = sc.addStyle("CommandStyle", null);
		StyleConstants.setForeground(commandStyle, Constants.Command_Color);
		errorStyle = sc.addStyle("ErrorStyle", null);
		StyleConstants.setForeground(errorStyle, Constants.Error_Color);
		infoStyle = sc.addStyle("InfoStyle", null);
		StyleConstants.setForeground(infoStyle, Constants.Info_Color);
	}
	
	public ConsoleEditor(CommandController cmdCtrl) {
		super();
		((DefaultStyledDocument)this.getDocument()).setDocumentFilter(new CustomDocumentFilter());

		this.cmdCtrl = cmdCtrl;
		this.cmdCtrl.setEditor(this);

		queryHistory = new QueryHistory(Constants.Query_History_Capacity);
		

		this.putClientProperty("caretWidth", 2);
		this.setBackground(Constants.BackGround_Color);
		//this.setForeground(Constants.Text_Color);
		this.setCaretColor(Constants.Command_Color);
		this.init();

		this.addKeyListener(new ConsoleKeyListener(this));
	}
	private void init() {
		this.setEntry();
	}
	public void notify(char c) {
		switch(c) {
		case '\n':
			String query = this.getCurrentQuery();
			if(query.length()>0) {
				boolean r = this.cmdCtrl.execute(query);
				if(r||Constants.Save_Incorrect_Commands) {
					this.queryHistory.push(query);
				}
			}
			this.setEntry();
			break;
		case '\t':
			String query2 = this.getCurrentQuery();
			String newquery = this.cmdCtrl.autoComplete(query2);
			this.removeCurrentQuery();
			this.insertString(newquery, commandStyle, true);
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
				case KeyEvent.VK_UP://retrive last query
					editor.removeCurrentQuery();
					editor.insertString(editor.queryHistory.getPreviousQuery(), commandStyle, true);
					break;
				}
				editor.last = e.getKeyChar();
			});
	    }
	}
	/**
	 * Writes the initial symbol that represents the ready state (>)
	 */
	private void setEntry() {
		insertString(Constants.Entry_String, commandStyle, true);
	}
	/**
	 * Cleans all the text after the initial symbol
	 */
	public void removeCurrentQuery() {
		Document doc = this.getDocument();
		String lineText = this.getCurrentQuery();
		
		try {
			doc.remove(doc.getLength()-lineText.length(), lineText.length());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Finds the text after the last initial symbol
	 * @return the current query or ""
	 */
	private String getCurrentQuery() {
		Document doc = this.getDocument();
		
		int endPosition = doc.getLength() - 1;
		Element root = doc.getDefaultRootElement();
		int endLine = root.getElementIndex(endPosition);

		Element lineElement = root.getElement(endLine);
		int start = lineElement.getStartOffset();
		int end = lineElement.getEndOffset() - 1;

		String lineText;
		try {
		    lineText = this.getDocument().getText(start, end - start);
			lineText = lineText.substring(1);
		} catch (BadLocationException ex) {
		    lineText = "";
		}
		return lineText;
	}
	/**
	 * Inserts a string
	 * @param s
	 */
	public void insertString(String s) {
		insertString(s, this.commandStyle, false);
	}
	/**
	 * Inserts a string with a given style
	 * @param s
	 * @param style
	 */
	public void insertString(String s, Style style) {
		insertString(s, style, false);
	}
	/**
	 * Inserts a string with a given style and moves the caret to the end of the doc
	 * @param s
	 * @param style
	 * @param moveCaret
	 */
	public void insertString(String s, Style style, boolean moveCaret) {
		Document doc = this.getDocument();
		try {
			doc.insertString(doc.getLength(), s, style);
			if(moveCaret)this.setCaretPosition(doc.getLength()); 
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Inserts string with {@link #infoStyle} style
	 * @param s
	 */
	public void sendInfo(String s) {
		insertString(s, this.infoStyle, false);
	}
	/**
	 * Inserts string with {@link #errorStyle} style
	 * @param s
	 */
	public void sendError(String s) {
		insertString(s, this.errorStyle, false);
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
		/**
		 * Pushes a given query to the list or replace the oldest if the new size exceeds the {@link #capacity}
		 * @param query
		 */
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
		/**
		 * Retrieves the last refereced query
		 * @return
		 */
		public String getPreviousQuery() {
			if(last==null)return "";
			String aux = last.value;
			last = last.prev;
			return aux;
		}
	}
	private class CustomDocumentFilter extends DocumentFilter {
		@Override
		public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {
			if(text.charAt(0)=='\n') {
				super.insertString(fb, ConsoleEditor.this.getDocument().getLength(), text, attrs);
				ConsoleEditor.this.notify(text.charAt(0));
			}
			else if(text.charAt(0)=='\t') {
				ConsoleEditor.this.notify(text.charAt(0));
			}
			else super.insertString(fb, offset, text, attrs);
		}
	    @Override
	    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			if(text.charAt(0)=='\n') {
				super.insertString(fb, ConsoleEditor.this.getDocument().getLength(), text, attrs);
				ConsoleEditor.this.notify(text.charAt(0));
			}
			else if(text.charAt(0)=='\t') {
				ConsoleEditor.this.notify(text.charAt(0));
			}
			else super.replace(fb, offset, length, text, attrs);
	    }
	    @Override
	    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
	    	int rowStart = Utilities.getRowStart(ConsoleEditor.this, offset);
	    	int column = offset - rowStart;
	    	if(column!=0)fb.remove(offset, length);
	    }
	}
	/*public static void main(String args[]) {
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
	}*/
	public void moveCaretToEnd() {
		this.setCaretPosition(this.getDocument().getLength());
	}
}
