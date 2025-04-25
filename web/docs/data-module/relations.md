---
sidebar_position: 2
---
# Relations

Definition of https://dictionary.cambridge.org/dictionary/english/relation[relation] is
"`the way in which two people or groups of people feel and behave towards each other`". By replacing
people or groups of people with entity we can than talk about how do entities **feel and behave** with 
each other.

## Types of relations
### One to One

Describes relation where only one entity A exists for one entity B

### One to Many

Describes relation where one entity A can have one or more than one entity B

### Relation Example
<img class="screenshot_img"
  src={require('/img/eywa/user-group-relation.png').default}
  alt="User - User Group relation"
  width="450px"
/>


Lets explain this relation. Relation between _User_ and _User Role_ from perspective of _User_
states that single user can have many _User Role_ records that are referenced by *roles* field.
Other way arround when we look at relation from _User Role_ entity relation states that single _User Role_ record can have multiple _User_ entities referenced by *users* field.

This type of relation is called

### Many to Many

Describes relation where one entity A can have one or more than one entity B, as well as one entity B can have one or more entity A.

### Recursive

<img class="screenshot_img"
  src={require('/img/eywa/recursive.png').default}
  alt="recursive relation"
  width="250px"
/>


On picture above you can find recursive relation. This type of relation is referencing
same entity. Entities with recursive relations represent tree like structures in EYWA. 
