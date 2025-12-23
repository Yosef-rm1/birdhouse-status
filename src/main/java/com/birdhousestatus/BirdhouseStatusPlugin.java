package com.birdhousestatus;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import static net.runelite.api.NullObjectID.NULL_30565;
import static net.runelite.api.NullObjectID.NULL_30566;
import static net.runelite.api.NullObjectID.NULL_30567;
import static net.runelite.api.NullObjectID.NULL_30568;

@Slf4j
@PluginDescriptor(
		name = "Birdhouse Status"
)
public class BirdhouseStatusPlugin extends Plugin
{
	// Birdhouse duration in milliseconds (50 minutes)
	public static final long BIRD_HOUSE_DURATION_MS = 50 * 60 * 1000;

	// Time Tracking config group
	private static final String TIME_TRACKING_CONFIG_GROUP = "timetracking";

	private static final int MEADOW_NORTH = NULL_30565;
	private static final int MEADOW_SOUTH = NULL_30566;
	private static final int VALLEY_NORTH = NULL_30567;
	private static final int VALLEY_SOUTH = NULL_30568;

	@Inject
	private Client client;

	@Inject
	private BirdhouseStatusConfig config;

	@Inject
	private BirdhouseStatusOverlay overlay;

	@Inject
	@Getter(AccessLevel.PACKAGE)
	private ConfigManager configManager;

	@Getter(AccessLevel.PACKAGE)
	private GameObject meadowNorth;
	@Getter(AccessLevel.PACKAGE)
	private GameObject meadowSouth;
	@Getter(AccessLevel.PACKAGE)
	private GameObject valleyNorth;
	@Getter(AccessLevel.PACKAGE)
	private GameObject valleySouth;

	@Inject
	private OverlayManager overlayManager;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		log.info("Birdhouse Status started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		meadowNorth = null;
		meadowSouth = null;
		valleyNorth = null;
		valleySouth = null;
		log.info("Birdhouse Status stopped!");
	}

	@Provides
	BirdhouseStatusConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BirdhouseStatusConfig.class);
	}

	/**
	 * Get the stored timestamp for when a birdhouse was seeded from Time Tracking plugin.
	 * @param location The birdhouse location
	 * @return The timestamp in milliseconds, or -1 if not found
	 */
	public long getBirdhouseTimestamp(BirdhouseLocation location)
	{
		String key = "birdhouse." + location.getVarpId();
		String data = configManager.getRSProfileConfiguration(TIME_TRACKING_CONFIG_GROUP, key);

		if (data == null || data.isEmpty())
		{
			return -1;
		}

		try
		{
			// Time Tracking stores data as "varpValue:timestamp"
			String[] parts = data.split(":");
			if (parts.length == 2)
			{
				return Long.parseLong(parts[1]);
			}
		}
		catch (NumberFormatException e)
		{
			log.debug("Failed to parse birdhouse timestamp for {}: {}", location, data);
		}

		return -1;
	}

	/**
	 * Check if a birdhouse is done (50 minutes have passed since seeding).
	 * @param location The birdhouse location
	 * @return true if the birdhouse is ready to harvest
	 */
	// At top of class, change:
	public static final long BIRD_HOUSE_DURATION_SECONDS = 50 * 60; // 50 minutes in seconds

	// Then in isBirdhouseDone:
	public boolean isBirdhouseDone(BirdhouseLocation location)
	{
		long timestamp = getBirdhouseTimestamp(location);
		if (timestamp <= 0)
		{
			return false;
		}

		long currentTimeSeconds = System.currentTimeMillis() / 1000;
		long elapsedSeconds = currentTimeSeconds - timestamp;
		return elapsedSeconds >= BIRD_HOUSE_DURATION_SECONDS;
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		GameObject gameObject = event.getGameObject();
		switch (gameObject.getId())
		{
			case MEADOW_NORTH:
				meadowNorth = gameObject;
				break;
			case MEADOW_SOUTH:
				meadowSouth = gameObject;
				break;
			case VALLEY_NORTH:
				valleyNorth = gameObject;
				break;
			case VALLEY_SOUTH:
				valleySouth = gameObject;
				break;
		}
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		GameObject gameObject = event.getGameObject();
		switch (gameObject.getId())
		{
			case MEADOW_NORTH:
				meadowNorth = null;
				break;
			case MEADOW_SOUTH:
				meadowSouth = null;
				break;
			case VALLEY_NORTH:
				valleyNorth = null;
				break;
			case VALLEY_SOUTH:
				valleySouth = null;
				break;
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOADING)
		{
			meadowNorth = null;
			meadowSouth = null;
			valleyNorth = null;
			valleySouth = null;
		}
	}
}