package tk.hugo4715.slotmachine;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SlotMachine extends JavaPlugin{
	@Override
	public void onEnable() {
		Bukkit.getPluginCommand("arcade").setExecutor(new SlotExecutor(this));
	}
	
}
