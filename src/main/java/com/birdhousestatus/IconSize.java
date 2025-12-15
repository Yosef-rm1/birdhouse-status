package com.birdhousestatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IconSize
{
    SMALL("Small", 32),
    MEDIUM("Medium", 40),
    LARGE("Large", 48);

    private final String name;
    private final int size;

    @Override
    public String toString()
    {
        return name;
    }
}
