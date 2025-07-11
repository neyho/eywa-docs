## Identity Access Management

The IAM module is the foundational layer responsible for **authentication**, **authorization**, and **fine-grained access control** in the system. Built on **OAuth 2.1** and **OpenID Connect (OIDC)** standards, it supports flexible identity types, role-based permissions, and structured access to protected data and APIs.

---

# 🧍 User

Users represent identities in the system and can be individuals, agents, or backend services.

### Types
- **Person** – Human users with full OIDC profiles (e.g., name, email, birthdate)
- **Robot** – Autonomous agents such as robots or RPA processes
- **Service** – Backend integrations needing programmatic access

---

# 👥 User Group

Groups organize users and help manage permissions at scale.

### Types
- **ACCESS** – Restrict or filter visibility (e.g., dataset search limits)
- **ROBOTICS** – Logical groups for robotic/service-type users

### Usage
- Apply global access restrictions
- Simplify role and client authorization

---

# 👮 Role

Roles represent sets of permissions and serve as the core unit for authorization.

- Control access to:
  - Dataset Entities
  - Entity Attributes
  - Dataset Relations
- Define:
  - **Read/Write/Delete Access**
  - Ownership of entities
- Can be assigned to:
  - Users
  - Groups
  - OAuth Scopes

---

## 🧩 OAuth Clients and OAuth APIs (Resource Servers)

OAuth 2.1 introduces a clear distinction between two types of components:

- **OAuth Clients** – The applications that want to *access something*
- **OAuth Resource Servers (APIs)** – The services that *hold protected data*

In your IAM module, these concepts are implemented via **Apps** and **APIs**.


## 🎯 Why Separate Clients from APIs?

This separation ensures:
- **Security** – Clients only get access tokens scoped to what they need
- **Flexibility** – Different apps can access different APIs with different scopes
- **Interoperability** – Standards ensure clients and APIs work across organizations

---

## 🧭 OAuth Clients (Apps)

An **OAuth Client** is any app that requests access to protected resources on behalf of a user (or itself).

### Examples
- A web dashboard requesting access to a user's data
- A robot requesting task updates
- A CI/CD service pulling deployment secrets

### Key Attributes
| Field      | Description |
|------------|-------------|
| `Client ID` | Unique public identifier for the app |
| `Secret`    | Confidential (used only by trusted clients) |
| `Type`      | Web, Native, Device, Service |
| `Active`    | Whether the client is enabled |
| `Settings`  | Redirect URIs, grant types, PKCE, etc. |
| `Avatar`    | Optional UI display icon/name |

### Client Types
- **Confidential** – Can securely store secrets (e.g., backend apps)
- **Public** – Cannot securely store secrets (e.g., mobile apps, SPAs)

### Supported Grant Flows
- `Authorization Code` (for user login)
- `Client Credentials` (for machine-to-machine)
- `Device Code` (for headless devices like TVs)
- `Refresh Token` (for session continuity)

---

## 🌍 OAuth APIs (Resource Servers)

An **OAuth API**, or **Resource Server**, is a backend that hosts protected data and validates access tokens.

### Examples
- A GraphQL API serving user dashboards
- A telemetry API for robots
- A database access API

### Key Attributes
| Field       | Description |
|-------------|-------------|
| `Audience`  | Identifier clients must target in access tokens |
| `Name`      | Human-readable name of the API |
| `Description` | What the API provides |
| `Avatar`    | Optional icon |
| `Configuration` | Custom token validation logic, CORS rules, etc. |

### Responsibilities
- Validates tokens via `Authorization` headers
- Enforces access based on **scopes**
- Responds with protected resources if access is allowed

---

## 🔐 OAuth Scopes

**Scopes** are strings that define what the client is allowed to do.

- Defined by the **API**
- Requested by the **Client**
- Authorized by the **User or IAM policy**




### Example

If the API defines:

```text
Scope: robot:read
Scope: robot:write
```

Then:
- A Client may request `robot:read`
- The token it receives will contain that scope
- The API will allow the client to **read robot data**, but **not write**

---

## 🔄 How They Work Together

```text
1. OAuth Client asks for access:
   → "I want `robot:read` for API `robot-api`"

2. IAM evaluates and issues a token:
   → Token contains audience = `robot-api`, scope = `robot:read`

3. Client uses token to call the API:
   → Sends token in Authorization header

4. API validates token:
   → Checks audience and scope
   → Responds with data or rejects the request
