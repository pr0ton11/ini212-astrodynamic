# Astrodynamic

Must have from test standpoint
- [] Documentation
    - [] User guide with Goal
    - [] Technical documentation
        - [] Reason for implementation choice and place for each requirement thema
- [] Unitests
    - [] No class without unit test
- [] Enum
    - [] SI Units for conversion
- [] Inheritance
    - [] Base class between Planetoid, Satellite and Spaceship
- [] Casting
- [] Interface
    - [] Per physic calculation
        - [] Gravitation
        - [] Atmosphere
        - [] Affected by Gravitation
        - [] Affected by Drag
- [] Collections
    - [] Scenario list
    - [] Physic object list
- [] Sorting
    - [] Scenario sorting by Name, Tags, Text in Description
- [] Serialization
    - [] JSONB Scenarios with subobjects
    - [] Save state
- [] Exception handling
- [] Log file
    - [] With log levels
- [] Maven build
- [] Executable

Must have from project standpoint
- [] Gravitational calculation
- [] Drag calculation
- [] Scenario
- [] Planetoid
- [] Spaceship

Optional
- [] Predefined objects loadable from json
- [] Lift calculation
- [] Air breathing engines
- [] Fuel usage
- [] Split ship into components
- [] Calculation performance
    - [] Threading
    - [] Oct-Tree
