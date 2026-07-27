# intellij-nomo

JetBrains Platform file type, fallback syntax highlighter, and
language-server integration for the early-preview
[Nomo language](https://www.nomo-lang.org), built on
[LSP4IJ](https://github.com/redhat-developer/lsp4ij).

## Status and compatibility

The plugin's lexer provides local highlighting when semantic services are not
available. Diagnostics, completion, hover, signatures, symbols, navigation,
rename, code actions, formatting, semantic tokens, and inlay hints come from an
external [`nomo-lsp`](https://github.com/nomo-lang/nomo-lsp) process.

The current packaged plugin is prerelease
[`v0.0.0-20260721120555`](https://github.com/nomo-lang/intellij-nomo/releases/tag/v0.0.0-20260721120555).
The current canonical source contract is plugin commit
[`bf3144e`](https://github.com/nomo-lang/intellij-nomo/commit/bf3144e3995bf3550f44bfcfb7e37ec4c5dde946),
language-server commit
[`708427d`](https://github.com/nomo-lang/nomo-lsp/commit/708427d27891a06d0a9e20b542784fdf01622244),
and compiler commit
[`6acff2b`](https://github.com/nomo-lang/nomo/commit/6acff2bba0113efa3d49254ec2b9c72e1d442b33).
Those `main` commits are newer than the packaged timestamp plugin.

Nomo has no stable `v0.1.0` release. Pin a matched release set or the documented
commit chain.

## Install

Download `intellij-nomo-<version>.zip` from a
[GitHub release](https://github.com/nomo-lang/intellij-nomo/releases). In a
JetBrains IDE, open **Settings → Plugins**, choose **Install Plugin from Disk**,
and select the archive.

Install the matching `nomo-lsp` archive and put `nomo-lsp` on the IDE process
`PATH`. To build the current source contract:

```sh
git clone https://github.com/nomo-lang/nomo-lsp.git
cd nomo-lsp
git checkout 708427d27891a06d0a9e20b542784fdf01622244
cargo install --path . --locked
```

Restart the IDE after changing its inherited `PATH`. The plugin currently
resolves the executable name `nomo-lsp`; it does not expose a custom path
setting.

## Quick verification

Open a compiler-created project:

```sh
nomo new hello-world
```

The plugin should recognize `hello-world/src/main.nomo`:

```nomo
package hello_world

import std.io

fn main() {
    io.println("Hello, Nomo")
}
```

The fallback lexer highlights `suspend`, the `void` value type, and `task` only
when it introduces a callable `fn`. Ordinary variables named `task` remain
identifiers. The language server validates the manifest-derived package root
and provides the canonical signature display.

## Features

- `.nomo` file type and icon registration;
- local highlighting for keywords, primitive and nominal types, strings,
  numbers, comments, attributes, operators, and punctuation;
- compiler-backed diagnostics and quick fixes through LSP4IJ;
- completion, hover, signature help, document/workspace symbols, definition,
  references, and rename;
- formatting, semantic tokens, and inlay hints from `nomo-lsp`.

The local lexer is deliberately not a parser or type checker. When fallback and
semantic highlighting disagree, the pinned compiler/LSP contract is
authoritative.

## Supported IDE baseline

| Component | Baseline |
| --- | --- |
| IntelliJ Platform | 2024.2 (`sinceBuild = 242`) |
| Verification upper endpoint | IntelliJ IDEA 2026.1.4 |
| JDK / toolchain | 21 |
| Gradle wrapper | 9.5.1 |
| LSP4IJ | 0.20.1 |

CI verifies the minimum platform and the bounded upper endpoint. It does not
claim that every intermediate IDE product/version has been manually exercised.

## Development and validation

```sh
./gradlew test --no-daemon
./gradlew buildPlugin --no-daemon
./gradlew verifyPluginProjectConfiguration verifyPlugin --no-daemon
```

`test` covers language-server launch and lexer contracts. `buildPlugin`
produces `build/distributions/intellij-nomo-*.zip`. Plugin Verifier checks
configuration and binary compatibility at the bounded endpoints above.

Launch a sandbox IDE for interactive work with:

```sh
./gradlew runIde --no-daemon
```

The first build downloads the IntelliJ Platform, instrumentation tools, and
LSP4IJ, so it requires network access and significant disk space.

## Release

A signed tag must exactly match the Gradle version, for example
`v0.0.0-20260721120555`. The release workflow builds and verifies the plugin,
attaches the ZIP to a GitHub prerelease for timestamp versions, and publishes
to JetBrains Marketplace only when all signing and publishing credentials are
configured.

Incomplete Marketplace credentials skip Marketplace publication; they do not
turn an unverified build into a release. GitHub remains the artifact-of-record
for the Preview plugin.

## Boundaries and authority

- LSP features depend on a compatible, reachable `nomo-lsp` executable.
- Fallback highlighting accepts incomplete code and is not semantic evidence.
- Plugin Verifier compatibility is not equivalent to full manual testing in
  every JetBrains IDE.
- Internal editor tests do not establish Nomo production readiness.

For normative syntax, consult the
[English specification](https://github.com/nomo-lang/rfcs/blob/main/en/SPEC.md),
[中文规范](https://github.com/nomo-lang/rfcs/blob/main/zh-CN/SPEC.md),
[RFC 0021](https://github.com/nomo-lang/rfcs/blob/main/en/0021-module-system-imports.md),
and
[RFC 0041](https://github.com/nomo-lang/rfcs/blob/main/en/0041-implicit-void-return-omission.md).
Contributions follow the
[shared guide](https://github.com/nomo-lang/.github/blob/main/CONTRIBUTING.md).

## License

See [LICENSE](LICENSE).
