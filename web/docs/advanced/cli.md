## EYWA CLI client

EYWA client was built for Robotics module but it got a long way since. Initially
it was made to allow robots (scripts) to communicate with Robotics module. Now
it is used for:

 * **Distribution**
 * **Management**
 * **[Development](#development)**
 * Robotics Agent

Here you can find [installation](https://github.com/neyho/eywa-core?tab=readme-ov-file#installation)
instructions.


## Distribution

With EYWA CLI client you can list and install EYWA based application versions
that are available at some AWS S3 compatible endpoint.

Sepecify usual environment variables for S3 authorization and bucket.
```shell
export AWS_ACCESS_KEY_ID=<your_access_key>
export AWS_REGION=<your_region>
export AWS_SECRET_ACCESS_KEY=<your_secret>
export EYWA_BUCKET=<name_of_your_bucket>
```
This is the way how EYWA core is distributed with eywa cli client. Versions
are stored in AWS bucket with public read access. It is just bucket with bunch
of JAR files.

<div id="eywa_core_bucket"></div>


#### Use CLI
```shell
eywa server -l       # will list versions in bucket
eywa server -s x.y.z # will download version from bucket
eywa server start    # will start server in backround
eywa server stop     # will stop server


eywa server -h       # will show you all doc that you need :)
```


:::tip Distribute YOUR app
Use EYWA CLI with your bucket and distribute your EYWA based app. It is a shortcut.
Take a look on how to build/package app
[here](https://github.com/neyho/eywa-examples/blob/master/clojure/build.clj)

Steps would be:

 * compile
 * **specify main namespace** and make uberjar
 * upload jar to bucket
:::


## Management
CLI client can be used to download packaged apps from S3 compatible endpoint.
It can also be used to initialize app, to add super users and to enable encryption.

#### Initialization

```shell
eywa server init

For EYWA Postgres initialization you have to
specify user and database to connect to that has
rights to create new databases. Also password is
required to establish this connection. So if you
would be kind:

Admin DB(postgres): 
User: postgres
Password: password
```

Will start EYWA initialization for given configuration
or environment variables. Dataset models will be deployed and
default data stored.

Still you will have to add user so that you can access running
server.

```shell
eywa server super -s admin
```

#### Encryption
You can use cli client to initialize EYWA [encryption](./encryption).

```shell
❯ eywa encryption init -h
Initialize EYWA encryption module. This is one time operation 
so make sure to save generated output.

  -m, --master  Initialize encryption and return master key
  -s, --shares  Initialize encryption and return master key shares
  -h, --help
```

Or to unseal already encrypted instance.
```shell
❯ eywa encryption unseal -h 
Unseal EYWA encryption module. You will be prompted
to input master key or secret share.

  -m, --master  Unseal encryption module with master key
  -s, --share   Unseal encryption module with key share
  -h, --help
```


## Development

This section is really important. As mentioned we use EYWA client
in Robotics module for two way communication with our "robotics resource servers".

You can use one way communication with **any** EYWA instance by connecting
eywa client to running EYWA server. 

<div id="eywa_connect"></div>

This is screenshot of terminal after running `eywa connect` command. It will
start [device code]() flow specified by OAuth RFC. In other words you should
authenticate to running EYWA service through browser and you will recive
access and refresh token for EYWA client.

#### Run anything
You can than use `eywa run` command to run **anything**.

<div id="eywa_run_explanation"></div>

What is important here is that you can use scripts in different languages
to interact with EYWA. Deploy data models, write data, read data.

It sounds basic but think of it as resource that you can leverage
during development or in production. Data migrations, infrastructure management,
build process, CI/CD, monitoring... It just keeps going.

:::info When EYWA?
When you need it. When it helps you. You can build fully functional SPA
using only core functionality. You can extend it with your logic and bend
it to fit your business needs.

You can use it as tool as well! **From command line interface!!!**
:::

Concept is simple and reliable. It is easy to write clients
in different languages that work this way.


#### Examples

 * [Babashka](https://github.com/neyho/eywa-examples/tree/e01353604ad3ad976131f28e48c082504c4ee11c/bb)
 * [Javascript](https://github.com/neyho/eywa-examples/tree/e01353604ad3ad976131f28e48c082504c4ee11c/js/scripting)
 * [Python](https://github.com/neyho/eywa-examples/tree/e01353604ad3ad976131f28e48c082504c4ee11c/py/scripting)



## Configuration

During development it is very likely that you will work on multiple
projects or/and multiple environments (testing, staging, production)
and with multiple versions.

Environment variables don't play that nicely with development. If you
aren't using [direnv](https://direnv.net/) or something simillar. 
It is very likely that you will set some environment variable, forget 
about it and then do something that you will regret.

Because you were thinking that you were
working with this DB and not with production DB and... You'll have a
bad day.

This is where _"configuration"_ comes in. It is not exactly configuration
as much as **EYWA CLI client state**. It looks like configuration and
feels like configuration so lets call it configuration.

:::info Why state?
Because EYWA CLI client will change that file as well. When you set server
version for some bucket it will store that information.

Also when connecting to running server, you'll receive tokens that EYWA CLI
client will store in that file as well. Therefore it is **not exactly configuration**
:::

It works like this. When you are working on some project where you wan't
to connect to some running server or you wan't to run server on some
specific environment for that project you should initialize config.

```shell
❯ eywa server gen-config
EYWA will try to read configuration
in following order:

./eywa.edn
./eywa.json
~/.eywa/config/eywa.edn
~/.eywa/config/eywa.json

By calling gen-config and specifying what format to generate
either ./eywa.edn or ./eywa.json file will be created. This
is intended for local development, where you need to connect
to different EYWA instances in different projects in different
folders

To edit global configuration that is not folder/project specific
position yourself to "~/.eywa/config" and run gen-config command

Also don't forget that EYWA uses environment variables to the same
extent as configuration file

  -j, --json
  -e, --edn
  -h, --help



❯ eywa server gen-config -e
```
:::tip Configuration per project
During development create configuration file for each project
that you are working on to minimise risk of unintentional damage.
:::

#### EDN Config
```clojure
❯ cat eywa.edn 
;; {:server
;;  {:options ["-Duser.country=HR"
;;             "-Duser.language=hr"
;;             "-Dfile.encoding=UTF-8"]
;;   :port 8080}
;;  :postgres
;;  {:host "localhost"
;;   :port 5432
;;   :db "eywa_test"
;;   :user "eywa"
;;   :password "********"
;;  }}
{:postgres
 {:host "localhost",
  :port 5432,
  :db "eywa_test",
  :user "postgres",
  :password "password"}}


```

#### JSON Config
```json
❯ cat eywa.json
{"server":
 {"options":
  ["-Duser.country=HR", "-Duser.language=hr",
   "-Dfile.encoding=UTF-8"],
  "port":8080},
 "postgres":
 {"host":"localhost",
  "port":5432,
  "db":"eywa_test",
  "user":"postgres",
  "password":"password"}}
```


:::warning Configuration is important
EYWA CLI client will **first look for configuration**. If it can't find
configuration file than it will fallback to environment variables.

**WHY?**

In production environment you probably won't use configuration, therefore
this won't be a problem.

During development it is very likely that you will have multiple projects
or environments (testing, staging, production) and environment variables
don't shine here and can lead to unexpected situations.

For development use configuration!
:::


