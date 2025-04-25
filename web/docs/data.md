## Entities

Definition of [entity](https://dictionary.cambridge.org/dictionary/english/entity) is 
`a thing with distinct and independent existence`. That said
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
that will be accessible as primary key. If composed primariy key is required it can be emulated by
marking multiple fields with uniqueue constraint, and afterwards grouped in constraints tab for that entity
:::

<div class="image-wrapper">
  <img class="screenshot_img"
  src="/img/eywa/entity.png"
  alt="Entity example"
  width="250px"/>
</div>

#### Supported data types

* **Integer**
* **Float**
* **String**
* **Boolean**
* **timestamp**
* **uuid**
* **transit**
* **json**
* **enum**
* **encrypted**
* **hashed**



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


#### Relation Example
<div class="image-wrapper">
  <img class="screenshot_img"
    src="/img/eywa/user-group-relation.png"  alt="User - User Group relation"
    width="450px"/>
</div>


Lets explain this relation. Relation between _User_ and _User Role_ from perspective of _User_
states that single user can have many _User Role_ records that are referenced by *roles* field.
Other way arround when we look at relation from _User Role_ entity relation states that single _User Role_ record can have multiple _User_ entities referenced by *users* field.

### Recursive

<div class="image-wrapper">
  <img class="screenshot_img"
    src="/img/eywa/recursive.png"
    alt="recursive relation"
    width="250px"
  />
</div>


On picture above you can find recursive relation. This type of relation is referencing
same entity. Entities with recursive relations represent tree like structures in EYWA. 
