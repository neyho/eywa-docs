#### Extending GraphQL Types
``` clojure
type Mutation {
  importDataset(dataset: Transit):DatasetVersion
  @resolve(fn: "neyho.eywa.dataset/import-dataset")
  @protect(scopes: ["dataset:deploy"])}
```


```clojure
(defn import-dataset
  ([context {dataset :dataset} _]
   (deploy-dataset context {:version dataset} nil)))
```


#### ADDING HOOKS
```graphql
type Mutation {
  deleteDataset:Boolean
  @hook(fn: "neyho.eywa.dataset/prepare-deletion-context" metric: -1)
  @hook(fn: "neyho.eywa.dataset/destroy-linked-versions")
  @protect(scopes: ["dataset:delete"])
}
```

#### Extend Dataset Entity
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


#### Subscriptions


```clojure
type Subscription {
  refreshedGlobalDataset:DatasetVersion
  @resolve(fn: "neyho.eywa.dataset/deploy-update-subscription")
  @protect(scopes: ["dataset:subscription"])
}
```

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


#### FULL MUTATION SCHEMA

```graphql
type Mutation {
  deployDataset(version: DatasetVersionInput):DatasetVersion
  @resolve(fn: "neyho.eywa.dataset/deploy-dataset")
  @protect(scopes: ["dataset:deploy"])

  importDataset(dataset: Transit):DatasetVersion
  @resolve(fn: "neyho.eywa.dataset/import-dataset")
  @protect(scopes: ["dataset:deploy"])


  deleteDataset:Boolean
  @hook(fn: "neyho.eywa.dataset/prepare-deletion-context" metric: -1)
  @hook(fn: "neyho.eywa.dataset/destroy-linked-versions")
  @protect(scopes: ["dataset:delete"])

  initializeEncryptionWithShare(share: SSShareInput): EncryptionResponse
  @resolve(fn: "neyho.eywa.dataset.encryption/init-with-share")

  initializeEncryptionRaw(master: String): EncryptionResponse
  @resolve(fn: "neyho.eywa.dataset.encryption/init-with-master")


  generateShares(shares: Int=5, threshold: Int=3):[SSShare]
  @resolve(fn: "neyho.eywa.dataset.encryption/generate-shares")

  generateMaster:String
  @resolve(fn: "neyho.eywa.dataset.encryption/generate-master")

}
```

#### APPLY GRAPHQL SCHEMA SHARD
```clojure

;; from neyho.eywa.dataset namespace

(defn start
  "Function initializes EYWA datasets by loading last deployed model."
  ([] (start *db*))
  ([db]
   (log/info "Initializing Datasets...")
   (try
     (dataset/reload db {:model (dataset/get-last-deployed db)})
     (lacinia/add-directive :hook wrap-hooks)
     (lacinia/add-shard ::dataset-directives (slurp (io/resource "dataset_directives.graphql")))
     (lacinia/add-shard ::datasets (slurp (io/resource "datasets.graphql")))
     (catch Throwable e (log/errorf e "Couldn't initialize Datasets...")))
   ; (alter-var-root #'*datasets* (fn [_] db))
   (dataset/reload db)
   (binding [dataset/*return-type* :edn]
     (bind-service-user #'neyho.eywa.data/*EYWA*))
   nil))
```


