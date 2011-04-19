(ns sarkbot.commands
  (:require [clojure.contrib.string :as string-util]))

(defn now []
  "Returns the current server date and time."
  (java.util.Date.))

(defn echo [& content]
  "Simply returns the content."
  (string-util/join " " content))

;; A map of known commands.
(def commands (dissoc (ns-publics 'sarkbot.commands)
		      'commands))

(defn help []
  "Returns a list of the available commands."
  (string-util/join "\n" (keys commands)))