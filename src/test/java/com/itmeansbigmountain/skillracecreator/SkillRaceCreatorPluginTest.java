package com.itmeansbigmountain.skillracecreator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import net.runelite.api.Skill;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;
import net.runelite.client.plugins.PluginDescriptor;
import org.junit.Test;

public class SkillRaceCreatorPluginTest
{
	@Test
	public void descriptorMatchesPluginMetadata()
	{
		PluginDescriptor descriptor = SkillRaceCreatorPlugin.class.getAnnotation(PluginDescriptor.class);

		assertEquals("SkillRaceCreator", descriptor.name());
		assertTrue(descriptor.description().contains("skill race"));
		assertEquals("skill", descriptor.tags()[0]);
	}

	@Test
	public void configDefaultsDefineUsableLocalRace()
	{
		SkillRaceCreatorConfig config = new SkillRaceCreatorConfig() {};

		assertEquals(Skill.AGILITY, config.trackedSkill());
		assertEquals(1000000, config.targetXp());
		assertTrue(config.showLoginMessage());
		assertTrue(config.showProgressMessages());
	}

	@Test
	public void formatProgressShowsPercentAndRemainingXp()
	{
		String message = SkillRaceCreatorPlugin.formatProgress(Skill.RUNECRAFT, 250000, 1000000);

		assertTrue(message.contains("Runecraft race"));
		assertTrue(message.contains("250,000 / 1,000,000 XP"));
		assertTrue(message.contains("25.0%"));
		assertTrue(message.contains("750,000 XP remaining"));
	}

	@Test
	public void formatProgressCapsCompletedRaceAtOneHundredPercent()
	{
		String message = SkillRaceCreatorPlugin.formatProgress(Skill.AGILITY, 1500000, 1000000);

		assertTrue(message.contains("100.0%"));
		assertTrue(message.contains("0 XP remaining"));
		assertFalse(message.contains("-"));
	}

	@Test
	public void runelitePluginPropertiesPointAtPluginClass() throws Exception
	{
		String properties = new String(Files.readAllBytes(Paths.get("runelite-plugin.properties")), StandardCharsets.UTF_8);

		assertTrue(properties.contains("displayName=SkillRaceCreator"));
		assertTrue(properties.contains("author=Oyama"));
		assertTrue(properties.contains("plugins=com.itmeansbigmountain.skillracecreator.SkillRaceCreatorPlugin"));
		assertTrue(properties.contains("tags=skill,race,xp,competition"));
	}

	@Test
	public void pluginJsonMatchesRuneLiteMetadata() throws Exception
	{
		String pluginJson = new String(Files.readAllBytes(Paths.get("plugin.json")), StandardCharsets.UTF_8);

		assertTrue(pluginJson.contains("\"name\": \"SkillRaceCreator\""));
		assertTrue(pluginJson.contains("\"author\": \"Oyama\""));
		assertTrue(pluginJson.contains("\"skill\""));
		assertTrue(pluginJson.contains("\"competition\""));
	}

	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(SkillRaceCreatorPlugin.class);
		RuneLite.main(args);
	}
}
