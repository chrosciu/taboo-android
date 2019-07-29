taboo-android
=============

Taboo game for Android (Polish version)


###Preparing cards

**NOTICE: Before you run application a set of cards must be created. Following rules apply:**

* All cards must be stored in ```assets/cards``` folder
* Each card must be stored in separate file, named ```tabooX.txt``` where `X` is card number
* Cards must be numbered in a continuous manner starting from 1. For example if you have 50 cards their files should be named as: ```taboo1.txt```, ```taboo2.txt```, ... ```taboo50.txt```
* Card file must contain exactly one line in following format: ```word;taboo1;taboo2;taboo3;taboo4;taboo5```
    * ```word``` is word that must be guesssed
    * ```taboo1```, ... ```taboo5``` are words that are not allowed (taboos)
    * ```word``` and and taboos must be separated with semicolons
* Example card is stored in file ```tabooExample.txt```

**WARNING: Disregarding of rules mentioned above will result in application crash.**
