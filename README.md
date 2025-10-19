# Ruota della Fortuna

## Descrizione Progetto
La Ruota della Fortuna è un'applicazione Java ispirata al famoso gioco televisivo. Il software permette a più giocatori di competere per indovinare una frase misteriosa girando una ruota, acquistando vocali e risolvendo enigmi.

## Contesto Accademico
La Ruota della Fortuna è un progetto sviluppato per la mia Tesi Sperimentale, presso l'Università della Calabria, con l'obiettivo di approfondire le conoscenze nel mondo dell'Artificial Intelligence (AI) e del Large Language Model (LLM).

## Funzionalità Principali
- **Gestione Partite**: Supporto per 2-5 giocatori con multiple sessioni di gioco.
- **Ruota Interattiva**:Sistema di settori con premi, penalità e bonus (Bancarotta, Passa, Raddoppia).
- **Tabellone Dinamico**: Visualizzazione della frase nascosta con rivelazione progressiva delle lettere.
- **Acquisto Vocali**: Sistema di acquisto vocali con controllo del montepremi.
- **Modalità Soluzione**: Possibilità di tentare la soluzione completa della frase.
- **Persistenza Dati**: Salvataggio delle partite e storico risultati du database SQLite.
- **Generazione Frasi**: Integrazione con LLM (Ollama) per generare frasi casuali per categoria.

## Info sul Progetto

### Design Pattern:
- **Command** per l'esecuzione delle operazioni di gioco (gira la ruota, compra vocale, risolvi).
- **Observer** per aggiornare l'interfaccia in tempo reale sui cambiamenti di stato.
- **Singleton** per la gestione del contesto di gioco e della connessione al database.
- **Facade** per separare la logica di business dall'interfaccia utente.
- **Memento** per salvare e ripristinare lo stato del tabellone nella modalità soluzione.
- **Template** per la gestione di più tipi di ruota.
- **Strategy** per le operazioni di normalizzazione del testo (gestione accenti).

### Tecnologie:
- **Java** per il Backend.
- **Vaadin** per l'interfaccia web.
- **SQLite** per la persistenza dei dati.
- **Ollama** con modelli LLM per generazione delle frasi.
- **JUnit** per il testing.
- **Maven** per la gestione delle dipendenze.

## Istallazione ed Esecuzione
1. Clona il repository.
2. Assicurati di avere Java 17+ installato.
3. Esegui 'mvn clear install'.
4. Avvia Ollama localmente sulla porta 11434.
5. Esegui 'mvn spring-boot:run'.
6. Apri il browser su 'http://localhost:8080'.

## Testing 
Il testing è stato svolto tramite **JUnit**, verificando:
- Logica di gioco e gestione turni.
- Rivelazione lettere e controllo soluzioni.
- Gestione montepremi e penalità.
- Persistenza dati e storico partite.