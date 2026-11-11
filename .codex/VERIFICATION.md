# Verification

Use Java 21 and the checked-in wrapper from the project root. Start with the smallest check that
covers the change; broader runs require the current task's authorization.

- `./gradlew test`: unit tests.
- `./gradlew build`: compilation, tests, binary jar, and sources jar.
- `./gradlew tasks --all`: confirms development tasks, including `runClient` and `runServer`.
- `./gradlew runClient --dry-run`: validates the client development task graph.
- `./gradlew runSmoke`: creates a fresh flat world under ignored `run-smoke/`, exercises server
  policies and selected client screens, and requires `smoke-result.txt`.
- `./gradlew runServerSmoke`: checks dedicated-server initialization with `--initSettings`; it
  accepts no EULA and starts no world.

Smoke classes must stay in the `smoke` source set and out of release jars. Artifacts live under
ignored `run-smoke/`; screenshots and logs are temporary evidence.

## Release coverage still requiring live testing

- Exercise citizen, hut, and town-hall pages, permissions, and inventory transfer in a real colony.
- Compare pinned Resource HUD counts through work-order changes, reconnects, and dimension changes.
- Locate moving citizens and graves across relevant lifecycle states.
- Start and reset research with both sufficient and insufficient resources and permissions.
- Craft and shift-click cutter outputs on a remote server.
- Reproduce cook behavior with high-level housing, empty stocks, and later raw food/fuel.
- Confirm disabling each replacement restores its upstream presentation.

Historical results are evidence, not current task instructions. See
`history/VERIFICATION_HISTORY.md` when regression archaeology or release history requires them.


## Voice integration — 2026-11-02

- `./gradlew build tasks --all`: passed; six imported voice tests passed.
- `./gradlew runClient --dry-run`: passed; main mod registration and required development dependencies remain configured.
- `./gradlew runServerSmoke`: passed dedicated-server settings initialization.
- Client launched through `runSmoke` and completed resource/audio initialization without logged errors. This checkout has no smoke source classes, so the client was stopped manually; this is startup evidence, not an automated gameplay smoke pass.
- Release jar includes all 391 source OGG files, 23 catalogue sets, sound definitions, mixins, picker assets and the MIT notice. Source voice assets match the standalone project byte for byte; all sound references resolve to bundled audio.
- Still requires a live colony check: trigger adult dialogue, manually choose a voice, reconnect/reload to confirm the assignment, and exercise the picker with the Tweaks citizen screen enabled and disabled.
