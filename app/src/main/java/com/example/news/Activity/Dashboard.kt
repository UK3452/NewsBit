package com.example.news.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.example.news.Fragment.Screen2_Fragment
import com.example.news.Interface.Communicator
import com.example.news.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.header_navigation_drawer.view.*


const val BASE_URL = "https://newsapi.org/v2/"
private lateinit var auth: FirebaseAuth

class Dashboard : AppCompatActivity(), Communicator {
    private var isCatVisible: Boolean = false

    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val drawerToggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navigationView.bringToFront()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        auth = FirebaseAuth.getInstance()
        val mUser: FirebaseUser = auth.getCurrentUser()
        home_filled.isEnabled = false
        val fragment1 = Screen2_Fragment()

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment1)
            .commit() // news fragment

        supportFragmentManager.commit {
            hide(fragment_one) //category fragment
        }


        // Home page ClickListener
        home_filled.setOnClickListener {
            goToHomepage()
        }

        userMenu.setOnClickListener {
            if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
                drawer_layout.closeDrawer(Gravity.RIGHT)
            } else {
                drawer_layout.openDrawer(Gravity.RIGHT)
            }
        }

        //TO DO.........................................................................................
        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigationView.menu!!.findItem(R.id.trendingStories)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                if (menuItem != null && !menuItem.isChecked) {
                    goToHomepage()
                }
                true
            }
        bottomNavigationView.menu!!.findItem(R.id.categories)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                if (menuItem != null) {
                    showCategoryFragment()
                }
                true
            }
        bottomNavigationView.menu!!.findItem(R.id.more)
            .setOnMenuItemClickListener {
                var menuItemView: View = findViewById(R.id.more)
                val popupMenu: PopupMenu = PopupMenu(this, menuItemView)
                popupMenu.menuInflater.inflate(R.menu.more_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
                    when (item!!.itemId) {
                        R.id.Articles ->
                            Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
                                .show()
                        R.id.Trends -> {
                            Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
                                .show()
                        }
                        R.id.explore -> {

                        }
                    }
                    true
                })
                popupMenu.show()

                true
            }
        val navigationView: NavigationView = findViewById(R.id.navigationView) as NavigationView

        val headerView: View = navigationView.getHeaderView(0)

        if (mUser.email != null) {
            headerView.userEmailTxt.text = mUser.email
        } else {
            headerView.userEmailTxt.text = "newsbit@gmail.com"
        }
        if (mUser.displayName != null) {
            headerView.userNameTxt.text = mUser.displayName
        } else {
            headerView.userNameTxt.text = "NEWSBIT"
        }
        if (mUser.photoUrl != null) {
            Glide.with(baseContext)
                .load(mUser.photoUrl)
                .into(headerView.user_img)
        } else {
            Glide.with(baseContext)
                .load(R.drawable.nlogo)
                .into(headerView.user_img)
        }

        navigationView.menu!!.findItem(R.id.nav_home)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                Toast.makeText(applicationContext, "HOME", Toast.LENGTH_SHORT).show()
                true
            }
        navigationView.menu!!.findItem(R.id.LogOut)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                logoutUser()
                Toast.makeText(applicationContext, "LOGOUT!!", Toast.LENGTH_SHORT).show()
                true
            }
        navigationView.menu!!.findItem(R.id.nav_tools)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                Toast.makeText(applicationContext, "Tools", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, ChannelNewsActivity::class.java)
                startActivity(intent)
                true
            }
    }
    private fun goToHomepage() {
        passDataCom("Top Stories")
    }

    @SuppressLint("ResourceAsColor")
    override fun passDataCom(newsString: String) {
        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.bottom_navigation) as BottomNavigationView
        val bundle = Bundle()
        bundle.putString("message", newsString)

        if (!newsString.equals("Top Stories")) {
            home_filled.isEnabled = true
            home_filled.setImageResource(R.drawable.ic_baseline_home_24)
            bottom_navigation.menu!!.findItem(R.id.categories).title=newsString
            bottomNavigationView.menu!!.findItem(R.id.categories).setChecked(true)
        } else {
            home_filled.isEnabled = false
            home_filled.setImageResource(R.drawable.ic_outline_home_24)
            bottom_navigation.menu!!.findItem(R.id.categories).title="Category"
            bottomNavigationView.menu!!.findItem(R.id.trendingStories).setChecked(true)
        }
        Toast.makeText(applicationContext, newsString, Toast.LENGTH_LONG)
        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = Screen2_Fragment()
        frag2.arguments = bundle
        transaction.replace(R.id.fragment_container, frag2)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
        hideCategoryFragment()
    }

    override fun hideCategoryFragment() {
        supportFragmentManager.commit {
            isCatVisible = false
            hide(fragment_one)
        }
    }

    fun showCategoryFragment() {
        supportFragmentManager.commit {
            isCatVisible = true
            show(fragment_one)
        }
    }

    fun logoutUser() {
        auth.signOut()
        var intent = Intent(applicationContext, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}