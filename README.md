# Pangu Spacing / 盘古之白

Automatically inserts spaces between CJK (Chinese, Japanese, Korean) characters and Latin alphabets/numbers in Minecraft for a cleaner, more readable chat and UI.

自动在 Minecraft 的中日韩字符（CJK）与拉丁字母、数字之间插入空格（盘古之白），告别挤作一团的排版，给你的游戏 UI 和聊天框带来呼吸感。

## Supports / 支持版本

- Minecraft 26.x Fabric

We plan to port this mod to older Minecraft versions and add (Neo)Forge support.

我们有将此 Mod 移植到 (Neo)Forge 和更老的 Minecraft 版本的计划。

## Introduction / 简介

In Chinese, Japanese, and Korean (CJK) typesetting, it is a common aesthetic practice to leave a blank space between CJK characters and Latin characters (letters, numbers, and symbols) — a concept affectionately known in the Chinese community as "Pangu's Whitespace" (盘古之白).

This mod automatically applies this spacing rule to Minecraft's text rendering, making your in-game text, chat, tooltips, and menus look beautifully formatted and much easier to read.

在中文排版审美中，在中文字符与英文字母、数字之间留出一个空格的间距（俗称“盘古之白”），能够极大地提升文本的可读性和视觉美感。

本模组通过拦截并自动处理游戏内的文本渲染，将这一排版规则带入 Minecraft。无论是聊天栏、物品提示（Tooltip）、公告、书籍还是各类 GUI 界面，都能自动呈现出最优雅的排版效果。

## Effects / 效果

Before / 安装 Mod 前：

![Before](https://github.com/user-attachments/assets/ad7fe336-f231-4f1f-b0fe-9e00717e6497)

After / 安装 Mod 后：

![After](https://github.com/user-attachments/assets/b94c2537-c18a-45f3-a93e-d4afadb010e6)

## Features / 特性：

- Real-time Formatting: Automatically formats chat messages, item tooltips, books, and GUI text.

- Client-side Only: Runs entirely on the client. You can join any vanilla or modded server with this mod installed.

- Lightweight & High Performance: Minimal impact on game performance, utilizing efficient regex/character checking.

- In-game Commands: Quickly check the status with `/pangu` or toggle the spacing on/off on the fly using `/pangu toggle` without restarting the game.

Chinese translation for features:

- 实时格式化：无缝支持聊天信息、物品提示、服务器公告、书籍、甚至其他模组的 GUI 文本。

- 纯客户端模组：无需服务器支持。安装后，你可以带着它加入任何原版或跨版本服务器，不影响正常联机。

- 轻量且无感：采用高效的字符匹配算法，对游戏帧率和文本渲染性能几乎零影响。

- 便捷命令： 支持在游戏内使用命令 `/pangu` 查看当前启用状态，支持输入 `/pangu toggle` 一键开关模组。

## Installation / 安装方法：

1. Make sure you have [Fabric / Forge / NeoForge] installed.
2. Download the latest `.jar` file from the Modrinth [Modrinth Page Link Placeholder].
3. Drop the file into your `.minecraft/mods` folder.
4. Launch the game and enjoy the beautiful typography!

Chinese translation for installation methods:

1. 请确保你已安装 [Fabric / Forge / NeoForge] 对应版本的加载器。
2. 前往 Modrinth 页面下载最新的 `.jar` 文件。
3. 将下载的文件放入你游戏目录的 `.minecraft/mods` 文件夹中。
4. 启动游戏，享受盘古之白吧！

## License / 协议

Contributions, issues, and feature requests are welcome! Feel free to check the [issues](https://github.com/lihugang/pangu-spacing-mcmod/issues) page.

This project is licensed under the GNU General Public License v3.0 only (`GPL-3.0-only`). See [LICENSE](LICENSE) for the complete terms.

欢迎提交 Bug 反馈、功能建议或 Pull Request！如果你有任何想法，请随时提交 [Issue](https://github.com/lihugang/pangu-spacing-mcmod/issues)。

本项目采用 GPL v3.0 协议，完整条款请见 [LICENSE](LICENSE).
