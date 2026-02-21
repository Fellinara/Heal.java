# HealPlugin

Ein Minecraft **Paper-Plugin** für Version **1.21.1**, das den Befehl `/heal` hinzufügt.  
Der Befehl ist auf **OPs** beschränkt (Permission `heal.use`, Standard: OP).

---

## Befehle

| Befehl | Beschreibung |
|--------|-------------|
| `/heal` | Heilt dich selbst (volle HP, Hunger & Sättigung) |
| `/heal <Spieler>` | Heilt den angegebenen Spieler |

---

## Plugin bauen (JAR erstellen)

Du brauchst:
- **Java 21** (JDK) → [adoptium.net](https://adoptium.net/)
- **Apache Maven 3.8+** → [maven.apache.org](https://maven.apache.org/download.cgi)

### Option 1 – Kommandozeile (empfohlen)

```bash
# 1. In den Projektordner wechseln
cd Heal.java

# 2. Plugin bauen
mvn clean package

# 3. Fertige JAR-Datei liegt danach hier:
#    target/HealPlugin-1.0.0.jar
```

Die fertige Datei `HealPlugin-1.0.0.jar` aus dem `target/`-Ordner in den  
`plugins/`-Ordner deines Paper-Servers kopieren und den Server (neu) starten.

---

### Option 2 – IntelliJ IDEA

1. **IntelliJ IDEA** starten → *File → Open* → den Projektordner (`Heal.java`) öffnen.
2. IntelliJ erkennt das Maven-Projekt automatisch und lädt die Abhängigkeiten.
3. Rechts im **Maven**-Panel: `HealPlugin → Lifecycle → package` per Doppelklick ausführen.
4. Die fertige JAR liegt in `target/HealPlugin-1.0.0.jar`.

---

## Installation auf dem Server

1. Paper-Server für 1.21.1 herunterladen: [papermc.io/downloads](https://papermc.io/downloads/paper)
2. `HealPlugin-1.0.0.jar` in den Ordner `plugins/` des Servers kopieren.
3. Server starten oder `/reload confirm` ausführen.
4. einem Spieler OP-Rechte geben: `op <Spielername>`
5. Mit `/heal` testen.

---

## Projektstruktur

```
Heal.java/
├── pom.xml                                   ← Maven-Buildkonfiguration
└── src/main/
    ├── java/de/heal/
    │   ├── HealPlugin.java                   ← Plugin-Einstiegspunkt
    │   └── Heal.java                         ← /heal Befehlslogik
    └── resources/
        └── plugin.yml                        ← Plugin-Metadaten & Berechtigungen
```
