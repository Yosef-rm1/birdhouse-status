package com.birdhousestatus;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("birdhousestatus")
public interface BirdhouseStatusConfig extends Config
{
	@ConfigSection(
			name = "Icon Settings",
			description = "Configure icon size",
			position = 0
	)
	String iconSection = "iconSection";

	@ConfigSection(
			name = "Seeded Birdhouse",
			description = "Settings for seeded birdhouses",
			position = 1
	)
	String seededSection = "seededSection";

	@ConfigSection(
			name = "Needs Seeds",
			description = "Settings for birdhouses that need seeds",
			position = 2
	)
	String needsSeedsSection = "needsSeedsSection";

	@ConfigItem(
			keyName = "iconSize",
			name = "Icon size",
			description = "The size of icons displayed on birdhouses",
			position = 0,
			section = iconSection
	)
	default IconSize iconSize()
	{
		return IconSize.MEDIUM;
	}

	// Seeded section
	@ConfigItem(
			keyName = "showSeededIcon",
			name = "Show Seeded Icon",
			description = "Display a seed icon on birdhouses that have been seeded",
			position = 0,
			section = seededSection
	)
	default boolean showSeededIcon()
	{
		return true;
	}

	@ConfigItem(
			keyName = "seedType",
			name = "Seed icon type",
			description = "Which seed icon to display for seeded birdhouses",
			position = 1,
			section = seededSection
	)
	default SeedType seedType()
	{
		return SeedType.HAMMERSTONE;
	}

	// Needs seeds section
	@ConfigItem(
			keyName = "showNeedsSeedsIcon",
			name = "Show Needs Seeds Icon",
			description = "Display a warning icon on birdhouses that need seeds",
			position = 0,
			section = needsSeedsSection
	)
	default boolean showNeedsSeedsIcon()
	{
		return true;
	}
}
