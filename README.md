# App-Instant-Messaging-Client-Server

A lightweight client-server chat application built in Java 8 with a JavaFX graphical interface. It features a dual-protocol architecture separating control signals from message transmission to optimize latency and reliability.

---

## 🏗️ Architecture & APDU Protocol

The system splits Application Protocol Data Units (APDUs) between transport layers:

* **TCP Channel (Control):** Handles handshakes, room registration, authentication, and connection state. Guarantees packet delivery and ordering for critical control events.
* **UDP Channel (Data):** Handles real-time text message transmission and broadcasting. Minimizes overhead and eliminates head-of-line blocking.

---

## 🛠️ Tech Stack & Features

* **Language & UI:** Java 8, JavaFX
* **Networking:** `java.net.Socket`, `java.net.ServerSocket`, `java.net.DatagramSocket`
* **Concurrency:** Multi-threaded client handling with synchronized session registries.
* **Protocol:** Custom structured APDU frame parsing.
