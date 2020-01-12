(ns org.parkerici.image-utils.core
  (:require [clojure.tools.cli :as cli]
            [clojure.string :as str]
            [org.parkerici.image-utils.ij.core :as ij]))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))

(defmulti command
  (fn [argument _options _summary] argument))

(defmethod command "split-hyperstack"
  [_ options _]
  ; TODO - Figure out how to make input required outside of here.
  (if (contains? options :input)
    (ij/split-hyperstack (:input options))
    (exit 1 (error-msg ["Input is required"]))))

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

(defn validate-args
  "Validate command line arguments. Either return a map indicating the program
  should exit (with a error message, and optional ok status), or a map
  indicating the action the program should take and the options provided."
  [args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args cli-options)]
    (cond
      (:help options) ; help => exit OK with usage summary
      {:exit-message (usage summary) :ok? true}
      errors ; errors => exit with description of errors
      {:exit-message (error-msg errors)}
      ;; custom validation on arguments
      :else ; failed custom validation => exit with usage summary
      {:action (first arguments) :options options :summary summary})))

(defn -main [& args]
  (let [{:keys [action options summary exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
       (command action options summary))))
