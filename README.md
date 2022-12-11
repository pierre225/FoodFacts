# Food facts app !
The food facts app is a demo app that can retrieved product information from the OpenFoodFact api from the bar code of the product.
You can either type the bar code or scan it.

It took approximately 2.5 working days to build the app and with more time it could be improved (see last section "Areas of improvement")

## Architecture
### MVVM
The application uses an MVVM architecture which is the most common one (in Android apps), the basic components of this architecture are :
- The model: Containing the data of the application
- The view: What will be shown on the screen, what the user will interact with
- The viewmodel: acting like a bridge between the model and the view. It will be able to get the data and transform it so it can be used by the view. The view will be subscribed to it to be updated when needed.

MVVM is the recommended pattern for Android app.

### Modules
In order to respect the "separation of concerns" concept, this app is separated in 3 modules:
- The data module, exposing repositories and handling how the data is retrieved.
- The domain module, supposed to encapsulate business logic.
- The ui module, displaying the data on the screen, containing all the Android specific related code.
The separation of these 3 modules also helps us to make the app testable. We can independently test each layer.

### Flow
I chose to use Kotlin Flow (over live data / Rx) for async tasks.
I prefer Flow over LiveData mostly because LiveData is bound to Android while we can use Flow everywhere as it comes from Kotlin (like in our domain and data modules).
Rx is a great reactive solution too, I simply had to make a choice ;)

## Working flow
### Git Flow
I am used to working in a team following git flow concepts, it means defining some specific branches with roles:
- master: the production branch, containing the code currently in production.
- develop: the branch on which we implement new features, contains the latest development changes.
- features: we create feature branches from dev to implement new features that will then be merged on dev (see name convention below).
- release: when ready we build a release candidate from dev and we start some rollout (alpha, beta... depending on the strategy). Once the rollout is completed, we merge it on master and dev.
- hotfix: if we need to fix a bug quickly, we can branch from master, do our changes and merge back on master and dev.

We usually tag branches with some convention name in order to trigger actions on the CI.

### Convention
In this project, the feature branches have been named: 'ANDX-name-of-feature'
The goal is to have a unique 'X' id for every feature so we keep track of who is working on which issue and what is the status (When working in agile team with a kanban board)

## Libraries
### Hilt
Hilt (dagger) for the dependency injection, reducing boilerplate code and making testing easier. (Alternative: Koin)

### Camera
Androix camera2, camera-lifecycle and camera-view in order to show a camera view in our project that is bound to a lifecycle

### MlKit
MlKit barcode scanning to recognize and read bar code from our camera images

### MockK
MockK in order to mock classes while testing. (Alternative: Mockito)

### Espresso
Espresso library for UI tests, checking views visibility when ui events are triggered

### Retrofit (and Gson)
Probably the best REST client to easily implement network calls.
Works super well with Gson, to serialize and deserialize classes in Json

### Glide
Helping to load images from url (And transforms it if we need)

## Areas of improvment

### CICD - channels
Implement a CICD tool (Jenkins, Bitrise...) in order to build and test and deploy the app efficiently.
We could also configure some channels (Alpha, Beta) to have some internal or external testing on each features / releases

### Keystore
The app doesn't have any keystore strategy while it is very important for an Android app. Imo the best solution is to let the CICD handle the Keystore so only it can build a release of the app.

### Branch rules
We should define some branch rules on git to make sure nothing is pushed by accident

### A cache strategy
Our repository could have a cache strategy. We could preload the DB or save some response to not always use the remote api

### Some logging and event tools
An event tool is always useful to understand how our users interact with the app.
We can use pre built tools like Firebase to centralize and monitor the user actions.

### An improved model
We could have parsed more data from the OFF api, also the ingredients list could be a list for a better way of displaying it

### UI
A more elaborated UI... :), a more adaptive dark mode, some animations (maybe using Lottie?), better landscape UI, and a real launcher icon :D

### Lint
Add some lint checks to avoid mistakes

### Codestyle
Define some common codestyle rules for readability, consistency

### More UI and Unit test
Testing more each states. Also better UI async tests using Idling resources, and manage the permissions

## Thank you for your time, reading this and the code!!!