package com.birdhousestatus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ImageUtil;

import static net.runelite.api.VarPlayer.BIRD_HOUSE_MEADOW_NORTH;
import static net.runelite.api.VarPlayer.BIRD_HOUSE_MEADOW_SOUTH;
import static net.runelite.api.VarPlayer.BIRD_HOUSE_VALLEY_NORTH;
import static net.runelite.api.VarPlayer.BIRD_HOUSE_VALLEY_SOUTH;

@Slf4j
public class BirdhouseStatusOverlay extends Overlay
{
    private static final int ICON_HEIGHT_OFFSET = 150;

    private final Client client;
    private final BirdhouseStatusConfig config;
    private final BirdhouseStatusPlugin plugin;
    private final ItemManager itemManager;

    // Cached seed image
    private BufferedImage seedImage;
    private int cachedSeedId;
    private int cachedSeedSize;

    // Warning icon for "needs seeds"
    private BufferedImage warningImage;
    private int cachedWarningSize;

    // Done icon for "ready to harvest"
    private BufferedImage doneImage;
    private int cachedDoneSize;

    @Inject
    BirdhouseStatusOverlay(Client client, BirdhouseStatusConfig config, BirdhouseStatusPlugin plugin, ItemManager itemManager)
    {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.itemManager = itemManager;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        WorldView wv = client.getTopLevelWorldView();
        if (wv == null)
        {
            return null;
        }

        // Update cached images if needed
        int size = config.iconSize().getSize();
        updateSeedImage(size);
        updateWarningImage(size);
        updateDoneImage(size);

        // Check each birdhouse location
        renderBirdhouse(graphics, wv, plugin.getMeadowNorth(),
                client.getVarpValue(BIRD_HOUSE_MEADOW_NORTH), BirdhouseLocation.MEADOW_NORTH);
        renderBirdhouse(graphics, wv, plugin.getMeadowSouth(),
                client.getVarpValue(BIRD_HOUSE_MEADOW_SOUTH), BirdhouseLocation.MEADOW_SOUTH);
        renderBirdhouse(graphics, wv, plugin.getValleyNorth(),
                client.getVarpValue(BIRD_HOUSE_VALLEY_NORTH), BirdhouseLocation.VALLEY_NORTH);
        renderBirdhouse(graphics, wv, plugin.getValleySouth(),
                client.getVarpValue(BIRD_HOUSE_VALLEY_SOUTH), BirdhouseLocation.VALLEY_SOUTH);

        return null;
    }

    private void renderBirdhouse(Graphics2D graphics, WorldView wv, GameObject gameObject,
                                 int state, BirdhouseLocation location)
    {
        if (Objects.isNull(gameObject) || state == 0)
        {
            return;
        }

        WorldPoint worldPoint = gameObject.getWorldLocation();
        LocalPoint lp = LocalPoint.fromWorld(wv, worldPoint);
        if (lp == null)
        {
            return;
        }

        boolean isSeeded = isSeeded(state);
        boolean needsSeeds = needsSeeds(state);

        // Priority: Done > Seeded > Needs Seeds
        if (isSeeded)
        {
            // Check if birdhouse is done (using Time Tracking data)
            boolean isDone = plugin.isBirdhouseDone(location);

            if (isDone && config.showDoneIcon() && doneImage != null)
            {
                // Show green checkmark when ready to harvest
                OverlayUtil.renderImageLocation(client, graphics, lp, doneImage, ICON_HEIGHT_OFFSET);
            }
            else if (config.showSeededIcon() && seedImage != null)
            {
                // Show seed icon while waiting
                OverlayUtil.renderImageLocation(client, graphics, lp, seedImage, ICON_HEIGHT_OFFSET);
            }
        }
        else if (needsSeeds && config.showNeedsSeedsIcon() && warningImage != null)
        {
            // Render warning icon if needs seeds
            OverlayUtil.renderImageLocation(client, graphics, lp, warningImage, ICON_HEIGHT_OFFSET);
        }
    }

    /**
     * Check if birdhouse state indicates it has been seeded.
     * State logic:
     * - state == 0: no birdhouse built
     * - state % 3 == 0 (and state > 0): seeded
     * - else: built but needs seeds
     */
    private boolean isSeeded(int state)
    {
        return state > 0 && state % 3 == 0;
    }

    /**
     * Check if birdhouse needs seeds (built but not seeded).
     */
    private boolean needsSeeds(int state)
    {
        return state > 0 && state % 3 != 0;
    }

    private void updateSeedImage(int size)
    {
        int seedItemId = config.seedType().getItemId();

        if (seedImage == null || cachedSeedId != seedItemId || cachedSeedSize != size)
        {
            BufferedImage image = itemManager.getImage(seedItemId, 5, false);
            if (image != null)
            {
                seedImage = ImageUtil.resizeImage(image, size, size);
                cachedSeedId = seedItemId;
                cachedSeedSize = size;
            }
        }
    }

    private void updateWarningImage(int size)
    {
        if (warningImage == null || cachedWarningSize != size)
        {
            warningImage = createWarningIcon(size);
            cachedWarningSize = size;
        }
    }

    private void updateDoneImage(int size)
    {
        if (doneImage == null || cachedDoneSize != size)
        {
            doneImage = createDoneIcon(size);
            cachedDoneSize = size;
        }
    }

    /**
     * Creates a simple exclamation mark warning icon.
     */
    private BufferedImage createWarningIcon(int size)
    {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Yellow/orange background circle
        g.setColor(new Color(255, 180, 0));
        g.fillOval(0, 0, size, size);

        // Dark border
        g.setColor(new Color(100, 70, 0));
        g.drawOval(0, 0, size - 1, size - 1);

        // Black exclamation mark
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, (int)(size * 0.7)));
        FontMetrics fm = g.getFontMetrics();
        String text = "!";
        int textX = (size - fm.stringWidth(text)) / 2;
        int textY = (size - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, textX, textY);

        g.dispose();
        return image;
    }

    /**
     * Creates a green checkmark icon for completed birdhouses.
     */
    private BufferedImage createDoneIcon(int size)
    {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Green background circle
        g.setColor(new Color(34, 177, 76));
        g.fillOval(0, 0, size, size);

        // Dark green border
        g.setColor(new Color(0, 100, 0));
        g.drawOval(0, 0, size - 1, size - 1);

        // White checkmark
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, (int)(size * 0.7)));
        FontMetrics fm = g.getFontMetrics();
        String text = "✓";
        int textX = (size - fm.stringWidth(text)) / 2;
        int textY = (size - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, textX, textY);

        g.dispose();
        return image;
    }
}