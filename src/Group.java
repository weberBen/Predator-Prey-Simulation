
public abstract class Group implements I_Living
{
	private final int type;
	protected double needsToEat;
	protected double sociability;
	protected double agressivity;
	protected double agility;
	protected double strength;
	protected String specie;
	
	public Group(int type)
	{
		this.type = type;
	}
	
	public int getType()
	{
		return type;
	}
	
	public boolean isFamily()
	{
		return (type==Parms.TYPE_FAMILY);
	}
	
	public boolean isPack()
	{
		return (type==Parms.TYPE_PACK);
	}
	
	public double getStrength()
	{
		return strength;
	}
	
	public double getAgility()
	{
		return agility;
	}
	
	public double getAgressivity()
	{
		return agressivity;
	}
	
	public double getSociability()
	{
		return sociability;
	}
	
	public String getSpecie()
	{
		return specie;
	}
	
	public double getNeedsToEat()
	{
		return needsToEat;
	}
	
	public abstract void setNeedsToEat();	
}
