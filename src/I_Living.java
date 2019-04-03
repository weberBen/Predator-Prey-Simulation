
public interface I_Living 
{
	public void setStrength();
	public double getStrength();
	
	public void setSociability();
	public double getSociability();
	
	public void setAgility();
	public double getAgility();
	
	public void setAgresivity();
	public double getAgressivity();
	
	public String getSpecie();
	
	public double getNeedsToEat();
	public void decreaseEnergy(double ep);
}