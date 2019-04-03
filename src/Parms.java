
public final class Parms 
{
	public static final int TYPE_FAMILY = 0;
	public static final int TYPE_PACK = 1;
	
	public static final double hungerThreshold = 0.5;
	
	private Parms(){}
	
	public static boolean AnimalNeedToEat(Animal a)
	{
		return a.getNeedsToEat()>=0;
	}
	
}
