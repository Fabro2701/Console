package console.model;

import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import console.view.ConsoleEditor;

public interface OptionsModel {
	public Map<String, Options> buildOptions();
	public Map<String, BiFunction<CommandLine,ConsoleEditor,Boolean>> buildActions();
	//public boolean execute(String command, CommandLine cmd, ConsoleEditor editor);
}
