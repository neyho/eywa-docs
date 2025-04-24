(ns build
  (:require
   [clojure.edn :as edn]
   [clojure.tools.build.api :as b]
   [toddler.template :as template]))

(defonce salt (template/random-string))
(defonce index-file (format "/docs.index.%s.edn" salt))

(defn frontend-config
  []
  (let [config (edn/read-string (slurp "shadow-cljs.edn"))
        {{main-build :dev} :builds} config]
    (->
     main-build
     (update :modules (fn [current]
                        (reduce-kv
                         (fn [r k v]
                           (assoc r (keyword (str (name k) "." salt))
                                  (if (contains? v :depends-on)
                                    (update v :depends-on
                                            (fn [deps]
                                              (reduce
                                               (fn [r d]
                                                 (conj r (keyword (str (name d) \. salt))))
                                               #{}
                                               deps)))
                                    v)))
                         nil
                         current)))
     (dissoc :output-to :output-dir)
     (assoc :closure-defines `{eywa-docs.docs.main/ROUTER_BASE ""
                               eywa-docs.docs.lazy/MD_BASE ""
                               eywa-docs.docs.lazy/MD_REFRESH_PERIOD 0
                               eywa-docs.docs.lazy/SEARCH_INDEX ~(str "/docs.index." salt ".edn")}
            :asset-path "/js"))))

(letfn [(->link [route]
          (str "/docs" route))]
  (def mds
    [{:route (->link "/intro")
      :topic "Rationale"
      :path "dev/docs/intro.md"}]))

(defn release
  [_]
  ;; BUILD CSS
  (b/delete {:path "target"})
  (comment
    (template/run-script (str "clj -X:shadow:css salt " salt)))
  (let [{:keys [err]} (template/run-script
                       (str "clj -X:shadow:css salt " salt)
                       (str "npx shadow-cljs --config-merge "
                            (template/escape-data (frontend-config))
                            " release production")
                       (str "clj -X:index :mds " (template/escape-data mds) " :output " (str "target/" index-file)))]
    (when (not-empty err) (println err)))
  (b/copy-dir
   {:src-dirs ["dev/docs"]
    :target-dir "target/docs"})
  (b/delete {:path "target/index.html"})
  (b/delete {:path "target/404.html"})
  (template/process
   "index.html.tmp" "target/index.html" {:salt salt :root ""})
  (template/process
   "index.html.tmp" "target/404.html" {:salt salt :root ""}))
