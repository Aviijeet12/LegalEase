package cm.avisingh.legalease.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import cm.avisingh.legalease.R
import cm.avisingh.legalease.models.User
import cm.avisingh.legalease.utils.AnalyticsHelper
import cm.avisingh.legalease.activities.MainActivity


class AuthActivity : AppCompatActivity() {
    private lateinit var analyticsHelper: AnalyticsHelper
    private lateinit var tabLogin: TextView
    private lateinit var tabSignUp: TextView
    private lateinit var layoutLogin: LinearLayout
    private lateinit var layoutSignUp: LinearLayout
    private lateinit var spinnerAccountType: Spinner
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etConfirmPassword: EditText
    // Sign up specific fields
    private lateinit var etEmailSignUp: EditText
    private lateinit var etPasswordSignUp: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnCreateAccount: Button
    private lateinit var layoutDemoAccess: LinearLayout

    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        analyticsHelper.trackScreen(this::class.java.simpleName)
        //analyticsHelper = AnalyticsHelper(SharedPrefManager(this))

        initializeViews()
        setupClickListeners()
        setupAccountTypeSpinner()
        showLoginMode()
    }

    private fun initializeViews() {
        tabLogin = findViewById(R.id.tabLogin)
        tabSignUp = findViewById(R.id.tabSignUp)
        layoutLogin = findViewById(R.id.layoutLogin)
        layoutSignUp = findViewById(R.id.layoutSignUp)
        spinnerAccountType = findViewById(R.id.spinnerAccountType)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
    etEmailSignUp = findViewById(R.id.etEmailSignUp)
    etPasswordSignUp = findViewById(R.id.etPasswordSignUp)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnCreateAccount = findViewById(R.id.btnCreateAccount)
        layoutDemoAccess = findViewById(R.id.layoutDemoAccess)
    }

    private fun setupClickListeners() {
        tabLogin.setOnClickListener {
            showLoginMode()
        }

        tabSignUp.setOnClickListener {
            showSignUpMode()
        }

        btnLogin.setOnClickListener {
            handleLogin()
        }

        btnCreateAccount.setOnClickListener {
            handleSignUp()
        }

        // Demo access buttons
        findViewById<TextView>(R.id.btnUserDemo).setOnClickListener {
            loginWithDemo(User("1", "Demo User", "user@demo.com", "user"))
        }

        findViewById<TextView>(R.id.btnLawyerDemo).setOnClickListener {
            loginWithDemo(User("2", "Demo Lawyer", "lawyer@demo.com", "lawyer"))
        }

        findViewById<TextView>(R.id.btnAdminDemo).setOnClickListener {
            loginWithDemo(User("3", "Demo Admin", "admin@demo.com", "admin"))
        }
    }

    private fun setupAccountTypeSpinner() {
        val accountTypes = arrayOf("Client", "Lawyer")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, accountTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAccountType.adapter = adapter
    }

    private fun showLoginMode() {
        isLoginMode = true
        tabLogin.setBackgroundResource(R.drawable.bg_tab_selected)
        tabSignUp.setBackgroundResource(R.drawable.bg_tab_unselected)
        tabLogin.setTextColor(resources.getColor(android.R.color.white))
        tabSignUp.setTextColor(resources.getColor(R.color.legal_blue))

        layoutLogin.visibility = android.view.View.VISIBLE
        layoutSignUp.visibility = android.view.View.GONE
        layoutDemoAccess.visibility = android.view.View.VISIBLE
    }

    private fun showSignUpMode() {
        isLoginMode = false
        tabLogin.setBackgroundResource(R.drawable.bg_tab_unselected)
        tabSignUp.setBackgroundResource(R.drawable.bg_tab_selected)
        tabLogin.setTextColor(resources.getColor(R.color.legal_blue))
        tabSignUp.setTextColor(resources.getColor(android.R.color.white))

        layoutLogin.visibility = android.view.View.GONE
        layoutSignUp.visibility = android.view.View.VISIBLE
        layoutDemoAccess.visibility = android.view.View.GONE
    }

    private fun handleLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val accountType = spinnerAccountType.selectedItem as String

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Demo login - in real app, this would call your backend
        val user = User(
            id = System.currentTimeMillis().toString(),
            name = "Demo $accountType",
            email = email,
            accountType = accountType.toLowerCase()
        )

        loginWithDemo(user)
    }

    private fun handleSignUp() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val email = etEmailSignUp.text.toString().trim()
        val password = etPasswordSignUp.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
            return
        }

        // Demo signup
        val user = User(
            id = System.currentTimeMillis().toString(),
            name = "$firstName $lastName",
            email = email,
            accountType = "client"
        )

        loginWithDemo(user)
    }

    private fun loginWithDemo(user: User) {
        // Save user data to shared preferences
        val sharedPref = getSharedPreferences("legalease_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_id", user.id)
            putString("user_name", user.name)
            putString("user_email", user.email)
            putString("user_type", user.accountType)
            putBoolean("is_logged_in", true)
            apply()
        }

        Toast.makeText(this, "Welcome ${user.name}!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, cm.avisingh.legalease.activities.MainActivity::class.java))
        finish()
    }
}