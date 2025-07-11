# GraphQL

EYWA is highly dynamic set of services that are designed to provide zero downtime deploy cycles
and easy to maintain graphical UI for service administration. Core of EYWA is **Data Module**, specifically **DATASETS**.

Applications are build on top of data and data models, and with EYWA it is easy
to model data. What happens after modeling? After successfull deployment of Dataset model in 
**Deploy Applet** EYWA storage service will rearange DB, create GraphQL API 
and reload HTTP endpoint to reflect deployed changes. 


There are 3 generic GraphQL queries and 5 generic GraphQL mutations that 
EYWA will generate from combined deployed Dataset Models.

## Queries
* [Get](./graphql/queries#get)
* [Search](./graphql/queries#search)
* [SearchTree](./graphql/queries#search-tree)

## Mutations
* [Sync](./graphql/mutations#sync)
* [Stack](./graphql/mutations#stack)
* [Slice](./graphql/mutations#slice)
* Delete
* [Purge](./graphql/mutations#purge)


## Extend

 * [Shards](./graphql/extend)
 * [Hooks](./graphql/extend#hooks)
 * [Subscriptions](./graphql/extend#subscriptions)
