# BrainySnake


## Build Project for IntelliJ
1. checkout project
1. run _gradlew idea_ or for reinit _gradlew cleanidea idea_ in your favorite console
1. open the generated .ipr file

## Create Desktop Run-Configuration in IntelliJ
1. create a knew Application Configuration
1. set _de.adesso.brainysnake.desktop.DesktopLauncher_ as Main class
1. set _[projectPath]\BrainySnake\core\assets_ as Working directory
1. set _desktop_ as classpath of module
1. run