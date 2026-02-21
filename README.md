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

### ✅ Option 1 – GitHub Actions (kein Tool nötig, empfohlen)

Du brauchst **gar nichts** zu installieren – GitHub baut das Plugin automatisch für dich!

#### Schritt 1 – Workflow genehmigen (einmalig, nur beim ersten Mal nötig)

Da der Workflow über einen Pull Request hinzugefügt wurde, muss er einmalig freigegeben werden:

1. Gehe zu **Actions** in deinem Repository.
2. Klicke links auf **„Build HealPlugin"**.
3. Falls ein gelber Banner **„This workflow requires approval"** erscheint → klicke auf **„Approve and run"**.

#### Schritt 2 – Build manuell starten

1. Gehe zu **Actions** → **„Build HealPlugin"**.
2. Klicke rechts auf den Button **„Run workflow"** → dann auf den grünen **„Run workflow"**-Button.
3. Warte ~1–2 Minuten, bis der grüne ✅ erscheint.

#### Schritt 3 – JAR herunterladen

1. Klicke auf den abgeschlossenen Run.
2. Scrolle unten zu **Artifacts**.
3. Klicke auf **HealPlugin** → ZIP-Datei wird heruntergeladen.
4. ZIP entpacken → **`HealPlugin-1.0.0.jar`** liegt darin.

> Nach der ersten Freigabe läuft der Workflow automatisch bei jedem weiteren Push.

---

### Option 2 – Kommandozeile (lokal)

Du brauchst:
- **Java 21** (JDK) → [adoptium.net](https://adoptium.net/)
- **Apache Maven 3.8+** → [maven.apache.org](https://maven.apache.org/download.cgi)

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
