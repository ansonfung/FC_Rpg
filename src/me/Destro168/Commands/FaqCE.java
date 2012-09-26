package me.Destro168.Commands;

import me.Destro168.Configs.FaqManager;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FaqCE implements CommandExecutor
{
	public FaqCE() { }
		
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
		Player player = (Player) sender;
		RpgMessageLib msgLib = new RpgMessageLib(player);
		ArgParser ap = new ArgParser(args);
		FaqManager fm = new FaqManager();
		String tag = "";
		String faqFirstHalf = "";
		String faqSecondHalf = "";
		boolean success = false;
		int breakPoint1 = 0;
		int breakPoint2 = 0;
		
		//We attempt to display faqs and parse based on faq.
		for (int i = 0; i < 10000; i++)
		{
			//Store the tag that we are currently parsing.
			tag = fm.getFaqTag(i);
			
			//If the tag isn't null...
			if (fm.getFaqTag(i) != null)
			{
				//Also if the argument entered is equal to the tag...
				if (ap.getArg(0).equals(tag))
				{
					//We prase through the faqs for that tag now.
					for (int j = 0; j < 10000; j++)
					{
						//Store first and second half of the faq.
						try
						{
							faqFirstHalf = fm.getFaq1(i).get(j);
							faqSecondHalf = fm.getFaq2(i).get(j);
							
							if (faqFirstHalf != null && faqSecondHalf != null)
							{
								if (success == false)
									msgLib.standardHeader(fm.getFaqName(i));
								
								if (!(faqSecondHalf.equals("[empty]")))
									msgLib.standardMessage(faqFirstHalf,faqSecondHalf);
								else
									msgLib.standardMessage(faqFirstHalf);

								success = true;
							}
							else
							{
								breakPoint2++;
								
								if (breakPoint2 > 50)
									break;
							}
						}
						catch (IndexOutOfBoundsException e) { }
					}
				}
			}
			else
			{
				breakPoint1++;
				
				if (breakPoint1 > 50)
					break;
			}
		}
		
		if (success == false)
			return msgLib.helpFaq();
		
		return true;
    }
}
