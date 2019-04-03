
public abstract class Animal implements I_Living
{
	private double energy;
	
	public double getNeedsToEat()
	{
		return energy - Parms.hungerThreshold;
	}
}
