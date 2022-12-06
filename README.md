# Food facts app !

Description here

## Architecture
## Libraries


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
