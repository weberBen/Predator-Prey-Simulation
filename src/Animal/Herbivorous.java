package Animal;

import Cell.Cell;

public class Herbivorous extends Animal
{
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
