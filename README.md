# soundboard
A dj keyboard. Intedned to be an external keyboard to be jacked into dj-equipment.

 - can take input, performs FFT on it, uses spectrogram to highlight keys
 - on touching keys, spectrogram is modulated
 - new sound signal is synthesised from modulated spectrogram
 - new signal is sent to another device via OSC
 

Todos:

 - OSC
   - Try this: https://courses.ideate.cmu.edu/16-223/f2014/tutorial-android-osc-communication/ 
   - https://wush.net/trac/lsu-idt/wiki/JavaOSC 

 - MIDI-Output
   - expose available devices in a selection menu
   - update linux kernel to allow midi   
   - Ugh. My §$% lenovo tab doesnt support midi. Could use https://github.com/kshoji/USB-MIDI-Driver 
   
 - Aaudio for in- and output
   - https://github.com/google/oboe/blob/master/GettingStarted.md
   - https://github.com/googlesamples/android-audio-high-performance
   - https://developer.android.com/ndk/guides/audio/aaudio/aaudio.html
   - https://developer.android.com/ndk/guides/audio/audio-latency.html
 - disable mic when double cinch attached
   - might already work with AudioSource = DOWNLINK?
   - else (but requires API 23 => Android 6): setPreferredDevice
   
 - why does input not work most of the time?
   - permissions? order of jacking in?



