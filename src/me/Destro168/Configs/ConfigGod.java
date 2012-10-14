package me.Destro168.Configs;

import me.Destro168.ConfigManagers.CustomConfigurationManager;
import me.Destro168.Conversions.StringToY;
import me.Destro168.FC_Rpg.FC_Rpg;

public class ConfigGod 
{
	protected CustomConfigurationManager ccm;
	protected String prefix;
	protected StringToY converter;
	
	protected void setVersion(double x) { ccm.set(prefix + "version", x); }
	protected double getVersion() { return ccm.getDouble(prefix + "version"); }
	
	//Need to give each config it's own folder.
	public ConfigGod(String target)
	{
		//Initialize variables.
		converter = new StringToY();
		prefix = target + ".";
		ccm = new CustomConfigurationManager(FC_Rpg.plugin.getDataFolder().getAbsolutePath(), target);
	}
}
