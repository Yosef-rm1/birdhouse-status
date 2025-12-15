package com.birdhousestatus;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class BirdhouseStatusPluginTest
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(BirdhouseStatusPlugin.class);
        RuneLite.main(args);
    }
}
