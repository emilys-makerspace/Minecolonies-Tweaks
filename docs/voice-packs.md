## Adding voices or languages

Copy src/main/resources/voiceexpanded-catalogue.json to config/voiceexpanded/catalogue.json to override the catalogue. Each entry has a permanent unique id, gender (male/female/neutral), language (any language code), and descriptive metadata. Add a resource pack defining voiceexpanded:<id>.<event> in assets/voiceexpanded/sounds.json and matching mono OGG Vorbis audio. Supply all 17 events listed in src/main/resources/assets/voiceexpanded/sounds.json. Restart both server and clients after changing the catalogue and keep copies identical. Existing assignments never depend on catalogue order. Do not change IDs of deployed sets.

Registering a new catalogue entry alone does not generate recordings. Offline authors use ElevenLabs; players only install audio. The separate basic Quebec pack is not required.

## Installing the combined mod

Install MineColonies Tweaks on the server and every client for expanded voices. Remove the standalone Voice Expanded jar; its features and audio are included. Existing world assignments and config/voiceexpanded/catalogue.json continue to work. No ElevenLabs account is needed to play.
