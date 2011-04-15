(ns sarkbot.xmpp)

(defn make-connection-config [config]
  "Creates a configuration holder object for a connection endpoint."
  (let [host (:host config)
	port (:port config)
	domain (:domain config)]
    (org.jivesoftware.smack.ConnectionConfiguration. host port domain)))

(defn make-connection [config]
  "Creates a connection to the endpoint described by the configuration."
  (let [connection-config (make-connection-config config)]
    (org.jivesoftware.smack.XMPPConnection. connection-config)))

(defn connect [config]
  "Creates an open connection the service described in the cofiguration."
  (let [connection (make-connection config)
	username (:username config)
	password (:password config)]
    (.connect connection)
    (.login connection username password)
    connection))

(defn disconnect [connection]
  "Disconnects from the service."
  (.disconnect connection))