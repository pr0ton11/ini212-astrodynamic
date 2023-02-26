# Style guide

* Writing style should be CamelCase like java default.
* Interfaces are named like a normal class without linting.

example: Loggable for a logging interface instead of ILoggable

# Comment Template

Comments that are required for the whole project

## Header comment

After the imports, but before the class header

``` java
/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */
```

## Function / Class comment

Will be written before the class/method definition. 

``` java
// This function is used to write log files to the disk
@someDecorator
public void writeLog(String msg) { ... }
```
