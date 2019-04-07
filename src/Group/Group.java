package Group;
import Parameters.Parms;
import Animal.Animal;
import Interfaces.I_Living;

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
	
	public boolean isHerd()
	{
		return (type==Parms.TYPE_HERD);
	}
	
	public boolean isPack()
	{
		return (type==Parms.TYPE_PACK);
	}
	
	public boolean isAnimal()
	{
		return isPack() && getSize()==1;
	}
	
	
	public boolean isFamilyMember()
	{
		if(!isPack())
			return false;
		
		Pack p = (Pack)this;
		if(p.getChief()==null)
			return false;
		
		return p.getChief().getGroup()!=null;
	}
	
	public abstract double getNeedsToEat();	
	public abstract int getSize();
	public abstract void setDeath(Animal a);
	
	public void interact(Group p)
	{
		if(this.isFamilyMember())//père ou mère par exemple
		{
			if(p.isFamilyMember())
			{
				
			}else if(p.isAnimal())
			{
				
			}else if(p.isPack())
			{
				
			}else if(p.isHerd())
			{
				
			}
		}else if(p.isAnimal())
		{
			
		}
	}
	
}
