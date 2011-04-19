(ns sarkbot.commands
  (:require [clojure.contrib.string :as string-util]))

(defn now []
  "Returns the current server date and time."
  (java.util.Date.))

(defn echo [& content]
  "Simply returns the content."
  (string-util/join " " content))

(def commands {:echo echo
	       :time now})