package com.vaca.myapplication


import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener


class MainActivity : Activity(), View.OnClickListener, OnItemClickListener {
    private var search_btn: Button? = null
    private var wifi_lv: ListView? = null
    private var mUtils: WifiUtils? = null
    private var result: List<String>? = null
    private var progressdlg: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mUtils = WifiUtils(this)
        findViews()
        setLiteners()
    }

    private fun findViews() {
        search_btn = findViewById<View>(R.id.search_btn) as Button
        wifi_lv = findViewById<View>(R.id.wifi_lv) as ListView
    }

    private fun setLiteners() {
        search_btn!!.setOnClickListener(this)
        wifi_lv!!.setOnItemClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.getId() === R.id.search_btn) {
            showDialog()
            MyAsyncTask().execute()
        }
    }

    /**
     * init dialog and show
     */
    private fun showDialog() {
        progressdlg = ProgressDialog(this)
        progressdlg!!.setCanceledOnTouchOutside(false)
        progressdlg!!.setCancelable(false)
        progressdlg!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressdlg!!.setMessage(getString(R.string.wait_moment))
        progressdlg!!.show()
    }

    /**
     * dismiss dialog
     */
    private fun progressDismiss() {
        if (progressdlg != null) {
            progressdlg!!.dismiss()
        }
    }

    internal inner class MyAsyncTask : AsyncTask<Void?, Void?, Void?>() {


        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            progressDismiss()
            initListViewData()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            result = mUtils!!.scanWifiResult
            return null
        }
    }

    private fun initListViewData() {
        if (null != result && result!!.size > 0) {
            wifi_lv!!.setAdapter(ArrayAdapter(
                    applicationContext, R.layout.wifi_list_item,
                    R.id.ssid, result!!))
        } else {
            wifi_lv!!.emptyView = findViewById(R.layout.list_empty)
        }
    }

    override fun onItemClick(arg0: AdapterView<*>?, arg1: View, arg2: Int, arg3: Long) {
        val tv = arg1.findViewById(R.id.ssid) as TextView
        if (!TextUtils.isEmpty(tv.text.toString())) {
            val `in` = Intent(this@MainActivity, WifiConnectActivity::class.java)
            `in`.putExtra("ssid", tv.text.toString())
            startActivity(`in`)
        }
    }
}
