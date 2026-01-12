# Accord Innnovation

This sample Spring Boot application demonstrates:

- REST endpoints with different HTTP methods (GET, POST, PUT, DELETE)
- Search with pagination
- Aspect-based logging for controller methods
- In-memory H2 database with sample data and a JOIN query
- External API call (fetching https://www.google.com)
- Unit/integration tests

Run locally:

```bash
mvn spring-boot:run
```

H2 console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:accorddb`)

Postman collection: `postman_collection.json`

To push to GitHub (create repo first on GitHub), run:

```bash
git init
git add .
git commit -m "Initial Spring Boot app"
git branch -M main
# Add remote created on GitHub, for example:
git remote add origin https://github.com/<your-user>/accord-innnovation.git
git push -u origin main
```
