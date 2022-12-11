# ------------- WIP ------------------
### Hours worked : 12

TODO :
- tests

NICE TO HAVE :
- A cache strategy
- keystore management
- Analytics
- CICD
- THEME Management
- UI
- list of ingredients instead of string

# ------------- WIP ------------------

# Food facts app !

Description here

## Architecture
### MVVM
The application uses an MVVM architecture which is the most common one (in Android apps), the basic components of this architecture are :
- The model: Containing the data of the application
- The view: What will be shown on the screen, what the user will interact with
- The viewmodel: acting like a bridge between the model and the view. It will be able to get the data and transform it so it can be used by the view. The view will be subscribed to it to be updated when needed.

MVVM is the recommended pattern for Android app.

### Modules
In order to respect the "separation of concerns" concept, this app is separated in 3 modules :
- The data module, exposing repositories and handling how the data is retrieved.
- The domain module, supposed to encapsulate business logic.
- The ui module, displaying the data on the screen, containing all the Android specific related code.
The separation of these 3 modules also helps us to make the app testable. We can independently test each layer.

### Flow
I chose to use Kotlin Flow (over live data / Rx) for async tasks.
I prefer Flow over LiveData mostly because LiveData is bound to Android while we can use Flow everywhere as it comes from Kotlin (like in our domain and data modules).
Rx is a great reactive solution too, I simply had to make a choice ;)

## Libraries

## Libraries
### Hilt
Hilt (dagger) for the dependency injection, reducing boilerplate code and making testing easier. (Alternative I used : Koin)

### MockK
MockK in order to mock classes while testing. (Alternative: Mockito)

### Espresso
///// todo

### Retrofit (and Gson)
Probably the best REST client to easily implement network calls.
Works super well with Gson, to serialize and deserialize classes in Json

### Glide
Helping to load images from url (And transforms it if we need)

## Working flow
### Git Flow
I am used to work in a team following git flow concepts, it means defining some specific branches with roles :
- master : the production branch, containing the code currently in production.
- develop : the branch on which we implement new features, contains the latest development changes.
- features : we create feature branches from dev to implement new features that will then be merged on dev (see name convention below).
- release : When ready we build a release candidate from dev and we start some rollout (alpha, beta... depending on the strategy). Once the rollout is completed, we merge it on master and dev.
- hotfix : If we need to fix a bug quickly, we can branch from master, do our changes and merge back on master and dev.

We usually tag branches with some convention name in order to trigger actions on the CI.

### Convention
In this project, the feature branches have been named : 'ANDX-name-of-feature'
The goal is to have a unique 'X' id for every feature so we keep track of who is working on which issue and what is the status (When working in agile team with a kanban board)

## Areas of improvment
-cache using room