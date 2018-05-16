# soundboard
a dj keyboard

Todos:
 - MIDI-Output
   - https://source.android.com/devices/audio/midi
   - https://stackoverflow.com/questions/36193250/android-6-0-marshmallow-how-to-play-midi-notes
   - Merge Board and BoardScene?
   - Sound-Analysis and keypresses should be handled by the SoundWrapper, not the board. The board shouldn't access the raw data. 
 - Aaudio for in- and output
   - https://github.com/google/oboe/blob/master/GettingStarted.md
   - https://github.com/googlesamples/android-audio-high-performance
   - https://developer.android.com/ndk/guides/audio/aaudio/aaudio.html
   - https://developer.android.com/ndk/guides/audio/audio-latency.html
 - disable mic when double cinch attached
   - might already work with AudioSource = DOWNLINK?
   - else (but requires API 23 => Android 6): setPreferredDevice



