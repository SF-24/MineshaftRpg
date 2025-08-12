# Archived.
## Succeded by: https://github.com/SF-24/MineshaftApi and dependents <br>
(at time of writing: https://github.com/SF-24/MineshaftRpg and https://github.com/SF-24/MineshaftItemPort)

A bunch of RPG features. Some ported from the old [RpgPlugin](https://github.com/SF-24/RpgPlugin) and some completely new. Requires [MineshaftAPI](https://github.com/SF-24/MineshaftAPI) and Java 23.

Licences under GNU Affero GPL v3

# Documentation

*Work in progress. Not complete in any way.*

## Features

The main menu for the plugin can be accessed with the `/menu` chat command.

### Profiles

The plugin supports multiple player profiles. When the player first joins the game, a menu is displayed, where the player can create a profile.

### Levelling

When the player obtains the minimum amount of experience points required to reach a certain level, the player levels up, displaying a level up message and obtaining any specified levelling rewards.
<br><br>The number of levels is not hardcoded and can be modified by adding or removing elements from the `experience-per-elvel` list in the configuration file.

### Configuration

```yaml
# Mineshaft RPG configuration file.

# Worlds in which some features are disabled
# Currently this only includes updating the experience bar
disabled-worlds:
- world1
- world2

default-language: Westron

# BetonQuest event which should be triggered when the player is given their starting items
default-item-bq-event-package: default
default-item-bq-event: give_items

# The number of Skill Points the player starts the game with.
default-skill-points: 15

# The default attribute value.
default-ability-score-value: 8

# Experience required to reach each level,
# This list displays total xp, not from the previous level.
# This list can be extended to allow for more levels
experience-per-level:
- 0        #Lv1
- 300
- 900
- 2700
- 6500     #Lv5
- 14000
- 23000
- 34000
- 48000
- 64000    #Lv10
- 85000
- 100000
- 120000
- 140000
- 165000   #Lv15
- 195000
- 225000
- 265000
- 305000
- 355000   #Lv20

maximum-ability-score-per-level:
1: 15
2: 16
3: 16
4: 16
5: 18
10: 20
15: 20
20: 20

ability-score-increases:
  1: 1
  2: 1
  3: 1
  4: 1
  5: 1
  6: 1
  7: 1
  8: 1
  9: 1
  10: 1
  12: 1
  14: 1
  16: 1
  18: 1
  20: 1
```

