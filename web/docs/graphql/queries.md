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
 * **_boolean** - TRUE | FALSE | NOT_TRUE| NOT_FALSE | NULL
 * **_limit** - limits output results for that entity to [Int] value
 * **_distinct** - marks that only distinct values will be returned
 * **_offset** - drop first *offset* results
 * **_order_by** - order by one or more attributes :asc or :desc

### Search Operator

To enable detailed search EYWA will create `SearchOperator` for each entity. This operator
object is consisted of searchable fields. Scalar fields will be of matching scalar query operator.
Following fields are added to **searchOperator** object.

 * **and** [searchOperator] - AND condition
 * **or** [searchOperator] - OR condition
 * **not** [searchOperator] - Negation

:::tip Complex Queries
Additional conditions accept list of **same searchOperator** object. As consequence AND, OR and NOT
can be combined with multiple scalar field conditions to enable query creation without restrictions

Generated operator object will have camelCased name that starts with _search_ following
entity name and ending with suffix _Operator_. More details in **search operator example**.

### Order By Operator
To enable ordering of search query results EYWA will create OrderByOperator object for each entity. 
This operator object will have same fields as entity that it was created from and value 
type for each scalar field is *order_by_enum* type that is consisted of two values:

 * **asc** - Ascending
 * **desc** - Descending

Generated operator object have camelCased name that starts with _orderBy_ following
entity name and ending with suffix _Operator_. More details in [example](). Finally
every entity search query accepts **_order_by** field that is list of _orderByOperator_ for that entity.


## Get

Takes dataset entity unique identifier and arguments to pinpoint target row, selection that specifies
which attributes and relations selection should be returned. 

<div id="open-get-anchor"></div>

Each entity
will have generic get query in camelCase entity name prefixed by `get`. For example 
<a id="open-datasets-model">dataset</a> following get methods are generated:

.Datasets Get Methods:
* **getDataset**(euuid:UUID, name:String)
* **getDatasetVersion**(euuid:UUID)
* **getDatasetRelation**(euuid:UUID)
* **getDatasetEntity**(euuid:UUID, name:String)
* **getDatasetEntityAttribute**(euuid:UUID)

Entity records can be located by specifying euuid or if unique key/s are defined for that entity
than those argument fields are are also valid (_getDataset_, _getDatasetEntity_).
Lets make some queries. I.E. we'll query _Dataset Entity_ to return _Dataset Entity_ record.

```graphql
{
  getDatasetEntity(name:"Dataset Entity") {
    configuration
    euuid
    height
    modified_on
    name
    position
    type
    width
  }
}
```

And response should be:

```json
{
  "data": {
    "getDatasetEntity": {
      "configuration": "{\"~:constraints\":{\"~:unique\":[[\"~u48631c43-1a57-4ed2-9d8e-57a286142a23\"]]}}",
      "euuid": "a0d304a7-afe3-4d9f-a2e1-35e174bb5d5b",
      "height": 152.7505645751953,
      "modified_on": "2020-07-09T08:26:25Z",
      "name": "Dataset Entity",
      "position": {
        "x": -880,
        "y": 630
      },
      "type": "STRONG",
      "width": 178.97463989257812
    }
  }
}
```

:::info Newbie
Wou, this is so cool. What else can I do?
:::

It is possible to pull data from related entities by specifying directed relation label and fields.
Let's pull all attribute name and types and all dataset versions where this entity is used.

#### QUERY
```graphql
{
  getDatasetEntity(name:"Dataset Entity") {
    configuration
    euuid
    height
    modified_on
    name
    position
    type
    width
    attributes {
      name
      type
    }
    dataset_versions {
      name
      modified_by {
        name
      }
      modified_on
    }
  }
}
```

#### RESPONSE
```json
{
  "data": {
    "getDatasetEntity": {
      "configuration": "{\"~:constraints\":{\"~:unique\":[[\"~u48631c43-1a57-4ed2-9d8e-57a286142a23\"]]}}",
      "euuid": "a0d304a7-afe3-4d9f-a2e1-35e174bb5d5b",
      "height": 152.7505645751953,
      "modified_on": "2020-07-24T22:30:30Z",
      "name": "Dataset Entity",
      "position": {
        "x": -880,
        "y": 630
      },
      "type": "STRONG",
      "width": 178.97463989257812,
      "attributes": [
        {
          "name": "Name",
          "type": "string"
        },
        {
          "name": "Width",
          "type": "float"
        },
        {
          "name": "Height",
          "type": "float"
        },
        {
          "name": "Position",
          "type": "json"
        },
        {
          "name": "Type",
          "type": "enum"
        },
        {
          "name": "Configuration",
          "type": "transit"
        }
      ],
      "dataset_versions": [
        {
          "name": "1",
          "modified_by": {
            "name": "rgersak"
          },
          "modified_on": "2020-07-24T22:30:28Z"
        }
      ]
    }
  }
}
```
:::info Newbie
I'm actually crying now... Are you saying that I can just ask what I wan't and I will GET it?
It's like best religion ever!
:::

It is possible to pull data for related entites by referencing labels on modeled relations. This
is not restricted to one model. **ALL** models are combined in one big data model with all entities and
relations reconciled and available through single GraphQL endpoint. Everything is connected!

:::info Newbie
Don't play smart with me! Is there anything else?`
:::

To narrow subselection search, user can set the parameters for relation. Lets try to find all attributes
that are of a type _string_ or _float_.

#### QUERY
```graphql
{
  getDatasetEntity(name:"Dataset Entity") {
    name
    type
    attributes(_where:{_or:[{type:{_eq:string}} {type:{_eq:float}}]} _order_by:{name:desc}) {
      name
      type
    }
  }
}
```

#### RESPONSE
```json
{
  "data": {
    "getDatasetEntity": {
      "name": "Dataset Entity",
      "type": "STRONG",
      "attributes": [
        {
          "name": "Width",
          "type": "float"
        },
        {
          "name": "Name",
          "type": "string"
        },
        {
          "name": "Height",
          "type": "float"
        }
      ]
    }
  }
}
```


## Search
Search query accepts attribute arguments to pinpoint target rows and selection that
specifies which attributes and relations should be returned.

<div id="open-search-anchor"></div>

Search result will always return list of results. To control how search results will
be returned EYWA will generate search queries that will accept
**_limit**, **_offset** arguments as Int type, and for each entity special
_orderByOperator_ object will be generated to allow user to specify
sequence of attributes with _desc_ and _asc_ values.

Following example will search through all attributes that are active and return
first five records of found attributes with `active=true`.

#### QUERY
```graphql
{
  searchDatasetEntityAttribute(active:{_eq:true} _limit:5) {
    active
    configuration
    constraint
    euuid
    modified_on
    name
    seq
    type
    dataset {
      name
      datasets {
        dataset {
          name
        }
      }
    }
  }
}
```
#### RESPONSE
```json
{
  "data": {
    "searchDatasetEntityAttribute": [
      {
        "active": true,
        "configuration": null,
        "constraint": "unique",
        "euuid": "b9d88982-7d35-4b26-813a-a8d0365c68d6",
        "modified_on": "2020-07-24T22:30:24Z",
        "name": "name",
        "seq": 0,
        "type": "string",
        "entity": {
          "name": "User",
          "dataset_versions": [
            {
              "dataset": {
                "name": "Authentication, Authorization & Access"
              }
            }
          ]
        }
      },
      {
        "active": true,
        "configuration": null,
        "constraint": "optional",
        "euuid": "2c5684ac-d8e1-40a9-8a4b-db602052907f",
        "modified_on": "2020-07-24T22:30:25Z",
        "name": "Password",
        "seq": 1,
        "type": "hashed",
        "entity": {
          "name": "User",
          "dataset_versions": [
            {
              "dataset": {
                "name": "Authentication, Authorization & Access"
              }
            }
          ]
        }
      },
      {
        "active": true,
        "configuration": null,
        "constraint": "optional",
        "euuid": "5cc58b70-6897-4919-bb4f-74e5faea55a4",
        "modified_on": "2020-07-24T22:30:25Z",
        "name": "Avatar",
        "seq": 2,
        "type": "string",
        "entity": {
          "name": "User",
          "dataset_versions": [
            {
              "dataset": {
                "name": "Authentication, Authorization & Access"
              }
            }
          ]
        }
      },
      {
        "active": true,
        "configuration": null,
        "constraint": "optional",
        "euuid": "4ae319a6-2e71-4bf5-ba13-b758e43f0366",
        "modified_on": "2020-07-24T22:30:25Z",
        "name": "Active",
        "seq": 2,
        "type": "boolean",
        "entity": {
          "name": "User",
          "dataset_versions": [
            {
              "dataset": {
                "name": "Authentication, Authorization & Access"
              }
            }
          ]
        }
      },
      {
        "active": true,
        "configuration": null,
        "constraint": "optional",
        "euuid": "6a44cf06-d72c-4930-b2d2-3d607ebf5a04",
        "modified_on": "2020-07-24T22:30:25Z",
        "name": "Personalization",
        "seq": 4,
        "type": "json",
        "entity": {
          "name": "User",
          "dataset_versions": [
            {
              "dataset": {
                "name": "Authentication, Authorization & Access"
              }
            }
          ]
        }
      }
    ]
  }
}
```

## Controling Order

For _Dataset Entity Attribute_ entity **orderByDatasetEntityAttributeOperator** object is 
created with following fields:

 * active: order_by_enum
 * configuration: order_by_enum
 * constraint: order_by_enum
 * euuid: order_by_enum
 * modified_by: order_by_enum
 * modified_on: order_by_enum
 * name: order_by_enum
 * seq: order_by_enum
 * type: order_by_enum

Where order_by_enum is either **asc** or **desc**. Search queries accept **_order_by** operator
that allows to fine tune result order of returned results. Order by can be used on multiple query levels. 

Root entity records can be sorted by any __one to many__ or  __many to many__ relation, as shown
on example below. This ordering is not limited to one level, it can be nested
in query attributes at desired level.

#### Query
```graphql
{
  searchDatasetEntityAttribute(_limit:10 _order_by:{name:desc entity:{name:asc}}) {
    name
    entity {
      name
    }
  }
}
```

#### Response
```json
{
  "data": {
    "searchDatasetEntityAttribute": [
      {
        "name": "ZoneInfo",
        "entity": {
          "name": "Person Info"
        }
      },
      {
        "name": "Width",
        "entity": {
          "name": "Dataset Entity"
        }
      },
      {
        "name": "website",
        "entity": {
          "name": "Person Info"
        }
      },
      {
        "name": "Value",
        "entity": {
          "name": "Access Token"
        }
      },
      {
        "name": "Value",
        "entity": {
          "name": "Refresh Token"
        }
      },
      {
        "name": "URL",
        "entity": {
          "name": "Git Repository"
        }
      },
      {
        "name": "Type",
        "entity": {
          "name": "Dataset Entity"
        }
      },
      {
        "name": "Type",
        "entity": {
          "name": "Dataset Entity Attribute"
        }
      },
      {
        "name": "Type",
        "entity": {
          "name": "OAuth Client"
        }
      },
      {
        "name": "Type",
        "entity": {
          "name": "Task"
        }
      }
    ]
  }
}
```

When executing query that refers to related entity in selection it is possible to order
requested records if relation returns multiple result. This is the case when entity has
__one to many__ or __many to many__ relation. Example bellow shows how nested selection
can control order of returned results.

#### Query
```graphql
{
  searchDatasetEntity (name:{_eq:"Dataset Entity Attribute"}) {
    attributes(_order_by:{name:asc}) {
      name
    }
  }
}
```

#### RESPONSE
```json
{
  "data": {
    "searchDatasetEntity": [
      {
        "attributes": [
          {
            "name": "Active"
          },
          {
            "name": "Configuration"
          },
          {
            "name": "Constraint"
          },
          {
            "name": "Name"
          },
          {
            "name": "Seq"
          },
          {
            "name": "Type"
          }
        ]
      }
    ]
  }
}
```

## Focused Query
World is a complex place, and business rules are usually over complicated, inherited, personalized
and opinionated client view of their business. Each client would say that `This is the way!`.
So many ways...

To navigate throught this data jungle EYWA provides set of tools and one of these 
tools is search query as mentioned.

Is the force strong with search query? Indeed force is strong with this one.
As mentioned ordering can be combined on multiple levels, but so can query
parameters. What does this meen?

EYWA search queries can be focused on **any level** of selection for 
**any attribute**.

:::danger Newbie
This is one ugly bastard.
:::


```graphql
{
  searchDataset (_order_by:{name:asc}) {
    name
    versions(_where:{deployed:{_boolean:TRUE}}) {
      name
      entities (_order_by:{name:desc}) {
        name
        attributes(_where:{name:{_eq:"Active"}}) {
          name
          type
        }
      }
    }
  }
}
```

#### RESPONSE
```json
{
  "data": {
    "searchDataset": [
      {
        "name": "Datasets",
        "versions": [
          {
            "name": "1.0",
            "entities": [
              {
                "name": "Dataset Entity Attribute",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              }
            ]
          }
        ]
      },
      {
        "name": "IAM",
        "versions": [
          {
            "name": "0.80.0",
            "entities": [
              {
                "name": "User Role",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "User Group",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "User",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "Project",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "OAuth Client",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "Dataset Entity Attribute",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              }
            ]
          }
        ]
      },
      {
        "name": "OAuth Session",
        "versions": [
          {
            "name": "0.1.0",
            "entities": [
              {
                "name": "User",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "OAuth Session",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "OAuth Client",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              }
            ]
          },
          {
            "name": "0.1.1",
            "entities": [
              {
                "name": "User",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "OAuth Session",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "OAuth Keypair",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "OAuth Client",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              }
            ]
          },
          {
            "name": "0.1.2",
            "entities": [
              {
                "name": "User",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "OAuth Session",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "OAuth Keypair",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "OAuth Client",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              }
            ]
          },
          {
            "name": "0.1.3",
            "entities": [
              {
                "name": "User",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "OAuth Session",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "OAuth Keypair",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "OAuth Client",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              }
            ]
          }
        ]
      },
      {
        "name": "Robotics",
        "versions": [
          {
            "name": "0.0.14",
            "entities": [
              {
                "name": "User Group",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "User",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "Service Location",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              }
            ]
          },
          {
            "name": "0.1.0",
            "entities": [
              {
                "name": "User Group",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "User",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "Service Location",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              }
            ]
          },
          {
            "name": "0.1.1",
            "entities": [
              {
                "name": "User Group",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "User",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              },
              {
                "name": "Service Location",
                "attributes": [
                  {
                    "name": "Active",
                    "type": "boolean"
                  }
                ]
              }
            ]
          }
        ]
      }
    ]
  }
}
```


## Search Recursive
