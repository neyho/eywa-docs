(ns eywa.docs.graphql
  (:require
   [helix.core :refer [$ defnc <>]]
   [helix.dom :as d]
   [helix.hooks :as hooks]
   [toddler.app :as app]
   [toddler.ui :as ui]
   [toddler.layout :as layout]
   [toddler.i18n.time]
   [toddler.md.lazy :as md]
   [toddler.docs :as docs]
   [toddler.core :as toddler]
   [toddler.material.outlined :as outlined]
   [toddler.router :as router]
   [toddler.search :as search]
   [toddler.md.lazy :as lazy]
   [eywa.docs.util :as util]
   [shadow.css :refer [css]]))

(defnc Queries
  {:wrap [(router/wrap-rendered :eywa.graphql.queries)
          (router/wrap-link
           :eywa.graphql.queries
           [{:id :eywa.graphql.queries.operators
             :name "Operators"
             :hash "operators"}
            {:id :eywa.graphql.queries.get
             :name "Get"
             :hash "get"}
            {:id :eywa.graphql.queries.search
             :name "Search"
             :hash "search"}
            {:id :eywa.graphql.queries.controling-order
             :name "Controling Order"
             :hash "controling-order"}
            {:id :eywa.graphql.queries.focused-query
             :name "Focused Query"
             :hash "focused-query"}
            {:id :eywa.graphql.queries.search-tree
             :name "Search Recursive"
             :hash "search-recursive"}])]}
  []
  (let [{ww :width} (toddler/use-window-dimensions)
        {:keys [hash]} (router/use-location)
        {:keys [show]} (router/use-query)]
    ($ ui/row
       {:style {:max-width (min ww 500)}}
       ($ util/mount-modal-image
          {:id "open-get"
           :url "queries/get"
           :className (css :p-6)
           :width 400})
       ($ util/mount-modal-image
          {:id "open-search"
           :url "queries/search"
           :className (css :p-6)
           :width 400})
       ($ util/mount-modal-image
          {:id "open-datasets-model"
           :url "queries/datasets_model"})
       ($ md/watch-url {:url (router/use-with-base "/graphql/queries.md")}))))

(defnc Mutations
  {:wrap [(router/wrap-rendered :eywa.graphql.mutations)
          (router/wrap-link
           :eywa.graphql.mutations
           [{:id :eywa.graphql.mutations.sync
             :name "Sync"
             :hash "sync"}
            {:id :eywa.graphql.mutations.stack
             :name "Stack"
             :hash "stack"}
            {:id :eywa.graphql.mutations.delete
             :name "Slice"
             :hash "slice"}
            {:id :eywa.graphql.mutations.purge
             :name "Purge"
             :hash "purge"}])]}
  []
  (let [ww (toddler/use-window-width)
        img-width (min ww 360)]
    ($ ui/row
       {:style {:max-width (min ww 500)}}
       ($ ui/column
          ($ md/watch-url {:url (router/use-with-base "/graphql/mutations.md")})
          ($ util/mount-image
             {:id "sync-mutation"
              :width img-width
              :url "/graphql/mutations/sync"})
          ($ util/mount-image
             {:id "stack-mutation"
              :width img-width
              :url "/graphql/mutations/stack"})
          ($ util/mount-image
             {:id "slice-entities"
              :width (min ww 440)
              :className (css :my-4)
              :url "/graphql/mutations/slice_entities"})
          ($ util/mount-image
             {:id "slice-mutation"
              :width img-width
              :className (css :my-4)
              :url "/graphql/mutations/slice"})
          ($ util/mount-image
             {:id "purge-mutation"
              :width img-width
              :className (css :my-4)
              :url "/graphql/mutations/purge"})))))

(defnc Extend
  {:wrap [(router/wrap-rendered :eywa.graphql.extend)
          (router/wrap-link
           :eywa.graphql.extend
           [{:id :eywa.graphql.extend.types
             :name "Types"
             :hash "types"}
            {:id :eywa.graphql.extend.hooks
             :name "HOOKS"
             :hash "hooks"}
            {:id :eywa.graphql.extend.subscriptions
             :name "Subscriptions"
             :hash "subscriptions"}])]}
  []
  ($ md/watch-url {:url (router/use-with-base "/graphql/extend.md")}))

(defnc InGeneral
  {:wrap [(router/wrap-rendered :eywa.graphql true)]}
  []
  ($ md/watch-url {:url (router/use-with-base "/graphql.md")}))

(defnc GraphQL
  {:wrap [(router/wrap-link
           :eywa.graphql
           [{:id :eywa.graphql.queries
             :segment "queries"
             :name "Queries"}
            {:id :eywa.graphql.mutations
             :name "Mutations"
             :segment "mutations"}
            {:id :eywa.graphql.extend
             :name "Extend"
             :segment "extend"}])]}
  []
  (let [{:keys [width height]} (layout/use-container-dimensions)]
    ($ ui/simplebar
       {:style {:height height}}
       ($ ui/row {:align :center}
          ($ InGeneral)
          ($ Queries)
          ($ Mutations)
          #_($ ui/row
               {:style {:max-width (min width 800)}})))))
