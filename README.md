# Maven project skeleton for using Boot from IntelliJ with Cursive

Temporary solution for the
 [Add basic support for boot #692](https://github.com/cursive-ide/cursive/issues/692)
 issue.
 
The `pom.xml` is the only source of dependencies.

IntelliJ has built-in support for editing `pom.xml` with auto-completion.


## Allow Cursive to resolve to resolve symbols in `build.boot`
 
If we open the `build.boot` symlink under `src/`, IntelliJ won't complain about
the file not being *under a source root*.


## Opening for the first time from IntelliJ

1. *File / New / Project from Existing Sources...*
2. ...
3. For the `src/` directory: *Mark Directory as... / Sources route* 


## Running once

```
boot run
```

## Running with REPL

```
boot dev
```
