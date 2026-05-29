package com.itmeansbigmountain.skillracecreator;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SkillRaceCreatorPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(SkillRaceCreatorPlugin.class);
		RuneLite.main(args);
	}
}