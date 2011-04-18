(ns sarkbot.handlers
  (:require [sarkbot.xmpp :as xmpp])
  (:require [sarkbot.commands :as cmd])
  (:require [clojure.contrib.string :as string-util]))

(defn echo-handler [connection message]
  "Echoes the body of a message back to its sender"
  (let [body (:body message)]
    (if (not (empty? body))
      (xmpp/send-chat connection message body))))

(defn command-handler [connection message]
  "Interrogates a message for an instruction from the sender and attempts to execute the instruction if it is recognized."
  (let [message-body (:body message)
	[command & args] (string-util/split #"\s+" message-body)
	command-function (cmd/commands (keyword command))]
    (if (not (nil? command-function))
      (xmpp/send-chat connection message (str (apply command-function args)))
      (xmpp/send-chat connection message (str "I don't know how to interpret the command: " (keyword command))))))