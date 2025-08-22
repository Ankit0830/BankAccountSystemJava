# 🏦 BankAccountSystemJava

A simple Java-based banking system demonstrating core Object-Oriented Programming (OOP) concepts like **inheritance**, **encapsulation**, and **polymorphism**. This project simulates banking operations for **Savings** and **Loan** accounts.

## 📌 Features

### 🔐 Common Features (`AccountTest`)
- Account number, name, phone number, DOB, and balance
- Getters and setters for data encapsulation
- Constructor-based initialization
- Basic `display()` method

### 💰 Savings Account (`SavingAccount`)
- Deposit and withdraw money
- Create and liquidate a fixed deposit (FD)
- FD earns simple interest (configurable months and rate)

### 🧾 Loan Account (`LoanAccount`)
- EMI calculation based on principal, annual rate, and tenure
- Pay monthly EMI or make lump-sum prepayment
- Top-up existing loan
- Track outstanding balance
- Override `display()` to show loan details

## 🧱 Project Structure

