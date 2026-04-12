package com.github.studyandroid.app.server

import android.content.Context
import android.util.Log
import fi.iki.elonen.NanoHTTPD

/**
 * 本地 HTTP 服务器（NanoHTTPD），单例模式。
 *
 * 提供两类接口：
 *  - GET /users            → 返回用户列表 JSON
 *  - GET /avatars/{id}.jpg → 返回本地头像图片（来自 assets/avatars/）
 *
 * 使用方式：
 *   NanoHttpServer.start(context)   // Application.onCreate()
 *   NanoHttpServer.stop()           // Application.onTerminate()
 */
object NanoHttpServer {
    const val PORT = 8080
    const val BASE_URL = "http://localhost:$PORT/"
    private const val TAG = "NanoHttpServer"

    // ─────────────────────────────────────────────────────────────────────
    // 静态用户数据，avatarUrl 直接由本服务器提供
    // ─────────────────────────────────────────────────────────────────────
    private val USERS_JSON = """
        [
          {"id":1,"name":"张伟","username":"zhangwei","email":"zhangwei@meituan.com","phone":"138-0010-0001","website":"zhangwei.dev","avatarUrl":"${BASE_URL}avatars/1.jpg","company":{"name":"美团科技","slogan":"让生活更美好","bs":"本地生活服务平台"}},
          {"id":2,"name":"李娜","username":"lina","email":"lina@alibaba.com","phone":"139-0020-0002","website":"lina.tech","avatarUrl":"${BASE_URL}avatars/2.jpg","company":{"name":"阿里巴巴","slogan":"让天下没有难做的生意","bs":"电子商务与云计算"}},
          {"id":3,"name":"王芳","username":"wangfang","email":"wangfang@tencent.com","phone":"136-0030-0003","website":"wangfang.io","avatarUrl":"${BASE_URL}avatars/3.jpg","company":{"name":"腾讯科技","slogan":"连接一切","bs":"社交与数字内容"}},
          {"id":4,"name":"刘洋","username":"liuyang","email":"liuyang@bytedance.com","phone":"137-0040-0004","website":"liuyang.app","avatarUrl":"${BASE_URL}avatars/4.jpg","company":{"name":"字节跳动","slogan":"激发创造，丰富生活","bs":"内容与信息科技"}},
          {"id":5,"name":"陈静","username":"chenjing","email":"chenjing@jd.com","phone":"135-0050-0005","website":"chenjing.shop","avatarUrl":"${BASE_URL}avatars/5.jpg","company":{"name":"京东集团","slogan":"多快好省","bs":"供应链与零售科技"}},
          {"id":6,"name":"杨磊","username":"yanglei","email":"yanglei@xiaomi.com","phone":"186-0060-0006","website":"yanglei.mi","avatarUrl":"${BASE_URL}avatars/6.jpg","company":{"name":"小米科技","slogan":"为发烧而生","bs":"智能硬件与IoT"}},
          {"id":7,"name":"赵丽","username":"zhaoli","email":"zhaoli@huawei.com","phone":"188-0070-0007","website":"zhaoli.hw","avatarUrl":"${BASE_URL}avatars/7.jpg","company":{"name":"华为技术","slogan":"把数字世界带入每个人","bs":"ICT基础设施与终端"}},
          {"id":8,"name":"孙强","username":"sunqiang","email":"sunqiang@baidu.com","phone":"158-0080-0008","website":"sunqiang.ai","avatarUrl":"${BASE_URL}avatars/8.jpg","company":{"name":"百度公司","slogan":"简单可依赖","bs":"AI与搜索引擎"}},
          {"id":9,"name":"周敏","username":"zhoumin","email":"zhoumin@netease.com","phone":"150-0090-0009","website":"zhoumin.163","avatarUrl":"${BASE_URL}avatars/9.jpg","company":{"name":"网易公司","slogan":"网聚人的力量","bs":"游戏与互联网服务"}},
          {"id":10,"name":"吴浩","username":"wuhao","email":"wuhao@didi.com","phone":"152-0100-0010","website":"wuhao.ride","avatarUrl":"${BASE_URL}avatars/10.jpg","company":{"name":"滴滴出行","slogan":"让出行更美好","bs":"共享出行与智慧交通"}}
        ]
        """.trimIndent()

    private var mNanoHTTPD: NanoHTTPD? = null

    /**
     * 启动服务器（幂等：已运行则直接返回）。
     * @param context Application Context，用于读取 assets 资源，不持有引用。
     */
    fun start(context: Context) {
        if (mNanoHTTPD?.isAlive == true) {
            Log.d(TAG, "Already running on $BASE_URL")
            return
        }
        // 只持有 ApplicationContext，不持有 Activity/Fragment 引用
        val appContext = context.applicationContext
        mNanoHTTPD = object : NanoHTTPD(PORT) {
            override fun serve(session: IHTTPSession): Response {
                Log.d(TAG, "← ${session.method} ${session.uri}")
                return when {
                    // GET /users
                    session.uri == "/users" && session.method == Method.GET ->
                        newFixedLengthResponse(Response.Status.OK, "application/json", USERS_JSON)

                    // GET /avatars/{id}.jpg  (id: 1-10)
                    session.uri.matches(Regex("/avatars/\\d+\\.jpg")) && session.method == Method.GET -> {
                        val id = session.uri.removePrefix("/avatars/").removeSuffix(".jpg").toIntOrNull()
                        serveAvatar(id)
                    }

                    else -> newFixedLengthResponse(
                        Response.Status.NOT_FOUND,
                        MIME_PLAINTEXT,
                        "Not Found"
                    )
                }
            }

            /** 从 assets/avatars/avatar_{id}.jpg 读取并返回图片流，用 .use 确保流关闭 */
            private fun serveAvatar(id: Int?): Response {
                if (id == null || id < 1 || id > 10) {
                    return newFixedLengthResponse(
                        Response.Status.NOT_FOUND,
                        MIME_PLAINTEXT,
                        "Avatar not found"
                    )
                }
                return try {
                    val bytes = appContext.assets.open("avatars/avatar_$id.jpg").use { it.readBytes() }
                    newFixedLengthResponse(Response.Status.OK, "image/jpeg", bytes.inputStream(), bytes.size.toLong())
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to serve avatar $id", e)
                    newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Error")
                }
            }
        }.apply {
            start()
            Log.i(TAG, "Started → $BASE_URL")
        }
    }

    fun stop() {
        mNanoHTTPD?.stop()
        mNanoHTTPD = null
        Log.i(TAG, "Stopped")
    }
}
