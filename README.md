# Lavori

App Android (PWA + Capacitor) per la contabilità dei piccoli lavori: clienti, ore per intervento, spese con foto degli scontrini, pagamenti ricevuti e residuo da incassare.

## Struttura
- `www/index.html` – tutta l'app (dati in IndexedDB sul telefono)
- `www/manifest.webmanifest`, `www/sw.js`, icone – PWA
- `assets/` – icona e splash per l'APK
- `capacitor.config.json`, `package.json` – Capacitor (appId `it.iw1fzr.lavori`)
- `.github/workflows/build.yml` – genera l'APK firmato
- `netlify.toml` – se vuoi pubblicarla anche come sito

## APK
Il workflow parte a ogni commit su `main`. Servono i 4 secret del repository (Settings → Secrets and variables → Actions):
`KEYSTORE_BASE64`, `KEY_ALIAS`, `KEYSTORE_PASSWORD`, `KEY_PASSWORD`.
L'APK firmato si scarica da Actions → ultima esecuzione → Artifacts → `lavori-apk`.

## Dati
Restano solo sul telefono. Fare il backup da **Altro → Esporta backup** (file .json con anche le foto) e conservarlo fuori dal telefono.
