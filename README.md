# Simple blockchain example for messaging

This is an simple implementation for a blockchain usage for a ciphered messaging with java. 
For more informations about blockchain implementation i suggest to read this blog tutorial from Kass.
 
 * [Creating Your First Blockchain with Java. Part 1](https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa)
 * [Creating Your First Blockchain with Java. Part 2 — Transactions](https://medium.com/programmers-blockchain/creating-your-first-blockchain-with-java-part-2-transactions-2cdac335e0ce)
 
Simple use any IDE with gradle support or build it from terminal with.

```
For Windows
.\gradlew.bat run

For Linux
./gradlew run
```

This example generates three ciphered messages which will be stored to a blockchain messaging list. 
Ciphered messages are ciphered from public key with an prime256v1 elliptic curve.

## License

Copyright 2018 Andreas Sekulski

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.