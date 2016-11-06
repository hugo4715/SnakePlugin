package tk.hugo4715.slotmachine.gui;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.md_5.bungee.api.ChatColor;
import tk.hugo4715.slotmachine.SlotMachine;
import tk.hugo4715.slotmachine.util.AbstractGui;
import tk.hugo4715.slotmachine.util.Direction;
import tk.hugo4715.slotmachine.util.ItemFactory;

public class SlotMachineInv extends AbstractGui {

	private SlotMachine plugin;

	private Map<Point, Tile> map = Maps.newHashMap();

	private int speed = 20;
	private int nextUpdate = speed;
	private float percentAppl = 0.5f;
	private Point headPos;

	private int score = 0;
	public SlotMachineInv(Player player, SlotMachine plugin) {
		super(plugin, player, "SlotMachine", 9 * 9, 1);

		for (int i = 0; i < 5 * 9; i++) {
			map.put(new Point(i % 9, (int) Math.floor(i / 9)), new Background());
		}

		map.put(headPos = new Point(4, 0), new Head());
	}

	@Override
	public void update() {

		/*
		 * Gui update
		 */
		for (int i = 5 * 9; i < 6 * 9; i++) {
			inv.setItem(i, new ItemFactory(Material.STAINED_GLASS_PANE).withColor(DyeColor.BLACK).withName(" ").done());
		}

		inv.setItem(6 * 9 + 12, new ItemFactory(Material.WOOL).withColor(DyeColor.BLACK).withName("Left").done());
		inv.setItem(6 * 9 + 14, new ItemFactory(Material.WOOL).withColor(DyeColor.BLACK).withName("Right").done());
		inv.setItem(6 * 9 + 4, new ItemFactory(Material.WOOL).withColor(DyeColor.BLACK).withName("Up").done());
		inv.setItem(6 * 9 + 22, new ItemFactory(Material.WOOL).withColor(DyeColor.BLACK).withName("Down").done());

		/*
		 * buttons
		 */

		buttons.clear();
		buttons.put(6 * 9 + 12, new ButtonClickListener() {
			@Override
			public void onClick(int slot) {
				goLeft();
			}
		});
		buttons.put(6 * 9 + 14, new ButtonClickListener() {
			@Override
			public void onClick(int slot) {
				goRight();
			}
		});
		buttons.put(6 * 9 + 4, new ButtonClickListener() {
			@Override
			public void onClick(int slot) {
				goUp();
			}
		});
		buttons.put(6 * 9 + 22, new ButtonClickListener() {
			@Override
			public void onClick(int slot) {
				goDown();
			}
		});

		/*
		 * Game logic
		 */
		if (map != null && !map.isEmpty()) {
			if (nextUpdate == 0) {
				nextUpdate = speed;
				
				if(Math.random() < percentAppl){
					Random r = new Random();
					Point p = new Point(r.nextInt(9), r.nextInt(6));
					if(map.get(p) instanceof Background){
						map.put(p, new Apple());
					}
				}
				
				
				Head head = (Head) map.get(headPos);
				Validate.notNull(head);
				Validate.notNull(head.dir);
				
				if (headPos.getX() >= 9 && head.dir.equals(Direction.RIGHT)
						|| headPos.getX() <= 0 && head.dir.equals(Direction.LEFT)
						|| headPos.getY() <= 0 && head.dir.equals(Direction.UP)
						|| headPos.getY() >= 4 && head.dir.equals(Direction.DOWN)) {
					lose("You crashed into a wall! score: " + score);
					return;
				}

				map.put(headPos, new Background());
				head.body.add((Point) headPos.clone());
				headPos.x += head.dir.getX();
				headPos.y += head.dir.getY()*-1;
				System.out.println(head.len + "/" + head.body.size());

				
				head.body.forEach((Point p) -> {
					if(headPos.equals(p))lose("You crashed into yourself! score: " + score);
				});
				Tile t = map.get(headPos);
				
				if(t instanceof Apple){
					Apple ap = (Apple)t;
					head.len += ap.golden ? -1 : 1;
					score++;
					if(speed > 1)speed--;
				}
				
				if(head.body.size() > head.len-1){
					head.body.remove();
				}
				
				map.put(headPos, head);

			} else {
				nextUpdate--;
			}

			map.forEach((Point p, Tile t) -> {
				int i = (int) (p.getX() + (p.getY() * 9));
				if(i >= 0){
					inv.setItem(i, t.getItem());
				}
			});
			
			Head h = (Head) map.get(headPos);
			h.body.forEach((Point loc) -> {
				inv.setItem((int) (loc.getX()+(loc.getY()*9)), new ItemFactory(Material.WOOL).withColor(DyeColor.PINK).withName("").done());
			});
		}



	}

	private void lose(String string) {
		player.closeInventory();
		player.sendMessage(ChatColor.RED + string);
	}

	private void goUp() {
		System.out.println("up");
		Head head = (Head) map.get(headPos);
		head.dir = Direction.UP;
	}

	private void goDown() {
		System.out.println("down");
		Head head = (Head) map.get(headPos);
		head.dir = Direction.DOWN;
	}

	private void goLeft() {
		System.out.println("left");
		Head head = (Head) map.get(headPos);
		head.dir = Direction.LEFT;
	}

	private void goRight() {
		System.out.println("right");
		Head head = (Head) map.get(headPos);
		head.dir = Direction.RIGHT;
	}

	class Tile {
		public ItemStack getItem() {
			return new ItemFactory(Material.STAINED_GLASS_PANE).withColor(DyeColor.WHITE).withName(" ").done();
		}
	}

	class Background extends Tile {
	}

	class Apple extends Tile {
		boolean golden = false;

		public ItemStack getItem() {
			return new ItemFactory(Material.GOLDEN_APPLE).withName(" ").done();
		}
	}

	class Head extends Tile {
		LinkedList<Point> body = Lists.newLinkedList();
		Direction dir = Direction.DOWN;
		int len = 1;
		@Override
		public ItemStack getItem() {
			return new ItemFactory(Material.WOOL).withColor(DyeColor.RED).withName(" ").done();
		}

	}
}
