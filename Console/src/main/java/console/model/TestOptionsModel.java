package console.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import console.view.ConsoleEditor;

public class TestOptionsModel implements OptionsModel{

	@Override
	public Map<String, Options> buildOptions() {
		Map<String, Options> options = new HashMap<>();
		
		Options ops = new Options();
		ops.addOption(Option.builder("f").longOpt("file").hasArg().build());
		
		options.put("save", ops);
		
		return options;
	}

	@Override
	public boolean execute(CommandLine cmd, ConsoleEditor editor) {
		/*if(cmd.getOptions().length==0) {
			editor.insertString("No commands found\n");
			return false;
		}*/
		if(cmd.hasOption("file")) {
			editor.insertString(cmd.getOptionValue("file")+'\n');
			return true;
		}
		return false;
	}

}
