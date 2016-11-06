package tk.hugo4715.slotmachine.util;

import java.util.Arrays;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

public class ItemFactory {
	
	private ItemStack item;
	
	public ItemFactory(ItemStack item){
		this.item = item;
	}
	
	public ItemFactory(Material mat) {
		item = new ItemStack(mat);
	}
	
	public ItemFactory(Material mat, int amount) {
		item = new ItemStack(mat,amount);
	}
	
	public ItemFactory withName(String name){
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		
		return this;
	}
	
	public ItemFactory withLore(String... lore){
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemFactory withColor(DyeColor color){
		item.setDurability(color.getData());
		return this;
	}
	
	public ItemFactory withOwner(String owner){
		if(item.getType().equals(Material.SKULL_ITEM)){
			item.setDurability((short) 3);
			SkullMeta m = (SkullMeta) item.getItemMeta();
			m.setOwner(owner);
			item.setItemMeta(m);
		}
		return this;
	}
	
	public ItemFactory withAmount(int amount){
		item.setAmount(amount);
		return this;
	}
	
	public ItemFactory withEnchant(Enchantment e, int lvl){
		item.addEnchantment(e, lvl);
		return this;
	}
	
	public ItemStack done(){
		return item;
	}
}