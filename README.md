guido
=====

Guido is a small Java app that listens in a given TCP port, and when someone connects, without reading any input from the socket, responds only 'true' or 'false' if the given files exist or not.

Requirements
------------

Guido requires a **JDK 8** to run, and nothing else. It's built for Windows, but could work on Mac or Linux if the proper startup script is built (the current one, `guido.cmd`, was made for Windows)

Installation
------------

Pack the source files into an executable jar file, mark the class `guido.Main` as the jar entry point. Copy the jar file, the .cmd file (or your own script) and the properties file in the new directory.
Edit the .cmd file and set the location of the properties file, and the log file you want Guido to log into.
You're done. Run the .cmd file and Guido will start listening for requests.

> You can setup Guido to run on system startup. i.e.: you can put a link to your .cmd file inside the 'Startup' menu on Windows.

Did I forget anything...?
-------------------------

Contact me!

@luispablo
