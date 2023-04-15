Note: This is a fork of Spigot plugin YAGHGP, which I am using as the basis of a personal project. Trying to recreate the MCPVP 2011-2013 era type of hunger games mode :)

# MC-HG - Golden Era Hunger Games

This plugin hosts the hunger games in a highly customisable way, with many features:

- Wide range of selectable kits
  - Add your own kits if you know a bit of Java!
- The Feast
- Random map option / premade map option
- Option to enable / disable loot chests, with 'Rare' loot chest functionality
  - Loot is also entirely customisable per map
- Deathmatch phase

## Dependencies

- Lib's Disguises (& ProtocolLib) for Kit Chameleon
- WorldEdit for feasts

## What is MC-HG?
MC-HG was a gamemode hosted by MCPVP which peaked in popularity back in 2011-2013. Back then, 120 player servers would be filled up in a matter of minutes to play this gamemode.
There's not much of this scene left, but I look back fondly on this era of Minecraft, which is why I decided to make a plugin which emulates (and improves upon) what was done back then.

### Development roadmap:
 * [x] Basic functionality for hosting a randomly generated world-type hunger games game
 * [x] Soup PvP
 * [x] Soup custom crafting recipes
 * [x] Better UI for lobby
 * [x] Lobby voting between random map and pre-made map selections
 * [x] Random world generation modification
 * [x] Kits
 * [x] Loot management for custom maps
 * [x] Feasts
 * [ ] Allow users to join during a game in progress and be sent to lobby, with option to spectate
 * [ ] Better UI for in-game (?)

### Development notes

This plugin uses NMS to modify the default terrain generation in Minecraft, please see [here](https://www.spigotmc.org/threads/spigot-bungeecord-1-17-1-17-1.510208/#post-4184317) for info on how to set this up for development.
 

## Original README:

## YAGHGP - Yet Another Generic Hunger Games Plugin
### What is YAGHGP?
Yet Another Generic Hunger Games Plugin (abbreviated as YAGHGP) is a Hunger Games plugin created and updated for modern Minecraft versions featuring high customizability, extensive support and guaranteed compatibility with the latest Minecraft version.
### Installation
#### Requirements
* Java 17 or later
* Spigot or many of its forks
#### Installation procedure
Place the .jar file from our [releases section](https://github.com/therealdgrew/AntiAFK/releases) or [SpigotMC page](https://www.spigotmc.org/resources/yaghgp-yet-another-generic-hunger-games-plugin.106792/) into your server's plugins folder. After a server restart, you will be able to modify the plugin's configuration file, which is located in your plugins/YAGHGP folder.
### Support
If you're an ordinary user of this plugin and if you just want to report a bug or suggest a feature, then you can join our [support Discord server](https://discord.gg/Hpj7qEhDEC) or head over to the project's [issue tracker](https://github.com/therealdgrew/YAGHGP/issues) to do so.
### Contributions
While I certainly have made plugins before, YAGHGP is my very first large minigame plugin, so all contributions that can improve upon the concept of the plugin are appreciated. The best way to contribute is via [pull requests](https://github.com/therealdgrew/YAGHGP/pulls).
