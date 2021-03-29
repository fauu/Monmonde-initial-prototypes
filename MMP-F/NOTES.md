- Increase the probability of League admission for countries in a region every time a country from this region gets admitted.

# Trainer population control
12.06.2018
- A global factor concerned only with the number of NEW rankingEntrants, still scaled according to
  country's name count.
Country's local factor removed -- replaced by a completely random value.
## Retirement:
1. Take a couple of rankingEntrants at random, every day.
2. Calculate probability for retiring based on age (later results, personality, etc.) and do RNG.
3. Tune the numbers by trial-and-error.
This should be enough for now.

14.06.2018
RatingPeriod to use RankingEntrant (or similar) interface, not the concerte Trainer class
Implement tests first before debugging

# Kotlin
- Provider<T>
  https://blog.kotlin-academy.com/effective-java-in-kotlin-item-1-consider-static-factory-methods-instead-of-constructors-8d0d7b5814b2
- asSequence() faster when more than one processing step
  https://blog.kotlin-academy.com/effective-kotlin-use-sequence-for-bigger-collections-with-more-than-one-processing-step-649a15bb4bf
- Parametrized tests
  https://blog.kotlin-academy.com/parametrized-tests-with-spek-e0e02d5766a
- Scoping functions
  | Function | Access to caller |         Returns |
  |----------|------------------|-----------------|
  |    apply |             this |	caller (this) |
  |     also |               it |     caller (it) |
  |      run |             this |    block result |
  |      let |               it |    block result |
- https://github.com/korlibs/kds - Extra Data Structures
- https://github.com/hankdavidson/ktime
- disable thread safety when using `by lazy` to avoid overhead
- @JvmName for circumventing type erasure in overloads
