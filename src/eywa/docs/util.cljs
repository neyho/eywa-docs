(ns eywa.docs.util
  (:require
   [clojure.core.async :as async]
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

(defnc themed-image
  [{:keys [url] :as props}]
  (let [theme (toddler/use-theme)
        real-url (str url "_" theme ".png")]
    (d/img {:src real-url & (dissoc props :url)})))

(defnc mount-image
  [{:keys [url id width]
    :or {width 400}
    :as props}]
  ($ toddler/portal
     {:locator #(.getElementById js/document id)}
     ($ ui/row
        {:align :center
         & (select-keys props [:class :className])}
        ($ themed-image
           {:url (router/use-with-base url)
            :style {:width width}}))))

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
            {:on-close #(set-opened! false)}
            (d/div
             {:class (toddler/conj-prop-classes
                      [(css ["& img" :rounded-lg])]
                      props)}
             (d/img
              {:src url
               :style {:width width}})))))))
