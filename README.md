<p align="center"><a href="https://kitsunetechnologies.org/work"><img src="https://raw.githubusercontent.com/KitsuneTech1/.github/main/assets/kitsune-banner.svg" alt="Built by Kitsune Technologies" width="760"></a></p>

# ShulkerInShulker

Put a shulker box inside another shulker box. Vanilla blocks this, this plugin allows it.

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21-brightgreen.svg)
![Paper](https://img.shields.io/badge/Paper-API-orange.svg)
![Java](https://img.shields.io/badge/Java-21-blue.svg)

## What it does

ShulkerInShulker removes the vanilla restriction that stops you from placing a shulker box inside another shulker box's inventory. It works from both clicking and dragging items into an open shulker box GUI. Nesting depth and whether filled shulkers can be nested are both configurable, and there is a bypass permission for unlimited nesting.

## Features

- Place any shulker box color inside another shulker box, by click or drag
- Optional maximum nesting depth (shulker in shulker in shulker, etc), 0 for unlimited
- Optional restriction to only allow empty shulker boxes to be nested
- Optional chat message shown when a player successfully nests a shulker box
- `shulkerinshulker.bypass` permission ignores the configured nesting depth limit

## Commands

| Command | Aliases | Description | Permission |
|---|---|---|---|
| `/shulkerinshulker` | `/sis`, `/shulkerbox` | Shows plugin status and the current max nesting depth | `shulkerinshulker.use` |
| `/shulkerinshulker reload` | | Reloads config.yml | `shulkerinshulker.admin` |

## Permissions

| Permission | Default | Description |
|---|---|---|
| `shulkerinshulker.use` | true | Nest shulker boxes inside other shulker boxes, and run the base command |
| `shulkerinshulker.bypass` | op | Ignore the configured max nesting depth |
| `shulkerinshulker.admin` | op | Reload the plugin configuration |

## Config

Key options in `config.yml`:

- `max-nesting-depth` - how many levels deep shulkers can be nested, 0 means unlimited
- `show-nesting-message` - whether to chat a message when a player nests a shulker box
- `nesting-message` - the message text, supports `&` color codes
- `allow-filled-shulkers` - whether shulker boxes that already contain items can be nested

## Install

### What you need

- A Spigot or Paper server on Minecraft 1.21. Get Paper at https://papermc.io/downloads . This plugin will not work on vanilla Minecraft or on Realms.
- Java 21. Get it at https://adoptium.net . Check what you have with `java -version` in a terminal.

### Step by step

**Option A: download the jar**

1. Go to the [v1.0.0 release](https://github.com/KitsuneTech1/ShulkerInShulker/releases/tag/v1.0.0) and download `ShulkerInShulker-1.0.0.jar` from the Assets list.

**Option B: build from source**

1. Install a Java 21 JDK from https://adoptium.net if you don't already have one.
2. Clone the repo, or click "Code" then "Download ZIP" on GitHub if you don't use git: `git clone https://github.com/KitsuneTech1/ShulkerInShulker.git`
3. `cd ShulkerInShulker`
4. Run `mvn clean package`
5. The jar lands at `target/ShulkerInShulker-1.0.0.jar`

**Then, for either option:**

1. Drop the jar into your server's `plugins/` folder.
2. Restart the server. Don't use `/reload`, do a full restart. `config.yml` is generated the first time the plugin loads.
3. Verify it worked: run `/shulkerinshulker` in-game or in the console. You should see the plugin's status and its current max nesting depth. You can also check `/plugins`, ShulkerInShulker should be listed in green.

It worked if you can place a shulker box inside another shulker box's inventory and `/shulkerinshulker` shows plugin status instead of "unknown command."

**Troubleshooting**

- `UnsupportedClassVersionError` when starting the server: your Java is too old, install Java 21 from the link above.
- Console warning about an unsupported API version, or the plugin fails to load: your server isn't running Minecraft 1.21 Paper/Spigot.
- Shulker boxes still won't nest after installing: make sure the server fully restarted (not `/reload`) and that your player has the `shulkerinshulker.use` permission (it defaults to true, but permission plugins can override it).

## License

MIT. Copyright (c) 2026 Kitsune Technologies LLC. Do what you like with it, commercially or otherwise, just keep the copyright notice. Full text in [LICENSE](LICENSE).

---

<p align="center"><sub>Part of <b>Buildtober 2026</b>: 31 open source projects in 31 days. Built by <a href="https://kitsunetechnologies.org/work">Kitsune Technologies</a>. More at <a href="https://github.com/KitsuneTech1">github.com/KitsuneTech1</a>.</sub></p>
