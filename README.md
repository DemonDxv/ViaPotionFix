# ViaPotionFix
A lightweight, high-performance packet interceptor that fixes broken potion consumption and splashing for modern Minecraft clients playing on legacy servers using ViaVersion.

## Small Backstory:
If you are here, you’ve probably experienced the same issues I did. 
**Modern clients (1.20.5+) joining a 1.8.9 server** would grab a potion, drink it or splash it, and... nothing would happen. No effects applied, no server response, nothing.
I was completely convinced it was an issue related to my anticheat, trying multiple different things, until I tested on multiple networks, all with the same issue.
After digging deep into the packet protocol, and deleting multiple plugins, until I came to the realization it was **ViaVersion** all along.
So I went out of my way, and fixed it and released this here on GitHub for anyone to use.

## The Root Cause
In Modern Versions of Minecraft, Mojang completely overhauled how items work, moving from NBT tags to Data Components and etc..
When **ViaVersion** translates these modern items backward through 10+ years of updates, it tries its best, but a critical issue occurs
### The Ghost NBT Bug: 
When players pull potions from the Creative menu, ViaVersion assigns the correct 1.8.9 damage value but accidentally attaches an empty CustomPotionEffects=List([]) NBT tag. The 1.8.9 server prioritizes NBT over damage values, sees a list of zero effects, and literally applies zero effects to the player.

## The Solution
[**ViaPotionFix**](https://github.com/DemonDxv/ViaPotionFix/releases/tag/Release) corrects these translation gaps at the packet level using **PacketEvents**, completely bypassing the Bukkit API to ensure zero conflict with anticheats or custom plugins.
Using **PacketEvents**, we intercept ``CREATIVE_INVENTORY_ACTION`` and ``PLAYER_BLOCK_PLACEMENT`` packets, and if a potion contains the corrupted **ViaVersion** ghost tag, it is completely deleted out before the server processes it.

## Requirements
To run this plugin, your server must have the following dependencies installed:
* [PacketEvents: 2.11.x](https://github.com/retrooper/packetevents/) *(Used for packet interception)*
