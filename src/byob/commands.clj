(ns byob.commands
  (:require [clojure.contrib.string :as string-util]))

(defn now
  "Returns the current server date and time."
  [] (java.util.Date.))

(defn echo
  "Simply returns the content."
  [& content] (string-util/join " " content))

(declare commands)

(defn help
  "Returns a list of the available commands."
  [] (string-util/join "\n" (map #(str (key %) ": " (:doc (meta (val %)))) commands)))

(def commands
  #^{:doc "A map of known commands."}
  (dissoc (ns-publics 'byob.commands)
	  'commands))