(ns org.parkerici.image-utils.utils.error
  (:require [clojure.string :as str]))


(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))