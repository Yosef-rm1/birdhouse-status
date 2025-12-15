package com.birdhousestatus;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("birdhousestatus")
public interface BirdhouseStatusConfig extends Config
{

	@ConfigItem(
			keyName = "iconSize",
			name = "Icon size",
			description = "The size of icons displayed on birdhouses",
			position = 0
	)
	default IconSize iconSize()
	{
		return IconSize.MEDIUM;
	}

	// Needs seeds section
	@ConfigItem(
			keyName = "showNeedsSeedsIcon",
			name = "Show Needs Seeds Icon",
			description = "Display a warning icon on birdhouses that need seeds",
			position = 1
	)
	default boolean showNeedsSeedsIcon()
	{
		return true;
	}

	// Seeded section
	@ConfigItem(
			keyName = "showSeededIcon",
			name = "Show Seeded Icon",
			description = "Display a seed icon on birdhouses that have been seeded",
			position = 2
	)
	default boolean showSeededIcon()
	{
		return true;
	}

	@ConfigItem(
			keyName = "seedType",
			name = "Seed icon type",
			description = "Which seed icon to display for seeded birdhouses",
			position = 3
	)
	default SeedType seedType()
	{
		return SeedType.HAMMERSTONE;
	}
}
