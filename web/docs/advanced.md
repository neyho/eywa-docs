## EYWA is not an island

Some features like **Data Modeling** are powerful and very useful—an out-of-the-box
replacement for CRUD applications. But soon, you'll reach a point where you need
to extend the generated GraphQL schema and add custom state or logic.

Most how-to guides are explained in the GraphQL [Extend](./graphql/extend) section of this documentation.


:::info Production?
Ready for production?
What do you need to set up so that IAM works properly?
How do you package your datasets, roles, groups, apps, and APIs?
When and how do you import data critical to **your** application?
:::


<div id="tour"></div>

## [EXAMPLES](https://github.com/neyho/eywa-examples)
Checkout examples how to use EYWA from different perspectives. There are multiple
examples of extending EYWA backend, interaction with EYWA through
[Babashka](https://github.com/neyho/eywa-examples/tree/master/bb),
[Python](https://github.com/neyho/eywa-examples/tree/master/py/scripting),
[Javascript](https://github.com/neyho/eywa-examples/tree/master/js/scripting) from command line.


Using EYWA just as Authorization Server in combination with Python and JS resource
servers.

Include EYWA IAM in your [Clojurescript]() application.

## [CLI](./advanced/cli)

EYWA’s IAM implementation supports the **device code** flow.
This allows interaction with EYWA directly from the command line.

The **EYWA CLI Client** makes this even easier. Its features include:

* **Distribution** — use the CLI client to distribute apps built on top of EYWA
* **Management** — initialize new deployments, add superusers, enable encryption, etc.
* **Development** — includes a client-side implementation of the device code flow.
  This makes it possible to “piggyback” any language to use EYWA’s backend.
  [See more...](./advanced/cli#development)


## [Environment](./advanced/environment)

EYWA is configured **only through environment variables**.
Here are some advanced features you can control via environment variables:

* **Serve SPA Assets** — define the folder served by the EYWA server
* **Enforce Data Access** — disabled by default for easier development
* **Public Access** — allows public access to entities assigned to the **Public** role.
  You define what the public can read/write/delete
* **Encryption** — support for encrypted attributes in entities.
  Encryption must be initialized once, and **unsealed** on every restart
* **OAuth Persistence** — stores OAuth sessions, tokens, JWKS, etc.
  *Requires encryption to be enabled!*


## [Encryption](./advanced/encryption)

EYWA supports database-level encryption.
Encrypted data can only be read by EYWA itself.

You must initialize encryption before use.

Every time the EYWA server starts, you must **unseal** encryption
by providing either a master key or a number of shared keys.

:::info Sounds scary
But it isn’t.
You can define the master key using an environment variable in production.
Or you can use the EYWA CLI Client to unseal encryption from the command line!
:::

