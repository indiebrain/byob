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
  (let [connection (make-connection config)
	username (:username config)
	password (:password config)]
    (.connect connection)
    (org.jivesoftware.smack.SASLAuthentication/supportSASLMechanism "PLAIN" 0)
    (.login connection username password)))