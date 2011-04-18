(ns sarkbot.xmpp)

(def chat-message-type org.jivesoftware.smack.packet.Message$Type/chat)

(def chat-filter (org.jivesoftware.smack.filter.MessageTypeFilter.
		    chat-message-type))

(defn message-to-map [message]
  "Breaks apart a message into a map of its interesting parts."
  {:to (.getTo message)
   :from (org.jivesoftware.smack.util.StringUtils/parseBareAddress (.getFrom message))
   :body (.getBody message)})

(defn send-chat [connection orig-message-map response-body]
  "Responds to a message."
  (let [to (:from orig-message-map)
	threadID (:thread orig-message-map)]
    (try
      (println "Responding to message: " to " " response-body)
      (.sendMessage (.createChat (.getChatManager connection) to nil) response-body)
      (catch Exception exception
	(println exception)))))

(defn make-packet-listener [connection handler]
  "Make a packet listener using the handler as the execution body."
  (proxy [org.jivesoftware.smack.PacketListener]
      []
    (processPacket [packet]
      (println "Handling message: " (message-to-map packet))
      (try
	(handler connection (message-to-map packet))
	(catch Exception exception
	  (println exception))))))

(defn add-message-handler [connection handler]
  "Adds a handler to the connection to process inbound messages."
  (try
    (.addPacketListener connection (make-packet-listener connection handler) chat-filter)
    (catch Exception exception
      (println exception))))

(defn make-connection-config [config]
  "Creates a configuration holder object for a connection endpoint."
  (let [host (:host config)
	port (:port config)
	domain (:domain config)]
    (org.jivesoftware.smack.ConnectionConfiguration. host port domain)))

(defn make-xmpp-connection [config]
  "Creates a XMPP connection to the endpoint described by the configuration."
  (let [connection-config (make-connection-config config)]
     (org.jivesoftware.smack.XMPPConnection. connection-config)))

(defn make-connection [config]
  "Creates an open connection the service described in the cofiguration."
  (let [connection (make-xmpp-connection config)
	username (:username config)
	password (:password config)]
    (try
      (.connect connection)
      (.login connection username password)
      connection
      (catch Exception exception
	(println exception)))))

(defn disconnect [connection]
  "Disconnects from the service."
  (.disconnect connection))

(defn stdout-handler [message]
  "Dumps the body of a message to STDOUT."
  (println (:body message)))