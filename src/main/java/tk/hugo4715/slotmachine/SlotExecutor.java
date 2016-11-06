package tk.hugo4715.slotmachine;

import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.hugo4715.slotmachine.gui.SlotMachineInv;

public class SlotExecutor implements CommandExecutor {

	private SlotMachine plugin;

	public SlotExecutor(SlotMachine slotMachine) {
		this.plugin = slotMachine;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		Validate.isTrue(sender instanceof Player);
		
		Player p = (Player)sender;
		
		p.closeInventory();
		new SlotMachineInv(p,plugin);
		return false;
	}

}
