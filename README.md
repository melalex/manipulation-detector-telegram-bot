# Manipulation detector telegram bot

## Description

A Telegram bot designed for group chats that automatically detects manipulation or persuasive language spans within user messages. When manipulation is detected, the bot replies directly to the user's message, highlighting the manipulative parts with underlined text to raise awareness and promote healthier conversations.

Features:
* Real-time manipulation detection: Analyzes messages as they arrive in group chats.
* Span highlighting: Replies with the original message but underlines the specific manipulation spans for clear visibility.
* Non-intrusive moderation: Provides feedback without deleting or altering user messages.
* Easy integration: Simple to add to any Telegram group.


## Project structure

```
├── app                <- Implementation of telegram bot.
│   ├── project        <- SBT sources.
│   ├── src            <- Bot source code.
│   ├── build.sbt      <- SBT build file.
│   └── Dockerfile     <- Bot docker file.
├── ml                 <- Definition of ml model and bentoml service.
│   ├── curl           <- Request examples.
│   ├── src            <- bentoml service definition.
│   ├── Dockerfile     <- ML model docker file.
│   └── Makefile       <- Build definition.
├── docker-compose.yml <- Docker compose file to bootstrap application
└── README.md          <- The top-level README for developers using this project.
```


## Run locally
```
export TELEGRAM_API_KEY=<your telegram API key>
docker-compose up
```