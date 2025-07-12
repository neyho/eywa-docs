(ns eywa.main
  {:shadow.css/include
   ["eywa/main.css"]}
  (:require
   ["react-dom/client" :refer [createRoot]]
   [goog.string :refer [format]]
   [shadow.css :refer [css]]
   [helix.core :refer [$ defnc provider <>]]
   [helix.dom :as d]
   [helix.hooks :as hooks]
   [toddler.app :as app]
   [toddler.ui :as ui]
   [toddler.i18n.time]
   [toddler.core :as toddler]
   [toddler.popup :as popup]
   [toddler.layout :as layout]
   [toddler.md.lazy :as md]
   [toddler.ui.components :refer [components]]
   [toddler.ui.css :as ui.css]
   [toddler.notifications :as notifications]
   [toddler.material.outlined :as outlined]
   [toddler.fav6.brands :as brands]
   [toddler.window :refer [wrap-window-provider]]
   [toddler.router :as router]
   [eywa.docs.lazy :refer [Docs]]))

(defonce root (atom nil))

(def goku "https://giphy.com/embed/fmMdxlVwsCmTtA4V6a")

(defnc GiphyEmbed [{:keys [src width height]}]
  (d/div
   {:dangerouslySetInnerHTML
    #js {:__html (format
                  "<iframe src=\"%s\" width=\"%s\" height=\"%s\" style=\" \" frameBorder=\"0\" class=\"giphy-embed\" allowFullScreen></iframe><p><a href=\"https://giphy.com/gifs/studiosoriginals-2yLNN4wTy7Zr8JSXHB\"></a></p>"
                  src width height)}}))

(goog-define ROUTER_BASE "")

(defnc actions
  {:wrap [(ui/forward-ref)]}
  [_ _ref]
  (let [open-docs (router/use-go-to ::docs)
        theme (toddler/use-theme)
        on-theme-change (toddler/use-theme-change)]
    (d/div
     {:className (css :flex
                      :items-center
                      :h-10
                      :flex-row-reverse
                      :pr-1
                      :box-border
                      ["& .wrapper"
                       :items-center :flex :items-center {:font-size "24px"} :mr-4
                       :cursor-pointer :color-inactive]
                      ["& .wrapper:first-child" :mr-0]
                      ["& .tooltip-popup-area:hover" :color-normal])}
     (d/div
      {:className "wrapper"}
      ($ ui/tooltip {:message "Change theme"}
         (d/div
          {:on-click (fn []
                       (on-theme-change
                        (case theme
                          ("dark" 'dark) "light"
                          ("light" 'light) "dark"
                          "light")))}
          (if (= "dark" theme)
            ($ outlined/light-mode)
            ($ outlined/dark-mode)))))
     (d/div
      {:className "wrapper"}
      ($ ui/tooltip {:message "API Docs"}
         ($ outlined/info
            {:on-click #(open-docs)}))))))

(defnc GreetingText
  []
  (d/div
   {:className ui.css/$md
    :style {:margin-bottom 24}}
   (d/p "Template project setup complete! You're ready to code.")
   (d/p "Check out the "
        (d/a {:href "https://gersak.github.io/toddler/rationale"} "SHOWCASE")
        " page to see what Toddler can do.")
   (d/hr)
   (d/section
    {:id "document"}
    (d/h4 "Document App")
    (d/p "If you wan't to see how to document your app click "
         (d/a {:href "/docs"} "here") "."))
   (d/hr)
   (d/section
    {:id "theme"}
    (d/h4 "Theme")
    (d/p "If you wan't to style this project, open "
         (d/code "eywa_docs/main.css")
         " file and change variables!"))))

(defnc Greeting
  {:wrap [(router/wrap-rendered ::greeting)
          (router/wrap-link
           ::router/ROOT
           [{:id ::greeting
             :segment "greeting"
             :landing 5}])]}
  []
  (let [{window-width :width
         window-height :height} (toddler/use-window-dimensions)
        [_greeting {greeting-height :height greeting-width :width}] (toddler/use-dimensions)
        scale 0.7
        layout (toddler/use-layout)]
    (case layout
      :mobile
      ($ ui/column
         {:align :center}
         ($ ui/row
            {:align :explode
             :className (css :pl-4 :pr-2 {:height "50px"})}
            (d/h1 "Toddler")
            ($ actions))
         ($ ui/simplebar
            {:style {:width window-width
                     :height (- window-height 80)}}
            ($ ui/row
               {:align :center
                :className (css
                            ["& .message" {:max-width "300px"}]
                            ["& hr" :mt-6 :mb-2]
                            ["& h4" {:margin-top "8px !important"}]
                            ["& h1" :mb-6 {:font-size "32px"}]
                            ["& p" :mt-2 {:font-size "12px"}]
                            ["& a" {:color "var(--link-color)" :font-weight "600"}]
                            ["& .toddler-markdown" :my-6 #_{:margin-bottom "8px !important"}])}
               (d/div
                {:className "message"}
                ($ GreetingText)))
            ($ ui/row
               {:align :center}
               (d/div
                {:className (css :border :border-normal :rounded-lg :overflow-hidden)}
                ($ GiphyEmbed {:src goku
                               :width (* 265 scale)
                               :height (* scale 200)})))))
      ;;
      ($ ui/row
         {:align :center
          :className (css :items-center :w-full :h-full)}
         (d/div
          {:ref #(reset! _greeting %)
           :style {:width (min window-width 400)
                   :height (min (- window-height 100) 650)}
           :className (css :border :border-normal
                           :rounded-lg :relative :bg-normal+)}
          ($ ui/row
             {:align :explode
              :className (css :pt-8 :px-8 :pb-2)}
             (d/h1 "Toddler")
             ($ actions))
          ($ ui/simplebar
             {:style {:width greeting-width
                      :height (- greeting-height 88)}}
             ($ ui/row
                {:align :center
                 :className (css
                             ["& .message" {:max-width "300px"}]
                             ["& hr" :mt-6 :mb-2]
                             ["& h4" {:margin-top "8px !important"}]
                             ["& h1" :mb-6 {:font-size "32px"}]
                             ["& p" :mt-2 {:font-size "12px"}]
                             ["& a" {:color "var(--link-color)" :font-weight "600"}]
                             ["& .toddler-markdown" :my-6 #_{:margin-bottom "8px !important"}])}
                (d/div
                 {:className "message"}
                 ($ GreetingText)))
             ($ ui/row
                {:align :center}
                (d/div
                 {:className (css :border :border-normal :rounded-lg :overflow-hidden :mb-8)}
                 ($ GiphyEmbed {:src goku
                                :width (* 265 scale)
                                :height (* scale 200)})))))))))

(defnc App
  {:wrap [;; Wraps landing page at root URL. It takes into
          ;; account ROUTER_BASE so don't duplicate that value
          ;; Landing will redirect if exact target URL is loaded
          ;; in browser ("/") or if browser is loading URL that
          ;; is not available in ROUTING tree
          (router/wrap-landing "/" false)
          ;; Add theme context. Theme is cached in browser
          ;; localstorage and when application is mounted it
          ;; will be initialized by that value. In this case
          ;; under ::theme key 
          (toddler/wrap-theme ::theme)
          ;; Add locale context. Locale is cached in browser
          ;; localstorage and when application is mounted it
          ;; will be initialized by that value. In this case
          ;; under ::locale key 
          (toddler/wrap-locale ::locale)
          ;; Add notifications store component to DOM
          ;; Toddler will use store component to render
          ;; notifications
          (notifications/wrap-store {:class ui.css/$store})
          ;; Add popup container to DOM so toddler
          ;; can use portal to mount popup components
          (popup/wrap-container)
          ;; Window provider will track window resizing events
          ;; and provide window size value through 
          ;; toddler.app/window context
          (wrap-window-provider)
          ;; This will wrap default toddler components and
          ;; provide those components as toddler.ui/__commponents__
          ;; context
          (ui/wrap-ui (assoc components :markdown md/show))
          ;; Wraps toddler router relative to ROUTER_BASE
          ;; If you are serving assets at some URL different
          ;; from "/" than you should specify ROUTER_BASE
          ;; I.E. for https://meetup.com/toddler.demo/
          ;; ROUTER_BASE = 'toddler.demo'
          (router/wrap-router ROUTER_BASE)]}
  []
  (let [app-mobile (toddler/use-window-width-test < 600)
        docs-mobile (toddler/use-window-width-test < 1000)]
    (toddler/use-mouse-tracker)
    (<>
     (provider
      {:context app/layout
       :value (if app-mobile :mobile :desktop)}
      ($ Greeting))
     (provider
      {:context app/layout
       :value (if docs-mobile :mobile :desktop)}
      ($ Docs)))))

(defn ^:dev/after-load start! []
  (let [target ^js (.getElementById js/document "app")]
    (when-not @root
      (reset! root ^js (createRoot target)))
    (.render ^js @root ($ App))))
