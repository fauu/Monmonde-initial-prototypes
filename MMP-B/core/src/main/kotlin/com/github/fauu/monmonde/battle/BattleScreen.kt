package com.github.fauu.monmonde.battle

import com.artemis.World
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.fauu.monmonde.shared.Assets
import com.github.fauu.monmonde.battle.hud.BattleHud
import com.github.fauu.monmonde.battle.types.BattleSpot
import com.github.fauu.monmonde.DevConsole
import com.github.fauu.monmonde.shared.Message
import com.github.fauu.monmonde.battle.components.*
import com.github.fauu.monmonde.shared.components.*
import com.github.fauu.monmonde.shared.types.MonMove

class BattleScreen(private val stage: Stage,
                   private val camera: OrthographicCamera): ScreenAdapter() {
  private val messenger = MessageManager.getInstance()

  private val battleHUD = BattleHud()

  lateinit var world: World

  fun init() {
    stage.addActor(battleHUD)

    val atlas = Assets.TEXTURE_ATLAS

    camera.zoom = 1f
    camera.setToOrtho(false, 928f, 800f)
    camera.update()

    val bgSprite = atlas.createSprite("battle/bgs/meadow")
    var cHasSprite = HasSprite()
    cHasSprite.sprite = bgSprite
    world.createEntity()
        .edit()
        .add(cHasSprite)

    val trainerMonSprite = atlas.createSprite("battle/sprites/back/1", 0)
    with (trainerMonSprite) {
      setPosition(160f, 80f)
      setOrigin(width / 2, 0f)
      scale(.001f)
    }
    var battleSpot = BattleSpot.FOREGROUND
    var name = "Bulbasaur"
    var extraInfo = Pair(battleSpot, name)
    messenger.dispatchMessage(Message.MON_ENTERED_BATTLE, extraInfo)
    var cIsAtBattleSpot = IsAtBattleSpot()
    cIsAtBattleSpot.spot = battleSpot
    var cIsColorized = HasColorization()
    cIsColorized.colorization = Color.WHITE.cpy()
    var cHasFatigue = HasFatigue()
    cHasFatigue.maxReserve = 30
    var cHasMoves = HasMoves()
    cHasMoves.moves.addAll(listOf(MonMove.TACKLE, MonMove.VINE_WHIP))
    messenger.dispatchMessage(Message.PLAYER_AVAILABLE_MOVES_CHANGED, cHasMoves.moves)
    var cHasName = HasName()
    cHasName.name = name
    var cHasSpriteFrameAnimation = HasSpriteFrameAnimation()
    cHasSpriteFrameAnimation.frames = atlas.findRegions("battle/sprites/back/1").toList()
    cHasSpriteFrameAnimation.frameDuration = .05f
    cHasSprite = HasSprite()
    cHasSprite.sprite = trainerMonSprite
    cHasSprite.zIndex = 1
    world.createEntity()
        .edit()
        .add(IsPlayerControlled())
        .add(cIsAtBattleSpot)
        .add(cHasName)
        .add(cHasMoves)
        .add(cHasSprite)
        .add(cIsColorized)
        .add(cHasSpriteFrameAnimation)
        .add(HasSpriteScaleAnimation(3.7f, .1f))
        .add(cHasFatigue)
    DevConsole.register("bulba", trainerMonSprite)

    val foeMonSprite = atlas.createSprite("battle/sprites/front/16", 0)
    with (foeMonSprite) {
      setPosition(700f, 360f)
      setScale(1.3f)
    }
    battleSpot = BattleSpot.BACKGROUND
    name = "Pidgey"
    extraInfo = Pair(battleSpot, name)
    messenger.dispatchMessage(Message.MON_ENTERED_BATTLE, extraInfo)
    cIsAtBattleSpot = IsAtBattleSpot()
    cIsAtBattleSpot.spot = battleSpot
    cIsColorized = HasColorization()
    cIsColorized.colorization = Color.WHITE.cpy()
    cHasFatigue = HasFatigue()
    cHasFatigue.maxReserve = 15
    cHasMoves = HasMoves()
    cHasMoves.moves.addAll(listOf(MonMove.TACKLE, MonMove.QUICK_ATTACK, MonMove.GUST))
    cHasName = HasName()
    cHasName.name = name
    cHasSpriteFrameAnimation = HasSpriteFrameAnimation()
    cHasSpriteFrameAnimation.frames = atlas.findRegions("battle/sprites/front/16").toList()
    cHasSpriteFrameAnimation.frameDuration = .05f
    cHasSprite = HasSprite()
    cHasSprite.sprite = foeMonSprite
    cHasSprite.zIndex = 1
    world.createEntity()
        .edit()
        .add(IsAiControlled())
        .add(cIsAtBattleSpot)
        .add(cHasName)
        .add(cHasMoves)
        .add(cHasSpriteFrameAnimation)
        .add(cHasSprite)
        .add(cIsColorized)
        .add(cHasFatigue)
    DevConsole.register("pidgey", foeMonSprite)
  }
}

