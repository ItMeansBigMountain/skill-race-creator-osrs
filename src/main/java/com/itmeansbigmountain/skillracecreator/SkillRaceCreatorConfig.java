package com.itmeansbigmountain.skillracecreator;

import net.runelite.api.Skill;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("skillracecreator")
public interface SkillRaceCreatorConfig extends Config
{
	@ConfigItem(
		keyName = "trackedSkill",
		name = "Tracked skill",
		description = "The skill this local race tracker watches for XP gains",
		position = 0
	)
	default Skill trackedSkill()
	{
		return Skill.AGILITY;
	}

	@Range(
		min = 1,
		max = 200000000
	)
	@ConfigItem(
		keyName = "targetXp",
		name = "Target XP",
		description = "Race target XP for the tracked skill",
		position = 1
	)
	default int targetXp()
	{
		return 1000000;
	}

	@ConfigItem(
		keyName = "showLoginMessage",
		name = "Show login summary",
		description = "Show the configured race target when you log in",
		position = 2
	)
	default boolean showLoginMessage()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showProgressMessages",
		name = "Show progress messages",
		description = "Show a game chat message when XP changes in the tracked skill",
		position = 3
	)
	default boolean showProgressMessages()
	{
		return true;
	}
}
