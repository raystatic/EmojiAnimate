package com.mathongo.emojianimate

import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import androidx.appcompat.app.AppCompatActivity
import com.mathongo.emojianimate.utils.Direction
import com.mathongo.emojianimate.utils.Utility
import com.mathongo.emojianimate.utils.ZeroGravityAnimation
import kotlinx.android.synthetic.main.activity_main.*
import java.time.Duration


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart.setOnClickListener {
            emoji_one()
            emoji_two()
            emoji_three()
      //      emoji_four()
        }

    }

    fun flyEmoji(resId: Bitmap) {
        val animation = ZeroGravityAnimation()
        animation.setCount(1)
        animation.setScalingFactor(0.2f)
        animation.setOriginationDirection(Direction.BOTTOM)
        animation.setDestinationDirection(Direction.TOP)
        animation.setImage(resId)
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
        }
        )
        val container: ViewGroup = findViewById(R.id.animation_holder)
        animation.play(this, container)
    }

    fun emoji_one() {
        // You can change the number of emojis that will be flying on screen
        for (i in 0..2) {
            //flyEmoji(Utility.drawableToBitmap(resources.getDrawable(R.drawable.ic_1f90f)))
            flyObject(Utility.drawableToBitmap(resources.getDrawable(R.drawable.ic_1f90f)),8000,Direction.BOTTOM, Direction.TOP,1F)
        }
    }
    // You can change the number of emojis that will be flying on screen

    // You can change the number of emojis that will be flying on screen
    fun emoji_two() {
        for (i in 0..2) {
            //flyEmoji(Utility.drawableToBitmap(resources.getDrawable(R.drawable.ic_1f929)))
            flyObject(Utility.drawableToBitmap(resources.getDrawable(R.drawable.ic_1f929)),16000,Direction.BOTTOM, Direction.TOP,1F)
        }
    }
    // You can change the number of emojis that will be flying on screen

    // You can change the number of emojis that will be flying on screen
    fun emoji_three() {
        for (i in 0..2) {
           // flyEmoji(Utility.drawableToBitmap(resources.getDrawable(R.drawable.ic_2764)))
            flyObject(Utility.drawableToBitmap(resources.getDrawable(R.drawable.ic_2764)),20000,Direction.BOTTOM, Direction.TOP,1F)
        }
    }

    fun emoji_four() {
        for (i in 0..2) {
//            flyEmoji(Utility.drawableToBitmap(resources.getDrawable(R.drawable.ic_e10a)))
            flyObject(Utility.drawableToBitmap(resources.getDrawable(R.drawable.ic_e10a)),15000,Direction.BOTTOM, Direction.TOP,1F)
        }
    }


    // This method will be used if You want to fly your Emois Over any view

//    public void flyObject(final int resId, final int duration, final Direction from, final Direction to, final float scale) {
//
//        ZeroGravityAnimation animation = new ZeroGravityAnimation();
//        animation.setCount(1);
//        animation.setScalingFactor(scale);
//        animation.setOriginationDirection(from);
//        animation.setDestinationDirection(to);
//        animation.setImage(resId);
//        animation.setDuration(duration);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//                flyObject(resId, duration, from, to, scale);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        ViewGroup container = (ViewGroup) findViewById(R.id.animation_bigger_objects_holder);
//        animation.play(this,container);
//
//    }

    fun flyObject(bitmap: Bitmap,duration: Int, from:Direction, to:Direction,scale:Float){
        val animation = ZeroGravityAnimation()
        animation.setCount(1)
        animation.setScalingFactor(scale)
        animation.setOriginationDirection(from)
        animation.setDestinationDirection(to)
        animation.setImage(bitmap)
        animation.setDuration(duration)
        animation.setAnimationListener(object: AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                flyObject(bitmap, duration, from, to, scale)
            }

            override fun onAnimationStart(p0: Animation?) {

            }
        })

        val  container:ViewGroup = findViewById(R.id.animation_holder);
        animation.play(this,container)
    }

}