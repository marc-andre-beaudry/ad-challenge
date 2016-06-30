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
- Subscription create (/api/v1/subscription/notification/create?url={eventUrl})
- Subscription change (/api/v1/subscription/notification/change?url={eventUrl})
- Subscription cancel (/api/v1/subscription/notification/cancel?url={eventUrl})
- Subscription status (/api/v1/subscription/notification/status?url={eventUrl})
- User assigned (/api/v1/subscription/notification/user/assign?url={eventUrl})
- User unassigned (/api/v1/subscription/notification/user/unassign?url={eventUrl})

- OAuth incoming request validation (otherwise denied 403)
- Admin read-only endpoint to view users + account subscriptions (/admin/v1/user, /admin/v1/subscription, etc.)
