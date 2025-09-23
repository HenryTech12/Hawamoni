# Hawamoni

**Hawamoni** is a blockchain-native group treasury for student groups and small teams. It enables **fast deposits via Solana Pay**, **on-chain governed withdrawals requiring ≥80% owner approval**, and **real-time SMS + in-app notifications** for transparency and trust.

---

## 🚀 Features

- **Wallet-based authentication** using signed nonce challenges  
- **Create / Join groups** with automatic supermajority thresholds  
- **Treasury PDA & Solana Pay deposits** (QR / link)  
- **Withdrawal requests** with ≥80% approval required  
- **On-chain approvals & execution** for fund transfers  
- **Notifications** (SMS + in-app) for requests and executions  
- **Audit trail & history** with CSV export  

---

## 🎯 Purpose & Goals

- Deliver secure, auditable, easy-to-use group fund management.  
- Ensure deposits, approvals, and withdrawals are transparent.  
- Provide a hackathon-ready MVP running on Solana devnet.  
- Keep UX mobile-first and simple for non-technical users.  

---

## 🧑‍🤝‍🧑 Target Users

- **Student clubs / coursemates** — collect dues and pay vendors.  
- **Small project teams** — manage shared funds and reimbursements.  
- **Campus vendors** — accept payments with verifiable receipts.  

---

## 📐 Architecture

```
User (mobile/web)
   ↕
Frontend (Next.js)
   ↕
Backend (Java, Spring Boot) → DB (Postgres/Supabase), Redis queue
   ↕
Node Service (Solana Pay + web3.js)
   ↕
Anchor Program (Rust, Solana devnet)
   ↕
Notifications (SMS via Africa's Talking / Twilio)
   ↕
AI Microservice (Python, FastAPI)
```

---

## 🛠 Tech Stack

- **Frontend:** Next.js + wallet-adapter + TypeScript  
- **Backend:** Java (Spring Boot) + Postgres + Redis  
- **Blockchain:** Solana (Anchor, PDAs, Solana Pay)  
- **AI (optional):** FastAPI (fraud detection, spending insights)  
- **Notifications:** Africa’s Talking / Twilio  

---

## ⚡ Quickstart

### Prerequisites
- Node.js ≥ 18  
- Java 17 + Maven  
- Python 3.10+  
- Solana CLI + Anchor  
- Postgres DB + Redis  

### Environment Setup
```bash
# Clone the repository
git clone https://github.com/<your-org>/hawamoni.git
cd hawamoni

# Install frontend deps
cd frontend
npm install

# Install backend deps
cd ../backend
mvn clean install

# Install AI service deps
cd ../ai-service
pip install -r requirements.txt
```

Configure your `.env` files with:  
- Postgres connection  
- Redis URL  
- Solana devnet RPC URL  
- SMS provider keys  

---

## ▶️ Running Locally

```bash
# Start Solana local validator (optional)
solana-test-validator

# Deploy Anchor program
anchor deploy

# Run backend
cd backend
mvn spring-boot:run

# Run frontend
cd frontend
npm run dev

# Run Node Solana service
cd solana-service
npm run dev

# Run AI service
cd ai-service
uvicorn main:app --reload
```

---

## 📊 Demo Flow (Hackathon-Ready)

1. **Login:** Connect wallet and sign nonce  
2. **Create Group:** Add 5 owners → 4 approvals required  
3. **Deposit:** Generate QR, scan with Phantom, confirm on devnet  
4. **Withdrawal Request:** Submit request (amount, reason, recipient)  
5. **Approval:** Owners sign approval → counter updates  
6. **Execution:** Request auto-executes once threshold met  
7. **Notifications:** SMS + in-app updates sent automatically  
8. **Audit Trail:** Export CSV with tx history  

---

## 📑 API Endpoints (Backend)

- `POST /auth/nonce` → get nonce to sign  
- `POST /auth/verify` → verify signature, issue session  
- `POST /groups` → create group  
- `POST /groups/{id}/deposit` → generate Solana Pay QR  
- `POST /groups/{id}/requests` → create withdrawal request  
- `POST /requests/{id}/approve` → approve request  
- `POST /requests/{id}/execute` → execute withdrawal  
- `GET /requests/{id}` → get request status  

---

## 📜 Acceptance Criteria (MVP)

- Wallet auth via signed nonce works  
- Group creation stores on-chain and DB records  
- Solana Pay deposits verified on devnet  
- Withdrawal requests created, approved, and executed  
- SMS + in-app notifications triggered  
- Full transaction history exportable as CSV  

---

## 👥 Contributors

- **Frontend Developer** — Next.js app, wallet integration, QR scanning, UI  
- **Backend Developer** — Spring Boot APIs, DB, SMS jobs, CI/CD  
- **AI Engineer** — Fraud detection, embeddings, summaries  

---

## 📌 Roadmap (Future)

- Tokenized shares & weighted approvals  
- Gasless UX (relayer)  
- Scheduled payouts & batching  
- AI-driven fraud detection and spending insights  
- Multi-chain support  
