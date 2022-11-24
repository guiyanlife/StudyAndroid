package com.github.studyandroid.app.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.github.studyandroid.app.databinding.ActivityItemBinding

/**
 * Copyright (c) 2022 GitHub, Inc.
 * Description: Item Activity
 * Author(s): Gui Yan (guiyanlife@163.com)
 */
class ItemActivity : Activity(), View.OnClickListener {
    companion object {
        const val INTENT_KEY_TITLE: String = "title"
    }

    private lateinit var mBinding: ActivityItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setListener()
        init()
    }

    private fun setListener() {
        mBinding.incLayoutToolbarActivity.flToolbarBack.setOnClickListener(this)
    }

    private fun init() {
        mBinding.incLayoutToolbarActivity.tvToolbarTitle.text = intent.getStringExtra(INTENT_KEY_TITLE)
    }

    override fun onClick(view: View) {
        when (view) {
            mBinding.incLayoutToolbarActivity.flToolbarBack -> finish()
        }
    }
}