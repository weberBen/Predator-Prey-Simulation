package Group;

import Animal.Animal;
import Cell.Cell;
import Parameters.Parms;

public class Family extends Group
{
	private Pack g_father;
	private Pack g_mother;
	
	public Family()
	{
		super(Parms.TYPE_FAMILY);
		this.g_father = null;
		this.g_mother = null;
	}
	
	//animal in charge of food
	public void setFather(Animal a)
	{
		g_father = new Pack(a);
		a.setGroup(this);
	}
	
	public Pack getFather()
	{
		return g_father;
	}
	
	//animal in charge of children
	public void setMother(Animal a)
	{
		g_mother = new Pack(a);
		a.setGroup(this);
	}
	
	public Pack getMother()
	{
		return g_mother;
	}
	
	//children
	public void addChild(Animal a)
	{
		g_mother.addMember(a);
		a.setGroup(this);
	}
	
	//implement interface
	public double getStrength()
	{
		double res = 0;
		if(g_father!=null)
		{
			res+=g_father.getStrength();
		}
		
		if(g_mother!=null)
		{
			res+=g_mother.getStrength();
		}
		
		return res;
	}
	
	public double getSociability()
	{
		double res = 0;
		if(g_father!=null)
		{
			res+=g_father.getStrength();
		}
		
		if(g_mother!=null)
		{
			res+=g_mother.getStrength();
		}
		
		return res;
	}
	
	public double getAgility()
	{
		double res = 0;
		if(g_father!=null)
		{
			res+=g_father.getStrength();
		}
		
		if(g_mother!=null)
		{
			res+=g_mother.getStrength();
		}
		
		return res;
	}
	
	public double getAgressivity()
	{
		double res = 0;
		if(g_father!=null)
		{
			res+=g_father.getStrength();
		}
		
		if(g_mother!=null)
		{
			res+=g_mother.getStrength();
		}
		
		return res;
	}
	
	public String getSpecie()
	{
		return g_father.getSpecie();
	}
	
	public void decreaseEnergy(double ep)
	{
		if(g_father!=null)
		{
			g_father.decreaseEnergy(ep);
		}
		
		if(g_mother!=null)
		{
			g_mother.decreaseEnergy(ep);
		}
	}
	
	
	//extends
	public boolean needToEat()
	{
		return Parms.AnimalNeedToEat(getNeedsToEat());
	}
	
	public double getNeedsToEat()
	{
		double output = 0;
		
		//check if each animal need to eat, if so, add the amount of needs to the sum
		if(g_father!=null && g_father.needToEat())
		{
			output+=g_father.getNeedsToEat();
		}
		
		if(g_mother!=null && g_mother.needToEat())
		{
			output+=g_mother.getNeedsToEat();
		}
		
		return (output<0)?Math.abs(output):0;
		//if the animal need to eat, then the value of the need will be negative, so we use the absolute value of the sum
	}
	
	public void setDeath(Animal a)
	{
		if(g_father!=null && a.equals(g_father.getChief()))
		{
			setDeathForFather();
		}else if(g_mother!=null && a.equals(g_mother.getChief()))
		{
			setDeathForMother();
		}else 
		{
			setDeathForChild(a);
		}
	}
	
	private void setDeathForFather()
	{
		if(g_father != null)
		{
			g_father.setChief(g_mother.getChief());//the mother become the father
			g_mother.setDeathForChief();
		}else
		{
			g_father = new Pack(g_mother.getChief());
			g_mother.setDeathForChief();
		}
	}
	
	private void setDeathForMother()
	{
		g_mother.setDeathForChief();
	}
	
	private void setDeathForChild(Animal a)
	{
		g_mother.setDeath(a);
	}
	
	public int getSize()
	{
		int count = 0;
		if(g_father!=null)
		{
			count++;
		}
		
		if(g_mother!=null)
		{
			count+= g_mother.getSize();
		}
		
		return count;
	}
	
	public boolean isCarnivorous()
	{
		return g_father.getChief().isCarnivorous();
	}
	
	public boolean isHerbivorous()
	{
		return g_father.getChief().isHerbivorous();
	}
	
	public void interact(Group p)
	{
		
	}
	
	public void evoluateFather(Cell[][] map)
	{
		/* si la famille a faim alors le père part cherche de la nouriture
		 * S'il est à proximité de sa famille alors sa force est celle de tous les membres de sa famille
		 * Sinon sa force est la sienne
		 */
	}
	
	public void evoluateMother(Cell[][] map)
	{
		/* si la famille a faim alors le père part cherche de la nouriture
		 * S'il est à proximité de sa famille alors sa force est celle de tous les membres de sa famille
		 * Sinon sa force est la sienne
		 */
	}
	
	public void findFood(Cell[][] map)
	{
		Animal father = g_father.getChief();
		if(father==null)
			return;
		
		
		
	}
}
