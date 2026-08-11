# One Piece App

## Course
Programming 5

## Student
- Vladyslav Ivanov

## Academic year & group
- Academic year: 2025-2026
- Group: <ACS202>

## Domain entities
- **Character** — a One Piece character (name, age, appearance, powertype, power); subclass **Swordsman** adds a sword name.
- **Crew** — a pirate crew (name, ship name, bounty flag); one crew has many characters.
- **Battle** — a battle (name, location, date, winner).
- **CharacterBattle** — join entity linking characters to the battles they took part in.

## Build & run (CLI)

Requirements: JDK 21, Docker.

1. Start the database:
   ```bash
   docker compose up -d
   ```
2. Build and run the application:
   ```bash
   ./gradlew bootRun
   ```
3. Open [http://localhost:8080/characters](http://localhost:8080/characters) in your browser.

To stop the database:
```bash
docker compose down
```