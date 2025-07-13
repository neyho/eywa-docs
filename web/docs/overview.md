**E**liminate **Y**our **W**aste **A**ctivities by  using set 
of services connected in opinionated way by levereging some
of edge web/mobile technologies.


**[Data Modeling](./data)**

 - **exportable/transportable/deployable** data models
 - **on deploy GraphQL exposure** of generated API
 - **[Embeddable](https://github.com/neyho/eywa-examples/tree/master/clojure)**
 - **[Extendable](./graphql/extend)**
 - **[Plugable](./graphql/extend#hooks)**
 - **[Hackable](./advanced/cli#development)**

<div id="in-short"></div>


**[Identity Access Management](./iam)**

#### OAuth 2.1
- ✅ Authorization Code Flow with PKCE
- ✅ Client Credentials Flow
- ✅ Device Code Flow
- ✅ Refresh Token Support
- ✅ Token Revocation 
- ✅ JWT Access Tokens
- 🚧 Token Exchange RFC 8693
- 🚧 Conscent Screens & Prompt Handling
- 🚧 Dynamic Client Registration



#### OIDC
- ✅ ID Token
- ✅ UserInfo Endpoint
- 🚧 Authentication Context Class Reference
- 🚧 Authentication Method Reference
- ✅ OIDC Discovery Endpoint
- ✅ JWKS
- 🚧 Federation/External Identity Providers
  - 🚧 LDAP
  - 🚧 Google
  - 🚧 Github
  - 🚧 Facebook
  - 🚧 Microsoft...
- ✅ Session Management
- ✅ Client Logout
- 🚧 Backchannel Logout
- ✅ Frontchannel Logout



## Why?


## Quickstart

#### Windows

```ps1
Invoke-WebRequest -Uri "https://s3.eu-central-1.amazonaws.com/eywa.cli/install_eywa_cli.ps1" -OutFile eywa_cli_install.ps1
./eywa_cli_install.ps1
rm eywa_cli_install.ps1
```

:::note Add following to PATH environment variable
```text
%USERPROFILE%\.eywa\bin
``` 
:::


#### Linux and MacOs
```bash
curl -s https://s3.eu-central-1.amazonaws.com/eywa.cli/install_eywa_cli.sh | bash
```


#### Install DB
Currently supported database is Postgres, although support for MySQL, SQLite, XTDB and others is on roadmap. 

Requirement is to have Postgres, so if you don't have installation i recommend using docker to download Postgres
image and afterwards run:

```text
docker run --name EYWA_DEV -p 5432:5432 -e POSTGRES_PASSWORD=password postgres
```

This will spin up container with port forwarding so that you can connect to database on localhost using
postgres user.

Now we need to install eywa server jar file. Run
```text
eywa server -l

# And output should look something like this
List of available versions. '*' installed, '-' not installed

[*] 0.5.0
[-] 0.4.1
[-] 0.3.9
[-] 0.3.7
[-] 0.3.5
[-] 0.3.4
[-] 0.3.3
[-] 0.3.2
[-] 0.3.1
[-] 0.3.0
[-] 0.2.9
```

To install some version run
```text
eywa server -s 0.5.0
```

eywa server server is installed, now we need to initialize EYWA IAM and Datacraft. So we should run:

```text
eywa server init
```

If above command didn't throw any error that implies that initialization was successfull and DB is initialized.
So everything is ready to start EYWA server, except there is no user that can login to EYWA.
```text
eywa server super -s admin
```

Will prompt for password for admin password, and when supplied admin user will be created with target password.
Now run:

```text
eywa server start
```

And navigate to **https://my.eywaonline.com/eywa/** and login screen should be waiting for you. Use username and
password from previous step to login.

To track what is happening open log file at location __~/.eywa/logs/system.log__


If something went wrong or eywa server server isn't running as supposed to, run
```text
eywa server doctor
```


#### CLI
__eywa__ cli client can connect to EYWA server by running
```text
eywa connect http://localhost:8080
```
You will have to complete device code flow for target user and
after successfull connection client will use provided tokens to
interact with EYWA server.

Client also supports executing scripts through
```text
eywa run -c "py example.py"
```
This will encapsulate running script process and by using one of
supported client libraries script can interact with connected EYWA
server with authorized EYWA user. The part above

For now we support:
 * Babashka [eywa-client](https://clojars.org/org.neyho/eywa-client)
 * Python - [eywa-client](https://pypi.org/project/eywa-client/)
 * Javascript - [eywa-client](https://www.npmjs.com/package/eywa-client)
 * Go  - [eywa-client](https://github.com/neyho/eywa-go/tree/main)
 * Ruby - [eywa-client](https://rubygems.org/gems/eywa-client/versions/0.3.0)

EYWA can be controlled through environment variables as well. To see
how, run:

```text
eywa server -h
```
