# Lets-Play Audit Checklist & Test Commands

App runs over HTTPS on `https://localhost:8443` (self-signed dev cert — `curl` needs `-k` to skip
certificate verification). Start the app first:

```powershell
.\mvnw.cmd spring-boot:run
```

---

## Functional

### 0. Does the application run and respond to API requests?

```powershell
curl.exe -k -s -o NUL -w "%{http_code}`n" https://localhost:8443/api/products
```

**Expected:** `200`.

---

### 0b. Are all CRUD operations correctly implemented for Users and Products?

```powershell
$adminToken = (curl.exe -k -s -X POST https://localhost:8443/api/auth/login -H "Content-Type: application/json" -d '{\"username\":\"admin\",\"password\":\"change-this-dev-only-admin-password\"}' | ConvertFrom-Json).token

# --- Users CRUD ---
curl.exe -k -s -X POST https://localhost:8443/api/users -H "Content-Type: application/json" -d '{\"username\":\"player1\",\"email\":\"player1@test.com\",\"password\":\"password123\"}'
curl.exe -k -s https://localhost:8443/api/users -H "Authorization: Bearer $adminToken"
curl.exe -k -s https://localhost:8443/api/users/<id> -H "Authorization: Bearer $adminToken"
curl.exe -k -s -X PUT https://localhost:8443/api/users/<id> -H "Content-Type: application/json" -H "Authorization: Bearer $adminToken" -d '{\"username\":\"player1\",\"email\":\"player1@test.com\",\"password\":\"newpassword123\"}'
curl.exe -k -s -X DELETE https://localhost:8443/api/users/<id> -H "Authorization: Bearer $adminToken"

# --- Products CRUD --- (see section 6 below for the full sequence)
```

**Expected:** `201` create, `200` read/update, `204` delete — for both resources.

---

### 0c. Is product ownership enforced correctly?

```powershell
# Create a product as player1
$userToken = (curl.exe -k -s -X POST https://localhost:8443/api/auth/login -H "Content-Type: application/json" -d '{\"username\":\"player1\",\"password\":\"password123\"}' | ConvertFrom-Json).token
$productId = (curl.exe -k -s -X POST https://localhost:8443/api/products -H "Content-Type: application/json" -H "Authorization: Bearer $userToken" -d '{\"name\":\"Owned Game\",\"description\":\"Owned by player1\",\"price\":10.0,\"quantity\":1}' | ConvertFrom-Json).id

# Verify owner lookup returns the product
curl.exe -k -s https://localhost:8443/api/products/owner/<player1-id> -H "Authorization: Bearer $userToken"

# Another user should not be allowed to update/delete it
$otherToken = (curl.exe -k -s -X POST https://localhost:8443/api/auth/login -H "Content-Type: application/json" -d '{\"username\":\"player2\",\"password\":\"password123\"}' | ConvertFrom-Json).token
curl.exe -k -s -X PUT https://localhost:8443/api/products/$productId -H "Content-Type: application/json" -H "Authorization: Bearer $otherToken" -d '{\"name\":\"Hijacked\",\"description\":\"Not allowed\",\"price\":15.0,\"quantity\":1}' -w "`nStatus: %{http_code}`n"
curl.exe -k -s -X DELETE https://localhost:8443/api/products/$productId -H "Authorization: Bearer $otherToken" -w "`nStatus: %{http_code}`n"

# Owner can update/delete its own product
curl.exe -k -s -X PUT https://localhost:8443/api/products/$productId -H "Content-Type: application/json" -H "Authorization: Bearer $userToken" -d '{\"name\":\"Owned Game Updated\",\"description\":\"Still owned\",\"price\":12.0,\"quantity\":1}' -w "`nStatus: %{http_code}`n"
curl.exe -k -s -X DELETE https://localhost:8443/api/products/$productId -H "Authorization: Bearer $userToken" -w "`nStatus: %{http_code}`n"
```

**Expected:** `403` when a non-owner tries update/delete; `200`/`204` when the owner performs the same actions; owner lookup returns the product.

---

### 0d. Does authentication work correctly? Do operations respect user roles?

Covered in depth in section 8 below (`@PreAuthorize`/`@PermitAll` tests): no token → `401`,
non-admin token → `403`, admin token → `200`.

---

### 0d. Does the application handle exceptions and return appropriate HTTP codes (e.g. updating a non-existing user)?

```powershell
# Update a non-existent user
curl.exe -k -s -X PUT https://localhost:8443/api/users/000000000000000000000000 -H "Content-Type: application/json" -H "Authorization: Bearer $adminToken" -d '{\"username\":\"x\",\"email\":\"x@x.com\",\"password\":\"password123\"}' -w "`nStatus: %{http_code}`n"

# Get a non-existent product
curl.exe -k -s https://localhost:8443/api/products/000000000000000000000000 -w "`nStatus: %{http_code}`n"

# Malformed JSON body (must NOT be a 500)
curl.exe -k -s -X POST https://localhost:8443/api/products -H "Content-Type: application/json" -H "Authorization: Bearer $adminToken" -d '{ this is not valid json' -w "`nStatus: %{http_code}`n"

# Duplicate username registration (must NOT silently succeed)
curl.exe -k -s -X POST https://localhost:8443/api/users -H "Content-Type: application/json" -d '{\"username\":\"admin\",\"email\":\"other@test.com\",\"password\":\"password123\"}' -w "`nStatus: %{http_code}`n"
```

**Expected:** `404`, `404`, `400`, `409` — never a `5XX`.

> ⚠️ Requires `spring.data.mongodb.auto-index-creation=true` in
> [application.properties](src/main/resources/application.properties) — without it, the
> `@Indexed(unique = true)` constraints on `User.username`/`email` are never created in MongoDB and
> duplicates silently succeed instead of returning `409`.

---

### 0e. Is the "GET Products" API accessible without authentication?

```powershell
curl.exe -k -s -o NUL -w "%{http_code}`n" https://localhost:8443/api/products
```

**Expected:** `200` with no `Authorization` header at all.

---

## Security

### 1. Are passwords hashed?

Register a user, then inspect the stored document directly in MongoDB — it must **not** be plaintext.

```powershell
# Register a user via the API
curl.exe -k -s -X POST https://localhost:8443/api/users -H "Content-Type: application/json" -d '{\"username\":\"player1\",\"email\":\"player1@test.com\",\"password\":\"password123\"}'

# Inspect the stored document
mongosh "mongodb://localhost:27017/letsplay" --eval "db.users.findOne({username:'player1'})" --quiet
```

**Expected:** the `password` field is a bcrypt hash (starts with `$2a$` / `$2b$`), never `password123`.

---

### 2. Is input validated?

Send invalid payloads — they must be rejected with `400` and field-level error messages.

```powershell
# Invalid user (blank username, bad email, short password)
curl.exe -k -s -X POST https://localhost:8443/api/users -H "Content-Type: application/json" -d '{\"username\":\"\",\"email\":\"not-an-email\",\"password\":\"123\"}'

# Invalid login (blank fields)
curl.exe -k -s -X POST https://localhost:8443/api/auth/login -H "Content-Type: application/json" -d '{\"username\":\"\",\"password\":\"\"}'
```

For the product one, log in as admin first (see section 5) to get a token:

```powershell
$adminToken = (curl.exe -k -s -X POST https://localhost:8443/api/auth/login -H "Content-Type: application/json" -d '{\"username\":\"admin\",\"password\":\"change-this-dev-only-admin-password\"}' | ConvertFrom-Json).token

# Invalid product (blank name/description, negative price/quantity)
curl.exe -k -s -X POST https://localhost:8443/api/products -H "Content-Type: application/json" -H "Authorization: Bearer $adminToken" -d '{\"name\":\"\",\"description\":\"\",\"price\":-5,\"quantity\":-1}'
```

**Expected:** `400 Bad Request` with a JSON body listing which fields failed and why.

---

### 2b. Is input validated to prevent MongoDB (NoSQL) injection attacks?

Try sending MongoDB query operators (`$ne`, `$gt`, etc.) instead of plain strings — since request
bodies are bound to strongly-typed Java records (not raw BSON), these should be rejected outright,
never used as part of a query.

```powershell
# Operator injection on login (attempted auth bypass)
curl.exe -k -s -X POST https://localhost:8443/api/auth/login -H "Content-Type: application/json" -d '{\"username\":{\"$ne\":\"\"},\"password\":{\"$ne\":\"\"}}' -w "`nStatus: %{http_code}`n"

# Operator injection on user registration
curl.exe -k -s -X POST https://localhost:8443/api/users -H "Content-Type: application/json" -d '{\"username\":{\"$gt\":\"\"},\"email\":\"a@a.com\",\"password\":\"password123\"}' -w "`nStatus: %{http_code}`n"
```

**Expected:** `400 Bad Request` (rejected as an unreadable/invalid body) — **never** a successful
login/registration and **never** a `500`.

---

### 3. Is sensitive information protected?

Fetch a user via the API and confirm the password is never returned.

```powershell
curl.exe -k -s https://localhost:8443/api/users -H "Authorization: Bearer $adminToken"
```

**Expected:** response contains `id`, `username`, `email`, `role` — **no** `password` field.

Also confirm unhandled errors don't leak stack traces:

```powershell
curl.exe -k -s https://localhost:8443/api/users/not-a-real-id -H "Authorization: Bearer $adminToken"
```

**Expected:** clean JSON error message (`404` / generic error), not a raw Java stack trace.

---

### 4. Is HTTPS enabled?

```powershell
# HTTPS works
curl.exe -k -s -o NUL -w "HTTPS status: %{http_code}`n" https://localhost:8443/api/products

# Plain HTTP on the same port must fail (server only speaks TLS)
curl.exe -s -o NUL -w "HTTP status: %{http_code}`n" http://localhost:8443/api/products
```

**Expected:** the HTTPS call returns `200`; the plain HTTP call fails/errors out (protocol mismatch),
proving TLS is enforced. Config lives in [application.properties](src/main/resources/application.properties)
(`server.ssl.enabled=true`, `server.port=8443`, PKCS12 keystore).

---

## Code Quality and Standards

### 5. Are `@Document`, `@Id`, `@Field` used correctly?

Inspection, not a runtime test — see [User.java](src/main/java/com/letsplay/model/User.java) and
[Product.java](src/main/java/com/letsplay/model/Product.java): both are annotated `@Document(collection = "...")`
with `@Id` on the identifier field. `@Field` isn't used because no Java property name differs from its
MongoDB field name (it's only needed when they diverge).

You can confirm the collection/field names match at runtime:

```powershell
mongosh "mongodb://localhost:27017/letsplay" --eval "db.users.findOne()" --quiet
mongosh "mongodb://localhost:27017/letsplay" --eval "db.products.findOne()" --quiet
```

---

### 6. Are `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` used correctly?

Exercise every verb against the `products` resource end-to-end:

```powershell
# CREATE (POST)
$product = curl.exe -k -s -X POST https://localhost:8443/api/products -H "Content-Type: application/json" -H "Authorization: Bearer $adminToken" -d '{\"name\":\"Chess Set\",\"description\":\"Wooden chess set\",\"price\":29.99,\"quantity\":10}' | ConvertFrom-Json
$id = $product.id

# READ (GET, list + by id)
curl.exe -k -s https://localhost:8443/api/products
curl.exe -k -s https://localhost:8443/api/products/$id

# UPDATE (PUT)
curl.exe -k -s -X PUT https://localhost:8443/api/products/$id -H "Content-Type: application/json" -H "Authorization: Bearer $adminToken" -d '{\"name\":\"Chess Set Deluxe\",\"description\":\"Wooden chess set\",\"price\":39.99,\"quantity\":8}'

# DELETE
curl.exe -k -s -o NUL -w "%{http_code}`n" -X DELETE https://localhost:8443/api/products/$id -H "Authorization: Bearer $adminToken"
```

**Expected:** `201` on create, `200` on reads/update, `204` on delete.

---

### 7. Is `@Autowired` used correctly for dependency injection?

Inspection only: the project uses **constructor injection without `@Autowired`** (e.g.
[`UserController`](src/main/java/com/letsplay/controller/UserController.java),
[`SecurityConfig`](src/main/java/com/letsplay/config/SecurityConfig.java)). This is the Spring-recommended
approach — `@Autowired` is optional and implicit when a class has a single constructor, so its absence isn't
a defect, but note it if the audit expects the annotation to be visibly present somewhere.

---

### 8. Are `@EnableWebSecurity`, `@EnableMethodSecurity`, `@PermitAll`, `@PostAuthorize`, `@PreAuthorize` used correctly?

[`SecurityConfig`](src/main/java/com/letsplay/config/SecurityConfig.java) has `@EnableWebSecurity` +
`@EnableMethodSecurity(jsr250Enabled = true)`. `@PermitAll` guards public endpoints, `@PreAuthorize("hasRole('ADMIN')")`
guards admin-only ones. Test all three access levels:

```powershell
# 1. Log in as the seeded admin
$adminToken = (curl.exe -k -s -X POST https://localhost:8443/api/auth/login -H "Content-Type: application/json" -d '{\"username\":\"admin\",\"password\":\"change-this-dev-only-admin-password\"}' | ConvertFrom-Json).token

# 2. Register + log in as a normal (non-admin) user
curl.exe -k -s -X POST https://localhost:8443/api/users -H "Content-Type: application/json" -d '{\"username\":\"player1\",\"email\":\"player1@test.com\",\"password\":\"password123\"}'
$userToken = (curl.exe -k -s -X POST https://localhost:8443/api/auth/login -H "Content-Type: application/json" -d '{\"username\":\"player1\",\"password\":\"password123\"}' | ConvertFrom-Json).token

# 3. No token -> 401 Unauthorized
curl.exe -k -s -o NUL -w "no token:    %{http_code}`n" https://localhost:8443/api/users

# 4. Regular user token -> 403 Forbidden
curl.exe -k -s -o NUL -w "user token:  %{http_code}`n" https://localhost:8443/api/users -H "Authorization: Bearer $userToken"

# 5. Admin token -> 200 OK
curl.exe -k -s -o NUL -w "admin token: %{http_code}`n" https://localhost:8443/api/users -H "Authorization: Bearer $adminToken"

# 6. Public endpoint (@PermitAll) -> 200 with no token at all
curl.exe -k -s -o NUL -w "public GET products (no token): %{http_code}`n" https://localhost:8443/api/products
```

**Expected:** `401`, `403`, `200`, `200` respectively.

---

### 9. Are validation annotations (`@NotNull`, `@Size`, `@Email`, etc.) used to validate input data?

Same as section 2 — see [`UserRequest`](src/main/java/com/letsplay/dto/UserRequest.java),
[`ProductRequest`](src/main/java/com/letsplay/dto/ProductRequest.java),
[`LoginRequest`](src/main/java/com/letsplay/dto/LoginRequest.java) for the annotations
(`@NotBlank`, `@Size`, `@Email`, `@NotNull`, `@DecimalMin`, `@Min`), enforced via `@Valid` on the
controller methods and reported through
[`GlobalExceptionHandler`](src/main/java/com/letsplay/exception/GlobalExceptionHandler.java).

---

## Bonus

### 10. Are CORS policies set appropriately?

❌ **Not implemented.** There is no `CorsConfigurationSource`/`@CrossOrigin` anywhere in the project,
and no `.cors(...)` call in [`SecurityConfig`](src/main/java/com/letsplay/config/SecurityConfig.java).
A cross-origin preflight request currently fails outright since `OPTIONS` isn't a permitted method:

```powershell
curl.exe -k -s -i -X OPTIONS https://localhost:8443/api/products -H "Origin: http://example.com" -H "Access-Control-Request-Method: GET"
```

**Current result:** `401`, no `Access-Control-*` headers — i.e. cross-origin browser access doesn't
work at all yet. Optional to implement (bonus).

---

### 11. Is rate limiting implemented to prevent brute force attacks?

❌ **Not implemented.** There's no request-rate filter/bucket (e.g. Bucket4j, a custom
`OncePerRequestFilter` counter, or an API gateway) anywhere in the project. Confirm by hammering the
login endpoint — it currently never throttles:

```powershell
1..20 | ForEach-Object {
    curl.exe -k -s -o NUL -w "%{http_code} " -X POST https://localhost:8443/api/auth/login -H "Content-Type: application/json" -d '{\"username\":\"admin\",\"password\":\"wrong-password\"}'
}
```

**Current result:** all 20 attempts return `401` individually — none get throttled with `429 Too Many Requests`. Optional to implement (bonus).

