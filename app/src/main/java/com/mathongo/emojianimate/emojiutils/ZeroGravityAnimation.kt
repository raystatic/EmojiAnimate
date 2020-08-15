package com.mathongo.emojianimate.emojiutils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.TranslateAnimation


class ZeroGravityAnimation {

    private val RANDOM_DURATION = -1


    private var mOriginationDirection =
        Direction.RANDOM
    private var mDestinationDirection =
        Direction.RANDOM
    private var mDuration = RANDOM_DURATION
    private var mCount = 1
    private var mImageResId = 0
    private var mScalingFactor = 1f
    private var mAnimationListener: AnimationListener? = null


    /**
     * Sets the orignal direction. The animation will originate from the given direction.
     *
     */
    fun setOriginationDirection(direction: Direction): ZeroGravityAnimation? {
        mOriginationDirection = direction
        return this
    }

    /**
     * Sets the animation destination direction. The translate animation will proceed towards the given direction.
     * @param direction
     * @return
     */
    fun setDestinationDirection(direction: Direction): ZeroGravityAnimation? {
        mDestinationDirection = direction
        return this
    }

    /**
     * Will take a random time duriation for the animation
     * @return
     */
    fun setRandomDuration(): ZeroGravityAnimation? {
        return setDuration(RANDOM_DURATION)
    }

    /**
     * Sets the time duration in millseconds for animation to proceed.
     * @param duration
     * @return
     */
    fun setDuration(duration: Int): ZeroGravityAnimation? {
        mDuration = duration
        return this
    }

    /**
     * Sets the image reference id for drawing the image
     * @param resId
     * @return
     */
    fun setImage(resId: Int): ZeroGravityAnimation? {
        mImageResId = resId
        return this
    }

    /**
     * Sets the image scaling value.
     * @param scale
     * @return
     */
    fun setScalingFactor(scale: Float): ZeroGravityAnimation? {
        mScalingFactor = scale
        return this
    }

    fun setAnimationListener(listener: AnimationListener?): ZeroGravityAnimation? {
        mAnimationListener = listener
        return this
    }

    fun setCount(count: Int): ZeroGravityAnimation? {
        mCount = count
        return this
    }


    /**
     * Starts the Zero gravity animation by creating an OTT and attach it to th given ViewGroup
     * @param activity
     * @param ottParent
     */
    fun play(activity: Activity, ottParent: ViewGroup?) {
        val generator = DirectionGenerator()
        if (mCount > 0) {
            for (i in 0 until mCount) {
                val origin =
                    if (mOriginationDirection === Direction.RANDOM) generator.getRandomDirection() else mOriginationDirection
                val destination =
                    if (mDestinationDirection === Direction.RANDOM) generator.getRandomDirection(
                        origin
                    ) else mDestinationDirection
                val startingPoints =
                    generator.getPointsInDirection(activity, origin)
                val endPoints =
                    generator.getPointsInDirection(activity, destination)
                val bitmap =
                    BitmapFactory.decodeResource(activity.resources, mImageResId)
                val scaledBitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    (bitmap.width * mScalingFactor).toInt(),
                    (bitmap.height * mScalingFactor).toInt(),
                    false
                )
                when (origin) {
                    Direction.LEFT -> startingPoints!![0] -= scaledBitmap.width
                    Direction.RIGHT -> startingPoints!![0] += scaledBitmap.width
                    Direction.TOP -> startingPoints!![1] -= scaledBitmap.height
                    Direction.BOTTOM -> startingPoints!![1] += scaledBitmap.height
                }
                when (destination) {
                    Direction.LEFT -> endPoints!![0] -= scaledBitmap.width
                    Direction.RIGHT -> endPoints!![0] += scaledBitmap.width
                    Direction.TOP -> endPoints!![1] -= scaledBitmap.height
                    Direction.BOTTOM -> endPoints!![1] += scaledBitmap.height
                }
                val layer = OverTheTopLayer()
                val ottLayout = layer.with(activity)
                    ?.scale(mScalingFactor)
                    ?.attachTo(ottParent)
                    ?.setBitmap(scaledBitmap, startingPoints)
                    ?.create()
                when (origin) {
                    Direction.LEFT -> {
                    }
                }
                val deltaX = endPoints!![0] - startingPoints!![0]
                val deltaY = endPoints[1] - startingPoints[1]
                var duration = mDuration
                if (duration == RANDOM_DURATION) {
                    duration = RandomUtil.generateRandomBetween(3500, 12500)
                }
                val animation = TranslateAnimation(
                    0F,
                    deltaX.toFloat(), 0F,
                    deltaY.toFloat()
                )
                animation.duration = duration.toLong()
                animation.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        if (i == 0) {
                            if (mAnimationListener != null) {
                                mAnimationListener!!.onAnimationStart(animation)
                            }
                        }
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        layer.destroy()
                        if (i == mCount - 1) {
                            if (mAnimationListener != null) {
                                mAnimationListener!!.onAnimationEnd(animation)
                            }
                        }
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                layer.applyAnimation(animation)
            }
        } else {
            Log.e(
                ZeroGravityAnimation::class.java.simpleName,
                "Count was not provided, animation was not started"
            )
        }
    }

    /**
     * Takes the content view as view parent for laying the animation objects and starts the animation.
     * @param activity - activity on which the zero gravity animation should take place.
     */
    fun play(activity: Activity) {
        play(activity, null)
    }

}