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
		event.getListenerList().register(id, EventPriority.NORMAL, new EventHandler());
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
			int dim = 0; // field_73011_w
			String dimName = "";
			String worldName = "";
			try
			{
				// dim = event.world.provider.dimensionId;
				Field f = null;
				WorldProvider provider = null;
				WorldInfo info = null;
				try
				{
					f = event.world.getClass().getField("provider");
				}
				catch (NoSuchFieldException e1)
				{
					f = event.world.getClass().getField("field_73011_w");
				}
				f.setAccessible(true);
				provider = (WorldProvider) f.get(event.world);
				try
				{
					f = provider.getClass().getField("dimensionId");
				}
				catch (NoSuchFieldException e1)
				{
					f = provider.getClass().getField("field_76574_g");
				}
				f.setAccessible(true);
				dim = f.getInt(provider);
				Method m = null;
				try
				{
					m = provider.getClass().getMethod("getDimensionName");
				}
				catch (NoSuchMethodException e1)
				{
					m = provider.getClass().getMethod("func_80007_l");
				}
				if (m != null)
					dimName = (String) m.invoke(provider);
				
				try
				{
					m = event.world.getClass().getMethod("getWorldInfo");
				}
				catch (NoSuchMethodException e1)
				{
					m = event.world.getClass().getMethod("func_72912_H");
				}
				if(m != null)
					info = (WorldInfo) m.invoke(event.world);
				if(info != null)
				{
					try
					{
						m = info.getClass().getMethod("getWorldName");
					}
					catch (NoSuchMethodException e1)
					{
						m = info.getClass().getMethod("func_76065_j");
					}
					if(m != null)
						worldName = m.invoke(info);
				}
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			World world = Bukkit.getWorld("DIM" + dim);
			event.world.getWorldInfo().getWorldName();
			if (world == null)
			{
				System.out.println("Failed to get world by ID");
				System.out.println("Trying with dimName -> " + dimName);
				world = Bukkit.getWorld(dimName);
				if(world == null)
				{
					System.out.println("Failed to get world by dimName");
					System.out.println("Trying with worldName -> " + worldName);
				}
				world = Bukkit.getWorld(worldName);
			}
			if (world != null)
			{
				System.out.println("World is not null");
				RegionManager manager = guard.getRegionManager(world);
				ApplicableRegionSet set = manager.getApplicableRegions(vec);
			}
			else
			{
				System.out.println("World is null");
			}
		}
	}
}
