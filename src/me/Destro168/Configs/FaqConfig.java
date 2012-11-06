package me.Destro168.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;

public class FaqConfig extends ConfigGod
{
	public void setHeader(int i, String x) { ccm.set(prefix + i + ".header", x); }
	public void setDisplayTag(int i, String x) { ccm.set(prefix + i + ".displayTag", x); }
	public void setFaqList(int i, int a, List<String> x) { ccm.setList(prefix + i + ".faqList" + a, x); } //a = 1 or 2
	
	public String getHeader(int i) { return ccm.getString(prefix + i + ".header"); }
	public String getDisplayTag(int i) { return ccm.getString(prefix + i + ".displayTag"); }
	public List<String> getFaqList(int i, int a) { return ccm.getStringList(prefix + i + ".faqList" + a); }
	
	public void setFaqNull(String faqKeyWord) { ccm.set(prefix + getFaqIndex(faqKeyWord), null); }
	
	public FaqConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Faqs");
		handleConfig();
	}
	
	public void addNewFaq(String displayTag)
	{
		List<String> testCase;
		
		for (int i = 0; i < 10000; i++)
		{
			testCase = getFaqList(i, 1);
			
			if (testCase.size() == 0)
			{
				setDisplayTag(i, displayTag);
				break;
			}
		}
	}
	
	public boolean editFaqProperties(String displayTag, String modifable, String val)
	{
		int i = getFaqIndex(displayTag);
		
		if (i == -1)
			return false;
		
		if (modifable.equalsIgnoreCase("header"))
			setHeader(i, val);
		else if (modifable.equalsIgnoreCase("displayTag"))
			setDisplayTag(i, val);
		
		return true;
	}
	
	public boolean editFaqLines(String displayTag, int line, int half, String newValue)
	{
		int i = getFaqIndex(displayTag);
		
		if (i == -1)
			return false;
		
		List<String> args;
		
		//Chance the faq halves.
		args = getFaqList(i, half);
		
		if (newValue == null)
			args.remove(line);
		else
		{
			try { args.set(line, newValue); }
			catch (IndexOutOfBoundsException e) { args.add(newValue); }
		}
		
		setFaqList(i, half, args);
		
		return true;
	}
	
	public int getFaqIndex(String displayTag)
	{
		for (int i = 0; i < 10000; i++)
		{
			if (getDisplayTag(i) != null)
			{
				if (getDisplayTag(i).equalsIgnoreCase(displayTag))
					return i;
			}
		}
		
		return -1;
	}
	
	public void handleConfig()
	{
		//Create a config  if not created
		if (getVersion() < 0.1)
		{
			//Update version.
			setVersion(0.1);
			
			//Begin setting defaults.
			setHeader(0, "FabledCraft Server Information");
			setDisplayTag(0, "server");
			
			//Create an arraylist of faqs
			List<String> faqs = new ArrayList<String>();
			
			faqs.add("Description");
			faqs.add("Destro168 owns FabledCraft and is the developer of FC_Rpg.");
			faqs.add("Server Host/Location");
			faqs.add("Website Link");
			faqs.add("Donation Info");
			
			//Store 1st half of faqs.
			setFaqList(0, 1, faqs);
			
			//Clear the list
			List<String> faqs2 = new ArrayList<String>();
			
			//Add new items
			faqs2.add("100% Custom RPG Server Experience!");
			faqs2.add("[empty]");
			faqs2.add("Beastnode - New York");
			faqs2.add("http://fabledcraft.enjin.com/");
			faqs2.add("http://fabledcraft.enjin.com/store");
			
			//Set second half of faqs.
			setFaqList(0, 2, faqs2);
		}
	}
}




















