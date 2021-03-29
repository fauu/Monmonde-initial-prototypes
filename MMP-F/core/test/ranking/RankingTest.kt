package com.github.fauu.monmonde.sim.ranking

import org.amshove.kluent.`should be in range`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class RankingEntrantImpl(private val id: Int, override val rating: Rating) : RankingEntrant {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as RankingEntrantImpl

    if (id != other.id) return false

    return true
  }

  override fun hashCode() = id
}

object RankingSpec : Spek({
  describe("RatingPeriod") {
    val rankingPeriod = RatingPeriod(0.5)

    on("rating") {
      val player = RankingEntrantImpl(0, Rating(1500.0, 200.0, 0.06))
      val opponents = arrayOf<RankingEntrant>(
        RankingEntrantImpl(1, Rating(1400.0, 30.0, 0.06)),
        RankingEntrantImpl(2, Rating(1550.0, 100.0, 0.06)),
        RankingEntrantImpl(3, Rating(1700.0, 300.0, 0.06))
      )
      val results = listOf(
        Pair(player, opponents[0]),
        Pair(opponents[1], player),
        Pair(opponents[2], player)
      )
      rankingPeriod.run {
        registerEntrants(listOf(player, *(opponents)))
        results.forEach { registerResult(it.first, it.second) }
        rate()
      }

      it("should correctly update RankingEntrants' rating values") {
        val expected = Rating(1464.06, 151.52, 0.05999)
        val delta = 0.01
        player.rating.also {
          it.value.`should be in range`(
            expected.value - delta,
            expected.value + 1
          )
          it.deviation.`should be in range`(
            expected.deviation - delta,
            expected.deviation + delta
          )
          it.volatility.`should be in range`(
            expected.volatility - delta,
            expected.volatility + delta
          )
        }
      }
    }
  }
})
