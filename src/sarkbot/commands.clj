(ns sarkbot.commands)

(defn now []
  "Returns the current server date and time."
  (java.util.Date.))

(defn echo [content]
  "Simply returns the content."
  content)

(def commands {:echo echo
	       :time now})