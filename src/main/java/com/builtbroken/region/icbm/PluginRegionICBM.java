package com.builtbroken.region.icbm;

import icbm.api.explosion.ExplosionEvent.PreExplosionEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.ForgeSubscribe;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;

/**
 * Bukkit plugin to help support ICBM interaction with WorldGuard
 * 
 * @since 6/26/2014
 * @author Robert Seifert
 */
public class PluginRegionICBM extends JavaPlugin
{
	private static PluginRegionICBM instance;
	private PluginLogger logger;

	public static PluginRegionICBM instance()
	{
		return instance;
	}

	@Override
	public void onEnable()
	{
		instance = this;
		logger().info("Enabled!");
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void onDisable()
	{
		logger().info("Disabled!");
	}

	/** Logger used by the plugin, mainly just prefixes everything with the name */
	public PluginLogger logger()
	{
		if (logger == null)
		{
			logger = new PluginLogger(this);
			logger.setParent(getLogger());
		}
		return logger;
	}

	/** Gets the worldguard plugin currently loaded */
	protected WorldGuardPlugin getWorldGuard()
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

		if (plugin == null || !(plugin instanceof WorldGuardPlugin))
		{
			return null;
		}

		return (WorldGuardPlugin) plugin;
	}

	/** Gets the custom flags plugin currently loaded */
	protected WGCustomFlagsPlugin getWGCustomFlags()
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("WGCustomFlags");

		if (plugin == null || !(plugin instanceof WGCustomFlagsPlugin))
		{
			return null;
		}

		return (WGCustomFlagsPlugin) plugin;
	}
	
	@ForgeSubscribe
	public void preExplosion(PreExplosionEvent event)
	{
		System.out.println("Boom Time");
		WorldGuardPlugin guard = getWorldGuard();
		if (guard != null)
		{
			Vector vec = new Vector(event.x, event.y, event.z);
			World world = Bukkit.getWorld("DIM" + event.world.provider.dimensionId);
			RegionManager manager = guard.getRegionManager(world);
			ApplicableRegionSet set = manager.getApplicableRegions(vec);			
		}
	}
}
