
public abstract class Animal implements I_Living
{
	private double energy;
	private Group group;
	
	public double getNeedsToEat()
	{
		return energy - Parms.hungerThreshold;
	}
	
	public boolean needToEat(Animal a)
	{
		return Parms.AnimalNeedToEat(getNeedsToEat());
	}
	
	public boolean equals(Object o)
	{
		return true;
	}
	
	public void setGroup(Group g)
	{
		group = g;
	}
	
	public Group getGroup()
	{
		return group;
	}
	
	public void kill()
	{
		if(group!=null)
		{
			group.setDeath(this);
			group = null;
		}
	}
	
	public void interact(Animal a)
	{
		
	}
}
