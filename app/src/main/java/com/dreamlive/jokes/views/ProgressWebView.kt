package com.dreamlive.jokes.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.webkit.*
import android.widget.AbsoluteLayout
import android.widget.ProgressBar
import com.dreamlive.jokes.R


/**
 * 仿微信加载进度的WebView
 * Created by Administrator on 2016/9/23 15:41
 * mail：changliugang@sina.com
 */
class ProgressWebView : WebView {

    lateinit var progressbar: ProgressBar

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        progressbar = ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal)
        progressbar.layoutParams = AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.MATCH_PARENT,
                6, 0, 0)

        val drawable = context.resources.getDrawable(R.drawable.progress_bar_states)
        progressbar.progressDrawable = drawable
        addView(progressbar)
        // setWebViewClient(new WebViewClient(){});
        // 拦截url
        setWebViewClient(object : WebViewClient() {

            /*    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                    progressbar.visibility = View.VISIBLE
                    progressbar.alpha = 1.0f
                }*/


            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return true   // 在当前webview内部打开url
            }


        })
        setWebChromeClient(WebChromeClient())
        //是否可以缩放
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.domStorageEnabled = true
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    inner class WebChromeClient : android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            currentProgress = progressbar.progress
            if (newProgress >= 100 && !isAnimStart) {
                // 防止调用多次动画
                isAnimStart = true
                progressbar.progress = newProgress
                // 开启属性动画让进度条平滑消失
                startDismissAnimation(progressbar.progress)
            } else {
                // 开启属性动画让进度条平滑递增
                startProgressAnimation(newProgress)
            }

            //            if (newProgress == 100) {
            //                progressbar.setVisibility(GONE);
            //            } else {
            //                if (progressbar.getVisibility() == GONE)
            //                    progressbar.setVisibility(VISIBLE);
            //                progressbar.setProgress(newProgress);
            //            }
            //            super.onProgressChanged(view, newProgress);
        }

    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        val lp = progressbar.layoutParams as AbsoluteLayout.LayoutParams
        lp.x = l
        lp.y = t
        progressbar.layoutParams = lp
        super.onScrollChanged(l, t, oldl, oldt)
    }

    internal var currentProgress: Int = 0
    internal var isAnimStart: Boolean = false

    /**
     * progressBar递增动画
     */
    private fun startProgressAnimation(newProgress: Int) {
        val animator = ObjectAnimator.ofInt(progressbar, "progress", currentProgress, newProgress)
        animator.duration = 300
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

    /**
     * progressBar消失动画
     */
    private fun startDismissAnimation(progress: Int) {
        val anim = ObjectAnimator.ofFloat(progressbar, "alpha", 1.0f, 0.0f)
        anim.duration = 1500  // 动画时长
        anim.interpolator = DecelerateInterpolator()     // 减速
        // 关键, 添加动画进度监听器
        anim.addUpdateListener { valueAnimator ->
            val fraction = valueAnimator.animatedFraction      // 0.0f ~ 1.0f
            val offset = 100 - progress
            progressbar.progress = (progress + offset * fraction).toInt()
        }

        anim.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                // 动画结束
                progressbar.progress = 0
                progressbar.visibility = View.GONE
                isAnimStart = false
            }
        })
        anim.start()
    }


}
