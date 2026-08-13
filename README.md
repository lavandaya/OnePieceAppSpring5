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

### Fetching all characters - OK

Request:
```
GET http://localhost:8080/api/characters
Accept: application/json
```

Response:
```
HTTP/1.1 200
Content-Type: application/json

[
  {"id":1,"name":"Luffy","age":18,"appearance":"https://placehold.co/400x400/d62828/ffffff?text=Luffy","powertype":"DEVIL_FRUIT","power":10.0,"crewName":"Straw Hat Pirates","swordName":null},
  {"id":2,"name":"Zoro","age":20,"appearance":"https://placehold.co/400x400/2a6f4e/ffffff?text=Zoro","powertype":"WILL","power":9.0,"crewName":"Straw Hat Pirates","swordName":"Wado Ichimonji"},
  {"id":3,"name":"Sanji","age":20,"appearance":"https://placehold.co/400x400/e8a23d/000000?text=Sanji","powertype":"NO_POWER","power":8.0,"crewName":"Straw Hat Pirates","swordName":null},
  {"id":4,"name":"Ussop","age":19,"appearance":"https://placehold.co/400x400/8a5a44/ffffff?text=Ussop","powertype":"NO_POWER","power":1.0,"crewName":"Straw Hat Pirates","swordName":null},
  {"id":5,"name":"Nami","age":19,"appearance":"https://placehold.co/400x400/e07a9b/000000?text=Nami","powertype":"NO_POWER","power":1.0,"crewName":"Straw Hat Pirates","swordName":null},
  {"id":6,"name":"Trafalgar","age":21,"appearance":"https://placehold.co/400x400/4a4e69/ffffff?text=Trafalgar","powertype":"DEVIL_FRUIT","power":10.0,"crewName":"Heart Pirates","swordName":null}
]
```

### Searching characters by name - OK

Request:
```
GET http://localhost:8080/api/characters?name=Luffy
Accept: application/json
```

Response:
```
HTTP/1.1 200
Content-Type: application/json

[
  {"id":1,"name":"Luffy","age":18,"appearance":"https://placehold.co/400x400/d62828/ffffff?text=Luffy","powertype":"DEVIL_FRUIT","power":10.0,"crewName":"Straw Hat Pirates","swordName":null}
]
```

### Searching characters by minimum power - OK

Request:
```
GET http://localhost:8080/api/characters?minPower=8
Accept: application/json
```

Response:
```
HTTP/1.1 200
Content-Type: application/json

[
  {"id":1,"name":"Luffy","age":18,"appearance":"https://placehold.co/400x400/d62828/ffffff?text=Luffy","powertype":"DEVIL_FRUIT","power":10.0,"crewName":"Straw Hat Pirates","swordName":null},
  {"id":2,"name":"Zoro","age":20,"appearance":"https://placehold.co/400x400/2a6f4e/ffffff?text=Zoro","powertype":"WILL","power":9.0,"crewName":"Straw Hat Pirates","swordName":"Wado Ichimonji"},
  {"id":3,"name":"Sanji","age":20,"appearance":"https://placehold.co/400x400/e8a23d/000000?text=Sanji","powertype":"NO_POWER","power":8.0,"crewName":"Straw Hat Pirates","swordName":null},
  {"id":6,"name":"Trafalgar","age":21,"appearance":"https://placehold.co/400x400/4a4e69/ffffff?text=Trafalgar","powertype":"DEVIL_FRUIT","power":10.0,"crewName":"Heart Pirates","swordName":null}
]
```

### Fetching one character - OK

Request:
```
GET http://localhost:8080/api/characters/1
Accept: application/json
```

Response:
```
HTTP/1.1 200
Content-Type: application/json

{"id":1,"name":"Luffy","age":18,"appearance":"https://placehold.co/400x400/d62828/ffffff?text=Luffy","powertype":"DEVIL_FRUIT","power":10.0,"crewName":"Straw Hat Pirates","swordName":null}
```

### Fetching one character - Not Found

Request:
```
GET http://localhost:8080/api/characters/999
Accept: application/json
```

Response:
```
HTTP/1.1 404
Content-Type: application/json

{"error": "Character with id=999 was not found"}
```

### Fetching one character - Not Acceptable

Request:
```
GET http://localhost:8080/api/characters/1
Accept: application/xml
```

Response:
```
HTTP/1.1 406
```

### Fetching battles for a character - OK

Request:
```
GET http://localhost:8080/api/characters/1/battles
Accept: application/json
```

Response:
```
HTTP/1.1 200
Content-Type: application/json

[
  {"id":1,"name":"Arlong Park showdown","location":"Arlong Park","date":"2005-07-23T12:20:00","winner":"Luffy"},
  {"id":4,"name":"Crocodile","location":"Alabasta","date":"2006-03-15T16:45:00","winner":"Luffy"},
  {"id":5,"name":"Enel","location":"Skypiea","date":"2007-08-09T10:30:00","winner":"Luffy"}
]
```

### Fetching battles for a character - Not Found

Request:
```
GET http://localhost:8080/api/characters/999/battles
Accept: application/json
```

Response:
```
HTTP/1.1 404
Content-Type: application/json

{"error": "Character with id=999 was not found"}
```

### Deleting a character - No Content

Request:
```
DELETE http://localhost:8080/api/characters/6
```

Response:
```
HTTP/1.1 204
```

### Deleting a character - Not Found

Request:
```
DELETE http://localhost:8080/api/characters/6
```

Response:
```
HTTP/1.1 404
Content-Type: application/json

{"error": "Character with id=6 was not found"}
```

## Week 3

### Creating a character - Created

Request:
```
POST http://localhost:8080/api/characters
Content-Type: application/json
Accept: application/json

{
  "name": "Nico Robin",
  "age": 28,
  "appearance": "https://placehold.co/400x400/6b4c9a/ffffff?text=Robin",
  "powertype": "DEVIL_FRUIT",
  "power": 8.5,
  "crewName": "Straw Hat Pirates"
}
```

Response:
```
HTTP/1.1 201
Location: http://localhost:8080/api/characters/8
Content-Type: application/json

{
  "id": 8,
  "name": "Nico Robin",
  "age": 28,
  "appearance": "https://placehold.co/400x400/6b4c9a/ffffff?text=Robin",
  "powertype": "DEVIL_FRUIT",
  "power": 8.5,
  "crewName": "Straw Hat Pirates",
  "swordName": null
}
```

### Creating a swordsman - Created

Request:
```
POST http://localhost:8080/api/characters
Content-Type: application/json
Accept: application/json

{
  "name": "Brook",
  "age": 90,
  "appearance": "https://placehold.co/400x400/3a3a3a/ffffff?text=Brook",
  "powertype": "WILL",
  "power": 7.5,
  "crewName": "Straw Hat Pirates",
  "swordName": "Soul Solid"
}
```

Response:
```
HTTP/1.1 201
Location: http://localhost:8080/api/characters/9
Content-Type: application/json

{
  "id": 9,
  "name": "Brook",
  "age": 90,
  "appearance": "https://placehold.co/400x400/3a3a3a/ffffff?text=Brook",
  "powertype": "WILL",
  "power": 7.5,
  "crewName": "Straw Hat Pirates",
  "swordName": "Soul Solid"
}
```

### Creating a character - Bad Request (constraint violations)

Request:
```
POST http://localhost:8080/api/characters
Content-Type: application/json
Accept: application/json

{
  "name": "Kaido",
  "age": -5,
  "appearance": "not-a-url",
  "powertype": "DEVIL_FRUIT",
  "power": 500
}
```

Response:
```
HTTP/1.1 400
Content-Type: application/json

{
  "appearance": "Appearance must be a valid http(s) URL",
  "power": "Power may not exceed 100 DON",
  "age": "Age cannot be negative"
}
```

### Creating a character - Bad Request (unknown crew)

Request:
```
POST http://localhost:8080/api/characters
Content-Type: application/json
Accept: application/json

{
  "name": "Smoker",
  "age": 36,
  "appearance": "https://placehold.co/400x400/9aa0a6/000000?text=Smoker",
  "powertype": "DEVIL_FRUIT",
  "power": 6.0,
  "crewName": "Marines"
}
```

Response:
```
HTTP/1.1 400
Content-Type: application/json

{
  "message": "Crew 'Marines' was not found"
}
```

### Creating a character - Bad Request (invalid enum value)

Request:
```
POST http://localhost:8080/api/characters
Content-Type: application/json
Accept: application/json

{
  "name": "Shanks",
  "age": 39,
  "appearance": "https://placehold.co/400x400/b02c2c/ffffff?text=Shanks",
  "powertype": "MAGIC",
  "power": 9.5
}
```

Response:
```
HTTP/1.1 400
Content-Type: application/json

{
  "message": "Request body is malformed or contains an invalid value"
}
```

### Creating a character - Unsupported Media Type

Request:
```
POST http://localhost:8080/api/characters
Content-Type: text/plain
Accept: application/json

name=Shanks
```

Response:
```
HTTP/1.1 415
Accept: application/json
Content-Type: application/json

{
  "timestamp": "2026-08-13T13:09:30.303+00:00",
  "status": 415,
  "error": "Unsupported Media Type",
  "message": "Content-Type 'text/plain' is not supported.",
  "path": "/api/characters"
}
```

### Updating a character - OK

Request:
```
PATCH http://localhost:8080/api/characters/2
Content-Type: application/json
Accept: application/json

{
  "power": 9.5,
  "swordName": "Enma"
}
```

Response:
```
HTTP/1.1 200
Content-Type: application/json

{
  "id": 2,
  "name": "Zoro",
  "age": 20,
  "appearance": "https://placehold.co/400x400/2a6f4e/ffffff?text=Zoro",
  "powertype": "WILL",
  "power": 9.5,
  "crewName": "Straw Hat Pirates",
  "swordName": "Enma"
}
```

### Updating a character - Bad Request

Request:
```
PATCH http://localhost:8080/api/characters/2
Content-Type: application/json
Accept: application/json

{
  "power": 500
}
```

Response:
```
HTTP/1.1 400
Content-Type: application/json

{
  "power": "Power may not exceed 100 DON"
}
```

### Updating a character - Not Found

Request:
```
PATCH http://localhost:8080/api/characters/999
Content-Type: application/json
Accept: application/json

{
  "power": 5.0
}
```

Response:
```
HTTP/1.1 404
Content-Type: application/json

{
  "error": "Character with id=999 was not found"
}
```

### Updating a character - Conflict

Request:
```
PATCH http://localhost:8080/api/characters/1
Content-Type: application/json
Accept: application/json

{
  "swordName": "Yoru"
}
```

Response:
```
HTTP/1.1 409
Content-Type: application/json

{
  "message": "Character with id=1 is not a swordsman and has no sword"
}
```

### Updating a character - Unsupported Media Type

Request:
```
PATCH http://localhost:8080/api/characters/2
Content-Type: text/plain
Accept: application/json

power=9.5
```

Response:
```
HTTP/1.1 415
Accept: application/json
Accept-Patch: application/json
Content-Type: application/json

{
  "timestamp": "2026-08-13T13:11:14.102+00:00",
  "status": 415,
  "error": "Unsupported Media Type",
  "message": "Content-Type 'text/plain' is not supported.",
  "path": "/api/characters/2"
}
```

### Updating a character - Not Acceptable

Request:
```
PATCH http://localhost:8080/api/characters/2
Content-Type: application/json
Accept: application/xml

{
  "power": 9.5
}
```

Response:
```
HTTP/1.1 406
Accept: application/json
Content-Length: 0
```


## Week 4

### Seeded users

These accounts are created by `src/main/resources/data.sql` when the application starts.
Passwords are stored as BCrypt hashes; the plain values below are for testing only.

| Username | Password      | Email                |
|----------|---------------|----------------------|
| `luffy`  | `password123` | luffy@strawhat.com   |
| `zoro`   | `password123` | zoro@strawhat.com    |
| `admin`  | `admin123`    | admin@onepiece.com   |

The same list is shown on the login page.

New accounts can also be created through the registration form at
[http://localhost:8080/register](http://localhost:8080/register).

### Public page

[http://localhost:8080/characters](http://localhost:8080/characters) — the character
overview is reachable by anyone, signed in or not.

### Authenticated page

[http://localhost:8080/characters/add](http://localhost:8080/characters/add) — adding a
character requires an authenticated user. Anonymous visitors are redirected to the login page.

All state-changing MVC actions are protected the same way: adding a character, adding a
battle, updating a sword name, and deleting a character or a battle.

### Information that differs for anonymous and authenticated users

The exact power value (in DON) of a character is only shown to signed-in users. Anonymous
visitors see a `???` placeholder instead, while the rest of the page stays fully accessible
to them — they are not redirected to the login page.

This applies consistently to every view that renders a power value:

| Page                                                                                  | Where                    |
|---------------------------------------------------------------------------------------|--------------------------|
| [/characters](http://localhost:8080/characters)                                         | character cards          |
| [/characters/1](http://localhost:8080/characters/1)                                     | detail page              |
| [/characters/search](http://localhost:8080/characters/search)                           | result tables            |
| [/battles/1](http://localhost:8080/battles/1)                                           | participants table       |

Hiding the value in the UI is a presentation concern, not a security boundary: the REST API
under `/api/characters` is still public in this step and returns the power value. Securing
the API is part of week 5.

### Security setup

- Passwords are hashed with `BCryptPasswordEncoder` (strength 10). Each user has its own
  salt, so `luffy` and `zoro` have different hashes despite sharing the same password.
- `CustomUserDetailsService` loads the persisted `User` entity and exposes it to Spring
  Security. Every user currently gets the `ROLE_USER` authority; multiple roles follow in week 5.
- CSRF protection is temporarily disabled so that the AJAX features from weeks 2 and 3 keep
  working unchanged. It is re-enabled in week 5.