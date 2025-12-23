## Heimdall - No hacked Minecraft accounts.

[简体中文](./README-zh.md)

Heimdall is a solution for protecting Minecraft servers from being sabotaged by hacked Minecraft accounts. By periodically verifying the ownership of the Microsoft account associated with the Minecraft profile, Heimdall ensures that the player logging in is the legitimate owner of the account. This protects Minecraft servers from malicious activities associated with hacked Minecraft accounts and upholds the rights of players and the community.

Heimdall provides a Web service and an HTTP API:

- [Web Service](https://heimdall.honoka.cafe): Used by players to verify their Minecraft accounts.
- [HTTP API](https://petstore.swagger.io/?url=https://heimdall.honoka.cafe/openapi.yml): Used by server plugins and third-party applications to query the verification status of Minecraft accounts.

This repository contains the source code for the Heimdall Minecraft server plugin. The plugin is responsible for querying the Heimdall API when a player joins and determining whether to allow them in based on their verification status.

The Heimdall plugin supports multiple Minecrfat server platforms, including:

- Spigot / Paper and their forks
  - Supports Minecraft 1.8 or higher
- BungeeCord / Waterfall
  - Supports Build #1383 or higher
- Velocity
  - Supports version 3.1.0 or higher

### Get the Plugin

You can get the latest version of plugin from the [Releases](https://github.com/LittleSkinChina/Heimdall/releases) page.

**Heimdall plugin is cross-platform compatible.** The single JAR file works across all supported server platforms.

### Usage

Before installing the Heimdall plugin, please ensure that your Minecraft server has online mode enabled and only accepts players authenticated by Mojang.

1. [Download the plugin](#get-the-plugin) and put the JAR file into your server's `plugins/` directory.
2. Restart the server. Now the plugin should be installed and running.
3. The plugin will generate a configuration file `config.yml` and a language file `lang.yml` in the `plugins/Heimdall/` or `plugins/heimdall/` directory. You can modify entries in these two files as needed.

Once installed, all players must verify their Minecraft account on Heimdall before they can join the server.

Note for proxy servers: it's only need to install Heimdall plugin on the proxy (BungeeCord / Waterfall / Velocity). There is no need to install the plugin on backend servers.

### Configuration & Localization

Depending on the server platform, the plugin will generate a configuration file `config.yml` and a language file `lang.yml` in either the `plugins/Heimdall/` or `plugins/heimdall/` directory. You can modify entries in these two files as needed.

After editing the configuration or language file, please restart the server or execute `/heimdall reload` to reload the configuration and language files.

[](#Configuration--Localization)

#### Configuration File (`config.yml`)

```yml
# After editing the configuration or language file, please restart the server or execute "/heimdall reload" to reload the configuration.
# For more information about these configurations, please visit:
# https://github.com/LittleSkinChina/Heimdall/blob/master/README.md#Configuration--Localization

# API Root for Heimdall service
# DO NOT EDIT THIS UNLESS YOU KNOW WHAT YOU ARE DOING
api-root: https://heimdall.honoka.cafe/api
# Bearer Token for authenticating with Heimdall API
# For most servers it's not required, you can just leave it empty
bearer-token: ''
# How often the player should be re-verified (in days)
# Must be an integer between 30 and 180
verification-validation: 90
# Block players with Outlook email addresses from joining the server?
# Please visit https://heimdall.honoka.cafe/#outlook-note for the reason behind this restriction.
block-outlook: false
# How to handle joining requests when Heimdall service is unavailable
# - ALLOW: Allow all joining requests disregarding verification status
# - DENY: Deny all joining requests
offline-action: DENY
```

#### Language File (`lang.yml`)

```yml
# MiniMessage format is supported for kick messages.
# For more information about MiniMessage format, please visit: https://docs.papermc.io/adventure/minimessage/format
# Note: Interactions is not available in kick messages due to the restriction of Minecraft client.
# 如需获取中文语言文件，请参阅：https://github.com/LittleSkinChina/Heimdall/blob/master/README-zh.md#%E8%AF%AD%E8%A8%80%E6%96%87%E4%BB%B6langyml

console:
    initializing: Heimdall plugin initializing...
    initialized: Heimdall plugin initialized.
    reloaded: Heimdall plugin configuration reloaded.
    invalid-api-root: Invalid Heimdall API Root provided. Please check your Heimdall plugin configuration.
    invalid-response: Unable to parse response from Heimdall API. Please check your network connection and the status of Heimdall service.
    request-error: An error occurred while requesting Heimdall API. Please check your network connection and the status of Heimdall service.
    service-unavailable: Heimdall API is currently unavailable.
    service-unavailable-allowed-join: Player is allowed to join the server by your configuration.
    rate-limit-exceeded: Heimdall API rate limit exceeded. Please try again later. If the issue persists, please contact LittleSkin Support to request a higher rate limit.
    not-online-mode: '------

        The server is running in offline mode.

        Heimdall is designed for servers running in online mode. Using Heimdall with
        offline mode will block all players from joining the server.

        To avoid unexpected behaviors, the server will shut down. Please enable online
        mode or remove Heimdall plugin before starting the server again.

        ------'

kick:
    not-verified: |
        <b><yellow>This Minecraft account is not verified or the verificationhas expired.</yellow></b>
        <newline>
        <newline>
        To join the server, please verify your account first at:
        <newline>
        <aqua>https://heimdall.honoka.cafe</aqua>
    hacked: |
        <b><red>This Minecraft account has been marked as hacked and not allowed to join the server.</red></b>
        <newline>
        <newline>
        For more information, please visit:
        <newline>
        <aqua>https://heimdall.honoka.cafe</aqua>
    outlook: |
        <b><yellow>This Minecraft account is link to an Outlook email address and not allowed to join the server.</yellow></b>
        <newline>
        <newline>
        To learn more about this restriction and update your email address, please visit:
        <newline>
        <aqua>https://heimdall.honoka.cafe/#outlook-note</aqua>
    request-error: |
        <yellow>An error occurred while verifying your Minecraft account. Please try again later.</yellow>
        <newline>
        <newline>
        If the issue persists, please contact the server administrators.
    service-unavailable: <yellow>Heimdall service is currently unavailable. Please try again later.</yellow>

```

### Build the Plugin

Requires Gradle 9+ and JDK 11+.

Execute the following command in terminal to build the plugin:

```sh
./gradlew clean :bundle:shadowJar
```

The artifact is located in `bundle/build/libs/`.

### Telemetry

Heimdall server plugin uses [bStats](https://bstats.org) to collect anonymous usage data. In addition, the plugin version, server platform name and version, and Java version used to run the server is included in the `User-Agent` of HTTP requests sent by the plugin.

Other than those, Heimdall server plugin does not collect any telemetry data.

### Copyright

Copyright (c) 2026-present Suzhou Honoka Technology Co., Ltd. Made with <3 by [LittleSkin](https://littleskin.cn).

_NOT AN OFFICIAL MINECRAFT PRODUCT. NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT._

The source code of Heimdall server plugin is licensed under the MIT License. See the LICENSE file for more details.

If you like the services we provide, please consider sponsor us: [Afdian](https://afdian.com/a/tnqzh123) (PayPal & Stripe is supported). Any amount of sponsor is a huge support and help for us.
