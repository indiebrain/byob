(ns sarkbot.core
  (:require [sarkbot.xmpp :as xmpp])
  (:require [clojure.contrib.properties :as prop]))

(prop/set-system-properties (prop/read-properties "resources/sarkbot.properties"))

(def *config*
  {:username (prop/get-system-property "sarkbot.username")
   :password (prop/get-system-property "sarkbot.password")
   :host (prop/get-system-property "sarkbot.host")
   :port (Integer/parseInt (prop/get-system-property "sarkbot.port"))
   :domain (prop/get-system-property "sarkbot.domain")})

(def sarkbot (xmpp/make-connection *config*))

(defn echo-handler [message]
  "Echoes the body of a message back to its sender"
  (let [body (:body message)]
    (if (not (empty? body))
      (xmpp/respond sarkbot message body))))

(xmpp/add-message-handler sarkbot echo-handler)
