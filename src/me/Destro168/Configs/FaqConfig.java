package me.Destro168.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;

public class FaqConfig extends ConfigGod
{
	private void setFaqName(int i, String x) { ccm.set(prefix + i + ".name", x); }
	private void setFaqTag(int i, String x) { ccm.set(prefix + i + ".tag", x); }
	private void setFaq1(int i, List<String> x) { ccm.setList(prefix + i + ".faq1", x); }
	private void setFaq2(int i, List<String> x) { ccm.setList(prefix + i + ".faq2", x); }
	
	public String getFaqName(int i) { return ccm.getString(prefix + i + ".name"); }
	public String getFaqTag(int i) { return ccm.getString(prefix + i + ".tag"); }
	public List<String> getFaq1(int i) { return ccm.getStringList(prefix + i + ".faq1"); }
	public List<String> getFaq2(int i) { return ccm.getStringList(prefix + i + ".faq2"); }
	
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
	
	public FaqConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Faqs");
		handleConfig();
	}
	
	public void handleConfig()
	{
		//Create a config  if not created
		if (getVersion() < 0.1)
		{
			//Update version.
			setVersion(0.1);
			
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
		}
	}
}




















