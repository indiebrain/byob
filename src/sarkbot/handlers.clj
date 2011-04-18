(ns sarkbot.handlers
  (:require [sarkbot.xmpp :as xmpp]))

(defn echo-handler [connection message]
  "Echoes the body of a message back to its sender"
  (let [body (:body message)]
    (if (not (empty? body))
      (xmpp/send-chat connection message body))))