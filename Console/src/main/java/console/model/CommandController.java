package console.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import console.view.ConsoleEditor;

public class CommandController {
	ConsoleEditor editor;
	private Map<String, Options> options;
	private static CommandLineParser parser = new DefaultParser();
	private OptionsModel model;
	
	public CommandController(OptionsModel model) {
		options = model.buildOptions();
		this.model = model;
	}
	public boolean execute(String query) {
		String args[] = query.split(" ");
		if(!options.containsKey(args[0])) {
			editor.insertString(args[0]+" command not supported\n");
			return false;
		}
		try {
			CommandLine cmd = parser.parse(options.get(args[0]), Arrays.copyOfRange(args, 1, args.length));
			return model.execute(cmd, editor);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public void setEditor(ConsoleEditor consoleEditor) {
		this.editor = consoleEditor;
	}

}
