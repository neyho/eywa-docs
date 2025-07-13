## System
Variables that will control where EYWA will store logs and where it
will look for executable code.

:::note EYWA_HOME
Path to location that EYWA will consider root.
Defaults to `~/.eywa`
:::


:::note EYWA_LOG_DIR
Path to directory where EYWA will store logs. 
If path is relative it will be computed relative to `EYWA_HOME`
environment value
:::


:::note EYWA_GIT_DIR
Path to directory where EYWA will store GIT repositories.
If path is relative it will be computed relative to `EYWA_HOME` environment value
:::


:::note EYWA_BIN_DIR
Path to directory where EYWA will look for binaries
that are important for eywa command line client
If path is relative it will be computed relative to 
`EYWA_HOME` environment value
:::

## Platform
Variables that are important to configure how EYWA
platform will work. Set up log level or configure IAM
parameters important for your setup!

:::tip EYWA_SERVE
This environment variable can be used to serve web resources.
It is SPA compliant.

I.E. lets say that **EYWA_SERVE='/opt/web/'**.
When EYWA receives GET request it will look for resource
file relative to that location.

If resource file isn't found it will try to serve **index.html**. If
that resource isn't available it will try to serve **index.html** from
one level down, until it reaches **'/'**. If none is available it will
return "404 Not found".
:::

:::note EYWA_LOG_LEVEL
Specify EYWA log level. Allowed values are:

- trace
- debug
- info
- error
- fatal

Doesn't matter if value is lowercase or uppercase.

Also it is possible to narrow down log level:
- **EYWA_DATASET_LOG_LEVEL**
- **EYWA_IAM_LOG_LEVEL**
- **EYWA_GIT_LOG_LEVEL**
- **EYWA_ROBOTICS_LOG_LEVEL**
- **EYWA_REACHER_LOG_LEVEL**
- **EYWA_ROBOTICS_LOG_LEVEL**
:::


:::note EYWA_SERVER_HOST
Configure host where EYWA will be served. "localhost", "0.0.0.0"...
:::

:::note EYWA_SERVER_PORT
Configure port where EYWA will be served. "8080"
:::

:::danger EYWA_IAM_ROOT_URL
This is very important! When you develop and work with localhost
everything will work OK.

But once you wan't to go to production it is very important to
know that OAuth requires **https** endpoint.

In addition this variable is required to specify what URL request
will it allow to access IAM module. This is where you should specify
your production host URL.

I.E. **https://192.168.1.100:8080**, or **https://my.company.com**
:::

:::danger EYWA_IAM_ENFORCE_ACCESS
By default enforcing role access on deployed data models is disabled.
You need to explicitly set **EYWA_IAM_ENFORCE_ACCESS=true** to enable it.

Reason is practical. You need to feel the power! Control it later!
:::

:::tip EYWA_IAM_ALLOW_PUBLIC
By setting this value to true, EYWA will add __public__ role to 
'User Role' records. This is **special** role.

You can use this role to make dataset available to public. EYWA
won't look for access token or try to validate user for deployed entities
and releations that have R/W/D associated with __public__ role.
:::


:::tip EYWA_DATASET_ENCRYPTION_KEY
When encryption is enabled you are responsible for [unlocking encryption](./encryption).
If your setup is using **master** key for encryption than set this
value in this environment variable and encryption will be unlocked 
during bootstrap.
:::

:::tip EYWA_OAUTH_PERSISTENCE
Set this value to true if you wan't to store access and refresh tokens
so that after restart authorized users don't have to re-authorize. 

**EYWA encryption has to be enabled!** to allow storing tokens and
other important data used to create access and refresh tokens.
:::

:::note EYWA_ROBOTICS_ENDPOINT
Path where eywa client can register to transform host
into resource server.

EYWA_ROBOTICS_ENDPOINT='ws://localhost:8080/robotics'
:::

:::note EYWA_LICENSE
Value of EYWA license that is used to register at
eywa robotics module
:::



## Database
Specify Postgres parameteres required that are required to make
JDBC connection with PostgreSQL server. Define following environment
variables:


```shell
POSTGRES_HOST='localhost'
POSTGRES_PORT='5432'
POSTGRES_DB='eywa_dev'
POSTGRES_USER='postgres'
POSTGRES_PASSWORD='password'
HIKARI_MAX_POOL_SIZE='20'

# It is important to specify ADMIN params
# during EYWA initialization. EYWA will 
# try to create DB specified in above params
# and ADMIN user must have role that can
# do that. So in order to initialize EYWA
# we need to define ADMIN user params
POSTGRES_ADMIN_DB
POSTGRES_ADMIN_USER
POSTGRES_ADMIN_PASSWORD
```

:::note Pool Size
For development keep this number low. Postgres connection limit
is about 100 connections by default. If you are working in a team
and you are all connecting to same Postgres server than some of
you won't make it if pool size is large.

**In production increase this number for better performance!**
:::

## File Storage
Configure S3-compatible storage for file operations. EYWA supports both AWS S3 
and MinIO for file storage. These variables configure the storage backend used 
for file uploads, downloads, and management.

:::note EYWA_STORAGE_ENDPOINT
S3-compatible storage endpoint URL.

For MinIO (local development): `http://localhost:9000`
For AWS S3: `https://s3.amazonaws.com` or region-specific endpoint

Default: `http://localhost:9000`
:::

:::note EYWA_STORAGE_ACCESS_KEY
Access key for S3-compatible storage authentication.

For MinIO (local development): `minioadmin`
For AWS S3: Your AWS access key ID

Default: `minioadmin`
:::

:::note EYWA_STORAGE_SECRET_KEY
Secret key for S3-compatible storage authentication.

For MinIO (local development): `minioadmin`
For AWS S3: Your AWS secret access key

Default: `minioadmin`
:::

:::note EYWA_STORAGE_REGION
AWS region or region name for S3-compatible storage.

Examples: `us-east-1`, `eu-west-1`, `ap-southeast-1`

Default: `us-east-1`
:::

:::note EYWA_STORAGE_BUCKET
Storage bucket name where files will be stored.

The bucket will be created automatically if it doesn't exist.

Default: `eywa-files`
:::

:::note EYWA_STORAGE_EXPIRY_MINUTES
Expiry time in minutes for presigned URLs used for file uploads and downloads.

Shorter times improve security but may cause issues with large file uploads.

Default: `15`
:::

:::tip MinIO Setup
For local development, you can run MinIO using Docker:

```shell
docker run -p 9000:9000 -p 9001:9001 \
  -e "MINIO_ROOT_USER=minioadmin" \
  -e "MINIO_ROOT_PASSWORD=minioadmin" \
  minio/minio server /data --console-address ":9001"
```

Then access the MinIO console at `http://localhost:9001`
:::

## Trust Store
Configure custom SSL/TLS trust stores for enterprise environments that use internal Certificate Authorities (CA). 
These variables allow EYWA to validate SSL certificates signed by your organization's internal CA.

:::note TRUST_STORE_PATH
Path to the Java keystore file containing trusted certificates.

This should point to a JKS (Java KeyStore) file that includes your organization's 
internal CA certificates and any other trusted certificates.

Example: `/opt/eywa/security/truststore.jks`
:::

:::note TRUST_STORE_PASSWORD
Password for accessing the trust store specified in `TRUST_STORE_PATH`.

Keep this password secure and consider using environment variable injection 
or secrets management systems in production.
:::

:::tip Creating a Trust Store
To create a new trust store and add your internal CA certificate:

1. **Create a new keystore:**
   ```shell
   keytool -genkey -alias temp -keystore truststore.jks -storepass mypassword
   keytool -delete -alias temp -keystore truststore.jks -storepass mypassword
   ```

2. **Add your CA certificate:**
   ```shell
   keytool -importcert -alias mycompany-ca -file ca-certificate.crt \
     -keystore truststore.jks -storepass mypassword -noprompt
   ```

3. **Verify the certificate was added:**
   ```shell
   keytool -list -keystore truststore.jks -storepass mypassword
   ```

4. **Set environment variables:**
   ```shell
   export TRUST_STORE_PATH="/path/to/truststore.jks"
   export TRUST_STORE_PASSWORD="mypassword"
   ```
:::

:::warning Certificate Management
- Always verify certificate fingerprints before importing
- Keep your trust store file secure with appropriate file permissions (600)
- Regularly update certificates before they expire
- In production, use secrets management for the trust store password
:::
