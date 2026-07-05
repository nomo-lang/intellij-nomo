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

Build and install the language server so it is on your `PATH`:

```bash
cd ../nomo-lsp
cargo install --path .
```

The plugin looks up `nomo-lsp` on the `PATH`.

## Development

```bash
./gradlew runIde      # launch a sandbox IDE with the plugin
./gradlew buildPlugin  # produce build/distributions/intellij-nomo-*.zip
```

The first build downloads the IntelliJ Platform and the LSP4IJ plugin, which can
take a while.
