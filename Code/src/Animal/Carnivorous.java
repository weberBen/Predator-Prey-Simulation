package Animal;

import Cell.Cell;
import Parameters.Parms;

public class Carnivorous extends Animal
{
	public Carnivorous()
	{
		super();
	}
	
	public Carnivorous(String specie,
					  double energy, 
					  double strength, 
					  double agility,
					  double agressivity, 
					  double sociability,
					  Cell zoneLive,
					  int liveRadius,
					  int NB_POSSIBLE_KID, 
					  int age,
					  double height, 
					  double vision,
					  int teenAge,
					  int oldAge,
					  int maxAge)
	{
		super(specie,energy,strength,agility,agressivity,sociability,zoneLive,
				liveRadius,NB_POSSIBLE_KID,age,height,vision,teenAge,oldAge,maxAge);
	}
	public Animal createChild(Animal v)
	{
		//copie des paramètres propre à l'espece et force et autre parms à 0
		Animal child = new Carnivorous(this.specie,
									   1,
									   3*(this.strength+v.strength)/(2*teenAge),
									   3*(this.agility+v.agility)/(2*teenAge),
									   (this.agressivity+v.agressivity)/2,
									   (this.sociability+v.sociability)/2,
									   this.zoneLive,
									   this.liveRadius,
									   this.NB_POSSIBLE_KID,
									   3,
									   3*(this.agility+v.agility)/(2*teenAge),
									   (this.sociability+v.sociability)/2,
									   this.teenAge,
									   this.oldAge,
									   this.maxAge);
		child.setX(getX());
		child.setY(getY());
		return child;
	}
	
	
}
