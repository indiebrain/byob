(ns sarkbot.xmpp)

(def chat-message-type
  #^{:doc "A MessageType for XMPP chat messages"}
  org.jivesoftware.smack.packet.Message$Type/chat)

(def chat-filter
  #^{:doc "A MessageTypeFilter for XMPP chat messages"}
  (org.jivesoftware.smack.filter.MessageTypeFilter. qchat-message-type))

(defn message-to-map
  "Breaks apart a message into a map of its interesting parts."
  [message]
  {:to (.getTo message)
   :from (org.jivesoftware.smack.util.StringUtils/parseBareAddress (.getFrom message))
   :body (.getBody message)})

(defn send-chat
  "Responds to a message."
  [connection orig-message-map response-body]
  (let [to (:from orig-message-map)
	threadID (:thread orig-message-map)]
    (try
      (println "Responding to message: " to " " response-body)
      (.sendMessage (.createChat (.getChatManager connection) to nil) response-body)
      (catch Exception exception
	(println exception)))))

(defn make-packet-listener
  "Make a packet listener using the handler as the execution body."
  [connection handler]
  (proxy [org.jivesoftware.smack.PacketListener]
      []
    (processPacket [packet]
      (println "Handling message: " (message-to-map packet))
      (try
	(handler connection (message-to-map packet))
	(catch Exception exception
	  (println exception))))))

(defn add-message-handler
  "Adds a handler to the connection to process inbound messages."
  [connection handler]
  (try
    (.addPacketListener connection (make-packet-listener connection handler) chat-filter)
    (catch Exception exception
      (println exception))))

(defn make-connection-config
  "Creates a configuration holder object for a connection endpoint."
  [config]
  (let [host (:host config)
	port (:port config)
	domain (:domain config)]
    (org.jivesoftware.smack.ConnectionConfiguration. host port domain)))

(defn make-xmpp-connection
  "Creates a XMPP connection to the endpoint described by the configuration."
  [config]
  (let [connection-config (make-connection-config config)]
     (org.jivesoftware.smack.XMPPConnection. connection-config)))

(defn make-connection
  "Creates an open connection the service described in the cofiguration."
  [config]
  (let [connection (make-xmpp-connection config)
	username (:username config)
	password (:password config)]
    (try
      (.connect connection)
      (.login connection username password)
      connection
      (catch Exception exception
	(println exception)))))

(defn disconnect
  "Disconnects from the service."
  [connection]
  (.disconnect connection))