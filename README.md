# E-Learning Platform - Microservices Architecture

Une plateforme complète d'apprentissage en ligne utilisant une architecture microservices avec Spring Boot.

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      API Gateway (8080)                     │
│              Spring Cloud Gateway + JWT Auth                 │
└────────┬───────────────┬──────────────┬─────────────────────┘
         │               │              │
    ┌────▼────┐  ┌──────▼────┐  ┌─────▼──────┐
    │   Auth   │  │  Course   │  │ Enrollment │
    │ Service  │  │  Service  │  │  Service   │
    │ (8081)   │  │  (8082)   │  │  (8083)    │
    └──────────┘  └───────────┘  └────────────┘
         │
    ┌────▼────────────────────────┐
    │  Notification Service (8084)│
    │    WebFlux Reactive         │
    └─────────────────────────────┘
         │
    ┌────▼─────────────┐
    │   RabbitMQ       │
    │ (Async Messages) │
    └──────────────────┘
         │
    ┌────▼──────────────┐
    │  Eureka Server    │
    │  (8761)           │
    │  Service Discovery│
    └───────────────────┘
```

## Technologies Utilisées

- **Spring Boot 3.1.5**: Framework principal
- **Spring Cloud 2022.0.4**: Services cloud
- **Spring Cloud Gateway**: API Gateway avec JWT
- **Eureka Server**: Service discovery
- **Spring Security**: JWT Authentication
- **RabbitMQ**: Communication asynchrone
- **Spring WebFlux**: Programmation réactive
- **Spring Data JPA**: ORM avec Hibernate
- **H2 Database**: Base de données en mémoire
- **Spring Cloud Contract**: Tests de contrat

## Prérequis

- Java 17+
- Maven 3.9+
- RabbitMQ (optionnel, mais recommandé)

## Installation et Configuration

### 1. Installation des dépendances

```bash
# Java 17
sudo apt-get install openjdk-17-jdk

# Maven
sudo apt-get install maven

# RabbitMQ (optionnel)
sudo apt-get install rabbitmq-server
sudo systemctl start rabbitmq-server
```

### 2. Cloner et construire le projet

```bash
cd /home/dell/Bureau/projetDevAvance/elearning-platform
mvn clean install -DskipTests
```

## Démarrage des Services

### Automatique (avec script)

```bash
chmod +x start.sh
./start.sh
```

### Manuel (sans Docker)

Ouvrez 6 terminaux différents et lancez chaque service:

**Terminal 1 - Eureka Server:**
```bash
cd eureka-server
mvn spring-boot:run
```

**Terminal 2 - Auth Service:**
```bash
cd auth-service
mvn spring-boot:run
```

**Terminal 3 - Course Service:**
```bash
cd course-service
mvn spring-boot:run
```

**Terminal 4 - Enrollment Service:**
```bash
cd enrollment-service
mvn spring-boot:run
```

**Terminal 5 - Notification Service:**
```bash
cd notification-service
mvn spring-boot:run
```

**Terminal 6 - API Gateway:**
```bash
cd api-gateway
mvn spring-boot:run
```

## Endpoints API

### Authentication
```
POST   /auth/register          - Enregistrer un nouvel utilisateur
POST   /auth/login             - Connexion (retourne JWT token)
GET    /auth/users/{id}        - Obtenir les infos utilisateur
GET    /auth/validate          - Valider le JWT token
```

### Courses
```
GET    /api/courses            - Lister tous les cours
POST   /api/courses            - Créer un nouveau cours
GET    /api/courses/{id}       - Obtenir un cours
PUT    /api/courses/{id}       - Modifier un cours
DELETE /api/courses/{id}       - Supprimer un cours

GET    /api/courses/{courseId}/modules           - Lister modules d'un cours
POST   /api/courses/{courseId}/modules           - Ajouter un module
```

### Enrollments
```
POST   /api/enrollments/student/{id}/course/{courseId}  - S'inscrire à un cours
GET    /api/enrollments/{id}                            - Obtenir inscription
GET    /api/enrollments/student/{studentId}            - Inscriptions d'un étudiant
GET    /api/enrollments/course/{courseId}              - Inscriptions pour un cours
PUT    /api/enrollments/{id}/complete                  - Marquer comme complété
PUT    /api/enrollments/{id}/cancel                    - Annuler l'inscription
```

### Notifications
```
GET    /api/notifications/{id}              - Obtenir une notification
POST   /api/notifications                   - Créer une notification
GET    /api/notifications/student/{id}      - Notifications d'un étudiant
GET    /api/notifications/student/{id}/unread - Notifications non lues
PUT    /api/notifications/{id}/read         - Marquer comme lue
```

## Exemple d'Utilisation

### 1. S'enregistrer

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "student1",
    "email": "student1@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "role": "STUDENT"
  }'
```

### 2. Se connecter

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "student1",
    "password": "password123"
  }'
```

### 3. Créer un cours

```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "title": "Spring Boot Microservices",
    "description": "Learn microservices architecture",
    "instructor": "John Smith"
  }'
```

### 4. Ajouter un module

```bash
curl -X POST http://localhost:8080/api/courses/1/modules \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "name": "Introduction à Spring Boot",
    "description": "Les bases de Spring Boot"
  }'
```

### 5. S'inscrire à un cours

```bash
curl -X POST http://localhost:8080/api/enrollments/student/1/course/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

## Architecture en Détail

### Spring Cloud Gateway
- Route les requêtes vers les services appropriés
- Applique le filtre JWT pour l'authentification
- Load balancing avec Eureka

### Service Discovery (Eureka)
- Enregistrement dynamique des services
- Health checks automatiques
- Load balancing côté client

### JWT Authentication
- Token généré lors de la connexion
- Validation à chaque requête via le gateway
- Informations utilisateur dans les headers

### RabbitMQ Messaging
- **Cours Service**: Publie des événements "ModuleCreated"
- **Notification Service**: Écoute les événements et crée des notifications

### Spring WebFlux
- Endpoints réactifs dans Notification et Course Services
- `/api/courses/reactive/{id}`
- `/api/notifications/reactive/student/{id}`

### Tests de Contrat
- Located in: `course-service/src/test/`
- Utilise RestAssured pour les contrats HTTP

## Structure du Projet

```
elearning-platform/
├── pom.xml                          # POM parent
├── start.sh                         # Script de démarrage
├── stop.sh                          # Script d'arrêt
├── README.md                        # Ce fichier
│
├── common/                          # Module commun
│   ├── pom.xml
│   └── src/main/java/.../
│       ├── dto/
│       ├── event/
│       └── security/
│
├── eureka-server/                   # Service Discovery
│   ├── pom.xml
│   └── src/
│
├── api-gateway/                     # API Gateway
│   ├── pom.xml
│   └── src/
│       ├── filter/
│       └── config/
│
├── auth-service/                    # Service d'authentification
│   ├── pom.xml
│   └── src/
│       ├── entity/
│       ├── repository/
│       ├── service/
│       └── controller/
│
├── course-service/                  # Service des cours
│   ├── pom.xml
│   └── src/
│       ├── entity/
│       ├── repository/
│       ├── service/
│       ├── controller/
│       └── test/
│
├── enrollment-service/              # Service d'inscription
│   ├── pom.xml
│   └── src/
│       ├── entity/
│       ├── repository/
│       ├── service/
│       └── controller/
│
└── notification-service/            # Service de notifications
    ├── pom.xml
    └── src/
        ├── entity/
        ├── repository/
        ├── service/
        └── controller/
```

## Connexions Inter-Services

### Course Service → Notification Service
- **Via RabbitMQ**: Envoi d'événements ModuleCreated
- **Pattern**: Publish-Subscribe asynchrone

### Enrollment Service → Course Service
- **Via OpenFeign**: Appels synchrones pour valider les cours
- **Pattern**: Request-Reply synchrone

## Configuration

### JWT Secret
Modifiez dans `auth-service/src/main/resources/application.yml`:
```yaml
app:
  jwtSecret: "votre-secret-de-32-caracteres"
  jwtExpiration: 86400000
```

### RabbitMQ
Configuration dans chaque service:
```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

## Arrêt des Services

### Avec script
```bash
chmod +x stop.sh
./stop.sh
```

### Manuel
```bash
# Tuer par port
lsof -t -i :8080 | xargs kill -9
lsof -t -i :8081 | xargs kill -9
# ... répéter pour les autres ports
```

## Logs

Les logs sont sauvegardés dans le dossier `logs/`:
```bash
tail -f logs/auth-service.log
tail -f logs/course-service.log
tail -f logs/notification-service.log
```

## Monitoring

### Eureka Dashboard
Accédez à: http://localhost:8761

### H2 Console (Auth Service)
Accédez à: http://localhost:8081/h2-console
```
URL: jdbc:h2:mem:authdb
User: sa
Password: (vide)
```

## Tests

### Exécuter les tests
```bash
mvn test
```

### Tests de contrat (Course Service)
```bash
cd course-service
mvn test
```

## Dépannage

### Erreur: Port déjà utilisé
```bash
# Trouver le processus utilisant le port
lsof -i :8080

# Tuer le processus
kill -9 <PID>
```

### Erreur: Eureka pas accessible
- Vérifier que Eureka Server est démarré sur le port 8761
- Vérifier la configuration `eureka.client.serviceUrl.defaultZone`

### Erreur: RabbitMQ pas accessible
- Installer RabbitMQ: `sudo apt-get install rabbitmq-server`
- Démarrer: `sudo systemctl start rabbitmq-server`
- Vérifier: `sudo systemctl status rabbitmq-server`

## Performance et Scalabilité

- **Réactivité**: WebFlux pour les opérations non-bloquantes
- **Communication Asynchrone**: RabbitMQ pour découpler les services
- **Service Discovery**: Eureka pour la scalabilité horizontale
- **Load Balancing**: Spring Cloud Gateway pour distribuer la charge

## Sécurité

- **JWT**: Authentification sans état
- **HTTPS Ready**: Configuration pour HTTPS en production
- **CORS**: Activé pour les tests
- **Validation**: Validation des DTOs avec annotations

## Améliorations Futures

- [ ] Ajouter Spring Cloud Config Server
- [ ] Impl menter Circuit Breaker (Hystrix/Resilience4j)
- [ ] Ajouter Spring Cloud Sleuth pour le tracing distribué
- [ ] Implémenter Eureka Client-side Load Balancing
- [ ] Ajouter Spring Cloud Stream pour plus d'options de messaging
- [ ] Implémenter caching avec Redis
- [ ] Ajouter OAuth2 pour une sécurité renforcée
- [ ] Implémenter API versioning

## Contributeurs

E-Learning Platform Development Team

## Licence

MIT License - Voir LICENSE file pour plus de détails
# e-learning-platforme
# e-learning
