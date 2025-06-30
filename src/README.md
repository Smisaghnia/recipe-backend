🍲 RecipeApp – Spring Boot Projekt mit JWT-Authentifizierung & Favoritenfunktion
Ein modernes Backend für eine Rezeptverwaltungsanwendung mit Nutzerregistrierung, Login, JWT-Authentifizierung, Rezeptverwaltung und Favoritenfunktion.

✅ Features
🔐 JWT-Authentifizierung (Login & Registrierung)

👥 Benutzerrollen: USER, ADMIN

📋 CRUD für Rezepte (Titel, Anleitungen, Kategorien)

⭐ Favoritenfunktion (Rezept zu Favoriten hinzufügen/entfernen)

📁 Datenbank: MySQL / PostgreSQL

✅ Saubere DTO-Architektur & REST API

🛠 Tech Stack
Java 17+

Spring Boot 3

Spring Security

JWT (JSON Web Tokens)

JPA (Hibernate)

Lombok

MySQL oder PostgreSQL

Postman (für Tests)

🧪 1. API-Test mit Postman
🔐 Authentifizierung
POST /api/auth/register
POST /api/auth/login
🔸 Beispiel Login (POST /api/auth/login)
{
"email": "test@example.com",
"password": "password123"
}
✅ Im Response: token (als Bearer Token in folgenden Requests verwenden)


🍽️ 2. Rezepte
🔹 POST /api/recipes
→ Rezept erstellen
(z. B. durch Admin oder bei offener Registrierung)
{
"title": "Linsensuppe",
"instructions": "Zwiebel schneiden, Linsen kochen...",
"categoryIds": [1, 3]
}


⭐ 3. Favoriten-Funktion
Alle Requests benötigen einen Bearer Token in der Autorisierung.

🔸 POST /api/favorites/{recipeId}
→ Rezept als Favorit speichern
POST /api/favorites/123
Authorization: Bearer {TOKEN}

🔸 DELETE /api/favorites/{recipeId}
→ Rezept aus Favoriten entfernen
DELETE /api/favorites/123
Authorization: Bearer {TOKEN}

🔸 GET /api/favorites
→ Alle Lieblingsrezepte des eingeloggten Users anzeigen
GET /api/favorites
Authorization: Bearer {TOKEN}
[
{
"id": 123,
"title": "Linsensuppe",
"instructions": "Zwiebel schneiden, Linsen kochen...",
"createdBy": "admin@kochwelt.de",
"categoryNames": ["Vegan", "Suppe"]
}
]

🗂️ 4. Projektstruktur (Auszug)
com.recipeapp.recipe
├── controller
├── service
├── entity
├── repository
├── dto
├── security
└── config
└── SecurityConfig.java


🧩 5. Datenbank
🔹 Tabelle user_favorites (automatisch durch @ManyToMany)
| user\_id | recipe\_id |
| -------- | ---------- |
| 1        | 3          |
| 1        | 5          |

🔎 6. Rezeptsuche & Kategorie-Filter
   🔸 GET /api/recipes/search?query=linsen
   → Suche nach Rezepttitel oder Anleitungen
GET /api/recipes/search?query=linsen
[
{
"id": 123,
"title": "Linsensuppe",
"instructions": "Zwiebel schneiden, Linsen kochen...",
"createdBy": "admin@example.com",
"categoryNames": ["Vegan", "Suppe"]
}
]
🔸 GET /api/recipes/filter?categories=Vegan,Suppe
→ Filtert Rezepte, die alle angegebenen Kategorien enthalten
GET /api/recipes/filter?categories=Vegan,Suppe
🔹 Man kann eine oder mehrere Kategorien als Komma-separierte Liste angeben.
✅ Beispiel:

categories=Vegan → Alle veganen Rezepte

categories=Vegetarisch, Dessert → Nur Rezepte, die beides sind

🧠 7. Tipps für Weiterentwicklung
🖼️ Bild-Upload für Rezepte

📊 Bewertungssystem (1–5 Sterne)

🛒 Einkaufsliste

📱 Frontend mit React (z.B. über Vite)

🤖 KI-Rezeptvorschläge