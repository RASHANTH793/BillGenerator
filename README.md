# 🍽️ Restaurant Bill Generator 💻

## 📖 Description

The **Restaurant Bill Generator** is a Java-based console application designed to automate billing for restaurants. This system allows both new and returning customers to place orders, calculates the total cost (including 18% GST), and generates a detailed bill. The system uses a MySQL database to store customer details, menu items, and bill information.

## ✨ Features

- 👥 **Customer Management**: Supports both new and returning customers.
- 📝 **Order Placement**: Customers can view the menu, select items, and specify quantities.
- 💵 **Dynamic Billing**: Automatically generates unique customer and bill IDs. Combines new and previous orders for existing customers.
- 🧾 **GST Calculation**: Automatically applies 18% GST to the total bill.
- 🗃️ **Persistent Storage**: Stores customer information, orders, and all bill details in a MySQL database.

## 🛠️ Technology Stack

- **Language**: Java ☕
- **Database**: MySQL 🗄️
- **IDE**: Compatible with IntelliJ IDEA, Eclipse, NetBeans, or any Java IDE.

## 📋 Prerequisites

1. **Java Development Kit (JDK)**: Version 8 or later 🖥️
2. **MySQL Server**: Ensure MySQL is installed and running 🗃️
3. **JDBC Driver**: Add the `mysql-connector-java` library to your classpath 📦

## 🎯 How It Works

1. The system first asks whether the customer is **new** or **returning**.
2. If the customer is new, a unique `customer_id` is created and stored in the database.
3. Returning customers can place new orders, and their previous orders are fetched and combined with the new ones.
4. The system calculates the **subtotal** of the order and applies **18% GST**.
5. After the bill is generated, the system resets automatically for the next customer.

## 🛍️ Example Workflow

1. The system prompts: **"Is this an existing customer? (yes/no)"**
2. If the customer is **new**, the customer’s name is entered, and a unique `customer_id` is generated.
3. The menu is displayed, and the customer can choose products by entering the **product ID** and **quantity**.
4. If the customer is **returning**, their previous orders are retrieved, and they can place new orders, which will be added to their bill.
5. The bill is calculated with **itemized breakdown** and **18% GST** added.
6. The total bill is displayed, and the system thanks the customer, ready to serve the next one.

## 🔮 Future Enhancements

- 🖥️ **Graphical User Interface (GUI)**: Implement a user-friendly GUI for ease of use.
- 🎁 **Discounts & Promo Codes**: Add functionality to apply special discounts or promo codes.
- 💳 **Multiple Payment Methods**: Support for different payment options (e.g., credit cards, wallets, etc.).
- 📊 **Sales Analytics**: Include reports and sales analytics to help restaurant owners understand trends.
- 🛡️ **User Authentication**: Introduce role-based access for staff, managers, and admins.

## 🛠️ Troubleshooting

- **JDBC Driver Issues**: Ensure that the MySQL JDBC driver (`mysql-connector-java.jar`) is included in your classpath.
- **Database Connection Failure**:
    - Make sure the MySQL server is running.
    - Check that your MySQL connection credentials (username, password, and database URL) are correct.
    - Verify the MySQL port (`3306` by default).

- **SQL Errors**: If you're getting SQL errors, ensure that the **customer**, **menu**, and **bill** tables are created correctly, and that data types match between your database and code.

## 👨‍💻 Contributing

1. **Fork the Repository** 🍴: Click on the "Fork" button at the top-right corner of the page.
2. **Create a New Branch** 🌱: Use the command `git checkout -b feature-branch-name` to create a new feature branch.
3. **Commit Changes** ✅: Once you've made changes, run `git commit -m "Add new feature"` to save your changes.
4. **Push to GitHub** 🚀: Push the branch to your forked repository using `git push origin feature-branch-name`.
5. **Open a Pull Request** 📝: Navigate to your fork on GitHub and click "New Pull Request" to submit your changes for review.

## 📄 License

This project is licensed under the **MIT License**.
