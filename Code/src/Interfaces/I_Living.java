package Interfaces;

public interface I_Living 
{
	public double getStrength();
	public double getSociability();
	public double getAgility();
	public double getAgressivity();
	public String getSpecie();
	
	public void age();
	
	public double getNeedsToEat();
	public boolean needToEat();
	
	public boolean isHerbivorous();
	public boolean isCarnivorous();
}