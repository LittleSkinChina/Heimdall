## Heimdall - Minecraft 服务器反黑卡解决方案

[English](./README.md)

Heimdall 是一个 Minecraft 服务器反黑卡解决方案。通过周期性地验证 Minecraft 账号对应的 Microsoft 帐户的归属，确保当前登录的玩家是该账号的合法拥有者，从而保护 Minecraft 服务器免受黑卡账号的破坏，维护 Minecraft 社区和正版玩家的权益。

Heimdall 提供了一个 Web 服务和一个 HTTP API：

- [Web 服务](https://heimdall.honoka.cafe) 用于玩家验证 Minecraft 账号
- [HTTP API](https://petstore.swagger.io/?url=https://heimdall.honoka.cafe/openapi.yml) 用于服务器插件和第三方应用查询 Minecraft 账号的验证结果。

这个仓库存放了 Heimdall 的 Minecraft 服务器插件的源代码。该插件负责在玩家进入服务器时向 Heimdall API 查询账号验证结果，并决定是否放行。

Heimdall 服务器插件支持多种 Minecraft 服务端平台，包括：

- Spigot / Paper 及其分支
  - 支持 Minecraft 1.8 或更高版本
- BungeeCord / Waterfall
  - 支持 Build #1383 或更新版本
- Velocity
  - 支持 3.1.0 或更高版本

### 获取插件

你可以在 [Releases](https://github.com/LittleSkinChina/Heimdall/releases) 页面下载到最新版本的插件。

**Heimdall 服务端插件采用跨平台兼容设计，不区分特定的服务端平台版本**，单一插件 JAR 文件即适用于所有支持服务端平台。

### 使用方法

在安装 Heimdall 服务器插件之前，请确保你的 Minecraft 服务器已启用在线模式（`online-mode`），且仅接受正版 Minecraft 玩家进入。

1. [下载插件](#获取插件)，将插件 JAR 文件放入服务端的 `plugins/` 目录下。
2. 重启服务端。此时插件已完成安装，并已开始工作。
3. 插件会在 `plugins/Heimdall/` 或 `plugins/heimdall/` 目录下生成配置文件 `config.yml` 和语言文件 `lang.yml`，你可以根据需要修改这两个文件中的条目。

安装 Heimdall 服务器插件后，所有玩家在进入服务器前都需要通过 Heimdall 的 Minecraft 账号验证。

对于群组服，只需在代理端（BungeeCord / Waterfall / Velocity）安装 Heimdall 服务器插件即可，无需在后端子服上安装。

### 插件配置 & 本地化

根据服务端平台的不同，插件会在 `plugins/Heimdall/` 目录或 `plugins/heimdall/` 目录下生成配置文件 `config.yml` 和本地化语言文件 `lang.yml`，你可以根据需要修改这两个文件中的条目。

编辑配置文件或语言文件后，请重启服务器，或执行 `/heimdall reload` 命令来重新读取配置文件和语言文件。

[](#插件配置--本地化)

#### 配置文件（`config.yml`）

```yml
# 编辑配置文件或语言文件后，请重启服务器，或执行 "/heimdall reload" 命令来重新加载配置。
# 要了解关于这些配置项的更多信息，请访问：
# https://github.com/LittleSkinChina/Heimdall/blob/master/README-zh.md#%E6%8F%92%E4%BB%B6%E9%85%8D%E7%BD%AE--%E6%9C%AC%E5%9C%B0%E5%8C%96

# Heimdall API 的 API Root 地址
# 除非你知道你在做什么，否则请不要更改这一项
api-root: https://heimdall.honoka.cafe/api
# 用于 Heimdall API 身份认证的 Bearer Token
# 对于大多数服务器来说都是不需要的，留空即可
bearer-token: ''
# 玩家的 Minecraft 账号验证的有效期，单位为天
# 必须是 30 至 180 之间的整数
verification-validation: 90
# 风控策略：是否拦截使用 Outlook 邮箱作为 Microsoft 帐户主用户名的玩家进服
# 详情参考：https://heimdall.honoka.cafe/#outlook-note
block-outlook: false
# 当 Heimdall 服务不可用时，如何处理玩家的进服请求
# - ALLOW: 允许所有玩家进入服务器
# - DENY: 禁止所有玩家进入服务器
offline-action: DENY
```

#### 语言文件（`lang.yml`）

```yml
# 踢出消息支持 MiniMessage 格式
# 详见：https://docs.papermc.io/adventure/minimessage/format
# 注意：由于 Minecraft 客户端的限制，踢出消息不支持交互元素
# For English language file, please visit: https://github.com/LittleSkinChina/Heimdall/blob/master/README.md#language-file-langyml

console:
    initializing: Heimdall 插件加载中…
    initialized: Heimdall 插件已加载。
    reloaded: Heimdall 插件配置文件已刷新。
    invalid-api-root: API Root 无效，请检查你的 Heimdall 插件配置文件。
    invalid-response: 无法解析 Heimdall API 的响应，请检查服务器网络连接和 Heimdall 服务的状态。
    request-error: 请求 Heimdall API 时发生错误，请检查服务器网络连接和 Heimdall 服务的状态。
    service-unavailable: Heimdall API 暂时不可用，请稍后再试。
    service-unavailable-allowed-join: 已根据插件配置文件允许玩家进入服务器。
    rate-limit-exceeded: 触发 Heimdall API 请求频率限制，请稍后再试。如频繁出现此情况，请联系 LittleSkin 支持，申请更高的请求频限。
    not-online-mode: '------

        服务器当前处于离线模式，Heimdall 插件无法正常工作。

        Heimdall 插件是为在线模式的服务器设计的，在离线模式的服务器中使用 Heimdall 插件会导致所有玩家都无法进入服务器。

        为避免预期外的行为，服务端将自动关闭。请在重启服务端前启用在线模式，或移除 Heimdall 插件。

        ------'

kick:
    not-verified: |
        <b><yellow>你的 Minecraft 账号尚未通过 Heimdall 验证，或验证已过期。</yellow></b>
        <newline>
        请使用浏览器访问以下地址，完成 Minecraft 账号验证：
        <aqua>https://heimdall.honoka.cafe</aqua>
    hacked: |
        <b><red>你的 Minecraft 账号是黑卡账号，禁止进入服务器。</red></b>
        <newline>
        如需了解更多信息，请访问：
        <aqua>https://heimdall.honoka.cafe</aqua>
    outlook: |
        <b><yellow>安全策略限制：你的 Minecraft 账号对应的 Microsoft 帐户使用了 Outlook 邮箱作为主用户名，因此暂时无法进入服务器。</yellow></b>
        <newline>
        如需了解关于该限制的更多信息，以及更新 Microsoft 帐户主用户名的方法，请参阅：
        <aqua>https://heimdall.honoka.cafe/#outlook-note</aqua>
    request-error: |
        <yellow>验证 Minecraft 账号时发生错误，请稍后再试。</yellow>
        <newline>
        如果问题持续存在，请联系服务器管理员。
    service-unavailable: <yellow>Heimdall 服务当前不可用，请稍后再试。</yellow>
```

### 构建插件

要求 Gradle 9+ 和 JDK 11+。

在终端中执行以下指令以构建本插件：

```sh
./gradlew clean :bundle:shadowJar
```

构建产物位于 `bundle/build/libs/`。

### 遥测数据收集

Heimdall 服务器插件使用 [bStats](https://bstats.org) 收集匿名的插件使用数据。此外，插件发起的 HTTP 请求的 `User-Agent` 头部中会包含插件版本号和服务端平台的名称和版本号，以及用于运行服务端的 Java 运行时的版本。

除此之外，Heimdall 服务器插件不收集任何遥测数据。

### 版权信息

Copyright (c) 2026-present Suzhou Honoka Technology Co., Ltd. Made with <3 by [LittleSkin](https://littleskin.cn).

_非 Minecraft 官方产品。未经 Mojang 或 Microsoft 批准，也不与 Mojang 或 Microsoft 关联。_

Heimdall 服务器插件以 MIT 协议开源。我们相信开源社区 \:\)

如果你喜欢我们提供的服务，请考虑捐助支持我们：[爱发电](https://afdian.com/a/tnqzh123)，任意金额的捐助都是对我们巨大的支持和帮助。
