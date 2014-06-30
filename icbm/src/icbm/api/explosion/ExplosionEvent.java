package icbm.api.explosion;

import net.minecraft.world.World;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.Event;

public class ExplosionEvent extends Event
{
	public final World world;
	public final double x;
	public final double y;
	public final double z;
	public IExplosive explosive;

	public ExplosionEvent(World world, double x, double y, double z, IExplosive explosive)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.explosive = explosive;
	}

	@Cancelable
	public static class PreExplosionEvent extends ExplosionEvent
	{
		public PreExplosionEvent(World world, double x, double y, double z, IExplosive explosive)
		{
			super(world, x, y, z, explosive);
		}
	}

	public static class PostExplosionEvent extends ExplosionEvent
	{
		public PostExplosionEvent(World world, double x, double y, double z, IExplosive explosive)
		{
			super(world, x, y, z, explosive);
		}
	}
}