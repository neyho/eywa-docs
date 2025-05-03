## Operators
GraphQL queries accept arguments as per GraphQL [specification draft](https://spec.graphql.org/June2018/). 
Usually user will wan't to do filtering, ordering, limit results
and other features to make concise queries to EYWA background.

Arguments can either be [Scalars](https://spec.graphql.org/June2018/#sec-Scalars), 
[Enums](https://spec.graphql.org/June2018/#sec-Enums) or [Objects](https://spec.graphql.org/June2018/#sec-Objects).

EYWA will generate search _Types_ that are called operators for modeled entities
and relations in data model. 


## Scalar Query Operators
For each of **supported types** special _Query_ operator is created to
enable parametrized queries. Search results can be filtered by specifying one or more
values for __QueryOperator__ object fields. 

### Legend
 * **_eq** - short for Equal `=`
 * **_neq** - short for Not Equal `!=`
 * **_ge** - short for Greater or Equal `>=`
 * **_gt** - short for Greater Than `>`
 * **_le** - short for Less or Equal `<=`
 * **_lt** - short for Less Than `<`
 * **_ilike** - enables string matching by specifying pattern (caseing ignored)
 * **_like** - enables string matching by specifying pattern
 * **_in** - if value is in list of strings than return that record
 * **_not_in** - if value is *not* in list of strings than return that record
 * **_limit** - limits output results for that entity to [Int] value
 * **_distinct** - marks that only distinct values will be returned
 * **_offset** - drop first *offset* results
 * **_order_by** - order by one or more attributes :asc or :desc


### Boolean

 * **_boolean**: TRUE | FALSE | NOT_TRUE| NOT_FALSE | NULL


### String

 * **_eq**: String
 * **_neq**: String
 * **_ilike**: String
 * **_like**: String
 * **_in**: [String]
 * **_not_in**: [String]


### Integer and Float

 * **_eq**
 * **_neq**
 * **_ge**
 * **_gt**
 * **_le**
 * **_lt**
 * **_in**
 * **_not_in**


### UUID

 * **_eq**: UUID
 * **_neq**: UUID
 * **_in**: [UUID]
 * **_not_in**: [UUID]

### Timestamp

 * **_eq**: Timestamp
 * **_ge**: Timestamp
 * **_gt**: Timestamp
 * **_le**: Timestamp
 * **_lt**: Timestamp
 * **_neq**: Timestamp


### Search Operator

To enable detailed search EYWA will create `SearchOperator` for each entity. This operator
object is consisted of searchable fields. Scalar fields will be of matching scalar query operator.
Following fields are added to **searchOperator** object.

## Additional search operators
 * **and** [searchOperator] - AND condition
 * **or** [searchOperator] - OR condition
 * **not** [searchOperator] - Negation

:::tip Complex Queries
Additional conditions accept list of **same searchOperator** object. As consequence AND, OR and NOT
can be combined with multiple scalar field conditions to enable query creation without restrictions

Generated operator object will have camelCased name that starts with _search_ following
entity name and ending with suffix _Operator_. More details in **search operator example**.


## Get


## Search


## Search Recursive
