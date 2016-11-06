package tk.hugo4715.slotmachine.util;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Maps;

abstract public class AbstractGui implements Listener{
    
    protected String name;
    private int taskId;
    protected boolean stopped = false;

    protected Map<Integer, ButtonClickListener> buttons;
    protected Player player;
    protected Inventory inv;
    
    public AbstractGui(JavaPlugin plugin, Player player,String invName, int size ,int refreshTicks){
        this.player = player;
        this.name = invName;
        this.buttons = Maps.newHashMap();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        player.openInventory(getInventory(size));
        update();
        
        taskId = new BukkitRunnable(){
        	public void run() {
        		update();
        	};
        }.runTaskTimer(plugin, 0, refreshTicks).getTaskId();
    }
    
    private Inventory getInventory(int size) {
    	inv = Bukkit.createInventory(null, size,name);
    	update();
		return inv;
	}

	protected void stop(){
		if(stopped)return;
		stopped = true;
		player.closeInventory();
    	HandlerList.unregisterAll(this);
    	Bukkit.getScheduler().cancelTask(taskId);
    }
    
    public void update(){}
    
    @EventHandler
    public void onInvClose(InventoryCloseEvent e){
    	if(!(e.getPlayer() instanceof Player && ((Player)e.getPlayer()).equals(player) && e.getInventory().getName().equals(name)))return;
    	stop();
    }
    
    @EventHandler
    public void onClick(InventoryClickEvent e){
    	if(!(e.getWhoClicked() instanceof Player && ((Player)e.getWhoClicked()).equals(player) && e.getInventory().getName().equals(name)))return;
    	e.setCancelled(true);
    	
    	if(!buttons.containsKey(e.getSlot()))return;
    	buttons.get(e.getSlot()).onClick(e.getSlot());
    }
    
    public abstract class ButtonClickListener{
    	public abstract void onClick(int slot);
    }
}