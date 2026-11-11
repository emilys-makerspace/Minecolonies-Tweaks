# Project context

GPL-3.0-or-later addon with mod ID `minecoloniestweaks`, currently version `0.1.4`.
Target Java 21, Minecraft 1.21.1, and NeoForge 21.1.252 or newer.

## Dependency pins

- MineColonies 1.1.1406-1.21.1-snapshot.
- Structurize 1.0.837-1.21.1-snapshot.
- BlockUI 1.0.212-1.21.1-snapshot.
- Domum Ornamentum 1.0.237-snapshot, `main` classifier.

Dependencies resolve from Maven and belong on the development runtime classpath. Do not use local
jars, private paths, or the owner's other mods. The vanilla UI implementation is adapted from
`projethub42-creator/minecolonies-vanilla-tweaks` under GPL-3.0-or-later.

## Where to look

- Accepted product scope: `REQUIREMENTS.md`.
- Current architecture: `ARCHITECTURE.md`.
- Current checks and release coverage: `VERIFICATION.md`.
- Historical test evidence: `history/VERIFICATION_HISTORY.md`.
- Source-specific rules: the nearest nested `AGENTS.md`.

Read only the document relevant to the current task. Keep machine-local notes in ignored
`.codex/local/`; do not store credentials, home paths, logs, jars, worlds, screenshots, or chat
history in this directory.


## Integrated voices

Voice Expanded is initialized by the common Tweaks entry point; there is no second mod entry. Its classes live under `ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded`, nested inside the main mod package. Its sound namespace, catalogue path, command, and SavedData key are retained for compatibility. The voice mixin config handles server sound substitution and the upstream citizen-window fallback; Tweaks’ citizen screen also opens the picker. Do not install the standalone jar alongside Tweaks. Preserve its MIT notice in release jars.

The Gradle group is `ca.emilybelnavis.mcmods.minecolonies-tweaks`. Java packages use
`ca.emilybelnavis.mcmods.minecoloniestweaks` (hyphens are not valid Java identifiers), with
matching source paths in each source set.
