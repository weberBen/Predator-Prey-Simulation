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
	
	private Family(){};
	
	public Family(Pack g_father, Pack g_mother) throws IllegalArgumentException
	{
		super();
		
		if(g_father==null || g_mother==null)
			throw new IllegalArgumentException("Un des parents de la famille est nul");
		
		setFather(g_father);
		setMother(g_mother);
		addChildren();
	}
	
	private void addChildren()//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	{
		Animal mother = g_mother.getChief();
		int numChildren = 1+ (int)(1 + Math.random()*(mother.NB_POSSIBLE_KID));
		
		Animal a;
		Pack p;
		
		for(int i=0; i<numChildren; i++)
		{
			a = mother.createChild();
			p = new Pack(a);
			addChild(p);
		}
	}
	
	public void setCaller(Animal caller)
	{
		this.caller = caller;
	}
	
	//animal in charge of food
	private void setFather(Pack o)//remove later
	{
		g_father = o;
		g_father.setGroup(this);
	}
	
	public Pack getFather()
	{
		return g_father;
	}
	
	//animal in charge of children
	private void setMother(Pack o)
	{
		g_mother = o;
		g_mother.setGroup(this);
	}
	
	public Pack getMother()
	{
		return g_mother;
	}
	
	//children
	private void addChild(Group o)
	{
		g_mother.add(o);
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
	
	public void age()
	{//verifier si enfant trop grand (comme pere mere ou "enfant" =>plus de mineur dans la famille) on la split
		//si enfant trop grand on le sort de la famille
		if(g_father!=null)
		{
			g_father.age();
		}
		
		if(g_mother!=null)
		{
			g_mother.age();
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
	
	
	public void setDeath(Group o)
	{
		if(g_father!=null && o==g_father)
		{
			setDeathForFather();
		}else if(g_mother!=null && o==g_mother)
		{
			setDeathForMother();
		}else 
		{
			setDeathForChild(o);
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
			{
				g_mother = null;
			}
			
			if(getGroup()!=null)//family is part of an other group
			{
				Pack p = (Pack)getGroup();
				p.splitFamily(this);
			}else//family is a stand alone group
			{
				if(temp.getSize()==0)
				{
					/* If the children of the family is not old enough, then we don't split
					 * the family because the children need to be protected
					 * (otherwise, if there is no more children in the family we split it)
					 */
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
		Animal mother = g_mother.getChief();
		g_mother.setDeath(new Pack(mother));
	}
	
	private void setDeathForMother()
	{
		g_mother.setDeath(new Pack(g_mother.getChief()));
	}
	
	private void setDeathForChild(Group o)
	{
		g_mother.setDeath(o);
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
	
	
	public boolean _fight(Pack o)
	{
		return false;
	}
	
	public boolean _fight(Herd o)
	{
		return false;
	}
	
	public String toString()
	{
		String res ="";
		res+="Famille : \n";
		res+="\tPère : " + g_father.getChief().toString();
		res+="\n\tMère : " + g_mother.toString();
		
		return res;
	}
}
