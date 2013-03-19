Note: all paths are relative to project root directory.

*To compile*

javac -cp 'lib/*'-d \[build output dir\] \[java files under src\]

(on linux w/ bash: javac -cp 'lib/*' -d [build output dir] \`find src -name '*.java' \` )

*To start the server*

java -cp 'lib/*:\[build output dir\]' com.Betable.BJServer

*How to play*

The game can be played through a simple html interface by pointing your browser to

http://\[server address\]:8080/play.html,

or through the command line (e.g. curl or wget). For example when playing through wget do:

wget 'http://\[server address\]:8080/?op=\[operation\]&gid=\[game id\]&pid=\[pid\]' -O -

(please keep the gid and pid parameters even if they are blank; the -O - option dumps the response to stdout
like curl)

A simple help message can be obtained at the url:

'http://\[server address\]:8080/?op=Help&gid=&pid='

On the actual game play, first we need a game handle. Game handles are unique 64 bit signed longs that can be
retrieved either with the 'List' command, which returns a list of available games, or the 'Add' command, which
returns the id of the newly added game (if successful).

To join a game, issue the 'Join' command with a game id parameter, which would return a player id (a 64 bit signed
long unique in each game) and player position when successful. A game is started by the 'Start' command, which also
takes a game id parameter.

Once a game is started, a player can issue 'Hit' or 'Stand' commands at his/her/its turn. 'Hit' and 'Stand' take
game id and player id (not player position) as parameters. The server issues a 'Stand' automatically for players
idle for over 30 seconds. To check the current game state, use the 'Get' command with the game id. The 'Win'
command queries the winner of the supplied game id.
