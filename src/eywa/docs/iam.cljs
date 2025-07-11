(ns eywa.docs.iam
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

(defnc IAM
  {:wrap [(router/wrap-rendered :eywa.iam)
          #_(router/wrap-link
             :eywa.iam
             [{:id :eywa.iam.entities
               :hash "entities"
               :name "Entities"}])]}
  []
  (let [{:keys [width height]} (layout/use-container-dimensions)
        model? (router/use-rendered? :eywa.data.model)
        open-tour (router/use-go-to :eywa.data.tour)
        max-width (min width 600)]
    ($ ui/simplebar
       {:style {:height height}}
       ($ ui/row {:align :center}
          ($ ui/row
             {:style {:max-width max-width}}
             ($ md/watch-url
                {:url (router/use-with-base "/iam.md")
                 :style {:max-width max-width}}))))))
