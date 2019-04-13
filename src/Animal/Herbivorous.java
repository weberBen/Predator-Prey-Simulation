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
}
