package com.arshramgarhia.otest.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.arsh.testo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val navController = findNavController(R.id.nav_host_fragment_container)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.miHome, R.id.miTests, R.id.miScores, R.id.miSettings,R.id.miSignOut), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)


        auth = Firebase.auth


        val uri: Uri? = intent?.data
        if (uri!=null) {
            val list = uri.pathSegments as List<String>
            val id = list[list.size - 1]
            val bundle = bundleOf("id" to id)
            val navGraph = findNavController(R.id.nav_host_fragment_container)
            navGraph.navigate(R.id.action_homeFragment_to_takeTestFragment, bundle)
            Log.i("uriId", id)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_container).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}