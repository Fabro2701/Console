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
	private static Map<String, Options> options;
	private static CommandLineParser parser = new DefaultParser();
	
	static {
		buildOptions();
	}
	public CommandController() {

	}
	private static void buildOptions() {
		options = new HashMap<>();
		
		Options ops = new Options();
		ops.addOption(Option.builder("f").longOpt("file").hasArg().build());
		
		options.put("save", ops); 
	}
	public void execute(String query) {
		String args[] = query.split(" ");
		try {
			CommandLine cmd = parser.parse(options.get(args[0]), Arrays.copyOfRange(args, 1, args.length));
			if(cmd.getOptions().length==0) {
				editor.insertString("No commands found\n");
			}
			if(cmd.hasOption("file")) {
				editor.insertString(cmd.getOptionValue("file")+'\n');
		         System.out.println(cmd.getOptionValue("file"));
		     }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setEditor(ConsoleEditor consoleEditor) {
		this.editor = consoleEditor;
	}

}
