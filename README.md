Stock Exchange Simulator

This project, developed in Java, simulates a stock exchange trading system. It demonstrates a deep understanding of object-oriented design principles and core data structures by handling real-time trading, price updates, and order matching. The system models key components of a trading system, including orders, quotes, and product books, to accurately process transactions and maintain a live market state.



Technical Stack

Languages: Java

Core Concepts: Object-Oriented Programming (OOP), Data Structures (HashMap, ArrayList, TreeMap), Interfaces, Enums, Singletons, Exception Handling, Comparable interface

Design Patterns: Flyweight, Singleton, Façade

Tools: IntelliJ, VS Code

Core Features

Real-time Trading: The system processes buy and sell transactions based on a user's intent to buy or sell a stock at a specified price and volume.


Order and Quote Management: It handles different types of tradable items, including Orders and Quotes, each with specific behaviors and data fields such as user, product, price, and volume.


Product Books: The system maintains a "book" for each stock, which holds buy and sell tradables that have not yet been matched. The buy side of the book is sorted in descending order by price, and the sell side is sorted in ascending order.



Price Handling: The Price class stores price values as integers (cents) to avoid floating-point precision errors, ensuring accurate calculations. It also includes a 

PriceFactory to create new immutable Price objects.


User and Product Management: The UserManager and ProductManager classes use the Singleton and Façade design patterns to maintain and manage all users and product books in the system.



Object-Oriented Design & Implementation

This project was a comprehensive exercise in applying advanced OOP concepts to build a robust system. It showcases several key design patterns:

Flyweight Pattern: The PriceFactory implements the Flyweight pattern to ensure that only one Price object of a specific value is created, which helps manage memory efficiently.

Singleton Pattern: The UserManager, ProductManager, and CurrentMarketPublisher classes are all Singletons, ensuring that only one instance of each exists throughout the application.



Façade Pattern: The UserManager and ProductManager classes act as a Façade, providing a simplified interface for interacting with the complex user and product book systems.

Challenges & Solutions

Handling Immutability: A core challenge was ensuring that key classes like Price, Order, and QuoteSide were immutable or had specific unchangeable fields. This was achieved by designing private constructors and modifiers, as well as by creating new objects for any changes in value rather than modifying existing ones.





State Management: The system had to manage the state of each tradable item, tracking its original volume, remaining volume, filled volume, and canceled volume. This was handled with dedicated private data members and public modifiers to ensure data integrity.




Correct Execution: A significant portion of the project involved ensuring correct and accurate application execution, which required meticulous debugging and adherence to the detailed test cases provided.

