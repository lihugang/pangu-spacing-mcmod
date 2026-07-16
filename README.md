# Pangu Spacing / 盘古间距

Pangu Spacing is a client-side Fabric mod for Minecraft Java Edition 26.1.

盘古间距是一个面向 Minecraft Java Edition 26.1 的客户端 Fabric 模组。它会在游戏渲染的中西文边界增加透明的排版间距，同时保持实际文本不变。

## Behavior

- Applies to text rendered through Minecraft's `Font`, including screens, HUD, chat, tooltips, books, signs, and entity labels.
- Handles CJK/Western boundaries, brackets, quotes, punctuation, prefixes, and operators such as `+`, `-`, `*`, and `=`.
- Uses the Western side's space-glyph advance, clamped to one quarter through one third of the 9-pixel line height.
- Keeps copied text, chat messages, commands, translations, hover/click styles, and network data unchanged.
- Uses the same spacing rules for drawing, measuring, clipping, centering, cursor placement, and line wrapping.

The implementation is allocation-free per code point: it uses a one-code-point lookahead, primitive Unicode range checks, and cached font glyph data. Mods with their own font renderer are outside the supported rendering path.

## Requirements

- Minecraft Java Edition 26.1
- Fabric Loader 0.19.3 or newer
- Liberica JDK 25

Fabric API is not required and is intentionally not included.

## Development

Build the mod:

```sh
./gradlew build
```

Run the development client:

```sh
./gradlew runClient
```

The release JAR is written to `build/libs/pangu_spacing-0.1.0.jar`.

## License

Copyright (C) lihugang

This project is licensed under the GNU General Public License v3.0 only (`GPL-3.0-only`). See [LICENSE](LICENSE) for the complete terms.
