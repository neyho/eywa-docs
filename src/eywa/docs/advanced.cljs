(ns eywa.docs.advanced
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

(defnc Environment
  {:wrap [(router/wrap-rendered :eywa.advanced.environment)
          (router/wrap-link
           :eywa.advanced.environment
           [{:id :eywa.advanced.environment.system
             :name "System"
             :hash "system"}
            {:id :eywa.advanced.environment.platform
             :name "Platform"
             :hash "platform"}
            {:id :eywa.advanced.environment.db
             :name "Database"
             :hash "database"}])]}
  []
  (let [ww (toddler/use-window-width)
        max-width (min 600 ww)]
    ($ md/watch-url
       {:url (router/use-with-base "/advanced/environment.md")
        :style {:max-width max-width}})))

(defnc Encryption
  {:wrap [(router/wrap-rendered :eywa.advanced.encryption)]}
  []
  (let [ww (toddler/use-window-width)
        max-width (min 600 ww)]
    ($ md/watch-url
       {:url (router/use-with-base "/advanced/encryption.md")
        :style {:max-width max-width}})))

(defnc CLI
  {:wrap [(router/wrap-rendered :eywa.advanced.cli)
          (router/wrap-link
           :eywa.advanced.cli
           [{:id :eywa.advanced.cli.eywa
             :name "Client"
             :hash "eywa-cli-client"}
            {:id :eywa.advanced.cli.distribution
             :name "Distribution"
             :hash "distribution"}
            {:id :eywa.advanced.cli.development
             :name "Development"
             :hash "development"}
            {:id :eywa.advanced.cli.configuration
             :name "Configuration"
             :hash "configuration"}])]}
  []
  (let [ww (toddler/use-window-width)
        max-width (min ww 800)]
    ($ ui/row
       {:style {:max-width max-width}
        :align :center}
       ($ md/watch-url
          {:url (router/use-with-base "/advanced/cli.md")
           :style {:max-width max-width}})
       ($ util/mount-image
          {:id "eywa_core_bucket"
           :url "/advanced/cli/eywa_core_bucket"
           :className (css :my-4)
           :themed? false})
       ($ util/mount-image
          {:id "eywa_connect"
           :url "/advanced/cli/eywa_connect"
           :className (css :my-4)
           :themed? false})
       ($ toddler/portal
          {:locator #(.getElementById js/document "eywa_run_explanation")}
          ($ ui/row
             {:align :center
              :className (css :py-4)}
             ($ ui/button
                {:id "open-run-explanation"}
                "See what will happen!")))
       ($ util/mount-modal-image
          {:id "open-run-explanation"
           :url "cli/eywa_client"
           :className (css :p-6)
           :width (min 800 (- ww 100))}))))

(defnc InGeneral
  {:wrap [(router/wrap-rendered :eywa.advanced true)]}
  []
  ($ md/watch-url {:url (router/use-with-base "/advanced.md")}))

(defnc Tour
  {:wrap [(router/wrap-rendered :eywa.advanced.tour)]}
  []
  (let [back (router/use-go-to :eywa.advanced)
        {ww :width
         wh :height} (toddler/use-window-dimensions)
        width (min (- ww 60) 1400)
        height (min (- wh 60) 900)]
    ($ ui/modal-dialog
       {:on-close #(back)
        :className (css :border-0 :bg-transparent)}
       ($ util/tour
          {:width width
           :height height
           :images ["/advanced/custom/app1.png"
                    "/advanced/custom/app2.png"
                    "/advanced/custom/app3.png"
                    "/advanced/custom/app4.png"
                    "/advanced/custom/app5.png"
                    "/advanced/custom/app6.png"
                    "/advanced/custom/app7.png"]}))))

(defnc Advanced
  {:wrap [(router/wrap-rendered :eywa.advanced)
          (router/wrap-link
           :eywa.advanced
           [{:id :eywa.advanced.cli
             :segment "cli"
             :name "CLI"}
            {:id :eywa.advanced.environment
             :segment "environment"
             :name "Environment"}
            {:id :eywa.advanced.encryption
             :segment "encryption"
             :name "Encryption"}
            {:id :eywa.advanced.tour
             :segment "tour"}])]}
  []
  (let [{:keys [width height]} (layout/use-container-dimensions)
        open-tour (router/use-go-to :eywa.advanced.tour)]
    ($ ui/simplebar
       {:style {:height height}}
       ($ ui/row {:align :center}
          ($ toddler/portal
             {:locator #(.getElementById js/document "tour")}
             ($ ui/row
                {:align :center
                 :style {:margin-top 20}}
                ($ ui/button
                   {:on-click #(open-tour)}
                   "Build App Tour")))
          ($ Tour)
          ($ ui/row
             {:style {:max-width (min width 600)}}
             ($ InGeneral)
             ($ Environment)
             ($ Encryption)
             ($ CLI))))))
