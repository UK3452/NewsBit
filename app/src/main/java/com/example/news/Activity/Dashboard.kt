package com.example.news.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
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
import com.example.news.Interface.Communicator
import com.example.news.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_dashboard2.*
import kotlinx.android.synthetic.main.header_navigation_drawer.view.*


const val BASE_URL = "https://newsapi.org/v2/"

//private val placeHolderImage = "@drawable/nlogo"
private lateinit var auth: FirebaseAuth
private val placeHolderImage =
    "https://blog.rahulbhutani.com/wp-content/uploads/2020/05/Screenshot-2018-12-16-at-21.06.29.png"

class Dashboard : AppCompatActivity(), Communicator {


    private var isCatVisible: Boolean = false

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard2)
        val drawerToggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navigationView.bringToFront()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        headlinesBtn.setTextSize(16f)
        auth = FirebaseAuth.getInstance()
        val mUser: FirebaseUser = auth.getCurrentUser()
        home_filled.isEnabled = false
        val fragment1 = Screen2()

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment1)
            .commit()

        supportFragmentManager.commit {
            hide(fragment_one)
        }

        headlinesBtn.setOnClickListener {
            goToHomepage()
        }

        categoryButton.setOnClickListener {
            showCategoryFragment()
        }

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
        more_Btn.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, more_Btn)
            popupMenu.menuInflater.inflate(R.menu.more_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.Articles ->
                        Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
                            .show()
                    R.id.Trends -> {
                        Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
                            .show()
                    }
                    R.id.Explore -> {
                        var intent = Intent(applicationContext, ChannelNewsActivity::class.java)
                        startActivity(intent)
                    }
                }
                true
            })
            popupMenu.show()
        }

        val navigationView: NavigationView = findViewById(R.id.navigationView) as NavigationView

        val headerView: View = navigationView.getHeaderView(0)

        if (mUser.email != null) {
            headerView.userEmailTxt.text = mUser.email
        }
        else{
            headerView.userEmailTxt.text = "newsbit@gmail.com"
        }
        if(mUser.displayName != null){
            headerView.userNameTxt.text = mUser.displayName
        }
        else{
            headerView.userNameTxt.text = "NEWSBIT"
        }
        if(mUser.photoUrl != null){
            Glide.with(baseContext)
                .load(mUser.photoUrl)
                .into(headerView.user_img)
        }
        else
        {
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
                true
            }

    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.nav_home -> {
//                drawer_layout.openDrawer(GravityCompat.START)
//                Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
//                    .show()
//                true
//            }
//            R.id.LogOut ->{
//                Toast.makeText(this, "LOG OUT!!", Toast.LENGTH_SHORT)
//                    .show()
//                logoutUser()
//                true
//            }
//            else -> false
//        }
//    }

    private fun goToHomepage() {
        passDataCom("NEWSBIT")
        categoryButton.text = "Category"
        headlinesBtn.setTextColor(Color.parseColor("#FFFFFFFF"))
        categoryButton.setTextColor(Color.parseColor("#FFE927"))
        headlinesBtn.setTextSize(16f)
        categoryButton.setTextSize(14f)
    }

    @SuppressLint("ResourceAsColor")
    override fun passDataCom(newsString: String) {
        if (!newsString.equals(categoryButton.text.toString())) {
            val bundle = Bundle()
            bundle.putString("message", newsString)
            selectNavItem(newsString)
            if (!newsString.equals("NEWSBIT")) {
                home_filled.isEnabled = true
                headlinesBtn.isEnabled = true
                home_filled.setImageResource(R.drawable.ic_baseline_home_24)
            } else {
                home_filled.isEnabled = false
                headlinesBtn.isEnabled = false
                home_filled.setImageResource(R.drawable.ic_outline_home_24)
            }
            Toast.makeText(applicationContext, newsString, Toast.LENGTH_LONG)
            val transaction = this.supportFragmentManager.beginTransaction()
            val frag2 = Screen2()
            frag2.arguments = bundle
            transaction.replace(R.id.fragment_container, frag2)
            transaction.addToBackStack(null)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commit()
        }
        hideCategoryFragment()
    }

    private fun selectNavItem(newsString: String) {
        if (!newsString.equals("NEWSBIT")) {
            categoryButton.setTextSize(16f)
            headlinesBtn.setTextSize(14f)
            categoryButton.text = newsString
            categoryButton.setTextColor(Color.parseColor("#FFFFFFFF"))
            headlinesBtn.setTextColor(Color.parseColor("#FFE927"))
        }
    }

    override fun hideCategoryFragment() {
        supportFragmentManager.commit {
            isCatVisible = false
//            categoriesBtn.isEnabled = true
            hide(fragment_one)
        }
    }

    fun showCategoryFragment() {
        supportFragmentManager.commit {
            isCatVisible = true
//            categoriesBtn.isEnabled = false
            show(fragment_one)
        }
    }

    fun logoutUser() {
        auth.signOut()
        var intent = Intent(applicationContext, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

//    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
//        when (menuItem.itemId) {
//            R.id.nav_home -> {
//                Toast.makeText(this, "HOME", Toast.LENGTH_SHORT).show()
//            }
//            R.id.LogOut -> {
//                Log.i("INFO","LOGOUTTTTTTTTTT")
//            }
//        }
//        drawer_layout.closeDrawer(Gravity.RIGHT)
//        return true
//    }
}