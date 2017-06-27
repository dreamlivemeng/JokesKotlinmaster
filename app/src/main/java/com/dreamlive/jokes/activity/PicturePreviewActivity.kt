package com.dreamlive.jokes.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dreamlive.jokes.R
import com.dreamlive.jokes.utils.FileUtils
import uk.co.senab.photoview.PhotoView

/**
 * 图片预览的Activity
 * @author 2017/6/23 17:41 / mengwei
 */
class PicturePreviewActivity : AppCompatActivity() {

    lateinit var photoView: PhotoView
    lateinit var toolbar: Toolbar
    lateinit var url: String
    var drawable: Drawable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_preview)
        photoView = findViewById(R.id.preview_photo_view) as PhotoView
        toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.welfare)
        setSupportActionBar(toolbar)
        val actionBar: ActionBar = supportActionBar as ActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val intent: Intent = intent
        if (intent != null) {
            url = intent.getStringExtra("url")
            Glide.with(this).load(url).into(photoView)
            Glide.with(this@PicturePreviewActivity).load(url).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    Log.e("TAG", "onLoadFailed1=======================")
                    return false
                }

                override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    Log.e("TAG", "onResourceReady1=======================")
                    drawable = resource
                    return false
                }
            }).into(photoView)

        }
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_picture, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_save_picture -> {
                //保存图片
                if (drawable != null) {
                    val bitmap = FileUtils.drawableToBitmap(drawable!!)
                    val path: String = externalCacheDir.path + "/log/" + System.currentTimeMillis() + ".jpg"
                    val isSuccess = ImageUtils.save(bitmap, path, Bitmap.CompressFormat.JPEG)
                    if (isSuccess) {
                        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    Toast.makeText(this, "正在加载图片，请稍后重试", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


}