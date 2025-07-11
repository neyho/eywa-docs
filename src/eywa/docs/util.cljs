(ns eywa.docs.util
  (:require
   [clojure.core.async :as async]
   [helix.core :refer [$ defnc]]
   [helix.dom :as d]
   [helix.hooks :as hooks]
   [toddler.ui :as ui]
   [toddler.i18n.time]
   [toddler.core :as toddler]
   [toddler.router :as router]
   [shadow.css :refer [css]]))

(defnc themed-image
  [{:keys [url] :as props}]
  (let [theme (toddler/use-theme)
        real-url (str url "_" theme ".png")]
    (d/img {:src real-url & (dissoc props :url)})))

(defnc mount-image
  [{:keys [url id width themed?]
    :or {width 400
         themed? true}
    :as props}]
  (let [url (router/use-with-base url)]
    ($ toddler/portal
       {:locator #(.getElementById js/document id)}
       ($ ui/row
          {:align :center
           & (select-keys props [:class :className])}
          (if themed?
            ($ themed-image
               {:url url
                :style {:width width}})
            (d/img
             {:src (str url ".png")
              :style {:width width}
              & (dissoc props :url :width :themed? :id)}))))))

(defnc mount-modal-image
  [{:keys [url id width timeout close-url]
    :or {width 600
         timeout 1500}
    :as props}]
  (let [[opened? set-opened!] (hooks/use-state false)
        {ww :width} (toddler/use-window-dimensions)
        width (min width ww)
        initialized? (hooks/use-ref nil)
        now (.now js/Date)
        theme (toddler/use-theme)
        url (str url "_" theme ".png")]
    (hooks/use-effect
      [id]
      (letfn [(open! [] (set-opened! (fn [current] (not current))))
              (locator [] (.getElementById js/document id))]
        (async/go
          (loop []
            (if-some [target (locator)]
              (.addEventListener target "mousedown" open!)
              (when (< (- (.now js/Date) now) timeout)
                (async/<! (async/timeout 30))
                (recur)))))
        (fn []
          (when-some [el (locator)]
            (.removeEventListener el "mousedown" open!)))))
    (when opened?
      ($ toddler/portal
         {:locator #(.getElementById js/document id)}
         ($ ui/modal-dialog
            {:on-close #(set-opened! false)
             :style {:background-color (case theme
                                         "light" "white"
                                         "dark" "black")}}
            (d/div
             {:class (toddler/conj-prop-classes
                      [(css ["& img" :rounded-lg])]
                      props)}
             (d/img
              {:src url
               :style {:width width}})))))))

(defnc tour
  [{:keys [height width images]}]
  (let [ww (toddler/use-window-width)
        [current set-current!] (hooks/use-state 0)
        width (min ww width)
        base (router/use-with-base "")
        navigation-width (max (min 800 width) 400)
        navigation-height 60
        margin-top 10
        slide-height (- height navigation-height margin-top)]
    (letfn [(next []
              (set-current!
               (fn [current]
                 (mod (inc current) (count images)))))
            (prev []
              (set-current!
               (fn [current]
                 (mod (dec current) (count images)))))
            (pick [idx]
              (set-current! idx))]
      (d/div
       {:style {:display "flex"
                :overflow "hidden"
                :align-items "center"
                :width width
                :height height
                :position "relative"
                :transiton "left .3s ease-in-out"}
        :on-click (fn [e]
                    (.stopPropagation e)
                    (.preventDefault e))}
       (d/div
        {:style {:position "absolute"
                 :height slide-height
                 :overflow "hidden"
                 :top margin-top
                 :display "flex"
                 :align-items "center"
                 :transition "left .3s ease-in-out"
                 :left (* current width -1)}}
        (map
         (fn [image]
           ($ ui/row
              {:key image
               :align :center
               :style {:width width
                       :height slide-height}}
              (d/img
               {:src (str base image)
                :style {:max-width "100%" :max-height "100%"
                        :object-fit "contain"
                        :border-radius 10}})))
         images))
       (d/div
        {:style {:position "absolute"
                 :bottom 0
                 :left 0
                 :display "flex"
                 :justify-content "center"
                 :align-items "center"
                 :width width
                 :height navigation-height}}
        (d/div
         {:style {:width navigation-width
                  :display "flex"
                  :align-items "center"
                  :justify-content "space-between"}
          :className (css
                      :bg-normal :rounded-md
                      ["& .left" :pl-2]
                      ["& .right" :pr-2])}
         (d/div
          {:className "left"}
          ($ ui/button {:on-click prev} "Prev"))
         (d/div
          {:className (css
                       "center"
                       :flex
                       :justify-center
                       :items-center)}
          (map
           (fn [idx]
             (d/button
              {:key idx
               :on-click #(pick idx)
               :class [(when (= idx current) "selected")
                       (css :w-2 :h-2 :mx-1 :cursor-pointer :border :border-normal {:border-radius "24px"}
                            ["&.selected" :bg-normal-])]}))
           (range (count images))))
         (d/div
          {:className "right"}
          ($ ui/button {:on-click next} "Next"))))))))
