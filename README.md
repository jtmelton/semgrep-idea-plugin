# semgrep-idea-plugin

![Build](https://github.com/jtmelton/semgrep-idea-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/16133-semgrep-idea.svg)](https://plugins.jetbrains.com/plugin/16133-semgrep-idea)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/16133-semgrep-idea.svg)](https://plugins.jetbrains.com/plugin/16133-semgrep-idea)

## Description
The semgrep-idea-plugin is a simple wrapper tool for the [Semgrep](https://semgrep.dev/) tool (see [GitHub](https://github.com/returntocorp/semgrep)). 
This should allow you to work in your code environment as you normally would, and receive semgrep notifications in the IDE.

<!-- Plugin description -->
This is a simple wrapper for the semgrep open source tool. See https://github.com/jtmelton/semgrep-idea-plugin for more details.
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "semgrep-idea-plugin"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/jtmelton/semgrep-idea-plugin/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Configuration

- semgrep-wrapper:

  At the moment you need to create a wrapper for the semgrep tool. This is necessary because the IDEA plugin 
  has issues with the python env. Having said that, this may change in the future. The key pieces here are that you 
  should: 
   - include the full path to semgrep 
   - add the json parameter - we expect to parse the json blob to report findings
   - expect 2 parameters: 1) rules file/directory, and 2) file to analyze
  
  ```USDR00328:semgrep-idea-plugin johnmelton$ cat semgrep-wrapper 
     #!/bin/sh
     
     export PATH="/usr/local/Cellar/semgrep/0.31.1/bin/:$PATH"
     
     if [ "$#" -ne 2 ]; then
         echo "You must enter exactly 2 command line arguments: the rules file/dir first, and the file to analyze second"
     fi
     
     /usr/local/Cellar/semgrep/0.31.1/bin/semgrep --config $1 $2 --json --error
     ```
- Binary path setting:

  To update this setting, 
  - Go to `CMD + ,` or `CTRL + ,` or `Intellij IDEA -> Preferences`
  - Go to `Tools`
  - Go to `Semgrep Settings`
  - Set "Binary / Run Script Location (fully qualified file path): "
                
  In this field you should enter the fully qualified path to the semgrep-wrapper file you created in the step above
  
- Binary path setting:
    
  To update this setting, 
  - Go to `CMD + ,` or `CTRL + ,` or `Intellij IDEA -> Preferences`
  - Go to `Tools`
  - Go to `Semgrep Settings`
  - Set "Rules Location (fully qualified file path): "
                  
  In this field you should enter the fully qualified path to the semgre rules file or a directory containing one or more semgrep rules files
---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
