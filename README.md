# LinkUpdateCheckerBot

Проект представляет собой бота в телеграмме, которому можно отправлять ссылки на различные ресурсы, статьи в интернете, которые могут обновляться в течение времени (например репозиторий GitHub или ответы на StackOverflow), бот будет присылать сообщения, когда какой-либо из отслеживаемых ресурсов обновится.

## Модули проекта

1. Модуль bot - отвечает за работу бота.
2. Модуль link-parser - отвечает за парсинг присылаемых пользователями ссылок.
3. Модуль scrapper - является реализацией REST API сервера.
4. Модуль shareddto - общие dto модулей bot и scraper.
