## Shards
Function `add-shard` is defined in `neyho.eywa.lacinia` namespace. This function
can be used to extend GraphQL schema that is built from Data Models. Input for this
function should be id of shard and content of shard in form of string.

In following example we are adding new mutation for importDataset model. 


#### Extending GraphQL Types
```graphql
type Mutation {
  """Mutation can be used to import dataset through cli clients,
  after completing device flow authorization. Given the authenticated
  user has sufficient privileges"""
  importDataset(dataset: Transit):DatasetVersion
    @resolve(fn: "neyho.eywa.dataset/import-dataset")
    @protect(scopes: ["dataset:deploy"])
}

```
:::info Resolver
**@resolve** declaration will bind function to mutation. When mutation is called 
resolver function will be executed. It is different than **hooks**. You can
think of it as: "Use resolver when not working with generic mutations and queries."
:::

If you run `neyho.eywa.lacinia/add-shard` on graphql schema from above than following
will happen. 

 * importDataset mutation will be merged into GraphQL schema
 * when called through GraphQL interface, function _neyho.eywa.dataset/import-dataset_
 will be called
 * But only if authorized user has _dataset:deploy_ scope obtained during authorization


 This is actual implementation of that is available in eywa-core project. It will just deploy
dataset that it received.

For more information how to use resolvers use [Lacinia](https://lacinia.readthedocs.io/en/latest/resolve/overview.html)
documentation.

```clojure
;; neyho.eywa.dataset
(defn import-dataset
  ([context {dataset :dataset} _]
   (deploy-dataset context {:version dataset} nil)))
```
:::tip Shard are important!
All of examples that are extending EYWA must be loaded through shard.

Functionality that is leveraging or extending deployed model(s)
functionality should be packed in single shard.
Check out [datasets.graphql](https://github.com/neyho/eywa-core/blob/master/resources/datasets.graphql)
:::


#### Extend Dataset Entity
You can extend **any** type. Not just Mutations and Queries. In following
example User entity is extended with additional field that shows if
user is **online** or not.

Value of this field isn't part od Data Model and will not be stored in DB.
Its value is resolved by looking at state of connected atom and returned
from there.

```graphql
type User {
  online: Boolean
  @resolve(fn: "demo.chat.users/is-online")
}
```


```clojure
(def connected (atom nil))

(defn is-online
  "Function extends User entity with online attribute"
  [_ _ {:keys [euuid]}]
  (boolean (get @*connected* :euuid)))
```
:::tip Application state
Not everything should be stored in DB. There will be situations that are
important for application logic only or relevant only in real-time. 

Storing this values in DB isn't recommended. Keep application state local
and resolve value only when asked.

**Also** keep in mind that you can change application state by adding
new _Mutation_ types and resolvers. To round things up!
:::



## HOOKS

Hooks are another way to override default GraphQL mutations and queries. Concept
is simillar to **@resolve** directive. Difference is that hooks will be called
before or after resolver function.

Will it be before or after depends on **metric**. If it is negative than hook
is called before and if it is positive it will be called after.

```graphql
type Mutation {
  deleteDataset:Boolean
  @hook(fn: "neyho.eywa.dataset/prepare-deletion-context" metric: -1)
  @hook(fn: "neyho.eywa.dataset/destroy-linked-versions")
  @protect(scopes: ["dataset:delete"])
}
```

<div id="hooks-visual"></div>

In this example **deleteDataset** generic mutation is extended with hooks. Hook
that is bound to `neyho.eywa.dataset/prepare-deletion-context` has **metric: -1** and
will be executed before dataset is deleted in DB.

Hook will store all dataset versions that are available for that dataset and
store that value in lacinia context.

When resolver is successfully executed it will delete dataset from DB and
next `neyho.eywa.dataset/destroy-linked-versions` hook will be called (metric +1).

This hook will pull value of prepared deletion context from lacinia context
and than delete all model versions that were associated with that dataset.


:::info Hook metric
Think of hook metric as sorting parameter, where 0 is place where resolver
is executed!

I.E.
```text
metric:     [ -4    -1     0        3    10] 
execution:  [ hook hook resolver  hook  hook
```
:::



## Subscriptions
There is not much to say about Subscriptions. Except they are great
and simple to implement. Adding subscription is as simple as implementing
@resolve.



```graphql
type Subscription {
  refreshedGlobalDataset:DatasetVersion
  @resolve(fn: "neyho.eywa.dataset/deploy-update-subscription")
  @protect(scopes: ["dataset:subscription"])
}
```

Resolver function will receive three arguments. First is Lacinia
[context](https://lacinia.readthedocs.io/en/latest/resolve/context.html?highlight=context)
 second one is arguments sent in GraphQL subscription and **third is
async channel that is used to stream values to subscriber**.

```clojure
(defn deploy-update-subscription
  [{:keys [username]} _ upstream]
  (let [sub (async/chan)]
    (async/sub publisher :refreshedGlobalDataset sub)
    (async/go-loop [{:keys [data]
                     :as published} (async/<! sub)]
      (when published
        (log/tracef "Sending update of global model to user %s" username)
        (upstream data)
        (recur (async/<! sub))))
    (upstream
     {:name "Global"
      :model (dataset/get-model *db*)})
    (fn []
      (async/unsub publisher :refreshedGlobalDataset sub)
      (async/close! sub))))
```
