package com.birdhousestatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static net.runelite.api.VarPlayer.BIRD_HOUSE_MEADOW_NORTH;
import static net.runelite.api.VarPlayer.BIRD_HOUSE_MEADOW_SOUTH;
import static net.runelite.api.VarPlayer.BIRD_HOUSE_VALLEY_NORTH;
import static net.runelite.api.VarPlayer.BIRD_HOUSE_VALLEY_SOUTH;

@AllArgsConstructor
@Getter
public enum BirdhouseLocation
{
    MEADOW_NORTH(BIRD_HOUSE_MEADOW_NORTH),
    MEADOW_SOUTH(BIRD_HOUSE_MEADOW_SOUTH),
    VALLEY_NORTH(BIRD_HOUSE_VALLEY_NORTH),
    VALLEY_SOUTH(BIRD_HOUSE_VALLEY_SOUTH);

    private final int varpId;
}