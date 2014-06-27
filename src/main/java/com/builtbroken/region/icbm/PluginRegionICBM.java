package com.builtbroken.region.icbm;

import icbm.api.explosion.ExplosionEvent.PreExplosionEvent;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.EventPriority;
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
		Field f = null;
		Event event = new Event();
		int id = 0;
		try
		{
			f = MinecraftForge.EVENT_BUS.getClass().getDeclaredField("busID");
			f.setAccessible(true);
			id = f.getInt(MinecraftForge.EVENT_BUS);
		}
		catch (NoSuchFieldException e1)
		{
			logger().fine("Failed to get event bus ID defaulting to zero");
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		event.getListenerList().register(id, EventPriority.NORMAL, new ForgeEventHandler());
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
			int dim = WorldUtility.getDimID(event.world);
			String dimName = WorldUtility.getDimName(event.world);
			String worldName = WorldUtility.getWorldName(event.world);
			
			// First try getting world by dim id
			World world = Bukkit.getWorld("DIM" + dim);
			if (world == null)
			{
				// Then try with dimName
				world = Bukkit.getWorld(dimName);
				if (world == null)
				{
					// IF all fails which it normally does for bukkit world try with the worldName
					world = Bukkit.getWorld(worldName);
				}
			}
			
			if (world != null)
			{
				RegionManager manager = guard.getRegionManager(world);
				ApplicableRegionSet set = manager.getApplicableRegions(vec);
			}
		}
	}
}
