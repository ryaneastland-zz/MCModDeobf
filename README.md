MCModDeobf
==========
MCModDeobf is a java program I have written directed for use at Minecraft Mod Developers.
This program Decompiles and Deobfuscates any Minecraft code you specify.
This is handy when you have something which you want to see how it works.

Current Version
---------------
2.0.0 Beta

Changelog
---------
* Added support for multiple Minecraft versions (1.6.4, 1.7.2, 1.7.10)
* Using Procyon Decompiler for decompiling code
* Created custom deobfuscater using MCP csv files

How to build
------------
Run
```
gradle clean build uberjar
```
inside the project root, you can find the final jar in build/libs/

License
-------
MCModDeobf is licensed under the MIT license which can be found in the root of this projects source
