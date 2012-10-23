package me.Destro168.LoadedObjects;

import me.Destro168.FC_Suite_Shared.StringToY;

public class Group 
{
	String name;
	String display;
	int timeReq;
	int jobReq;
	
	public String getName() { return name; }
	public String getDisplay() { return display; }
	public int getTimeReq() { return timeReq; }
	public int getJobReq() { return timeReq; }
	
	public Group(String parsable)
	{
		char[] c = parsable.toCharArray();
		int lastReadCounter = 0;
		int lastPos = 0;
		StringToY y = new StringToY();
		
		for (int i = 0; i < c.length; i++)
		{
			if (c[i] == ',')
			{
				if (lastReadCounter == 0)
					name = y.returnMergedCharacters(lastPos, i, c);
				else if (lastReadCounter == 1)
					display = y.returnMergedCharacters(lastPos, i, c);
				else if (lastReadCounter == 2)
					timeReq = Integer.valueOf(y.returnMergedCharacters(lastPos, i, c));
				else if (lastReadCounter == 3)
					jobReq = Integer.valueOf(y.returnMergedCharacters(lastPos, i, c));
				
				lastPos = i + 1;
				lastReadCounter++;
			}
		}
	}
}