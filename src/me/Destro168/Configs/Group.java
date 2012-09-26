package me.Destro168.Configs;

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
		
		for (int i = 0; i < c.length; i++)
		{
			if (c[i] == ',')
			{
				if (lastReadCounter == 0)
					name = returnMergedCharacters(lastPos, i, c);
				else if (lastReadCounter == 1)
					display = returnMergedCharacters(lastPos, i, c);
				else if (lastReadCounter == 2)
					timeReq = Integer.valueOf(returnMergedCharacters(lastPos, i, c));
				else if (lastReadCounter == 3)
					jobReq = Integer.valueOf(returnMergedCharacters(lastPos, i, c));
				
				lastPos = i + 1;
				lastReadCounter++;
			}
		}
	}
	
	private String returnMergedCharacters(int startPosition, int endPosition, char[] c)
	{
		String finalString = "";
		
		for (int j = startPosition; j < endPosition; j++)
			finalString = finalString + c[j];
		
		return finalString;
	}
}
