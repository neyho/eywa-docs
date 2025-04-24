(ns eywa-docs.docs.data
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

(defnc Model
  {:wrap [(router/wrap-rendered :eywa.data.model)]}
  []
  (d/div "Hell from model"))

(defnc Data
  {:wrap [(router/wrap-rendered :eywa.data)
          (router/wrap-link
           :eywa.data
           [{:id :eywa.data.entities
             :hash "entities"
             :name "Entities"}
            {:id :eywa.data.relations
             :hash "relations"
             :name "Relations"}
            {:id :eywa.data.model
             :segment "model"
             :name "Model"
             :children [{:id :eywa.data.model.save
                         :name "Save"
                         :hash "save"}
                        {:id :eywa.data.model.load
                         :name "Load"
                         :hash "load"}
                        {:id :eywa.data.model.deploy
                         :name "Deploy"
                         :hash "deploy"}]}])]}
  []
  (let [{:keys [width height]} (layout/use-container-dimensions)
        model? (router/use-rendered? :eywa.data.model)]
    ($ ui/simplebar
       {:style {:height height}}
       ($ ui/row {:align :center}
          ($ ui/row
             {:style {:max-width (min width 800)}}
             (cond
               model? ($ Model)
               :else ($ md/watch-url {:url "/docs/data.md"})))))))
