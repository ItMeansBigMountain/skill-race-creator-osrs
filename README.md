# SkillRaceCreator

SkillRaceCreator is a lightweight RuneLite external plugin scaffold for local OSRS skill-race tracking. It lets a player choose one skill, set a target XP value, and receive in-game chat progress updates as XP changes.

This repo is prepared as a standalone RuneLite plugin project using the same Gradle/wrapper layout as the user's `breach-check-osrs` boilerplate convention.

## Current behavior

- Adds the RuneLite plugin `SkillRaceCreator` from package `com.itmeansbigmountain.skillracecreator`.
- Provides config options for:
  - tracked skill (default: Agility)
  - target XP (default: 1,000,000)
  - login summary message toggle
  - XP-change progress message toggle
- On login, optionally prints current progress toward the configured target.
- On XP changes in the tracked skill, optionally prints updated race progress.
- Uses only RuneLite client APIs; no external Wise Old Man, TempleOSRS, or other network APIs are currently required.

## Repository layout

```text
src/main/java/com/itmeansbigmountain/skillracecreator/
  SkillRaceCreatorPlugin.java   # plugin entry point and progress formatting
  SkillRaceCreatorConfig.java   # RuneLite config group/options
src/test/java/com/itmeansbigmountain/skillracecreator/
  SkillRaceCreatorPluginTest.java # lightweight metadata/config/format smoke tests and dev launcher
runelite-plugin.properties       # RuneLite plugin metadata
plugin.json                      # repo/plugin metadata
build.gradle                     # Java 11 RuneLite Gradle build
```

## Requirements

- Java 11
- Gradle wrapper included in this repository
- RuneLite dependencies pinned to `1.12.27` and resolved from `https://repo.runelite.net` and Maven Central

Recommended local environment for this workspace:

```bash
export JAVA_HOME=/opt/data/jdks/current-java11
export PATH="$JAVA_HOME/bin:$PATH"
```

## Run checks

From the repository root:

```bash
./gradlew test --no-daemon -q
./gradlew assemble --no-daemon -q
```

The tests are intentionally lightweight and do not start a live RuneLite client. They verify plugin descriptor metadata, config defaults, and progress-message formatting.

## Manual RuneLite testing

After the Gradle checks pass, manually smoke-test in RuneLite developer mode:

```bash
./gradlew run --no-daemon
```

Then verify:

1. RuneLite starts with the SkillRaceCreator plugin available as an external plugin.
2. The plugin config panel shows the tracked skill, target XP, login summary, and progress message options.
3. Logging into OSRS shows the configured login summary when enabled.
4. Gaining XP in the tracked skill emits a readable progress message when enabled.
5. Disabling progress messages suppresses XP-change chat output.

## Plugin Hub prep notes

Before plugin-hub submission, capture at least one screenshot/GIF of the config panel and an in-game progress message. If the plugin grows into shared competitions or public leaderboards, document the external API provider, rate limits, cache strategy, privacy implications, and failure behavior before submission.
