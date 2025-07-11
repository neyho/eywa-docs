## Data Modeling that Thinks Ahead

Data modeling is the backbone of every scalable system—but in fast-paced environments,
traditional modeling approaches fall short. **Data Modeling module** was built to turn
complex, fragile, and isolated models into a unified, modular, and maintainable asset
across your architecture.

<div id="tour"></div>

### 🔄 Exportable, Transportable, Deployable Data Models  
Model your data once, and take it anywhere. EYWA allows you to define your schema 
through a clean, intuitive UI and **export your model for use across environments**—from 
development to production, or between distributed instances. 
Your models aren’t locked into one system—they're **portable by design**.

### 📌 Built-In Versioning  
EYWA keeps track of every change to your model, so you don’t have to. 
With **versioning**, you’ll always know what’s changing, what’s deployed,
and what effect a version will have on your global dataset. Migrations
become predictable, auditable, and controlled.

### 🧠 Designed for Real-World Complexity  
Data modeling often becomes a bottleneck as systems grow. EYWA is built
to **live with complexity**, not avoid it. With modular structures,
composable entities, and centralized access control, you can delegate
cognitive overhead to the system. EYWA handles the details so your team
can focus on the domain.

### 🔧 Maintenance Without Meltdown  
Need to refactor a schema? Update a relationship? Roll out new entities?
EYWA simplifies model maintenance by **isolating changes**, allowing teams
to test, preview, and deploy updates with confidence. The system provides
**safe boundaries and clear impact mapping**, so nothing breaks by accident.

### 🧩 Modular by Nature  
Large systems need to be **built from reusable, modular parts**. EYWA
embraces this principle, allowing teams to break down their data models
into domain-specific modules. Share models across teams, plug into
existing schemas, or refactor without touching the core.

:::note Grow
**Data Modeling** gives teams the freedom to **design for growth**,
not just survival. Whether you’re building microservices,
evolving legacy systems, or setting up a data-first product
from scratch—EYWA makes your data model a strategic asset,
not a liability.
:::




## Entities

Definition of [entity](https://dictionary.cambridge.org/dictionary/english/entity) is 
_"a thing with distinct and independent existence"_. That said
entity in Datasets is a collection of attributes that uniquely define some thing. Attributes
are defined by attribute **name**, **constraint** and **type**.

Attribute name is reference to storage service under what _name_ to store/search some value.
Attribute constratint defines if this attribute is **optional**, **mandatory** or **unique**. 

Unique attribute creates constraint on
dataset. Rule for this constraint is that in collection of all records for given entity 
only one record exists for combination of unique attribute values. Entities that have one
or more unique attributes are called *STRONG* entities. Entities without unique attributes
are *WEAK* entities. For WEAK entities it is possible store/search multiple entity records
with same attribute values.

Beside constraint, and attribute name, attribute requires value *type* to be defined so that
EYWA data storage service will know what type of data is it storing and which search options
are available for given type.



:::tip TIP:
Don't create Primary key fields, EYWA will generate special EUUID field for each entity
that will be accessible as primary key. If composed primary key is required it can be emulated by
marking multiple fields with uniqueue constraint, and afterwards grouped in constraints tab for that entity
:::

<div id="entity"></div>

#### Supported data types

* **Integer**
* **Float**
* **String**
* **Boolean**
* **Timestamp**
* **UUID**
* **Transit**
* **JSON**
* **Enum**
* **Encrypted**
* **Hashed**



## Relations

Definition of [relation](https://dictionary.cambridge.org/dictionary/english/relation) is
_"the way in which two people or groups of people feel and behave towards each other"_. By replacing
people or groups of people with entity we can than talk about **how do entities feel and behave with
each other**.

#### One to One

Describes relation where only one entity A exists for one entity B

#### One to Many

Describes relation where one entity A can have one or more than one entity B

#### Many to Many

Describes relation where one entity A can have one or more than one entity B, as well as one entity B can have one or more entity A.


## Relation Example
<div id="relation"></div>


Lets explain this relation. Relation between _User_ and _User Group_ from perspective of _User_
states that single user can have many _User Group_ records that are referenced by *groups* field.
Other way arround when we look at relation from _User Group entity relation states that single 
_User Group_ record can have multiple _User_ entities referenced by *users* field.

## Recursive

<div id="recursive-relation"></div>

On picture above you can find recursive relation. This type of relation is referencing
same entity. Entities with recursive relations represent tree like structures in EYWA. 
