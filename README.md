# BlueCow problem (The ‘Round the World Challenge’)

Solution (algorithm) developed by myself for the Traveler Salesman Problem. This was the final assignment of the module COM627 - Computability and Optimisation in Wrexham Glyndŵr University (United Kingdom).

### Overview: The ‘Round the World Challenge’
The energy drink company, Blue Cow, is organising a round-the world aeroplane race. Any country’s capital can bid to be one of the stage points around the tour; most do. So Blue Cow will have well over 100 offers of money but they can’t take all of them. They need optimisation software, which will find the tour that maximises their income – subject to the following restrictions:

The tour starts and finishes in the same city (it’s a circuit)

There’s a minimum and maximum number of cities in the tour (30-50 in 2017)

There’s a minimum and maximum total distance for the tour (in miles or km)

There’s a minimum and maximum distance for each flight between cities (m/km)

Flying directly between some cities isn’t permitted

The tour must have a minimum and maximum number of cities in each of the main continents:
- Europe
- Asia
- Africa
- Australasia
- South America
- North America

The actual values for all of these minima and maxima vary from year to year so the optimisation algorithm must be flexible enough to solve Blue Cow’s problem each time.
