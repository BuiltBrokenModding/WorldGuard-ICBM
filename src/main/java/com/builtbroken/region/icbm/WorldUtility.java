package com.builtbroken.region.icbm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldInfo;

public class WorldUtility
{
	/** Gets the world provider from the world using reflection */
	public static WorldProvider getProvider(net.minecraft.world.World world)
	{
		Field f = null;
		try
		{
			try
			{
				f = world.getClass().getField("provider");
			}
			catch (NoSuchFieldException e1)
			{
				f = world.getClass().getField("field_73011_w");

			}
			f.setAccessible(true);
			return (WorldProvider) f.get(world);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/** Gets the dim id from the world using reflection */
	public static int getDimID(net.minecraft.world.World world)
	{
		try
		{
			WorldProvider provider = getProvider(world);
			if (provider != null)
			{
				Field f = null;
				try
				{
					f = provider.getClass().getField("dimensionId");
				}
				catch (NoSuchFieldException e1)
				{
					f = provider.getClass().getField("field_76574_g");
				}
				f.setAccessible(true);
				return f.getInt(provider);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/** Gets the dim id from the world using reflection */
	public static String getDimName(net.minecraft.world.World world)
	{
		try
		{
			WorldProvider provider = getProvider(world);
			if (provider != null)
			{
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
					return (String) m.invoke(provider);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/** Gets the WorldInfo from the world using reflection */
	public static WorldInfo getWorldInfo(net.minecraft.world.World world)
	{
		try
		{
			Method m = null;
			try
			{
				m = world.getClass().getMethod("getWorldInfo");
			}
			catch (NoSuchMethodException e1)
			{
				m = world.getClass().getMethod("func_72912_H");
			}
			if (m != null)
				return (WorldInfo) m.invoke(world);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/** Gets the WorldName from the world using reflection */
	public static String getWorldName(net.minecraft.world.World world)
	{
		try
		{
			WorldInfo info = getWorldInfo(world);
			if (info != null)
			{
				Method m = null;
				try
				{
					m = info.getClass().getMethod("getWorldName");
				}
				catch (NoSuchMethodException e1)
				{
					m = info.getClass().getMethod("func_76065_j");
				}
				if (m != null)
					return (String) m.invoke(info);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
