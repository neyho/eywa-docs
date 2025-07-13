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
        {:keys [show]} (router/use-query)
        max-width (min ww 500)]
    ($ ui/row
       {:style {:max-width max-width}
        :align :center}

       ($ md/watch-url
          {:url "/docs/graphql/queries.md"
           :style {:max-width max-width}})
       ($ toddler/portal
          {:locator #(.getElementById js/document "open-get-anchor")}
          ($ ui/row
             {:align :center}
             ($ ui/button
                {:id "open-get"}
                "Visual")))
       ($ toddler/portal
          {:locator #(.getElementById js/document "open-search-anchor")}
          ($ ui/row
             {:align :center}
             ($ ui/button
                {:id "open-search"}
                "Visual")))
       ($ util/mount-modal-image
          {:id "open-get"
           :url "/docs/graphql/queries/get"
           :className (css :p-6)
           :width 400})
       ($ util/mount-modal-image
          {:id "open-search"
           :url "/docs/graphql/queries/search"
           :className (css :p-6)
           :width 400})
       ($ util/mount-modal-image
          {:id "open-datasets-model"
           :url "/docs/graphql/queries/datasets_model"}))))

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
        img-width (min ww 360)
        max-width (min ww 500)]
    ($ ui/row
       {:style {:max-width max-width}}
       ($ ui/column
          ($ md/watch-url
             {:url "/docs/graphql/mutations.md"
              :style {:max-width max-width}})
          ($ util/mount-image
             {:id "sync-mutation"
              :width img-width
              :url "/docs/graphql/mutations/sync"})
          ($ util/mount-image
             {:id "stack-mutation"
              :width img-width
              :url "/docs/graphql/mutations/stack"})
          ($ util/mount-image
             {:id "slice-entities"
              :width (min ww 440)
              :className (css :my-4)
              :url "/docs/graphql/mutations/slice_entities"})
          ($ util/mount-image
             {:id "slice-mutation"
              :width img-width
              :className (css :my-4)
              :url "/docs/graphql/mutations/slice"})
          ($ util/mount-image
             {:id "purge-mutation"
              :width img-width
              :className (css :my-4)
              :url "/docs/graphql/mutations/purge"})))))

(defnc Extend
  {:wrap [(router/wrap-rendered :eywa.graphql.extend)
          (router/wrap-link
           :eywa.graphql.extend
           [{:id :eywa.graphql.extend.shards
             :name "Shards"
             :hash "shards"}
            {:id :eywa.graphql.extend.hooks
             :name "Hooks"
             :hash "hooks"}
            {:id :eywa.graphql.extend.subscriptions
             :name "Subscriptions"
             :hash "subscriptions"}])]}
  []
  (let [ww (toddler/use-window-width)
        max-width (min ww 500)]
    ($ ui/row
       {:style {:max-width max-width}
        :align :center}
       ($ toddler/portal
          {:locator #(.getElementById js/document "hooks-visual")}
          ($ ui/row
             {:align :center}
             ($ ui/button
                {:id "open-hooks-visual"}
                "Visual")))
       ($ util/mount-modal-image
          {:id "open-hooks-visual"
           :url "docs/graphql/extend/hooks"
           :className (css :p-6)
           :width 400})
       ($ md/watch-url
          {:url "/docs/graphql/extend.md"
           :max-width max-width}))))

(defnc InGeneral
  {:wrap [(router/wrap-rendered :eywa.graphql true)]}
  []
  (let [ww (toddler/use-window-width)
        max-width (min ww 500)]
    ($ ui/row
       {:style {:max-width max-width}
        :align :center}
       ($ md/watch-url
          {:url "/docs/graphql.md"
           :style {:max-width max-width}}))))

(defnc GraphQL
  {:wrap [(router/wrap-rendered :eywa.graphql)
          (router/wrap-link
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
          ($ Extend)
          #_($ ui/row
               {:style {:max-width (min width 800)}})))))
