package com.birdhousestatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@AllArgsConstructor
@Getter
public enum SeedType
{
    // Hop Seeds
    HAMMERSTONE("Hammerstone", ItemID.HAMMERSTONE_SEED),
    BARLEY("Barley", ItemID.BARLEY_SEED),
    ASGARNIAN("Asgarnian", ItemID.ASGARNIAN_SEED),
    JUTE("Jute", ItemID.JUTE_SEED),
    YANILLIAN("Yanillian", ItemID.YANILLIAN_SEED),
    KRANDORIAN("Krandorian", ItemID.KRANDORIAN_SEED),
    WILDBLOOD("Wildblood", ItemID.WILDBLOOD_SEED),

    // Allotment Seeds
    POTATO("Potato", ItemID.POTATO_SEED),
    ONION("Onion", ItemID.ONION_SEED),
    CABBAGE("Cabbage", ItemID.CABBAGE_SEED),
    TOMATO("Tomato", ItemID.TOMATO_SEED),
    SWEETCORN("Sweetcorn", ItemID.SWEETCORN_SEED),
    STRAWBERRY("Strawberry", ItemID.STRAWBERRY_SEED),
    WATERMELON("Watermelon", ItemID.WATERMELON_SEED),
    SNAPE_GRASS("Snape Grass", ItemID.SNAPE_GRASS_SEED),

    // Flower Seeds
    MARIGOLD("Marigold", ItemID.MARIGOLD_SEED),
    ROSEMARY("Rosemary", ItemID.ROSEMARY_SEED),
    NASTURTIUM("Nasturtium", ItemID.NASTURTIUM_SEED),
    WOAD("Woad", ItemID.WOAD_SEED),
    LIMPWURT("Limpwurt", ItemID.LIMPWURT_SEED),

    // Herb Seeds
    RANARR("Ranarr", ItemID.RANARR_SEED);

    private final String name;
    private final int itemId;

    @Override
    public String toString()
    {
        return name;
    }
}