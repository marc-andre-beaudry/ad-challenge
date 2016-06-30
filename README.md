# App Direct Challenge
This is the App Direct Backend Coding Challenge, 2016 edition.

# Requirements
- Java 8
- Maven
- A configured AppDirect product with :
  - consumer key + consumer secret
  - subscriptions + access management 
  
# How to run
1. Clone the repository
2. From the directory root, run maven install.
3. After a successful build, go to the target directory.
4. Start the java application with -> java -Dchallengeapi.consumer.key=CONSUMER_KEY -Dchallengeapi.consumer.secret=CONSUMER_SECRET -jar appdirect-challenge-1.0.0.jar where CONSUMER_KEY and CONSUMER_SECRET are provided from the AppDirect's dashboard.

# Implemented
- Subscription create
- Subscription change
- Subscription cancel
- Subscription status

- User assigned
- User unassigned

- OAuth incoming request validation (otherwise denied 403)
- Admin read-only endpoint to view users + account subscriptions
