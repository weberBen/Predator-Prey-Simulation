package Animal;

import Cell.Cell;
import Parameters.Parms;

public class Carnivorous extends Animal
{
	public Animal createChild()
	{
		//copie des paramètres propre à l'espece et force et autre parms à 0
		return new Carnivorous();
	}
	
	public void eat(Cell[][] map)
	{
		
	}
	
	public double getStrength()
	{
		return 0;
	}
	
	public double getSociability()
	{
		return 0;
	}
	
	public double getAgility()
	{
		return 0;
	}
	
	public double getAgressivity()
	{
		return 0;
	}
	
	public String getSpecie()
	{
		return null;
	}
	
	public void decreaseEnergy(double ep)
	{
		
	}
	
}
