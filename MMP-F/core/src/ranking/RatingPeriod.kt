package com.github.fauu.monmonde.sim.ranking

import java.lang.Math.exp
import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.sqrt

data class RatingPeriodEntrantResult(
  val score: Double,
  val opponent: RankingEntrant
)

data class RatingPeriodEntrantRecord(
  val initialRating: Glicko2ScaledRating,
  val results: MutableList<RatingPeriodEntrantResult> = mutableListOf()
)

private const val WIN_SCORE = 1.0
private const val LOSS_SCORE = 0.0

class RatingPeriod(private val tau: Double) {
  private val tauSq = pow(tau, 2.0)

  private val entrantRecords = mutableMapOf<RankingEntrant, RatingPeriodEntrantRecord>()

  fun registerEntrants(entrants: List<RankingEntrant>) {
    entrants.forEach {
      entrantRecords.putIfAbsent(it, RatingPeriodEntrantRecord(Glicko2ScaledRating(it.rating)))
    }
  }

  fun registerResult(winner: RankingEntrant, loser: RankingEntrant) {
    entrantRecords.run {
      this[winner]!!.results.add(RatingPeriodEntrantResult(WIN_SCORE, loser))
      this[loser]!!.results.add(RatingPeriodEntrantResult(LOSS_SCORE, winner))
    }
  }

  fun rate() {
    entrantRecords.forEach { entrant, entry ->
      if (entry.results.size == 0) {
        val (_, phi, sigma) = entry.initialRating
        entrant.rating.deviation = CONVERSION_COEFFICIENT * sqrt(pow(phi, 2.0) + pow(sigma, 2.0))
        return@forEach
      }
      val (mu, phi, sigma) = entry.initialRating

      val v = v(entry.results, mu)
      val delta = delta(entry.results, mu, v)

      val a = ln(pow(sigma, 2.0))
      val epsilon = 0.000001
      val deltaSq = pow(delta, 2.0)
      val phiSq = pow(phi, 2.0)
      val sigmaPrime = sigmaPrime(a, deltaSq, phiSq, v, epsilon)

      val phiStar = sqrt(phiSq + pow(sigmaPrime, 2.0))
      val phiPrime = 1 / sqrt((1 / pow(phiStar, 2.0)) + (1 / v))
      val muPrime = muPrime(entry.results, mu, phiPrime)

      entrant.rating.run {
        value = CONVERSION_COEFFICIENT * muPrime + 1500
        deviation = CONVERSION_COEFFICIENT * phiPrime
        volatility = sigmaPrime
      }
    }
  }

  private fun v(results: MutableList<RatingPeriodEntrantResult>, mu: Double): Double {
    return 1 / results.sumByDouble {
      val (oMu, oPhi) = entrantRecords[it.opponent]!!.initialRating
      val E = E(mu, oMu, oPhi)
      pow(g(oPhi), 2.0) * E * (1 - E)
    }
  }

  private fun delta(
    results: MutableList<RatingPeriodEntrantResult>,
    mu: Double,
    v: Double
  ): Double {
    return v * results.sumByDouble {
      val (oMu, oPhi) = entrantRecords[it.opponent]!!.initialRating
      g(oPhi) * (it.score - E(mu, oMu, oPhi))
    }
  }

  private fun sigmaPrime(
    a: Double,
    deltaSq: Double,
    phiSq: Double,
    v: Double,
    epsilon: Double
  ): Double {
    var A = a
    var B = if (deltaSq > phiSq + v) {
      ln(deltaSq - phiSq - v)
    } else {
      var k = 1
      while (f(a - k * tau, deltaSq, phiSq, v, a) < 0) {
        k++
      }
      a - k * tau
    }

    var fa = f(A, deltaSq, phiSq, v, a)
    var fb = f(B, deltaSq, phiSq, v, a)
    while (abs(B - A) > epsilon) {
      val C = A + ((A - B) * (fa / (fb - fa)))
      val fc = f(C, deltaSq, phiSq, v, a)
      if (fc * fb < 0) {
        A = B
        fa = fb
      } else {
        fa /= 2
      }
      B = C
      fb = fc
    }

    return exp(A / 2)
  }

  private fun muPrime(
    results: MutableList<RatingPeriodEntrantResult>,
    mu: Double,
    phiPrime: Double
  ): Double {
    return mu + (pow(phiPrime, 2.0) * results.sumByDouble {
      val (oMu, oPhi) = entrantRecords[it.opponent]!!.initialRating
      g(oPhi) * (it.score - E(mu, oMu, oPhi))
    })
  }

  private fun E(mu: Double, oMu: Double, oPhi: Double) = 1 / (1 + exp(-g(oPhi) * (mu - oMu)))

  private fun g(phi: Double) = 1 / sqrt(1 + (3 * pow(phi, 2.0)) / pow(Math.PI, 2.0))

  private fun f(x: Double, deltaSq: Double, phiSq: Double, v: Double, a: Double): Double {
    val expX = exp(x)
    return (
        ((expX * (deltaSq - phiSq - v - expX)) / (2 * pow((phiSq + v + expX), 2.0)))
            - ((x - a) / tauSq)
        )
  }
}
