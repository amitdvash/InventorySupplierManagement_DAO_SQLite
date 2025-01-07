# InventorySupplierManagement_DAO_SQLite

## Overview
This project is an inventory and supplier management system that uses the DAO pattern and SQLite database for efficient data handling. The system supports supplier agreements, product management, and order tracking.

## Features
- Supplier management, including agreements and contact details.
- Product management with dynamic discount handling.
- Order tracking with active and recurring order support.
- SQLite integration for data storage and retrieval.
- DAO pattern for clean separation between logic and data access.

## Database
The system uses SQLite with the following main tables:
- **Suppliers**: Manages supplier details and agreements.
- **Products**: Stores product details, including discounts and catalog information.
- **Orders**: Tracks orders, including quantities, discounts, and delivery information.
- **SupplyDays**: Handles recurring delivery schedules.

## Code Structure
- **Domain**: Contains main business logic classes (e.g., `Order`, `Product`).
- **Database**: Includes DAO classes for database interaction.
- **Controllers**: Manages the interaction between the domain and the database.

## About
This system was built as a project to showcase integration between Java, SQLite, and the DAO design pattern. It focuses on clean code principles and efficient data handling.
