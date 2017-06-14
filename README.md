# local-db-service

## Tables
### User
#### Requests
##### GetUserByToken(token) -> Usata per query esiste utente
##### GetUser(token) -> Nome, cognome, last height, last weight
##### NewUser(token) -> 
##### SetUserName(token, name) ->
##### SetUserSurname(token, surname) ->

### Measure
##### getLast10Measures(token, type)
##### getLastMeasure(token, type)
##### updateMeasure(token, type, value) -> add measure ricordare data

### MeasureType
##### getTypes()

### Goal
##### getGoals(token) -> n goal quanti i tipi
##### setGoal(token, type, goaltype, value) -> overwrite the previous goal of that type
##### getGoal(token, type)

### GoalType
##### getGoalType()
