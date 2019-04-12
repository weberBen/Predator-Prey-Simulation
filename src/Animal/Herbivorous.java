package Animal;

import Cell.Cell;

public class Herbivorous extends Animal
{
	public Animal createChild()
	{
		//copie des paramètres propre à l'espece et force et autre parms à 0
		return new Herbivorous();
	}
	
	public void eat(Cell[][] map)
	{
		Cell cell = getCell(map);
		cell.deviateAnimal(this);
		energy+=cell.getFood(getNeedsToEat());
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
