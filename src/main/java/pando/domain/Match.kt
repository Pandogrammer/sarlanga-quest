package pando.domain

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import pando.actions.Action
import pando.actions.ActionDie
import pando.actions.Attack
import pando.creatures.*
import pando.creatures.CreatureCode.*
import pando.creatures.cards.CreatureCard
import pando.creatures.races.*
import pando.turns.*
import kotlin.random.Random

class Match(playerCreatures: Map<Position, CreatureCard>,
            opponentCreatures: Map<Position, CreatureCard>,
            actionDie: ActionDie = ActionDie()) {

    private val creatureAction = ExecuteAction(actionDie)
    private val restingTurn = RestingTurn()
    private val nextTurn = NextTurn()
    private val winnerValidation = WinnerValidation()

    private val actionsExecuted = creatureAction.executed
    private val rests = restingTurn.executed
    private val damage = actionsExecuted.filter { it.roll >= it.actor.stats.dexterity }.map { DamageEvent(it.actor, it.action, it.target) } //asco
    private val kills = actionsExecuted.filter { it.target.health() == 0 }.map { Kill(it.actor) }
    private val deaths = actionsExecuted.filter { it.target.health() == 0 }.map { Death(it.target) }

    private val matchEnd = PublishSubject.create<MatchEnd>()
    private val activeCreature = PublishSubject.create<ActiveCreature>()

    val events = MatchEvents(
            actionsExecuted,
            rests,
            damage,
            kills,
            deaths,
            matchEnd,
            activeCreature
    )

    var activeSpawnedCreature: SpawnedCreature? = null
    var winner: Int? = null

    val spawnedCreatures = spawnCreatures(playerCreatures, opponentCreatures)

    private fun spawnCreatures(playerCreatures: Map<Position, CreatureCard>,
                               opponentCreatures: Map<Position, CreatureCard>): List<SpawnedCreature> {
        val creatures = ArrayList<SpawnedCreature>()
        var id = 0
        playerCreatures.forEach {
            val behaviour = getBehaviour(it.value.code)
            val stats = getStats(it.value.code)
            val spawned = SpawnedCreature(id, it.key, 1, stats, behaviour, it.value)
            behaviour.attachTo(spawned, events)
            creatures.add(spawned)
            id++
        }

        opponentCreatures.forEach {
            val behaviour = getBehaviour(it.value.code)
            val stats = getStats(it.value.code)
            val spawned = SpawnedCreature(id, it.key, 2, stats, behaviour, it.value)
            behaviour.attachTo(spawned, events)
            creatures.add(spawned)
            id++
        }

        return creatures
    }

    fun start() {
        activeSpawnedCreature = FirstTurn().execute(spawnedCreatures)

        messageActiveCreature()
        playIaTurn()
    }

    private fun playIaTurn() {
        activeSpawnedCreature?.let {
            if (it.team == 2) {
                val action = Attack()
                val possibleTargets = spawnedCreatures.filter { it.team == 1 }
                var objective = Random.nextInt(0, possibleTargets.size)
                while (!validate(action, objective)) {
                    objective = Random.nextInt(0, possibleTargets.size)
                }

                actionExecution(action, objective)
            }
        }
    }

    fun validate(action: Action, objectiveId: Int): Boolean {
        if (action.melee) {
            if (HasBlockers().execute(spawnedCreatures[objectiveId], spawnedCreatures)) return false
        }
        return true
    }

    //refactorizame pls :c
    fun actionExecution(action: Action, objectiveId: Int) {
        activeSpawnedCreature?.let {
            creatureAction.execute(it, action, spawnedCreatures[objectiveId])
            if (thereIsNoWinner()) {
                activeSpawnedCreature = nextTurn.execute(spawnedCreatures, it.team)
                while (activeSpawnedCreature == null) {
                    restingTurn.execute(spawnedCreatures)
                    activeSpawnedCreature = nextTurn.execute(spawnedCreatures, it.team)
                }

                messageActiveCreature()
                playIaTurn()
            } else {
                messageWinningTeam()
            }
        }
    }

    private fun messageWinningTeam() {
        matchEnd.onNext(MatchEnd(winner!!))
    }


    private fun messageActiveCreature() {
        activeSpawnedCreature?.let { activeCreature.onNext(ActiveCreature(it.id, it.team)) }
    }

    private fun thereIsNoWinner(): Boolean {
        winner = winnerValidation.execute(spawnedCreatures) ?: return true

        activeSpawnedCreature = null
        return false
    }


    private fun getBehaviour(creature: CreatureCode): CreatureBehaviour {
        return when (creature) {
            EYE -> EyeBehaviour()
            SKELETON -> SkeletonBehaviour()
            FLUFFY -> FluffyBehaviour()
            FROG -> FrogBehaviour()
            GOLEM -> GolemBehaviour()
        }
    }

    private fun getStats(creature: CreatureCode): CreatureStats {
        return when (creature) {
            EYE -> EyeStats()
            SKELETON -> SkeletonStats()
            FLUFFY -> FluffyStats()
            FROG -> FrogStats()
            GOLEM -> GolemStats()
        }
    }

}


//hecho solo para poder mockear. -_-
class MatchEvents(override val actions: Observable<ActionExecution>,
                  override val rest: Observable<Rest>,
                  override val damageEvent: Observable<DamageEvent>,
                  override val kills: Observable<Kill>,
                  override val deaths: Observable<Death>,
                  override val matchEnd: Observable<MatchEnd>,
                  override val activeCreature: Observable<ActiveCreature>) : Events

