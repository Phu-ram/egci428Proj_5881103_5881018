package com.example.project_personaldoctor

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project_personaldoctor.Model.User
import com.example.project_personaldoctor.ui.diagnose.DiagnoseFragment
import com.example.project_personaldoctor.ui.profile.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.nav_header_home_page.*


class HomePageActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    var mAuth: FirebaseAuth? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        mAuth = FirebaseAuth.getInstance()
        var user = mAuth!!.currentUser
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val users = firebaseAuth.currentUser
            if (users == null) {
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }

        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

       val fab: FloatingActionButton = findViewById(R.id.fab)
       fab.setOnClickListener { view ->
           Snackbar.make(view, "Looking for hospitals nearby", Snackbar.LENGTH_LONG)
               .setAction("Action", null).show()
           val map = Intent(this@HomePageActivity,MapTestActivity::class.java)
           startActivity(map)
       }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.profile, R.id.diagnose
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
    private fun selectItemDrawer(menuItem: MenuItem) {
        var fragment: Fragment = when (menuItem.itemId) {
            R.id.diagnose ->DiagnoseFragment()
            R.id.profile -> ProfileFragment()
            else -> DiagnoseFragment()
        }
        var fragmentManager = supportFragmentManager
        fragmentManager
            .beginTransaction()
            .replace(R.id.diagnose, fragment)
            .commit()
        menuItem.isChecked = true
        menuItem.title
        drawer_layout.closeDrawers()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_page, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener { mAuthListener }
    }

    override fun onStop() {
        super.onStop()
        if(mAuthListener != null){ mAuth!!.removeAuthStateListener { mAuthListener }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout->{
                mAuth!!.signOut()
                Toast.makeText(this,"Sign out!",Toast.LENGTH_SHORT).show()
                val login = Intent(this@HomePageActivity,LoginActivity::class.java)
                startActivity(login)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
