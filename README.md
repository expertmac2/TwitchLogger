TwitchLogger
============

A program that logs a Twitch IRC channel. It will output in a human-friendly .txt file and a .json file for easy use with other projects. The dates and times will be in GMT.

Feel free to fork TwitchLogger and do what you wish with it.

## Usage

```
java -jar <the jar> <username> <OAuth token> <channel> <log output directory> [any other args]

Optional Args: 
--timeout       : Disconnects the bot after a certain number of seconds passes
                  without anybody talking. (e.g. --timeout=20 disconnects the 
                  bot if no message is recieved for 20 seconds.)
```

## How do I get my OAuth token?

[Over here! (click me)](http://www.twitchapps.com/tmi/)

## JSON format

```
[

{
"sender":"expertmac2",
"message":"The quick brown fox jumps over the lazy dog.",
"month":12,
"day":1,
"year":2014,
"hours":16,
"minutes":45,
"seconds":9
}

]
```
