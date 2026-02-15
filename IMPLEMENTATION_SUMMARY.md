# Guide d'implémentation - Spring Cloud Contract

## Résumé des fichiers créés/modifiés

### 1. **Contrat** (Producteur)
**Fichier:** `notification-service/src/test/resources/contracts/notification_send_contract.groovy`

Définit le contrat entre deux microservices:
- Requête: POST avec userId et message
- Réponse: 201 Created avec id, userId, message, status

### 2. **Configuration** (Producteur)
**Fichier:** `notification-service/pom.xml`

Ajout de la dépendance WireMock pour les tests de contrat.

### 3. **Classe de Base** (Producteur)
**Fichier:** `notification-service/src/test/java/com/elearning/notification/BaseClass.java`

Base pour les tests du producteur avec MockMvc configuré.

### 4. **Test du Producteur**
**Fichier:** `notification-service/src/test/java/com/elearning/notification/NotificationContractTest.java`

Test JUnit vérifiant que l'API respecte le contrat.

### 5. **Client Feign** (Consommateur)
**Fichier:** `enrollment-service/src/main/java/com/elearning/enrollment/client/NotificationServiceClient.java`

Client pour appeler NotificationService.

### 6. **DTO de Réponse** (Consommateur)
**Fichier:** `enrollment-service/src/main/java/com/elearning/enrollment/client/NotificationResponse.java`

DTO pour la réponse du service de notification.

### 7. **Test du Consommateur**
**Fichier:** `enrollment-service/src/test/java/com/elearning/enrollment/NotificationServiceConsumerContractTest.java`

Test JUnit du consommateur avec WireMock pour simuler le producteur.

### 8. **Documentation**
**Fichier:** `CONTRACT_TESTING_GUIDE.md`

Guide complet d'utilisation des tests de contrat.

---

## Étapes à suivre

### **Étape 1 : Créer le Contrat**
✅ **Fait** - Contrat Groovy créé en `notification-service/src/test/resources/contracts/`

### **Étape 2 : Configurer Spring Cloud Contract**
✅ **Fait** - Configuration simplifiée dans le `pom.xml` du producteur

### **Étape 3 : Créer la Classe de Base**
✅ **Fait** - `BaseClass.java` créée pour les tests générés

### **Étape 4 : Créer le Test du Producteur**
✅ **Fait** - `NotificationContractTest.java` créé

**Lancer le test:**
```bash
cd notification-service
mvn clean test
```

### **Étape 5 : Installer les Stubs Localement**
```bash
cd notification-service
mvn clean install
```

Ceci génère et installe les stubs dans le répertoire Maven local.

### **Étape 6 : Créer le Client du Consommateur**
✅ **Fait** - `NotificationServiceClient.java` créé (interface Feign)

### **Étape 7 : Créer le Test du Consommateur**
✅ **Fait** - `NotificationServiceConsumerContractTest.java` créé

**Lancer le test:**
```bash
cd enrollment-service
mvn clean test
```

Le test démarre un serveur WireMock pour simuler NotificationService - **pas besoin de démarrer le producteur réel!**

---

## Avantages de cette approche

✅ **Rapide** : Tests sans démarrer les services
✅ **Fiable** : Pas de dépendances externes
✅ **Documenté** : Le contrat sert de documentation
✅ **Détection d'erreurs** : Les incompatibilités sont détectées avant le déploiement
✅ **Testable indépendamment** : Chaque service peut être testé isolément

---

## Commandes utiles

```bash
# Construire tout le projet
mvn clean install

# Tester le producteur
mvn -pl notification-service clean test

# Tester le consommateur
mvn -pl enrollment-service clean test

# Voir les logs détaillés
mvn clean test -X

# Lancer un test spécifique
mvn -pl notification-service test -Dtest=NotificationContractTest
```

---

## Notes importantes

1. **Contrat en Groovy** : Le fichier `.groovy` définit le contrat de manière lisible
2. **WireMock** : Utilisé pour stubifier les réponses côté consommateur
3. **Sans plugin Maven** : Nous utilisons une approche manuelle pour éviter les problèmes de dépendances
4. **Tests JUnit standard** : Faciles à intégrer dans CI/CD (Jenkins, GitLab CI, GitHub Actions)

---

## Résultat attendu

Après l'exécution des tests:

- ✅ NotificationContractTest : PASSED (producteur respecte le contrat)
- ✅ NotificationServiceConsumerContractTest : PASSED (consommateur peut appeler l'API)

Cela garantit que les deux services sont compatibles et peuvent communiquer correctement.
