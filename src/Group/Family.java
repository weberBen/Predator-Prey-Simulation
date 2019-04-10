package Group;

import Animal.Animal;
import Cell.Cell;
import Parameters.Parms;
import Main.*;

public class Family extends Group
{
	private Pack g_father;
	private Pack g_mother;
	private Animal caller;
	
	public Family()
	{
		super(Parms.TYPE_FAMILY);
		this.g_father = null;
		this.g_mother = null;
	}
	
	public void setCaller(Animal caller)
	{
		this.caller = caller;
	}
	
	//animal in charge of food
	public void setFather(Animal a)
	{
		g_father = new Pack(a);
		g_father.setGroup(this);
	}
	
	public Pack getFather()
	{
		return g_father;
	}
	
	//animal in charge of children
	public void setMother(Animal a)
	{
		g_mother = new Pack(a);
		g_mother.setGroup(this);
	}
	
	public Pack getMother()
	{
		return g_mother;
	}
	
	//children
	public void addChild(Animal a)
	{
		g_mother.addMember(a);
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
	
	public void setDeath(int number)
	{
		
	}
	
	public boolean isTogether()
	{
		Animal father = g_father.getChief();
		Animal mother = g_mother.getChief();
		
		return false;
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
	
	public void setDeath()
	{	
		if(g_mother!=null)
		{
			g_mother.clear();
		}
		g_mother.setGroup(null);
		g_father.setGroup(null);
	
		Pack temp_f = g_father;
		Pack temp_m = g_mother;
		
		g_mother = null;
		g_father = null;
		if(getGroup()!=null)
		{
			Pack p = (Pack)getGroup();
			p.splitFamily(this);
		}else
		{
			Run.removeGroup(temp_f);
			Run.removeGroup(temp_m);
		}
	}
	
	
	public void setDeath(Animal a)
	{
		if(g_father!=null && a==g_father.getChief())
		{
			setDeathForFather();
		}else if(g_mother!=null && a==g_mother.getChief())
		{
			setDeathForMother();
		}else 
		{
			setDeathForChild(a);
		}
		
		if(g_father!=null && g_father.getSize()==0)
		{
			Pack temp1 = g_father;
			Pack temp2 = g_mother;
			
			g_father = null;
			g_mother = null;
			
			if(getGroup()!=null)//family is part of an other group
			{
				Pack p = (Pack)getGroup();
				p.splitFamily(this);
			}else//family is a stand alone group
			{
				Run.removeGroup(temp1);
				Run.removeGroup(temp2);
			}
		}else if(g_mother !=null && g_mother.getSize()<=1)
		{
			g_father.setGroup(null);
			g_mother.setGroup(null);
			
			Pack temp = g_mother;
			if(temp.getSize()==0)
				g_mother = null;
			
			if(getGroup()!=null)//family is part of an other group
			{
				Pack p = (Pack)getGroup();
				p.splitFamily(this);
			}else//family is a stand alone group
			{
				if(temp.getSize()==0)
				{
					Run.removeGroup(temp);
				}
			}
			
		}else if(g_mother==null && g_father==null)
		{
			if(getGroup()!=null)//family is part of an other group
			{
				Pack p = (Pack)getGroup();
				p.splitFamily(this);
			}
		}
		
	}
	
	private void setDeathForFather()
	{
		if(g_father != null)
		{
			g_father.setChief(g_mother);//the mother become the father
		}else
		{
			g_father = new Pack(g_mother);
		}
		g_mother.setDeath(g_mother.getChief());
	}
	
	private void setDeathForMother()
	{
		g_mother.setDeath(g_mother.getChief());
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
