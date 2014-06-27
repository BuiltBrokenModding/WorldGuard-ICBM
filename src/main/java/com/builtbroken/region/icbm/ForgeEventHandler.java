package com.builtbroken.region.icbm;

import icbm.api.explosion.ExplosionEvent.PreExplosionEvent;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.IEventListener;

public class ForgeEventHandler implements IEventListener
{

	@Override
	public void invoke(Event paramEvent)
	{
		if(paramEvent instanceof PreExplosionEvent)
		{
			PluginRegionICBM.instance().preExplosion((PreExplosionEvent) paramEvent);
		}		
	}

}
