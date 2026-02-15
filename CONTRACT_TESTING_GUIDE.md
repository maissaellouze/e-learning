# Spring Cloud Contract - Test de Contrat entre Microservices

Ce document décrit comment exécuter les tests de contrat entre le producteur (`notification-service`) et le consommateur (`enrollment-service`).

## Vue d'ensemble

Les tests de contrat vérifient que:
1. Le **producteur** (NotificationService) fournit l'API selon les spécifications du contrat
2. Le **consommateur** (EnrollmentService) peut appeler l'API sans démarrer le producteur réel

## Structure des fichiers

```
notification-service/
  └── src/test/
      ├── resources/
      │   └── contracts/
      │       └── notification_send_contract.groovy    # Définition du contrat
      └── java/com/elearning/notification/
          ├── NotificationContractTest.java            # Test du producteur
          └── BaseClass.java                           # Base pour les tests générés

enrollment-service/
  └── src/test/java/com/elearning/enrollment/
      └── NotificationServiceConsumerContractTest.java # Test du consommateur
```

## Contrat

Le contrat est défini dans `notification-service/src/test/resources/contracts/notification_send_contract.groovy`:

```groovy
Request:
  Method: POST
  URL: /notifications
  Headers: Content-Type: application/json
  Body: { userId: 123, message: "Hello Notification" }

Response:
  Status: 201 Created
  Headers: Content-Type: application/json
  Body: { id: 1, userId: 123, message: "Hello Notification", status: "SENT" }
```

## Étapes d'exécution

### Étape 1: Lancer les tests du producteur

Les tests du producteur vérifient que l'API respecte le contrat:

```bash
cd notification-service
mvn clean test
```

Cela exécute `NotificationContractTest.java` qui:
- Appelle POST `/api/notifications` avec les paramètres du contrat
- Vérifie que la réponse est HTTP 201
- Vérifie que le corps de la réponse contient les champs définis dans le contrat

### Étape 2: Installer le producteur localement

Pour que le consommateur puisse accéder aux stubs:

```bash
cd notification-service
mvn clean install
```

### Étape 3: Lancer les tests du consommateur

Les tests du consommateur vérifient que le client peut appeler le service sans démarrer le producteur:

```bash
cd enrollment-service
mvn clean test
```

Cela exécute `NotificationServiceConsumerContractTest.java` qui:
- Démarre un serveur WireMock (stub) sur le port 8888
- Configure le stub pour simuler la réponse de NotificationService
- Teste que le client peut faire l'appel et traiter la réponse
- Vérifie que l'API consommée respecte le contrat

## Approche utilisée

Nous utilisons **WireMock** pour simuler les réponses du service. Cela permet au consommateur de tester sans démarrer le producteur réel.

### Client Feign

Le consommateur utilise un client Feign pour appeler NotificationService:

```java
@FeignClient(name = "notification-service", 
             url = "${notification-service.url:http://localhost:8080}")
public interface NotificationServiceClient {
    @PostMapping("/api/notifications")
    ResponseEntity<NotificationResponse> sendNotification(
        @RequestParam("studentId") Long studentId,
        @RequestParam("message") String message,
        @RequestParam("type") String type);
}
```

### Stub WireMock

Le test configure WireMock pour stubifier les réponses:

```java
stubFor(post(urlEqualTo("/api/notifications"))
    .willReturn(aResponse()
        .withStatus(201)
        .withBody(...)));
```

## Commandes utiles

### Tester le producteur uniquement
```bash
mvn -pl notification-service clean test
```

### Tester le consommateur uniquement
```bash
mvn -pl enrollment-service clean test
```

### Construire tous les modules
```bash
mvn clean install
```

### Voir les logs détaillés
```bash
mvn -pl notification-service clean test -X
```

## Résumé du flux

1. **Producteur** expose une API `/api/notifications` (POST)
2. **Contrat** définit le format de requête/réponse attendu
3. **Test du producteur** vérifie que l'API implémente le contrat
4. **Test du consommateur** utilise WireMock pour simuler l'API et tester l'intégration
5. Le consommateur n'a **jamais besoin de démarrer le producteur réel** pendant les tests

## Avantages

✅ Tests rapides (pas de démarrage de services)
✅ Tests fiables (pas de dépendances externes)
✅ Détection précoce d'incompatibilités entre services
✅ Facilite la documentation et la collaboration
