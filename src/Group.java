
public abstract class Group implements I_Living
{
	private final int type;
	
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
	
	public boolean isHerd()
	{
		return (type==Parms.TYPE_HERD);
	}
	
	public boolean isAnimal()
	{
		return isPack() && getSize()==1;
	}
	
	public abstract double getNeedsToEat();	
	public abstract int getSize();
	public abstract void setDeath(Animal a);	
	public abstract void interact(Group p);
}
