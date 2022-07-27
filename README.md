# BeMayor - Minecraft Spigot Plugin

## 关于

- 主旨：该插件旨在多人游戏中增强原版的多元游戏性。
- 组件：原版修改、付费修理、便携工具、钻石银行
- 基础：指令、自定义物品、异常信息、配置文件
- 依赖：CoreProtectAPI、VaultAPI


- **核心开发者**
    - [manufacturer](https://gitee.com/wyzxmlehd)
    - [ConquerSki](https://github.com/ConquerSki)

## 插件介绍

- 目前框架包含4样东西：
  - 自定义物质管理器CustomItemManager
  - 基础界面CustomGui、WorkbenchGui（专用于工作台类界面）
  - 自定义指令PlayerCommand、SubCommand、GiveCommand\OnOffCommand\SetIntCommand\SetDoubleCommand（专用于给与玩家自定义物品\设置组件的功能开关或值）
  - 调用也可以发送预设在配置文件messages.yml中的文本ChatMessages
- 以上皆通过框架管理器apiManager调用


- 目前框架提供的工具有：
  - ChatUtils
  - CommonPatterns
  - ItemMetaBuilder
  - ItemStackUtils
  - LoreBuilder
  - PlayerUtils
  - ConfigUtils（用于保证配置文件的存在）
- 以上在api.common目录中

## 许可证

**BeMayor** 的代码采用 [MIT-licensed](LICENSE).

- **请注意：** 所有希望使用我们的框架的第三方应用程序开发人员，请尊重 MIT 许可，向 BeMayor 及其开发人员致谢。

## 致谢

- [CoreProtect API](https://docs.coreprotect.net/api/) - 用于验证方块自然生成。
- [VaultAPI](https://github.com/MilkBowl/VaultAPI) - 插件内的经济系统支持。
- [Triumph GUI](https://triumphteam.dev/library/triumph-gui/introduction) - 插件内的GUI界面支持。

## 向本项目贡献

- 对 **BeMayor** 的持续开发感兴趣的任何人，请向我们发送 PR，我们将对其进行审核。
- 这个项目是100%开源的，任何人都可以按照自己的意愿免费使用和修改。
- 我们只对 **BeMayor** 主分支master进行维护开发和支持。
