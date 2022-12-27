package wtf.godlydev.exampleplugin;

import wtf.godlydev.console.plugin.api.Plugin;
import wtf.godlydev.exampleplugin.command.AddCommand;
import wtf.godlydev.exampleplugin.command.ExampleCommand;

public class Main extends Plugin {

	private ExampleCommand exampleCommand;
	private AddCommand addCommand;
	
	@Override
	public void onEnable() {
		super.onEnable();
		this.registerCommand(this.exampleCommand = new ExampleCommand());
		this.registerCommand(this.addCommand = new AddCommand());
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		this.unregisterCommand(this.exampleCommand);
		this.unregisterCommand(this.addCommand);
	}

}
