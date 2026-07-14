// ============================================================
// Let's Play — MongoDB schema initialisation
// Run with:  mongosh --file scripts/init-db.js
// ============================================================

db = db.getSiblingDB("lets_play_db");

// ── users ────────────────────────────────────────────────────

db.createCollection("users", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["name", "email", "password", "role"],
      additionalProperties: true,
      properties: {
        _id: { bsonType: "string" },
        name: {
          bsonType: "string",
          minLength: 2,
          maxLength: 100,
          description: "required, 2–100 characters"
        },
        email: {
          bsonType: "string",
          pattern: "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$",
          description: "required, must be a valid e-mail address"
        },
        password: {
          bsonType: "string",
          minLength: 8,
          description: "required, stored as a bcrypt hash"
        },
        role: {
          bsonType: "string",
          enum: ["user", "admin"],
          description: "required, must be 'user' or 'admin'"
        }
      }
    }
  },
  validationLevel: "moderate",   // existing documents are not re-validated
  validationAction: "error"      // reject inserts/updates that violate the schema
});

db.users.createIndex(
  { email: 1 },
  { unique: true, name: "unique_email" }
);

print("✔  users collection created");

// ── products ─────────────────────────────────────────────────

db.createCollection("products", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["name", "description", "price", "userId"],
      additionalProperties: true,
      properties: {
        _id: { bsonType: "string" },
        name: {
          bsonType: "string",
          minLength: 2,
          maxLength: 200,
          description: "required, 2–200 characters"
        },
        description: {
          bsonType: "string",
          maxLength: 1000,
          description: "required, up to 1 000 characters"
        },
        price: {
          bsonType: "double",
          minimum: 0.01,
          description: "required, must be a positive number"
        },
        userId: {
          bsonType: "string",
          description: "required, references the owning user's _id"
        }
      }
    }
  },
  validationLevel: "moderate",
  validationAction: "error"
});

db.products.createIndex(
  { userId: 1 },
  { name: "idx_products_userId" }
);

print("✔  products collection created");
print("✔  Database 'lets_play_db' initialised successfully");
