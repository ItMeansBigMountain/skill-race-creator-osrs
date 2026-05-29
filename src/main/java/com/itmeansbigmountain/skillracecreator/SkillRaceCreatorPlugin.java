package com.itmeansbigmountain.skillracecreator;

import com.google.inject.Provides;
import java.text.NumberFormat;
import java.util.Locale;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "SkillRaceCreator",
	description = "Tracks a configured OSRS skill race target with lightweight in-game progress messages.",
	tags = {"skill", "race", "xp", "competition"}
)
public class SkillRaceCreatorPlugin extends Plugin
{
	private static final NumberFormat INTEGER_FORMAT = NumberFormat.getIntegerInstance(Locale.US);

	@Inject
	private Client client;

	@Inject
	private SkillRaceCreatorConfig config;

	@Override
	protected void startUp()
	{
		log.debug("SkillRaceCreator started");
	}

	@Override
	protected void shutDown()
	{
		log.debug("SkillRaceCreator stopped");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN && config.showLoginMessage())
		{
			Skill skill = config.trackedSkill();
			int targetXp = config.targetXp();
			int currentXp = client.getSkillExperience(skill);
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", formatProgress(skill, currentXp, targetXp), null);
		}
	}

	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{
		Skill skill = config.trackedSkill();
		if (!config.showProgressMessages() || statChanged.getSkill() != skill)
		{
			return;
		}

		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", formatProgress(skill, statChanged.getXp(), config.targetXp()), null);
	}

	static String formatProgress(Skill skill, int currentXp, int targetXp)
	{
		int safeTargetXp = Math.max(1, targetXp);
		int remainingXp = Math.max(0, safeTargetXp - currentXp);
		double percent = Math.min(100.0, (currentXp * 100.0) / safeTargetXp);

		return String.format(
			"SkillRaceCreator: %s race is %s / %s XP (%.1f%%). %s XP remaining.",
			formatSkillName(skill),
			INTEGER_FORMAT.format(currentXp),
			INTEGER_FORMAT.format(safeTargetXp),
			percent,
			INTEGER_FORMAT.format(remainingXp)
		);
	}

	private static String formatSkillName(Skill skill)
	{
		String rawName = skill.name().toLowerCase().replace('_', ' ');
		String[] words = rawName.split(" ");
		StringBuilder builder = new StringBuilder();
		for (String word : words)
		{
			if (word.isEmpty())
			{
				continue;
			}
			if (builder.length() > 0)
			{
				builder.append(' ');
			}
			builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
		}
		return builder.toString();
	}

	@Provides
	SkillRaceCreatorConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SkillRaceCreatorConfig.class);
	}
}
