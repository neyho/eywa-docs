(ns eywa.docs.data
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

(defnc Tour
  {:wrap [(router/wrap-rendered :eywa.data.tour)]}
  []
  (let [back (router/use-go-to :eywa.data)
        {ww :width
         wh :height} (toddler/use-window-dimensions)
        width (min (- ww 60) 1400)
        height (min (- wh 60) 900)]
    ($ ui/modal-dialog
       {:on-close #(back)
        :className (css :bg-transparent :border-0)}
       ($ util/tour
          {:width width
           :height height
           :images ["/data/tour/1.png"
                    "/data/tour/2.png"
                    "/data/tour/3.png"
                    "/data/tour/4.png"
                    "/data/tour/5.png"
                    "/data/tour/6.png"
                    "/data/tour/7.png"]}))))

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
            {:id :eywa.data.tour
             :segment "tour"}])]}
  []
  (let [{:keys [width height]} (layout/use-container-dimensions)
        model? (router/use-rendered? :eywa.data.model)
        open-tour (router/use-go-to :eywa.data.tour)
        $image nil #_(css ["& img" :border :border-normal :rounded-xl])
        max-width (min width 600)]
    ($ ui/simplebar
       {:style {:height height}}
       ($ ui/row {:align :center}
          ($ ui/row
             {:style {:max-width max-width}}
             ($ toddler/portal
                {:locator #(.getElementById js/document "tour")}
                ($ ui/row
                   {:align :center
                    :style {:margin-top 20}}
                   ($ ui/button
                      {:on-click #(open-tour)}
                      "Data Modeling Tour")))
             ($ Tour)
             ($ util/mount-image
                {:id "entity"
                 :url "/data/entity"
                 :className $image})
             ($ util/mount-image
                {:id "relation"
                 :url "/data/relation"
                 :className $image})
             ($ util/mount-image
                {:id "recursive-relation"
                 :url "/data/recursive_relation"
                 :className $image})
             ($ md/watch-url
                {:url (router/use-with-base "/data.md")
                 :style {:max-width max-width}}))))))
