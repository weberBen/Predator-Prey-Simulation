import Cell.ObjectMap;
import Group.Group;
import Interfaces.I_Living;
import Parameters.Parms;

public abstract class Animal1 extends ObjectMap implements I_Living
{
	private double energy;
	private Group group;
	
	public double getNeedsToEat()
	{
		return energy - Parms.hungerThreshold;
	}
	
	public int getAge()
	{
		return 0;
	}
	
	public boolean needToEat(Animal1 a)
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
			//group.setDeath(this);
			group = null;
		}
	}
	
	public double getHeight()
	{
		return 1;
	}
	
}
