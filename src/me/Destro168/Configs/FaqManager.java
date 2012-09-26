package me.Destro168.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.configuration.file.FileConfiguration;

public class FaqManager 
{
	private FileConfiguration config;
	private final String faqPrefix = "Faqs.";
	
	public void setVersion(double x) { config.set(faqPrefix + "version", x); }
	public double getVersion() { return config.getDouble(faqPrefix + "version"); }
	
	private void setFaqName(int i, String x) { config.set(faqPrefix + i + ".name", x); }
	private void setFaqTag(int i, String x) { config.set(faqPrefix + i + ".tag", x); }
	private void setFaq1(int i, List<String> x) { config.set(faqPrefix + i + ".faq1", x); }
	private void setFaq2(int i, List<String> x) { config.set(faqPrefix + i + ".faq2", x); }
	
	public String getFaqName(int i) { return config.getString(faqPrefix + i + ".name"); }
	public String getFaqTag(int i) { return config.getString(faqPrefix + i + ".tag"); }
	public List<String> getFaq1(int i) { return config.getStringList(faqPrefix + i + ".faq1"); }
	public List<String> getFaq2(int i) { return config.getStringList(faqPrefix + i + ".faq2"); }
	
	public void addNewFaq(List<String> x, List<String> y)
	{
		List<String> testCase;
		
		for (int i = 0; i < 10000; i++)
		{
			testCase = getFaq1(i);
			
			if (testCase.size() == 0)
			{
				setFaq1(i, x);
				setFaq2(i, y);
			}
		}
	}
	
	public FaqManager()
	{
		handleConfig();
	}
	
	public void handleConfig()
	{
		config = FC_Rpg.plugin.getConfig();
		
		//Create a config  if not created
		if (getVersion() < 1.0)
		{
			//Update version.
			setVersion(1.0);
			
			//Begin setting defaults.
			setFaqName(0, "FabledCraft Server Information");
			setFaqTag(0, "server");
			
			//Create an arraylist of faqs
			List<String> faqs = new ArrayList<String>();
			
			faqs.add("Description");
			faqs.add("Destro168 owns FabledCraft and is the developer of FC_Rpg.");
			faqs.add("Server Host/Location");
			faqs.add("Website Link");
			faqs.add("Donation Info");
			
			//Store 1st half of faqs.
			setFaq1(0, faqs);
			
			//Clear the list
			List<String> faqs2 = new ArrayList<String>();
			
			//Add new items
			faqs2.add("100% Custom RPG Server Experience!");
			faqs2.add("[empty]");
			faqs2.add("Beastnode - New York");
			faqs2.add("http://fabledcraft.enjin.com/");
			faqs2.add("http://fabledcraft.enjin.com/store");
			
			//Set second half of faqs.
			setFaq2(0, faqs2);
			
			//Save default faqs
			FC_Rpg.plugin.saveConfig();
		}
	}
}




















