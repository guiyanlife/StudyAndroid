package com.github.studyandroid.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.github.studyandroid.app.databinding.ActivityMainBinding
import com.github.studyandroid.app.ui.activity.ItemActivity

/**
 * Copyright (c) 2022 GitHub, Inc.
 * Description: Main Activity
 * Author(s): Gui Yan (guiyanlife@163.com)
 */
class MainActivity : Activity(), View.OnClickListener {
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setListener()
        init()
    }

    private fun setListener() {
        mBinding.tvItem.setOnClickListener(this)
        mBinding.incLayoutToolbarActivity.flToolbarClose.setOnClickListener(this)
    }

    private fun init() {
        mBinding.incLayoutToolbarActivity.tvToolbarTitle.text = getString(R.string.app_name)
        mBinding.incLayoutToolbarActivity.ivToolbarBack.setImageResource(R.mipmap.ic_launcher)
        mBinding.incLayoutToolbarActivity.ivToolbarClose.visibility = View.VISIBLE
    }

    override fun onClick(view: View) {
        when (view) {
            mBinding.tvItem -> {
                val intent = Intent(this, ItemActivity::class.java)
                intent.putExtra(ItemActivity.INTENT_KEY_TITLE, mBinding.tvItem.text)
                startActivity(intent)
            }
            mBinding.incLayoutToolbarActivity.flToolbarClose -> finish()
        }
    }
}