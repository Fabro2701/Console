package console.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import console.view.ConsoleEditor;

public class TestOptionsModel implements OptionsModel{

	@Override
	public Map<String, Options> buildOptions() {
		Map<String, Options> options = new HashMap<>();
		
		options.put("save", saveOption());
		
		return options;
	}
	@Override
	public Map<String, BiFunction<CommandLine, ConsoleEditor, Boolean>> buildActions() {
		Map<String, BiFunction<CommandLine,ConsoleEditor,Boolean>> actions = new HashMap<>();
		
		actions.put("save", (c,e)->saveAction(c,e));
		
		return actions;
	}
	private Options saveOption() {
		Options ops = new Options();
		ops.addOption(Option.builder("f").longOpt("file").hasArg().build());
		return ops;
	}
	private Boolean saveAction(CommandLine cmd, ConsoleEditor editor){
		if(cmd.hasOption("file")) {
			editor.insertString(cmd.getOptionValue("file")+'\n');
			return true;
		}
		return false;
	}

}
