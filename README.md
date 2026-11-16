# Каталог белков (Protein Catalog) — Compose Multiplatform

Кроссплатформенное приложение для просмотра и поиска информации о белках человека и модельных организмов на базе базы данных [UniProtKB](https://www.uniprot.org/).

Проект выполнен в рамках лабораторного курса по кроссплатформенной разработке на **Kotlin Multiplatform (Compose Multiplatform)**.

## Предметная область
Приложение представляет собой справочник белков:
- **Идентификация:** первичный доступ (`Accession`), мнемоническое имя записи (`Entry name`), ген (`Gene`).
- **Биологические свойства:** научное и общепринятое название организма-хозяина (`Organism`), рекомендованное полное название белка.
- **Структура:** первичная аминокислотная последовательность (`Sequence`).
- **Взаимодействия:** список перекрестно связанных белков (`Related proteins`) с возможностью быстрого перехода к связанному белку.

В текущей вехе (В1 — Каркас) данные формируются через моковый репозиторий (`MockProteinRepositoryImpl`), насчитывающий 20 детально описанных записей, строго повторяющих структуру будущего сетевого API UniProtKB.

## Архитектура
- **Domain Layer:** чистая модель `Protein` и контракт `ProteinRepository`.
- **Data Layer:** моковый источник данных `MockProteins` и реализация `MockProteinRepositoryImpl`.
- **Presentation Layer:** экраны на Compose Multiplatform, паттерн MVI/MVVM (`ProteinListViewModel`, `ProteinDetailViewModel`), однонаправленный поток данных через `StateFlow`.
- **Navigation:** Compose Navigation 3 (`NavDisplay`) со строго типизированными роутами (`Route.ProteinList`, `Route.ProteinDetail`) и настроенными анимациями прямого перехода и возврата.
- **Design & Theme:** дизайн-система Material 3, токены темы (светлая/тёмная), поддержка локализации (RU / EN) в памяти сессии.

## Команды запуска

### Desktop (JVM) — ведущий таргет вехи
```bash
./gradlew :desktopApp:run
```

### Web (Wasm / Compose)
```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

### Android
```bash
./gradlew :androidApp:installDebug
```

### Проверка сборки библиотеки
```bash
./gradlew :shared:assemble
```
