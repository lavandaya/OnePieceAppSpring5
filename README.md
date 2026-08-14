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

The embedded frontend's built assets (`src/main/resources/static/dist/`) are committed, so the
steps above are all that's needed to run the app — Node.js is **not** required just to start it.
Node is only needed if you're changing frontend source under `frontend/` (see Week 11 below).


## Week 2

### Fetching all characters - OK

> **Note on authentication.** These examples were written in week 2 and 3, before Spring
> Security was added. From week 4 onward, `GET` requests stay public, but every request that
> modifies data requires an authenticated session **and** a CSRF token. The examples below have
> been updated accordingly: they show the `Cookie` and `X-XSRF-TOKEN` headers that a signed-in
> client sends. Token values are shortened for readability. The runnable versions of all of
> these, including the sign-in requests that obtain the cookie and the token, are in
> [`character-api.http`](character-api.http).


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

## Week 5

### Roles

Two roles exist: `USER` and `ADMIN`. Every seeded and self-registered account has exactly
one role, stored on the persisted `User` entity (`Role` enum) and exposed to Spring Security
as `ROLE_USER` / `ROLE_ADMIN` by `CustomUserDetailsService`.

| Role          | Who has it                              | Can do                                                                                   |
|---------------|------------------------------------------|-------------------------------------------------------------------------------------------|
| Anonymous     | anyone not signed in                     | Browse characters/battles/crews; power value hidden; no create/edit/delete actions visible |
| `USER`        | `luffy`, `zoro`, self-registered accounts | Everything anonymous can, plus: see power values, add characters/battles, edit/delete **only characters they created** |
| `ADMIN`       | `admin`                                  | Everything `USER` can, plus: edit/delete **any** character regardless of owner            |

### Seeded users

These accounts are created by `src/main/resources/data.sql` when the application starts.
Passwords are stored as BCrypt hashes; the plain values below are for testing only.

| Username | Password      | Email                | Role    | Owns                  |
|----------|---------------|-----------------------|---------|------------------------|
| `luffy`  | `password123` | luffy@strawhat.com    | USER    | Luffy, Sanji           |
| `zoro`   | `password123` | zoro@strawhat.com     | USER    | Zoro, Ussop            |
| `admin`  | `admin123`    | admin@onepiece.com    | ADMIN   | Nami, Trafalgar         |

### Character ownership

`Character` now has an `owner` (`User`, `@ManyToOne(fetch = LAZY)`). The creator becomes the
owner automatically, both through the MVC form
([http://localhost:8080/characters](http://localhost:8080/characters), "Quick add" panel) and
through `POST /api/characters`.

Only the owner or an `ADMIN` can update (power, sword name) or delete a character. This is
enforced with `@PreAuthorize` on `CharacterServiceImpl` (`deleteCharacter`, `updateSwordName`,
`updateCharacter`), backed by a custom permission bean, `CharacterSecurity`. Because both the
MVC controller and the REST controller call the same service methods, the check applies
consistently regardless of entry point — there is no separate authorization logic duplicated
per controller.

Example: sign in as `luffy` and open
[http://localhost:8080/characters/1](http://localhost:8080/characters/1) (owned by `luffy`) —
the "Quick edit" panel is visible. Open
[http://localhost:8080/characters/2](http://localhost:8080/characters/2) (owned by `zoro`) —
the panel is gone, even though the character itself is still fully visible.

### Hidden for unauthenticated / non-owner users

- The exact power value (DON) — unchanged from week 4, still hidden for anonymous visitors on
  every view that renders it.
- The "Quick add" form on
  [http://localhost:8080/characters](http://localhost:8080/characters) — only rendered for
  signed-in users.
- The delete button on each character card — only rendered for that character's owner or an
  `ADMIN`.
- The "Quick edit" panel on a character's detail page — only rendered for that character's
  owner or an `ADMIN`.

Hiding these is a UI convenience on top of the actual authorization: the underlying
`/api/characters` endpoints enforce the same rule server-side and reject unauthorized attempts
with `403 Forbidden`, independent of what the browser shows.

### REST API authorization

- `GET /api/characters/**` stays public (read-only).
- `POST /api/characters` requires authentication (the caller becomes the owner of the created
  character).
- `PATCH /api/characters/{id}` and `DELETE /api/characters/{id}` require authentication at the
  route level and ownership (or `ADMIN`) at the service level.
- Unauthorized calls receive `403 Forbidden` with a JSON body (`ApiErrorDto`), handled by
  `ApiExceptionHandler`; the equivalent MVC actions render `error/general` with `403` via
  `GlobalExceptionHandler`.

### CSRF

CSRF protection is re-enabled (it was temporarily disabled in week 4):

- `CookieCsrfTokenRepository` issues a `XSRF-TOKEN` cookie readable by JavaScript.
- Thymeleaf forms (login, register, add battle, sword update, delete forms) automatically
  include the hidden `_csrf` field — no template changes were needed there.
- The AJAX scripts (`characterAdd.js`, `characterPatch.js`, `characterDelete.js`) read the
  cookie and send it back in the `X-XSRF-TOKEN` header on every state-changing `fetch` call.

## Week 6

### Spring profiles

| Profile | Datasource                              | Seeding (`data.sql`)      |
|---------|------------------------------------------|----------------------------|
| `dev`   | `onepiece_db` (port 5433)                | `spring.sql.init.mode=always` |
| `test`  | `onepiece_test_db` (port 5434)           | `spring.sql.init.mode=never`  |

`dev` is the default (`spring.profiles.active=dev` in `application.properties`). `test` is
activated on the test classes via `@ActiveProfiles("test")`, never on the command line, so
`./gradlew test` and `./gradlew bootRun` always talk to different databases and never
interfere with each other's data.

### Test classes

- `CharacterRepositoryTest` — repository layer (`@DataJpaTest`)
- `CharacterServiceIntegrationTest`, `UserServiceIntegrationTest` — service layer
  (`@SpringBootTest`, integration tests)
- `@WithMockUser`-based tests live in `CharacterServiceIntegrationTest`, since
  `updateCharacter` / `deleteCharacter` / `updateSwordName` are annotated with `@PreAuthorize`

## Week 8

### Running the tests

```bash
./gradlew test
```

No extra profile flag is needed on the command line: every test class activates the `test`
profile itself via `@ActiveProfiles("test")`, so `./gradlew test` always talks to
`onepiece_test_db` (port 5434) regardless of what `spring.profiles.active` is set to for
`bootRun`. Both Postgres containers (`onepiece_db` and `onepiece_test_db`) must be running
(`docker compose up -d`) before running the tests.

### Test classes

- **MVC integration tests**: `CharacterControllerIntegrationTest`
  (`src/test/java/.../presentation/controller/CharacterControllerIntegrationTest.java`) — covers
  the character overview, search, and detail pages, including the anonymous-vs-authenticated
  behavior (hidden power value, `/characters/add` requiring a login redirect).
- **API integration tests**: `CharacterRestControllerIntegrationTest`
  (`src/test/java/.../presentation/controller/api/CharacterRestControllerIntegrationTest.java`)
  — covers `GET /api/characters` (with search filters), `GET /api/characters/{id}`,
  `GET /api/characters/{id}/battles`, including 404/406 cases.
- **Role verification tests**: `CharacterServiceIntegrationTest`
  (`src/test/java/.../business/service/CharacterServiceIntegrationTest.java`) — the
  authorization requirement "only the owner or an admin may update/delete a character" is
  enforced with `@PreAuthorize` on the **service** layer (`CharacterServiceImpl.deleteCharacter`,
  `updateSwordName`, `updateCharacter`), so per the assignment it's tested there rather than on
  the controller. For each of the three methods, tests cover: the owner succeeding, an `ADMIN`
  succeeding, a different (non-owner) `USER` getting `AccessDeniedException`, and (for
  `updateCharacter`) an anonymous caller getting `AccessDeniedException`.

### Code coverage

_Screenshot of the IntelliJ coverage tool window goes here (package names + tested classes
visible)._

### Two bugs found and fixed while writing these tests

- `CharacterBattleRepository.deleteByCharacterId` / `deleteByBattleId` and
  `CharacterRepository.updateSwordName` are `@Modifying` JPQL bulk statements. Without
  `clearAutomatically = true`, the persistence context kept serving stale, already-deleted or
  already-updated entities from its first-level cache after the bulk statement ran, which broke
  a repository test (`TransientObjectException` on deleting a character with battles) and a new
  service test (`updateSwordName` reading back the old sword name).
- `SecurityConfig`'s `defaultAuthenticationEntryPointFor` only registered a JSON 401 entry point
  scoped to `/api/**`. Because it was the only entry registered at that point, Spring Security
  used it as the fallback for *every* unmatched request too (not just the ones the matcher
  actually matched), so unauthenticated visits to protected MVC pages like `/characters/add`
  sometimes got a raw JSON 401 instead of a redirect to `/login`, depending on the `Accept`
  header. Fixed by explicitly registering a second, catch-all entry (`AnyRequestMatcher` →
  `LoginUrlAuthenticationEntryPoint("/login")`) so the two entries are mutually exhaustive.

## Week 9

### Running the tests

Same single command as week 8, no separate step needed:

```bash
./gradlew test
```

### Mocking tests

- `CharacterRestControllerMockTest`
  (`src/test/java/.../presentation/controller/api/CharacterRestControllerMockTest.java`) —
  `@WebMvcTest(CharacterRestController.class)` slice test for `POST /api/characters`, with
  `CharacterService`, `BattleService`, `CharacterMapper` and `BattleMapper` all replaced by
  `@MockitoBean`. Covers: authenticated success (201, `Location` header, owner becomes the
  authenticated user), an unauthenticated call being rejected before it ever reaches the
  service, a validation failure (400, service never called), a business exception from the
  mocked service (unknown crew → 400), and a missing CSRF token (403).
- `CharacterServiceImplTest`
  (`src/test/java/.../business/service/impl/CharacterServiceImplTest.java`) — plain Mockito
  unit tests (`@ExtendWith(MockitoExtension.class)`, `@Mock`/`@InjectMocks`, no Spring context)
  for `addCharacter` and `createCharacter`, with `CharacterRepository`, `CrewRepository` and
  `UserRepository` all mocked. Covers known/unknown crew, present/absent owner, and the
  unknown-crew failure path.

### `verify` tests

Both classes above use `verify(...)`, several with specific arguments and `ArgumentCaptor`:

- `CharacterRestControllerMockTest` — e.g. `verify(characterService).createCharacter(entity, "Straw Hat Pirates", "luffy")` and `verify(characterService, never()).createCharacter(any(), any(), any())` on the rejected/invalid paths.
- `CharacterServiceImplTest` — e.g. `addCharacter_withKnownCrewAndOwner_setsBothAndSaves` captures the `Character` passed to `characterRepository.save(...)` and asserts its `crew`/`owner`; `addCharacter_withNullOwnerUsername_...` asserts `verify(userRepository, never()).findByUsername(any())`.

### CI pipeline

A GitHub Actions workflow (`.github/workflows/ci.yml`) runs on every push/PR, with two
sequential jobs as required:

- **build** — compiles the project (`./gradlew build -x test`), with Gradle dependency/build
  caching via `actions/setup-java`'s built-in `cache: gradle`.
- **test** — depends on `build` (reusing its cache), spins up a `postgres:16-alpine` service
  container, points `spring.datasource.*` at it via environment variables (overriding
  `application-test.properties`'s local port without touching the file, so `./gradlew test`
  still works unchanged against the Docker Compose databases locally), runs `./gradlew test`,
  and publishes a JUnit test report plus the raw HTML report as workflow artifacts.

_The course explicitly asks for this pipeline (and its JUnit report link) on **GitLab**,
while this repository's working copy for this session lives on GitHub. A GitLab-flavored
version of the same pipeline (`.gitlab-ci.yml`, with a `postgres` service and a `reports.junit`
block) is ready to add once the project is pushed to GitLab — link to a successful pipeline
goes here once that's done._

## Week 10

The client-side work for this week (npm, webpack, Sass, the SPA, the fetch calls) lives in a
separate repository, **`OnePieceAppClient`**, per the assignment. This backend only gained
the two new REST endpoints and the CORS/security wiring it calls.

### New backend endpoints

- `GET /api/battles?name=...` — search battles by (partial, case-insensitive) name. Public,
  same as the rest of `GET /api/**`.
- `POST /api/battles` — create a battle. Unlike every other state-changing endpoint in this
  API, this one is `permitAll` **and** CSRF-exempt (see the comments on both rules in
  `SecurityConfig`) — solely so the standalone Client project, which has no session/cookie of
  its own, can exercise it while testing its "Add" form. `character-api.http` has runnable
  examples (including the 400 case).

### CORS

`SecurityConfig` registers a `CorsConfigurationSource` scoped to `/api/**`, allowing only the
Client project's dev-server origin (`client.cors.allowed-origin`, defaults to
`http://localhost:8081`) with `GET/POST/PATCH/DELETE`. Requests from any other origin are
rejected at the preflight (`OPTIONS`) stage.

### Running both projects together

```bash
docker compose up -d
./gradlew bootRun          # backend on :8080
```

```bash
cd ../OnePieceAppClient
npm install
npm start                  # client dev server on :8081, opens automatically
```

Verified manually end-to-end: searching from the client (with and without matches) and adding
a battle from the client's "Add" form, including the CORS preflight, both round-trip correctly
against this backend.

## Week 11

The MVC pages' JavaScript and CSS were migrated from plain `static/js/*.js` + `static/css/style.css`
into an embedded npm/webpack project at [`frontend/`](frontend), replacing per-page `<script>` tags
with a single bundle (`/dist/main.js`) loaded once from `fragments/layout.html`. Each page's
behavior lives in its own ES module under `frontend/src/js/modules/`, self-guarding on the presence
of its page's DOM elements — the same pattern the old scripts already used — so one bundle safely
covers every MVC page.

### Build instructions (frontend)

```bash
cd frontend
npm install
npm run build        # writes to ../src/main/resources/static/dist/
npm run lint          # ESLint
npm run format         # dprint, writes formatting fixes
npm run format:check   # dprint, fails if anything is unformatted
```

The build output is committed, so re-running `npm run build` (and committing the result) is only
needed after changing something under `frontend/src`.

### Sass

`frontend/src/scss/main.scss` imports Bootstrap through Sass with customized `$primary`/
`$secondary` variables, plus project SCSS using nesting, a `@mixin`, and a `@for` loop (migrated
from the old `style.css`).

### Bootstrap Icon added via npm

The delete-confirmation dialog (see below) uses `bootstrap-icons/icons/trash3-fill.svg`, imported
directly in JS (via `import trashIconSvg from "bootstrap-icons/icons/trash3-fill.svg"`, webpack
`asset/source`) — sourced from the npm package, not the `webjars` copy the rest of the app's
`<i class="bi ...">` icons still use.
Source: [`frontend/src/js/modules/characterDelete.js`](frontend/src/js/modules/characterDelete.js).
Visible at [http://localhost:8080/characters](http://localhost:8080/characters) (signed in) —
click the trash icon on any character you own.

### Custom client-side form validation

The "Quick add" character form on
[http://localhost:8080/characters](http://localhost:8080/characters) (signed in) is validated with
the [`validator`](https://www.npmjs.com/package/validator) npm package before the AJAX call is
made — name length, age range, `appearance` restricted to `http(s)` URLs (stricter than the native
`type="url"` check), and power range.
Source: [`frontend/src/js/modules/addCharacterValidation.js`](frontend/src/js/modules/addCharacterValidation.js),
wired into [`frontend/src/js/modules/characterAdd.js`](frontend/src/js/modules/characterAdd.js).

### JavaScript dependencies added

| Package | Where it's used | What it does for the user |
|---|---|---|
| [`sweetalert2`](https://www.npmjs.com/package/sweetalert2) | [`frontend/src/js/modules/characterDelete.js`](frontend/src/js/modules/characterDelete.js) | Replaces the native `confirm()` on [/characters](http://localhost:8080/characters) with a styled confirm dialog (with the npm-sourced trash icon above) before deleting a character. |
| [`dayjs`](https://www.npmjs.com/package/dayjs) (+ `relativeTime` plugin) | [`frontend/src/js/modules/characterBattles.js`](frontend/src/js/modules/characterBattles.js) | Formats each battle's date on a character's detail page (e.g. `/characters/1`) as `2005-07-23 12:20 (21 years ago)` when the "Reload" button is clicked. |
| [`validator`](https://www.npmjs.com/package/validator) | see above | Custom client-side form validation. |
| [`bootstrap-icons`](https://www.npmjs.com/package/bootstrap-icons) | see above | Source of the npm-added icon. |

### Verified manually

Logged in as `luffy` on [http://localhost:8080/characters](http://localhost:8080/characters):
the invalid-URL case in the Quick-add form is rejected client-side (no network call) with the
`validator`-driven message; a valid submission creates the character via AJAX; deleting it shows
the SweetAlert2 confirmation with the npm icon and removes the card on confirm. On a character's
detail page, "Reload" shows dayjs-formatted relative battle dates, and "Quick edit" saves the
power value via AJAX. The customized Sass variable (`$ocean-blue`) was confirmed applied via a
computed-style check on `.bg-ocean`.

## Week 12

### Asynchronous CSV upload

[http://localhost:8080/admin/upload](http://localhost:8080/admin/upload) (admin-only — link
in the navbar only appears for `ADMIN`, route protected by `SecurityConfig`) lets an admin
upload a CSV of characters to create in bulk.

- `AdminController` (`presentation/controller/AdminController.java`) reads the uploaded file's
  lines **synchronously** (just I/O, effectively instant) and immediately dispatches them to
  `CsvImportService.importCharacters(...)`, then redirects with a flash message — it never waits
  for a single character to actually be parsed or saved.
- `CsvImportServiceImpl.importCharacters` (`business/service/impl/CsvImportServiceImpl.java`) is
  `@Async("csvImportExecutor")` (executor bean in `config/AsyncConfig.java`, `@EnableAsync`), so
  it runs on a pooled background thread, not the HTTP request thread. It parses each row, skips
  and logs invalid ones instead of aborting the whole import, and reuses
  `CharacterService.createCharacter` (the same path the REST API's `POST /api/characters` uses)
  so crew lookup, optional `Swordsman` creation, and the uploading admin becoming the owner all
  work exactly like they do everywhere else.
- Sample file: [`src/main/resources/sample-characters.csv`](src/main/resources/sample-characters.csv)
  (columns: `name,age,appearance,powertype,power,crewName,swordName`, the last two optional).

**Verified manually**: uploaded the sample CSV via `curl` with a temporary `Thread.sleep(1500)`
per row (removed again afterward, per the assignment's own tip) — the HTTP response came back in
33ms while the 4-row import needed 6+ seconds in the background, and all 4 characters (including
one `Swordsman` and one with no crew) were correctly present afterward.

Tests: `CsvImportServiceImplTest` (plain Mockito, row parsing/crew/Swordsman logic and invalid-row
handling) and `AdminControllerIntegrationTest` (route security — anonymous/`USER`/`ADMIN` — and
that the controller dispatches to and never waits on `CsvImportService`, using a `@MockitoBean`
to keep the test deterministic instead of asserting on background-thread timing).

### Caching

`CharacterServiceImpl.findByNameContaining` (backing both `GET /api/characters?name=` and the MVC
search page) is `@Cacheable(cacheNames = "characterSearch", key = "#name")`
(`config/CacheConfig.java`, `@EnableCaching` + a `ConcurrentMapCacheManager`) — a repeated search
for the same name is served from memory instead of hitting the database again.

`CharacterRepository.findByNameContainingIgnoreCase` was changed from a derived query to a
`LEFT JOIN FETCH crew, owner` query. This matters specifically **because** of the caching: without
it, the cached `Character` entities would carry lazy `crew`/`owner` proxies tied to a closed
Hibernate session (`spring.jpa.open-in-view=false`), and a `LazyInitializationException` could
surface later when the DTO mapper accesses them outside the original request. Fetching eagerly
once means the cached entities are always fully initialized and safe to reuse.

Every method that can change which characters match a search — `addCharacter`, `createCharacter`,
`updateCharacter`, `updateSwordName`, `deleteCharacter` — is `@CacheEvict(cacheNames =
"characterSearch", allEntries = true)`, so the cache never serves stale results after a mutation.

Tests: `CharacterSearchCachingTest` (`business/service/CharacterSearchCachingTest.java`) proves
both directions without relying on SQL-statement counting — a search result stays stale (proving
the cache was hit, not the database) when a new matching row is inserted directly through the
repository, and correctly changes after a create/delete goes through the service (proving
eviction happened).
