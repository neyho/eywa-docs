## Sync

Sync mutation is created for every entity that is deployed. When called
sync takes input data and **stores it in DB exactly the way it was sent**.

In following example you can see that **John Wayne** lives in Wild West and has
three friends: Chuck Norris, Steven Seagal and Jackie Chan.
John decided to move White House and he stated that his friends are
currently: Chuck Norris, Steven Seagal and Bruce Lee.

Because John has **synced** changes in his life he now has 3 friends and Jackie
Chan is no longer one of them.

:::warning Think of relations
Be aware that when entity has one to many or many to many
relations and input value is "syncing" it will slice all relations that are
not present in sent data.
:::


<div id="sync-mutation"></div>


## Stack
Stack mutation is created for every entity that is deployed. When called
stack takes input data and **stores in DB what was provided**. Unlike
[sync](#sync) it won't slice anything.

In following example you can see that **John Wayne** lives in Wild West
and has four frieds. He decides to move to White House and invites two
of his friends. **stack** mutation will store changes in DB. So now
John, Steven and Chuck are all in White House.

John is still friends with Bruce and Jackie.


:::tip Set "nil" explicitly
**sync** and **stack** mutations work only on provided data. Omited 
attributes won't be synced or stacked so their values won't change.

If you wan't to clear some attribute than you have to explicitly set **nil**
value for that attribute. Same goes for relations
:::
<div id="stack-mutation"></div>

## Slice
Slice mutation is usefull when you want to remove multiple relations between
multiple entites. Think of it this way. Lets say that we have four entities
with many to many relations.

<div id="slice-entities"></div>

Person works in company and is part of several *groups*, has access to
systems through associated *roles*. Person also works on several *projects*.


Lets say that persons situation changed. It might be because of maternity leave
or promotion or something else. Now we need to apply those changes in DB. So we
use **slice** mutation where we specify conditions that will find all DB records
for Person. Next we need to specify what relations are going to be sliced.

Put constraints on related entities (Group, Role, Project) so that for every found
person proper relations are sliced in one single query. 


<div id="slice-mutation"></div>

:::info Why slice?
You could use [sync](./#sync) as mentioned above, but you would need to call it multiple
times. With slice this is possible in one single GraphQL mutation. 

## Purge
Purge is like search and destroy. It works exactly like search except it will
remove every record that it finds. Both relation records and entity records.

<div id="purge-mutation"></div>

:::danger Avoid using
Use purge only if you are sure what you are doing. Don't operate brain with
a hammer. Be expressive and specific when using purge. Be gentle, use scalpel.

Or... Consider alternative, like **delete** mutation.
