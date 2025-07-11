(ns eywa.docs.overview
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

(defnc Overview
  {:wrap [(router/wrap-rendered :eywa.overview)
          (router/wrap-link
           :eywa.overview
           [{:id :eywa.overview.why
             :name "Why?"
             :hash "why?"}
            {:id :eywa.overview.quickstart
             :name "Quickstart"
             :hash "quickstart"}])]}
  []
  (let [{:keys [width height]} (layout/use-container-dimensions)
        docs-url (router/use-with-base "/overview.md")]
    ($ ui/simplebar
       {:style {:height height}}
       ($ ui/row {:align :center}
          ($ ui/column
             {:style {:max-width (min width 800)}}
             ($ md/watch-url {:url docs-url})
             ($ toddler/portal
                {:locator #(.getElementById js/document "in-short")}
                ($ ui/row
                   {:align :center}
                   ($ ui/button
                      {:id "in-short-visual"}
                      "Click to see how?")))
             ($ util/mount-modal-image
                {:id "in-short-visual"
                 :url "overview/in_short"
                 :className (css :p-6)
                 :width 600})
             ($ toddler/portal
                {:locator #(.getElementById js/document "in-short")}
                #_($ util/themed-image {:url "overview/in_short"})))))))
