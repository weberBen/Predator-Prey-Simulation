
public final class Parms 
{
	public static final int TYPE_FAMILY = 0;
	public static final int TYPE_PACK = 1;
	public static final int TYPE_HERD = 2;
	
	public static final double FACTOR_ORGANIZATION_HERD = 0.5;
	
	public static final double hungerThreshold = 0.5;
	
	private Parms(){}
	
	public static boolean AnimalNeedToEat(double needs)
	{
		return needs>=0;
	}
	
	public static double sociabilityPack(int number_element)
	{
		double lambda, mu;
		lambda = 0.5;
		mu = 0.1;
		
		return Math.exp(-lambda*number_element) + mu;
	}
	
	public static double sociabilityHerd(int number_element)
	{
		double lambda, mu;
		lambda = 0.5;
		mu = 0.1;
		
		return Math.exp(-lambda*number_element) + mu;
	}
	
}
