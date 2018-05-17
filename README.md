# soundboard
a dj keyboard

Todos:
 - MIDI-Output
   - https://developer.android.com/reference/android/media/midi/package-summary
   - Merge Board and BoardScene
   - Sound-Analysis and keypresses should be handled by the SoundWrapper, not the board. The board shouldn't access the raw data.
 - Aaudio for in- and output
   - https://github.com/google/oboe/blob/master/GettingStarted.md
   - https://github.com/googlesamples/android-audio-high-performance
   - https://developer.android.com/ndk/guides/audio/aaudio/aaudio.html
   - https://developer.android.com/ndk/guides/audio/audio-latency.html
 - disable mic when double cinch attached
   - might already work with AudioSource = DOWNLINK?
   - else (but requires API 23 => Android 6): setPreferredDevice



