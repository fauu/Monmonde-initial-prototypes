- Increase the probability of League admission for countries in a region every time a country from this region gets admitted.

# Trainer population control
12.06.2018
- A global factor concerned only with the number of NEW rankingEntrants, still scaled according to
  country's name count.
Country's local factor removed — replaced by a completely random value.
## Retirement:
1. Take a couple of rankingEntrants at random, every day.
2. Calculate probability for retiring based on age (later results, personality, etc.) and do RNG.
3. Tune the numbers by trial-and-error.
This should be enough for now.

14.06.2018
RatingPeriod to use RankingEntrant (or similar) interface, not the concerte Trainer class
Implement tests first before debugging
