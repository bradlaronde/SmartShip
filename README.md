# Coding exercise

Write an Android application that displays a list of drivers, using the supplied json file as input.
When one is selected from the list display the correct shipment destination to that driver in a way
that maximizes the total suitability score over the set of drivers. Each driver can only have one
shipment and each shipment can only be offered to one driver.

## Calculating the suitability score

* If the length of the shipment's destination street name is even, the base suitability score
  (SS) is the number of vowels in the driver’s name multiplied by 1.5.
* If the length of the shipment's destination street name is odd, the base SS is the number
  of consonants in the driver’s name multiplied by 1.
* If the length of the shipment's destination street name shares any common factors
  (besides 1) with the length of the driver’s name, the SS is increased by 50% above the
  base SS.

## Building the app

1. Clone this repository
1. Open the project in Android Studio (Electric Eel 2022.1.1 Patch 2)
1. Select Build, Make Project