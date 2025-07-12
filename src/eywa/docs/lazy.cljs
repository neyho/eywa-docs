(ns eywa.docs.lazy
  (:require
   [helix.core :refer [$ defnc]]
   [helix.hooks :as hooks]
   [toddler.head :as head]
   [toddler.router :as router]
   [toddler.lazy :as lazy]
   [toddler.ui.css :as ui.css]
   [toddler.search :as search]
   [toddler.md.lazy :as md]))

(lazy/load-components
 ::_Docs eywa.docs/_Docs)

(defn change-highlight-js
  [theme]
  (let [dark-url "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.11.1/styles/base16/tomorrow-night.min.css"
        light-url "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.11.1/styles/base16/atelier-lakeside-light.min.css"]
    (letfn [(fetch-dark []
              (head/remove
               :link
               {:href light-url
                :rel "stylesheet"})
              (head/add
               :link
               {:href dark-url
                :rel "stylesheet"}))
            (fetch-light []
              (head/remove
               :link
               {:href dark-url
                :rel "stylesheet"})
              (head/add
               :link
               {:href light-url
                :rel "stylesheet"}))]
      (case theme
        "light" (fetch-light)
        "dark" (fetch-dark)
        nil))))

(goog-define MD_BASE "")
(goog-define MD_REFRESH_PERIOD 3000)
(goog-define SEARCH_INDEX "/docs.index.edn")

(defnc Docs
  {:wrap [(md/wrap-show {:className ui.css/$md
                         :on-theme-change change-highlight-js})
          (md/wrap-base MD_BASE)
          (md/wrap-refresh MD_REFRESH_PERIOD)
          (search/wrap-index SEARCH_INDEX)
          (router/wrap-rendered ::docs)
          (router/wrap-link
           ::router/ROOT
           [{:id ::docs
             :name "Docs"
             :segment "docs"
             :landing 10}])]}
  []
  (let [{:keys [pathname]} (router/use-location)
        {go-to :go} (router/use-navigate)
        base (hooks/use-context router/-base-)]
    (hooks/use-effect
      [pathname]
      (let [landings? (set
                       (map
                        (fn [path]
                          (if (not-empty base)
                            (str "/" base path)
                            path))
                        ["/docs" "/docs/"]))]
        (when (landings? pathname)
          (go-to "/eywa/docs/overview"))))
    ($ router/Provider
       {:base (if (not-empty base)
                (str base "/docs")
                "docs")}
       ($ _Docs))))
