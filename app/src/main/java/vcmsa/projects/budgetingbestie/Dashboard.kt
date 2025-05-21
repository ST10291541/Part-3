package vcmsa.projects.budgetingbestie


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Dashboard : AppCompatActivity() {

    private var userId: String? = null // Change to String for Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Get user ID from Firebase Authentication
        val currentUser  = FirebaseAuth.getInstance().currentUser
        userId = currentUser ?.uid

        if (userId == null) {
            Toast.makeText(this, "Unauthorized access. Please log in.", Toast.LENGTH_LONG).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }

        findViewById<Button>(R.id.btnExpenses).setOnClickListener {
            startActivity(Intent(this, ExpensesMain::class.java))
        }

        /*findViewById<Button>(R.id.btnBudgets).setOnClickListener {
            startActivity(Intent(this, BudgetMain::class.java))
        }

        findViewById<Button>(R.id.btnReports).setOnClickListener {
            startActivity(Intent(this, Reports::class.java))
        }

        findViewById<Button>(R.id.btnSavings).setOnClickListener {
            startActivity(Intent(this, SavingsMain::class.java))
        }

        findViewById<Button>(R.id.btnProfile).setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            intent.putExtra("USER_ID", userId) // Pass the user ID as a String
            startActivity(intent)
        }*/

        findViewById<Button>(R.id.btnSignOut).setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Sign out the user
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
