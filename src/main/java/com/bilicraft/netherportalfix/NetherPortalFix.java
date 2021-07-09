package com.bilicraft.netherportalfix;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class NetherPortalFix extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event){
        if(event.getPlayer().getLocation().getBlock().getType() != Material.NETHER_PORTAL){
            return;
        }
        getLogger().info(event.getPlayer().getName()+" stuck in portal at "+event.getPlayer().getLocation());

        event.getPlayer().sendMessage(ChatColor.YELLOW+"检测到你似乎卡在了下界传送门中，我们正在尝试修复该问题...");

        int maxHeight = event.getPlayer().getWorld().getMaxHeight();
        if(event.getPlayer().getWorld().getEnvironment() == World.Environment.NETHER){
            maxHeight = 128;
        }
        int safeArea = event.getPlayer().getWorld().getHighestBlockYAt(event.getPlayer().getLocation());
        if(safeArea < maxHeight){
            Location loc = event.getPlayer().getLocation().clone();
            loc.setY(safeArea);
            event.getPlayer().teleport(loc);
            event.getPlayer().sendMessage(ChatColor.GREEN+"您已被传送至安全区域");
            return;
        }
        event.getPlayer().sendMessage(ChatColor.GREEN+"由于无法查找到安全传送区域，下界门已强制破坏");
        Block block = event.getPlayer().getLocation().getBlock();
        block.setType(Material.OAK_SIGN);
        Sign sign = (Sign)block.getState();
        sign.setLine(0,ChatColor.WHITE+"[Bilicraft]");
        sign.setLine(1,ChatColor.WHITE+"因玩家被此门卡住");
        sign.setLine(2,ChatColor.WHITE+"该门已被强制破坏");
    }


}
