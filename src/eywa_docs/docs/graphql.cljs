(ns eywa-docs.docs.graphql
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
   [shadow.css :refer [css]]))

(defnc GraphQL
  {:wrap [(router/wrap-rendered :eywa.graphql)
          (router/wrap-link
           :eywa.graphql
           [{:id :eywa.graphql.queries
             :segment "queries"
             :name "Queries"
             :children [{:id :eywa.graphql.queries.get
                         :name "Get"
                         :hash "get"}
                        {:id :eywa.graphql.queries.search
                         :name "Search"
                         :hash "search"}
                        {:id :eywa.graphql.queries.search-tree
                         :name "Search Recursive"
                         :hash "search-recursive"}
                        {:id :eywa.graphql.queries.aggregate
                         :name "Aggregate"
                         :hash "search-aggregate"}]}
            {:id :eywa.graphql.mutations
             :name "Mutations"
             :segment "mutations"
             :children [{:id :eywa.graphql.mutations.sync
                         :name "Sync"
                         :hash "sync"}
                        {:id :eywa.graphql.mutations.stack
                         :name "Stack"
                         :hash "stack"}
                        {:id :eywa.graphql.mutations.delete
                         :name "Delete"
                         :hash "delete"}
                        {:id :eywa.graphql.mutations.purge
                         :name "Purge"
                         :hash "purge"}]}])]}
  []
  (let [{:keys [width height]} (layout/use-container-dimensions)]
    ($ ui/simplebar
       {:style {:height height}}
       ($ ui/row {:align :center}
          ($ ui/row
             {:style {:max-width (min width 800)}}
             ($ md/watch-url {:url "/docs/graphql.md"}))))))
