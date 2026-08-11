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


## Week 2

### Searching characters by name - OK

- GET http://localhost:8080/api/characters?name=Luffy
Accept: application/json

HTTP/1.1 200
Content-Type: application/json

[
{
"id": 1,
"name": "Luffy",
"age": 18,
"appearance": "https://placehold.co/400x400/d62828/ffffff?text=Luffy",
"powertype": "DEVIL_FRUIT",
"power": 10.0,
"crewName": "Straw Hat Pirates",
"swordName": null
}
]

- GET http://localhost:8080/api/characters/999
Accept: application/json

HTTP/1.1 404
Content-Type: application/json

{
"error": "Character with id=999 was not found"
}

- GET http://localhost:8080/api/characters/1
Accept: application/xml


HTTP/1.1 406

- GET http://localhost:8080/api/characters/1/battles
Accept: application/json

HTTP/1.1 200
Content-Type: application/json

[
{
"id": 1,
"name": "Arlong Park showdown",
"location": "Arlong Park",
"date": "2005-07-23T12:20:00",
"winner": "Luffy"
},
{
"id": 4,
"name": "Crocodile",
"location": "Alabasta",
"date": "2006-03-15T16:45:00",
"winner": "Luffy"
},
{
"id": 5,
"name": "Enel",
"location": "Skypiea",
"date": "2007-08-09T10:30:00",
"winner": "Luffy"
}
]

- GET http://localhost:8080/api/characters/999/battles
Accept: application/json

HTTP/1.1 404
Content-Type: application/json

{
"error": "Character with id=999 was not found"
}

- DELETE http://localhost:8080/api/characters/6

HTTP/1.1 204

- DELETE http://localhost:8080/api/characters/6

HTTP/1.1 404
Content-Type: application/json

{
"error": "Character with id=6 was not found"
}