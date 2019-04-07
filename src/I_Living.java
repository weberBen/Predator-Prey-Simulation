
public interface I_Living 
{
	public double getStrength();
	public double getSociability();
	public double getAgility();
	public double getAgressivity();
	public String getSpecie();
	
	public double getNeedsToEat();
	public void decreaseEnergy(double ep);
	
	public boolean needToEat();
	
}