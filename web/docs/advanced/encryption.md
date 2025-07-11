EYWA supports data encryption. Data will be encrypted in DB during write. When
data is pulled from DB it will be decrypted by using appropriate Data Encryption
Key (DEK) that was used to encrypt data.

:::info Encyrption in DB
Data will be visible to caller as is, not encrypted. So data is not encrypted
during transport. Only in DB.
:::

DEK keys are also encrypted and can be decrypted using one of two methods.

 * **master key**
 * **Shamir's secret sharing** (multiple keys with decryption threshold)

:::danger ONLY ONCE
Encryption initialization can be ran only once. Once encryption is initialized
you will get your key(s). Store them securely!

Additionaly you don't pick your master key. Master key or even Shamir's shared
secret is generated randomly. **You only need to save what was generated.**
:::


## Initializing Master Key

**initEncryptionMaster** mutation is exposed in GraphQL interface. By calling
this mutation eywa will initialize DEK table and return master key. 

#### Query
```graphql
mutation {
  initEncryptionMaster
}
```

#### Response
```json
{
  "data": {
    "initEncryptionMaster": "268140228460985196727735326177103211808297432639594406440473722508485328896"
  }
}
```

:::tip Using EYWA client
You can initialize encryption from CLI with EYWA client:
```
eywa encryption init --master
```
:::


## Initializing Shamir's shares

**initEncryptionShares** mutation is exposed in GraphQL interface. You can
specify:

 * **shares** - how many shares do you wan't to generate
 *  **threshold** - how many of shares are required to unlock master key


#### Query
``` GraphQL
mutation {
  initEncryptionShares(shares:5 threshold:3) {
    id
    value
  }
}
```

#### Response
```json
{
  "data": {
    "initEncryptionShares": [
      {
        "id": 1,
        "value": "73180974868740132401546629691466071341"
      },
      {
        "id": 2,
        "value": "291461197105375469893581276456537631648"
      },
      {
        "id": 3,
        "value": "315483198632504219279346262119052889749"
      },
      {
        "id": 4,
        "value": "145246979450126380558841586679011845644"
      },
      {
        "id": 5,
        "value": "121034906479180417195441857568182710630"
      }
    ]
  }
}
```
:::tip Using EYWA client
You can initialize encryption from CLI with EYWA client:
```
eywa encryption init --shares
```
:::



## Unseal Encrypted Data

When EYWA is started encryption is not initialized by default. You have to
**unseal** encrypted data. If you don't than values of encrypted data
will be **nil**.


:::tip With GraphQL
To **unseal** encrypted data use one of GraphQL mutations:

 * **unsealEncryptionWithMaster**
 * **unsealEncryptionWithShare**
:::


:::tip With environment variable
EYWA will recognise **EYWA_DATASET_ENCRYPTION_KEY** environment variable
and use that value to unseal encrypted data with that key.
:::


:::tip With EYWA client
Otherwise you can use one of EYWA client commands:
```
eywa encryption unseal --master
eywa encryption unseal --share
```
**Before that you have to connect to target server!**
:::
