package console.model;

import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import console.view.ConsoleEditor;

public interface OptionsModel {
	public Map<String, Options> buildOptions();

	public boolean execute(CommandLine cmd, ConsoleEditor editor);
}
