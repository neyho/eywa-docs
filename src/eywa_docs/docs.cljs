(ns eywa-docs.docs
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
   [eywa-docs.docs.graphql :refer [GraphQL]]
   [eywa-docs.docs.data :refer [Data]]
   [shadow.css :refer [css]]))

(defnc Intro
  {:wrap [(router/wrap-rendered :eywa.intro)]}
  []
  (let [{:keys [width height]} (layout/use-container-dimensions)]
    ($ ui/simplebar
       {:style {:height height}}
       ($ ui/row {:align :center}
          ($ ui/row
             {:style {:max-width (min width 800)}}
             ($ md/watch-url {:url "/docs/intro.md"}))))))

(def components
  [{:id :eywa.intro
    :landing 100
    :max-width 800
    :url "/docs/intro.md"
    :segment "intro"
    :name "Intro"}
   {:id :eywa.graphql
    :segment "graphql"
    :name "GraphQL"
    :render GraphQL}
   {:id :eywa.data
    :segment "data"
    :name "Data"
    :render Data}])

(defnc actions
  []
  (let [{go-to :go} (router/use-navigate)
        on-theme-change (toddler/use-theme-change)
        theme (toddler/use-theme)
        index (hooks/use-context search/-index-)]
    (<>
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
      ($ ui/tooltip {:message "Back to app"}
         (d/div
          {:on-click (fn [] (go-to "/"))}
          ($ outlined/arrow-back))))
     (when (not-empty index)
       (d/div
        {:className "wrapper"}
        ($ docs/search))))))

(defnc logo
  []
  (let [theme (hooks/use-context app/theme)
        logo (str "https://raw.githubusercontent.com/gersak/toddler/refs/heads/main/template/assets/your_logo_" theme ".png")
        desktop (css :flex :grow :justify-center :items-center {:min-height "100px"})
        layout (toddler/use-layout)
        mobile (css :ml-2 :flex :items-center {:max-height "16px"})]
    (d/div
     {:class (case layout
               :mobile mobile
               :desktop desktop
               nil)}
     (d/img
      {:src logo
       :className (css {:max-height "16px"})}))))

(defnc _Docs
  []
  (router/use-link ::router/ROOT components)
  ($ docs/page
     {:max-width 1200
      :className (css
                  ["& .container-block" :my-8 :p-4 :border-normal
                   :text-xxs :font-medium
                   :rounded-xl]
                  ["& .container-block .icon" :color]
                  ["& .container-block-title" :mt-0 :font-semibold :flex :items-center]
                  ["& .container-block.tip"
                   {:background-color "var(--background-positive)"}]
                  ["& .container-block.note, & .container-block.info"
                   :bg-normal+]
                  ["& .container-block.warning"
                   {:background-color "var(--background-warn)"
                    :border-color "var(--border-negative)"}]
                  ["& .container-block.danger"
                   {:background-color "var(--background-negative)"
                    :border-color "var(--border-negative)"}]
                  ["& .image-wrapper" :flex :justify-center]
                  ["& .image-wrapper img" :border :border-normal :my-4 :rounded-lg])
      :components components
      :render/logo logo
      :render/actions actions}))
