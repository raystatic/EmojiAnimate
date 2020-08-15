package com.mathongo.emojianimate.emojiutils

import android.app.Activity
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import com.mathongo.emojianimate.R
import java.lang.ref.WeakReference


class OverTheTopLayer {

    class OverTheTopLayerException(msg: String?) :
        RuntimeException(msg)

    private var mWeakActivity: WeakReference<Activity?>? = null
    private var mWeakRootView: WeakReference<ViewGroup?>? = null
    private var mCreatedOttLayer: FrameLayout? = null
    private var mScalingFactor = 1.0f
    private var mDrawLocation: IntArray? = intArrayOf(0, 0)
    private var mBitmap: Bitmap? = null


    fun OverTheTopLayer() {}

    /**
     * To create a layer on the top of activity
     *
     */
    fun with(weakReferenceActivity: Activity?): OverTheTopLayer? {
        mWeakActivity = WeakReference<Activity?>(weakReferenceActivity)
        return this
    }

    /**
     * Draws the image as per the drawable resource id on the given location pixels.
     *
     */
    fun generateBitmap(
        resources: Resources?,
        drawableResId: Int,
        mScalingFactor: Float,
        location: IntArray?
    ): OverTheTopLayer? {
        var location = location
        if (location == null) {
            location = intArrayOf(0, 0)
        } else if (location.size != 2) {
            throw OverTheTopLayerException("Requires location as an array of length 2 - [x,y]")
        }
        val bitmap = BitmapFactory.decodeResource(resources, drawableResId)
        val scaledBitmap = Bitmap.createScaledBitmap(
            bitmap!!,
            (bitmap.width * mScalingFactor).toInt(),
            (bitmap.height * mScalingFactor).toInt(),
            false
        )
        mBitmap = scaledBitmap
        mDrawLocation = location
        return this
    }

    fun setBitmap(bitmap: Bitmap?, location: IntArray?): OverTheTopLayer? {
        var location = location
        if (location == null) {
            location = intArrayOf(0, 0)
        } else if (location.size != 2) {
            throw OverTheTopLayerException("Requires location as an array of length 2 - [x,y]")
        }
        mBitmap = bitmap
        mDrawLocation = location
        return this
    }


    /**
     * Holds the scaling factor for the image.
     *
     * @param scale
     * @return
     */
    fun scale(scale: Float): OverTheTopLayer? {
        if (scale <= 0) {
            throw OverTheTopLayerException("Scaling should be > 0")
        }
        mScalingFactor = scale
        return this
    }

    /**
     * Attach the OTT layer as the child of the given root view.
     * @return
     */
    fun attachTo(rootView: ViewGroup?): OverTheTopLayer? {
        mWeakRootView = WeakReference<ViewGroup?>(rootView)
        return this
    }

    /**
     * Creates an OTT.
     * @return
     */
    fun create(): FrameLayout? {
        if (mCreatedOttLayer != null) {
            destroy()
        }
        if (mWeakActivity == null) {
            throw OverTheTopLayerException("Could not create the layer as not activity reference was provided.")
        }
        val activity: Activity? = mWeakActivity!!.get()
        if (activity != null) {
            var attachingView: ViewGroup? = null
            attachingView = if (mWeakRootView != null && mWeakRootView!!.get() != null) {
                mWeakRootView!!.get()
            } else {
                activity.findViewById<View?>(R.id.content) as ViewGroup?
            }
            val imageView = ImageView(activity)
            imageView.setImageBitmap(mBitmap)
            val minWidth = mBitmap!!.width
            val minHeight = mBitmap!!.height
            imageView.measure(
                View.MeasureSpec.makeMeasureSpec(minWidth, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(minHeight, View.MeasureSpec.AT_MOST)
            )
            var params =
                imageView.getLayoutParams() as FrameLayout.LayoutParams
            if (params == null) {
                params = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.TOP
                )
                imageView.setLayoutParams(params)
            }
            val xPosition = mDrawLocation!![0]
            val yPosition = mDrawLocation!![1]
            params.width = minWidth
            params.height = minHeight
            params.leftMargin = xPosition
            params.topMargin = yPosition
            imageView.setLayoutParams(params)
            val ottLayer = FrameLayout(activity)
            val topLayerParam = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
                Gravity.TOP
            )
            ottLayer.layoutParams = topLayerParam
            ottLayer.addView(imageView)
            attachingView!!.addView(ottLayer)
            mCreatedOttLayer = ottLayer
        } else {
            Log.e(
                OverTheTopLayer::class.java.simpleName,
                "Could not create the layer. Reference to the activity was lost"
            )
        }
        return mCreatedOttLayer
    }

    /**
     * Kills the OTT
     */
    fun destroy() {
        if (mWeakActivity == null) {
            throw OverTheTopLayerException("Could not create the layer as not activity reference was provided.")
        }
        val activity: Activity? = mWeakActivity!!.get()
        if (activity != null) {
            var attachingView: ViewGroup? = null
            attachingView = if (mWeakRootView != null && mWeakRootView!!.get() != null) {
                mWeakRootView!!.get()
            } else {
                activity.findViewById<View?>(R.id.content) as ViewGroup?
            }
            if (mCreatedOttLayer != null) {
                attachingView!!.removeView(mCreatedOttLayer)
                mCreatedOttLayer = null
            }
        } else {
            Log.e(
                OverTheTopLayer::class.java.simpleName,
                "Could not destroy the layer as the layer was never created."
            )
        }
    }

    /**
     * Applies the animation to the image view present in OTT.
     * @param animation
     */
    fun applyAnimation(animation: Animation?) {
        if (mCreatedOttLayer != null) {
            val drawnImageView: ImageView = mCreatedOttLayer!!.getChildAt(0) as ImageView
            drawnImageView.startAnimation(animation)
        }
    }

}