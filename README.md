# intellij-nomo

[Nomo](https://github.com/nomo-lang) language support for JetBrains IDEs
(IntelliJ IDEA, CLion, GoLand, etc.), built on
[LSP4IJ](https://github.com/redhat-developer/lsp4ij).

## Features

- Diagnostics, completion, hover, document/workspace symbols, go-to-definition,
  references, rename, formatting and semantic highlighting via the
  [`nomo-lsp`](https://github.com/nomo-lang/nomo-lsp) language server
- Quick fixes for compiler suggestions, missing imports and module/package mismatches
- Inlay hints for inferred `let` binding types and same-file function/method parameter names
- `.nomo` file type registration

## Requirements

Install the language server archive for your platform from the
[`nomo-lsp` releases](https://github.com/nomo-lang/nomo-lsp/releases), extract
it, and place the executable on your `PATH`. To build it from source, clone
the `nomo-lsp` repository, then run:

```bash
git clone https://github.com/nomo-lang/nomo-lsp.git
cd nomo-lsp
cargo install --path . --locked
```

The plugin looks up `nomo-lsp` on the `PATH`.

## Install

Release builds attach an installable `intellij-nomo-<version>.zip` archive to
the matching GitHub release. In a JetBrains IDE, open **Settings > Plugins**,
choose **Install Plugin from Disk**, and select that archive. After Marketplace
publication, search for **Nomo** in the Marketplace tab instead.

## Development

```bash
./gradlew runIde      # launch a sandbox IDE with the plugin
./gradlew buildPlugin  # produce build/distributions/intellij-nomo-*.zip
```

The first build downloads the IntelliJ Platform and the LSP4IJ plugin, which can
take a while.

`verifyPlugin` runs JetBrains Plugin Verifier against the minimum targeted
IntelliJ IDEA 2024.2 platform and IntelliJ IDEA 2026.1.4. This bounded endpoint
matrix avoids the unbounded disk use of resolving every intermediate IDE line.
Tag releases require a tag matching the Gradle version, such as `v0.1.0`.
Marketplace publication uses `PUBLISH_TOKEN`, `CERTIFICATE_CHAIN`,
`PRIVATE_KEY`, and `PRIVATE_KEY_PASSWORD`; the release workflow maps these from
the corresponding JetBrains repository secrets before running `publishPlugin`.
Incomplete credentials skip Marketplace publication without failing the GitHub
release artifact.
