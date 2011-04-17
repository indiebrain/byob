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

(defn echo-handler [connection message]
  "Echoes the body of a message back to its sender"
  (let [body (:body message)]
    (if (not (empty? body))
      (xmpp/send-chat connection message body))))

(defn create-bot [& handlers]
  "Creates a bot instance from the sarkbot.properties configuration
   and adds each message handler to its connection."
  (let [bot (xmpp/make-connection *config*)]
    (map #(xmpp/add-message-handler bot %) handlers)))