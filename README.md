# 👣 OnlyFeet – Platformă premium pentru foot fetish content

OnlyFeet este o aplicație web inspirată de modelul OnlyFans, dar nișată 100% pe conținut de tip **foot fetish**. Creatoarele pot posta poze și video-uri, iar utilizatorii pot plăti abonamente sau comenzi personalizate. Platforma este construită pe un stack modern și este gândită pentru scalabilitate și costuri reduse.

---

## 🚀 Tehnologii folosite

### 🧠 Backend (Spring Boot)
- Spring Boot 3 (REST API)
- PostgreSQL
- Spring Data JPA (fără relații directe)
- Keycloak (OAuth2, autentificare și autorizare)
- Liquibase (migrări SQL)
- Stripe (plăți și comision platformă)

### 💻 Frontend (Next.js)
- TypeScript
- Tailwind CSS + ShadCN UI
- Stripe Checkout
- JWT-based auth (prin Keycloak)
- SSR + client-side routing

---

## 📦 Structura backend

```bash
src/
├── content/
├── user/
├── subscription/
├── customrequest/
├── payment/
├── rating/
├── poll/
├── stripe/
├── comment/
└── config/

```
---

## 🧱 Structura bazei de date (entități principale)

- `users` – utilizatori și creatori de conținut
- `content` – conținut uploadat (poză, video)
- `subscriptions` – abonamente active/inactive
- `stripe_accounts` – conturi Stripe Connect (pentru payout-uri)
- `payments` – plăți pentru abonamente, tips, conținut custom
- `custom_requests` – cereri personalizate de la useri
- `polls` + `poll_votes` – sondaje și voturi
- `likes`, `comments`, `ratings` – interacțiune cu conținut

## 🔐 Autentificare și Autorizare

- Toate requesturile trec prin **JWT Bearer Token**, emis de **Keycloak**
- Role-based access control: `USER`, `CREATOR`, `ADMIN`
- Pagini publice pentru conținut gratuit, protejate pentru abonați

---

## 💳 Sistem de plăți

- Stripe Connect Express
- Abonamente lunare între utilizatori și creatori
- Comision automat pentru platformă (ex: `20%`)
- Plăți one-time pentru cereri personalizate

---

## 🧪 Comenzi utile

### Dev local:
```bash
./mvnw spring-boot:run
```

## 🧠 Ce ne diferențiază

- `100%` focus pe nișă: **foot fetish**
- Rating picioare (1–5 stele) în stil **WikiFeet**
- Cereri personalizate cu status (`pending`, `accepted`, `delivered`)
- Sondaje: creatorii pot cere opinia fanilor
- Split automat al plății între platformă și creator (comision ex: `20%`)

---

## 📫 Contact & contribuții

Pentru contribuții, întrebări sau propuneri de parteneriat, deschide un **GitHub Issue**  
sau trimite un email la:

**✉️** [`contact@onlyfeet.app`](mailto:contact@onlyfeet.app)

---

## ⚠️ Disclaimer

Acest proiect este pentru uz **educațional/prototipare** și respectă politicile platformelor
