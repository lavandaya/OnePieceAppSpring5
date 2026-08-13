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
```}