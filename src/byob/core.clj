(ns byob.core
  (:require [byob.xmpp :as xmpp])
  (:require [byob.handlers :as handlers])
  (:require [clojure.contrib.properties :as prop]))

(prop/set-system-properties (prop/read-properties "resources/xmpp_connection.properties"))

(def config
  #^{:doc "A map of configuration used to create an XMPP connection."}
  {:username (prop/get-system-property "xmpp.username")
   :password (prop/get-system-property "xmpp.password")
   :host (prop/get-system-property "xmpp.host")
   :port (Integer/parseInt (prop/get-system-property "xmpp.port"))})

(defn create-bot
  "Creates a bot instance from the configuration and adds each message handler to its connection."
  [& handlers]
  (let [bot (xmpp/make-connection config)]
    (map #(xmpp/add-message-handler bot %) handlers)))