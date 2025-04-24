(ns compile-css
  (:require
   [clojure.java.io :as io]
   [shadow.css.build :as cb]
   [shadow.cljs.devtools.server.fs-watch :as fs-watch]
   [toddler.ui.css :as css])
  (:import java.util.zip.ZipInputStream))

(defonce css-ref (atom nil))
(defonce css-watch-ref (atom nil))

(defn generate-css
  ([] (generate-css "dev"))
  ([dir]
   (let [result
         (-> @css-ref
             (cb/generate '{:main {:include [eywa-docs.main
                                             eywa-docs*
                                             toddler.ui*
                                             toddler.docs]}})
             (cb/write-outputs-to (io/file dir "css")))]
     (prn :CSS-GENERATED)
     (doseq [mod (:outputs result)
             {:keys [warning-type] :as warning} (:warnings mod)]
       (prn [:CSS (name warning-type) (dissoc warning :warning-type)]))
     (println))))

(defn init []
  (->
   (cb/init)
   (update :aliases merge css/aliases)
   (cb/start)
   (cb/index-path (io/file "src") {})
   (cb/index-path (io/file ".." ".." ".." "toddler" "ui" "src") {})))

(comment
  (.isDirectory (io/file ".." ".." ".." "toddler" "ui" "src")))

(defn start
  {:shadow/requires-server true}
  []

  ;; first initialize my css
  (reset! css-ref (init))

  ;; then build it once
  (generate-css)

  ;; then setup the watcher that rebuilds everything on change
  (reset! css-watch-ref
          (fs-watch/start
           {}
           [(io/file "src")]
           ["cljs" "cljc" "clj" "css"]
           (fn [updates]
             (try
               (doseq [{:keys [file event]} updates
                       :when (not= event :del)]
                  ;; re-index all added or modified files
                 (swap! css-ref cb/index-file file))

               (generate-css)
               (catch Exception e
                 (prn :css-build-failure)
                 (prn e))))))

  ::started)

(defn stop []
  (when-some [css-watch @css-watch-ref]
    (fs-watch/stop css-watch)
    (reset! css-ref nil))

  ::stopped)

(defn go []
  (stop)
  (start))

(defn release [{salt 'salt}]
  ;; first initialize my css
  (let [release-key (if salt
                      (keyword (str "main." salt))
                      :main)]
    (-> (init)
        (cb/generate {release-key
                      {:include '[eywa-docs.main
                                  eywa-docs*
                                  toddler.ui*
                                  toddler.docs]}})
        (cb/write-outputs-to (io/file "target" "css")))))

(comment
  (-> @css-ref)
  (go))
