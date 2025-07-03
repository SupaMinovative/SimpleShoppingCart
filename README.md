## 🛒 Simple Shopping Cart App (Android)


A fully self-built Android application that simulates the core logic of a real-world online shopping system — developed with a focus on clean architecture, real-time UI updates, and user interaction flow.


<p align="center"><img src="screenshots/cart_navigation.gif" alt="Main Screen" width="400"/></p>


## 🎯 Purpose & Learning Goals


*This app was designed as a personal learning project to deeply understand:*


The underlying mechanisms behind modern e-commerce apps


Real-time data flow between UI and business logic


Local authentication and session handling


(Soon) working with databases and APIs in a full-stack environment


⚙️ It's a hands-on, full-stack-friendly foundation, built entirely from scratch — no pre-built templates or third-party libraries — to maximize learning, flexibility, and control.


*The next step is to connect this app to a Spring Boot + PostgreSQL backend for real-time product updates, account management, and purchase tracking.*

<p align="center"><img src="screenshots/main_display.png" alt="Main Screen" width="500"/></p>

## 🔐 Login & Account Validation

*Includes a basic local authentication system with:*


Sign-up / login functionality


Login validation that checks against Room Database


SharedPreferences support for “Remember Me” functionality



*💡 This forms the groundwork for future user management and API-based authentication, which will be implemented via a remote backend.*


<p align="center"><img src="screenshots/item_view.png" alt="Item on cart" width="500"/></p>

## 📊 Real-Time Cart Updates (LiveData)


*Implements Android’s LiveData for a reactive and dynamic cart experience. The UI updates automatically when:*


Products are added or removed from the cart


Item quantity changes


The total price is recalculated in real time


Ensures a responsive and modular UI, cleanly separated from data operations.


## 🔍 Smart Search Feature


Includes a simple search box to filter products by name as you type.
The filtered results are shown live using a separate adapter, enhancing the browsing experience without needing any external APIs.


## ⚙️ Key Features


*🛒 Add/Remove Items:* Easily manage your cart with intuitive item controls

*➕ Adjust Quantity:* Modify product quantities in the cart summary view

*🖼️ Full-Screen Product Display:* View product details with a full-screen layout and description.

*💰 Live Price Updates:* Automatically update total price as cart changes

*📋 Checkout Summary:* See a full list of selected products before checkout

*🔍 Live Product Search:* Filter products instantly using the search bar

*🔐 Local Authentication:* Secure sign-up/login system using Room DB and SharedPreferences

*📦 Test Product Data:* Currently uses static product data; will be replaced with Room DB or remote JSON


## 🧠 Tech Stack

*Java* (Android)

*LiveData + ViewModel* architecture

*RecyclerView* for dynamic product display

*Room Database (SQLite)* for user data

*SharedPreferences* for login persistence


## 🎯 Planned:

 Spring Boot REST API + PostgreSQL DB for backend features


*What’s Next*

 Integrate with Spring Boot backend (authentication + product DB)


 Replace static product data with Room or API source


 Add filtering/sorting options (e.g., price, category)


 Build a user profile and order history system

 

