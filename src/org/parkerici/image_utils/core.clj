(ns org.parkerici.image-utils.core
  (:require [clojure.tools.cli :as cli]
            [clojure.string :as str]
            [org.parkerici.image-utils.ij.core :as ij]))

(defmulti command
  (fn [argument _options _summary] argument))

(defmethod command "split-hyperstack"
  [_ options _]
  ; TODO - Figure out how to make input required.
  (when (contains? options :input)
    (ij/split-hyperstack (:input options))))

(defn all-commands []
  (sort (keys (dissoc (methods command) :default))))

(defn usage
  [options-summary]
  (->> [""
        "Usage: java -jar image-utils.jar [ACTION] [OPTIONS]..."
        ""
        "Actions:"
        (print-str (all-commands))
        ""
        "Options:"
        options-summary]
       (str/join \newline)))

(defmethod command "help"
  [_ _ summary]
  (println (usage summary)))

(defmethod command :default
  [command _ summary]
  (println "Unknown command:" command)
  (println (usage summary)))

(def cli-options
  ;; An option with a required argument
  [["-i" "--input STRING" "Input image. Required."]])

(defn -main
  [& args]
  (let [{:keys [options arguments summary]} (cli/parse-opts args cli-options)]
    (command (first arguments) options summary)))